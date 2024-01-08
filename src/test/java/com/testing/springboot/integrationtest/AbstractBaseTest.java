package com.testing.springboot.integrationtest;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractBaseTest {

    @Container
    private static final  MySQLContainer MY_SQL_CONTAINER;
    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems");
        MY_SQL_CONTAINER.start();
    }

}
