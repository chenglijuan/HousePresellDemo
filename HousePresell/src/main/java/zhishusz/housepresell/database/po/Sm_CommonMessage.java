package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ITypeAnnotation(remark="公共消息")
public class Sm_CommonMessage implements Serializable
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
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@Getter @Setter @IFieldAnnotation(remark="消息类型")
	private String messageType;
	
	@Getter @Setter @IFieldAnnotation(remark="消息业务状态")
	private Integer busiState;

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;
	
	@Getter @Setter @IFieldAnnotation(remark="关联数据表ID")
	private String orgDataId;
	
	@Getter @Setter @IFieldAnnotation(remark="关联数据表eCode-冗余")
	private String orgDataCode;
	
	@Getter @Setter @IFieldAnnotation(remark="主题")
	private String theTitle;
	
	@Getter @Setter @IFieldAnnotation(remark="内容")
	private String theContent;
	
	@Getter @Setter @IFieldAnnotation(remark="扩展数据")
	private String theData;//JSON格式
	
	@Getter @Setter @IFieldAnnotation(remark="发送日期yyyy-MM-dd HH:mm:ss")
	private String sendTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	public String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="业务类型")
	private String busiKind;//业务类型
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	public Sm_CommonMessage sm_CommonMessage;						//接受信息，不存入数据库

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
