<style>

    .data-list {
        width: 100%;
        text-align: left;
    }

    .data-list li {
        color: #333;
        border-bottom: 1px solid #e7e7e7;
        background-color: #fafafa;
        font-size: 16px;
        font-weight: bold;
        padding: 10px 10px 10px 90px;
        display: block;
        position: relative;
    }

    .data-list li.ui-list-hover {
        background: #ededed;
    }

    .data-list li a {
        text-decoration: none;
    }

    .data-list li dt {
        font-size: 16px;
        font-weight: bold;
        line-height: 16px;
        padding-top: 10px;
        color: #333;
    }

    .data-list li dt:first-child {
        padding-top: 0;
    }

    .data-list li dd.content {
        font-size: 14px;
        color: #545454;
        line-height: 16px;
        margin-top: 8px;
    }

    .data-list li dd.source {
        font-size: 12px;
        color: #969696;
        margin-top: 8px;
    }

    .data-list img {
        position: absolute;
        left: 15px;
        top: 50%;
        width: 57px;
        margin-top: -35px;
    }


</style>


<div class="ui-refresh">
    <div class="ui-refresh-up"></div>
    <ul class="data-list">
        <li>
            <a href="http://www.baidu.com">
                <img src="http://clouddebug.duapp.com/tags/2.1.1/examples/assets/refresh/txt.png"/>
                <dl>
                    <dt>英国地标“大本钟”用女王名</dt>
                    <dd class="content">新华网深圳3月23日电（记者 赵瑞西）23日，深圳市南山区西里医院的大楼</dd>
                    <dd class="source">来源：新浪</dd>
                </dl>
            </a>
        </li>
        <li>
            <a href="http://www.baidu.com">
                <img src="http://clouddebug.duapp.com/tags/2.1.1/examples/assets/refresh/pdf.png"/>
                <dl>
                    <dt>英国地标“大本钟”用女王名</dt>
                    <dd class="content">新华网深圳3月23日电（记者 赵瑞西）23日，深圳市南山区西里医院的大楼</dd>
                    <dd class="source">来源：新浪</dd>
                </dl>
            </a>
        </li>
        <li>
            <a href="http://www.baidu.com">
                <img src="http://clouddebug.duapp.com/tags/2.1.1/examples/assets/refresh/xls.png"/>
                <dl>
                    <dt>英国地标“大本钟”用女王名</dt>
                    <dd class="content">新华网深圳3月23日电（记者 赵瑞西）23日，深圳市南山区西里医院的大楼</dd>
                    <dd class="source">来源：新浪</dd>
                </dl>
            </a>
        </li>
        <li>
            <a href="http://www.baidu.com">
                <img src="http://clouddebug.duapp.com/tags/2.1.1/examples/assets/refresh/ufo.png"/>
                <dl>
                    <dt>英国地标“大本钟”用女王名</dt>
                    <dd class="content">新华网深圳3月23日电（记者 赵瑞西）23日，深圳市南山区西里医院的大楼</dd>
                    <dd class="source">来源：新浪</dd>
                </dl>
            </a>
        </li>
    </ul>
    <div class="ui-refresh-down"></div>
    <!--setup方式带有class为ui-refresh-down或ui-refresh-up的元素必须加上，用于放refresh按钮-->
</div>


<script>


    (function () {
        var countUp = 0, countDown = 0;     //demo演示使用变量
        /*组件初始化js begin*/
        $('.ui-refresh').refresh({
            load: function (dir, type) {
                var me = this;
                $.getJSON('<@ofbizUrl>demoGmuAjaxRefreshData</@ofbizUrl>', function (data) {
                    var $list = $('.data-list');
                    var html = data.data;
                    $list[dir == 'up' ? 'prepend' : 'append'](html);
                    me.afterDataLoading(dir);    //数据加载完成后改变状态
                    dir == 'up' && ++countUp > 1 && me.disable(dir);     //上边refresh加载两次后不再加载，并且显示没有更多内容的文案
                    dir == 'down' && ++countDown > 1 && me.disable(dir, true);     //下边refresh加载两次后不再加载，并隐藏
                });
            }
        }).on('statechange', function (e) {
                    e.preventDefault();
                    var data = e.data,
                            $elem = data[0],
                            state = data[1],
                            dir = data[2];
                    switch (state) {
                        case 'loaded':
                            $elem.html(dir == 'up' ? '点击刷新' : '上拉加载更多');
                            break;
                        case 'beforeload':
                            $elem.html(dir == 'up' ? '松开立即刷新' : '松开立即加载');
                            break;
                        case 'loading':
                            $elem.html(dir == 'up' ? '正在刷新中...' : '正在加载中...');
                            break;
                        case 'disable':
                            $elem.html(dir == 'up' ? '对不起，没有更多内容了' : '没有更多内容');
                            break;
                    }
                });
        /*组件初始化js end*/
    })();


</script>



