package com.horizon.demo.service;

import org.apache.thrift.TException;

/**
 * @author horizon
 */
public class BizIntServiceImpl implements DemoIntThriftService.Iface {

    @Override
    public long getInt(int val1, int val2) throws TException {
        return val1 + val2;
    }
}
