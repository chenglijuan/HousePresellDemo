package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：基本户凭证
 * */
@ITypeAnnotation(remark="非基本户凭证")
public class Tgpf_BasicAccount implements Serializable
{
	
    private static final long serialVersionUID = 1850553975900046075L;

    //---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;

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
	@Getter @Setter @IFieldAnnotation(remark="账号名称")
    private String accountName;
	
	@Getter @Setter @IFieldAnnotation(remark="类别")
    private String voucherType;
	
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private String billTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="摘要")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="科目代码")
    private String subCode;
	
	@Getter @Setter @IFieldAnnotation(remark="金额")
	private Double totalTradeAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="推送状态")
	private String sendState;//推送状态【自动写入枚举：0-未推送、1-已推送】

	@Getter @Setter @IFieldAnnotation(remark="推送日期")
	private String sendTime;//推送日期
	
	@Getter @Setter @IFieldAnnotation(remark="凭证号")
	private String vou_No;//凭证号
	
	@Getter @Setter @IFieldAnnotation(remark="凭证内容")
    private String contentJson;
	
	@Getter @Setter @IFieldAnnotation(remark="账号")
    private String accountNumber;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}
