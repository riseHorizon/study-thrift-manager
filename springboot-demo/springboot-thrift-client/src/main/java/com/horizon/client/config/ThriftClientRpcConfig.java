package com.horizon.client.config;

import com.horizon.client.service.ServiceDiscoverer;
import com.horizon.client.service.ThriftClientRpcService;
import com.horizon.demo.dto.InstanceDetails;
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
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Configuration
public class ThriftClientRpcConfig {

    @Value("${thrift.zookeeper}")
    private String thriftZookeeper;

    @Value("${thrift.server.root.path}")
    private String serverApplicationName;

    private CuratorFramework client;

    private ServiceDiscoverer serviceDiscoverer;

    @Bean(initMethod = "init")
    public ThriftClientRpcService thriftClientRpcService() throws Exception {
        ServiceInstance<InstanceDetails> instance = serviceDiscoverer.getInstanceByName(serverApplicationName);
        ThriftClientRpcService thriftClientRpcService = new ThriftClientRpcService();
        thriftClientRpcService.setHost(instance.getAddress());
        thriftClientRpcService.setPort(instance.getPort());
        return thriftClientRpcService;
    }

    @PostConstruct
    private void init() throws Exception {
        // 服务节点发现
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(thriftZookeeper, retry);
        client.start();

        serviceDiscoverer = new ServiceDiscoverer(client,"services");
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
        if(Objects.nonNull(serviceDiscoverer)) {
            try {
                serviceDiscoverer.close();
            } catch (Exception e) {
                log.error("close serviceDiscoverer error", e);
            }
        }
    }
}
