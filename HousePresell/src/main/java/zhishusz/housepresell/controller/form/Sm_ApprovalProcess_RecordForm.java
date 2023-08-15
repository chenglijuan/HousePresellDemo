package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_User;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流-审批记录
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_RecordForm extends NormalActionForm
{
	private static final long serialVersionUID = 2503625385966136520L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private Sm_ApprovalProcess_Workflow approvalProcess;//业务审批流
	@Getter @Setter
	private Long approvalProcessId;//业务审批流-Id
	@Getter @Setter
	private Sm_ApprovalProcess_Cfg configuration;//流程配置-冗余
	@Getter @Setter
	private Long configurationId;//流程配置-冗余-Id
	@Getter @Setter
	private Sm_User userOperate;//审批人
	@Getter @Setter
	private Long userOperateId;//审批人-Id
	@Getter @Setter
	private String theContent;//审批内容
	@Getter @Setter
	private List attachmentList;//附件
	@Getter @Setter
	private Long operateTimeStamp;//操作时间点
	@Getter @Setter
	private Long[]  workflowIdArray; //结点id数组
	@Getter @Setter
	private Long workflowTime;//驳回时，更新上一个结点最后操作时间
	@Getter @Setter
	private Map<String,Object> extraObj;
	@Getter @Setter
	private String isSign;//是否签章通过

}
