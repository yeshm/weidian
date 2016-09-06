<#--
<!DOCTYPE html><head>
    <title>根据地址查询经纬度</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=enlz6vUdnBUtpFg3lGx2zpQz"></script>
</head>
<body style="background:#CBE1FF">
<div style="width:730px;margin:auto;">
    要查询的地址：<input id="text_" type="text" value="宁波天一广场" style="margin-right:100px;"/>
    查询结果(经纬度)：<input id="result_" type="text" />
    <input type="button" value="查询" onclick="searchByStationName();"/>
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
    var map = new BMap.Map("container");
    map.centerAndZoom("宁波", 12);
    map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
    map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    map.addControl(new BMap.ScaleControl());

    map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
    map.addControl(new BMap.OverviewMapControl()); //添加默认缩略地图控件
    map.addControl(new BMap.OverviewMapControl({ isOpen: true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT }));   //右下角，打开

    var localSearch = new BMap.LocalSearch(map);
    localSearch.enableAutoViewport(); //允许自动调节窗体大小
    function searchByStationName() {
        map.clearOverlays();//清空原来的标注
        var keyword = document.getElementById("text_").value;
        localSearch.setSearchCompleteCallback(function (searchResult) {
            var poi = searchResult.getPoi(0);
            document.getElementById("result_").value = poi.point.lng + "," + poi.point.lat;
            map.centerAndZoom(poi.point, 13);
            var marker = new BMap.Marker(new BMap.Point(poi.point.lng, poi.point.lat));  // 创建标注，为要查询的地方对应的经纬度
            map.addOverlay(marker);
            var content = document.getElementById("text_").value + "<br/><br/>经度：" + poi.point.lng + "<br/>纬度：" + poi.point.lat;
            var infoWindow = new BMap.InfoWindow("<p style='font-size:14px;'>" + content + "</p>");
            marker.addEventListener("click", function () { this.openInfoWindow(infoWindow); });
            // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
        });
        localSearch.search(keyword);
    }
</script>
</html>-->


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
    </style>
    <title>地址解析</title>
</head>
<body>
<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">
    $(function(){
        // 百度地图API功能
        var map = new BMap.Map("allmap");
        var point = new BMap.Point(116.331398,39.897445);
        map.centerAndZoom("厦门",12);
        // 创建地址解析器实例
        var myGeo = new BMap.Geocoder();
        // 将地址解析结果显示在地图上,并调整地图视野
        myGeo.getPoint("北京市海淀区上地10街", function(point){
            if (point) {
                map.centerAndZoom(point, 15);
                map.addOverlay(new BMap.Marker(point));
            }
        }, "北京市");
    });
</script>
<#include "/extcommon/webapp/extcommon/includes/bmap.ftl"/>
