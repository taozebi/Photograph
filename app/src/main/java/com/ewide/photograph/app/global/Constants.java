package com.ewide.photograph.app.global;

import com.ewide.photograph.BuildConfig;

/**
 * Http URL for application
 * Created by Taoze on 2018/4/2.
 */

public class Constants {

    public static final int HTTP_TIME_OUT = 30 * 1000;
    /*协议信息*/
    public static final String LOGIN_SOURCE = "MOBILESALES";
    public static final String LOGIN_TARGET = "YYSF";
    public static final String MSGCODE_LOGIN = "login";
    public static final String MSGCODE_DOWNDATA = "downData";
    public static final String MSGCODE_GETPARAM = "getParams";
    public static final String MSGCODE_GET_BFLIST = "getBfList";
    public static final String MSGCODE_UPLOADDATA = "uploadData";
    public static final String MSGCODE_CHECKDATA = "checkData";
    public static final String MSGCODE_SENDSMS = "sendSMS";
    public static final String MSGCODE_VERSION = "checkVer";
    public static final String MSGCODE_COSTBUDGET = "virtual";
    /**
     * 统计
     */
    public static final String MSGCODE_GETTJLIST = "getTJList";
    /**
     * 沙河统计
     */
    public static final String MSGCODE_GETSHTJLIST = "getSHTJList";
    //消息推送
    public static final String MSGCODE_PUSHDATA = "pushData";

    /**
     * 综合查询
     */
    public static final String MSGCODE_QUERY = "searchList";
    /**
     * 综合查询明细
     */
    public static final String MSGCODE_QUERY_DETAIL = "searchDetail";
    /**
     * 更新数据
     */
    public static final String MSGCODE_UPDATEDATA = "updateData";
    /**
     * 文件上传
     */
    public static final String MSGCODE_UPLOADFILE = "uploadFile";
    /**
     * 文件下载
     */
    public static final String MSGCODE_DOWNLOADFILE = "downloadFile";

    /**
     * 文件下载
     */
    public static final String MSGCODE_ADJUST_ORDER = "adjustOrder";

    /**
     * 根据水表编号查询欠费信息
     */
    public static final String MSGCODE_SERCHOWE = "serchOwe";
    /**
     * 根据水表编号查询欠费信息
     */
    public static final String MSGCODE_PAYOWE = "payOwe";
    /**
     * 根据水表编号查询打印信息
     */
    public static final String MSGCODE_PRINTINV = "printInv";

    /**
     * 走收表册列表
     */
    public static final String MSGCODE_GETBFIPAYLIST = "getBfIpayList";
    /**
     * 走收用户列表
     */
    public static final String MSGCODE_GETIPAYDATA = "getIpayData";
    /**
     * 缴费
     */
    public static final String MSGCODE_IPAY = "Ipay";

    /*数据模板信息*/
    public static String MOBILEDBPATH = "";
    public static final String DB_NAME = "meter.db";

    //加载数据
    public static final int INIT_DATA_CURRENT = 0;
    //下一户
    public static final int INIT_DATA_NEXT = 1;
    //上一户
    public static final int INIT_DATA_PRE = 2;
    //刷新界面
    public static final int INIT_DATA_REFRESH = 3;

    //TODO 是否需要预算水量 魏县需求抄表复核后需要预算水量
    public static final boolean needBudget = false;

    //TODO 是否需要费用预算 普兰店 沙河 衡水 廊坊 榔梨 包头 需求
    public static final boolean needBudgetCost = true;

    //TODO 是否需要缴费模块
    public static final boolean needPay = false;

    // TODO 是否需要在线复核
    public static final boolean needCheckData = true;

    public static class ERRConstant {
        public static final String NET_ERROR_MSG = "网络异常";
        public static final String ANALY_ERROR_MSG = "解析异常";
        public static final String RESPONSE_ERROR_MSG = "网络请求异常";
        public static final String ERR_MSG_EMPTY = "返回数据为空";
        public static final String ERR_MSG_FAILURE = "接口调用失败";
    }


    public static class KEYConstant {
        public static final String BUGLY_APPID = "5aac0032d9";
        public static final String BAIDUMAP_KEY = "6dbf4d61-abb8-4f90-bcfe-beb45821bbe7";
    }

    public static class URLConstant {

        /**
         * App配置信息
         */
        public static String APP_CONFIG_URL = "http://appi.ewidecloud.com/appver/latest";

        /**
         * 服务器地址
         */
        // 公司内网营收地址 cby/000000
//        public static String SERVER_URL = "http://192.168.1.171:9030/";
//        public static String SERVER_URL = "http://192.168.1.17:8191/watererp/";
//        public static String SERVER_URL = "http://119.97.214.18:1314/watererp/";
        //192.168.8.99:8083 老夏
//        public static String SERVER_URL = "http://172.16.8.21:8282/";
//        public static String SERVER_URL = "http://192.168.1.20:9080/";
        //熊伟
//        public static String SERVER_URL = "http://127.0.0.1:9080/platform/";
        //波哥
//        public static String SERVER_URL = "http://192.168.8.9:9080/platform/";
        //公司外网营收地址 cby/000000
//        public static String SERVER_URL = "http://119.97.214.18:1181/";
//        public static String SERVER_URL = "http://119.97.214.18:1168/watererp/";
//        public static String SERVER_URL = "http://119.97.214.18:1168/";

        //公司 http://192.168.1.17:8191/watererp/
//        public static String SERVER_URL = "http://192.168.1.17:8191/watererp/";

        //云锡 (102/000000)
//        public static String SERVER_URL = "http://222.56.118.73:9003/";

        //增城 (046/000000)
//        public static String SERVER_URL = "http://183.6.84.218:8081/";

        //沙河 (ewideadmin\000000) http://124.239.168.234:20104/watererp    44/000000
//        public static String SERVER_URL = "http://124.239.168.234:20104/watererp/";

        //沿河 (C01/123456)(CY/123456)
//        public static String SERVER_URL = "http://220.172.203.13:8081/";

        //魏县 ceshi/000000
//        public static String SERVER_URL = "http://222.222.241.197:89/";

        //普兰店  223.100.161.160  cj-008/123456
//        public static String SERVER_URL = "http://223.100.161.160:9009/";

        //相城移动抄表  http://221.224.214.178:9910/rest/data   5455/ok   5608/123456 106/123456
//        public static String SERVER_URL = "http://221.224.214.178:9910/";//旧
//        public static String SERVER_URL = "http://10.1.8.17:9030/";//新，210006/123456

        //绍兴 http://192.168.0.224:9080  hcy 123456
//        public static String SERVER_URL = "http://60.190.213.242:9400/";

//        曹妃甸  http://124.239.168.234:20102/rest/data   cby01/000000
//        public static String SERVER_URL = "http://124.239.168.234:20102/watererp/";

        //32 服务器 端口号前加1 比如 1778端口号 外网地址端口号就是11778
//        public static String SERVER_URL = "http://119.97.214.18:11778/";

        // 浣江 诸暨 http://60.190.214.214:9030/   21/1818   1541/123
//        public static String SERVER_URL = "http://60.190.214.214:9030/";

        // 衡水 http://60.190.214.214:9030/   21/1818   1541/123
//        public static String SERVER_URL = "http://124.239.168.234:20106/watererp/";

        // 谷城 http://60.190.214.214:9030/   21/1818   1541/123
//        public static String SERVER_URL = "http://111.177.124.6:9000/";

        //濮阳
//        public static String SERVER_URL = "http://192.168.22.3:8081/watererp/";
//        public static String SERVER_URL = "http://117.158.243.34:8081/watererp/";


        //朝阳,ceshi/147258369
//        public static String SERVER_URL = "http://221.180.255.197:8081/watererp/";//测试
//        public static String SERVER_URL = "http://39.152.70.243:8083/watererp/";//正式
//
        //廊坊 账号：1119   密码：000000
//        public static String SERVER_URL = "http://124.239.168.234:20110/watererp/";

        //长兴岛
//        public static String SERVER_URL = "http://221.180.184.9:7070/watererp/";

        //宜昌民生    抄表员 7/123456    标签管理员 001/123456
//        public static String SERVER_URL = "http://119.97.214.18:1327/";
//        public static String SERVER_URL = "http://120.202.69.50:9002/";//正式环境

        //榔梨 7011 密码123456
//        public static String SERVER_URL = "http://113.247.234.106:9200/watererp/";

        //包头 1153 123456
        //是否需要预算水量needBudget = false;
        //是否需要费用预算needBudgetCost = true;
        //是否需要缴费模块 needPay = true;
        //是否需要在线复核needCheckData = true;
        public static String SERVER_URL = "http://110.16.13.70:9080/watererp/";


        /**
         * 用户登录接口
         */
//        public static String URL_INTERFACE = SERVER_URL+"rest/data";
        public static String URL_INTERFACE = SERVER_URL + "rest/ewideInterface";
        //        public static String URL_FILE_UPLOAD = SERVER_URL + "rest/uploadForMobileMulti ";
        public static String URL_FILE_UPLOAD = SERVER_URL + "rest/uploadForMobile";
        public static String URL_FILE_DOWNLOAW = SERVER_URL + "rest/dowloadForMobile";

        public static void updateUrl(String url) {
            SERVER_URL = url;
//            URL_INTERFACE = SERVER_URL+"rest/data";
            URL_INTERFACE = SERVER_URL + "rest/ewideInterface";
            String appId = BuildConfig.APPLICATION_ID;
            //相城使用独立的文件上传下载地址 http://10.1.8.23/
            if ("com.ewide.mobilemr.xiangcheng".equals(appId)) {
                URL_FILE_UPLOAD = "http://10.1.8.23:9030/" + "rest/uploadForMobile";
//            URL_FILE_UPLOAD = SERVER_URL + "rest/uploadForMobileMulti";
                URL_FILE_DOWNLOAW = "http://10.1.8.23:9030/" + "rest/dowloadForMobile";
            } else {
                URL_FILE_UPLOAD = SERVER_URL + "rest/uploadForMobile";
//            URL_FILE_UPLOAD = SERVER_URL + "rest/uploadForMobileMulti";
                URL_FILE_DOWNLOAW = SERVER_URL + "rest/dowloadForMobile";
            }
        }
    }

    public static class FileConstant {
        /**
         * 正常抄表
         */
        public static final String FILE_TYPE_CBZC = "0";
        /**
         * 表况异常
         */
        public static final String FILE_TYPE_BKYC = "1";
        /**
         * 水量波动
         */
        public static final String FILE_TYPE_SLBD = "2";
        /**
         * 信息变更
         */
        public static final String FILE_TYPE_XXBG = "3";
        /**
         * 欠费停水
         */
        public static final String FILE_TYPE_QFTS = "4";
        /**
         * 恢复供水
         */
        public static final String FILE_TYPE_HFGS = "5";
        /**
         * 水表维修
         */
        public static final String FILE_TYPE_SBWX = "6";
        /**
         * 故障换表
         */
        public static final String FILE_TYPE_GZHB = "7";
        /**
         * 周期换表
         */
        public static final String FILE_TYPE_ZQHB = "8";
        /**
         * 大街
         */
        public static final String FILE_TYPE_DJ = "9";
        /**
         * 单元
         */
        public static final String FILE_TYPE_DY = "10";
        /**
         * 表箱
         */
        public static final String FILE_TYPE_BX = "11";
        /**
         * 水表
         */
        public static final String FILE_TYPE_SB = "12";
        /**
         * 申报拍照
         */
        public static final String FILE_TYPE_SBPZ = "13";
        /**
         * 巡线工单
         */
        public static final String FILE_TYPE_XXGD = "14";
    }

    public static class DICTConstants {
        /**
         * 表况
         */
        public static final String DICT_TYPE_METER_STATUS = "检表表态";
        /**
         * 用户状态
         */
        public static final String DICT_TYPE_USER_STATUS = "用户状态";
        /**
         * 表位
         */
        public static final String DICT_TYPE_METER_POSTITION = "表位";
        /**
         * 表参数品牌
         */
        public static final String DICT_TYPE_METER_FACTORY = "表参数品牌";
    }

    /**
     * DB更新常量
     */
    public static class PUSHConstants {
        /**
         * 业务相关数据
         */
        public static final int PUSH_TYPE_BUSINESS = 1;

        /**
         * 业务相关数据
         */
        public static final int PUSH_TYPE_USER = 2;

        /**
         * 业务相关数据
         */
        public static final int PUSH_TYPE_OTHER = 3;

        /**
         * 复核下载
         */
        public static final int PUSH_CMD_REVIEW_DOWN = 1002;

    }



}
