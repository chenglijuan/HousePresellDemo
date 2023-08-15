package zhishusz.housepresell.messagequeue.consumer;

import zhishusz.housepresell.util.MQConnectionUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Dechert on 2018-08-09.
 * Company: zhishusz
 */

public abstract class BaseConsumer
{
	@Autowired
	protected Gson gson;
	@Autowired
	MQConnectionUtil mqConnectionUtil;

	/**
	 * 在这里填写项目开始时要加载的内容
	 */
	public abstract void onInit();
}
