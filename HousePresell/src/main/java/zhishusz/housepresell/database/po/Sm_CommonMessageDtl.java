package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ITypeAnnotation(remark="公共消息")
public class Sm_CommonMessageDtl implements Serializable
{
	private static final long serialVersionUID = -2661099448804071788L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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

	//---------公共字段-End---------//
	@Getter @Setter @IFieldAnnotation(remark="消息主体")
	private Sm_CommonMessage message;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="消息业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已读 S_IsReaderState")
	private Integer isReader;//是否已读 S_IsReaderState
	
	@Getter @Setter @IFieldAnnotation(remark="消息类型")
	private String messageType;

	@Getter @Setter @IFieldAnnotation(remark="发送日期yyyy-MM-dd HH:mm:ss")
	private String sendTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="读取日期yyyy-MM-dd HH:mm:ss")
	private String readTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="接收人")
	private Sm_User receiver;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	public String remark;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
