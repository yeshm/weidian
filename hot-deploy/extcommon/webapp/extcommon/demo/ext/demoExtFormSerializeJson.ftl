<div class="container">
    <form id="J_Form" class="form-horizontal" action="#">
    <div class="control-group">
        <label class="control-label" for="title"><s>*</s>官网标题：</label>

        <div class="controls">
            <input type="text" name="title" id="title" value="豪客来" class="input-large" >
        <span class="help-inline">
			 请认真填写，错了不能修改。比如：gh_423dwjkeww3  <a href="#"><i class="icon-question-sign"></i> 不懂问我</a> <a href="http://wpa.qq.com/msgrd?v=3&amp;uin=4006305400&amp;site=qq&amp;menu=yes" target="_blank"><i class="icon-info-sign"></i> 联系客服</a>
        </span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for="keyword"><s>*</s>触发官网关键词：</label>

        <div class="controls">
            <input type="text" name="keyword" id="keyword" value="微官网" class="input-large">
            <span class="help-inline">多个关键字请用空格分开</span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label"><s>*</s>匹配模式：</label>

        <div class="controls">
            <label class="radio">
                <input type="radio" name="matchtype" checked="checked">完全匹配（用户输入的和此关键词一样才会触发!）</label>
            <label class="radio ">
                <input type="radio" name="matchtype">包含匹配 (只要用户输入的文字包含本关键词就触发！)</label>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for="brief">图文消息简介：</label>

        <div class="controls">
            <textarea id="brief" name="brief" class="input-large" style="height:120px;" >豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来豪客来</textarea>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for="select_bg">选择已有的官网背景：</label>

        <div class="controls">
            <select name="bg_img" id="select_bg">
                <option value="">选择官网背景图片</option>
                <option value="http://stc.weimob.com/img/template/lib/home-default9.jpg?v=2014-01-23-4">01</option>
                <option value="http://stc.weimob.com/img/template/lib/home-default10.jpg?v=2014-01-23-4">02</option>
                <option value="http://stc.weimob.com/img/template/lib/home-default11.jpg?v=2014-01-23-4">03</option>
            </select>
            <span class="help-inline">以预览背景图为标准</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="mp3url">背景音乐：</label>

        <div class="controls">
            <div class="inline mp3" style="display:none">
                <a id="m1" class="audio {skin:'blue'}" href="http://stc.weimob.com/img/template/lib/default.mp3"
                   style="display: none;">default.mp3</a>

                <div id="mp_m1" isplaying="false" class="mbMiniPlayer blue shadow"
                     style="display: inline-block; vertical-align: middle;">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tbody>
                        <tr>
                            <td unselectable="on">
                                <span class="map_volume" style="opacity: 1;">Vm</span>
                            </td>
                            <td unselectable="on" style="display: none;">
                      <span class="map_volumeLevel" style="display: none;">
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a class="sel" style="opacity: 0.4; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                        <a style="opacity: 0.1; height: 80%; width: 2px;"></a>
                      </span>
                            </td>
                            <td unselectable="on" class="map_controlsBar" style="display: none;">
                                <div class="map_controls">
                                    <span class="map_title">default.mp3</span>

                                    <div class="jp-progress">
                                        <div class="jp-load-bar" id="loadBar_mp_m1" style="width: 100%;">
                                            <div class="jp-play-bar" id="playBar_mp_m1"
                                                 style="overflow: hidden; width: 0%;"></div>
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td unselectable="on" style="display: none;">
                                <span class="map_time" style="display: none;" title="01:06">00:00</span>
                            </td>
                            <td unselectable="on" style="display: none;">
                                <span class="map_rew" style="display: none;">R</span>
                            </td>
                            <td unselectable="on">
                                <span class="map_play">P</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <input type="hidden" name="bg_audio" id="mp3url" value="http://stc.weimob.com/img/template/lib/default.mp3">
          <span class="help-inline">
            <button type="button" class="button-small" >选择音乐</button>
          </span>
          <span class="help-inline">
            (保证浏览网页的加载速度,上传音乐最大为
            <span class="red">3MB</span>
            )
          </span>
        </div>

    </div>
    <div class="control-group">
        <label class="control-label" for="select_animation">功能选择：</label>

        <div class="controls ">
            <label class="checkbox inline"><input type="checkbox" name="comment" value="1">开启素材图文评论</label>
            <label class="checkbox inline"><input type="checkbox" name="play_img" value="1" checked="checked">开启背景图片</label>
            <label class="checkbox inline"><input type="checkbox" name="play_audio" value="1" checked="checked">开启背景音乐</label>
            <span class="help-inline">(只有开启背景音乐或图片前台页面才会显示或播放)</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="select_animation">开场动画：</label>

        <div class="controls">
            <select name="animation" id="select_animation">
                <option value="0">关闭开场动画</option>
                <option value="6">宝马动画</option>
                <option value="4">左右展开</option>
                <option value="5" selected="selected">上下展开</option>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="bg_animation">背景动画：</label>

        <div class="controls">
            <select name="bg_animation" id="bg_animation">
                <option value="0" selected="selected">关闭背景动画</option>
                <option value="2">雪花</option>
                <option value="1">玫瑰</option>
                <option value="9">秋天落叶</option>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="tj_code">统计代码：</label>

        <div class="controls">
            <textarea id="tj_code" name="stat_code" class="input-xxlarge" style="height: 120px;"></textarea>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="sites">智能分流：</label>

        <div class="controls">
            <lable>
                请将此段代码复制到您 <strong class="red">PC网站</strong>
                &lt;/head&gt;&lt;body&gt;之间,这样手机访问PC网站的用户就会自动跳转到微官网
            </lable>
            <br>
            <textarea id="smart_branch" name="smart_branch" class="input-xxlarge copy_text" readonly="readonly">
                只读
            </textarea>
            <button class="button-small"  type="button" data-clipboard-target="smart_branch" data-clipboard-text="">
                <i class="icon-share-alt"></i>复制
            </button>
            <span class="alert copy-success help-inline alert-success hide ">复制成功,请粘帖到您需要的地方</span>
        </div>
    </div>
    <input type="hidden" name="id" value="16396">

        <div class="row form-actions actions-bar">
            <div class="span13 offset3 ">
                <button type="submit" class="button button-colorful">保存</button>
                <button type="reset" class="button">重置</button>
            </div>
        </div>
    </form>

</div>
<script type="text/javascript">
    BUI.use('form',function (Form) {
        var form = new Form.Form({
            srcNode : '#J_Form',
            submitType : 'ajax',
            callback: function(d){
                if(app.ajaxHelper.handleAjaxMsg(d)){
                    app.showSuccess('设置被关注自动回复成功！',{
                        callback: function(){
                            app.page.reload();
                        }
                    })
                }
            }
        });
        form.render();
        form.on("beforesubmit", function(){
            var json = $("#J_Form").serializeJson();
            alert(JSON.stringify(json));
            return false;
        })
    });
</script>
<!-- 仅仅为了显示代码使用，不要在项目中引入使用-->
<script type="text/javascript">
    $(function () {
        prettyPrint();
    });
</script>