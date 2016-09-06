<div class="row">
    <form id="searchForm" class="form-horizontal span24">
        <div class="row">
            <div class="control-group span8">
                <label class="control-label">店铺名称：</label>
                <div class="controls">
                    <input type="text" class="control-text" name="like_storeName">
                </div>
            </div>
            <div class="control-group span8">
                <label class="control-label">手机号码：</label>
                <div class="controls">
                    <input type="text" class="control-text" name="like_telephone">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span9">
                <label class="control-label">创建时间：</label>
                <div class="controls">
                    <input type="text" class=" calendar" name="ge_createdDate"><span> - </span><input name="le_createdDate" type="text" class=" calendar">
                </div>
            </div>
            <div class="span4 offset2">
                <button  type="button" id="btnSearch" class="button button-primary">搜索</button>
                <button  type="reset" class="btn">重置</button>
            </div>
        </div>
    </form>
</div>

<div class="search-grid-container">
    <div id="grid"></div>
</div>

<div id="content" class="hide">
    <form id="J_Form" class="form-horizontal">
        <input type="hidden" name="a" value="3">
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>店铺名称</label>
                <div class="controls">
                    <input name="storeName" type="text" data-rules="{required:true}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span12">
                <label class="control-label"><s>*</s>手机号码</label>
                <div class="controls">
                    <input name="telephone" type="text" data-rules="{required:true,mobileOrTel:true}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>密码</label>
                <div class="controls">
                    <input name="currentPassword" id="currentPassword" type="password" data-rules="{required:true,minlength:6}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>确认密码</label>
                <div class="controls">
                    <input name="currentPasswordVerify" type="password" data-rules="{required:true,equalTo:'#currentPassword'}" class="input-normal control-text">
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    BUI.use(['common/search', 'common/page', 'bui/form'], function (Search, Page, Form) {
        app.form.registerRules(Form);

        var enumObj = {"PERSON_ENABLED": "启用", "PERSON_DISABLED": "禁用"};
        var editing = new BUI.Grid.Plugins.DialogEditing({
            contentId: 'content', //设置隐藏的Dialog内容
            triggerCls: 'btn-edit', //触发显示Dialog的样式
            editor: {
                success: function () { //点击确认的时候触发，可以进行异步提交
                    var editor = this;
                    var record = editing.get('record');
                    var data = editor.getValue(); //编辑完成的记录
                    var form = editing.get('form');
                    if (form.isValid()) {
                        submit(record, data, editor);
                    }
                }
            }
        });
        var columns = [
            {title: '店铺ID', dataIndex: 'productStoreId', width: 80},
            {title: '店铺名称', dataIndex: 'storeName', width: 100},
            {title: '手机号码', dataIndex: 'telephone', width: 100},
            {title: '创建时间', dataIndex: 'createdDate', width: 126, renderer: BUI.Grid.Format.datetimeRenderer}/*,
            {title: '操作', dataIndex: '', width: 200, renderer: function (value, obj) {
                var editStr = '<span class="grid-command btn-edit" title="编辑">编辑</span>';
                var delStr = '<span class="grid-command btn-del" title="删除">删除</span>';
//页面操作不需要使用Search.createLink
                return editStr + delStr;
            }}*/
        ];
        var store = Search.createStore('<@ofbizUrl>gridMerchant</@ofbizUrl>', {
            pageSize: 15
        });
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
                    {text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',handler:showEditMerchantDialog}/*,
                    {text : '<i class="icon-edit"></i>编辑',btnCls : 'button button-small',handler:function(){alert('编辑');}},
                    {text: '<i class="icon-remove"></i>删除', btnCls: 'button button-small', handler: delFunction}*/
                ]
            },
            plugins: [editing, BUI.Grid.Plugins.CheckSelection] // 插件形式引入多选表格
        });

        var search = new Search({
            store: store,
            gridCfg: gridCfg
        });
        var grid = search.get('grid');

        function showEditMerchantDialog() {
            var newData = {isNew : true}; //标志是新增加的记录
            editing.add(newData,'name'); //添加记录后，直接编辑
        }

        function syncWeiDianMerchant() {
            app.ajaxHelper.submitRequest({
                url: "<@ofbizUrl>syncWeiDianMerchant</@ofbizUrl>",
                success: function (data) {
                    if (app.ajaxHelper.handleAjaxMsg(data)) {
                        app.showError("同步订单失败")
                    }
                }
            })
        }

        function submit(record, data, editor) {
            data.productStoreId = record.productStoreId;
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>saveMerchant</@ofbizUrl>',
                data: data,
                success: function (data) {
                    if(app.ajaxHelper.handleAjaxMsg(data)){
                        editor.accept(); //隐藏弹出框
                        search.load();
                    }
                }
            });
        }

        //删除操作
        function delFunction() {
            var selections = grid.getSelection();
            delItems(selections);
        }

        function delItems(items) {
            var ids = [];
            BUI.each(items, function (item) {
                ids.push(item.productStoreId);
            });

            if (ids.length) {
                BUI.Message.Confirm('确认要删除选中的记录么？', function () {
                    $.ajax({
                        type: 'post',
                        url: '<@ofbizUrl>deleteMerchant</@ofbizUrl>',
                        dataType: 'json',
                        data: {ids : ids},
                        success: function (data) {
                            if (data.result == "success") {
                                search.load();
                            } else {
                                var msg = data.msg;
                                BUI.Message.Alert('错误原因:' + msg);
                            }
                        }
                    });
                }, 'question');
            }
        }

        //监听事件，删除一条记录
        grid.on('cellclick', function (ev) {
            var sender = $(ev.domTarget); //点击的Dom
            if (sender.hasClass('btn-del')) {
                var record = ev.record;
                delItems([record]);
            }
        });
    });
</script>