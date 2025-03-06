package com.bvRadio.iLive.test;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.*;
import java.util.List;

public class WSClient {


    public void WSUtil(String url){


        try {
            // 这里用的binance的socket接口，国内调用需要VPN，使用换成你的就行
//            String url = "wss://stream.binance.com:9443/ws/ethbtc@ticker";
//            String url = "wss://stream.binance.com:9443/ws/ethbtc@depth20";
//            String url = "wss://stream.binance.com:9443/stream?streams=ethbtc@ticker/ethbtc@depth20/trxbtc@ticker/trxbtc@depth20";
            URI uri = new URI(url);
//            CookieManager manager = new CookieManager();
//            CookieHandler.setDefault(manager);
//            for(HttpCookie cookie : cookiesList){
//                manager.getCookieStore().add(uri, cookie);
////                System.out.println("setcookie-------------"+cookie);
//            }

            WebSocketClient mWs = new WebSocketClient(uri){
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    String message = serverHandshake.getHttpStatusMessage();
                    System.out.println(Thread.currentThread().getName()+"=========链接建立成功========="+message);
                }

                @Override
                public void onMessage(String s) {

//                    System.out.println(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {
                    System.err.println(Thread.currentThread().getName()+"==========链接断开===========");
                }
            };
            //建立连接
            mWs.connect();
//            CookieStore cookieJar = manager.getCookieStore();
//            List<HttpCookie> cookies = cookieJar.getCookies();
//            if(cookiesList.isEmpty()){
//                for (HttpCookie ck : cookies) {
//                    //StringBuffer str = new StringBuffer().append(ck);
//                    //cookie.setCookie(str);
//                    cookiesList.add(ck);
////                    System.out.println("getcookie'--->" + ck);
//                }
//            }
//            System.out.println("haha");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
