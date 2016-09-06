    <div class="row-fluid">
        <div class="span24">
            <div id="tab"></div>
            <div id="panel" class="bordered">
                <div>
                    标签内容一<br/>
                    标签内容一<br/>
                    标签内容一<br/>
                    标签内容一<br/>
                </div>
                <div>
                        <label class="control-label">供应商编码：</label>
                        <div class="controls">
                            <input type="text" class="control-text">
                        </div>
                </div>
                <div>
                    test                            test                    test                    test
                </div>
            </div>
        </div>
    </div>
<!-- 仅仅为了显示代码使用，不要在项目中引入使用-->
<script type="text/javascript">
    $(function () {
        prettyPrint();
    });
    BUI.use(['bui/tab'],function(Tab){
        var tab = new Tab.TabPanel({
            render : '#tab',
            elCls : 'nav-tabs',
            panelContainer : '#panel',
            autoRender: true,
            children:[
                {text:'源代码',value:'1'},
                {text:'HTML',value:'2'},
                {text:'JS',value:'3'}
            ]
        });
        tab.setSelected(tab.getItemAt(1));
    })
</script>