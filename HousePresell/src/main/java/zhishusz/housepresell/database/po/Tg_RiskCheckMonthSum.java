package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：风控例行抽查月汇总表
 */
@ITypeAnnotation(remark="风控例行抽查月汇总表")
public class Tg_RiskCheckMonthSum implements Serializable
{
	private static final long serialVersionUID = -3839007557500155229L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
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
	//---------公共字段-End---------//
	
	@Getter @Setter @IFieldAnnotation(remark="风控例行抽查单号")
	private String checkNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="抽查日期")
	private Long spotTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="业务")
	private Integer sumCheckCount;
	
	@Getter @Setter @IFieldAnnotation(remark="合格")
	private Integer qualifiedCount;
	
	@Getter @Setter @IFieldAnnotation(remark="不合格")
	private Integer unqualifiedCount;
	
	@Getter @Setter @IFieldAnnotation(remark="整改推送")
	private Integer pushCount;
	
	@Getter @Setter @IFieldAnnotation(remark="整改反馈")
	private Integer feedbackCount;
	
	@Getter @Setter @IFieldAnnotation(remark="完成整改")
	private Integer handleCount;
	
	@Getter @Setter @IFieldAnnotation(remark="整改状态 S_RectificationState")
	private String rectificationState;
}
