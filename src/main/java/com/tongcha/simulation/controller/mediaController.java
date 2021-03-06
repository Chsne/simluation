package com.tongcha.simulation.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongcha.simulation.bidding.SohuRTB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Controller
@Api(tags = "媒体请求测试")
public class mediaController {
    @ApiOperation(value = "搜狐测试请求")
    @PostMapping("/sohu")
    public SohuRTB.Response soHuController(HttpServletRequest request) throws IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        SohuRTB.Request.Builder BidRequest = SohuRTB.Request.newBuilder();
        BidRequest.setVersion(1);
        BidRequest.setBidid("sohubid");

        SohuRTB.Request.Impression.Builder imp = SohuRTB.Request.Impression.newBuilder();
        imp.setIdx(0);
        imp.setPid("35547");
        imp.setBidFloor(1000);
        imp.setIsPreferredDeals(true);
        imp.setCampaignId("35547");
        imp.setLineId("35547");

        SohuRTB.Request.Impression.Video.Builder banner = SohuRTB.Request.Impression.Video.newBuilder();
        banner.addMimes(1);
        banner.setWidth(1920);
        banner.setHeight(1080);
        imp.setVideo(banner);

        SohuRTB.Request.Impression.Banner.Builder video = SohuRTB.Request.Impression.Banner.newBuilder();
        video.addMimes(4);
//        video.setDurationLimit(15);
//        video.setProtocol(1);
        video.setWidth(1920);
        video.setHeight(1080);
//        imp.setVideo(video);
        imp.setBanner(video);

        BidRequest.addImpression(imp);

        SohuRTB.Request.Device.Builder Device = SohuRTB.Request.Device.newBuilder();
        Device.setType("mobile");
        Device.setIp("192.168.50.1");
        Device.setUa("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        Device.setImei("123jdiaskdjasoidjasiodasdnno1od");
        Device.setImsi("dsadasdasdasodsadjoas");
        Device.setMac("01-80-C2-00-00-00");

        BidRequest.setDevice(Device);

        byte[] bytes = BidRequest.build().toByteArray();
        String s = BidRequest.build().toString();
        SohuRTB.Response response = null;
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod("http://127.0.0.1:8080/sohu.htm");
//        PostMethod postMethod = new PostMethod("http://pub.hypersdesk.com/sohu.htm");
        postMethod.addRequestHeader("Content-Type", "application/octet-stream;charset=utf-8");
        postMethod.setRequestEntity(new ByteArrayRequestEntity(bytes));
        if (client.executeMethod(postMethod) == 200) {
            response = SohuRTB.Response.parseFrom(postMethod.getResponseBodyAsStream().readAllBytes());
            System.out.println(response);
            postMethod.releaseConnection();
            client.getHttpConnectionManager().closeIdleConnections(0);

        } else {
            postMethod.releaseConnection();
            client.getHttpConnectionManager().closeIdleConnections(0);

        }
        return response;


    }
}
