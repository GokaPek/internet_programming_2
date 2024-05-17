package com.example.demo.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.core.configuration.Constants;
import com.example.demo.users.api.UserSignupController;
import com.example.demo.users.model.UserRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(Customizer.withDefaults());

        httpSecurity.authorizeHttpRequests(requests -> requests
                .requestMatchers("/css/**", "/webjars/**", "/*.svg")
                .permitAll());

        httpSecurity.authorizeHttpRequests(requests -> requests
                .requestMatchers(Constants.ADMIN_PREFIX + "/**").hasRole(UserRole.ADMIN.name())
                .requestMatchers("/h2-console/**").hasRole(UserRole.ADMIN.name())
                .requestMatchers(UserSignupController.URL).anonymous()
                .requestMatchers(Constants.LOGIN_URL).anonymous()
                .anyRequest().authenticated());

        httpSecurity.formLogin(formLogin -> formLogin
                .loginPage(Constants.LOGIN_URL));

        httpSecurity.rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"));

        httpSecurity.logout(logout -> logout
                .deleteCookies("JSESSIONID"));

        return httpSecurity.build();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
