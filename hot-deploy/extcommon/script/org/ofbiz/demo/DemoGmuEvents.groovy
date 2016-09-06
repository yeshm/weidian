package org.ofbiz.demo

import org.ofbiz.ext.util.MessageUtil

public String demoGmuAjaxRefreshData() {
    StringBuilder sb = new StringBuilder()
    for (int i = 0; i < 1; i++) {
        sb.append("<li>" +
                "            <a href='http://www.baidu.com'>" +
                "                <img src='http://clouddebug.duapp.com/tags/2.1.1/examples/assets/refresh/xls.png'/>" +
                "                <dl>" +
                "                    <dt>英国地标“大本钟”用女王名</dt>" +
                "                    <dd class='content'>新华网深圳3月23日电（记者 赵瑞西）23日，深圳市南山区西里医院的大楼</dd>" +
                "                    <dd class='source'>来源：新浪</dd>" +
                "                </dl>" +
                "            </a>" +
                "        </li>")
    }
    println sb.toString()
    MessageUtil.saveSuccessMessage(request, sb.toString())
    return "success"
}