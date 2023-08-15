package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="审批流-审批记录")
public class Sm_ApprovalProcess_Record implements Serializable
{
	private static final long serialVersionUID = 7716776048281749025L;

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

	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="业务审批流")
	private Sm_ApprovalProcess_Workflow approvalProcess;

	@Getter @Setter @IFieldAnnotation(remark="流程配置-冗余")
	private Sm_ApprovalProcess_Cfg configuration;

	@Getter @Setter @IFieldAnnotation(remark="审批人")
	private Sm_User userOperate;
	
	@Getter @Setter @IFieldAnnotation(remark="审批内容")
	private String theContent;

//	@Getter @Setter @IFieldAnnotation(remark="附件")
//	private List<Sm_Attachment> attachmentList;
	
	@Getter @Setter @IFieldAnnotation(remark="审批动作")
	private Integer theAction;//通过: 0 、驳回 : 1
	
	@Getter @Setter @IFieldAnnotation(remark="操作时间点")
	private Long operateTimeStamp;

}
