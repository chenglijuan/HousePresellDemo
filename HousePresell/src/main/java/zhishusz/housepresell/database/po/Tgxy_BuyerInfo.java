package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：买受人信息
 * */
@ITypeAnnotation(remark="买受人信息")
public class Tgxy_BuyerInfo implements Serializable
{
	private static final long serialVersionUID = -6396428162984045816L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名")
	private String buyerName;
	
	@Getter @Setter @IFieldAnnotation(remark="买受人类型")
	private String buyerType;
	
	@Getter @Setter @IFieldAnnotation(remark="证件类型")
	private String certificateType;
	
	@IFieldAnnotation(remark="证件号码")
	private String eCodeOfcertificate;
	
	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="联系地址")
	private String contactAdress;
	
	@Getter @Setter @IFieldAnnotation(remark="代理人姓名")
	private String agentName;
	
	@Getter @Setter @IFieldAnnotation(remark="代理人证件类型")
	private String agentCertType;
	
	@Getter @Setter @IFieldAnnotation(remark="代理人证件号")
	private String agentCertNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="代理人联系电话")
	private String agentPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="代理人联系地址")
	private String agentAddress;
	
	@IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContract;
	
	@Getter @Setter @IFieldAnnotation(remark="合同信息")
	private Tgxy_ContractInfo contractInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议信息")
	private Tgxy_TripleAgreement tripleAgreement;
	
	@IFieldAnnotation(remark="三方协议号")
	private String eCodeOfTripleAgreement;

	@Getter @Setter @IFieldAnnotation(remark="关联户室")
	private Empj_HouseInfo houseInfo;
	
	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfcertificate() {
		return eCodeOfcertificate;
	}

	public void seteCodeOfcertificate(String eCodeOfcertificate) {
		this.eCodeOfcertificate = eCodeOfcertificate;
	}

	public String geteCodeOfContract()
	{
		return eCodeOfContract;
	}

	public void seteCodeOfContract(String eCodeOfContract)
	{
		this.eCodeOfContract = eCodeOfContract;
	}

	public String geteCodeOfTripleAgreement()
	{
		return eCodeOfTripleAgreement;
	}

	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement)
	{
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}
}
