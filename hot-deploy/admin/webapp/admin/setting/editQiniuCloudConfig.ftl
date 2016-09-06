<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
            <form id="editQiniuCloudForm" class="form-horizontal" method="post"
                  action="<@ofbizUrl>saveQiniuCloudConfig</@ofbizUrl>">
                <div class="control-group">
                    <label for="description" class="control-label"><s>*</s>AccessKey：</label>

                    <div class="controls">
                        <input type="text" id="accessKey" name="accessKey" value="${accessKey!}" class="input-large"
                               data-rules="{required:true}">
                    </div>
                </div>
                <div class="control-group">
                    <label for="mchId" class="control-label"><s>*</s>SecretKey：</label>

                    <div class="controls">
                        <input type="text" id="secretKey" name="secretKey" value="${secretKey!}" class="input-large"
                               data-rules="{required:true}">
                    </div>
                </div>
                <div class="control-group">
                    <label for="apiKey" class="control-label"><s>*</s>BucketName：</label>

                    <div class="controls">
                        <input type="text" id="bucketName" name="bucketName" value="${bucketName!}" class="input-large"
                               data-rules="{required:true}">
                    </div>
                </div>

                <div class="control-group">
                    <label for="apiKey" class="control-label"><s>*</s>UrlPrefix：</label>

                    <div class="controls">
                        <input type="text" id="urlPrefix" name="urlPrefix" value="${urlPrefix!}" class="input-large"
                               data-rules="{required:true}">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">是否启用：</label>

                    <div class="controls rd2">
                        <label class="radio">
                            <input type="radio" value="Y" name="enable" ${((enable!"Y") == "Y")?string("checked", "")}>启用
                        </label>
                        <label class="radio">
                            <input type="radio" value="N" name="enable" ${((enable!"Y") == "N")?string("checked", "")}>不启用
                        </label>
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
            srcNode: '#editQiniuCloudForm',
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
                {text: '七牛云存储设置'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });
</script>