package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口报文类
 * 
 * @author wuyu
 * @since 2018年8月10日14:06:34
 */
@ITypeAnnotation(remark = "接口报文表")
public class Tgpf_SocketMsg implements Serializable
{

	// ---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;

	@Getter	@Setter	@IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter	@Setter	@IFieldAnnotation(remark = "业务状态")
	private String busiState;

	@IFieldAnnotation(remark = "编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter	@Setter	@IFieldAnnotation(remark = "创建人")
	private Sm_User userStart;

	@Getter	@Setter	@IFieldAnnotation(remark = "创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter	@Setter	@IFieldAnnotation(remark = "备案人")
	private Sm_User userRecord;

	@Getter	@Setter	@IFieldAnnotation(remark = "备案日期")
	private Long recordTimeStamp;
	// ---------公共字段-Start---------//

	@Getter	@Setter	@IFieldAnnotation(remark = "总包长")
	private String msgLength;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "区位代码")
	private String locationCode;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "交易代码")
	private String msgBusinessCode;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行编码")
	private String bankCode;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "返回码")
	private String returnCode;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "备注")
	private String remark;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "报文方向")
	private String msgDirection;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "发送状态")
	private Integer msgStatus;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "MD5验证")
	private String md5Check;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "发生时间")
	private Long msgTimeStamp;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "三方协议号")
	private String eCodeOfTripleAgreement;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "预售许可证编号")
	private String eCodeOfPermitRecord;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "预售合同编号")
	private String eCodeOfContractRecord;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "中心平台流水号")
	private String msgSerialno;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "报文内容")
	private String msgContent;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "银行平台流水号")
	private String bankPlatformSerialNo;
	
	@Getter	@Setter	@IFieldAnnotation(remark = "与档案系统发送报文")
	private String msgContentArchives; //TODO 注意事项：由开发人员修改数据库字段为clob类型

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}

	public String geteCodeOfTripleAgreement()
	{
		return eCodeOfTripleAgreement;
	}

	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement)
	{
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}

	public String geteCodeOfPermitRecord()
	{
		return eCodeOfPermitRecord;
	}

	public void seteCodeOfPermitRecord(String eCodeOfPermitRecord)
	{
		this.eCodeOfPermitRecord = eCodeOfPermitRecord;
	}

	public String geteCodeOfContractRecord()
	{
		return eCodeOfContractRecord;
	}

	public void seteCodeOfContractRecord(String eCodeOfContractRecord)
	{
		this.eCodeOfContractRecord = eCodeOfContractRecord;
	}
}
