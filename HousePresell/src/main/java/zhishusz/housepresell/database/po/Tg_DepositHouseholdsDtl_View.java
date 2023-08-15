package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：托管户信息明细表
 */
@ITypeAnnotation(remark="托管户信息明细表")
public class Tg_DepositHouseholdsDtl_View implements Serializable
{
	
	private static final long serialVersionUID = -2677952037426223508L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	//---------公共字段-Start---------//
	
	@IFieldAnnotation(remark="合同编号")
	private String eCodeOfContractRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="坐落")
	private String position;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人名称")
	private String buyerName;
	
	@IFieldAnnotation(remark="买受人证件号")
	private String eCodeOfcertificate;

	@Getter @Setter @IFieldAnnotation(remark="买受人电话")
	public String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="付款方式")
	public String payWay;
	
	@Getter @Setter @IFieldAnnotation(remark="买卖合同签订与备案状态")
	private Integer contractCteateState;
	
	@Getter @Setter @IFieldAnnotation(remark="买卖合同签订时间")
	private String contractCreateTime;
	
	@Getter @Setter @IFieldAnnotation(remark="买卖合同备案时间")
	private String contractRecordTime;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议备案状态")
	private Integer tripleAgreementRecordState;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议备案时间")
	private String tripleAgreementRecordTime;

	@Getter @Setter @IFieldAnnotation(remark="入账金额")
	private Double totalAmountOfHouse;
	
	@Getter @Setter @IFieldAnnotation(remark="留存权益")
	private Double theAmountOfRetainedEquity;
	
	@Getter @Setter @IFieldAnnotation(remark="项目信息")
	private Empj_ProjectInfo projectInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢信息")
	private Empj_BuildingInfo buildingInfo;

	public String geteCodeOfContractRecord()
	{
		return eCodeOfContractRecord;
	}

	public void seteCodeOfContractRecord(String eCodeOfContractRecord)
	{
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}

	public String geteCodeOfcertificate()
	{
		return eCodeOfcertificate;
	}

	public void seteCodeOfcertificate(String eCodeOfcertificate)
	{
		this.eCodeOfcertificate = eCodeOfcertificate;
	}
}
