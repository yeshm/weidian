<#assign configKeyEnable = Static["com.juweitu.bonn.constant.common.SystemConfigTypeEnum"].WEIXIN_JMS_ENABLE>
<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
            <form id="editSystemConfigForm" class="form-horizontal" method="post" action="<@ofbizUrl>saveSystemConfig</@ofbizUrl>">

            <#if configKeyEnable?has_content>
                <#assign configEnableValue = Static["com.juweitu.bonn.SystemConfigWorker"].getSystemConfig(delegator, configKeyEnable)!"Y">
                <div class="control-group">
                    <label class="control-label">是否启用:</label>

                    <div class="controls rd2">
                        <label class="radio">
                            <input type="radio" value="Y" name="configMap|${configKeyEnable}" ${(configEnableValue == "Y")?string("checked", "")}>启用
                        </label>
                        <label class="radio">
                            <input type="radio" value="N" name="configMap|${configKeyEnable}" ${(configEnableValue == "N")?string("checked", "")}>不启用
                        </label>
                    </div>
                </div>
            </#if>

                <div class="actions-bar">
                    <button type="submit" class="button button-primary">保存</button>
                    <button type="reset" class="button">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    BUI.use('bui/form', function (Form) {
        app.form.registerRules(Form);
        new Form.Form({
            srcNode: '#editSystemConfigForm',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("保存成功");
                }
            }
        }).render();
    });
    BUI.use('bui/tab', function (Tab) {
        var tab = new Tab.TabPanel({
            render: '#tab',
            elCls: 'nav-tabs',
            panelContainer: '#panel',
            autoRender: true,
            children: [
                {text: '微信消息队列配置'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });
</script>