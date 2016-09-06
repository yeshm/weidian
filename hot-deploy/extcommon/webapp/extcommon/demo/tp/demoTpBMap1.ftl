<div class="container">

    <!--box-title-->
    <div class="box-title">
        <div class="row-fluid">
            <div class="span16">
                <h3><i class="title-ico-edit"></i>百度地图</h3>
            </div>
        </div>
    </div>
    <!--/box-title-->


    <!--box-content-->
    <div class="box-content">

        <div class="row-fluid">

                <div class="control-group">
                    <label class="control-label" for="geoAddress">经纬度</label>

                    <div class="controls">
                        <div class="input-append">
                            <input type="text" id="geoAddress" value="" class="input-large" name="address"/>
                            <button class="button" type="button" id="geoSearchBtn">搜索</button>
                        </div>

                        <div class="tips tips-small tips-notice" style="width: 605px;">
                            <div class="tips-content">注意：这个只是模糊定位，准确位置请地图上标注!</div>
                        </div>

                        <div id="bMap" style="width: 605px;height: 320px;margin-top: 10px;">
                            <i class="icon-spinner icon-spin icon-large"></i>地图加载中...
                        </div>
                        <div>
                            <input type="text" id="geoLng" name="geoLng" value=""/>
                            <input type="text" id="geoLat" name="geoLat" value=""/>
                        </div>

                    </div>
                </div>

        </div>

    </div>
    <!--/box-content-->
    <script type="text/javascript">

//        var data = {
//            lng:116.396797,
//            lat:39.999285,
//            adr:'天堂'
//        }

        $(function () {
            //bMap(data);
            bMap();
        });

    </script>
</div>
<#include "/extcommon/webapp/extcommon/includes/bmap.ftl"/>