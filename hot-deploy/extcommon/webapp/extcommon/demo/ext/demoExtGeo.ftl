<div class="container">
    <form id="J_Form" class="form-horizontal" action="#">
        <div class="control-group">
            <label class="control-label" for="select_bg">选择地区：</label>

            <div class="controls">
                <select name="bg_img" id="provinceId">
                    <option>省份</option>
                </select>
                <select name="bg_img" id="cityId">
                    <option>市</option>
                </select>
                <select name="bg_img" id="countyGeoId">
                    <option>区县</option>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="select_bg">地区名称：</label>

            <div class="controls">
                <input type="text" id="geoId" value="CN-310113">
                <button type="button" onclick="showGeoName();">获取地区名称</button>
            </div>
        </div>
    </form>

</div>
<script type="text/javascript">
    function showGeoName(){
        var geoId = $("#geoId").val();
        //alert(geoId);
        alert(app.geoHelper.getGeoName(geoId));
    }

    $(function(){
//        new app.CascadeSelect({
//            data:geoData,
//            uiIds:["provinceId","cityId","countyGeoId"]
//        });
        new app.CascadeSelect({
            data:geoData,
            uiIds:["provinceId","cityId","countyGeoId"],
            defaultValues:["CN-310000","CN-310100","CN-310107"],
            nameKey:"name",
            valueKey:"id",
            childsKey:"childs"
        });
    });
</script>
<#include "/extcommon/webapp/extcommon/includes/geo.ftl"/>