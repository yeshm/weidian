<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
            <div>
                <div class="search-grid-container">
                    <div class="title-bar-side title-bar-side3">
                        <div class="search-bar">
                            <input type="text" placeholder="用户名" class="control-text" id="fastSearchField">
                            <button><i class="icon-search"></i></button>
                        </div>
                        <div class="search-content">
                            <form id="searchForm" class="form-horizontal search-form">
                                <div class="row">
                                    <div class="control-group span24">
                                        <label class="control-label" for="nickname">用户名：</label>

                                        <div class="controls">
                                            <input type="text" id="like_userLoginId" name="like_userLoginId" class="input-normal" value="${requestParameters.like_userLoginId!}"/>
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
                {text: '管理员列表'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });

    BUI.use(['common/search', 'bui/overlay'], function (Search, Overlay) {
        var columns = [
            {
                title: '操作', dataIndex: 'partyId', width: 150, renderer: function (value, obj) {
                var str = "<a  href='javascript:void(0)' onclick='editUserLogin(\"" + value + "\",\"" + obj.userLoginId + "\")'>修改</a>";
                str = str + "<a  href='javascript:void(0)' onclick='resetPassword(\"" + obj.userLoginId + "\")'>重置密码</a>";
                str = str + "<a  href='javascript:void(0)' onclick='deleteUserLogin(\"" + obj.userLoginId + "\")'>删除</a>";
                return str;
            }
            },
            {title: '用户名', dataIndex: 'userLoginId', width: 200},
            {title: '所属权限组', dataIndex: 'description', width: 200},
            {title: '创建时间', dataIndex: 'createdStamp', width: 180, renderer: BUI.Grid.Format.datetimeRenderer}
        ];

        var store = Search.createStore('<@ofbizUrl>gridUserLogin</@ofbizUrl>');
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
                    {
                        text: '新建', btnCls: 'button button-primary', handler: function () {
                        editUserLogin('', '');
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
    });

    function editUserLogin(partyId, userLoginId) {
        app.page.open({
            id: 'editParty',
            href: '<@ofbizUrl>editUserLogin</@ofbizUrl>?partyId=' + partyId + '&userLoginId=' + userLoginId,
            title: '编辑个人资料'
        });
        return false;
    }

    function deleteUserLogin(userLoginId) {
        app.confirm('确认要删除该账号吗？', function () {
            app.ajaxHelper.submitRequest({
                url: '<@ofbizUrl>deleteUserLogin</@ofbizUrl>',
                data: {userLoginId: userLoginId},
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
        searchFieldId: "like_userLoginId",
        btnSearchId: "btnSearch"
    });
</script>
<#include "/extcommon/webapp/extcommon/common/resetPassword.ftl"/>