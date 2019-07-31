package com.skx.gain_taobao.controller;

import com.skx.gain_taobao.entity.JsonResult;
import com.skx.gain_taobao.entity.Taobao;
import com.skx.gain_taobao.service.ITaobaoService;
import com.skx.gain_taobao.tools.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class HomeController extends BaseController {

    @Autowired
    private ITaobaoService taobaoService;

    @RequestMapping(value = "taobao")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");

        return mv;
    }


    @RequestMapping(value = "taobao/fx")
    @ResponseBody
    public JsonResult fx(String url, HttpServletRequest request) {
        try {
            Taobao taobao = taobaoService.fx(url);
            if(StringUtil.isEmpty(taobao.getTitle())){
                throw new Exception("请确认您输入的地址是淘宝宝贝地址");
            }
            return renderSuccess(taobao);
        }
        catch (Exception ex){
            return renderError(ex.getMessage());
        }
    }


    @RequestMapping(value = "taobao/generateZip",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult generateZip(@RequestBody Taobao taobao, HttpServletResponse response, HttpServletRequest request) {
        try {
            if(StringUtil.isEmpty(taobao.getTitle())){
                throw new Exception("请先检测，再打包下载!");
            }



            String zipPath = taobaoService.downLoadZip(taobao,request);
            return renderSuccess("压缩完成",zipPath);
        }
        catch (Exception ex){
            return renderError(ex.getMessage());
        }
    }



    @RequestMapping(value = "taobao/download")
    public void downLoadModel(HttpSession session,
                              HttpServletRequest request,
                              HttpServletResponse response, String zipName) throws Exception {

        String reg = "^\\d+\\.zip$";
        if(!zipName.matches(reg)){
            throw new Exception("非法访问!");
        }
        ServletContext sc = request.getSession().getServletContext();
        String sim_dir = "/static/downloadzip/";
        String dir = sc.getRealPath(sim_dir);

        if (!new File(dir).exists()) {
            new File(dir).mkdirs();
        }

        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        File f = new File(dir + zipName);

        downLoadZip(request, response, in, out, zipName, f);
    }

    private void downLoadZip(HttpServletRequest request, HttpServletResponse response, BufferedInputStream in, BufferedOutputStream out, String zipName, File f) throws IOException {
        try {
            final String userAgent = request.getHeader("USER-AGENT");

            String finalFileName = null;
            if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent,"Trident")) {//IE浏览器
                finalFileName = URLEncoder.encode(zipName, "UTF8");
            }
            else if (StringUtils.contains(userAgent, "Edge")) {//google,火狐浏览器
                finalFileName = URLEncoder.encode(zipName, "UTF8");
            }
            else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
                finalFileName = new String(zipName.getBytes(), "ISO8859-1");
            }
            else {
                finalFileName = URLEncoder.encode(zipName, "UTF8");//其他浏览器
            }

            response.setContentType("application/zip");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
            response.setHeader("Content-Length", String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

        f.delete();
    }
    
}
