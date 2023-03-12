package com.horizon.demo.server;

import com.horizon.demo.service.BizCalcServiceImpl;
import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class TThreadedSelectorServerTest {

    private static final int SERVER_PORT = 12356;

    private static final BizCalcServiceImpl bizService = new BizCalcServiceImpl();

    public static void main(String[] args) {
        try {
            // 获取serverTransport
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(SERVER_PORT);
            TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(serverTransport);
            // 这里可以设置i/o的accept事件后，handleAccept方法是否以线程池的方式提交给selectorThread的阻塞队列
            //// FAST_ACCEPT为直接放入selectorThread的阻塞队列，如果队列满的话会阻塞（默认策略）
            // serverArgs.acceptPolicy(TThreadedSelectorServer.Args.AcceptPolicy.FAST_ACCEPT);
            //// FAIR_ACCEPT为先封装为Runnable再提交给线程池invoker，等待异步执行，会有一定的延迟，提供了accept的处理速度
            // serverArgs.acceptPolicy(TThreadedSelectorServer.Args.AcceptPolicy.FAIR_ACCEPT);

            // 获取processor
            DemoThriftService.Processor<DemoThriftService.Iface> processor = new DemoThriftService.Processor<>(bizService);
            serverArgs.processor(processor);

            // 指定TBinaryProtocol
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            serverArgs.protocolFactory(protocolFactory);
            // 对于AbstractNonblockingServerArgs 默认已经设置了new TFramedTransport.Factory()，这里可以不需要设置
            // serverArgs.transportFactory(new TFramedTransport.Factory());

            // 单线程监听NIO的accept事件，多线程执行NIO的read和write事件，read完成后具体业务执行交给线程池执行的模型
            TServer server = new TThreadedSelectorServer(serverArgs);
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
