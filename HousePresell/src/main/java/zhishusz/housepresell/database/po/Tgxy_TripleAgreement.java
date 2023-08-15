package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象名称：三方协议
 */
@ITypeAnnotation(remark="三方协议")
public class Tgxy_TripleAgreement implements Serializable,IApprovable
{
	private static final long serialVersionUID = -8183212723884331380L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;
	
	//---------公共字段-Start---------//

	@IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleAgreement;//系统生成，生成规则：Tgxy_EscrowAgreement.eCodeOfAgreement+四位流水号（按天流水）
	
	@Getter @Setter @IFieldAnnotation(remark="协议日期")
	private String tripleAgreementTimeStamp;
	
	@IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContractRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="出卖人")
	private String sellerName;
	
	@Getter @Setter @IFieldAnnotation(remark="托管机构")
	private String escrowCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theNameOfProject;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;

	@Getter @Setter @IFieldAnnotation(remark="关联单元")
	private Empj_UnitInfo unitInfo;//一对一关系
	
	@IFieldAnnotation(remark="单元")
	private String eCodeOfUnit;
	
	@Getter @Setter @IFieldAnnotation(remark="户室")
	private String unitRoom;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积（m2）")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="合同金额（元）")
	private Double contractAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="首付款")
	private Double firstPayment;
	
	@Getter @Setter @IFieldAnnotation(remark="贷款金额")
	private Double loanAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="关联买受人")
	private Set<Tgxy_BuyerInfo> buyerInfoSet;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议状态",columnName="theStateOfTA")
	private String theStateOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议归档状态",columnName="theStateOfTAF")
	private String theStateOfTripleAgreementFiling;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议效力状态",columnName="theStateOfTAE")
	private String theStateOfTripleAgreementEffect;
	
	@Getter @Setter @IFieldAnnotation(remark="打印方式")
	private String printMethod;
	
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额")
	private Double theAmountOfRetainedEquity;
	
	@Getter @Setter @IFieldAnnotation(remark="到期留存权益金额")
	private Double theAmountOfInterestRetained;
	
	@Getter @Setter @IFieldAnnotation(remark="未到期留存权益金额")
	private Double theAmountOfInterestUnRetained;
	
	@Getter @Setter @IFieldAnnotation(remark="户托管入账总金额（按揭入账）")
	private Double totalAmountOfHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="户托管入账总金额")
	private Double totalAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名")
	private String buyerName;
	
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	private Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="关联户室")
	private Empj_HouseInfo house;
	
//	@Getter	@Setter	@IFieldAnnotation(remark = "托管表")
//	private Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "关联合同编号eCode")
	private String ecodeOfContract;
	
	@Getter @Setter @IFieldAnnotation(remark="托管模式  0-贷款、1-全额")
	private String escrowPattern;
	
	@Getter @Setter @IFieldAnnotation(remark="托管方式 0-按幢、1-按户")
	private String escrowMode;
	
	@Getter @Setter @IFieldAnnotation(remark="定金金额（元）")
	private Double earnestMoney;
	
	@Getter @Setter @IFieldAnnotation(remark="处理结果")
	private String processingResults;
	
	@Getter @Setter @IFieldAnnotation(remark="处理意见")
	private String processingOpinions;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋坐落")
	private String roomlocation;

	@Override
	public String getSourceType() {
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
		peddingApprovalkey.add("busiState");
		peddingApprovalkey.add("eCode");
		//TODO  存放需要审批的字段
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
