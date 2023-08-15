package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管合作协议
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_EscrowAgreementForm extends NormalActionForm
{
	private static final long serialVersionUID = 3156362850553342217L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Sm_User userUpdate;//创建人
	@Getter @Setter
	private Long userUpdateId;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private String escrowCompany;//托管机构
	@Getter @Setter
	private String agreementVersion;//协议版本
	@Getter @Setter
	private String eCodeOfAgreement;//协议编号
	@Getter @Setter
	private String contractApplicationDate;//签约申请日期
	@Getter @Setter
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private String theNameOfDevelopCompany;//开发企业名称
	@Getter @Setter
	private Sm_CityRegionInfo cityRegion;//所属区域
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private List buildingInfoList;//关联楼幢
	@Getter @Setter
	private String eCodeOfBuildingInfo;//楼幢编号
	@Getter @Setter
	private String OtherAgreedMatters;//其它约定事项
	@Getter @Setter
	private String disputeResolution;//争议解决方式
	@Getter @Setter
	private String businessProcessState;//业务流程状态
	@Getter @Setter
	private String agreementState;//协议状态
	@Getter @Setter
	private String smAttachmentList;//附件参数
	@Getter @Setter
	private String buildingInfoCodeList;//幢号（施工编号拼接）
	@Getter @Setter
	private String eCodeOfContractRecord;
	@Getter @Setter
	private String startDate;//开始时间
	@Getter @Setter
	private String endDate;//结束时间
	@Getter @Setter
	private String isSign;//是否过滤签章 0-否 1-是
	@Getter @Setter
	private String buildingInfoCode;//幢号（施工编号拼接）
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfAgreement() {
		return eCodeOfAgreement;
	}
	public void seteCodeOfAgreement(String eCodeOfAgreement) {
		this.eCodeOfAgreement = eCodeOfAgreement;
	}
	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfBuildingInfo()
	{
		return eCodeOfBuildingInfo;
	}
	public void seteCodeOfBuildingInfo(String eCodeOfBuildingInfo)
	{
		this.eCodeOfBuildingInfo = eCodeOfBuildingInfo;
	}
	public String geteCodeOfContractRecord()
	{
		return eCodeOfContractRecord;
	}
	public void seteCodeOfContractRecord(String eCodeOfContractRecord)
	{
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}
	
	
}
