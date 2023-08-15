package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;

/*
 * Service更新操作：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlUpdateService
{
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		Properties properties = new MyProperties();
		
		// 获取需要关联的详情ID
		Long[] idArr = model.getIdArr();
		//修改人信息
		Sm_User sm_User = new Sm_User();
		sm_User.setTableId(123456L);
		
		if(null != idArr && idArr.length > 0){
			//关联详情
			for (Long tgpf_CyberBankStatementDelId : idArr)
			{
				Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = tgpf_CyberBankStatementDtlDao.findById(tgpf_CyberBankStatementDelId);
				tgpf_CyberBankStatementDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgpf_CyberBankStatementDtl.setUserUpdate(sm_User);
				tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
				
				tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
