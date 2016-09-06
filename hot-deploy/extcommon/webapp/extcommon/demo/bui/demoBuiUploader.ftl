<h2>使用js传参方式上传文件</h2>
<div class="container">
    <div class="row">
        <div class="span12">
            <div id="J_Uploader"></div>
        </div>
    </div>
    <div class="row">
        <img src="" id="demoImg">
    </div>
</div>
<script>
    BUI.setDebug(true);
    BUI.use(['bui/uploader'], function(Uploader){
        var uploader = new Uploader.Uploader({
            render: '#J_Uploader',
            url: '<@ofbizUrl>buiUpload</@ofbizUrl>',
            filter: {ext:".jpg,.jpeg,.png,.gif,.bmp"},
            success:function(d){
                //{"url":"/images/upload/temp/2013-10-21/20131021113420-1822302663.jpg","fileName":"2013-10-21/20131021113420-1822302663.jpg"}
                var el = uploader.get('el');
                 $("#demoImg").attr("src", d.url);
            }
        });
        uploader.render();
    });
</script>