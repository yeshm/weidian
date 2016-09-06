<audio id="audio" src="/images/demo/audio/android.mp3" loop></audio>

<button id="bp" onclick="return p();">播放</button>
<button id="bs" onclick="return s();">停止</button>

<script type="text/javascript">

    var snd = $("#audio")[0];

    function p() {
//        alert('click1');
        snd.play();
        return false;
    }
    function s() {
        snd.pause();
        return false;
    }

    $(function(){
//        snd.play();
        $("#bp").click();
    });
</script>
