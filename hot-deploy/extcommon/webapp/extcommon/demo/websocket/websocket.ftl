<html>
<head>
    <title>WebSoket Demo</title>
    <script type="text/JavaScript">
        //验证浏览器是否支持WebSocket协议
        if (!window.WebSocket) {
            alert("WebSocket not supported by this browser!");
        }
        var ws;
        function display() {
            //var valueLabel = document.getElementById("valueLabel");
            //valueLabel.innerHTML = "";
            ws=new WebSocket("ws://localhost:8080/ext/websocket");
            //监听消息
            ws.onmessage = function(event) {
                //valueLabel.innerHTML+ = event.data;
                log(event.data);
            };
            // 打开WebSocket
            ws.onclose = function(event) {
                //WebSocket Status:: Socket Closed
            };
            // 打开WebSocket
            ws.onopen = function(event) {
                //WebSocket Status:: Socket Open
                //// 发送一个初始化消息
                ws.send("Hello, Server!");
            };
            ws.onerror =function(event){
                //WebSocket Status:: Error was reported
            };
        }
        var log = function(s) {
            if (document.readyState !== "complete") {
                log.buffer.push(s);
            } else {
                document.getElementById("contentId").innerHTML += (s + "\n");
            }
        }
        function sendMsg(){
            var msg=document.getElementById("messageId");
            ws.send(msg.value+"_websocket");
        }
    </script>
</head>
<body onload="display();">
<div id="valueLabel"></div>
<textarea rows="30" cols="100" id="contentId"></textarea>
<br/>
<input name="message" id="messageId"/>
<button id="sendButton" onClick="javascript:sendMsg()" >Send</button>
</body>
</html>