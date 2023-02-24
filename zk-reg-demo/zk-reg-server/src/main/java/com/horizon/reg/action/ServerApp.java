package com.horizon.reg.action;

import com.horizon.reg.dto.InstanceDetails;
import com.horizon.reg.service.ServiceRegistrar;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.UriSpec;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Test ServiceRegistrar
 * In real application you may resister your service in a servlet listener.
 * You can use uriSpec to get the service url ,also you can store the information in a object
 */
public class ServerApp {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();

        ServiceRegistrar serviceRegistrar = new ServiceRegistrar(client,"services");

        ServiceInstance<InstanceDetails> instance1 = ServiceInstance.<InstanceDetails>builder()
                .serviceType(ServiceType.DYNAMIC)
                .name("service1")
                .port(12345)
                .address("192.168.1.100")
                .payload(new InstanceDetails(UUID.randomUUID().toString(),"192.168.1.100",12345,"Test.Service1"))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();

        ServiceInstance<InstanceDetails> instance2 = ServiceInstance.<InstanceDetails>builder()
                .serviceType(ServiceType.DYNAMIC)
                .name("service1")
                .port(12346)
                .address("192.168.1.100")
                .payload(new InstanceDetails(UUID.randomUUID().toString(),"192.168.1.100",12346,"Test.Service1"))
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .build();
        serviceRegistrar.registerService(instance1);
        serviceRegistrar.registerService(instance2);

        TimeUnit.SECONDS.sleep(60);

        serviceRegistrar.close();

        System.out.println("============================");
        TimeUnit.SECONDS.sleep(60);
        System.out.println("============================finish");
    }

}
