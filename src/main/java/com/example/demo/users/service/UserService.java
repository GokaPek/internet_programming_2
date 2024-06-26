package com.example.demo.users.service;

import java.util.List;
import java.util.stream.StreamSupport;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.core.configuration.Constants;
import com.example.demo.core.error.NotFoundException;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.service.LineService;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.model.UserRole;
import com.example.demo.users.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    private final UserRepository repository;
    private final LineService lineService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            LineService lineService,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.lineService = lineService;
        this.passwordEncoder = passwordEncoder;
    }

    private void checkLogin(Long id, String login) {
        final Optional<UserEntity> existsUser = repository.findByLoginIgnoreCase(login);
        if (existsUser.isPresent() && !existsUser.get().getId().equals(id)) {
            throw new IllegalArgumentException(
                    String.format("User with login %s is already exists", login));
        }
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public UserEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(UserEntity.class, id));
    }

    // @Transactional
    // public UserEntity create(UserEntity entity) {
    //     if (entity == null) {
    //         throw new IllegalArgumentException("Entity is null");
    //     }
    //     checkLogin(entity.getLogin());
    //     repository.save(entity);

    //     // возможно понадобится аналогия с добавлением lines книг
    //     /*
    //      * subscriptionService.getAll().forEach(subscription -> {
    //      * final UserSubscriptionEntity userSubscription = new
    //      * UserSubscriptionEntity(entity, subscription, true);
    //      * userSubscription.setUser(entity);
    //      * userSubscription.setSubscription(subscription);
    //      * });
    //      */
    //     return repository.save(entity);
    // }

    @Transactional
    public UserEntity create(UserEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        checkLogin(null, entity.getLogin());
        final String password = Optional.ofNullable(entity.getPassword()).orElse("");
        entity.setPassword(
                passwordEncoder.encode(
                        StringUtils.hasText(password.strip()) ? password : Constants.DEFAULT_PASSWORD));
        entity.setRole(Optional.ofNullable(entity.getRole()).orElse(UserRole.USER));
        repository.save(entity);
        return repository.save(entity);
    }

    @Transactional
    public UserEntity update(long id, UserEntity entity) {
        final UserEntity existsEntity = get(id);
        checkLogin(id, entity.getLogin());
        existsEntity.setLogin(entity.getLogin());
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public UserEntity delete(long id) {
        final UserEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    // возможно надо будет тут книжки доработать

    @Transactional
    public UserEntity addLine(long id, long lineId) {
        final UserEntity existsEntity = get(id);
        LineEntity line = lineService.get(lineId);
        existsEntity.addLine(line);
        repository.save(existsEntity);
        return existsEntity;
    }

    @Transactional
    public UserEntity removeLine(long userId, long lineId) {
        final UserEntity existsEntity = get(userId);
        LineEntity line = lineService.get(lineId);
        existsEntity.removeLine(line);
        return repository.save(existsEntity);
    }

    @Transactional
    public UserEntity removeAllLines(long userId) {
        final UserEntity user = get(userId);
        user.getLines().clear();
        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<LineEntity> getLines(long id, int page, int size) {
        UserEntity user = get(id);
    List<LineEntity>
        /*return*/ lines = user.getLines().stream().toList();
        Pageable pageable = PageRequest.of(page, size);
    int start = (int) pageable.getOffset();
    int end = (start + pageable.getPageSize()) > lines.size() ? lines.size() : (start + pageable.getPageSize());

    return new PageImpl<>(lines.subList(start, end), pageable, lines.size());
    }
    // @Transactional(readOnly = true)
    // public Page<LineEntity> getAll(long userId, int page, int size) {
    //     final Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
    //     return get(userId).getLines().stream().toList();
    //     if (typeId <= 0L) {
    //         return repository.findByUserId(userId, pageable);
    //     } else {
    //         return repository.findByUserIdAndTypeId(userId, typeId, pageable);
    //     }
    // }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity existsUser = getByLogin(username);
        return new UserPrincipal(existsUser);
    }

    @Transactional(readOnly = true)
    public UserEntity getByLogin(String login) {
        return repository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login"));
    }
}