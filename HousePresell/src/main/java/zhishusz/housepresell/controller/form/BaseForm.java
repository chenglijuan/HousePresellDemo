package zhishusz.housepresell.controller.form;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyLong;

@ToString(exclude = {"sessionId", "user", "isSuperAdmin", "token", "ipAddress", "operateStartDatetime"})
public class BaseForm implements Serializable
{
	private static final long serialVersionUID = 6656969077410851090L;

	@Getter @Setter
	private Long exceptTableId;//表ID (检查唯一性使用)

	@Getter @Setter
	protected String skey;
	@Getter @Setter
	protected String token;
	@Getter @Setter
	protected String phoneType;
	@Getter @Setter
	protected String sessionId;
	@Getter @Setter
	protected String ipAddress;
	@Getter @Setter
	protected Integer interfaceVersion;//格式：yyyyMMdd
	@Getter @Setter
	protected String info;// S_NormalFlag.info

	@Getter @Setter
	protected Long userId;
	@Getter @Setter
	protected Sm_User user;
	@Getter @Setter
	protected Boolean isSuperAdmin;

	@Getter @Setter
	protected Integer pageNumber;
	@Getter @Setter
	protected Integer countPerPage;
	@Getter @Setter
	protected String keyword;
//	@Getter @Setter
//	protected Integer theState;
	@Getter @Setter
	private Long[] idArr;

	//--------------审批相关--------------------//
	@Getter @Setter
	private Integer sourcePage;//来源页面，1：代办，2：已办或者我发起的
	@Getter @Setter
	private Long afId;  //审批流程 - 申请单Id
	@Getter @Setter
	protected String busiCode; // 业务编码
	@Getter @Setter
	private String buttonType;  // 1: 保存按钮  2. 提交按钮
	@Getter @Setter
	private String approvalState;  // 审批状态

	//用款申请和资金拨付审批的是同一个 用款申请单 ，所有资金拨付审批不能在审批公共接口中再设置审批状态
	@Getter @Setter
	private Boolean IsSetApprovalState = true;
//--------------审批相关--------------------//

	@Getter @Setter
	private Long operateStartDatetime = System.currentTimeMillis();

	@Getter @Setter
	private Integer sortordTime;//创建时间排序
	@Getter @Setter
	private Integer sortordOrder;//序号排序

	@Getter @Setter
	private String serverBasePath;
	
	@Getter @Setter
	private Long[] cityRegionInfoIdArr;//范围授权-城市过滤 根据Id查询
	@Getter @Setter
	private List<Sm_CityRegionInfo> loginCityRegionInfoList;//范围授权-城市过滤 根据对象查询
	
	@Getter @Setter
	private Long[] projectInfoIdArr;//范围授权-项目过滤 根据Id查询
	@Getter @Setter
	private List<Empj_ProjectInfo> logInProjectInfoList;//范围授权-项目过滤 根据对象查询
	
	@Getter @Setter
	private Long[] buildingInfoIdIdArr;//范围授权-楼幢过滤 根据Id查询
	@Getter @Setter
	private List<Empj_BuildingInfo> logInBuildingInfoList;//范围授权-楼幢过滤 根据对象查询

	@Getter @Setter
	private Long[] developCompanyInfoIdArr;//范围授权-开发企业过滤 根据Id查询
	@Getter @Setter
	private List<Emmp_CompanyInfo> developCompanyInfoList;//范围授权-开发企业过滤 根据对象查询
	
	//附件相关
	@Getter @Setter
	private String busiType;
	@Getter @Setter
	private Sm_AttachmentForm[] generalAttachmentList;

	//排序相关
	@Getter @Setter
	private String orderBy;
	@Getter @Setter
	private String orderByType;
	
	//审批回显数据差相关
	@Getter @Setter
	private String reqSource;//请求来源
	
	@Getter @Setter
	private Integer theAction;//审批动作  0: 通过 1：驳回   2：不通过 
	
	//风控抽查时间范围
	@Getter @Setter
	private Long checkStartTimeStamp;//抽查开始时间
	@Getter @Setter
	private Long checkEndTimeStamp;//抽查结束时间
	
	//--------------审批附件相关 start--------------------//
	@Getter @Setter
	private String sm_AttachmentList;//附件参数
	@Getter @Setter
	private Long picTableId;//单据主键
	
	//--------------审批附件相关 end--------------------//
	
	//--------------签章参数 start--------------------//
		@Getter @Setter
		private String picTypeCode;//附件配置类型ecode（sourceType）
		@Getter @Setter
		private String ukeyRealNumber;//获取的ukey序列号
		
		//--------------签章参数 end--------------------//

	public void init(HttpServletRequest request)
	{
//		this.skey = request.getParameter(S_NormalFlag.skey);
//		this.token = request.getParameter(S_NormalFlag.token);
//		this.interfaceVersion = MyInteger.getInstance().parse(request.getParameter(S_NormalFlag.interfaceVersion));
//		this.phoneType = request.getParameter("phoneType");
		this.ipAddress = request.getRemoteAddr();
		
		HttpSession session = request.getSession();
		this.sessionId = session.getId();
		this.userId = MyLong.getInstance().parse(session.getAttribute(S_NormalFlag.userId));
		this.user = (Sm_User)(session.getAttribute(S_NormalFlag.user));
		
		if (this.serverBasePath == null) {
			StringBuffer serverBasePath = new StringBuffer();
			if(80 == request.getServerPort())
	    	{
	    		serverBasePath.append(request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/");
	    	}
	    	else
	    	{
	    		serverBasePath.append(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
	    	}
			this.serverBasePath = serverBasePath.toString();
		}
	}
	
	private static Digester md5 = new Digester(DigestAlgorithm.MD5);
	public String getMD5()
	{
		return md5.digestHex(this.toString());
	}
}
