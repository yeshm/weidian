<div class="header">

    <div class="dl-title">
        <span class="dl-title-text">Demo</span>
    </div>

    <div class="dl-log"><button type="button" class="button-small button-primary" id="testIntoIFrame" onclick="testIntoIFrame()">iframe测试</button>欢迎您，<span class="dl-log-user">Commander</span>

    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <ul id="J_Nav" class="nav-list ks-clear">
            <li class="nav-item">
                <div class="nav-item-inner nav-home">BUI</div>
            </li>
            <li class="nav-item">
                <div class="nav-item-inner nav-home">GMU</div>
            </li>
            <li class="nav-item">
                <div class="nav-item-inner nav-home">第三方</div>
            </li>
            <li class="nav-item">
                <div class="nav-item-inner nav-home">Ext</div>
            </li>
            <li class="nav-item">
                <div class="nav-item-inner nav-home">JS</div>
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
                id: 'demoBui',
                homePage: 'demoBuiFormBeforeSubmit',
                menu: [
                    {
                        text: '表单',
                        items: [
                            {id: 'demoBuiFormBeforeSubmit', text: '提交前操作', href: '<@ofbizUrl>demoBuiPage?page=demoBuiFormBeforeSubmit</@ofbizUrl>'},
                            {id: 'demoBuiFormGroupValidate', text: '分组验证', href: '<@ofbizUrl>demoBuiPage?page=demoBuiFormGroupValidate</@ofbizUrl>'},
                            {id: 'demoBuiFormDynamicValidate', text: '动态验证', href: '<@ofbizUrl>demoBuiPage?page=demoBuiFormDynamicValidate</@ofbizUrl>'},
                            {id: 'demoBuiIFrame', text: 'bui框架测试', href: '<@ofbizUrl>demoBuiPage?page=demoBuiIFrame</@ofbizUrl>'}
                        ]
                    },
                    {
                        text: 'Tab',
                        items: [
                            {id: 'demoBuiTab', text: 'Tab', href: '<@ofbizUrl>demoBuiPage?page=demoBuiTab</@ofbizUrl>'},
                            {id: 'demoBuiTabRepeat', text: 'TabRepeat', href: '<@ofbizUrl>demoBuiPage?page=demoBuiTabRepeat</@ofbizUrl>'}
                        ]
                    },
                    {
                        text: '文件上传',
                        items: [
                            {id: 'demoBuiUploader', text: '文件上传', href: '<@ofbizUrl>demoBuiPage?page=demoBuiUploader</@ofbizUrl>'},
                        ]
                    },
                    {
                        text: 'acharts',
                        items: [
                            {id: 'demoBuiAcharts', text: '基本折线图', href: '<@ofbizUrl>demoBuiPage?page=demoBuiAcharts</@ofbizUrl>'},
                        ]
                    }
                ]
            },
            {
                id: 'demoGmu',
                homePage: 'gmuRefresh',
                menu: [
                    {
                        text: 'GMU',
                        items: [
                            {id: 'gmuRefresh', text: 'gmuRefresh', href: '<@ofbizUrl>demoGmuPage?page=demoGmuRefresh</@ofbizUrl>'},
                            {id: 'demoGmuCookie', text: 'gmuCookie', href: '<@ofbizUrl>demoGmuPage?page=demoGmuCookie</@ofbizUrl>'},
                            {id: 'demoHtml5Upload', text: 'html5Upload', href: '<@ofbizUrl>demoGmuPage?page=demoHtml5Upload</@ofbizUrl>'},
                            {id: 'demoAudio', text: 'Audio', href: '<@ofbizUrl>demoGmuPage?page=demoAudio</@ofbizUrl>'}
                        ]
                    }
                ]
            },
            {
                id: 'demoTp',
                homePage: 'demoTpQrCode',
                menu: [
                    {
                        text: '第三方',
                        items: [
                            {id: 'demoTpQrCode', text: '二维码', href: '/tp/control/qrcodeIndex'},
                        ]
                    },
                    {
                        text: 'UEditor',
                        items: [
                            {id: 'demoTpUEditorCss', text: '修改UEditor内部css', href: '<@ofbizUrl>demoTpPage?page=demoTpUEditorCss</@ofbizUrl>'},
                            {id: 'demoTpUEditorFullDemo', text: '完整demo', href: '<@ofbizUrl>demoTpPage?page=demoTpUeditorFullDemo</@ofbizUrl>'}
                        ]
                    },
                    {
                        text: '百度地图',
                        items: [
                            {id: 'demoTpBMap', text: '百度地图', href: '<@ofbizUrl>demoTpPage?page=demoTpBMap</@ofbizUrl>'},
                            {id: 'demoTpBMapTest', text: '百度地图test', href: '<@ofbizUrl>demoTpPage?page=demoTpBMapTest</@ofbizUrl>'},
                            {id: 'demoTpBMap1', text: '百度地图封装', href: '<@ofbizUrl>demoTpPage?page=demoTpBMap1</@ofbizUrl>'}
                        ]
                    }
                ]
            },
            {
                id: 'demoExt',
                homePage: 'demoExtFormSerializeJson',
                menu: [
                    {
                        text: 'Ext',
                        items: [
                            {id: 'demoExtFormSerializeJson', text: 'serializeJson', href: '<@ofbizUrl>demoExtPage?page=demoExtFormSerializeJson</@ofbizUrl>'},
                            {id: 'demoExtGeo', text: '地区级联', href: '<@ofbizUrl>demoExtPage?page=demoExtGeo</@ofbizUrl>'},
                            {id: 'demoExtZclib', text: 'web剪贴板', href: '<@ofbizUrl>demoExtPage?page=demoExtZclib</@ofbizUrl>'},
                            {id: 'demoExtSwfupload', text: 'swfupload', href: '<@ofbizUrl>demoExtPage?page=demoExtSwfupload</@ofbizUrl>'},
                            {id: 'demoExtUploadHelperSingleUploader', text: '单文件上传', href: '<@ofbizUrl>demoExtPage?page=demoExtUploadHelperSingleUploader</@ofbizUrl>'},
                            {id: 'demoExtUploadHelperSingleUploaderCallBack', text: '单文件上传回调', href: '<@ofbizUrl>demoExtPage?page=demoExtUploadHelperSingleUploaderCallBack</@ofbizUrl>'},
                            {id: 'demoExtPdfExport', text: 'pdf导出', href: '<@ofbizUrl>demoExtPdfExport</@ofbizUrl>'}
                        ]
                    }
                ]
            },
            {
                id: 'demoJS',
                homePage: 'demoJSHtml5Upload',
                menu: [
                    {
                        text: 'JS',
                        items: [
                            {id: 'demoJSHtml5Upload', text: 'html5Upload', href: '<@ofbizUrl>demoJsPage?page=demoJSHtml5Upload</@ofbizUrl>'},
                            {id: 'demoJSLocationSearch', text: 'locationSearch', href: '<@ofbizUrl>demoJsPage?page=demoJSLocationSearch</@ofbizUrl>'}
                        ]
                    }
                ]
            }
        ];
        new PageUtil.MainPage({
            modulesConfig: config
        });
    });


    function testIntoIFrame() {
        $(".tab-content").each(function(){
            if ($(this).css("display") == "block") {
                var iframeObj = $(this).find("iframe")[0];
                var doc = iframeObj.contentDocument || iframeObj.document;
                var val = $("#testTextArea", doc).val();
                alert(val)
            }
        })
    }
</script>