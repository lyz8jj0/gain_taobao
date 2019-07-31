package com.skx.gain_taobao.service;

import com.skx.gain_taobao.entity.Taobao;
import com.skx.gain_taobao.tools.RegUtil;
import com.skx.gain_taobao.tools.StringUtil;
import com.skx.gain_taobao.tools.ZipUtils;
import com.skx.gain_taobao.tools.taobao.ImageUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * https://www.cnblogs.com/Jeremy2001/p/6858106.html
 * https://blog.csdn.net/yzllz001/article/details/79870118
 * 压缩文件下载
 * https://www.cnblogs.com/Animation-programmer/p/8124463.html
 *
 */


@Service
public class TaobaoService implements ITaobaoService {
    private static Logger logger = Logger.getLogger(TaobaoService.class);
    @Override
    public Taobao fx(String url) throws Exception {
        if(RegUtil.isUrl(url)==false){
            throw new Exception("请输入合法的淘宝URL");
        }

        Taobao taobao = new Taobao();


        try {
            logger.info("*************into ** 1 ***");
            Document doc = Jsoup.connect(url)
                    .header("Content-Type", "application/xhtml+xml;charset=UTF-8")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                    .get();
            logger.info("*************into ** 2 ***");
            logger.info(doc.title());

            String baobeiTitle = "";
            try {
                baobeiTitle = doc.title();
                baobeiTitle = baobeiTitle.substring(0,baobeiTitle.lastIndexOf("-"));
            }
            catch (Exception ex){
                baobeiTitle = "";
            }
            taobao.setTitle(baobeiTitle);

            //主图
            Element ulThumb = doc.getElementById("J_UlThumb");
            if (ulThumb == null) {
                throw new Exception("请确认您输入的地址是淘宝宝贝地址");
            }

            Elements lis = ulThumb.select("img");

            List<String> arrLis = new ArrayList<String>();
            for (Element model : lis) {
                logger.info(model.attributes().get("src"));
                String imageUrl = model.attributes().get("src");
                if(StringUtil.isEmpty(imageUrl)){
                    imageUrl = model.attributes().get("data-src");
                }
                String replaceNum = "_\\d+x\\d+.*\\..*$";
                String resultUrl = imageUrl.replaceAll(replaceNum,"");
                if(resultUrl.startsWith("//")){
                    resultUrl = "https:" + resultUrl;
                }
                logger.info(resultUrl);
                arrLis.add(resultUrl);
            }

            List<String> arrDescs = new ArrayList<String>();

            //天猫
            String rootHtml = doc.html();
            if(url.indexOf("detail.tmall.com")>=0){
                // "descUrl":"//dscnew.taobao.com/i6/551/361/559368202786/TB1PAZiavBj_uVjSZFp8qw0SXla.desc%7Cvar%5Edesc%3Bsign%5Efc4fb701253597115d8f4164476ea8bb%3Blang%5Egbk%3Bt%5E1561046445","httpsDescUrl":"//descnew.taobao.com/i6/551/361/559368202786/TB1PAZiavBj_uVjSZFp8qw0SXla.desc%7Cvar%5Edesc%3Bsign%5Efc4fb701253597115d8f4164476ea8bb%3Blang%5Egbk%3Bt%5E1561046445"
                String descUrl = "\"descUrl\":\"(.+)\",\"httpsDescUrl\"";
                String httpsDescUrl = "\"httpsDescUrl\":\"(.+)\",\"fetchDcUrl\"";


                Pattern p=Pattern.compile(httpsDescUrl);
                Matcher m = p.matcher(rootHtml);
                if(m.find()){
                    String url1 = m.group(1);
                    logger.info(url1);
                    taobao.setDesUrl(url1);
                    generateDescImages(arrDescs, url1);
                }

            }
            else if(url.indexOf("item.taobao.com")>=0){
                //淘宝
                String reg = "descUrl\\s*:\\s*.+\\?\\s*'(.+)'\\s*:\\s*'(.+)',";
                Pattern p=Pattern.compile(reg);
                Matcher m = p.matcher(rootHtml);
                if(m.find()){
                    String url1 = m.group(1);
                    String url2 = m.group(2);
                    logger.info(url1);
                    logger.info(url2);
                    taobao.setDesUrl(url2);
                    generateDescImages(arrDescs, url2);
                }

            }

            taobao.setMasterImages(arrLis);
            taobao.setDesImages(arrDescs);
        }
        catch (UnknownHostException ex1){
            throw new Exception("您输入的网址无法访问");
        }

        return taobao;

    }

    private void generateDescImages(List<String> arrDescs, String url1) throws IOException {
        if (url1.startsWith("//")) {
            url1 = "http:" + url1;
        }
        logger.info("145 row :" + url1);
        logger.info("*************into ** 3 ***");

//        headers = {
//                'Accept':'application/json, text/plain, */*',
//                'Accept-Language':'zh-CN,zh;q=0.3',
//                'Referer':'https://item.taobao.com/item.htm',
//                'User-Agent':'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36',
//                'Connection':'keep-alive',
// }


        Document doc = Jsoup.connect(url1)
                .ignoreContentType(true)
                .header("Accept","application/json, text/plain, */*")
                .header("Accept-Language","zh-CN,zh;q=0.3")
                .header("Referer","https://item.taobao.com/item.htm")
                .header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .get();

        logger.info("*************into ** 4 ***");
        Elements imagesElement = doc.select("img");
        logger.info(doc.html());
        for (Element model2 : imagesElement) {
            logger.info(model2.attributes().get("src"));
            String imageUrl = model2.attributes().get("src");
            if(imageUrl.startsWith("//")){
                imageUrl = "https:" + imageUrl;
            }
            arrDescs.add(imageUrl);
        }
    }

    @Override
    public String downLoadZip(Taobao taobao, HttpServletRequest request) throws Exception {
        //第一步创建下载文件目录
        ServletContext sc = request.getSession().getServletContext();

        String currentName = System.nanoTime()+"";
        String root_dir = "/static/downloadzip/" ;
        String root = sc.getRealPath(root_dir);

        String sim_dir = "/static/downloadzip/" + currentName + "/";
        String dir = sc.getRealPath(sim_dir);
        if (!new File(dir).exists()) {
            new File(dir).mkdirs();
        }
        String titleName = "标题.txt";

        try {
            RandomAccessFile raf = new RandomAccessFile(dir + titleName, "rw");
            raf.write(taobao.getTitle().getBytes());
            raf.close();

            int i=0;
            //主图
            for(String master : taobao.getMasterImages()){
                i++;
                String extName = "";
                try {
                    extName = master.substring(master.lastIndexOf("."), master.length()).toLowerCase();
                }
                catch (Exception ex){
                    extName = "";
                }

                byte [] bytes = ImageUtil.getImageFromNetByUrl(master);
                String imageLocal = dir + "主图_" + i + extName;
                ImageUtil.writeImageToDisk(bytes,imageLocal);
            }

            //宝贝详情
            i=0;
            for(String des : taobao.getDesImages()){
                i++;
                String extName = "";
                try {
                    extName = des.substring(des.lastIndexOf("."), des.length()).toLowerCase();
                }
                catch (Exception ex){
                    extName = "";
                }

                byte [] bytes = ImageUtil.getImageFromNetByUrl(des);
                String imageLocal = dir + "宝贝详情_" + i + extName;
                ImageUtil.writeImageToDisk(bytes,imageLocal);
            }

            String zipName = currentName + ".zip";

            ZipUtils.zipFolder(dir,root + zipName);

            ImageUtil.deleteDir(dir);

            return zipName;
        }
        catch (FileNotFoundException ex){
            throw new Exception("未发现文件");
        }
    }

}
