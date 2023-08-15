package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="审批流-节点")
public class Sm_ApprovalProcess_Node implements Serializable
{
	private static final long serialVersionUID = -3299552601112285475L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;  // 0 ：已办   1 ： 代办

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

	@IFieldAnnotation(remark="结点编号")
	private String eCode;

	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="暂存态 S_TheState")
	private Integer temporaryState;  //  暂存 ：1   正确保存：0

	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;

	@Getter @Setter @IFieldAnnotation(remark="角色")
	private Sm_Permission_Role role;

	@Getter @Setter @IFieldAnnotation(remark="流程配置-冗余")
	private Sm_ApprovalProcess_Cfg configuration;

	@Getter @Setter @IFieldAnnotation(remark="结点类型")
	private Integer nodeType; // 0：第一个结点 1：其他结点

	@Getter @Setter @IFieldAnnotation(remark="上一个节点")
	private Sm_ApprovalProcess_Node lastNode;
	
	@Getter @Setter @IFieldAnnotation(remark="下一个节点")
	private Sm_ApprovalProcess_Node nextNode;

	@Getter @Setter @IFieldAnnotation(remark="审批模式")
	private Integer approvalModel; // 0：抢占 1：会签

	@Getter @Setter @IFieldAnnotation(remark="会签完成阀值")
	private Integer finishPercentage;

	@Getter @Setter @IFieldAnnotation(remark="会签通过阀值")
	private Integer passPercentage;

	@Getter @Setter @IFieldAnnotation(remark="驳回模式 S_RejectModel")
	private Integer rejectModel; // 0: 驳回到起草人 1: 驳回到上一级

	@Getter @Setter @IFieldAnnotation(remark="审批条件")
	private List<Sm_ApprovalProcess_Condition> approvalProcess_conditionList;

	@Getter @Setter @IFieldAnnotation(remark="消息模板配置列表")
	public List<Sm_MessageTemplate_Cfg> sm_messageTemplate_cfgList;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
