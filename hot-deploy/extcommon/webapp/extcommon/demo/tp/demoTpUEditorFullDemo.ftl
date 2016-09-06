<div class="row-fluid">
    <div>
        <h1>完整demo</h1>
        <script id="editor" type="text/plain" style="width:1024px;height:500px;"></script>
    </div>
    <div id="btns">
        <div>
            <button onclick="getAllHtml()">获得整个html的内容</button>
            <button onclick="getContent()">获得内容</button>
            <button onclick="setContent()">写入内容</button>
            <button onclick="setContent(true)">追加内容</button>
            <button onclick="getContentTxt()">获得纯文本</button>
            <button onclick="getPlainTxt()">获得带格式的纯文本</button>
            <button onclick="hasContent()">判断是否有内容</button>
            <button onclick="setFocus()">使编辑器获得焦点</button>
            <button onmousedown="isFocus(event)">编辑器是否获得焦点</button>
            <button onmousedown="setblur(event)" >编辑器失去焦点</button>

        </div>
        <div>
            <button onclick="getText()">获得当前选中的文本</button>
            <button onclick="insertHtml()">插入给定的内容</button>
            <button id="enable" onclick="setEnabled()">可以编辑</button>
            <button onclick="setDisabled()">不可编辑</button>
            <button onclick=" UE.getEditor('editor').setHide()">隐藏编辑器</button>
            <button onclick=" UE.getEditor('editor').setShow()">显示编辑器</button>
            <button onclick=" UE.getEditor('editor').setHeight(300)">设置编辑器的高度为300</button>
        </div>

        <div>
            <button onclick="getLocalData()" >获取草稿箱内容</button>
            <button onclick="clearLocalData()" >清空草稿箱</button>
        </div>

    </div>
    <div>
        <button onclick="createEditor()"/>
        创建编辑器</button>
        <button onclick="deleteEditor()"/>
        删除编辑器</button>
    </div>
</div>


<script type="text/javascript" charset="utf-8" src="<@ofbizContentUrl>/ext/assets/ueditor/1.3.6/ueditor.config.js</@ofbizContentUrl>"></script>
<script type="text/javascript" charset="utf-8" src="<@ofbizContentUrl>/ext/assets/ueditor/1.3.6/ueditor.all.js</@ofbizContentUrl>"></script>


<script type="text/javascript">

    var setting = {
        toolbars:[["fullscreen","fontfamily","fontsize","source","background","justifyleft","justifycenter","justifyright","justifyjustify","|","source","bold","italic","underline","|","insertorderedlist","insertunorderedlist","|","insertimage","|",'removeformat','forecolor','backcolor',"insertvideo","|",'emotion','insertvote']],
        labelMap:{'insertvote': '投票添加'},
        initialFrameWidth: 700,
        initialFrameHeight: 150
    };

    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    UE.getEditor('editor', setting);
    UE.dialogShow = function showDialog(){
        var content = UE.getEditor('editor').getContent();
        var voteIframeDom = UE.htmlparser(content,true);
        var fu
        if(voteIframeDom){
            fu = voteIframeDom.getNodeById("voteIframe");
        }
        if(fu!=undefined && fu!=null){
            alert("只允许添加一个投票");
            alert($(window.frames["voteIframe"].document).html());
        }else{
            var Overlay = BUI.Overlay
            var dialog = new Overlay.Dialog({
                title:'模态窗口',
                width:600,
                height:750,
                bodyContent:'<iframe id="show" name="show" style="width: 100%;height: 100%;" src="<@ofbizContentUrl>/ext/assets/ueditor/1.3.6/dialogs/vote/vote.html</@ofbizContentUrl>"></iframe>',
                success:function () {
                    var url = parent.window.location.href;
                    var baseUrl = url.substring(0,url.indexOf("control")+8);
                    var containDataUrl = window.frames["show"].getData();
                    var iframeTemp = "<iframe id='voteIframe' style='height: 400px;background-color:  transparent;border: 0; ' src='"+baseUrl+containDataUrl+"'></iframe>";
                    UE.getEditor('editor').execCommand('insertHtml', iframeTemp);
                    this.close();
                }
            });
            dialog.show();
        }

    }

    function isFocus(e){
        alert(UE.getEditor('editor').isFocus());
        UE.dom.domUtils.preventDefault(e)
    }
    function setblur(e){
        UE.getEditor('editor').blur();
        UE.dom.domUtils.preventDefault(e)
    }
    function insertHtml() {
        var value = prompt('插入html代码', '');
        UE.getEditor('editor').execCommand('insertHtml', value)
    }
    function createEditor() {
        enableBtn();
        UE.getEditor('editor');
    }
    function getAllHtml() {
        alert(UE.getEditor('editor').getAllHtml())
    }
    function getContent() {
        var arr = [];
        arr.push("使用editor.getContent()方法可以获得编辑器的内容");
        arr.push("内容为：");
        arr.push(UE.getEditor('editor').getContent());
        alert(arr.join("\n"));
    }
    function getPlainTxt() {
        var arr = [];
        arr.push("使用editor.getPlainTxt()方法可以获得编辑器的带格式的纯文本内容");
        arr.push("内容为：");
        arr.push(UE.getEditor('editor').getPlainTxt());
        alert(arr.join('\n'))
    }
    function setContent(isAppendTo) {
        var arr = [];
        arr.push("使用editor.setContent('欢迎使用ueditor')方法可以设置编辑器的内容");
        UE.getEditor('editor').setContent('欢迎使用ueditor', isAppendTo);
        alert(arr.join("\n"));
    }
    function setDisabled() {
        UE.getEditor('editor').setDisabled('fullscreen');
        disableBtn("enable");
    }

    function setEnabled() {
        UE.getEditor('editor').setEnabled();
        enableBtn();
    }

    function getText() {
        //当你点击按钮时编辑区域已经失去了焦点，如果直接用getText将不会得到内容，所以要在选回来，然后取得内容
        var range = UE.getEditor('editor').selection.getRange();
        range.select();
        var txt = UE.getEditor('editor').selection.getText();
        range.cloneContents(),
        node = document.getElementById("voteIframe").innerHTML;
        alert(node);
    }

    function getContentTxt() {
        var arr = [];
        arr.push("使用editor.getContentTxt()方法可以获得编辑器的纯文本内容");
        arr.push("编辑器的纯文本内容为：");
        arr.push(UE.getEditor('editor').getContentTxt());
        alert(arr.join("\n"));
    }
    function hasContent() {
        var arr = [];
        arr.push("使用editor.hasContents()方法判断编辑器里是否有内容");
        arr.push("判断结果为：");
        arr.push(UE.getEditor('editor').hasContents());
        alert(arr.join("\n"));
    }
    function setFocus() {
        UE.getEditor('editor').focus();
    }
    function deleteEditor() {
        disableBtn();
        UE.getEditor('editor').destroy();
    }
    function disableBtn(str) {
        var div = document.getElementById('btns');
        var btns = domUtils.getElementsByTagName(div, "button");
        for (var i = 0, btn; btn = btns[i++];) {
            if (btn.id == str) {
                domUtils.removeAttributes(btn, ["disabled"]);
            } else {
                btn.setAttribute("disabled", "true");
            }
        }
    }
    function enableBtn() {
        var div = document.getElementById('btns');
        var btns = domUtils.getElementsByTagName(div, "button");
        for (var i = 0, btn; btn = btns[i++];) {
            domUtils.removeAttributes(btn, ["disabled"]);
        }
    }

    function getLocalData () {
        alert(UE.getEditor('editor').execCommand( "getlocaldata" ));
    }

    function clearLocalData () {
        UE.getEditor('editor').execCommand( "clearlocaldata" );
        alert("已清空草稿箱")
    }

</script>