package com.horizon.server.config.reg;

import com.google.common.base.Preconditions;
import com.horizon.demo.dto.InstanceDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 注册业务类
 *
 * @author horizon
 */
@Slf4j
public class ServiceRegister {

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private ServiceDiscovery<InstanceDetails> serviceDiscovery;
    private final CuratorFramework client;

    public ServiceRegister(CuratorFramework client, String basePath) throws Exception {
        this.client = client;
        JsonInstanceSerializer<InstanceDetails> serializer =
                new JsonInstanceSerializer<>(InstanceDetails.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .serializer(serializer)
                .basePath(basePath)
                .build();
        serviceDiscovery.start();
    }

    public void registerService(ServiceInstance<InstanceDetails> serviceInstance) throws Exception {
        serviceDiscovery.registerService(serviceInstance);
    }

    public void unregisterService(ServiceInstance<InstanceDetails> serviceInstance) throws Exception {
        serviceDiscovery.unregisterService(serviceInstance);

    }

    public void updateService(ServiceInstance<InstanceDetails> serviceInstance) throws Exception {
        serviceDiscovery.updateService(serviceInstance);

    }

    public void close() throws IOException {
        Preconditions.checkState(closed.compareAndSet(false, true), "Registry service already closed...");
        serviceDiscovery.close();
    }
}
