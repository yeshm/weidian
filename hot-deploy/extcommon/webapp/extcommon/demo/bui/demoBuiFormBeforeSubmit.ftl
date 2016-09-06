<div class="container">
    <form id="J_Form" class="form-horizontal" action="#">
        <div class="row">
            <div class="control-group span24">
                <label class="control-label">备注：</label>
                <div class="controls control-row4">
                    <textarea name="memo" class="input-large" type="text"></textarea>
                </div>
            </div>
        </div>
        <div class="row form-actions actions-bar">
            <div class="span13 offset3 ">
                <button type="submit" class="button button-colorful">保存</button>
                <button type="reset" class="button">重置</button>
            </div>
        </div>
    </form>

    <br>
    验证码：<img id="captchaImg" src="/ext/control/captcha" class="verifycode" onclick="this.src=this.src+'?'"> 亲用鼠标点图片哟   ${sessionAttributes.CAPTCHA_CODE_SESSION_KEY!""}
</div>
<script type="text/javascript">
    BUI.use('form',function (Form) {
        var form = new Form.Form({
            srcNode : '#J_Form',
            submitType : 'ajax',
            callback: function(d){
                if(app.ajaxHelper.handleAjaxMsg(d)){
                    app.showSuccess('设置被关注自动回复成功！',{
                        callback: function(){
                            app.page.reload();
                        }
                    })
                }
            }
        });
        form.render();
        form.on("beforesubmit", function(){
            alert("beforesubmit");
            return false;
        })
    });
</script>
<!-- 仅仅为了显示代码使用，不要在项目中引入使用-->
<script type="text/javascript">
    $(function () {
        prettyPrint();
    });
</script>