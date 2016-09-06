
<html>
<head>
    <title>${Static["org.weidian.SystemConfigWorker"].getSystemName(delegator)!}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<#if layoutSettings.styleSheets?has_content>
<#--layoutSettings.styleSheets is a list of style sheets. So, you can have a user-specified "main" style sheet, AND a component style sheet.-->
    <#list layoutSettings.styleSheets as styleSheet>
        <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>" type="text/css"/>
    </#list>
</#if>
<#if layoutSettings.javaScripts?has_content>
    <#list layoutSettings.javaScripts as javaScript>
        <script type="text/javascript" src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>"></script>
    </#list>
</#if>
    <script type="text/javascript">
        if(!app) var app = {};
        if(!(app.config)) app.config = {};
        app.config.imageHomeUrl = "${StringUtil.wrapString(Static["org.weidian.SystemConfigWorker"].getQiNiuUrlPrefix(delegator))!}";
        app.config.qiniuIsOpen = "${Static["org.weidian.SystemConfigWorker"].isQiNiuEnable(delegator)?string("Y", "N")}";
    </script>
</head>
<body>