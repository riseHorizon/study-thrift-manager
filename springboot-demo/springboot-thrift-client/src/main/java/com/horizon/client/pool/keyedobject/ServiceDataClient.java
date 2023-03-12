package com.horizon.client.pool.keyedobject;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServiceDataClient {

    private String ip;
    private Integer port;
    private boolean open = false;

    public void open() {
        open = true;
        System.out.println("===open>>>" + ip + ":" + port + "|" + open);
    }

    public void act() {
        System.out.println("===act>>>" + ip + ":" + port);
    }

    public void close() {
        System.out.println("===close>>>" + ip + ":" + port);
    }
}
