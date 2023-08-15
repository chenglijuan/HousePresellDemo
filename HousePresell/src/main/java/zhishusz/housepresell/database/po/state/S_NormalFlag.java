package zhishusz.housepresell.database.po.state;

//通用标签
public class S_NormalFlag
{
	public static final String loginUserType = "loginUserType";//登录者的类型 S_UserType
	
	public static final String userName = "userName";
	public static final String userId = "userId";
	public static final String user = "user";
	public static final String companyType = "companyType";//机构类型
	
	public static final String admin = "admin";
	public static final String adminId = "adminId";
	public static final String token = "token";
	public static final String skey = "skey";
	public static final String interfaceVersion = "interfaceVersion";
	public static final String encryptKey= "ABCDEFGH";
	
	public static final String sessionId = "sessionId";
	public static final String ipAddress = "ipAddress";
	
	public static final String result = "result";
	public static final String success = "success";
	public static final String successWithVersionControl = "successWithVersionControl";
	public static final String fail = "fail";
	public static final String info = "info";
	public static final String info_Success = "操作成功";
	public static final String info_BusiError = "系统繁忙，请稍后再试";
	public static final String info_NeedLogin = "该操作需要您先登录！";
	public static final String info_ErrorInterfaceVersion = "请更新到最新版本";
	public static final String info_InDeveloping = "暂无数据";//"新功能开发中，敬请期待！";
	public static final String info_PhoneNumberFail = "手机号码格式不正确";
	public static final String info_IdCardFail = "身份证号码格式不正确";
	public static final String info_FtpPwdEmpty = "FTP账号密码不能为空";
	public static final String info_PwdIncorrectFormat = "密码必须由6-20位数字、字母、一般符号组成的";
	public static final String pageNumber = "pageNumber";
	public static final String countPerPage = "countPerPage";
	public static final String totalPage = "totalPage";
	public static final String totalCount = "totalCount";
	public static final String keyword = "keyword";
	public static final String info_NoFind = "未查询到有效的信息，请刷新后重试";
	
	public static final String serverBasePath = "serverBasePath";
	
	public static final String debugInfo = "debugInfo";
	
}
