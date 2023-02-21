package com.horizon.demo.service;

import org.apache.thrift.TException;

/**
 * @author horizon
 */
public class BizStrServiceImpl implements DemoStrThriftService.Iface {

    @Override
    public String getStr(String srcStr1, String srcStr2) throws TException {
        return "hi" + srcStr1 + "-" + srcStr2;
    }

}
