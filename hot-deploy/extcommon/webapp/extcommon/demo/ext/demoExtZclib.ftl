<div class="row-fluid">
    <div class="span24">
        <div class="control-row-auto">
            <div class="controls">
                <textarea id="testTextArea">新浪微博在赴美上市前高调封杀微信公众号</textarea>
                <button type="button" id="copyButton" class="button-small button-primary">复制到剪贴板</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
     $(function(){
         new app.zclip({
             buttonId: "copyButton",
             dataSourceInputId: "testTextArea"
         });
     });
</script>
<#include "/extcommon/webapp/extcommon/includes/zclip.ftl"/>