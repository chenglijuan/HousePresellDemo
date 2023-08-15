package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：留存权益比对
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRight_DtlForm extends NormalActionForm
{
	private static final long serialVersionUID = 8237610298477101986L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	private String eCode;//编号
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
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
	private String eCodeOfContractRecord;//合同备案号
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private String srcBusiType;//来源业务类型
	@Getter @Setter
	private Long projectId;//关联项目-Id
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private Long buildingId;//所属楼栋-Id
	private String eCodeOfBuilding;//楼幢号
	@Getter @Setter
	private Long buildingUnitId;//所属单元-Id
	private String eCodeOfBuildingUnit;//单元号
	@Getter @Setter
	private Long houseId;//戶室-Id
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
	private Boolean hasUploadData;
	@Getter @Setter
	private Boolean hasPlatformData;
	//数据上传留存权益
	@Getter @Setter
	private Double theAmount_Upload;
	@Getter @Setter
	private Double limitedRetainRight_Upload;
	@Getter @Setter
	private Double withdrawableRetainRight_Upload;
	@Getter @Setter
	//差异
	private Double theAmount_Compare;
	@Getter @Setter
	private Double limitedRetainRight_Compare;
	@Getter @Setter
	private Double withdrawableRetainRight_Compare;
	@Getter @Setter
	private Integer index;
	
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
