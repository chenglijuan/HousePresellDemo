package zhishusz.housepresell.database.po.state;

/**
 * 类型名称：房屋状态
 */
public class S_HouseBusiState
{
	public static final String Establish = "1";//已搭建
	public static final String ApprovalSale = "2";//已批准预售
	public static final String ContractSign = "3";//合同已签订
	public static final String ContractRecord = "4";//合同已备案
	public static final String PropertyRight = "5";//已办产权
	public static final String EmptyHouse = "6";//空户（楼盘表补户室专用）
	
	public static final String[] HouseBusiStateStrArr = {"已搭建","已批准预售","合同已签订","合同已备案","已办产权"};
}
