<!DOCTYPE html>
<html>
<head>
    <title>ling.html5upload|www.xxling.com</title>
    <script src="/ext/assets/mobile/html5-upload.js" type="text/javascript"></script>
    <script type="text/javascript">
        window.onload = function () {
            app.html5Upload.init({
                //fileInputId: "fileInput",
                selectButtonId: "selectButton",
                //uploadButtonId: "uploadButton",
                //fileProgressContainerId: "processContainer",
                fileTypes: "|.jpg|.jpeg|.png|",
                fileSize: 200 * 1024, //这里的限制 要跟服务端对文件大小的限制一致 或者 小于服务器文件大小的限制
                url: '<@ofbizUrl>omUploadFile</@ofbizUrl>',
                callback: function (d) {
                    if (d.result == "error") {
                        alert(d.message);
                        return false
                    }
                    var fileData = d.data[0];//是个数组
                    $("#img").attr("src", fileData.fileUrl).show()
                }
            });

            app.html5Upload.init({
                fileInputId: "fileInput2",
                //selectButtonId: "selectButton",
                //uploadButtonId: "uploadButton",
                //fileProgressContainerId: "processContainer",
                fileTypes: "|.jpg|.jpeg|.png|",
                fileSize: 200 * 1024, //这里的限制 要跟服务端对文件大小的限制一致 或者 小于服务器文件大小的限制
                url: '<@ofbizUrl>omUploadFile</@ofbizUrl>',
                callback: function (d) {
                    if (d.result == "error") {
                        alert(d.message);
                        return false
                    }
                    var fileData = d.data[0];//是个数组
                    $("#img2").attr("src", fileData.fileUrl).show()
                }
            });
        };
    </script>
</head>
<body>
<div>
    <p>
        <input id="selectButton" type="button" value="Select"/>
        <input id="fileInput" type="file"/>
        <img id="img">
    </p>

    <p>
        <input id="fileInput2" type="file"/>
        <img id="img2">
    </p>
</div>
</body>
</html>