package com.horizon.client.pool.object;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServiceNode {

    private String ip;
    private Integer port;

    public void act() {
        System.out.println("===act>>>" + ip + ":" + port);
    }

    public void close() {
        System.out.println("===close>>>" + ip + ":" + port);
    }
}
