package com.horizon.demo.client;

import com.horizon.demo.service.DemoIntThriftService;
import com.horizon.demo.service.DemoStrThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;

import java.util.Objects;

public class ClientTest {

    public static void main(String[] args) {
        TSocket transport = null;
        try {
            transport = new TSocket("localhost", 8888);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpStr = new TMultiplexedProtocol(protocol, "bizStrService");
            DemoStrThriftService.Client serviceStr = new DemoStrThriftService.Client(mpStr);

            System.out.println(serviceStr.getStr("demo", "---str"));

            TMultiplexedProtocol mpInt = new TMultiplexedProtocol(protocol, "bizIntService");
            DemoIntThriftService.Client serviceInt = new DemoIntThriftService.Client(mpInt);

            System.out.println(serviceInt.getInt(5, 2));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(Objects.nonNull(transport)) {
                transport.close();
            }
        }
    }
}
