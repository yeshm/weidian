<div id="resetPasswordContent" class="hide">
    <form id="resetPasswordForm" class="form-horizontal" action="<@ofbizUrl>resetPassword</@ofbizUrl>">
        <input type="hidden" id="userLoginId" name="userLoginId">

        <div class="control-group">
            <label class="control-label">帐号：</label>

            <div class="controls">
                <input type="text" id="resetPasswordUserLoginId" class="input-normal" readonly/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="newPassword">新密码：</label>

            <div class="controls">
                <input type="password" name="newPassword" id="newPassword" class="input-normal" data-rules="{required:true}"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="newPasswordVerify">确认新密码：</label>

            <div class="controls">
                <input type="password" name="newPasswordVerify" id="newPasswordVerify" class="input-normal" data-rules="{equalTo:'#newPassword'}"/>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    var resetPasswordForm;
    var resetPasswordDialog;

    BUI.use('bui/form', function (Form) {
        resetPasswordForm = new Form.Form({
            srcNode: '#resetPasswordForm',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("密码重置成功!", {
                        callback: function () {
                            resetPasswordDialog.hide();
                        }
                    });
                }
            }
        }).render();
    });

    BUI.use(['bui/overlay'], function (Overlay) {
        resetPasswordDialog = new Overlay.Dialog({
            title: '重置密码',
            width: '500px',
            contentId: 'resetPasswordContent',
            buttons: [
                {
                    text: "确定",
                    elCls: 'button button-primary',
                    handler: function () {
                        $("#resetPasswordForm").submit();
                    }
                },
                {
                    text: "取消",
                    elCls: 'button',
                    handler: function () {
                        this.hide();
                    }
                }
            ]
        })
    })

    function resetPassword(userLoginId) {
        $("#resetPasswordForm")[0].reset();
        $("#userLoginId").val(userLoginId);
        $("#resetPasswordUserLoginId").val(userLoginId);

        resetPasswordDialog.show();

        return false;
    }
</script>