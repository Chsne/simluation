package com.tongcha.simulation.controller;

import com.tongcha.simulation.VO.Content;
import com.tongcha.simulation.service.infiniteLoop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Api(tags = "循环")
public class mainController {
    //localhost:8080/main
    @PostMapping("/start")
    @ApiOperation(value = "启动刷量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "threadCount", value = "开启线程数量", required = true, dataType = "int"),
            @ApiImplicitParam(name = "apiUrl", value = "设定代理ip来源url", required = false, dataType = "int"),
            @ApiImplicitParam(name = "time", value = "每个线程运行几次 0为死循环，默认死循环", required = false, dataType = "Integer")
    })
    public void start(int threadCount,String apiUrl,Integer time) {
        int count = 0;
        while (0 < threadCount--) {
            System.out.println("创建第" + count++ + "线程");
            infiniteLoop infiniteLoop = new infiniteLoop();
            if (apiUrl != null) {
                infiniteLoop.setAPI_URL(apiUrl);
            }
            if (time != null) {
                infiniteLoop.setCount(time);
            }
            Thread thread = new Thread(infiniteLoop);
            thread.start();
        }
    }
}
