package com.ohpenl.midoffice.configurationtracker;

import org.springframework.boot.SpringApplication;

public class TestConfigurationTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.from(ConfigurationTrackerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
