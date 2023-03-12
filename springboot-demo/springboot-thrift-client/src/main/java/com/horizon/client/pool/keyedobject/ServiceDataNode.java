package com.horizon.client.pool.keyedobject;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class ServiceDataNode {

    private String ip;
    private Integer port;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceDataNode that = (ServiceDataNode) o;

        if (!port.equals(that.port)) return false;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }
}
