

<div class="row-fluid">
    <div class="span24">
        <div class="control-row-auto">
            <div class="controls">
                <textarea id="testTextArea">你来抓我呀</textarea>
                <button type="button" class="button-small button-primary" onclick="testOutIFrame()">去iframe外面抓</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
     function testOutIFrame() {
          var test = top.document.getElementById("testIntoIFrame");
         test = top.$("#testIntoIFrame")
         top.topManager.setPageTitle(test.html());
     }
</script>