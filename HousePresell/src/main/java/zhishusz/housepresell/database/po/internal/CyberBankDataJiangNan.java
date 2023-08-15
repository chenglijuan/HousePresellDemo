package zhishusz.housepresell.database.po.internal;

/*
 * 对象名称：网银数据上传-南京银行
 * */
public class CyberBankDataJiangNan
{
	String eCode;//网银数据上传编号
	String theAccountOfBankAccountEscrowed;//托管账号
	String theNameOfBankAccountEscrowed;//托管账号名称
	String theNameOfBankBranch;//开户行
	Long tradeTimeStamp;//交易日期
	String recipientAccount;//对方账号
	String recipientName;//对方账户名称
	String summaryIfo;//摘要
	String theAccount;//账号
	Long startTimeStamp;//开始日期
	Long endTimeStamp;//结束日期
	Double income;//收入
	Double payout;//支出
	Double balance;//余额
	Integer dataState;//网银数据状态：1-上传、2-删除标记
	Long reconciliationTimeStamp;//网银对账日期
	Integer reconciliationState;//网银对账状态：0-未对账、1-已对账
	String reconciliationUser;//对账人【对账反写】
}
