package com.horizon.client.pool.keyedobject;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestKeyedObject {

    public static void main(String[] args) throws InterruptedException {
        GenericKeyedObjectPoolConfig<ServiceDataClient> config = new GenericKeyedObjectPoolConfig<>();
        config.setMaxTotalPerKey(2);
        config.setMinIdlePerKey(1);
        config.setMaxWait(Duration.ofMillis(3000));
        config.setBlockWhenExhausted(true);
        config.setTestOnReturn(true);

        ServiceObjectKeyedPoolFactory objectKeyedPoolFactory = new ServiceObjectKeyedPoolFactory();
        GenericKeyedObjectPool<ServiceDataNode, ServiceDataClient> pool =
                new GenericKeyedObjectPool<>(objectKeyedPoolFactory, config);
        for(int i = 0; i < 15; i++) {
            System.out.println("============================");
            ServiceDataNode key = new ServiceDataNode().setIp("127.0.0.1").setPort(1024 + i % 2);
            ServiceDataClient client = null;
            try{
                client = pool.borrowObject(key);
                client.act();
                TimeUnit.SECONDS.sleep(2);
                // 模拟失效
                if(i == 4) {
                    client.setOpen(false);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(Objects.nonNull(key) && Objects.nonNull(client)) {
                    pool.returnObject(key, client);
                }
            }
        }
        TimeUnit.SECONDS.sleep(12);
    }
}
