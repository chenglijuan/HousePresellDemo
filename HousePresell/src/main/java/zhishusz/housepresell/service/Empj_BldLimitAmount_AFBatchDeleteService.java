package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：申请表-受限额度变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldLimitAmount_AFBatchDeleteService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;

	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}
		String po_ret;
		for(Long tableId : idArr)
		{
			po_ret = "0";
			Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF)empj_BldLimitAmount_AFDao.findById(tableId);
			if(empj_BldLimitAmount_AF == null)
			{
				return MyBackInfo.fail(properties, "选择的单据号不存在");
			}
		
			/**
			 * XSZ by 2019年8月6日10:39:41
			 * BUG#4519 工程进度节点更显，被驳回过的业务部，流转到开发企业后，不允许删除，普通未提交的业务 可以删除
			 */
			try {
				po_ret = empj_BldLimitAmount_AFDao.PRO_CHECKISREJECT(tableId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if("1".equals(po_ret)){
				return MyBackInfo.fail(properties, "单据号："+ empj_BldLimitAmount_AF.geteCode() +"被驳回，不可删除！");
			}
			
			/**
			 * XSZ by 2019年8月6日10:39:41
			 * BUG#4519 工程进度节点更显，被驳回过的业务部，流转到开发企业后，不允许删除，普通未提交的业务 可以删除
			 */
			
			if( null != empj_BldLimitAmount_AF.getApprovalState() && S_ApprovalState.WaitSubmit.equals(empj_BldLimitAmount_AF.getApprovalState()))
			{
				empj_BldLimitAmount_AF.setTheState(S_TheState.Deleted);
				empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
