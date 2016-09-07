<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel" style="margin-top: 10px;">
            <form id="editWeiDianForm" class="form-horizontal" method="post"
                  action="<@ofbizUrl>saveWeiDian</@ofbizUrl>">
                <div class="control-group">
                    <label for="description" class="control-label"><s>*</s>appkey：</label>

                    <div class="controls">
                        <input id="appkey" name="appkey" class="input-large" data-rules="{required:true}" value="${(weidian.appkey)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="description" class="control-label"><s>*</s>secret：</label>

                    <div class="controls">
                        <input id="secret" name="secret" class="input-large" data-rules="{required:true}" value="${(weidian.secret)!}"/>
                    </div>
                </div>

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
            srcNode: '#editWeiDianForm',
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
                {text: '微店开发账号设置'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

</script>