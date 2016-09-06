<div class="container">
    <form id="J_Form" class="form-horizontal" action="#">
        <div class="control-group">
            <label class="control-label"><s>*</s>供应商编码：</label>
            <div class="controls">
                <input name="id" type="text" id="inputId" data-rules="{required:true}" class="input-normal control-text">
            </div>
        </div>

        <div class="row form-actions actions-bar">
            <div class="span13 offset3 ">
                <button type="submit" class="button button-colorful">保存</button>
                <button type="reset" class="button">重置</button>
                <button type="button" class="button" id="cancelValidate">取消验证</button>
                <button type="button" class="button" id="addValidate">加入验证</button>
            </div>
        </div>
    </form>

</div>
<script type="text/javascript">
    BUI.use('form',function (Form) {
        var form = new Form.Form({
            srcNode : '#J_Form',
            submitType : 'ajax',
            callback: function(d){
                if(app.ajaxHelper.handleAjaxMsg(d)){

                }
            }
        });
        form.render();

        var child;

        $("#cancelValidate").click(function(){
            child = form.getChild("inputId");
            if(child){
                form.removeChild(child);
                child.clearErrors();
            }
        });

        $("#addValidate").click(function(){
            if(!(form.getChild("inputId")) && child){
                form.addChild(child)
            }
        });

        //如果输入框不存在，可以新建
//        form.addChild({
//            xclass : 'form-field',
//            srcNode : "#inputId"
//        })
    });
</script>
<!-- 仅仅为了显示代码使用，不要在项目中引入使用-->
<script type="text/javascript">
    $(function () {
        prettyPrint();
    });
</script>