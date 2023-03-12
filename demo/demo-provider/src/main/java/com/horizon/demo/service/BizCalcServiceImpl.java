package com.horizon.demo.service;

import org.apache.thrift.TException;

/**
 * @author horizon
 */
public class BizCalcServiceImpl implements DemoThriftService.Iface {

    @Override
    public String getStr(String srcStr1, String srcStr2) throws TException {
        System.out.println("server001>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return "hi" + srcStr1 + "-" + srcStr2;
    }

    @Override
    public long getInt(int val1, int val2) throws TException {
        System.out.println("server002>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return val1 + val2;
    }
}
