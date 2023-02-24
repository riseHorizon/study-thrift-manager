package com.horizon.server.config.dto;

import com.horizon.demo.service.RPCDateService;
import com.horizon.server.service.RPCDateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

@Slf4j
public class RPCThriftServer {

    private int port;

    private int minThreads;

    private int maxThreads;

    private RPCDateServiceImpl rpcDateService;

    private TBinaryProtocol.Factory protocolFactory;
    private TTransportFactory transportFactory;

    public RPCThriftServer(int port, int minThreads, int maxThreads, RPCDateServiceImpl rpcDateService) {
        this.port = port;
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.rpcDateService = rpcDateService;
        protocolFactory = new TBinaryProtocol.Factory();
        transportFactory = new TTransportFactory();
    }

    public void start() {

        RPCDateService.Processor<RPCDateService.Iface> processor = new RPCDateService.Processor<>(rpcDateService);
        try {
            TServerTransport transport = new TServerSocket(port);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(protocolFactory);
            tArgs.transportFactory(transportFactory);
            tArgs.minWorkerThreads(minThreads);
            tArgs.maxWorkerThreads(maxThreads);

            TServer server = new TThreadPoolServer(tArgs);
            log.info("thrift服务启动成功, 端口={}", port);
            server.serve();
        } catch (Exception e) {
            log.error("thrift服务启动失败", e);
            throw new RuntimeException("thrift服务启动失败");
        }

    }
}
