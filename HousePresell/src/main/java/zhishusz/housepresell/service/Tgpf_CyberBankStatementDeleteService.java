package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDeleteService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;

	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_CyberBankStatementId = model.getTableId();
		Tgpf_CyberBankStatement tgpf_CyberBankStatement = (Tgpf_CyberBankStatement)tgpf_CyberBankStatementDao.findById(tgpf_CyberBankStatementId);
		if(tgpf_CyberBankStatement == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatement(Id:" + tgpf_CyberBankStatementId + ")'不存在");
		}
		
		tgpf_CyberBankStatement.setTheState(S_TheState.Deleted);
		tgpf_CyberBankStatementDao.save(tgpf_CyberBankStatement);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
