package zhishusz.housepresell.controller.form;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管户信息报表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_DepositHouseholdsDtl_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = -1653548727763317367L;
	
	@Getter @Setter
	private Long tableId; //表id
	@Getter @Setter
	private String eCodeOfContractRecord;// 合同编号	
	@Getter @Setter
	private String position; //坐落	
	@Getter @Setter
	private String buyerName; //买受人名称
	@Getter @Setter
	private String eCodeOfcertificate; //买受人证件号
	@Getter @Setter
	public String contactPhone; //买受人电话
	@Getter @Setter
	public String payWay;//付款方式	
	@Getter @Setter
	private Integer contractCteateState;//买卖合同签订与备案状态
	@Getter @Setter
	private String contractCreateTime;//买卖合同签订时间	
	@Getter @Setter
	private String contractRecordTime;//买卖合同备案时间
	@Getter @Setter
	private Integer tripleAgreementRecordState;//三方协议备案状态	
	@Getter @Setter
	private String tripleAgreementRecordTime;//三方协议备案时间
	@Getter @Setter
	private Double totalAmountOfHouse;//入账金额	
	@Getter @Setter
	private Double theAmountOfRetainedEquity;//留存权益
	@Getter @Setter
	private String payMethod;//方式
	@Getter @Setter
	private Long cityRegionId;//区域
	@Getter @Setter
	private Long projectId;//项目
	@Getter @Setter
	private Long buildingId;//楼幢
	
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
