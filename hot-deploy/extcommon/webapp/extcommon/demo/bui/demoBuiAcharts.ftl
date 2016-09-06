<h2>基本折线图</h2>
<div class="detail-section">
    <div id="canvas">

    </div>

    <br/>
    <input type="button" onclick="return changeData()" value="修改数据">
</div>
<script>
    var chart;
    var data = [
        [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6],
        [17.0, 16.9, 19.5, 24.5, 28.2, 31.5, 35.2, 36.5, 33.3, 28.3, 23.9, 19.6],
        [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
    ];

    function random(min,max){
        return min + (max - min) * Math.random();
    }

    var changeData = function () {
        //changeData接收的参数只包含数据，跟初始化chart时传入的series不完全一致
//        chart.changeData(data, true);

        var d = [];
        for (var i = 0; i < 3; i++) {
            var dArray = [];
            for(var j=0;j<12;j++) {
                dArray.push(random(0,40));
            }
            d.push(dArray);
        }
        chart.changeData(d, true);
    };

    $(function () {
        chart = new AChart({
            theme: AChart.Theme.SmoothBase,
            id: 'canvas',
            width: 950,
            height: 500,
            plotCfg: {
                margin: [50, 50, 80] //画板的边距
            },
            xAxis: {
                categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
            },
//            yAxis: {
//                min: 0,
//                max: 100
//            },
            tooltip: {
                valueSuffix: '°C',
                shared: true, //是否多个数据序列共同显示信息
                crosshairs: true //是否出现基准线
            },
            series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }, {
                name: 'Sahnghai',
                data: [17.0, 16.9, 19.5, 24.5, 28.2, 31.5, 35.2, 36.5, 33.3, 28.3, 23.9, 19.6]
            }]
        });

        chart.render();
    });
</script>
<#include "/extcommon/webapp/extcommon/includes/acharts.ftl"/>