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

<form id="syncForm" class="form-horizontal" action="<@ofbizUrl>syncWeiDianOrder</@ofbizUrl>">
    <div class="row">
        <div class="control-group span13">
            <label class="control-label">下单时间：</label>

            <div class="controls">
                <input type="text" class=" calendar" name="ge_createdDate" data-rules="{required:true}"><span> - </span><input name="le_createdDate" type="text" class=" calendar"
                                                                                                                               data-rules="{required:true}">
            </div>
        </div>
        <div class="span7 offset2">
            <button type="submit" class="button button-primary">同步微店订单</button>
            <button type="reset" class="btn">重置</button>
        </div>
    </div>
</form>
<hr style="margin-top: 0px;"/>
<form id="searchForm" class="form-horizontal">
    <div class="row">
        <div class="control-group span8">
            <label class="control-label">订单号：</label>

            <div class="controls">
                <input type="text" class="control-text" name="eq_externalId">
            </div>
        </div>
        <div class="control-group span8">
            <label class="control-label">代理：</label>

            <div class="controls">
                <select name="eq_agentId">
                    <option value="">请选择</option>
                <#if agentList?has_content>
                    <#list agentList as agent>
                        <option value="${agent.agentId}" ${(requestParameters.eq_agentId?default("") == agent.agentId)?string("selected","")}>${agent.name}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
        <div class="control-group span8">
            <label class="control-label">状态：</label>

            <div class="controls">
            <#assign personStatusItemList = delegator.findByAndCache("StatusItem",{"statusTypeId":"ORDER_STATUS"})>
                <select name="eq_statusId">
                    <option value="">请选择</option>
                <#list personStatusItemList as personStatusItem>
                    <option value="${personStatusItem.statusId}" ${(requestParameters.eq_statusId?default("ORDER_PAY") == personStatusItem.statusId)?string("selected","")}>${personStatusItem.description}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="control-group span8" style="display: none">
            <label class="control-label">类型：</label>

            <div class="controls">
            <#assign orderTypeList = delegator.findByAndCache("BizOrderType",{})>
                <select name="eq_orderTypeId">
                    <option value="">请选择</option>
                <#list orderTypeList as orderType>
                    <option value="${orderType.orderTypeId}" ${(requestParameters.eq_orderTypeId?default("") == orderType.orderTypeId)?string("selected","")}>${orderType.description}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="control-group span9">
            <label class="control-label">下单时间：</label>

            <div class="controls">
                <input type="text" class=" calendar" name="ge_orderDate"><span> - </span><input name="le_orderDate" type="text" class=" calendar">
            </div>
        </div>
    </div>
    <div class="row">
        <div class="control-group span8">
            <label class="control-label">姓名：</label>

            <div class="controls">
                <input type="text" class="control-text" name="like_buyerName">
            </div>
        </div>

        <div class="span3 offset2">
            <button type="button" id="btnSearch" class="button button-primary">搜索</button>
            <button type="reset" class="btn">重置</button>
        </div>
    </div>
</form>

<div class="search-grid-container">
    <div id="grid"></div>
</div>

<div id="contentManualOrder" class="hide">
    <form class="form-horizontal">
        <div class="row">
            <div class="control-group span15">
                <label for="buyerProvince" class="control-label"><s>*</s>收货人所在省</label>

                <div class="controls">
                    <input id="buyerProvince" name="buyerProvince" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerCity" class="control-label"><s>*</s>收货人所在市</label>

                <div class="controls">
                    <input id="buyerCity" name="buyerCity" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerRegion" class="control-label"><s>*</s>收货人所在区</label>

                <div class="controls">
                    <input id="buyerRegion" name="buyerRegion" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerAddress" class="control-label"><s>*</s>收货地址</label>

                <div class="controls">
                    <input id="buyerAddress" name="buyerAddress" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerPost" class="control-label">邮政编码</label>

                <div class="controls">
                    <input id="buyerPost" name="buyerPost" type="text" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerName" class="control-label"><s>*</s>买家姓名</label>

                <div class="controls">
                    <input id="buyerName" name="buyerName" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label for="buyerPhone" class="control-label"><s>*</s>收货人电话</label>

                <div class="controls">
                    <input id="buyerPhone" name="buyerPhone" type="text" data-rules="{required:true}" class="input-large control-text">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>商品名称</label>

                <div class="controls">
                <#assign skuEnumerationList = delegator.findByAndCache("Enumeration",{"enumTypeId":"SKU_NAME"})>
                    <select name="skuMerchantCode" data-rules="{required:true}">
                        <option value="">请选择</option>
                    <#list skuEnumerationList as skuEnumeration>
                        <option value="${skuEnumeration.enumCode}">${skuEnumeration.description}</option>
                    </#list>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>商品总数</label>

                <div class="controls">
                    <input name="totalQuantity" type="text" data-rules="{required:true}" class="input-normal control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>单价</label>

                <div class="controls">
                    <input name="price" type="text" data-rules="{required:true}" class="input-normal control-text">
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span15">
                <label class="control-label">代理</label>

                <div class="controls">
                    <select name="agentId" class="input-large">
                        <option value="">请选择</option>
                    <#if agentList?has_content>
                        <#list agentList as agent>
                            <option value="${agent.agentId}">${agent.name} ${agent.phone}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>
        </div>

    </form>
</div>

<div id="content" class="hide">
    <form id="J_Form" class="form-horizontal">
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>订单号</label>

                <div class="controls">
                    <input name="externalId" type="text" data-rules="{required:true}" class="input-normal control-text" readonly>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>订单内容</label>

                <div class="controls">
                    <input name="orderItemInfo" type="text" data-rules="{required:true}" class="input-normal control-text" readonly>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>商品总数</label>

                <div class="controls">
                    <input name="totalQuantity" type="text" data-rules="{required:true}" class="input-normal control-text" readonly>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>金额</label>

                <div class="controls">
                    <input name="price" type="text" data-rules="{required:true}" class="input-normal control-text" readonly>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span8">
                <label class="control-label"><s>*</s>状态</label>

                <div class="controls">
                    <input name="statusDesc" type="text" data-rules="{required:true}" class="input-normal control-text" readonly>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span15">
                <label class="control-label">买家留言</label>

                <div class="controls">
                    <input name="buyerNote" type="text" class="input-large control-text" readonly>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span15">
                <label class="control-label">卖家留言</label>

                <div class="controls">
                    <input name="sellerNote" type="text" class="input-large control-text" readonly>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>代理</label>

                <div class="controls">
                    <select name="agentId" class="input-large">
                        <option value="">请选择</option>
                    <#if agentList?has_content>
                        <#list agentList as agent>
                            <option value="${agent.agentId}">${agent.name} ${agent.phone}</option>
                        </#list>
                    </#if>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>收货人所在省</label>

                <div class="controls">
                    <input name="buyerProvince" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>收货人所在市</label>

                <div class="controls">
                    <input name="buyerCity" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>收货人所在区</label>

                <div class="controls">
                    <input name="buyerRegion" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>收货地址</label>

                <div class="controls">
                    <input name="buyerSelfAddress" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label">邮政编码</label>

                <div class="controls">
                    <input name="buyerPost" type="text" class="input-large control-text">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>买家姓名</label>

                <div class="controls">
                    <input name="buyerName" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="control-group span15">
                <label class="control-label"><s>*</s>收货人电话</label>

                <div class="controls">
                    <input name="buyerPhone" type="text" class="input-large control-text" data-rules="{required:true}">
                </div>
            </div>
        </div>
    </form>
</div>


<script>
    BUI.use(['common/search', 'common/page', 'bui/form'], function (Search, Page, Form) {

        var orderStatusJson = ${StringUtil.wrapString(Static['org.weidian.constant.order.OrderStatus'].toJson()!)};
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
                    //var form = $('#J_Form'); //也可以直接使用表单同步提交的方式
                    //alert(form.isValid());
                    //form.submit();
                }
            }
        });
        var editingManualOrder = new BUI.Grid.Plugins.DialogEditing({
            contentId: 'contentManualOrder', //设置隐藏的Dialog内容
            editor: {
                success: function () { //点击确认的时候触发，可以进行异步提交
                    var editor = this;
                    var record = editingManualOrder.get('record');
                    var data = editor.getValue(); //编辑完成的记录
                    var form = editingManualOrder.get('form');
                    if (form.isValid()) {
                        submitManualOrder(record, data, editor);
                    }
                    //var form = $('#J_Form'); //也可以直接使用表单同步提交的方式
                    //alert(form.isValid());
                    //form.submit();
                }
            }
        });
        var columns = [
            {
                title: '订单号', dataIndex: 'externalId', width: 115,
                renderer: function (v) {
                    var editStr = '<span class="grid-command btn-edit" title="编辑">' + v + '</span>';
                    return editStr;
                }
            },
            {title: '订单内容', dataIndex: 'orderItemInfo', width: 120},
            {title: '商品总数', dataIndex: 'totalQuantity', width: 60, renderer: app.grid.format.numberFunc(0)},
            {title: '金额', dataIndex: 'price', width: 50},
            {title: '状态', dataIndex: 'statusId', width: 60, renderer: BUI.Grid.Format.enumRenderer(orderStatusJson)},
            {title: '买家留言', dataIndex: 'buyerNote', width: 160},
            {title: '卖家留言', dataIndex: 'sellerNote', width: 100},
            {title: '代理', dataIndex: 'agentName', width: 100},
//            {title: '寄件人姓名', dataIndex: 'senderName', width: 100},
//            {title: '寄件人电话', dataIndex: 'senderPhone', width: 100},
            {title: '下单时间', dataIndex: 'orderDate', width: 126, renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '快递单号', dataIndex: 'expressNo', width: 100}
        ];
        columns.push({
            title: '收货信息', dataIndex: 'buyerAddress',
            renderer: function (value, obj) {
                return obj.buyerName + " " + obj.buyerPhone + " " + obj.buyerAddress;
            },
            width: app.grid.getLastColumnWidth(columns, 300)
        });

        var store = Search.createStore('<@ofbizUrl>gridOrder</@ofbizUrl>', {
            pageSize: 15
        });
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
//                    {text: '添加线下订单', btnCls: 'button button-small', handler: addManualOrder},
//                    {text: '<i class="icon-remove"></i>删除', btnCls: 'button button-small', handler: delFunction},
                    {text: '导出筛选的待发货订单', btnCls: 'button button-small', handler: exportPaidOrderDeliveryAddress}
                ]
            },
            plugins: [editing, editingManualOrder, BUI.Grid.Plugins.CheckSelection] // 插件形式引入多选表格
        });

        var search = new Search({
            store: store,
            gridCfg: gridCfg
        });
        var grid = search.get('grid');

        function submit(record, data, editor) {
            data.orderId = record.orderId;
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>updateOrder</@ofbizUrl>',
                data: data,
                success: function (data) {
                    if (app.ajaxHelper.handleAjaxMsg(data)) {
                        editor.accept();
                        search.load();
                    }
                }
            });
        }

        function submitManualOrder(record, data, editor) {
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>createManualOrder</@ofbizUrl>',
                data: data,
                success: function (data) {
                    if (app.ajaxHelper.handleAjaxMsg(data)) {
                        editor.accept();
                        search.load();
                    }
                }
            });
        }

        //添加线下订单
        function addManualOrder() {
            editingManualOrder.add({},'name');
        }

        //删除操作
        function delFunction() {
            var selections = grid.getSelection();
            delItems(selections);
        }

        function delItems(items) {
            var ids = [];
            BUI.each(items, function (item) {
                ids.push(item.personId);
            });

            if (ids.length) {
                BUI.Message.Confirm('确认要删除选中的记录么？', function () {
                    $.ajax({
                        type: 'post',
                        url: '<@ofbizUrl>deleteOrder</@ofbizUrl>',
                        dataType: 'json',
                        data: {ids: ids},
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

        function exportPaidOrderDeliveryAddress() {
            var formId = "searchForm";
            var str = $("#" + formId).serialize();

            window.location.href = "<@ofbizUrl>exportPaidOrderDeliveryAddress</@ofbizUrl>?"+str;
        }
    });
</script>