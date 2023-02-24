package com.horizon.server.service;

import com.horizon.demo.service.RPCDateService;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RPCDateServiceImpl implements RPCDateService.Iface {

    @Override
    public String getData(String userName) {

        Date now=new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("今天是yyyy年MM月dd日 E kk点mm分");
        String nowTime = simpleDateFormat.format( now );
        return "Hello " + userName + "\n" + nowTime;
    }
}
