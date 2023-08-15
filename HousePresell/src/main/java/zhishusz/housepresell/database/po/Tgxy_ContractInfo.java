package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 *对象名称：预售系统买卖合同
 */
@ITypeAnnotation(remark="预售系统买卖合同")
public class Tgxy_ContractInfo implements Serializable,IApprovable
{
	private static final long serialVersionUID = -4564266791899881746L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;//合同状态
	
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
	//---------公共字段-Start---------//

	@IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContractRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="关联企业")
	private Emmp_CompanyInfo company;

	@Getter @Setter @IFieldAnnotation(remark="企业名称-冗余")
	private String theNameFormCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	private String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="关联户室")
	private Empj_HouseInfo houseInfo;
	
	@IFieldAnnotation(remark="户室编号")
	private String eCodeOfHouseInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="室号")
	private String roomIdOfHouseInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="合同总价")
	private Double contractSumPrice;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积（㎡）")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋座落")
	private String position;
	
	@Getter @Setter @IFieldAnnotation(remark="合同签订日期")
	private String contractSignDate;
	
	@Getter @Setter @IFieldAnnotation(remark="付款方式")
	private String paymentMethod;
	
	@Getter @Setter @IFieldAnnotation(remark="贷款银行")
	private String loanBank;
	
	@Getter @Setter @IFieldAnnotation(remark="首付款金额（元）")
	private Double firstPaymentAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="贷款金额（元）")
	private Double loanAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="托管状态")
	private String escrowState;
	
	@Getter @Setter @IFieldAnnotation(remark="交付日期")
	private String payDate;
	
	@IFieldAnnotation(remark="备案系统楼幢编号")
	private String eCodeOfBuilding;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@Getter @Setter @IFieldAnnotation(remark="合同备案日期")
	private String contractRecordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="同步人")
	private String syncPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="同步日期")
	private Long syncDate;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	
	@Getter @Setter @IFieldAnnotation(remark="定金金额（元）")
	private Double earnestMoney;
	
	@Override
	public String getSourceType() {
		return null;
	}

	@Override
	public Long getSourceId() {
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		return null;
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

	public String geteCodeOfContractRecord() {
		return eCodeOfContractRecord;
	}

	public void seteCodeOfContractRecord(String eCodeOfContractRecord) {
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}

	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeOfHouseInfo() {
		return eCodeOfHouseInfo;
	}

	public void seteCodeOfHouseInfo(String eCodeOfHouseInfo) {
		this.eCodeOfHouseInfo = eCodeOfHouseInfo;
	}

	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}

	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}

	public String geteCodeFromPublicSecurity() {
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}
	
	

}
