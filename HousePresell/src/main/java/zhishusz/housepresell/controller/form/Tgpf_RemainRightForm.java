package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Emmp_BankInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：留存权益
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRightForm extends NormalActionForm
{
	private static final long serialVersionUID = 8237610298477101986L;
	
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
	private Long enterTimeStamp;//入账日期
	@Getter @Setter
	private String buyer;//买受人姓名
	@Getter @Setter
	private String theNameOfCreditor;//借款人名称
	@Getter @Setter
	private String idNumberOfCreditor;//借款人证件号码
	@Getter @Setter
	private String eCodeOfContractRecord;//合同备案号
	@Getter @Setter
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private String srcBusiType;//来源业务类型
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Empj_BuildingInfo building;//所属楼栋
	@Getter @Setter
	private Long buildingId;//所属楼栋-Id
	@Getter @Setter
	private String eCodeOfBuilding;//楼幢号
	@Getter @Setter
	private Empj_UnitInfo buildingUnit;//所属单元
	@Getter @Setter
	private Long buildingUnitId;//所属单元-Id
	@Getter @Setter
	private String eCodeOfBuildingUnit;//单元号
	@Getter @Setter
	private Long houseId;//戶室-Id
	@Getter @Setter
	private String eCodeFromRoom;//房间号
	@Getter @Setter
	private Double actualDepositAmount;//实际入账金额
	@Getter @Setter
	private Double depositAmountFromLoan;//按揭贷款入账金额
	@Getter @Setter
	private String theAccountFromLoan;//贷款账号
	@Getter @Setter
	private Integer fundProperty;//资金性质
	@Getter @Setter
	private Emmp_BankInfo bank;//入账银行
	@Getter @Setter
	private Long bankId;//入账银行-Id
	@Getter @Setter
	private String theNameOfBankPayedIn;//入账银行名称
	@Getter @Setter
	private Double theRatio;//留存权益系数
	@Getter @Setter
	private Double theAmount;//留存权益总金额
	@Getter @Setter
	private Double limitedRetainRight;//受限权益
	@Getter @Setter
	private Double withdrawableRetainRight;//可支取权益
	@Getter @Setter
	private Double currentDividedAmout;//本次分摊金额
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Long buildingRemainRightLogId;//楼幢留存权益记录Id
	@Getter @Setter
	private Tgpf_RemainRight_DtlForm[] tgpf_RemainRight_DtlTab;//留存权益比对明细
	@Getter @Setter
	private String billTimeStamp;//入账日期
	
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
