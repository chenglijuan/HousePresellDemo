package zhishusz.housepresell.database.po.state;

/**
 * 类型名称：申请单状态
 */
public class S_SpecialFundBusiState
{
	
	// 1-初始（保存）
	// 2-已提交（点击提交）
	// 3-已确认（财务人员初审后）
	// 4-已统筹（财务人员统筹后）
	// 5-已退回（财务人员审批不通过）
	// 6-已审批（财务总监已审批）
	// 7-已驳回统筹（财务总监审批不通过）
	/**
	 * 初始（保存）
	 */
    public static final String Saved = "1";
    /**
     * 已提交（点击提交）
     */
	public static final String Commited = "2";
	
	/**
	 * 已确认（财务人员初审后）
	 */
    public static final String Confirmed = "3";
    /**
     * 已统筹（财务人员统筹后）
     */
	public static final String IsCoordinated = "4";
	
	/**
	 * 已退回（财务人员审批不通过）
	 */
    public static final String IsReturned = "5";
    /**
     * 已审批（财务总监已审批）
     */
	public static final String Appropriated = "6";
	
	/**
	 * 已驳回统筹（财务总监审批不通过）
	 */
    public static final String NOCoordinated = "7";
	
}
