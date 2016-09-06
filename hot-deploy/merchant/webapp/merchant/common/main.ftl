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
        <div class="dl-inform">
            <div class="dl-inform-title">贴心小秘书<s class="dl-inform-icon dl-up"></s></div>
        </div>
        <ul id="J_Nav" class="nav-list ks-clear">
            <li class="nav-item">
                <div class="nav-item-inner nav-order">订单</div>
            </li>
            <li class="nav-item">
                <div class="nav-item-inner nav-order">设置</div>
            </li>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
</div>
<script>
    BUI.use('common/main', function () {
        var config = [
            {
                id: 'order',
                homePage: 'listPerson',
                menu: [
                    {
                        text: '详单管理',
                        items: [

                            {id: 'listOrder', text: '所有订单', href: '<@ofbizUrl>listOrder</@ofbizUrl>'},
                            {id: 'orders', text: '所有列表', href: 'form/code.html'}
                        ]
                    },
                    {
                        text: '快递管理',
                        items: [
                            {id: 'goodsorder', text: '快递列表', href: 'form/code.html'}
                        ]
                    }
                ]
            },
            {
                id: 'setting',
                menu: [
                    {
                        text: '系统配置',
                        items: [
                            {id: 'sysconf', text: '修改密码', href: 'form/code.html'},
                            {id: 'sysconf', text: '后台用户', href: 'form/code.html'},
                            {id: 'sysconf', text: '权限分配', href: 'form/code.html'},
                            {id: 'sysconf', text: '操作日志', href: 'form/code.html'},
                            {id: 'sysconf', text: '系统配置', href: 'form/code.html'},
                            {id: 'code', text: '管理工具', href: 'form/code.html'}
                        ]
                    }
                ]
            }
        ];
        new PageUtil.MainPage({
            modulesConfig: config
        });
    });
</script>