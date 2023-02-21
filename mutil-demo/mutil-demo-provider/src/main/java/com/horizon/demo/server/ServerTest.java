package com.horizon.demo.server;

import com.horizon.demo.service.BizIntServiceImpl;
import com.horizon.demo.service.BizStrServiceImpl;
import com.horizon.demo.service.DemoIntThriftService;
import com.horizon.demo.service.DemoStrThriftService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

public class ServerTest {

    public static void main(String[] args) {

        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        processor.registerProcessor( "bizStrService",
                new DemoStrThriftService.Processor<DemoStrThriftService.Iface>(new BizStrServiceImpl()));
        processor.registerProcessor( "bizIntService",
                new DemoIntThriftService.Processor<DemoIntThriftService.Iface>(new BizIntServiceImpl()));
        try {
            TServerSocket serverTransport = new TServerSocket(8888);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);

            System.out.println("Starting the simple server...");

            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
