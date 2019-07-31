package com.skx.gain_taobao.service;


import com.skx.gain_taobao.entity.Taobao;

import javax.servlet.http.HttpServletRequest;

public interface ITaobaoService {

    Taobao fx(String url) throws Exception;


    String downLoadZip(Taobao taobao, HttpServletRequest request) throws Exception;
}
