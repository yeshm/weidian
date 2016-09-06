<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
            <form id="updatePassword" class="form-horizontal" method="post" action="<@ofbizUrl>doUpdatePassword</@ofbizUrl>">
                <input type="hidden" name="userLoginId" value="${userLogin.userLoginId}">
                <div class="control-group">
                    <label class="control-label" for="currentPassword">原密码：</label>

                    <div class="controls">
                        <input type="password" name="currentPassword" id="currentPassword" class="input-large" data-rules="{required:true}"/>
                    </div>
                </div>

            <#include "doUpdatePassowrd.ftl"/>

                <div class="actions-bar">
                    <button type="submit" class="button button-primary">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    BUI.use('bui/tab',function(Tab){
        var tab = new Tab.TabPanel({
            render : '#tab',
            elCls : 'nav-tabs',
            panelContainer : '#panel',
            autoRender: true,
            children:[
                {text:'密码修改'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

    BUI.use('bui/form', function (Form) {
        app.form.registerRules(Form);
        new Form.Form({
            srcNode: '#updatePassword',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("保存成功");
                }
            }
        }).render();
    });
</script>