<div class="box-title">
    <div class="row-fluid">
        <div class="span20">
            <label class="crumbs-box">
                <a href="javascript:goBcak()">管理员列表</a>
                <em>>></em>
                <span>
                <#if adminUserLogin?has_content>
                    编辑管理员信息
                <#else>
                    新增管理员信息
                </#if>
                </span>
            </label>
        </div>
    </div>
</div>

<div class="box-content">
    <div class="row-fluid">
        <form id="J_Form" class="form-horizontal" method="post" action="<@ofbizUrl>saveUserLogin</@ofbizUrl>">
            <input type="hidden" name="userLoginId" value="${(requestParameters.userLoginId)!}">

            <div class="control-group">
                <label class="control-label" for="userLoginId"><s>*</s>登陆账号：</label>

                <div class="controls">
                    <input type="text" id="userLoginId" value="${(requestParameters.userLoginId)!}" name="userLoginId" ${(adminUserLogin?has_content)?string("disabled","")} class="input-large" data-rules="{required:true}" onkeyup="value=value.replace(/[\W]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
                </div>
            </div>

        <#if !adminUserLogin??>
            <div class="control-group">
                <label class="control-label" for="currentPassword"><s>*</s>新密码：</label>

                <div class="controls">
                    <input type="password" name="currentPassword" id="currentPassword" class="input-large" data-rules="{required:true}"/>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="currentPasswordVerify"><s>*</s>确认新密码：</label>

                <div class="controls">
                    <input type="password" name="currentPasswordVerify" id="currentPasswordVerify" class="input-large" data-rules="{equalTo:'#currentPassword'}"/>
                </div>
            </div>
        </#if>

            <div class="control-group">
                <label class="control-label"><s>*</s>权限组：</label>

                <div class="controls">
                    <select id="groupId" name="groupId" data-rules="{required:true}">
                    <#assign userGroupId = (userLoginSecurityGroup.groupId)!>
                        <option value="">请选择权限组</option>
                    <#if securityGroupList?has_content>
                        <#list securityGroupList as securityGroup>
                            <option value="${securityGroup.groupId}" <#if userGroupId == securityGroup.groupId>selected</#if> >${securityGroup.description!}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>

            <div class="actions-bar">
                <button type="submit" class="button button-primary">保存</button>
                <button type="button" class="button" onclick="return goBcak();">取消</button>
            </div>
        </form>
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
                    app.showSuccess("保存成功", {
                        callback: function () {
                            goBcak();
                        }
                    });
                }
            }
        }).render();
    });

    function goBcak() {
        app.page.openList({
            id: 'SETTING_ACCOUNT_USER_LOGIN'
        });
        return false;
    }
</script>