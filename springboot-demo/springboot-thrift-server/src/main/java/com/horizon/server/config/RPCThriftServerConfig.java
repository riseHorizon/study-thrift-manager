package com.horizon.server.config;

import com.horizon.server.config.dto.RPCThriftServer;
import com.horizon.server.service.RPCDateServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RPCThriftServerConfig {

    @Value("${thrift.port}")
    private int port;

    @Value("${thrift.minWorkerThreads}")
    private int minThreads;

    @Value("${thrift.maxWorkerThreads}")
    private int maxThreads;

    @Resource
    private RPCDateServiceImpl rpcDateService;

    @Bean(initMethod = "start")
    public RPCThriftServer rpcThriftServer() {
        return new RPCThriftServer(port, minThreads, maxThreads, rpcDateService);
    }
}
