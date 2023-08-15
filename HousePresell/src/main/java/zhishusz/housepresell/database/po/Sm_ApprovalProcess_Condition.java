package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ITypeAnnotation(remark="审批流-审批条件")
public class Sm_ApprovalProcess_Condition implements Serializable
{
	private static final long serialVersionUID = -5467498872279380048L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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

	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="暂存态 S_TheState")
	private Integer temporaryState;  //  暂存 ：1   正确保存：0

	@Getter @Setter @IFieldAnnotation(remark="关联节点")
	private Sm_ApprovalProcess_Node approvalProcess_node;

	@Getter @Setter @IFieldAnnotation(remark="条件内容")
	private String theContent;

	@Getter @Setter @IFieldAnnotation(remark="下一步骤")
	private Long nextStep; //节点Id

	@Getter @Setter @IFieldAnnotation(remark="下一步骤名称")
	private String nextStepName; //节点名称

}
