package com.horizon.demo.client;

import com.horizon.demo.service.DemoThriftService;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author horizon
 */
public class ClientAsyncTest {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12356;

    public static void main(String[] args) {
        TNonblockingTransport transport = null;
        try {
            // 设置调用的服务地址为本地，端口为6789
            transport = new TNonblockingSocket(SERVER_HOST, SERVER_PORT);

            // 数据传输协议有：二进制协议、压缩协议、JSON格式协议
            // 这里使用的是二进制协议
            // 协议要和服务端一致
            TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
            // 构建异步客户端
            TAsyncClientManager clientManager = new TAsyncClientManager();
            DemoThriftService.AsyncClient.Factory clientFactory = new DemoThriftService.AsyncClient.Factory(clientManager, protocolFactory);
            DemoThriftService.AsyncClient client = clientFactory.getAsyncClient(transport);

            CountDownLatch lock = new CountDownLatch(1);
            // 调用服务器端的服务方法，增加异步回调
            client.getStr("hi", "xiaoming", new AsyncMethodCallback<String>() {

                @Override
                public void onComplete(String response) {
                    System.out.println("响应结果:" + response);
                    lock.countDown();
                }

                @Override
                public void onError(Exception exception) {
                    System.out.println("请求失败:" + exception.getMessage());
                    exception.printStackTrace();
                    lock.countDown();
                }
            });

            lock.await();

            // TimeUnit.SECONDS.sleep(5);

            // 增加异步回调
            client.getInt(2, 5, new AsyncMethodCallback<Long>() {
                @Override
                public void onComplete(Long response) {
                    System.out.println("响应结果:" + response);
                }

                @Override
                public void onError(Exception exception) {
                    System.out.println("请求失败:" + exception.getMessage());
                    exception.printStackTrace();
                }
            });

            TimeUnit.SECONDS.sleep(60);

            System.out.println("client end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭
            if(transport != null) {
                transport.close();
            }
        }
    }

}
