package org.ofbiz.ext.biz;


import javolution.util.FastMap;

import java.util.Map;

/**
 * 系统常用静态变量，Type Enum Status等使用单独的类来管理，不放在这里，请逐步搬迁
 */
@Deprecated
public class ConstantParameter {
    public static final int DEFAULT_GRID_PAGE_SIZE = 30;//默认分页大小
    public static final int DEFAULT_SIMPLE_PAGE_SIZE = 10;//默认简单分页大小
    public static final double DEFAULT_LBS_DISTANCE = 1000;// 默认LBS查询范围
    public static final int RANDOM_RANGE = 9999999;// 随机数范围

    public static final String NA = "_NA_";//Not Applicable 不适用，多见于非空字段的无适用值时的取代，比如说汽车的参数中有火花塞的相关参数，那么对于柴油汽车就是“不适用”的，因为柴油发动机是压燃结构，没有火花塞

    /**
     * 场景类型*
     */
    public static final String SCENE_TYPE_MEDIA_WALL = "MEDIA_WALL";//媒体墙
    public static final String SCENE_TYPE_SERVICE_SYSTEM = "SERVICE_SYSTEM";//多客服

    /*微留言状态*/
    public static final Map<String, String> MESSAGE_STATUS_MAP = FastMap.newInstance();
    public static final String MESSAGE_STATUS_NORMAL = "MESSAGE_NORMAL";
    public static final String MESSAGE_STATUS_DELETED = "MESSAGE_DELETED";

    static {
        MESSAGE_STATUS_MAP.put(MESSAGE_STATUS_NORMAL, "正常");
        MESSAGE_STATUS_MAP.put(MESSAGE_STATUS_DELETED, "已删除");
    }

    /*微预约状态*/
    public static final Map<String, String> RESERVE_STATUS_MAP = FastMap.newInstance();
    public static final String RESERVE_STATUS_NORMAL = "RESERVE_NORMAL";
    public static final String RESERVE_STATUS_DELETED = "RESERVE_DELETED";

    static {
        RESERVE_STATUS_MAP.put(RESERVE_STATUS_NORMAL, "正常");
        RESERVE_STATUS_MAP.put(RESERVE_STATUS_DELETED, "已删除");
    }

    /*微预约订单审核状态*/
    public static final Map<String, String> RES_APPROVED_STATUS = FastMap.newInstance();
    public static final String RESERVE_STATUS_APPROVED_WAIT = "RES_APPROVED_WAIT";
    public static final String RESERVE_STATUS_APPROVED_PASS = "RES_APPROVED_PASS";
    public static final String RESERVE_STATUS_APPROVED_REFUSE = "RES_APPROVED_REFUSE";
    public static final String RESERVE_STATUS_APPROVED_CANCEL = "RES_APPROVED_CANCEL";
    public static final String RESERVE_STATUS_APPROVED_DELETED = "RES_APPROVED_DELETED";


    static {
        RES_APPROVED_STATUS.put(RESERVE_STATUS_APPROVED_WAIT, "等待审核");
        RES_APPROVED_STATUS.put(RESERVE_STATUS_APPROVED_PASS, "审核通过");
        RES_APPROVED_STATUS.put(RESERVE_STATUS_APPROVED_REFUSE, "拒绝");
        RES_APPROVED_STATUS.put(RESERVE_STATUS_APPROVED_CANCEL, "取消");
        RES_APPROVED_STATUS.put(RESERVE_STATUS_APPROVED_DELETED, "删除");
    }

    /*留言分类*/
    public static final Map<String, String> MESSAGE_LIST_CATEGORY_MAP = FastMap.newInstance();
    public static final String MESSAGE_LIST_CATEGORY_ALL = "MESSAGE_ALL";
    public static final String MESSAGE_LIST_CATEGORY_WAIT_APPROVE = "MESSAGE_LIST_WAIT_APPROVE";
    public static final String MESSAGE_LIST_CATEGORY_UN_REPLY = "MESSAGE_LIST_UN_REPLY";
    public static final String MESSAGE_LIST_CATEGORY_REPLYED = "MESSAGE_LIST_REPLYED";
    public static final String MESSAGE_LIST_CATEGORY_BLACKLIST = "MESSAGE_LIST_BLACKLIST";


    static {
        MESSAGE_LIST_CATEGORY_MAP.put(MESSAGE_LIST_CATEGORY_ALL, "全部");
        MESSAGE_LIST_CATEGORY_MAP.put(MESSAGE_LIST_CATEGORY_WAIT_APPROVE, "待审核");
        MESSAGE_LIST_CATEGORY_MAP.put(MESSAGE_LIST_CATEGORY_UN_REPLY, "未回复");
        MESSAGE_LIST_CATEGORY_MAP.put(MESSAGE_LIST_CATEGORY_REPLYED, "已回复");
        MESSAGE_LIST_CATEGORY_MAP.put(MESSAGE_LIST_CATEGORY_BLACKLIST, "已拉黑");
    }

    //活动二维码状态
    /**
     * 未领取*
     */
    public static final String LOTTERY_ACTIVITY_SN_CODE_STATUS_NO_GET = "LASC_NO_GET";
    /**
     * 已发放*
     */
    public static final String LOTTERY_ACTIVITY_SN_CODE_STATUS_GRANT = "LASC_GRANT";
    /**
     * 已消费*
     */
    public static final String LOTTERY_ACTIVITY_SN_CODE_STATUS_CONSUMED = "LASC_CONSUMED";

    /*微信群发消息*/
     /*微信群发消息的方式*/
    public static final String WEIXIN_MASS_SEND_TYPE_ADVANCED = "SEND_TYPE_ADVANCED";
    public static final String WEIXIN_MASS_SEND_TYPE_SERVICE = "SEND_TYPE_SERVICE";

    /*投票使用类型*/
    public static final String VOTE_USAGE_TYPE_MICRO = "USAGE_TYPE_MICRO";

    /*投票的状态*/
    public static final String VOTE_STATUS_NO_START = "VOTE_NO_START";
    public static final String VOTE_STATUS_START = "VOTE_START";
    public static final String VOTE_STATUS_END = "VOTE_END";
    public static final String VOTE_STATUS_DELETE = "VOTE_DELETE";

    /**
     * 首页横幅*
     */
    public static final String MICROSITE_BANNER_TYPE_INDEX = "INDEX";
    /*首页背景横幅*/
    public static final String MICROSITE_BANNER_TYPE_INDEX_BACKGROUND = "INDEX_BACKGROUND";
}