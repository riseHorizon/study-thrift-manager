package com.horizon.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class RPCThriftServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RPCThriftServerApplication.class, args);
    }
}
