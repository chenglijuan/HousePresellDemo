package zhishusz.housepresell.database.po.state;

/**
 * 类型名称：用款申请单状态
 */
public class S_ApplyState
{
    public static final Integer Application = 1;   			// 申请中
	public static final Integer Admissible = 2;      		// 已受理
	public static final Integer Overallplanning = 3; 	    // 统筹中
	public static final Integer Alreadycoordinated = 4;	   	// 已统筹
	public static final Integer Disbursement = 5;	  		// 拨付中
	public static final Integer Alreadydisbursed = 6;	  	// 已拨付
	public static final Integer Rescinded = 9;	   			// 已撤销
}
