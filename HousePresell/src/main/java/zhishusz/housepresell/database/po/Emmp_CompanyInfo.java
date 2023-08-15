package zhishusz.housepresell.database.po;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：机构信息
 * 正泰作为特殊机构，类型编号为00
 * */
@ITypeAnnotation(remark="机构信息")
public class Emmp_CompanyInfo implements Serializable,ILogable,IApprovable
{
	private static final long serialVersionUID = 9020712438644067769L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态 已备案/未备案")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;

	@Getter @Setter @IFieldAnnotation(remark="是否启用 1启用 2停用 S_IsUsedState")
	private String isUsedState;

	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;

	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;

	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;

	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案状态 S_RecordState")
	private Integer recordState;
	
	@Getter @Setter @IFieldAnnotation(remark="备案驳回原因")
	private String recordRejectReason;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="类型 S_CompanyType")
	private String theType;

	@Getter @Setter @IFieldAnnotation(remark="归属集团")
	private String companyGroup;
	
	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;//同步预售系统+手工输入
	
	@Getter @Setter @IFieldAnnotation(remark="机构简称")
	private String shortName;

	 @IFieldAnnotation(remark="预售系统企业编号(同步预售系统;手工新增的则为空)")
	private String eCodeFromPresellSystem;

	@Getter @Setter @IFieldAnnotation(remark="企业成立日期")
	private Long establishmentDate;//格式“yyyyMMdd”(日历控间选择+手工输入)

	@Getter @Setter @IFieldAnnotation(remark="资质等级")
	private String qualificationGrade;//仅开发企业使用（前台界面隐藏） S_QualificationGrade

	@Getter @Setter @IFieldAnnotation(remark="社会信用代码")
	private String unifiedSocialCreditCode;//同步预售系统+手工输入

	@Getter @Setter @IFieldAnnotation(remark="注册资本")
	private Double registeredFund;

	@Getter @Setter @IFieldAnnotation(remark="经营范围")
	private String businessScope;

	@Getter @Setter @IFieldAnnotation(remark="成立日期")
	private Long registeredDate;

	@Getter @Setter @IFieldAnnotation(remark="营业期限")
	private Long expiredDate;

	@Getter @Setter @IFieldAnnotation(remark="法定代表人")
	private String legalPerson;//法人代表姓名

	@Getter @Setter @IFieldAnnotation(remark="联系人姓名")
	private String contactPerson;//联系人姓名

	@Getter @Setter @IFieldAnnotation(remark="联系人手机号")
	private String contactPhone;//联系人手机号

	@Getter @Setter @IFieldAnnotation(remark="项目负责人")
	private String projectLeader;//当机构类型为“开发企业”时，同步预售系统+手工输入

	@Getter @Setter @IFieldAnnotation(remark="开票及账户信息")
	private Emmp_ComAccount financialAccount;

	@Getter @Setter @IFieldAnnotation(remark="资质信息")
	private List<Emmp_QualificationInfo> qualificationInformationList;

	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="所属街道")
	private Sm_StreetInfo street;

	@Getter @Setter @IFieldAnnotation(remark="单位网址")
	private String theURL;
	
	@Getter @Setter @IFieldAnnotation(remark="经营地址")
	private String address;

	@Getter @Setter @IFieldAnnotation(remark="电子邮件")
	private String email;
	
	@Getter @Setter @IFieldAnnotation(remark="传真号码")
	private String theFax;
	
	@IFieldAnnotation(remark="邮政编码")
	private String eCodeOfPost;
	
	@Getter @Setter @IFieldAnnotation(remark="单位介绍")
	private String introduction;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	
	@IFieldAnnotation(remark="关联日志Id")
	private Long logId;
	@Override
	public Long getLogId()
	{
		return logId;
	}
	@Override
	public void setLogId(Long logId)
	{
		this.logId = logId;
	}
	@Override
	public String getLogData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceType()
	{
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String>  peddingApprovalkey = new ArrayList<>();

		if (null == this.theType)
		{
			peddingApprovalkey.add("theName");
			peddingApprovalkey.add("address");
			peddingApprovalkey.add("registeredDateStr"); //企业成立日期
			peddingApprovalkey.add("legalPerson");
			peddingApprovalkey.add("projectLeader");

			peddingApprovalkey.add("contactPerson");
			peddingApprovalkey.add("contactPhone");

			peddingApprovalkey.add("orgMemberList"); //机构成员列表

			peddingApprovalkey.add("generalAttachmentList");
		}else if (this.theType.equals(S_CompanyType.Development))
		{
			peddingApprovalkey.add("theName"); //机构名称
			peddingApprovalkey.add("address");//地址
			peddingApprovalkey.add("registeredDateStr"); //企业成立日期
			peddingApprovalkey.add("legalPerson");//法定代表人
			peddingApprovalkey.add("projectLeader"); //项目负责人
			peddingApprovalkey.add("cityRegionId");//所属区域
			peddingApprovalkey.add("streetId"); //街道
			peddingApprovalkey.add("shortName");//机构简介

			peddingApprovalkey.add("contactPerson");//开发企业联系人
			peddingApprovalkey.add("contactPhone"); //开发企业联系电话

			peddingApprovalkey.add("orgMemberList"); //机构成员列表

			peddingApprovalkey.add("generalAttachmentList");
		}
		else
		{
			peddingApprovalkey.add("theName");
			peddingApprovalkey.add("address");
			peddingApprovalkey.add("registeredDateStr"); //企业成立日期
			peddingApprovalkey.add("legalPerson");
			peddingApprovalkey.add("projectLeader");

			peddingApprovalkey.add("contactPerson");
			peddingApprovalkey.add("contactPhone");

			peddingApprovalkey.add("orgMemberList"); //机构成员列表

			peddingApprovalkey.add("generalAttachmentList");
		}

		//TODO  存放可变更的字段
		return peddingApprovalkey;
	}
	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeFromPresellSystem() {
		return eCodeFromPresellSystem;
	}
	public void seteCodeFromPresellSystem(String eCodeFromPresellSystem) {
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}
	public String geteCodeOfPost() {
		return eCodeOfPost;
	}
	public void seteCodeOfPost(String eCodeOfPost) {
		this.eCodeOfPost = eCodeOfPost;
	}

//	@Override
//	public String toString() {
//		return "{\"theName\":\"" + theName + "\"}";
//	}
}
