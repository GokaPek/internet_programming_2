// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.example.demo;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.items.service.ItemService;
import com.example.demo.users.service.UserService;
import com.example.demo.lines.service.LineService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    private final UserService userService;
    private final ItemService itemService;
    private final LineService lineService;

    public DemoApplication(
            LineService lineService,
            UserService userService,
            ItemService itemService) {
        this.lineService = lineService;
        this.userService = userService;
        this.itemService = itemService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
