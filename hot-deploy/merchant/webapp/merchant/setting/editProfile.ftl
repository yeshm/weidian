<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
        <form id="J_Form" class="form-horizontal" method="post" action="<@ofbizUrl>saveProfile</@ofbizUrl>">
            <div class="control-group">
                <label class="control-label" for="firstName"><s>*</s>姓名：</label>

                <div class="controls">
                    <input type="text" id="firstName" value="${(person.firstName)!""}" name="firstName" class="input-large" data-rules="{required:true}">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="mobile"><s>*</s>手机号码：</label>

                <div class="controls">
                    <input type="text" name="mobile" id="mobile" value="${(person.mobile)!""}" class="input-large" data-rules="{required:true,mobile:true}">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label"><s>*</s>地区：</label>

                <div class="controls">
                    <select id="provinceGeoId" name="provinceGeoId" data-rules="{required:true}"></select>
                    <select id="cityGeoId" name="cityGeoId" data-rules="{required:true}"></select>
                    <select id="countyGeoId" name="countyGeoId"></select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="address"><s>*</s>详细地址：</label>

                <div class="controls">
                    <input type="text" id="address" value="${(person.address)!""}" name="address" class="input-large" data-rules="{required:true,maxlength:100}">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="email">email：</label>

                <div class="controls">
                    <input type="text" name="email" id="email" value="${(person.email)!""}" class="input-large" data-rules="{email:true,maxlength:40}">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="qq">QQ号码：</label>

                <div class="controls">
                    <input type="text" name="qq" id="qq" value="${(person.qq)!""}" class="input-large" data-rules="{qq:true,maxlength:20}">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="comments">备注：</label>

                <div class="controls">
                    <textarea id="comments" name="comments" class="input-large" style="height:120px;">${(person.comments)!""}</textarea>
                </div>
            </div>
            <div class="actions-bar">
                <button type="submit" class="button button-primary">保存</button>
            </div>
        </form>
        </div>
    </div>
</div>
<script type="text/javascript">
    BUI.use('bui/form', function (Form) {
        app.form.registerRules(Form);
        new Form.Form({
            srcNode: '#J_Form',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("保存成功");
                }
            }
        }).render();
    });

    $(function () {
        new app.CascadeSelect({
            data: geoData,
            uiIds: ["provinceGeoId", "cityGeoId", "countyGeoId"],
            defaultValues: ["${(person.provinceGeoId)!""}", "${(person.cityGeoId)!""}", "${(person.countyGeoId)!""}"]
        });
    });
    BUI.use('bui/tab',function(Tab){
        var tab = new Tab.TabPanel({
            render : '#tab',
            elCls : 'nav-tabs',
            panelContainer : '#panel',
            autoRender: true,
            children:[
                {text:'个人中心'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

</script>
<#include "/extcommon/webapp/extcommon/includes/geo.ftl"/>