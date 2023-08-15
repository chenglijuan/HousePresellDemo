package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：预售系统买卖合同
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_ContractInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 3147028763304631389L;
	
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
	private String eCodeOfContractRecord;//合同备案号
	@Getter @Setter
	private Emmp_CompanyInfo company;//关联企业
	@Getter @Setter
	private Long companyId;//关联企业-Id
	@Getter @Setter
	private String theNameFormCompany;//企业名称-冗余
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private Empj_HouseInfo houseInfo;//关联户室
	@Getter @Setter
	private Long houseInfoId;//关联户室-Id
	@Getter @Setter
	private String eCodeOfHouseInfo;//户室编号
	@Getter @Setter
	private String roomIdOfHouseInfo;//室号
	@Getter @Setter
	private Double contractSumPrice;//合同总价
	@Getter @Setter
	private Double buildingArea;//建筑面积（㎡）
	@Getter @Setter
	private String position;//房屋座落
	@Getter @Setter
	private Long contractSignDate;//合同签订日期
	@Getter @Setter
	private String paymentMethod;//付款方式
	@Getter @Setter
	private String loanBank;//贷款银行
	@Getter @Setter
	private Double firstPaymentAmount;//首付款金额（元）
	@Getter @Setter
	private Double loanAmount;//贷款金额（元）
	@Getter @Setter
	private String escrowState;//托管状态
	@Getter @Setter
	private Long payDate;//交付日期
	@Getter @Setter
	private String eCodeOfBuilding;//备案系统楼幢编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private Long contractRecordDate;//合同备案日期
	@Getter @Setter
	private String syncPerson;//同步人
	@Getter @Setter
	private Long syncDate;//同步日期
	
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
