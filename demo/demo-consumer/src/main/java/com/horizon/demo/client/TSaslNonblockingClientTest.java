package com.horizon.demo.client;

import com.horizon.demo.service.DemoThriftService;
import com.horizon.demo.service.cn.CnSaslCallbackHandler;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSocket;

import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TSaslNonblockingClientTest {

    public static final String REALM = "thrift-test-realm";
    public static final String SERVICE = "thrift-test";
    public static final String PRINCIPAL = "thrift-test-principal";
    public static final String PASSWORD = "super secret password";

    public static final String UNWRAPPED_MECHANISM = "CRAM-MD5";

    public static final String WRAPPED_MECHANISM = "DIGEST-MD5";
    public static final Map<String, String> WRAPPED_PROPS = new HashMap<>();

    private static final int SOCKET_TIMEOUT = 1500;
    private static final String HOST = "localhost";
    private static final int SERVER_PORT = 12356;

    static {
        WRAPPED_PROPS.put(Sasl.QOP, "auth-int");
        WRAPPED_PROPS.put("com.sun.security.sasl.digest.realm", REALM);
    }

    public static void main(String[] args) throws Exception {
        TSocket socket = new TSocket(HOST, SERVER_PORT);
        socket.setTimeout(SOCKET_TIMEOUT);
        TSaslClientTransport transport = null;
        try {
            SaslClient saslClient = Sasl.createSaslClient(
                    new String[] {WRAPPED_MECHANISM}, PRINCIPAL,
                    SERVICE, HOST, WRAPPED_PROPS, new CnSaslCallbackHandler(PASSWORD));
            transport = new TSaslClientTransport(saslClient, socket);
            transport.open();

            // 数据传输协议有：二进制协议、压缩协议、JSON格式协议
            // 这里使用的是二进制协议
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            DemoThriftService.Client client = new DemoThriftService.Client(protocol);

            // 调用服务器端的服务方法
            System.out.println(client.getStr("hi", "xiaoming-"));

            TimeUnit.SECONDS.sleep(5);

            System.out.println(client.getInt(2, 5));

            TimeUnit.SECONDS.sleep(5);
        } finally {
            if (Objects.nonNull(transport)) {
                transport.close();
            }
        }
    }

}
