<div class="header">

    <div class="dl-title">
        <span class="dl-title-text">商城管理后台</span>
    </div>

    <div class="dl-log">欢迎您，<span class="dl-log-user">${userLogin.userLoginId}</span><a
            href="<@ofbizUrl>logout</@ofbizUrl>" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
</div>

<div class="content">
    <div class="dl-main-nav">
        <ul id="J_Nav" class="nav-list ks-clear">
        <#if firstLevelMenuList?has_content>
            <#list firstLevelMenuList as firstLevelMenu>
                <#if firstLevelMenu_index != 0 && "Y" == firstLevelMenu.isNewMenuGroup!>
                    <li class="nav-line"><i></i></li>
                </#if>
                <li class="nav-item">
                    <div class="nav-item-inner">${Static["org.ofbiz.base.util.string.FlexibleStringExpander"].expandString(firstLevelMenu.description!, context)}</div>
                </li>
            </#list>
        </#if>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-content"></ul>
</div>

<script type="text/javascript">
    BUI.use('common/main', function () {
        var config = ${StringUtil.wrapString((menuJsonArray.toString())!"[]")};
        new PageUtil.MainPage({
            modulesConfig: config
        });
    });

    function updatePassword() {
        app.page.open({
            id: 'updatePassword'
        });
    }
</script>