<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>淘宝数据抓取</title>
    <meta name="keywords" content="爬虫,爬虫软件,网络爬虫,爬虫工具,采集器,数据采集器,采集软件,网页抓取工具,采集程序,论坛采集软件,文章采集,抓站工具,网页下载工具,淘宝数据采集"/>
    <meta name="description" content="网页数据采集器，是一款使用简单、功能强大的网络爬虫工具，完全可视化操作，无需编写代码，内置海量模板，支持任意网络数据抓取，连续四年大数据行业数据采集领域排名第一 ，爬虫工具、一键抓取淘宝数据、数据营销平台、数字营销解决方案、电商数据分析平台、电商数据分析、店铺数据分析、淘宝数据分析、 天猫数据分析、京东数据分析、电商竞品分析、电商评论分析、语义分析、精准营销解决方案、电商数据 存储中心"/>
    <link href="/taobao/plugins/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"
          type="text/css">
    <link href="/taobao/taobao.css" rel="stylesheet" type="text/css">
</head>
<body>
<header class="site-header">
    <ul class="site-nav">
        <li class="site-bzy-logo">
            <a href="/taobao"></a>
        </li>
    </ul>
</header>

<div class="content">
    <div class="row" name="noprint">
        <div class="bs-callout">
            <h4>使用说明</h4>
            <p>
                <code>1、</code> 输入宝贝网址，然后点击检测！<br>
                <code>2、</code> 待界面显示宝贝名称，宝贝主图，宝贝详情图之后点击打包下载及可！同时也可以点击宝贝主图或宝贝详情图进行查看！<br>
            </p>
            <p><code>注</code> 浏览器请使用ie9及以上，<strong style="color: red;">建议使用
                    <a href="https://www.baidu.com/s?wd=chrome&rsv_spt=1&rsv_iqid=0xd07a64e50001ccac&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=6&rsv_sug1=5&rsv_sug7=100" target="_blank">
                        chrome
                    </a> </strong>或<strong style="color: red;"><a href="https://browser.360.cn/ee/" target="_blank">
                        360极速
                    </a> </strong>浏览器； </p>
        </div>
    </div>
    <div class="row">
        <div class="input-group input-group-lg">
            <span class="input-group-addon" id="sizing-addon1">宝贝网址</span>
            <input type="text" class="form-control" id="txtUrl" placeholder="请输入淘宝宝贝网址"">
            <span class="input-group-btn">
                <button class="btn btn-primary" type="button" id="btnSearch"> 检 测 </button>
            </span>
        </div>
    </div>

    <div class="row">
        <div class="input-group input-group-lg">
            <span class="input-group-addon" id="sizing-addon1">宝贝名称</span>
            <input type="text" class="form-control" disabled id="txtName">
        </div>
    </div>

    <blockquote class="master">
        <h4>宝贝主图</h4>
        <p>
            <ul class="list-unstyled list-inline" id="masterImage">
            </ul>
        </p>
    </blockquote>


    <blockquote class="desc">
        <h4>宝贝详情图</h4>
        <p>
            <ul class="list-unstyled list-inline" id="descImage">
            </ul>
        </p>
    </blockquote>

    <div class="row" style="text-align: center">
        <button type="button" id="btnDownLoad" class="btn btn-success">打 包 下 载 </button>
    </div>

</div>


<div class="footer">
    版权所有 2019
    <a href="#">魅影</a>
</div>
</body>
</html>
<script src="/taobao/jquery-2.2.4.min.js"></script>
<script src="/taobao/plugins/layer/layer.js"></script>
<script src="/taobao/plugins/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="/taobao/taobao.js"></script>