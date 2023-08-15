package zhishusz.housepresell.util.excel.model;

import java.io.Serializable;

import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="留存权益")
public class Tgpf_RemainRightTemplate implements Serializable
{
	private static final long serialVersionUID = -7144793175570889497L;
	
	@Getter @Setter @IFieldAnnotation(remark="序号")
	private Integer index;
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String enterTimeStamp;
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
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	@IFieldAnnotation(remark="楼幢号")
	private String eCodeOfBuilding;
	@IFieldAnnotation(remark="单元号")
	private String eCodeOfBuildingUnit;
	@IFieldAnnotation(remark="房间号")
	private String eCodeFromRoom;
	@Getter @Setter @IFieldAnnotation(remark="实际入账金额")
	private Double actualDepositAmount;
	@Getter @Setter @IFieldAnnotation(remark="留存权益总金额")
	private Double theAmount;
	@Getter @Setter @IFieldAnnotation(remark="受限权益")
	private Double limitedRetainRight;//（未到期留存权益）
	@Getter @Setter @IFieldAnnotation(remark="可支取权益")
	private Double withdrawableRetainRight;

	public String getECodeOfContractRecord() {
		return eCodeOfContractRecord;
	}

	public void setECodeOfContractRecord(String eCodeOfContractRecord) {
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}

	public String getECodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}

	public void setECodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}

	public String getECodeOfBuilding() {
		return eCodeOfBuilding;
	}

	public void setECodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}

	public String getECodeOfBuildingUnit() {
		return eCodeOfBuildingUnit;
	}

	public void setECodeOfBuildingUnit(String eCodeOfBuildingUnit) {
		this.eCodeOfBuildingUnit = eCodeOfBuildingUnit;
	}

	public String getECodeFromRoom() {
		return eCodeFromRoom;
	}

	public void setECodeFromRoom(String eCodeFromRoom) {
		this.eCodeFromRoom = eCodeFromRoom;
	}
	
	public static Tgpf_RemainRightTemplate rebuild(Tgpf_RemainRight tgpf_RemainRight, int index) {
		Tgpf_RemainRightTemplate template = new Tgpf_RemainRightTemplate();
		template.setIndex(index);
		template.setEnterTimeStamp(DateUtil.format(DateUtil.date(tgpf_RemainRight.getEnterTimeStamp()), "yyyy-MM-dd"));
		template.setBuyer(tgpf_RemainRight.getBuyer());
		template.setTheNameOfCreditor(tgpf_RemainRight.getTheNameOfCreditor());
		template.setIdNumberOfCreditor(tgpf_RemainRight.getIdNumberOfCreditor());
		template.setECodeOfContractRecord(tgpf_RemainRight.geteCodeOfContractRecord());
		template.setECodeOfTripleAgreement(tgpf_RemainRight.geteCodeOfTripleAgreement());
		template.setTheNameOfProject(tgpf_RemainRight.getTheNameOfProject());
		template.setECodeOfBuilding(tgpf_RemainRight.geteCodeOfBuilding());
		template.setECodeOfBuildingUnit(tgpf_RemainRight.geteCodeOfBuildingUnit());
		template.setECodeFromRoom(tgpf_RemainRight.geteCodeFromRoom());
		template.setActualDepositAmount(tgpf_RemainRight.getActualDepositAmount());
		template.setTheAmount(tgpf_RemainRight.getTheAmount());
		template.setLimitedRetainRight(tgpf_RemainRight.getLimitedRetainRight());
		template.setWithdrawableRetainRight(tgpf_RemainRight.getWithdrawableRetainRight());
		return template;
	}
	
}
