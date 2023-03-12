package com.horizon.server.config.reg;

import com.horizon.demo.service.RPCDateService;
import com.horizon.server.service.DateRpcServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.util.Objects;

@Slf4j
public class ThriftServerRpc {

    private final int port;

    private final int minThreads;

    private final int maxThreads;

    private final TBinaryProtocol.Factory protocolFactory;
    private final TTransportFactory transportFactory;

    private final DateRpcServiceImpl rpcDateService;

    private TServer server;

    public ThriftServerRpc(int port, int minThreads, int maxThreads, DateRpcServiceImpl rpcDateService) {
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

            server = new TThreadPoolServer(tArgs);
            log.info("thrift服务启动成功, 端口={}", port);
            server.serve();
        } catch (Exception e) {
            log.error("thrift服务启动失败", e);
            throw new RuntimeException("thrift服务启动失败");
        }

    }

    public void close() {
        if(Objects.nonNull(server)) {
            server.stop();
        }
    }
}
