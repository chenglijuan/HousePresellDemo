package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：三方协议
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_TripleAgreementForm extends NormalActionForm
{
	private static final long serialVersionUID = -2048748738300983674L;
	
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
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private String userUpdate;//修改人
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
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private String tripleAgreementTimeStamp;//协议日期
	@Getter @Setter
	private String eCodeOfContractRecord;//合同备案号
	@Getter @Setter
	private String sellerName;//出卖人
	@Getter @Setter
	private String escrowCompany;//托管机构
	@Getter @Setter
	private String theNameOfProject;//项目名称
	@Getter @Setter
	private Long projectId;//项目-Id
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼幢
	@Getter @Setter
	private Long buildingInfoId;//关联楼幢-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢编号
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private Empj_UnitInfo unitInfo;//关联单元
	@Getter @Setter
	private Long unitInfoId;//关联单元-Id
	@Getter @Setter
	private String eCodeOfUnit;//单元
	@Getter @Setter
	private String unitRoom;//户室
	@Getter @Setter
	private Long houseId;//户室-Id
	@Getter @Setter
	private Double buildingArea;//建筑面积（m2）
	@Getter @Setter
	private Double contractAmount;//合同金额（元）
	@Getter @Setter
	private Double firstPayment;//首付款
	@Getter @Setter
	private Double loanAmount;//贷款金额
	@Getter @Setter
	private Set buyerInfoSet;//关联买受人
	@Getter @Setter
	private String theStateOfTripleAgreement;//三方协议状态
	@Getter @Setter
	private String theStateOfTripleAgreementFiling;//三方协议归档状态
	@Getter @Setter
	private String theStateOfTripleAgreementEffect;//三方协议效力状态
	@Getter @Setter
	private String printMethod;//打印方式
	@Getter @Setter
	private Double theAmountOfRetainedEquity;//留存权益总金额
	@Getter @Setter
	private Double theAmountOfInterestRetained;//到期留存权益金额
	@Getter @Setter
	private Double theAmountOfInterestUnRetained;//未到期留存权益金额
	@Getter @Setter
	private Double totalAmountOfHouse;//户托管入账总金额
	@Getter @Setter
	private String smAttachmentList;//附件参数
	
	@Getter @Setter
	private String buyerName;//买受人名称
	@Getter @Setter
	private Double contractSumPrice;//合同总价
	@Getter @Setter
	private String position;//房屋座落
	@Getter @Setter
	private String contractSignDate;//合同签订日期
	@Getter @Setter
	private String paymentMethod;//付款方式
	@Getter @Setter
	private String loanBank;//贷款银行
	@Getter @Setter
	private String syncPerson;//交付日期
	@Getter @Setter
	private String buyerlist;//买受人信息
	@Getter @Setter
	private String contractRecordDate;//合同签订日期
	@Getter	@Setter
	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed;// 托管表
	@Getter @Setter
	private Long beginTime;//查询起始时间
	@Getter @Setter
	private Long endTime;//查询终止时间
	@Getter @Setter
	private Empj_HouseInfo house;//户室
	@Getter @Setter
	private Emmp_CompanyInfo company;//关联开发企业
	@Getter @Setter
	private Long companyId;//关联开发企业
	@Getter @Setter 
	private String isAgency;//是否是代理公司 0-否 1-是
	@Getter @Setter
	private String startDate;//开始时间
	@Getter @Setter
	private String endDate;//结束时间
	@Getter @Setter
	private Long cityRegionId;//区域Id
	@Getter @Setter 
	private String codeFroTripleOrContract;//三方合同号
	@Getter @Setter
	private String payDate;
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}
	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}
	public String geteCodeOfContractRecord() {
		return eCodeOfContractRecord;
	}
	public void seteCodeOfContractRecord(String eCodeOfContractRecord) {
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}
	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}
	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public String geteCodeOfUnit() {
		return eCodeOfUnit;
	}
	public void seteCodeOfUnit(String eCodeOfUnit) {
		this.eCodeOfUnit = eCodeOfUnit;
	}
}
