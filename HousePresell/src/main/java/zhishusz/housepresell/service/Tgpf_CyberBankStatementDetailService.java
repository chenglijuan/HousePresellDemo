package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_CyberBankStatementDetailService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_CyberBankStatementId = model.getTableId();
		Tgpf_CyberBankStatement tgpf_CyberBankStatement = (Tgpf_CyberBankStatement)tgpf_CyberBankStatementDao.findById(tgpf_CyberBankStatementId);
		if(tgpf_CyberBankStatement == null || S_TheState.Deleted.equals(tgpf_CyberBankStatement.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Tgpf_CyberBankStatement(Id:" + tgpf_CyberBankStatementId + ")'不存在");
		}
		
		//查询网银上传详细的数据
		Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
		form.setMainTableId(tgpf_CyberBankStatementId);
		form.setMainTable(tgpf_CyberBankStatement);
		
		List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementDtlList = tgpf_CyberBankStatementDtlDao.findByPage(tgpf_CyberBankStatementDtlDao.getQuery(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
		if(null == tgpf_CyberBankStatementDtlList || tgpf_CyberBankStatementDtlList.size() == 0){
			tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_CyberBankStatement", tgpf_CyberBankStatement);
		properties.put("tgpf_CyberBankStatementDtlList", tgpf_CyberBankStatementDtlList);

		return properties;
	}
}
