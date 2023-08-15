package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlDetailService
{
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;

	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_CyberBankStatementDtlId = model.getTableId();
		Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = (Tgpf_CyberBankStatementDtl)tgpf_CyberBankStatementDtlDao.findById(tgpf_CyberBankStatementDtlId);
		if(tgpf_CyberBankStatementDtl == null || S_TheState.Deleted.equals(tgpf_CyberBankStatementDtl.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatementDtl(Id:" + tgpf_CyberBankStatementDtlId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_CyberBankStatementDtl", tgpf_CyberBankStatementDtl);

		return properties;
	}
}
