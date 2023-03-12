package com.horizon.client.pool.object;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ServiceObjectPoolFactory extends BasePooledObjectFactory<ServiceNode> {

    @Override
    public ServiceNode create() throws Exception {
        return new ServiceNode();
    }

    @Override
    public PooledObject<ServiceNode> wrap(ServiceNode obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void activateObject(PooledObject<ServiceNode> p) throws Exception {
        p.getObject().setIp("127.0.0.1").setPort(12345);
        super.activateObject(p);
    }

    @Override
    public void passivateObject(PooledObject<ServiceNode> p) throws Exception {
        p.getObject().setIp(null).setPort(null);
    }

    @Override
    public void destroyObject(PooledObject<ServiceNode> p) throws Exception {
        p.getObject().close();
        p.markAbandoned();
    }

    @Override
    public boolean validateObject(PooledObject<ServiceNode> p) {
        return true;
    }
}
