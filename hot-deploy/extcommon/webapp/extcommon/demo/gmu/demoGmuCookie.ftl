<div class="ui-refresh">
    <div class="ui-refresh-up"></div>
    <ul class="data-list">
        <li>
            <button onclick="return addCookie();">添加cookie</button>
            <dl>
                <dt>$.fn.cookie('foo', 'bar');</dt>
            </dl>
        </li>
        <li>
            <button onclick="return getCookie();">获取cookie</button>
            <dl>
                <dt>$.fn.cookie('foo');</dt>
            </dl>
        </li>
        <li>
            <button onclick="return deleteCookie();">删除cookie</button>
            <dl>
                <dt>$.fn.cookie('foo', 'bar', { expires: 0 });</dt>
            </dl>
        </li>

    </ul>
</div>


<script>
    function addCookie() {
        $.fn.cookie('foo', 'bar');
    }
    function getCookie() {
        alert($.fn.cookie('foo'));
    }
    function deleteCookie() {
        $.fn.cookie('foo', 'bar', { expires: 0 });
    }

</script>



