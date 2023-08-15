package zhishusz.housepresell.database.po.internal;

//可被添加日志的
public interface ILogable
{
	//获取日志Id
	Long getLogId();
	//设置日志Id
	void setLogId(Long logId);
	//返回需要记录到日志中的数据（Json格式）
	String getLogData();
}
