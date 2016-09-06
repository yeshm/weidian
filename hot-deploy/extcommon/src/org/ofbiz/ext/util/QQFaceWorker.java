package org.ofbiz.ext.util;

import org.apache.commons.lang.StringUtils;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.util.EntityUtil;

public class QQFaceWorker {

    private static final String module = QQFaceWorker.class.getName();


    public static String[] faceArray = {"/::)","/::~","/::B","/::|","/:8-)","/::<","/::$","/::X","/::Z","/::'(","/::-|","/::@","/::P","/::D","/::O","/::(","/::+","/:--b","/::Q","/::T","/:,@P","/:,@-D","/::d","/:,@o","/::g","/:|-)","/::!","/::L","/::>","/::,@","/:,@f","/::-S","/:?","/:,@x","/:,@@","/::8","/:,@!","/:!!!","/:xx","/:bye","/:wipe","/:dig","/:handclap","/:&-(","/:B-)","/:<@","/:@>","/::-O","/:>-|","/:P-(","/::'|","/:X-)","/::*","/:@x","/:8*","/:pd","/:<W>","/:beer","/:basketb","/:oo","/:coffee","/:eat","/:pig","/:rose","/:fade","/:showlove","/:heart","/:break","/:cake","/:li","/:bome","/:kn","/:footb","/:ladybug","/:shit","/:moon","/:sun","/:gift","/:hug","/:strong","/:weak","/:share","/:v","/:@)","/:jj","/:@@","/:bad","/:lvu","/:no","/:ok","/:love","/:<L>","/:jump","/:shake","/:<O>","/:circle","/:kotow","/:turn","/:skip","/:oY","/:#-0","/:hiphot","/:kiss","/:<&","/:&>"};
    public static String[] replayArray = {"/微笑","/撇嘴","/色","/发呆","/得意","/流泪","/害羞","/闭嘴","/睡","/大哭","/尴尬","/发怒","/调皮","/呲牙","/惊讶","/难过","/酷","/冷汗","/抓狂","/吐","/偷笑","/可爱","/白眼","/傲慢","/饥饿","/困","/惊恐","/流汗","/憨笑","/大兵","/奋斗","/咒骂","/疑问","/嘘","/晕","/折磨","/衰","/骷髅","/敲打","/再见","/擦汗","/抠鼻","/鼓掌","/糗大了","/坏笑","/左哼哼","/右哼哼","/哈欠","/鄙视","/委屈","/快哭了","/阴险","/亲亲","/吓","/可怜","/菜刀","/西瓜","/啤酒","/篮球","/乒乓","/咖啡","/饭","/猪头","/玫瑰","/凋谢","/示爱","/爱心","/心碎","/蛋糕","/闪电","/炸弹","/刀","/足球","/瓢虫","/便便","/月亮","/太阳","/礼物","/拥抱","/强","/弱","/握手","/胜利","/抱拳","/勾引","/拳头","/差劲","/爱你","/NO","/OK","/爱情","/飞吻","/跳跳","/发抖","/怄火","/转圈","/磕头","/回头","/跳绳","/挥手","/激动","/街舞","/献吻","/左太极","/右太极"};

    public static String replaceEach(String source){
        return StringUtils.replaceEach(source, faceArray, replayArray);
    }
}
