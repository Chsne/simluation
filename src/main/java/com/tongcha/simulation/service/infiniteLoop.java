package com.tongcha.simulation.service;

import cn.hutool.http.HttpUtil;
import com.tongcha.simulation.VO.Content;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class infiniteLoop implements Runnable {

    //    提取链接
    public String API_URL = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=&city=0&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=1&regions=";
    public static String trackUrl = "https://client.ronghuiad.com/huanghe/api/index.php/Wx_api/addTongji";
    public static Request request;
    public static Response response;
    public static OkHttpClient.Builder client = new OkHttpClient().newBuilder();
    public static Content content = new Content();
    public static String ip;
    public static int port;
    public static FormBody body;
    public int count = 0;
    public static String Time;
    public static ArrayList<String> firstClickList = new ArrayList<>();
    public static ArrayList<String> monkeyPageList = new ArrayList<>();
    public static ArrayList<String> origamiPageList = new ArrayList<>();
    public static String url;
    public static int firstclick;
    public static int origamiPageClick;
    public static String[] ipPool;

    public static String aliCookie;
    public static String phpSession;
    private static boolean label;
    private static String ua;
    public static ArrayList<String> uaList;
    public static ArrayList<String> urlList = new ArrayList<>();

    public static List uaList() throws IOException {
        String xmlpath = infiniteLoop.class.getClassLoader().getResource("").getPath();
        LinkedHashSet<String> list = new LinkedHashSet<String>();
        File file = new File(xmlpath+"/config/ua.txt");
        if (file.exists()) {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt = null;
            while (null != bufferedReader.readLine()) {
                lineTxt = bufferedReader.readLine();
                if (lineTxt != null && lineTxt.length() > 70) {
                    if (lineTxt.contains("iphone") || lineTxt.contains("Android")) {
                        if (!lineTxt.contains("windows") || !lineTxt.contains("Macintosh") || !lineTxt.contains("TV")) {
                            list.add(lineTxt);
                        }
                    }
                }

            }
            reader.close();
        }
        return new ArrayList<String>(list);
    }

    static {
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRF");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRA");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRB");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRC");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRD");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRE");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRG");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRH");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRI");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRJ");
        urlList.add("https://client.ronghuiad.com/mibmt/index.html?mz_ca=2271565&mz_sp=80LRg");
    }

    static {

        firstClickList.add("媒体,菜单页+button1wukong+媒体,菜单页,按键1悟空+click");
        firstClickList.add("媒体,菜单页+button1wukong+媒体,菜单页,按键1悟空+click");
        firstClickList.add("媒体,菜单页+button1wukong+媒体,菜单页,按键1悟空+click");
        firstClickList.add("媒体,菜单页+button1wukong+媒体,菜单页,按键1悟空+click");
        firstClickList.add("媒体,菜单页+button1wukong+媒体,菜单页,按键1悟空+click");
        firstClickList.add("媒体,菜单页+button2bajie+媒体,菜单页,按键2八戒+click");
        firstClickList.add("媒体,菜单页+button3shaseng+媒体,菜单页,按键3沙僧+click");
        firstClickList.add("媒体,菜单页+button4tangseng+媒体,菜单页,按键4唐僧+click");
        firstClickList.add("媒体,菜单页+button5zhezhi+媒体,菜单页,按键5折纸+click");
        firstClickList.add("媒体,菜单页+button5zhezhi+媒体,菜单页,按键5折纸+click");
        firstClickList.add("媒体,菜单页+button5zhezhi+媒体,菜单页,按键5折纸+click");
        firstClickList.add("媒体,菜单页+button5zhezhi+媒体,菜单页,按键5折纸+click");
        firstClickList.add("媒体,菜单页+button7rule+媒体,菜单页,按键7活动规则页+click");


        origamiPageList.add("媒体,折纸教程页+button8wukong+媒体,折纸教程页,按键8悟空+click");
        origamiPageList.add("媒体,折纸教程页+button9bajie+媒体,折纸教程页,按键9八戒+click");
        origamiPageList.add("媒体,折纸教程页+button10shaseng+媒体,折纸教程页,按键10沙僧+click");
        origamiPageList.add("媒体,折纸教程页+button11tangseng+媒体,折纸教程页,按键11唐僧+click");
        origamiPageList.add("媒体,折纸教程页+button13back+媒体,折纸教程页,按键13返回+click");

        monkeyPageList.add("媒体,故事页悟空+button18back+媒体,故事页悟空,按键18后退+click");
        monkeyPageList.add("媒体,故事页悟空+button17suspend+媒体,故事页悟空,按键17暂停+click");
        monkeyPageList.add("媒体,故事页悟空+butto19forward+媒体,故事页悟空,按键19前进+click");
        monkeyPageList.add("媒体,故事页悟空+button102gotoguide+媒体,故事页悟空,按键102跳转引流页+click");

    }

    static {

        try {
            uaList = (ArrayList<String>) uaList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        getAdress();
    }

    public void loop() {
        restart:
        while (count == 0 || 0 < count--) {
//            确定本次使用的UA
            ua = uaList.get(new Random().nextInt(0, uaList.size() - 1));
//            配置代理
            boolean flag = initProxy();
            if (!flag) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue restart;
                }
                continue restart;
            }

//            获得初始Cookie
            try {
                initCookie();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("获取cookie失败");
                continue restart;
            }


            try {
//            发起loading页请求
                Proxies("/+媒体,加载页+page");
                Proxies("/home+媒体,菜单页+page");
                Thread.sleep(new Random().nextInt(1000, 2000));
                randClikeNoSound("媒体,菜单页+button6music+媒体,菜单页,按键6音乐+click");
                randClikeNoSound("媒体,菜单页+button6music+媒体,菜单页,按键6音乐+click");
//              获取第一次点击的页面
                firstclick = new Random().nextInt(firstClickList.size() - 1);
                Proxies(firstClickList.get(firstclick));
                Thread.sleep(new Random().nextInt(3000, 5000));
                switch (firstclick) {
                    case 1:
                    case 2:
                    case 3:
                        Proxies("/guide+媒体,引流页+page");
                        randClikeNoSound("媒体,引流页+button6music+媒体,引流页,按键6音乐+click");
                        if (randToTaoBao()) {
                            continue restart;
                        } else {
                            if (1 == new Random().nextInt(5)) {
                                firstclick = new Random().nextInt(5);
                                Thread.sleep(new Random().nextInt(3000, 5000));
                                Proxies("媒体,菜单页+back+媒体,菜单页,返回+click");
                                Proxies("/home+媒体,菜单页+page");
                                Thread.sleep(new Random().nextInt(3000, 5000));
                                Proxies(firstClickList.get(firstclick));
                                continue;
                            } else {
                                Thread.sleep(new Random().nextInt(3000, 5000));
                                continue restart;
                            }
                        }
                    case 4:
                        Proxies("origami+媒体,折纸教程页+page");
                        Thread.sleep(new Random().nextInt(30000, 50000));
                        origamiPageClick = new Random().nextInt(4);
                        Proxies(origamiPageList.get(origamiPageClick));
                        if (origamiPageClick != 4) {
                            Thread.sleep(new Random().nextInt(30000, 50000));
                            if (1 == new Random().nextInt(0, 1)) {
                                origamiPageClick = new Random().nextInt(4);
                                Proxies(origamiPageList.get(origamiPageClick));
                            }
                        } else if (origamiPageClick == 4) {
                            Proxies("/home+媒体,菜单页+page");
                            firstclick = new Random().nextInt(5);
                            Thread.sleep(new Random().nextInt(3000, 5000));
                            Proxies(firstClickList.get(firstclick));
                        }
                        break;
                    case 5:
                        Thread.sleep(new Random().nextInt(10000, 20000));
                        break;

                    case 0:
                        Proxies("story/1+媒体,故事页悟空+page");
                        Thread.sleep(new Random().nextInt(60000, 100000));
                        int total = new Random().nextInt(1, 4);
                        while (0 < total--) {
                            int monkeyClick = new Random().nextInt(3);
                            if (monkeyClick != 3) {
                                Proxies(monkeyPageList.get(monkeyClick));

                                Thread.sleep(new Random().nextInt(10000, 20000));

                            } else if (monkeyClick == 3) {
                                randToTaoBao();
                                continue;
                            }
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue restart;
            } catch (InterruptedException e) {
                System.out.println("线程暂停失败");
                e.printStackTrace();
                System.out.println("重试");
                continue restart;
            }


        }
    }

    public void Proxies(String content) throws IOException {
        body = new FormBody.Builder().add("content", content).build();
        request = new Request.Builder().url(trackUrl).addHeader("referer", url).addHeader("user-agent", ua).addHeader("cookie", phpSession + ";" + aliCookie).post(body).build();
        response = client.build().newCall(request).execute();
        Time = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println(Time + "输出结果2：" + content + Objects.requireNonNull(response.body()).string());
    }


//    public static final MediaType header = MediaType.get("application/x-www-form-urlencoded");

    //    获取新ip并且配置代理
    public boolean initProxy() {
/*        String result = HttpUtil.get(API_URL);
        if (result.contains("code") || result.contains("频次")) {
            return false;
        }*/
        String result = ipPool[new Random().nextInt(ipPool.length - 1)];
        System.out.println("ip+端口 = " + result);
        String[] ipport = result.split(":");
        ip = ipport[0];
        port = Integer.parseInt(ipport[1].trim());
        client.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));
        return true;
    }

    //    发起请求获取cookie
    public void initCookie() throws IOException {
        url = urlList.get(new Random().nextInt(0, urlList.size() - 1));
        body = new FormBody.Builder().add("url", url).build();
        request = (new Request.Builder().addHeader("user-agent", ua).url("https://client.ronghuiad.com/wxapi.php").post(body).build());
        response = client.build().newCall(request).execute();
        List<String> values = response.headers().values("Set-Cookie");
        aliCookie = values.get(0).split(";")[0];
        phpSession = values.get(1).split(";")[0];
    }

    //随机点击静音

    //随机点击静音键
    public void randClikeNoSound(String content) throws IOException, InterruptedException {
        Thread.sleep(new Random().nextInt(3000, 5000));
        if (1 == new Random().nextInt(0, 20)) {
            Proxies(content);
        }
    }


    //    随机是否点击引流页
    public boolean randToTaoBao() throws IOException, InterruptedException {
        Thread.sleep(new Random().nextInt(3000, 5000));
        if (1 == new Random().nextInt(0, 20)) {
            Proxies("媒体,引流页+button21landing to EC+媒体,引流页,按键21非微信跳转电商+click");
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        loop();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAPI_URL() {
        return API_URL;
    }

    public void setAPI_URL(String API_URL) {
        this.API_URL = API_URL;
    }

    @Scheduled(fixedDelay = 30000)
    public static void getAdress() {
        System.out.println("更新ip池");
        String urls = HttpUtil.get("http://47.103.81.39:8000/proxy/?query=all");
        String[] split = urls.split("\n");
        ipPool = split;
    }

    @Scheduled(fixedDelay = 60000)
    public void getAdresses() {
        System.out.println("更新ip池2");
        String urls = HttpUtil.get("http://47.103.81.39:8000/proxy/?query=all");
        String[] split = urls.split("\n");
        ipPool = split;
    }
}
