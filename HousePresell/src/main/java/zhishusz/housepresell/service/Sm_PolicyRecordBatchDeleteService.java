package zhishusz.housepresell.service;import zhishusz.housepresell.controller.form.Sm_PolicyRecordForm;import zhishusz.housepresell.database.dao.Sm_PolicyRecordDao;import zhishusz.housepresell.database.po.Sm_PolicyRecord;import zhishusz.housepresell.database.po.Sm_User;import zhishusz.housepresell.database.po.state.S_NormalFlag;import zhishusz.housepresell.database.po.state.S_TheState;import zhishusz.housepresell.util.MyBackInfo;import zhishusz.housepresell.util.MyProperties;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.Properties;import javax.transaction.Transactional;/* * Service批量删除：政策公告 * Company：ZhiShuSZ * */@Service@Transactionalpublic class Sm_PolicyRecordBatchDeleteService{	@Autowired	private Sm_PolicyRecordDao sm_PolicyRecordDao;//政策公告		public Properties execute(Sm_PolicyRecordForm model)	{		Properties properties = new MyProperties();				Sm_User user = model.getUser();		if (null == user)		{			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");		}		Long[] idArr = model.getIdArr();				if(idArr == null || idArr.length < 1)		{			return MyBackInfo.fail(properties, "没有需要删除的信息");		}		for(Long tableId : idArr)		{			Sm_PolicyRecord sm_PolicyRecord = (Sm_PolicyRecord)sm_PolicyRecordDao.findById(tableId);			if(sm_PolicyRecord == null)			{							}			else			{				sm_PolicyRecord.setUserUpdate(user);				sm_PolicyRecord.setLastUpdateTimeStamp(System.currentTimeMillis());				sm_PolicyRecord.setTheState(S_TheState.Deleted);				sm_PolicyRecordDao.save(sm_PolicyRecord);			}					}				properties.put(S_NormalFlag.result, S_NormalFlag.success);		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);		return properties;	}}