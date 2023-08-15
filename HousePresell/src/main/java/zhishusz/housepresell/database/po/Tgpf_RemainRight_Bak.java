package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：留存权益(此表为留存权益计算时临时表)
 * TODO：表字段未整理
 */
@ITypeAnnotation(remark="留存权益(此表为留存权益计算时临时表)")
public class Tgpf_RemainRight_Bak implements Serializable
{
	private static final long serialVersionUID = 3765794380690767591L;
    
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
