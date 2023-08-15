package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Node;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流-节点
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_NodeForm extends NormalActionForm
{
	private static final long serialVersionUID = 4226623144890816385L;
	
	@Getter @Setter
	private Long tableId;//表ID
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
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//结点编号
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private Long roleId;//角色-Id
	@Getter @Setter
	private Integer orderNumber;//排序
	@Getter @Setter
	private Sm_ApprovalProcess_Cfg configuration;//流程配置-冗余
	@Getter @Setter
	private Long configurationId;//流程配置-冗余
	@Getter @Setter
	private Sm_ApprovalProcess_Workflow workflow;//审批流程
	@Getter @Setter
	private Long workflowId;//审批流程
	@Getter @Setter
	private Sm_ApprovalProcess_Node lastNote;//上一个节点
	@Getter @Setter
	private Long lastNoteId;//上一个节点Id
	@Getter @Setter 
	private Sm_ApprovalProcess_Node nextNote;//下一个节点
	@Getter @Setter
	private Long nextNoteId;//下一个节点Id
	@Getter @Setter
	private Integer nodeType;//结点类型
	@Getter @Setter
	private Integer approvalModel;//审批模式
	@Getter @Setter
	private Integer finishPercentage;//会签完成阀值
	@Getter @Setter
	private Integer passPercentage;//会签通过阀值
	@Getter @Setter
	private Integer rejectModel;//驳回模式 0: 驳回到起草人 1: 驳回到上一级
	@Getter @Setter
	private Long[] checkedMessageTemplateId;//选中的消息模板Id
	@Getter @Setter
	private Sm_ApprovalProcess_ConditionForm[] sm_ApprovalProcess_ConditionList;//审批条件
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
