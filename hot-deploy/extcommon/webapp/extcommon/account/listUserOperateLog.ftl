
<div class="box-content">
    <div class="row-fluid">
        <div id="tab"></div>
        <div id="panel">
           <div>
               <div class="search-grid-container">
                   <div class="title-bar-side title-bar-side3">
                       <div class="search-bar">
                           <input type="text" placeholder="操作人" class="control-text" id="fastSearchField">
                           <button><i class="icon-search"></i></button>
                       </div>
                       <div class="search-content" style="width: 380px;">
                           <form id="searchForm" class="form-horizontal search-form">
                               <div class="row">
                                   <div class="control-group span24">
                                       <label class="control-label">操作人：</label>
                                       <div class="controls">
                                           <input type="text"  name="like_operateUserLoginId" id="like_operateUserLoginId" class="input-normal">
                                       </div>
                                   </div>
                               </div>
                               <div class="row">
                                   <div class="control-group span24">
                                           <label class="control-label control-label-auto">操作时间：</label>
                                           <div class="controls">
                                               <input type="text" class=" calendar" name="ge_operateDate"><span> - </span><input
                                                   name="le_operateDate" type="text" class=" calendar">
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
    BUI.use('common/search', function (Search) {
        var columns = [
            {title: '操作人', dataIndex: 'operateUserLoginId', width: 200},
            {title: '操作时间', dataIndex: 'operateDate', width: 170, renderer: BUI.Grid.Format.datetimeRenderer},
            {title: 'IP地址', dataIndex: 'operateIp', width: 150},
            {title: '操作描述', dataIndex: 'comments', width: "100%"}
        ];
        var store = Search.createStore('<@ofbizUrl>gridUserOperateLog</@ofbizUrl>', {
            pageSize: 20,
            remoteSort: true
        });
        var gridCfg = Search.createGridCfg(columns,{ width: '100%',height: getContentHeight()});

        search = new Search({
            store: store,
            gridCfg: gridCfg
        });
        var grid = search.get('grid');
    });

    BUI.use('bui/tab',function(Tab){
        var tab = new Tab.TabPanel({
            render : '#tab',
            elCls : 'nav-tabs',
            panelContainer : '#panel',
            autoRender: true,
            children:[
                {text:'操作日志'}
            ]
        });
        tab.setSelected(tab.getItemAt(0));
    });
    app.searchForm.initSearch({
        fastSearchFieldId: "fastSearchField",
        searchFieldId: "like_productName",
        btnSearchId: "btnSearch"
    });
</script>