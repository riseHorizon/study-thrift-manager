package com.horizon.demo.client;

import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.concurrent.TimeUnit;

public class TThreadedSelectorServerClientTest {

    private static final String SERVER_HOST = "localhost";

    private static final int SERVER_PORT = 12356;

    public static void dealData(int mark) {
        TTransport transport = null;
        try {
            // 对TSocket的transport对象增加，TFramedTransport装饰，设置调用的服务地址为本地，端口为6789
            transport = new TFramedTransport(new TSocket(SERVER_HOST, SERVER_PORT));
            transport.open();

            // 数据传输协议有：二进制协议、压缩协议、JSON格式协议
            // 这里使用的是二进制协议
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            DemoThriftService.Client client = new DemoThriftService.Client(protocol);

            // 调用服务器端的服务方法
            System.out.println(client.getStr("hi", "xiaoming-" + mark));

            TimeUnit.SECONDS.sleep(60);

            System.out.println(client.getInt(2, 5 + mark));

            TimeUnit.SECONDS.sleep(60);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭传输通道
            if(transport != null) {
                transport.close();
            }
            System.out.println("client end");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread("Thread " + i) {
                @Override
                public void run() {
                    // 设置传输通道 对于非阻塞服务 需要使用TFramedTransport(用于将数据分块发送)
                    for (int j = 0; j < 10; j++) {
                        int mark = j;
                        dealData(mark);
                    }
                }
            }.start();
        }
    }
}
