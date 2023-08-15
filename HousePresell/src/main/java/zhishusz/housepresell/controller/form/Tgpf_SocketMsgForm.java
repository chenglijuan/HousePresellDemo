package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：接口报文表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_SocketMsgForm extends NormalActionForm
{
	private static final long serialVersionUID = 1043112356402450674L;
	
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
	private String msgLength;//总包长
	@Getter @Setter
	private String locationCode;//区位代码
	@Getter @Setter
	private String msgBusinessCode;//交易代码
	@Getter @Setter
	private String bankCode;//银行编码
	@Getter @Setter
	private String returnCode;//返回码
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String msgDirection;//报文方向
	@Getter @Setter
	private Integer msgStatus;//发送状态
	@Getter @Setter
	private String md5Check;//MD5验证
	@Getter @Setter
	private Long msgTimeStamp;//发生时间
	@Getter @Setter
	private String eCodeOfTripleAgreement;//三方协议号
	@Getter @Setter
	private String eCodeOfPermitRecord;//预售许可证编号
	@Getter @Setter
	private String eCodeOfContractRecord;//预售合同编号
	@Getter @Setter
	private String msgSerialno;//报文流水号
	@Getter @Setter
	private String msgContent;//报文内容
	@Getter	@Setter	
	private String bankPlatformSerialNo;//银行平台流水号

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
