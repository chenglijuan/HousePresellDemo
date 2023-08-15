package zhishusz.housepresell.database.po.state;

public enum S_UnitType
{
	_1("甲单元"),
	_2("乙单元"),
	_3("丙单元"),
	_4("丁单元"),
	_5("戊单元"),
	_6("己单元"),
	_7("庚单元"),
	_8("辛单元"),
	_9("壬单元"),
	_10("癸单元");
	
	private String msg;

	private S_UnitType(String msg) {
		this.msg = msg;
	}

	/** 
	 * 获取msg  
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * 获取msg  
	 * @param code 代码
	 * @return
	 */
	public static String getMsg(String code){
		return S_UnitType.valueOf("_" + code).getMsg();
	}
	
	
}
