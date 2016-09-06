<h2>动态创建上传按钮（未实现）</h2>
<div class="row-fluid">
    <form id="J_Form" class="form-horizontal span24" action="#">
        <br>
        <br>

        <div class="row">

            <div class="control-group">
                <label class="control-label">客户LOGO：</label>

                <div class="controls">
                    <img class="thumb-image" id="photoUrlImg" src="<@extUploadFileUrl fileUrl=(agent.photoUrl)!"images/system/avatar/head-default.png"/>">
                    <input type="hidden" name="photoUrl" id="photoUrl" value="${(agent.photoUrl)!"images/system/avatar/head-default.png"}">
                    <button type="button" class="button-small" id="photoUrlBtn">自定义</button>
                    <span class="help-inline">建议尺寸(200*200)</span>
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
</div>

<script type="text/javascript">
    $(function () {
        $("#photoUrlBtn").click(function(){
            app.uploadHelper.singleUploader({
                sign: "demo",
                callback: function(){
                    alert('back');
                }
            });
        });
    });
</script>
<#include "/extcommon/webapp/extcommon/includes/uploader.ftl"/>