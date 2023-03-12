package com.horizon.demo.dto;

import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonRootName;


/**
 * 注册详情
 *
 * @author horizon
 */
@Data
@JsonRootName("details")
public class InstanceDetails {

    private String id;

    private String listenAddress;

    private int listenPort;

    public InstanceDetails() {
    }

    public InstanceDetails(String id, String listenAddress, int listenPort) {
        this.id = id;
        this.listenAddress = listenAddress;
        this.listenPort = listenPort;
    }

    @Override
    public String toString() {
        return "InstanceDetails{" +
                "id='" + id + '\'' +
                ", listenAddress='" + listenAddress + '\'' +
                ", listenPort=" + listenPort +
                '}';
    }
}
