package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_QualificationInfoForm;
import zhishusz.housepresell.database.dao.Emmp_QualificationInfoDao;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：资质认证信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_QualificationInfoBatchDeleteService
{
	@Autowired
	private Emmp_QualificationInfoDao emmp_QualificationInfoDao;

	public Properties execute(Emmp_QualificationInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Emmp_QualificationInfo emmp_QualificationInfo = (Emmp_QualificationInfo)emmp_QualificationInfoDao.findById(tableId);
			if(emmp_QualificationInfo == null)
			{
				return MyBackInfo.fail(properties, "'Emmp_QualificationInfo(Id:" + tableId + ")'不存在");
			}
		
			emmp_QualificationInfo.setTheState(S_TheState.Deleted);
			emmp_QualificationInfoDao.save(emmp_QualificationInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
