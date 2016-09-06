<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${Static["org.weidian.SystemConfigWorker"].getSystemName(delegator)!}</title>
<#if layoutSettings.styleSheets?has_content>
<#--layoutSettings.styleSheets is a list of style sheets. So, you can have a user-specified "main" style sheet, AND a component style sheet.-->
    <#list layoutSettings.styleSheets as styleSheet>
        <link rel="stylesheet" href="<@ofbizContentUrl>${StringUtil.wrapString(styleSheet)}</@ofbizContentUrl>"
              type="text/css"/>
    </#list>
</#if>
<#if layoutSettings.javaScripts?has_content>
    <#list layoutSettings.javaScripts as javaScript>
        <script type="text/javascript"
                src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>"></script>
    </#list>
</#if>
</head>
<body>
