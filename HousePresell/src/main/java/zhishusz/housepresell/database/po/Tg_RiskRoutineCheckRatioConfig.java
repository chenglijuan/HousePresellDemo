package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：风控例行抽查比例配置表
 */
@ITypeAnnotation(remark="风控例行抽查比例配置表")
public class Tg_RiskRoutineCheckRatioConfig implements Serializable
{
	private static final long serialVersionUID = 7081679858222877801L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="业务大类值")
	private String largeBusinessValue;
	
	@Getter @Setter @IFieldAnnotation(remark="业务大类名")
	private String largeBusinessName;
	
	@Getter @Setter @IFieldAnnotation(remark="业务小类值")
	private String subBusinessValue;
	
	@Getter @Setter @IFieldAnnotation(remark="业务小类名")
	private String subBusinessName;
	
	@Getter @Setter @IFieldAnnotation(remark="角色")
	private Sm_Permission_Role role;
	
	@Getter @Setter @IFieldAnnotation(remark="抽查比例（%）")
	private Integer theRatio;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
}
