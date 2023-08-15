package zhishusz.housepresell.database.po.state;


/**
 * 接口错误代码
 * @description TODO 接口错误代码
 * @author wuyu
 * @date 2018年8月9日15:58:38
 */
public enum S_SocketErrCode
{
	_0000("交易成功"),
	_0001("数据包格式错误"),
	_0002("区域代码不存在"),
	_0003("交易代码错误"),
	_0004("金额超限"),
	_0005("流水号重复"),
	_0006("收款方账户错误"),
	_0007("收款方名称错误"),
	_0008("付款方名称错误"),
	_0009("付款方账户错误"),
	_0010("网络连接超时"),
	_0011("MD5校验失败"),
	_0012("不允许红冲"),
	_0013("总包长错误"),
	_0014("三方协议或合同备案号无效！"),
	_0015("合同受限"),
	_0016("存在他行贷款"),
	_0017("贷款金额超限"),
	_0018("无效的通知书编号"),
	_0019("文件名错误"),
	_0020("文件内容格式错误"),
	_0021("文件记录错误"),
	_0022("文件对账失败"),
	_9999("其他异常");
	
	private String msg;

	private S_SocketErrCode(String msg) {
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
		return S_SocketErrCode.valueOf("_" + code).getMsg();
	}
	
//	_0014("输入的三方协议不存在，请核对！"),
}
