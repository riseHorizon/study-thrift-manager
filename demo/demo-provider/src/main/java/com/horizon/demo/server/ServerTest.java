package com.horizon.demo.server;

import com.horizon.demo.service.BizCalcServiceImpl;
import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 * @author horizon
 */
public class ServerTest {

    private static final int SERVER_PORT = 12356;

    private static final BizCalcServiceImpl bizService = new BizCalcServiceImpl();

    public static void main(String[] args) {
        try {
            TServerTransport serverTransport = new TServerSocket(SERVER_PORT);
            TSimpleServer.Args serverArgs = new TSimpleServer.Args(serverTransport);
            // 获取processor
            DemoThriftService.Processor<DemoThriftService.Iface> processor = new DemoThriftService.Processor<>(bizService);
            serverArgs.processor(processor);
            // 指定TBinaryProtocol
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            serverArgs.protocolFactory(protocolFactory);

            //单线程服务模型，一般用于测试
            TServer server = new TSimpleServer(serverArgs);

            System.out.println("Starting the simple server...");

            // 暴露服务
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
