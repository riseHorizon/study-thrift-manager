package com.horizon.demo.server;

import com.horizon.demo.service.BizCalcServiceImpl;
import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 * @author horizon
 */
public class ThreadPoolServerTest {

    private static final int SERVER_PORT = 12356;

    private static final BizCalcServiceImpl bizService = new BizCalcServiceImpl();

    public static void main(String[] args) {
        try {
            // 获取serverTransport
            TServerTransport serverTransport = new TServerSocket(SERVER_PORT);
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);

            // 获取processor
            DemoThriftService.Processor<DemoThriftService.Iface> processor = new DemoThriftService.Processor<>(bizService);
            serverArgs.processor(processor);

            // 指定TBinaryProtocol
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            serverArgs.protocolFactory(protocolFactory);

            // 线程池服务模型，一般用于可以预知最多有多少个客户端并发的场景
            TServer server = new TThreadPoolServer(serverArgs);
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
            System.out.println("Starting the TThreadPoolServer server...");

            // 暴露服务
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
