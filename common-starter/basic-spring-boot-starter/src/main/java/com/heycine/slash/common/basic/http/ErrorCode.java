package com.heycine.slash.common.basic.http;


import com.heycine.slash.common.basic.BasicEnum;

/**
 * 标准错误码 [参考阿里巴巴规范]
 */
public enum ErrorCode implements BasicEnum<Integer, String> {

    /**
     * /系统自定义错误码
     */
    // 前置条件不满足，或为空，或未设置
    PREREQUISITES_ARE_NOT_SATISFIE(10404, "通用code"),
    // EXCEL相关
    EXCEL_TEMPLATE_ERROR(50500, "导入的Excel模板填写不规范，请按照官方提供的模板示范数据进行填写！"),
    COMPANY_NO_BIND_TASK(60500, "所属公司还未绑定相关任务！"),
    COMPANY_NO_BIND_USER(60501, "此用户还未绑定公司，无法配置权限！"),
    // 重复错误
    DUPLICATE_DATA_ADDED(14501,  "不允许重复数据的添加！"),
    DUPLICATE_DATA_UPDATE(14502,  "不允许重复数据的修改！"),
    REMOVE_REFERENCES(14505, "删除时存在引用，不允许进行删除！"),
    NETWORK_FLUCTUATIONS(14506, "网络波动，导致的IO异常！"),

    /**
     * 阿里标准错误码
     */
    SUCCESS(00000, "成功"), USER_ERROR_0001(10001, "用户端错误"), USER_ERROR_A0100(10100, "用户注册错误"),
    USER_ERROR_A0101(10101, "用户未同意隐私协议"), USER_ERROR_A0102(10102, "注册国家或地区受限"), USER_ERROR_A0110(10110, "用户名校验失败"),
    USER_ERROR_A0111(10111, "用户名已存在"), USER_ERROR_A0112(10112, "用户名包含敏感词"), USER_ERROR_A0113(10113, "用户名包含特殊字符"),
    USER_ERROR_A0120(10120, "密码校验失败"), USER_ERROR_A0121(10121, "密码长度不够"), USER_ERROR_A0122(10122, "密码强度不够"),
    USER_ERROR_A0130(10130, "校验码输入错误"), USER_ERROR_A0131(10131, "短信校验码输入错误"), USER_ERROR_A0132(10132, "邮件校验码输入错误"),
    USER_ERROR_A0133(10133, "语音校验码输入错误"), USER_ERROR_A0140(10140, "用户证件异常"), USER_ERROR_A0141(10141, "用户证件类型未选择"),
    USER_ERROR_A0142(10142, "大陆身份证编号校验非法"), USER_ERROR_A0143(10143, "护照编号校验非法"), USER_ERROR_A0144(10144, "军官证编号校验非法"),
    USER_ERROR_A0150(10150, "用户基本信息校验失败"), USER_ERROR_A0151(10151, "手机格式校验失败"), USER_ERROR_A0152(10152, "地址格式校验失败"),
    USER_ERROR_A0153(10153, "邮箱格式校验失败"), USER_ERROR_A0200(10200, "用户登陆异常"), USER_ERROR_A0201(10201, "用户账户不存在"),
    USER_ERROR_A0202(10202, "用户账户被冻结"), USER_ERROR_A0203(10203, "用户账户已作废"), USER_ERROR_A0210(10210, "用户密码错误"),
    USER_ERROR_A0211(10211, "用户输入密码次数超限"), USER_ERROR_A0220(10220, "用户身份校验失败"), USER_ERROR_A0221(10221, "用户指纹识别失败"),
    USER_ERROR_A0222(10222, "用户面容识别失败"), USER_ERROR_A0223(10223, "用户未获得第三方登陆授权"), USER_ERROR_A0230(10230, "用户登陆已过期"),
    USER_ERROR_A0240(10240, "用户验证码错误"), USER_ERROR_A0241(10241, "用户验证码尝试次数超限"), USER_ERROR_A0300(10300, "访问权限异常"),
    USER_ERROR_A0301(10301, "访问未授权"), USER_ERROR_A0302(10302, "正在授权中"), USER_ERROR_A0303(10303, "用户授权申请被拒绝"),
    USER_ERROR_A0310(10310, "因访问对象隐私设置被拦截"), USER_ERROR_A0311(10311, "授权已过期"), USER_ERROR_A0312(10312, "无权限使用API"),
    USER_ERROR_A0320(10320, "用户访问被拦截"), USER_ERROR_A0321(10321, "黑名单用户"), USER_ERROR_A0322(10322, "账号被冻结"),
    USER_ERROR_A0323(10323, "非法IP地址"), USER_ERROR_A0324(10324, "网关访问受限"), USER_ERROR_A0325(10325, "地域黑名单"),
    USER_ERROR_A0330(10330, "服务已欠费"), USER_ERROR_A0340(10340, "用户签名异常"), USER_ERROR_A0341(10341, "RSA签名错误"),
    USER_ERROR_A0400(10400, "用户请求参数错误"), USER_ERROR_A0401(10401, "包含非法恶意跳转链接"), USER_ERROR_A0402(10402, "无效的用户输入"),
    USER_ERROR_A0410(10410, "请求必填参数为空"), USER_ERROR_A0411(10411, "用户订单号为空"), USER_ERROR_A0412(10412, "订购数量为空"),
    USER_ERROR_A0413(10413, "缺少时间戳参数"), USER_ERROR_A0414(10414, "非法的时间戳参数"), USER_ERROR_A0420(10420, "请求参数值超出允许的范围"),
    USER_ERROR_A0421(10421, "参数格式不匹配"), USER_ERROR_A0422(10422, "地址不在服务范围"), USER_ERROR_A0423(10423, "时间不在服务范围"),
    USER_ERROR_A0424(10424, "金额超出限制"), USER_ERROR_A0425(10425, "数量超出限制"), USER_ERROR_A0426(10426, "请求批量处理总个数超出限制"),
    USER_ERROR_A0427(10427, "请求JSON解析失败"), USER_ERROR_A0430(10430, "用户输入内容非法"), USER_ERROR_A0431(10431, "包含违禁敏感词"),
    USER_ERROR_A0432(10432, "图片包含违禁信息"), USER_ERROR_A0433(10433, "文件侵犯版权"), USER_ERROR_A0440(10440, "用户操作异常"),
    USER_ERROR_A0441(10441, "用户支付超时"), USER_ERROR_A0442(10442, "确认订单超时"), USER_ERROR_A0443(10443, "订单已关闭"),
    USER_ERROR_A0500(10500, "用户请求服务异常"), USER_ERROR_A0501(10501, "请求次数超出限制"), USER_ERROR_A0502(10502, "请求并发数超出限制"),
    USER_ERROR_A0503(10503, "用户操作请等待"), USER_ERROR_A0504(10504, "WebSocket连接异常"),
    USER_ERROR_A0505(10505, "WebSocket连接断开"), USER_ERROR_A0506(10506, "用户重复请求"), USER_ERROR_A0600(10600, "用户资源异常"),
    USER_ERROR_A0601(10601, "账户余额不足"), USER_ERROR_A0602(10602, "用户磁盘空间不足"), USER_ERROR_A0603(10603, "用户内存空间不足"),
    USER_ERROR_A0604(10604, "用户OSS容量不足"), USER_ERROR_A0605(10605, "用户配额已用光"), USER_ERROR_A0700(10700, "用户上传文件异常"),
    USER_ERROR_A0701(10701, "用户上传文件类型不匹配"), USER_ERROR_A0702(10702, "用户上传文件太大"), USER_ERROR_A0703(10703, "用户上传图片太大"),
    USER_ERROR_A0704(10704, "用户上传视频太大"), USER_ERROR_A0705(10705, "用户上传压缩文件太大"), USER_ERROR_A0800(10800, "用户当前版本异常"),
    USER_ERROR_A0801(10801, "用户安装版本与系统不匹配"), USER_ERROR_A0802(10802, "用户安装版本过低"), USER_ERROR_A0803(10803, "用户安装版本过高"),
    USER_ERROR_A0804(10804, "用户安装版本已过期"), USER_ERROR_A0805(10805, "用户API请求版本不匹配"),
    USER_ERROR_A0806(10806, "用户API请求版本过高"), USER_ERROR_A0807(10807, "用户API请求版本过低"), USER_ERROR_A0900(10900, "用户隐私未授权"),
    USER_ERROR_A0901(10901, "用户隐私未签署"), USER_ERROR_A0902(10902, "用户摄像头未授权"), USER_ERROR_A0903(10903, "用户相机未授权"),
    USER_ERROR_A0904(10904, "用户图片库未授权"), USER_ERROR_A0905(10905, "用户文件未授权"), USER_ERROR_A0906(10906, "用户位置信息未授权"),
    USER_ERROR_A0907(10907, "用户通讯录未授权"), USER_ERROR_A1000(11000, "用户设备异常"), USER_ERROR_A1001(11001, "用户相机异常"),
    USER_ERROR_A1002(11002, "用户麦克风异常"), USER_ERROR_A1003(11003, "用户听筒异常"), USER_ERROR_A1004(11004, "用户扬声器异常"),
    USER_ERROR_A1005(11005, "用户GPS定位异常"),

    SYSTEM_ERROR_B0001(20001, "系统执行出错"), SYSTEM_ERROR_B0100(20100, "系统执行超时"), SYSTEM_ERROR_B0101(20101, "系统订单处理超时"),
    SYSTEM_ERROR_B0200(20200, "系统容灾功能被触发"), SYSTEM_ERROR_B0210(20210, "系统限流"), SYSTEM_ERROR_B0220(20220, "系统功能降级"),
    SYSTEM_ERROR_B0300(20300, "系统资源异常"), SYSTEM_ERROR_B0310(20310, "系统资源耗尽"), SYSTEM_ERROR_B0311(20311, "系统磁盘空间耗尽"),
    SYSTEM_ERROR_B0312(20312, "系统内存耗尽"), SYSTEM_ERROR_B0313(20313, "文件句柄耗尽"), SYSTEM_ERROR_B0314(20314, "系统连接池耗尽"),
    SYSTEM_ERROR_B0315(20315, "系统线程池耗尽"), SYSTEM_ERROR_B0320(20320, "系统资源访问异常"),
    SYSTEM_ERROR_B0321(20321, "系统读取磁盘文件失败"),

    SERVICE_ERROR_C0001(30001, "调用第三方服务出错"), SERVICE_ERROR_C0100(30100, "中间件服务出错"),
    SERVICE_ERROR_C0110(30110, "RPC服务出错"), SERVICE_ERROR_C0111(30111, "RPC服务未找到"),
    SERVICE_ERROR_C0112(30112, "RPC服务未注册"), SERVICE_ERROR_C0113(30113, "接口不存在"), SERVICE_ERROR_C0120(30120, "消息服务出错"),
    SERVICE_ERROR_C0121(30121, "消息投递出错"), SERVICE_ERROR_C0122(30122, "消息消费出错"), SERVICE_ERROR_C0123(30123, "消息订阅出错"),
    SERVICE_ERROR_C0124(30124, "消息分组未查到"), SERVICE_ERROR_C0130(30130, "缓存服务出错"),
    SERVICE_ERROR_C0131(30131, "key长度超过限制"), SERVICE_ERROR_C0132(30132, "value长度超过限制"),
    SERVICE_ERROR_C0133(30133, "存储容量已满"), SERVICE_ERROR_C0134(30134, "不支持的数据格式"), SERVICE_ERROR_C0140(30140, "配置服务出错"),
    SERVICE_ERROR_C0150(30150, "网络资源服务出错"), SERVICE_ERROR_C0151(30151, "VPN服务出错"),
    SERVICE_ERROR_C0152(30152, "CDN服务出错"), SERVICE_ERROR_C0153(30153, "域名解析服务出错"), SERVICE_ERROR_C0154(30154, "网关服务出错"),
    SERVICE_ERROR_C0200(30200, "第三方系统执行超时"), SERVICE_ERROR_C0210(30210, "RPC执行超时"),
    SERVICE_ERROR_C0220(30220, "消息投递超时"), SERVICE_ERROR_C0230(30230, "缓存服务超时"), SERVICE_ERROR_C0240(30240, "配置服务超时"),
    SERVICE_ERROR_C0250(30250, "数据库服务超时"), SERVICE_ERROR_C0300(30300, "数据库服务出错"), SERVICE_ERROR_C0311(30311, "表不存在"),
    SERVICE_ERROR_C0312(30312, "列不存在"), SERVICE_ERROR_C0321(30321, "多表关联中存在多个相同名称的列"),
    SERVICE_ERROR_C0331(30331, "数据库死锁"), SERVICE_ERROR_C0341(30341, "主键冲突"), SERVICE_ERROR_C0400(30400, "第三方容灾系统被触发"),
    SERVICE_ERROR_C0401(30401, "第三方系统限流"), SERVICE_ERROR_C0402(30402, "第三方功能降级"), SERVICE_ERROR_C0500(30500, "通知服务出错"),
    SERVICE_ERROR_C0501(30501, "短信提醒服务失败"), SERVICE_ERROR_C0502(30502, "语音提醒服务失败"),
    SERVICE_ERROR_C0503(30503, "邮件提醒服务失败");;

    private Integer code;

    private String info;

    ErrorCode(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getInfo() {
        return info;
    }

}
