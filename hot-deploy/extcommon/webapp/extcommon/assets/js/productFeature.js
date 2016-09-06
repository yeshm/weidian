BUI.use(['bui/grid', 'bui/data'], function (List) {

});

var gridColumnDataIndexPrefix = "product_feature";
function ProductVariantBuilder(renderTarget) {
    this.render = renderTarget;
    this.productVariantProductFeatureArray = [];
    this.columns = [];
    this.productVariantGridStore = [];
    this.productVariantGrid = null;
    this.productVariantGridData = [];

    var Data = BUI.Data;
    var Store = Data.Store;
    this.productVariantGridStore = new Store({
        data: this.productVariantGridData,
        autoLoad: true
    });
};

ProductVariantBuilder.prototype.addProductFeature = function (label, dataIndex) {
    if (!dataIndex) {
        dataIndex = label;
    }
    var productFeature = {
        label: label,
        dataIndex: dataIndex
    }

    this.productVariantProductFeatureArray.push(productFeature);
};

ProductVariantBuilder.prototype.addProductFeatures = function (labels, productFeatureIds) {
    for (var i = 0; i < labels.length; i++) {
        var label = labels[i];
        var dataIndex;
        if (productFeatureIds) {
            dataIndex = gridColumnDataIndexPrefix + productFeatureIds[i];
        } else {
            dataIndex = label;
        }
        this.addProductFeature(label, dataIndex);
    }
};

ProductVariantBuilder.prototype.reset = function () {
    this.resetProductFeatureArray();
    this.resetStore();
    this.resetColumns();
};

ProductVariantBuilder.prototype.resetStore = function () {
    var productVariantRecords = this.productVariantGridStore.getResult();
    if (productVariantRecords) {
        for (var i = 0; i < productVariantRecords.length; i++) {
            var productVariantFeatureIdKey = productVariantRecords[i].id;
            var child = form.getChild("productVariantInventoryQuantity_" + productVariantFeatureIdKey);
            form.removeChild(child, true);

            child = form.getChild("productVariantDefaultPrice_" + productVariantFeatureIdKey);
            form.removeChild(child, true);

            child = form.getChild("productVariantWholeSalePrice_" + productVariantFeatureIdKey);
            form.removeChild(child, true);
        }
    }

    this.productVariantGridStore.setResult([]);
};

ProductVariantBuilder.prototype.resetColumns = function () {
    this.columns = [];
};

/**
 *重置规格数组
 *
 */
ProductVariantBuilder.prototype.resetProductFeatureArray = function () {
    this.productVariantProductFeatureArray = [];
};

ProductVariantBuilder.prototype.getProductFeatures = function () {
    return this.productVariantProductFeatureArray;
};

ProductVariantBuilder.prototype.getProductFeaturesCount = function () {
    return this.productVariantProductFeatureArray.length;
};

ProductVariantBuilder.prototype.getProductFeatureByIndex = function (index) {
    return this.productVariantProductFeatureArray[index];
};

ProductVariantBuilder.prototype.buildGridColumn = function () {
    var idColumnStr = "{title : 'ID', dataIndex :'id', visible:false}";
    this.columns.push(eval('(' + idColumnStr + ')'));
    for (var i = 0; i < this.getProductFeaturesCount(); i++) {
        var productFeatureObj = this.getProductFeatureByIndex(i);
        var titleTip = productFeatureObj.label;
        var dataIndex = productFeatureObj.dataIndex;

        var columnStr = "  { title : '" + titleTip + "', dataIndex :'" + dataIndex + "', width:200}";

        this.columns.push(eval('(' + columnStr + ')'));
    }

    this.columns.push({
        title: '销售价格', dataIndex: 'productVariantDefaultPrice', width: 220, renderer: function (value, obj) {
            value = value || '';
            return '<input type="text" id="productVariantDefaultPrice_' + obj.id + '" data-rules="{required: true}" name="defaultPrice1" value="' + value + '"/>';
        }
    })
    this.columns.push({
        title: '成本价', dataIndex: 'productVariantWholeSalePrice', width: 220, renderer: function (value, obj) {
            value = value || '';
            return '<input type="text" id="productVariantWholeSalePrice_' + obj.id + '" data-rules="{required: true}" name="wholeSalePrice1" value="' + value + '"/>';
        }
    })

    this.columns.push({
        title: '库存', dataIndex: 'productVariantInventoryQuantity', width: 220, renderer: function (value, obj) {
            value = value || '';
            var str = '<input type="text" id="productVariantInventoryQuantity_' + obj.id + '" name="inventoryQuantity" data-rules="{required: true}" value="' + value + '"/>';
            return str;
        }
    })

    console.log('productFeature.js:105 ProductVariantBuilder.buildGridColumn')
    console.dir(this.columns)
};

/**
 * 构造表格
 *
 */
ProductVariantBuilder.prototype.buildGrid = function () {
    console.log('productFeature.js:114 ProductVariantBuilder.buildGrid')
    console.dir(this.columns)
    console.dir(this.productVariantGridStore.getResult())

    var Grid = BUI.Grid;
    var Data = BUI.Data;
    var Store = Data.Store;
    this.productVariantGridStore = new Store({
        data: this.productVariantGridData,
        autoLoad: true
    });
    this.productVariantGrid = new Grid.Grid({ //使用简单表格
        render: '#' + this.render,
        width: '100%',//这个属性一定要设置
        columns: this.columns,
        /*tableCls:'table table-bordered',  //定义表格样式*/
        store: this.productVariantGridStore
    });
    this.productVariantGrid.on('itemrendered', function (ev) {
        var item = ev.item;
        var itemEl = $(ev.element);

        var inputs = itemEl.find('input');
        if (inputs) {
            BUI.each(inputs, function (input) {
                form.addChild({
                    xclass: 'form-field',
                    errorTpl: '<span class="x-icon x-icon-small x-icon-error" title="{error}">!</span>',
                    srcNode: input
                });
            });
        }
    });
};

ProductVariantBuilder.prototype.renderGrid = function () {
    console.log('productFeature.js:150 this.productVariantGridStore.renderGrid()')
    this.productVariantGrid.render();
};

ProductVariantBuilder.prototype.loadStore = function (data) {
    this.resetStore();

    var targetNewDataIds = {};
    for (var i = 0; i < data.length; i++) {
        var newId = data[i].id;
        targetNewDataIds[newId] = newId;
    }
    var storeResultArray = this.productVariantGridStore.getResult();
    var deletedRecords = [];
    for (var i = 0; i < storeResultArray.length; i++) {
        var storeResult = storeResultArray[i];
        var compareId = storeResult.id;
        if (!targetNewDataIds[compareId] && !storeResult.serverProductVariant) {
            deletedRecords.push(storeResult);
        }
    }
    if (this.productVariantGridStore.remove) this.productVariantGridStore.remove(deletedRecords);

    var count = this.productVariantGridStore.getCount();
    this.productVariantGridStore.addAt(data, count, true, function (obj1, obj2) {
        return obj1.id == obj2.id;
    });
    console.log('productFeature.js:170 this.productVariantGridStore.loadStore()')
    console.dir(this.productVariantGridStore.getResult())
};

ProductVariantBuilder.prototype.containRecord = function (productVariantId) {
    var obj = new Object();
    obj.id = productVariantId;
    this.productVariantGridStore.contains(obj, function (obj1, obj2) { //使用匹配函数
        return obj1.id == obj2.id;
    });
};

ProductVariantBuilder.prototype.getAllRecords = function () {
    return this.productVariantGridStore.getResult();
};


var productFeaturePicGridColumnDataIndexPrefix = "product_feature_color_";
function ProductFeaturePicGridBuilder(renderTarget, productFeatureId, productFeatureLabel) {
    this.render = renderTarget;
    this.productFeatureId = productFeatureId;
    this.productFeatureLabel = productFeatureLabel;
    this.columns = [];
    this.productFeaturePicGridStore = [];
    this.productFeaturePicGrid = null;
    this.productFeaturePicGridData = [];
};

ProductFeaturePicGridBuilder.prototype.reset = function () {
    this.resetStore();
    this.resetColumns();
    this.productFeaturePicGridData = [];
    this.productFeaturePicGrid = null;
};

ProductFeaturePicGridBuilder.prototype.resetStore = function () {
    this.productFeaturePicGridStore.setResult([]);
};

ProductFeaturePicGridBuilder.prototype.resetColumns = function () {
    this.columns = [];
};

ProductFeaturePicGridBuilder.prototype.buildGridColumn = function () {
    var idColumnStr = "{ title : 'ID', dataIndex :'id', visible:false}";
    this.columns.push({title: 'ID', dataIndex: 'id', visible: false});

//    var firstColumnDataIndex = productFeaturePicGridColumnDataIndexPrefix + this.productFeatureId;
    var firstColumnStr = "  { title : '" + this.productFeatureLabel + "', dataIndex :'productFeatureName', width:120}";
    this.columns.push(eval('(' + firstColumnStr + ')'));

    this.columns.push({
        title: '自定义颜色名称', dataIndex: 'productFeatureCustomName', width: 220,
        renderer: function (value, obj) {
            value = value || obj.productFeatureName;
            return '<input type="text" onchange="javascript:productFeatureItemKeyup(this);" id="productFeatureCustomName_' + obj.id + '" name="productFeatureCustomName" value="' + value + '" data-old-value="' + value + '"/>'
        }
    })

    this.columns.push({
        title: '图片(无图片可不填)', dataIndex: 'productFeatureCustomImageUrl', width: 250,
        renderer: function (value, obj) {
            var imageUrl = ((!!value) ? (app.config.imageHomeUrl + value) : '');
            var str = '<img id="productFeatureCustomImagePreview_' + obj.id + '" width="50" height="50" src="' + imageUrl + '">';
            str += '<input type="hidden" id="productFeatureCustomImageUrl_' + obj.id + '" name="productFeatureCustomImageUrl" value="' + (value || '') + '"/>';
            str += '<a id="uploadFeatureCustomImage_' + obj.id + '" class="op_uploadFeatureCustomImage">上传图片</a><a href="#" onclick="return deleteFeatureCustomImage(\'' + obj.id + '\')">删除图片</a>';
            return str;
        }
    })
};

/**
 * 构造表格
 *
 */
ProductFeaturePicGridBuilder.prototype.buildGrid = function (partyId) {
    var Grid = BUI.Grid;
    var Data = BUI.Data;
    var Store = Data.Store;
    this.productFeaturePicGridStore = new Store({
        data: this.productFeaturePicGridData,
        autoLoad: true
    });
    this.productFeaturePicGrid = new Grid.Grid({ //使用简单表格
        render: '#' + this.render,
        width: '70%',//这个属性一定要设置
        columns: this.columns,
        store: this.productFeaturePicGridStore
    });

    this.productFeaturePicGrid.on('rowcreated', function (ev, b, c, d) {
        var sender = $(ev.domTarget);
        var record = ev.record;

        app.uploadHelper.singleUploader({
            uploadButtonId: "uploadFeatureCustomImage_" + record.id,
            sign: partyId,
            showFileUrlId: "productFeatureCustomImagePreview_" + record.id,
            showFileNameId: "productFeatureCustomImageUrl_" + record.id
        });

    })

    /*this.productFeaturePicGrid.on('itemrendered',function(ev){
     var item = ev.item;
     var itemEl = $(ev.element);

     var inputs = itemEl.find('input');
     if(inputs){
     BUI.each(inputs,function(input){
     form.addChild({
     xclass : 'form-field',
     errorTpl : '<span class="x-icon x-icon-small x-icon-error" title="{error}">!</span>',
     srcNode : input
     });
     });
     }
     });*/
};

ProductFeaturePicGridBuilder.prototype.renderGrid = function () {
    this.productFeaturePicGrid.render();
};

ProductFeaturePicGridBuilder.prototype.loadStore = function (data) {
    var targetNewDataIds = {};
    for (var i = 0; i < data.length; i++) {
        var newId = data[i].id;
        targetNewDataIds[newId] = newId;
    }
    var storeResultArray = this.productFeaturePicGridStore.getResult();
    var deletedRecords = [];
    for (var i = 0; i < storeResultArray.length; i++) {
        var storeResult = storeResultArray[i];
        var compareId = storeResult.id;
        if (!targetNewDataIds[compareId]) {
            deletedRecords.push(storeResult);
        }
    }
    this.productFeaturePicGridStore.remove(deletedRecords);

    var count = this.productFeaturePicGridStore.getCount();
    this.productFeaturePicGridStore.addAt(data, count, true, function (obj1, obj2) {
        return obj1.id == obj2.id;
    });
};

ProductFeaturePicGridBuilder.prototype.containRecord = function (productFeatureId) {
    var obj = new Object();
    obj.id = productFeatureId;
    this.productFeaturePicGridStore.contains(obj, function (obj1, obj2) { //使用匹配函数
        return obj1.id == obj2.id;
    });
};

ProductFeaturePicGridBuilder.prototype.getAllRecords = function () {
    return this.productFeaturePicGridStore.getResult();
};

ProductFeaturePicGridBuilder.prototype.updateRecordPic = function (id, assetUrl, assetId) {
    var targetUpdateObj = this.productFeaturePicGridStore.find('id', id);
    if (targetUpdateObj) {
        targetUpdateObj.url = ("admin" + assetUrl);
        targetUpdateObj.assetId = assetId;
        this.productFeaturePicGridStore.update(targetUpdateObj);
    }
};

ProductFeaturePicGridBuilder.prototype.deleteRecordPic = function (id) {
    var targetUpdateObj = this.productFeaturePicGridStore.find('id', id);
    if (targetUpdateObj) {
        targetUpdateObj.url = "";
        targetUpdateObj.assetId = "";
        this.productFeaturePicGridStore.update(targetUpdateObj);
    }
};