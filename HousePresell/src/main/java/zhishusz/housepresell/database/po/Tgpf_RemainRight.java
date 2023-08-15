package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：留存权益
 * 备注：这是户室的留存权益
 * */
@ITypeAnnotation(remark="留存权益")
public class Tgpf_RemainRight implements Serializable
{
	private static final long serialVersionUID = -7144793175570889497L;
	
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
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private Long enterTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名（张三，李四）")
	private String buyer;
	
	@Getter @Setter @IFieldAnnotation(remark="借款人名称")
	private String theNameOfCreditor;
	
	@Getter @Setter @IFieldAnnotation(remark="借款人证件号码")
	private String idNumberOfCreditor;
	
	@IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContractRecord;
	
	@IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleAgreement;
	
	@Getter @Setter @IFieldAnnotation(remark="来源业务类型")
	private String srcBusiType;
	
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="所属楼栋")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢号")
	private String eCodeOfBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="所属单元")
	private Empj_UnitInfo buildingUnit;
	
	@IFieldAnnotation(remark="单元号")
	private String eCodeOfBuildingUnit;

	@Getter @Setter @IFieldAnnotation(remark="户室")
	private Empj_HouseInfo house;
	
	@IFieldAnnotation(remark="房间号")
	private String eCodeFromRoom;

	@Getter @Setter @IFieldAnnotation(remark="实际入账金额")
	private Double actualDepositAmount;

	@Getter @Setter @IFieldAnnotation(remark="按揭贷款入账金额")
	private Double depositAmountFromLoan;

	@Getter @Setter @IFieldAnnotation(remark="贷款账号")
	private String theAccountFromLoan;
	
	@Getter @Setter @IFieldAnnotation(remark="资金性质")
	private Integer fundProperty;

	@Getter @Setter @IFieldAnnotation(remark="入账银行")
	private Emmp_BankInfo bank;

	@Getter @Setter @IFieldAnnotation(remark="入账银行名称")
	private String theNameOfBankPayedIn;

	@Getter @Setter @IFieldAnnotation(remark="留存权益系数")
	private Double theRatio;

	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额")
	private Double theAmount;

	@Getter @Setter @IFieldAnnotation(remark="受限权益")
	private Double limitedRetainRight;//（未到期留存权益）

	@Getter @Setter @IFieldAnnotation(remark="可支取权益")
	private Double withdrawableRetainRight;//（到期留存权益）
	
	@Getter @Setter @IFieldAnnotation(remark="本次分摊金额")
	private Double currentDividedAmout;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="楼幢留存权益")
	private Tgpf_BuildingRemainRightLog buildingRemainRightLog;
	
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

	public String geteCodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}

	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}

	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}

	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}

	public String geteCodeOfBuildingUnit() {
		return eCodeOfBuildingUnit;
	}

	public void seteCodeOfBuildingUnit(String eCodeOfBuildingUnit) {
		this.eCodeOfBuildingUnit = eCodeOfBuildingUnit;
	}

	public String geteCodeFromRoom() {
		return eCodeFromRoom;
	}

	public void seteCodeFromRoom(String eCodeFromRoom) {
		this.eCodeFromRoom = eCodeFromRoom;
	}
	
	
	
}
