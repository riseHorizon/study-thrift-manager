package com.horizon.demo.server;

import com.horizon.demo.service.BizCalcServiceImpl;
import com.horizon.demo.service.DemoThriftService;
import com.horizon.demo.service.cn.CnSaslCallbackHandler;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TSaslNonblockingServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

import javax.security.sasl.Sasl;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TSaslNonblockingServerTest {

    public static final String HOST = "localhost";
    public static final String SERVICE = "thrift-test";
    public static final String PRINCIPAL = "thrift-test-principal";
    public static final String PASSWORD = "super secret password";
    public static final String REALM = "thrift-test-realm";

    public static final String UNWRAPPED_MECHANISM = "CRAM-MD5";

    public static final String WRAPPED_MECHANISM = "DIGEST-MD5";
    public static final Map<String, String> WRAPPED_PROPS = new HashMap<>();

    static {
        WRAPPED_PROPS.put(Sasl.QOP, "auth-int");
        WRAPPED_PROPS.put("com.sun.security.sasl.digest.realm", REALM);
    }

    private static final int SERVER_PORT = 12356;

    private static final BizCalcServiceImpl bizService = new BizCalcServiceImpl();

    public static void main(String[] args) throws Exception {
        TSaslNonblockingServer server = null;
        try {
            // 获取serverTransport
            TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(SERVER_PORT);
            TSaslNonblockingServer.Args serverArgs = new TSaslNonblockingServer.Args(serverSocket);

            // 获取processor
            DemoThriftService.Processor<DemoThriftService.Iface> processor = new DemoThriftService.Processor<>(bizService);
            serverArgs.processor(processor);

            // 指定TBinaryProtocol
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            serverArgs.protocolFactory(protocolFactory);

            serverArgs.transportFactory(null).addSaslMechanism(
                    WRAPPED_MECHANISM,
                    SERVICE,
                    HOST,
                    WRAPPED_PROPS,
                    new CnSaslCallbackHandler(PASSWORD));
            server = new TSaslNonblockingServer(serverArgs);
            server.serve();
            System.out.println("....................");
        } finally {
            //stopServer(server);
        }
    }

    public static void stopServer(TSaslNonblockingServer server) throws Exception {
        if (Objects.nonNull(server)) {
            server.shutdown();
        }
    }
}
