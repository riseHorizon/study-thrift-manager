package com.horizon.client.controller;

import com.horizon.client.service.ThriftClientRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/hansonwang99")
public class ThriftRpcController {

    @Resource
    private ThriftClientRpcService thriftClientRpcService;

    @RequestMapping(value = "/thrift", method = RequestMethod.GET)
    public String thriftTest(HttpServletRequest request, HttpServletResponse response) {
        try {
            thriftClientRpcService.open();
            return thriftClientRpcService.getRPCThriftService().getData("hansonwang99");
        } catch (Exception e) {
            log.error("RPC调用失败", e);
            return "error";
        } finally {
            thriftClientRpcService.close();
        }
    }
}
