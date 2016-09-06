<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<form id="searchForm" class="form-horizontal">
    <div class="row">
        <div class="control-group span8">
            <label class="control-label">姓名：</label>

            <div class="controls">
                <input type="text" class="control-text" name="like_name">
            </div>
        </div>

        <div class="control-group span8">
            <label class="control-label">手机号码：</label>

            <div class="controls">
                <input type="text" class="control-text" name="like_phone">
            </div>
        </div>
        <div class="control-group span8">
            <label class="control-label">状态：</label>

            <div class="controls">
            <#assign agentStatusItemList = delegator.findByAndCache("StatusItem",{"statusTypeId":"COMMON_STATUS"})>
                <select name="eq_statusId">
                    <option value="">请选择</option>
                <#list agentStatusItemList as agentStatusItem>
                    <option value="${agentStatusItem.statusId}" ${(requestParameters.eq_statusId?default("COMMON_ENABLED") == agentStatusItem.statusId)?string("selected","")}>${agentStatusItem.description}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="control-group span9">
            <label class="control-label">创建时间：</label>

            <div class="controls">
                <input type="text" class=" calendar" name="ge_createdDate"><span> - </span><input name="le_createdDate" type="text" class=" calendar">
            </div>
        </div>
    </div>
    <div class="row">


        <div class="span3 offset2">
            <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            <button type="reset" class="btn">重置</button>
        </div>
    </div>
</form>

<div class="search-grid-container">
    <div id="grid"></div>
</div>

<div id="content" class="hide">
    <form id="J_Form" class="form-horizontal">
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>姓名</label>

                <div class="controls">
                    <input name="name" type="text" data-rules="{required:true}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span12">
                <label class="control-label"><s>*</s>手机号码</label>

                <div class="controls">
                    <input name="phone" type="text" data-rules="{required:true,mobileOrTel:true}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label">备注</label>

                <div class="controls control-row4">
                    <textarea name="note" class="input-large" type="text"></textarea>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    BUI.use(['common/search', 'common/page', 'bui/form'], function (Search, Page, Form) {
        app.form.registerRules(Form);

        var commonStatusJson = ${StringUtil.wrapString(Static['org.weidian.constant.common.CommonStatus'].toJson()!)};
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
            {
                title: '代理ID', dataIndex: 'agentId', width: 60,
                renderer: function (v) {
                    var editStr = '<span class="grid-command btn-edit" title="编辑">'+v+'</span>';
                    return editStr;
                }
            },
            {title: '姓名', dataIndex: 'name', width: 100},
            {title: '手机号码', dataIndex: 'phone', width: 120},
            {title: '状态', dataIndex: 'statusId', width: 60, renderer: BUI.Grid.Format.enumRenderer(commonStatusJson)},
            {title: '创建时间', dataIndex: 'createdDate', width: 126, renderer: BUI.Grid.Format.datetimeRenderer}
        ];
        columns.push({
            title: '备注', dataIndex: 'note',
            width: app.grid.getLastColumnWidth(columns, 300)
        });

        var store = Search.createStore('<@ofbizUrl>gridAgent</@ofbizUrl>', {
            pageSize: 15
        });
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
                    {text: '<i class="icon-plus"></i>新建', btnCls: 'button button-small btn-edit', handler: addFunction},
                    {text: '<i class="icon-remove"></i>删除', btnCls: 'button button-small', handler: delFunction}
                ]
            },
            plugins: [editing, BUI.Grid.Plugins.CheckSelection] // 插件形式引入多选表格
        });

        var search = new Search({
            store: store,
            gridCfg: gridCfg
        });
        var grid = search.get('grid');

        function submit(record, data, editor) {
            data.agentId = record.agentId;
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>saveAgent</@ofbizUrl>',
                data: data,
                success: function (data) {
                    if(app.ajaxHelper.handleAjaxMsg(data)){
                        editor.accept();
                        search.load();
                    }
                }
            });
        }

        function addFunction(){
            editing.add({},'name');
        }

        //删除操作
        function delFunction() {
            var selections = grid.getSelection();
            delItems(selections);
        }

        function delItems(items) {
            var ids = [];
            BUI.each(items, function (item) {
                ids.push(item.agentId);
            });

            if (ids.length) {
                app.ajaxHelper.confirm({
                    message: "确认要删除选中的记录么？",
                    url: "<@ofbizUrl>deleteAgent</@ofbizUrl>",
                    data: {ids: ids},
                    success: function (d) {
                        if(app.ajaxHelper.handleAjaxMsg(d)){
                            search.load();
                        }
                    }
                });
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

        new Form.Form({
            srcNode: '#syncForm',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("同步微店订单成功");
                    search.load()
                }
            }
        }).render();
    });
</script>