package com.horizon.demo.client;

import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFastFramedTransport;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.concurrent.TimeUnit;

public class THsHaServerClientTest {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12356;

    public static void main(String[] args) {
        try {
            // 对TSocket的transport对象增加，TFramedTransport装饰，设置调用的服务地址为本地，端口为6789
            TTransport transport = new TFastFramedTransport(new TSocket(SERVER_HOST, SERVER_PORT));
            transport.open();

            // 数据传输协议有：二进制协议、压缩协议、JSON格式协议
            // 这里使用的是二进制协议
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            DemoThriftService.Client client = new DemoThriftService.Client(protocol);

            // 调用服务器端的服务方法
            System.out.println(client.getStr("hi", "xiaoming"));

            TimeUnit.SECONDS.sleep(60);

            System.out.println(client.getInt(2, 5));

            TimeUnit.SECONDS.sleep(60);

            // 关闭
            transport.close();
            System.out.println("client end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
