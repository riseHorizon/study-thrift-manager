package com.horizon.reg.action;

import com.horizon.reg.dto.InstanceDetails;
import com.horizon.reg.service.ServiceDiscoverer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * test ServiceDiscoverer
 */
public class ClientApp {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        ServiceDiscoverer serviceDiscoverer = new ServiceDiscoverer(client,"services");

        ServiceInstance<InstanceDetails> instance1 = serviceDiscoverer.getInstanceByName("springboot-thrift-server");

        System.out.println(instance1.buildUriSpec());
        System.out.println(instance1.getPayload());

        ServiceInstance<InstanceDetails> instance2 = serviceDiscoverer.getInstanceByName("service1");

        System.out.println(instance2.buildUriSpec());
        System.out.println(instance2.getPayload());

        serviceDiscoverer.close();
        CloseableUtils.closeQuietly(client);
    }
}
