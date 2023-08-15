package zhishusz.housepresell.util.excel.model;

import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.controller.form.Tgpf_RemainRight_DtlForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

public class Tgpf_RemainRightDifferenceTemplate implements IExportExcel<Tgpf_RemainRight_DtlForm>
{
	@Getter @Setter @IFieldAnnotation(remark="序号")
	private Integer index;
//	@Getter @Setter @IFieldAnnotation(remark="楼幢号")
//	private String eCodeOfBuilding;
	@Getter @Setter @IFieldAnnotation(remark="单元号")
	private String eCodeOfBuildingUnit;
	@Getter @Setter @IFieldAnnotation(remark="房间号")
	private String eCodeFromRoom;
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额-平台数据")
	private Double theAmount;
	@Getter @Setter @IFieldAnnotation(remark="受限权益-平台数据")
	private Double limitedRetainRight;
	@Getter @Setter @IFieldAnnotation(remark="可支取权益-平台数据")
	private Double withdrawableRetainRight;
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额-上传数据")
	private Double theAmount_Upload;
	@Getter @Setter @IFieldAnnotation(remark="受限权益-上传数据")
	private Double limitedRetainRight_Upload;
	@Getter @Setter @IFieldAnnotation(remark="可支取权益-上传数据")
	private Double withdrawableRetainRight_Upload;
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额-差异")
	private Double theAmount_Compare;
	@Getter @Setter @IFieldAnnotation(remark="受限权益-差异")
	private Double limitedRetainRight_Compare;
	@Getter @Setter @IFieldAnnotation(remark="可支取权益-差异")
	private Double withdrawableRetainRight_Compare;
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名")
	private String buyer;
	@Getter @Setter @IFieldAnnotation(remark="借款人姓名")
	private String theNameOfCreditor;
	@Getter @Setter @IFieldAnnotation(remark="借款人证件号码")
	private String idNumberOfCreditor;
	@Getter @Setter @IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContractRecord;
	@Getter @Setter @IFieldAnnotation(remark="实际入账金额")
	private Double actualDepositAmount;
	
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("index", "序号");
//		map.put("eCodeOfBuilding", "楼幢号");
		map.put("eCodeOfBuildingUnit", "单元号");
		map.put("eCodeFromRoom", "房间号");
		map.put("theAmount", "平台-留存权益总金额");
		map.put("limitedRetainRight", "平台-受限权益");
		map.put("withdrawableRetainRight", "平台-可支取权益");
		map.put("theAmount_Upload", "上传-留存权益总金额");
		map.put("limitedRetainRight_Upload", "上传-受限权益");
		map.put("withdrawableRetainRight_Upload", "上传-可支取权益");
		map.put("theAmount_Compare", "差异-留存权益总金额");
		map.put("limitedRetainRight_Compare", "差异-受限权益");
		map.put("withdrawableRetainRight_Compare", "差异-可支取权益");
		map.put("buyer", "买受人");
		map.put("theNameOfCreditor", "借款人");
		map.put("idNumberOfCreditor", "借款人证件号码");
		map.put("eCodeOfContractRecord", "合同备案号");
		map.put("actualDepositAmount", "实际入账金额");
		return map;
	}

	@Override
	public void init(Tgpf_RemainRight_DtlForm fromClass)
	{
		this.setIndex(fromClass.getIndex());
//		this.seteCodeOfBuilding(fromClass.geteCodeOfBuilding());
		this.seteCodeOfBuildingUnit(fromClass.geteCodeOfBuildingUnit());
		this.seteCodeFromRoom(fromClass.geteCodeFromRoom());
		this.setTheAmount(fromClass.getTheAmount());
		this.setLimitedRetainRight(fromClass.getLimitedRetainRight());
		this.setWithdrawableRetainRight(fromClass.getWithdrawableRetainRight());
		this.setTheAmount_Upload(fromClass.getTheAmount_Upload());
		this.setLimitedRetainRight_Upload(fromClass.getLimitedRetainRight_Upload());
		this.setWithdrawableRetainRight_Upload(fromClass.getWithdrawableRetainRight_Upload());
		this.setTheAmount_Compare(fromClass.getTheAmount_Compare());
		this.setLimitedRetainRight_Compare(fromClass.getLimitedRetainRight_Compare());
		this.setWithdrawableRetainRight_Compare(fromClass.getWithdrawableRetainRight_Compare());
		this.setTheNameOfCreditor(fromClass.getTheNameOfCreditor());
		this.setIdNumberOfCreditor(fromClass.getIdNumberOfCreditor());
		this.seteCodeOfContractRecord(fromClass.geteCodeOfContractRecord());
		this.setActualDepositAmount(fromClass.getActualDepositAmount());
	}

//	public String geteCodeOfBuilding() {
//		return eCodeOfBuilding;
//	}
//
//	public void seteCodeOfBuilding(String eCodeOfBuilding) {
//		this.eCodeOfBuilding = eCodeOfBuilding;
//	}

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

	public String geteCodeOfContractRecord() {
		return eCodeOfContractRecord;
	}

	public void seteCodeOfContractRecord(String eCodeOfContractRecord) {
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}
}