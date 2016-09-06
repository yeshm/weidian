<#--<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=enlz6vUdnBUtpFg3lGx2zpQz"></script>
    <title>根据中心点关键字周边搜索</title>
</head>
<body>
<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">

    // 百度地图API功能
    var map = new BMap.Map("allmap");            // 创建Map实例
    map.centerAndZoom("厦门", 12);
   // map.centerAndZoom(new BMap.Point(23.34554, 24.9132345), 11);
    var local = new BMap.LocalSearch(map, {
        renderOptions:{map: map, autoViewport:true}
    });
    local.searchNearby("小吃", "前门");
</script>-->


<head>
    <title>根据地址查询经纬度</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body style="background:#CBE1FF">
<div style="width:730px;margin:auto;">
    要查询的地址：<input id="text_" type="text" value="厦门软件园望海路10号" style="margin-right:100px;"/>
    查询结果(经纬度)：<input id="result_" type="text" />
    <input type="button" value="查询" onclick="searchByStationName();"/>
    <input type="button" value="获取链接地址" onclick="getMapUrl();"/>
    <div id="testHtml"></div>
    <div id="container"
         style="position: absolute;
                margin-top:30px;
                width: 730px;
                height: 590px;
                top: 50;
                border: 1px solid gray;
                overflow:hidden;">
    </div>

</div>
</body>
<script type="text/javascript">

    function searchByStationName() {
        map.clearOverlays();//清空原来的标注
        var keyword = document.getElementById("text_").value;
        localSearch.setSearchCompleteCallback(function (searchResult) {
            var poi = searchResult.getPoi(0);
            point = poi.point
            document.getElementById("result_").value = point.lng + "," + point.lat;
            map.centerAndZoom(point, 16);

            marker = new BMap.Marker(point);  // 创建标注，为要查询的地方对应的经纬度
            map.addOverlay(marker);

            var opts = {
                width : 200,     // 信息窗口宽度
                height: 100,     // 信息窗口高度
                position : point,    // 指定文本标注所在的地理位置
                offset   : new BMap.Size(4, -4),    //设置文本偏移量
                enableMessage:false//设置允许信息窗发送短息
            }
            var content = document.getElementById("text_").value + "<br/><br/>经度：" + point.lng + "<br/>纬度：" + point.lat;
            var infoWindow = new BMap.InfoWindow("<p style='font-size:14px;'>" + content + "</p>", opts); // 创建信息窗口对象
            marker.addEventListener("click", function () { this.openInfoWindow(infoWindow); });//点击标注打开窗口
            map.openInfoWindow(infoWindow,point); //开启信息窗口

            /*var label = new BMap.Label("我是文字标注哦",{offset:new BMap.Size(20,-10)});
            marker.setLabel(label);*/

            marker.enableDragging();    //可拖拽
            // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        });
        localSearch.search(keyword);
    }

    function getMapUrl() {
        var center = map.getCenter();
        var zoom = map.getZoom();
        var size = map.getSize();
        var mapWidth = size.width;
        var mapHeight = size.height;

        var url = ["/ext/assets/ueditor/1.3.5/dialogs/map/show.html" + '#center=' + center.lng + ',' + center.lat,
            '&zoom=' + zoom,
            '&width=' + mapWidth,
            '&height=' + mapHeight,
            '&markers=' + point.lng + ',' + point.lat,
            '&markerStyles=' + 'l,A'].join('');
        $("#testHtml").html(url)
    }

    $(function(){
        var map = new BMap.Map("container"),marker,point;
        map.centerAndZoom("厦门", 13);
        map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
        map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用

        map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
        map.addControl(new BMap.OverviewMapControl()); //添加默认缩略地图控件
        map.addControl(new BMap.OverviewMapControl({ isOpen: true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT }));   //右下角，打开

        var localSearch = new BMap.LocalSearch(map);
        /*var localSearch = new BMap.LocalSearch(map, {
            renderOptions:{map: map}
        });*/
        localSearch.enableAutoViewport(); //允许自动调节窗体大小

        $('#text_').keydown(function(e){
            if(e.keyCode==13){
                searchByStationName()
            }
        });
    })
</script>
<#include "/extcommon/webapp/extcommon/includes/bmap.ftl"/>
</html>



<#--<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <script type="text/javascript" src="../internal.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=enlz6vUdnBUtpFg3lGx2zpQz"></script>
    <style type="text/css">
        .content{width:530px; height: 350px;margin: 10px auto;}
        .content table{width: 100%}
        .content table td{vertical-align: middle;}
        #city,#address{height:21px;background: #FFF;border:1px solid #d7d7d7; line-height: 21px;}
        #city{width:90px}
        #address{width:220px}
    </style>
</head>
<body>
<div class="content">
    <table>
        <tr>
            <td><var id="lang_city"></var>:</td>
            <td><input id="city" type="text" /></td>
            <td><var id="lang_address"></var>:</td>
            <td><input id="address" type="text" value="" /></td>
            <td><a href="javascript:doSearch()" class="button"><var id="lang_search"></var></a></td>
        </tr>
    </table>
    <div style="width:100%;height:340px;margin:5px auto;border:1px solid gray" id="container"></div>

</div>
<script type="text/javascript">
    var map = new BMap.Map("container"),marker,point,imgcss;
    map.enableScrollWheelZoom();
    map.enableContinuousZoom();
    function doSearch(){
        if (!document.getElementById('city').value) {
            alert(lang.cityMsg);
            return;
        }
        var search = new BMap.LocalSearch(document.getElementById('city').value, {
            onSearchComplete: function (results){
                if (results && results.getNumPois()) {
                    var points = [];
                    for (var i=0; i<results.getCurrentNumPois(); i++) {
                        points.push(results.getPoi(i).point);
                    }
                    if (points.length > 1) {
                        map.setViewport(points);
                    } else {
                        map.centerAndZoom(points[0], 13);
                    }
                    point = map.getCenter();
                    marker.setPoint(point);
                } else {
                    alert(lang.errorMsg);
                }
            }
        });
        search.search(document.getElementById('address').value || document.getElementById('city').value);
    }
    //获得参数
    function getPars(str,par){
        var reg = new RegExp(par+"=((\\d+|[.,])*)","g");
        return reg.exec(str)[1];
    }
    function init(){
        var img = ""//editor.selection.getRange().getClosedNode();
        if(img && /api[.]map[.]baidu[.]com/ig.test(img.getAttribute("src"))){
            var url = img.getAttribute("src"),centers;
            centers = getPars(url,"center").split(",");
            point = new BMap.Point(Number(centers[0]),Number(centers[1]));
            map.addControl(new BMap.NavigationControl());
            map.centerAndZoom(point, Number(getPars(url,"zoom")));
            imgcss = img.style.cssText;
        }else{
            point = new BMap.Point(116.404, 39.915);    // 创建点坐标
            map.addControl(new BMap.NavigationControl());
            map.centerAndZoom(point, 10);                     // 初始化地图,设置中心点坐标和地图级别。
        }
        marker = new BMap.Marker(point);
        marker.enableDragging();
        map.addOverlay(marker);
    }
    init();
    document.getElementById('address').onkeydown = function (evt){
        evt = evt || event;
        if (evt.keyCode == 13) {
            doSearch();
        }
    };
    dialog.onok = function (){
        var center = map.getCenter();
        var zoom = map.zoomLevel;
        var size = map.getSize();
        var point = marker.getPoint();
        var url = "http://api.map.baidu.com/staticimage?center=" + center.lng + ',' + center.lat +
                "&zoom=" + zoom + "&width=" + size.width + '&height=' + size.height + "&markers=" + point.lng + ',' + point.lat;
        editor.execCommand('inserthtml', '<img width="'+ size.width +'"height="'+ size.height +'" src="' + url + '"' + (imgcss ? ' style="' + imgcss + '"' :'') + '/>');
    };
    document.getElementById("address").focus();
</script>


</body>
</html>-->

