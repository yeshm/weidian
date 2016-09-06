<div class="container">
    <form id="J_Form" class="form-horizontal" action="#">
        <div class="control-group">
            <label class="control-label">日期范围：</label>
            <div class="bui-form-group controls" data-rules="{dateRange : true}">
                <input name="start" type="text" class="calendar"/> - <input name="end" type="text" class="calendar"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">年龄范围：</label>
            <div class="bui-form-group controls" data-rules="{numberRange : true}">
                <input name="start" type="number" class="input-small"/> - <input name="end" type="number" class="input-small"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">勾选2个：</label>
            <div class="bui-form-group controls" data-rules="{checkRange:[2,2]}" data-messages="{checkRange:'只能选择2个'}">
                <label class="checkbox"><input name="ck" type="checkbox" value="1" />一</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="2" />二</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="3" />三</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="4" />四</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="5" />五</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">勾选2-4个：</label>
            <div class="bui-form-group controls" data-rules="{checkRange:[2,4]}" data-messages="{checkRange:'可以勾选{0}-{1}个选项！'}">
                <label class="checkbox"><input name="ck" type="checkbox" value="1" />一</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="2" />二</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="3" />三</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="4" />四</label>
                <label class="checkbox"><input name="ck" type="checkbox" value="5" />五</label>
            </div>
        </div>

        <div class="control-group">
            <label>数值一：</label><input name="a" id="a" data-rules="{max:100}" type="text">
        </div>
        <div class="control-group">
            <label>必须大于数值一：</label><input name="b" data-rules="{greaterThan:'#a'}" type="text">
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
    BUI.use('form',function (Form) {
        app.form.registerRules(Form);
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
    });
</script>
<!-- 仅仅为了显示代码使用，不要在项目中引入使用-->
<script type="text/javascript">
    $(function () {
        prettyPrint();
    });
</script>