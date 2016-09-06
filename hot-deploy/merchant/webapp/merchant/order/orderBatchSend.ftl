<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel"></div>
    </div>
</div>

<script type="text/javascript">
    BUI.use('bui/tab', function (Tab) {
        var json = [{
            text: '订单发货记录',
            loader: {url: '<@ofbizUrl>listFileUploadOrderBatchSend</@ofbizUrl>'}
        }, {
            text: '数据上传记录',
            loader: {url: '<@ofbizUrl>listFileUpload</@ofbizUrl>?fileUploadTypeId='}
        }];

        var tab = new Tab.TabPanel({
            render: '#tab',
            elCls: 'nav-tabs',
            panelContainer: '#panel',
            autoRender: true,
            children: json
        });
        tab.setSelected(tab.getItemAt(0));
    });
</script>
<#include "/extcommon/webapp/extcommon/includes/uploader.ftl"/>