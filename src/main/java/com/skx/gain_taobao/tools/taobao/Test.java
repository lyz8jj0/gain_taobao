package com.skx.gain_taobao.tools.taobao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {

    public static void main(String args[]) throws Exception {
//        String result = Test.getURLInfo("http://dscnew.taobao.com/i5/590/630/595630392163/TB15x.QbmSD3KVjSZFK8qv10Vla.desc%7Cvar%5Edesc%3Bsign%5E4b0c38200136e5f82c2310de4cb00fee%3Blang%5Egbk%3Bt%5E1559542912","utf-8");
        String result = Test.getURLInfo("http://dscnew.taobao.com/i5/590/630/595630392163/TB15x.QbmSD3KVjSZFK8qv10Vla.desc%7Cvar%5Edesc%3Bsign%5E4b0c38200136e5f82c2310de4cb00fee%3Blang%5Egbk%3Bt%5E1559542912","utf-8");
        System.out.println(result);
    }

    public static String getURLInfo(String urlInfo,String charset) throws Exception {
        //读取目的网页URL地址，获取网页源码
        URL url = new URL(urlInfo);
        HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
        InputStream is = httpUrl.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        br.close();
        //获得网页源码
        return sb.toString().trim();
    }
}
