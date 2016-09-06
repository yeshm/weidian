<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
            <div>
                <div class="search-grid-container">
                    <div class="title-bar-side title-bar-side3">
                        <div class="search-bar">
                            <input type="text" placeholder="名称" class="control-text" id="fastSearchField">
                            <button><i class="icon-search"></i></button>
                        </div>
                        <div class="search-content">
                            <form id="searchForm" class="form-horizontal search-form">
                                <div class="row">
                                    <div class="control-group span24">
                                        <label class="control-label" for="nickname">名称：</label>

                                        <div class="controls">
                                            <input type="text" id="like_groupId" name="like_groupId" class="input-normal" value="${requestParameters.like_groupId!}"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="form-actions ml110">
                                        <button id="btnSearch" type="submit" class="button button-primary">搜索</button>
                                        <button type="reset" class="button">重置</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div id="grid"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var search;
    BUI.use('bui/tab', function (Tab) {
        var tab = new Tab.TabPanel({
            render: '#tab',
            elCls: 'nav-tabs',
            panelContainer: '#panel',
            autoRender: true,
            children: [
                {text: '权限组列表'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

    BUI.use(['common/search', 'bui/overlay'], function (Search, Overlay) {
        var columns = [
            {
                title: '操作', dataIndex: 'partyId', width: 150,
                renderer: function (value, obj) {
                    var str = '<a href="javascript:void(0)" class="op_edit" >编辑</a>';
                    str += '<a href="javascript:void(0)" class="op_delete" >删除</a>';
                    return str;
                }
            },
            {title: '名称', dataIndex: 'description', width: 200},
            {title: '创建时间', dataIndex: 'createdStamp', width: 180, renderer: BUI.Grid.Format.datetimeRenderer}
        ];

        var store = Search.createStore('<@ofbizUrl>gridSecurityGroup</@ofbizUrl>');
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
                    {
                        text: '新建', btnCls: 'button button-primary',
                        handler: function () {
                            editSecurityGroup();
                        }
                    }
                ]
            },
            width: '100%',
            height: getContentHeight()
        });
        search = new Search({
            store: store,
            gridCfg: gridCfg,
            btnId: "btnSearch"
        });
        grid = search.get('grid');
        grid.on('cellclick', function (ev) {
            var sender = $(ev.domTarget);
            var record = ev.record;
            var groupId = record.groupId;

            if (sender.hasClass('op_edit')) {
                editSecurityGroup(groupId);
                return false;
            } else if (sender.hasClass('op_delete')) {
                deleteSecurityGroup(groupId);
                return false;
            }
        });
    });

    function editSecurityGroup(groupId) {
        app.page.open({
            id: 'editSecurityGroup'+groupId,
            href: '<@ofbizUrl>editSecurityGroup</@ofbizUrl>?groupId=' + groupId,
            title: '编辑'
        });
        return false;
    }

    function deleteSecurityGroup(groupId) {
        app.confirm('确认要删除该权限组吗？', function () {
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>deleteSecurityGroup</@ofbizUrl>',
                data: {groupId: groupId},
                success: function (data) {
                    if (app.ajaxHelper.handleAjaxMsg(data)) {
                        app.showSuccess("删除成功", {
                            callback: function () {
                                search.load();
                            }
                        })
                    }
                }
            });
        });
    }

    app.searchForm.initSearch({
        fastSearchFieldId: "fastSearchField",
        searchFieldId: "like_groupId",
        btnSearchId: "btnSearch"
    });
</script>