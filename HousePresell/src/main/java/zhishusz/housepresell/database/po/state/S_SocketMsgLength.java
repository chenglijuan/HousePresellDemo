package zhishusz.housepresell.database.po.state;

/**
 * 接口报文总包长
 * @author 苏州智数
 * @sin
 *
 */
public enum S_SocketMsgLength
{
	_1001("151"),
	_1101("529"),
	_1002("653"),
	_1102("146"),
	_1008("653"),
	_1108("146"),
	_1092("250"),
	_1192("146"),
	_4001("127"),
	_4101("133"),
	_6001("183"),
	_6101("916"),
	_6002("180"),
	_6102("1016");
	
	
	private String msg;

	private S_SocketMsgLength(String msg) {
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
	public static int getMsg(String code){
		return Integer.parseInt(S_SocketMsgLength.valueOf("_" + code).getMsg());
	}
}
