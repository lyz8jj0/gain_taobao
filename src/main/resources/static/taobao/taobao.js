$(function () {
    Taobao.clear();

    $("#btnSearch").click(function () {
        Taobao.data = {};
        var url = $.trim($("#txtUrl").val());
        if(url==""){
            layer.alert("请输入淘宝宝贝网址");
            return false;
        }

        var b = isURL(url);
        if(b == false){
            layer.alert("请输入合法的淘宝宝贝网址");
            return false;
        }

        Taobao.fx(url);

    });

    $("#btnDownLoad").click(function () {
        Taobao.generateZip();
    });
});

function isURL(str_url) {// 验证url
    var strRegex = "^https?://[^\\s]*$";
    var re = new RegExp(strRegex);
    return re.test(str_url);
}

var Taobao = {
    loadindex:null,
    data:{},
    // getScript:function(url,callback){
    //     $.getScript(url,function (result) {
    //         var html = result;
    //         console.log(html);
    //
    //         callback();
    //     })
    // },
    clear:function(){
        $("#txtName").val("");
        $("#masterImage").html("");
        $("#descImage").html("");
    },
    fx:function (url) {
        Taobao.clear();
        Taobao.data={};
        Taobao.loadindex = layer.load(1);
        $.post("/taobao/fx",{url:url},function (result) {
            if(result.success == true){
                var data = result.data;

                var title = data.title;
                var masterImages = data.masterImages;
                var desImages = data.desImages;
                var desUrl = data.desUrl;


                $("#txtName").val(title);

                $(masterImages).each(function (index,item) {
                    $("#masterImage").append("<li><a href=\"" + item + "\" target=\"_blank\">主图_" + (index + 1) + "</a> </li>");
                });

                $(desImages).each(function (index,item) {
                    $("#descImage").append("<li><a href=\"" + item + "\" target=\"_blank\">详情_" + (index + 1) + "</a> </li>");
                });
                Taobao.data = data;
                layer.close(Taobao.loadindex);


                // Taobao.getScript(desUrl,function () {
                //     layer.close(Taobao.loadindex);
                // });
            }
            else{
                layer.close(Taobao.loadindex);
                layer.alert(result.msg);
            }
        },"json");
    },
    generateZip:function () {
        if(Taobao.data.title==undefined || Taobao.data.title==null || Taobao.data.title==""){
            layer.alert("请先检测，再打包下载!");
            return false;
        }

        Taobao.loadindex = layer.load(1);
        $.ajax({
            type: 'POST',
            url:  "/taobao/generateZip",
            data: JSON.stringify(Taobao.data),
            dataType: "json",
            contentType: 'application/json',
            success: function(result){
                layer.closeAll();
                if(result.success==true){
                    $("#txtUrl").val("");
                    Taobao.clear();
                    Taobao.data={};
                    Taobao.downLoad(result.data);
                }
                else{
                    layer.alert(result.msg);
                }
            }
        });

    },
    downLoad:function (zipName) {
        location.href = "/taobao/download?zipName=" + zipName;
    }


};