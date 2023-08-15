package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_ComAccount;
import java.util.List;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：机构信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Emmp_CompanyInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 136025580502029648L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState; //业务状态 已备案/未备案
	@Getter @Setter
	private String isUsedState; //是否启用
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Integer recordState;//备案状态 S_RecordState
	@Getter @Setter
	private String recordRejectReason;//备案驳回原因
	@Getter @Setter
	private String theType;//类型 S_CompanyType
	@Getter @Setter
	private Boolean exceptZhengTai;//去除正泰机构
	@Getter @Setter
	private Boolean onlyZhengTai;//只有正泰机构
	@Getter @Setter
	private String companyGroup;//归属集团
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private String shortName;//机构简称
	@Getter @Setter
	private String eCodeFromPresellSystem;//预售系统企业编号(同步预售系统;手工新增的则为空)
	@Getter @Setter
	private Long establishmentDate;//企业成立日期
	@Getter @Setter
	private String establishmentDateStr;//企业成立日期
	@Getter @Setter
	private String qualificationGrade;//资质等级 S_QualificationGrade
	@Getter @Setter
	private String unifiedSocialCreditCode;//统一社会信用代码
	@Getter @Setter
	private Double registeredFund;//注册资本
	@Getter @Setter
	private String businessScope;//经营范围
	@Getter @Setter
	private Long registeredDate;//成立日期
	@Getter @Setter
	private String registeredDateStr;//成立日期
	@Getter @Setter
	private Long expiredDate;//营业期限
	@Getter @Setter
	private String legalPerson;//法定代表人
	@Getter @Setter
	private String contactPerson;//联系人-姓名
	@Getter @Setter
	private String contactPhone;//联系人-手机号
	@Getter @Setter
	private String projectLeader;//项目负责人
	@Getter @Setter
	private Emmp_ComAccount financialAccount;//开票及账户信息
	@Getter @Setter
	private Long financialAccountId;//开票及账户信息-Id
	@Getter @Setter
	private List qualificationInformationList;//资质信息
	@Getter @Setter
	private Sm_CityRegionInfo cityRegion;//所属区域
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id
	@Getter @Setter
	private Sm_StreetInfo street;//所属街道
	@Getter @Setter
	private Long streetId;//所属街道-Id
	@Getter @Setter
	private String theURL;//单位网址
	@Getter @Setter
	private String address;//经营地址
	@Getter @Setter
	private String email;//电子邮件
	@Getter @Setter
	private String theFax;//传真号码
	@Getter @Setter
	private String eCodeOfPost;//邮政编码
	@Getter @Setter
	private String introduction;//单位介绍
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long logId;//关联日志Id
	@Getter @Setter
	private String orgMember;//区分机构成员
	
	//机构成员
	@Getter @Setter
	private Emmp_OrgMemberForm[] orgMemberList;

	//附件相关
	@Getter @Setter
	private String sourceId;
//	@Getter @Setter
//	private Sm_AttachmentForm[] attachmentList;
	
	@Getter @Setter
	private String externalCode;//外来数据关联字段
	@Getter @Setter
	private String externalId;//外来数据关联主键
	@Getter @Setter
	private String resourceNote;//数据来源说明


	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeFromPresellSystem()
	{
		return eCodeFromPresellSystem;
	}
	public void seteCodeFromPresellSystem(String eCodeFromPresellSystem)
	{
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}
	public String geteCodeOfPost()
	{
		return eCodeOfPost;
	}
	public void seteCodeOfPost(String eCodeOfPost)
	{
		this.eCodeOfPost = eCodeOfPost;
	}
}
