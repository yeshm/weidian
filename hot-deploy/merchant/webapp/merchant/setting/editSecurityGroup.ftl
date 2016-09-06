<style type="text/css">
    .bui-list-item-selected {
        font-size: 14px;
        font-weight: bold;
    }
</style>
<div class="box-title">
    <div class="row-fluid">
        <div class="span20">
            <label class="crumbs-box">
                <a href="javascript:toListSecurityGroup()">权限组列表</a>
                <em>>></em>
                <span>
                <#if securityGroup?has_content>
                    编辑权限组
                <#else>
                    新增权限组
                </#if>
                </span>
            </label>
        </div>
    </div>
</div>

<div class="box-content">
    <div class="row-fluid">
        <form id="J_Form" class="form-horizontal" method="post" action="<@ofbizUrl>saveSecurityGroup</@ofbizUrl>">
            <input type="hidden" name="groupId" value="${(securityGroup.groupId)!}">

            <div class="control-group">
                <label class="control-label" for="description"><s>*</s>权限组名称：</label>

                <div class="controls">
                    <input type="text" id="description" name="description" value="${(securityGroup.description)!}" class="input-large" data-rules="{required:true}">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">选择权限：</label>
                <label><input type="checkbox" id="more">全选</label>

                <div class="controls control-row-auto">
                    <div class="privilege-box">
                        <div class="privilege-listbox">
                            <div class="col col1">
                                <div class="hd">系统模块</div>
                                <div class="bd" id="list1"></div>
                            </div>
                            <div class="col col2">
                                <div class="hd">系统子模块</div>
                                <div class="bd">
                                    <div id='list2' class='bui-simple-list bui-select-list' aria-disabled='false' aria-pressed='false'></div>
                                </div>
                            </div>
                            <div class="col col3">
                                <div class="hd">操作权限</div>
                                <div class="bd">
                                    <div id="list3" class='bui-simple-list bui-select-list' aria-disabled='false' aria-pressed='false'></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="actions-bar">
                <button type="submit" class="button button-primary">保存</button>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    var selectedPermissionIdList = ${StringUtil.wrapString(Static["org.ofbiz.ext.util.AppUtil"].toJson(selectedPermissionIdList!"[]"))};
    BUI.use('bui/form', function (Form) {
        new Form.Form({
            srcNode: '#J_Form',
            submitType: 'ajax',
            callback: function (data) {
                if (app.ajaxHelper.handleAjaxMsg(data)) {
                    app.showSuccess("保存成功", {
                        callback: function () {
                            toListSecurityGroup();
                        }
                    });
                }
            }
        }).render().on("beforesubmit", function () {
                    if ($(":checkbox:checked").size() == 0) {
                        app.showError("至少选择一项权限！");
                        return false;
                    }
                });
    });

    function toListSecurityGroup() {
        app.page.openList({
            id: 'SETTING_ACCOUNT_SECURITY_GROUP'
        });
        return false;
    }

    $(function () {
        $('#list1').html("请稍等，系统正在加载中... ...");
        $('#list2').html("请稍等，系统正在加载中... ...");
        $('#list3').html("请稍等，系统正在加载中... ...");

        app.ajaxHelper.submitRequest({
            url: '<@ofbizUrl>editSecurityGroupQueryPermission</@ofbizUrl>',
            data: {"permissionId": ""},
            loadingMask: false,
            success: function (d) {
                if (app.ajaxHelper.handleAjaxMsg(d)) {
                    var permissionList = d.data.permissionList;
                    var item = "<div id='oneMenuDiv' class='bui-simple-list bui-select-list' aria-disabled='false' aria-pressed='false'><ul>";
                    var itemTwo = "<div class='twoDiv' id='twoDiv'><ul>";
                    var itemThree = "<div class='threeDiv' id='threeDiv'><ul>";
                    if (!app.validator.isEmpty(permissionList)) {
                        $('#list1').html("");
                        $('#list2').html("");
                        $('#list3').html("");
                        for (var i = 0; i < permissionList.length; i++) {
                            var level = permissionList[i]['permissionLevel'];
                            var id = permissionList[i]['permissionId'];
                            var flag = ($.inArray(id, selectedPermissionIdList) >= 0) ? 1 : 0;
                            var name = permissionList[i]['description'];
                            var parentId = permissionList[i]['parentPermissionId'];
                            if (level == "1") {
                                if (flag == "1") {
                                    item += "<li id='li_" + id + "' class='bui-list-item' role='option'><input id='" + id + "' type='checkbox' name='permissionId' checked='checked' onclick='checkBox(\"" + id + "\")'  value='" + id + "'>&nbsp;<label id='oneLabel" + id + "' onclick='checkDiv(\"" + id + "\")'>" + name + "</label></li>";
                                } else {
                                    item += "<li id='li_" + id + "' class='bui-list-item' role='option'><input id='" + id + "' type='checkbox' name='permissionId' onclick='checkBox(\"" + id + "\")' value='" + id + "'>&nbsp;<label id='oneLabel" + id + "' onclick='checkDiv(\"" + id + "\")'>" + name + "</label></li>";
                                }
                            } else if (level == "2") {
                                if (flag == "1") {
                                    itemTwo += "<li id='li_" + id + "' dataValue='" + parentId + "' class='bui-list-item' role='option'><input checked='checked' type='checkbox' dataValue='" + parentId + "' id='" + id + "' name='permissionId' onclick='checkAll(\"" + parentId + "\",\"" + id + "\")'  value='" + id + "'>&nbsp;<label onclick='checkDiv2(\"" + id + "\")'>" + name + "</label></li>";
                                } else {
                                    itemTwo += "<li id='li_" + id + "' dataValue='" + parentId + "' class='bui-list-item' role='option'><input type='checkbox'  dataValue='" + parentId + "' id='" + id + "' name='permissionId' onclick='checkAll(\"" + parentId + "\",\"" + id + "\")'  value='" + id + "'>&nbsp;<label onclick='checkDiv2(\"" + id + "\")'>" + name + "</label></li>";
                                }
                            } else if (level == "3") {
                                if (flag == "1") {
                                    itemThree = itemThree + "<li id='li_" + id + "'dataValue='" + parentId + "' class='bui-list-item' role='option'><input checked='checked' id='" + id + "'  onclick='checkThreeAll(\"" + parentId + "\",\"" + id + "\")' type='checkbox' name='permissionId'  value='" + id + "' dataValue='" + parentId + "'>&nbsp;<label dataValue=" + parentId + " id=lab" + id + ">" + name + "</label></li>";
                                } else {
                                    itemThree = itemThree + "<li id='li_" + id + "'dataValue='" + parentId + "' class='bui-list-item' role='option'><input id='" + id + "'  onclick='checkThreeAll(\"" + parentId + "\",\"" + id + "\")' type='checkbox' name='permissionId'  value='" + id + "' dataValue='" + parentId + "'>&nbsp;<label dataValue=" + parentId + " id=lab" + id + ">" + name + "</label></li>";
                                }
                            }
                        }
                        itemTwo += "<ul></div>"
                        itemThree += "</ul></div>"
                        item += "</ul></div>"
                        $('#list1').append(item);
                        $('#list2').append(itemTwo);
                        $('#list3').append(itemThree);
                        $('#oneLabelHOME').click();
                    }
                }
            }
        });

        $("#more").click(function () {
            if ($(this).prop("checked")) {
                $("#list1 input:checkbox").each(function (index) {
                    $(this).prop("checked", true);
                });
                $("#list2 input:checkbox").each(function (index) {
                    $(this).prop("checked", true);
                });
                $("#list3 input:checkbox").each(function (index) {
                    $(this).prop("checked", true);
                });
            } else {
                $("#list1 input:checkbox").each(function (index) {
                    $(this).prop("checked", false);
                });
                $("#list2 input:checkbox").each(function (index) {
                    $(this).prop("checked", false);
                });
                $("#list3 input:checkbox").each(function (index) {
                    $(this).prop("checked", false);
                });
            }
        });

    })

    function selectOneDiv(id) {
        $("#oneMenuDiv li").each(function (index) {
            $(this).attr("class", "bui-list-item");
        });
        $('#li_' + id).attr("class", "bui-list-item bui-list-item-selected");
    }
    function selectTwoDiv(id) {
        $("#twoDiv li").each(function (index) {
            $(this).attr("class", "bui-list-item");
        });
        $('#li_' + id).attr("class", "bui-list-item bui-list-item-selected")
    }
    function checkDiv(id) {
        var threeId;
        var i = 0;
        $("#twoDiv li").each(function (index) {
            var dataValue = $(this).attr("dataValue");
            if (id == dataValue) {
                $(this).show();
                if (i == 0) {
                    var str = $(this).attr("id");
                    threeId = str.substring(3, str.length);
                    i++;
                }
            } else {
                $(this).hide();
            }
        });
        selectOneDiv(id);
        checkDiv2(threeId);
    }

    function checkDiv2(id) {
        $("#threeDiv li").each(function (index) {
            var dataValue = $(this).attr("dataValue");
            if (id == dataValue) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
        selectTwoDiv(id);
    }

    function checkBox(id) {
        selectOneDiv(id);
        var threeId;
        var q = 0;
        $("#twoDiv li").each(function (index) {
            var dataValue = $(this).attr("dataValue");
            if (id == dataValue) {
                var str = $(this).attr("id");
                threeId = str.substring(3, str.length);
                $(this).show();
                if (document.getElementById(id).checked) {
                    $('#' + threeId).prop("checked", true);
                } else {
                    $('#' + threeId).prop("checked", false);
                }
                //三级菜单
                var threeIds;
                var indexId;
                $("#threeDiv input:checkbox").each(function (index) {
                    var dataValue = $(this).attr("dataValue");
                    if (threeId == dataValue) {
                        var id = $(this).attr("id");
                        if (q == 0) {
                            indexId = dataValue;
                        }
                        if (document.getElementById(threeId).checked) {
                            $('#' + id).prop("checked", true);
                        } else {
                            $('#' + id).prop("checked", false);
                        }
                    }
                });
                if (q == 0) {
                    checkDiv2(indexId);
                    q++;
                }
            } else {
                $(this).hide();
            }
        });
    }


    function checkAll(parentId, id) {
        selectTwoDiv(id);
        $("#threeDiv li").each(function (index) {
            var dataValue = $(this).attr("dataValue");
            if (id == dataValue) {
                $(this).show();
                var strs = $(this).attr("id");
                var threeIds = strs.substring(3, strs.length);
                if (document.getElementById(id).checked) {
                    $('#' + threeIds).prop("checked", true);
                } else {
                    $('#' + threeIds).prop("checked", false);
                }
            } else {
                $(this).hide();
            }
        });
        //判断同级是否有选中
        var flag = 0;
        $("#twoDiv input[type=checkbox]:checked").each(function (index) {
            var dateValues = $(this).attr("dataValue");
            var dataValue = $('#' + id).attr("dataValue");
            if (dataValue == dateValues) {
                flag++;
            }
        });

        if (flag == 0) {
            $("#" + parentId).prop("checked", false);
        } else {
            $("#" + parentId).prop("checked", true);
        }
    }

    function checkThreeAll(parentId, id) {
        var flag = 0;
        $("#threeDiv input[type=checkbox]:checked").each(function (index) {
            var dateValues = $(this).attr("dataValue");
            var dataValue = $('#' + id).attr("dataValue");
            if (dataValue == dateValues) {
                flag++;
            }
        });
        if (flag == 0) {
            $("#" + parentId).prop("checked", false);
        } else {
            $("#" + parentId).prop("checked", true);
        }
        //判断上级
        var ppid = $("#" + parentId).attr("dataValue");
        var flags = 0;
        $("#twoDiv input[type=checkbox]:checked").each(function (index) {
            var dateValues = $(this).attr("dataValue");
            if (ppid == dateValues) {
                flags++;
            }
        });
        if (flags == 0) {
            $("#" + ppid).prop("checked", false);
        } else {
            $("#" + ppid).prop("checked", true);
        }

        //查看必选
        $("#threeDiv label").each(function (index) {
            var labelDataValue = $(this).attr("dataValue");
            if ($('#' + id).attr("dataValue") == labelDataValue) {
                if ($(this).html() == "查看") {
                    if (document.getElementById(id).checked) {
                        if (id != $(this).attr("id").substring(3, $(this).attr("id").length)) {
                            $("#" + $(this).attr("id").substring(3, $(this).attr("id").length)).prop("checked", true);
                        }
                    }
                }
            }
        });
    }
</script>