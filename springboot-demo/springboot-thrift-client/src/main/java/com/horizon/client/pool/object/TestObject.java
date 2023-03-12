package com.horizon.client.pool.object;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestObject {

    public static void main(String[] args) throws InterruptedException {
        GenericObjectPoolConfig<ServiceNode> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(2);
        config.setMinIdle(1);
        config.setMaxWait(Duration.ofMillis(3000));
        config.setBlockWhenExhausted(true);
        config.setTestOnReturn(true);

        ServiceObjectPoolFactory objectPoolFactory = new ServiceObjectPoolFactory();
        GenericObjectPool<ServiceNode> pool = new GenericObjectPool<>(objectPoolFactory, config);
        for(int i = 0; i < 5; i++) {
            ServiceNode item = null;
            try{
                item = pool.borrowObject();
                item.act();
                TimeUnit.SECONDS.sleep(2);
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(Objects.nonNull(item)) {
                    pool.returnObject(item);
                }
            }
        }
        TimeUnit.SECONDS.sleep(12);
    }
}
