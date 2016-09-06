<div>
    <div class="search-grid-container">
        <div id="fileUploadGrid"></div>
    </div>
</div>

<script type="text/javascript">
    var fileUploadSearch;
    var taskStatusJson = ${StringUtil.wrapString(Static['org.weidian.constant.common.ProcessStatus'].toJson()!)};
    BUI.use(['common/search', 'bui/overlay', 'bui/grid'], function (Search, Overlay, Grid) {
        var columns = [
            {
                title: '操作', dataIndex: 'fileUrl', width: 65,
                renderer: function (value, obj) {
                    var str = "<a href='"+value+"'>下载</a>";
                    return str;
                }
            },
            {
                title: '处理状态', dataIndex: 'statusId', width: 100, renderer: BUI.Grid.Format.enumRenderer(taskStatusJson)
            },
            {title: '操作员', dataIndex: 'createdByUserLogin', width: 100},
            {title: '创建时间', dataIndex: 'createdDate', width: 180, renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '执行时间', dataIndex: 'executedDate', width: 180, renderer: BUI.Grid.Format.datetimeRenderer},
            {title: '完成时间', dataIndex: 'finishedDate', width: 180, renderer: BUI.Grid.Format.datetimeRenderer}
        ];
        columns.push({title: '备注', dataIndex: 'comments', width: app.grid.getLastColumnWidth(columns, 500)});

        var store = Search.createStore('<@ofbizUrl>gridFileUpload</@ofbizUrl>?taskTypeId=${requestParameters.taskTypeId!}');
        var gridCfg = Search.createGridCfg(columns, {
            tbar: {
                items: [
                    {
                        text: '刷新', btnCls: 'button button-small button-primary',
                        handler: function () {
                            fileUploadSearch.load();
                        }
                    }
                ]
            },
            width: '100%',
            height: getContentHeight()
        });

        fileUploadSearch = new Search({
            store: store,
            gridCfg: gridCfg,
            formId: "",
            gridId: 'fileUploadGrid'
        });
        var grid = fileUploadSearch.get('fileUploadGrid');
    });
</script>