package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_SocketMsgForm;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：接口报文表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SocketMsgDeleteService
{
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

	public Properties execute(Tgpf_SocketMsgForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_SocketMsgId = model.getTableId();
		Tgpf_SocketMsg tgpf_SocketMsg = (Tgpf_SocketMsg)tgpf_SocketMsgDao.findById(tgpf_SocketMsgId);
		if(tgpf_SocketMsg == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_SocketMsg(Id:" + tgpf_SocketMsgId + ")'不存在");
		}
		
		tgpf_SocketMsg.setTheState(S_TheState.Deleted);
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
