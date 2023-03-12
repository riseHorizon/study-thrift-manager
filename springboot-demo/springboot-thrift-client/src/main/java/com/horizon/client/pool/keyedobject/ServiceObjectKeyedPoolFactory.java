package com.horizon.client.pool.keyedobject;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Objects;

public class ServiceObjectKeyedPoolFactory extends BaseKeyedPooledObjectFactory<ServiceDataNode, ServiceDataClient> {

    @Override
    public ServiceDataClient create(ServiceDataNode key) throws Exception {
        ServiceDataClient client = new ServiceDataClient().setIp(key.getIp()).setPort(key.getPort());
        try {
            client.open();
        } catch (Exception e) {
            throw e;
        }
        return client;
    }

    @Override
    public PooledObject<ServiceDataClient> wrap(ServiceDataClient value) {
        return new DefaultPooledObject<>(value);
    }

    @Override
    public boolean validateObject(ServiceDataNode key, PooledObject<ServiceDataClient> value) {
        if (Objects.isNull(value)) {
            return false;
        }

        ServiceDataClient client = value.getObject();
        if (Objects.isNull(client)) {
            return false;
        }

        try {
            return client.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void destroyObject(ServiceDataNode key, PooledObject<ServiceDataClient> value) throws Exception {
        if (Objects.nonNull(value)) {
            ServiceDataClient client = value.getObject();
            if (Objects.nonNull(client)) {
                client.close();
            }
            value.markAbandoned();
        }
    }
}
