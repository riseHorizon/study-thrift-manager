package com.horizon.demo.service;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

/**
 * @author horizon
 */
public class BizCalcAsyncServiceImpl implements DemoThriftService.AsyncIface {

    @Override
    public void getStr(String srcStr1, String srcStr2, AsyncMethodCallback<String> resultHandler) throws TException {
        System.out.println("server001>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        resultHandler.onComplete("hi" + srcStr1 + "-" + srcStr2);
    }

    @Override
    public void getInt(int val1, int val2, AsyncMethodCallback<Long> resultHandler) throws TException {
        System.out.println("server002>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        resultHandler.onComplete((long) val1 + val2);
    }
}
