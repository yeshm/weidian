<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel" style="margin-top: 10px;">
            <form id="editDefaultSenderInfoForm" class="form-horizontal" method="post" action="<@ofbizUrl>saveDefaultSenderInfo</@ofbizUrl>">
                <div class="control-group">
                    <label for="senderName" class="control-label" style="width: 130px"><s>*</s>寄件人姓名：</label>

                    <div class="controls">
                        <input id="senderName" name="senderName" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderName)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderPhone" class="control-label" style="width: 130px"><s>*</s>寄件人电话：</label>

                    <div class="controls">
                        <input id="senderPhone" name="senderPhone" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderPhone)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderProvince" class="control-label" style="width: 130px"><s>*</s>寄件人所在省：</label>

                    <div class="controls">
                        <input id="senderProvince" name="senderProvince" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderProvince)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderCity" class="control-label" style="width: 130px"><s>*</s>寄件人所在市：</label>

                    <div class="controls">
                        <input id="senderCity" name="senderCity" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderCity)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderRegion" class="control-label" style="width: 130px"><s>*</s>寄件人所在区/县：</label>

                    <div class="controls">
                        <input id="senderRegion" name="senderRegion" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderRegion)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderAddress" class="control-label" style="width: 130px"><s>*</s>寄件人地址：</label>

                    <div class="controls">
                        <input id="senderAddress" name="senderAddress" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderAddress)!}"/>
                    </div>
                </div>
                <div class="control-group">
                    <label for="senderPost" class="control-label" style="width: 130px"><s>*</s>寄件人邮政编码：</label>

                    <div class="controls">
                        <input id="senderPost" name="senderPost" class="input-large" data-rules="{required:true}" value="${(defaultSenderInfo.senderPost)!}"/>
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
            srcNode: '#editDefaultSenderInfoForm',
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
                {text: '默认发货地址设置'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

</script>