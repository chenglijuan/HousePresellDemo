package zhishusz.housepresell.database.po.state;

/**
 * 类型名称：审批流程状态
 */
public class S_ApprovalState
{
	public static final String WaitSubmit = "待提交";//待提交0
	public static final String Examining = "审核中";//审核中1
	public static final String Completed = "已完结";//已完结2
	public static final String NoPass = "不通过";//不通过3
	
	public static final String WaitSubmit_Enum = "0";//待提交0
	public static final String Examining_Enum = "1";//审核中1
	public static final String Completed_Enum = "2";//已完结2
	public static final String NoPass_Enum = "3";//不通过3
}
