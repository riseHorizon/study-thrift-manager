package com.horizon.demo.server;

import com.horizon.demo.service.BizCalcServiceImpl;
import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;

public class THsHaServerTest {

    private static final int SERVER_PORT = 12356;

    private static final BizCalcServiceImpl bizService = new BizCalcServiceImpl();

    public static void main(String[] args) {
        try {
            // 获取serverTransport
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(SERVER_PORT);
            THsHaServer.Args serverArgs = new THsHaServer.Args(serverTransport);

            // 获取processor
            DemoThriftService.Processor<DemoThriftService.Iface> processor = new DemoThriftService.Processor<>(bizService);
            serverArgs.processor(processor);

            // 指定TBinaryProtocol
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            serverArgs.protocolFactory(protocolFactory);
            // 对于AbstractNonblockingServerArgs 默认已经设置了new TFramedTransport.Factory()，这里可以不需要设置
            // serverArgs.transportFactory(new TFramedTransport.Factory());

            // 单线程同步处理NIO的事件，将具体的业务处理交给线程池
            TServer server = new THsHaServer(serverArgs);
            /* 如果处理客户端连接过程中的事件信息可以设置一个事件处理器
            server.setServerEventHandler(new TServerEventHandler() {
                @Override
                public void preServe() {

                }

                @Override
                public ServerContext createContext(TProtocol input, TProtocol output) {
                    return null;
                }

                @Override
                public void deleteContext(ServerContext serverContext, TProtocol input, TProtocol output) {

                }

                @Override
                public void processContext(ServerContext serverContext, TTransport inputTransport, TTransport outputTransport) {

                }
            });
            */
            System.out.println("Starting the TNonblockingServer server...");

            // 暴露服务
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
