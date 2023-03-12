package com.horizon.server.config;

import com.horizon.server.config.reg.ThriftServerRpc;
import com.horizon.demo.dto.InstanceDetails;
import com.horizon.server.config.reg.ServiceRegister;
import com.horizon.server.service.DateRpcServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Configuration
public class ThriftServerRpcConfig {

    @Value("${thrift.zookeeper}")
    private String thriftZookeeper;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${thrift.host}")
    private String host;

    @Value("${thrift.port}")
    private int port;

    @Value("${thrift.minWorkerThreads}")
    private int minThreads;

    @Value("${thrift.maxWorkerThreads}")
    private int maxThreads;

    @Resource
    private DateRpcServiceImpl dateRpcService;

    private CuratorFramework client;

    private ThriftServerRpc thriftServerRpc;

    /**
     * 启动服务
     */
    @Bean(initMethod = "start")
    public ThriftServerRpc thriftServerRpc() {
        thriftServerRpc = new ThriftServerRpc(port, minThreads, maxThreads, dateRpcService);
        return thriftServerRpc;
    }

    @PostConstruct
    private void init() throws Exception {
        // 服务节点注册
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(thriftZookeeper, retry);
        client.start();

        ServiceRegister serviceRegister = new ServiceRegister(client,"services");
        ServiceInstance<InstanceDetails> instance = ServiceInstance.<InstanceDetails>builder()
                .serviceType(ServiceType.DYNAMIC)
                .name(applicationName)
                .port(port)
                .address(host)
                .payload(new InstanceDetails(UUID.randomUUID().toString(), host, port))
                .uriSpec(new UriSpec("thrift://{address}:{port}"))
                .build();

        serviceRegister.registerService(instance);
    }

    @PreDestroy
    protected void destroy() {
        if(Objects.nonNull(client)) {
            try {
                CloseableUtils.closeQuietly(client);
            } catch (Exception e) {
                log.error("close client error", e);
            }
        }
        if(Objects.nonNull(thriftServerRpc)) {
            try {
            thriftServerRpc.close();
            } catch (Exception e) {
                log.error("close thriftServerRpc error", e);
            }
        }
    }
}
