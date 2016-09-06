<style type="text/css">
    .msg-input,.cover-area,.msg-txta {
        background-color:#fff;
        border:1px solid #d3d3d3;
        color:#666;
        max-width:480px;
        padding:2px 8px;
        width:480px;
        -webkit-border-radius:4px;
        -moz-border-radius:4px;
        border-radius:4px;
    }
    .cover-area {
        max-width:496px;
        padding:0;
        width:496px;
        position:relative;
    }
    .cover-hd {
        padding:2px 8px 3px;
        position:relative;
    }
    .upload-tip {
        color:#666;
        font-size:12px;
        line-height:28px;
        text-align:right;
        position:absolute;
        top:-3px;
        right:8px;
    }
    .cover-bd {
        border-top:1px solid #ccc;
        font-size:0;
        overflow:hidden;
        padding:8px;
    }
    .cover-bd img {
        width:100px;
    }
    .cover-del {
        font-size:14px;
        margin-left:5px;
    }
</style>



<!-- 表单页 ================================================== -->
<div class="row-fluid">
    <div class="span24">
        <form id="J_Form" id="J_Form" class="form-horizontal" method="post" action="<@ofbizUrl>doDemoTpUEditor</@ofbizUrl>">
            <br>
            <div class="control-group">
                <label class="control-label">客户LOGO：</label>
                <div class="controls control-row-auto">
                    <div class="cover-area">
                        <div class="cover-hd">
                            <input id="file_upload" name="uploadFile" type="file" />
                            <input id="picUrl" value="" name="picUrl" type="hidden" />
                        </div>
                        <p id="upload-tip" class="upload-tip">建议尺寸(300*300)</p>
                        <p id="imgArea" class="cover-bd" ${(shop.logoUrl)?has_content?string("", "style='display: none;'")} >
                            <img <#if (shop.logoUrl)?has_content>src='<@extUploadFileUrl>${(shop.logoUrl)!""}</@extUploadFileUrl>'</#if> id="img">
                            <a href="javascript:void(0);" class="vb cover-del" id="delImg">删除</a>
                        </p>
                    </div>
                </div>
            </div>
            <br>
            <div class="control-group">
                <label class="control-label">内容：</label>
                <div class="controls control-row-auto">
                    <script type="text/plain" id="editor" name="myContent">



                        <p>
                            <br/>
                        </p>
                        <p>
                            <br/>
                        </p>
                        <p>
                            <br/>
                        </p>

                        <p>
                            <span style="font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;感谢各位对我们爱情的见证，期望您能在</span>
                        </p>
                        <p>
                            <span style="font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;10月18日晚18时莅临我们的婚礼，共同</span>
                        </p>
                        <p>
                            <span style="font-size: 12px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;感受我们的幸福。</span>
                        </p>
                        <p>
                            <span style="font-size: 12px;"><br/></span>
                        </p>
                        <p style="text-align: justify;">
                            <span style="font-size: 12px;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;地址：牡丹大酒店</span>
                        </p>
                        <p>
                            <span style="font-size: 12px;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;新郎：邱子鸣</span>
                        </p>
                        <p>
                            <span style="font-size: 12px;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;新娘：白绮慧</span>
                        </p>
                        <p>
                            <br/>
                        </p>



                    </script>
                    <input type="hidden" id="contentText" name="contentText" value="">
                </div>
            </div>

            <div class="row form-actions actions-bar">
                <div class="span13 offset3 ">
                    <button type="submit" class="button button-colorful">保存</button>
                    <button type="reset" class="button">重置</button>
                    <a href="javascript:void(0);" onclick="test();">TEST</a>
                    <a href="javascript:void(0);" onclick="testSet();">TESTset</a>
                </div>
            </div>

        </form>
    </div>
</div>
<!-- 表单页 ================================================== -->

<#include "/extcommon/webapp/extcommon/includes/ueditor.ftl"/>
<#include "/extcommon/webapp/extcommon/includes/uploader.ftl"/>

<script type="text/javascript">

    $(function() {
        window.msg_editor = new UE.ui.Editor({
            toolbars:[["fullscreen","fontfamily","fontsize","source","background","justifyleft","justifycenter","justifyright","justifyjustify","|","source","bold","italic","underline","|","insertorderedlist","insertunorderedlist","|","insertimage","|",'removeformat','forecolor','backcolor',"insertvideo","|",'emotion']],
            initialFrameWidth: 335,
            initialFrameHeight: 500,
           // initialStyle: 'body{background-repeat:no-repeat;background-position:7px 0px;background-image:url(\'/images/upload/temp/2013-11-20/20131120165912-9516442.jpg\')}',
            iframeCssUrl: "/ext/assets/ueditor/1.3.6/themes/iframe.css"
        });
        window.msg_editor.render("editor");

        BUI.use('form',function (Form) {
            var form = new Form.HForm({
                srcNode: '#J_Form'
            }).render();

            form.on("beforesubmit", function(){
                var editorContent = msg_editor.getContent();
                $("#contentText").val(editorContent);

                alert(editorContent);
                return true;
            })
        });

        app.uploadHelper.singleUploader({
            uploadButtonId: "file_upload",
            sign: "demo",
            showFileUrlId: "img",
            showFileNameId: "picUrl",
            callback: function(obj){
                $("#imgArea").show();
                setTemplateBackground(obj.fileUrl)
            }
        });

        $("#delImg").click(function () {
            $("#imgArea").hide();
            $("#picUrl").val("");
            setTemplateBackground();
        });

    });

    var test = function() {
        var doc = app.helper.getIframeDoc("ueditor_0")
        var su = UE.dom.domUtils.getComputedStyle(doc.body, "background-image")
        alert(su)
    }

    var setTemplateBackground = function(imageUrl) {
        var doc = app.helper.getIframeDoc("ueditor_0");
        var outstr = [];
        outstr.push("background-repeat:no-repeat");
        outstr.push("background-position:7px 0px")
        if (imageUrl) {
            outstr.push("background-image:url(\'"+ imageUrl+"\')")
        }
        UE.utils.cssRule('body','body{' + outstr.join(";") + '}',doc);
    }

</script>