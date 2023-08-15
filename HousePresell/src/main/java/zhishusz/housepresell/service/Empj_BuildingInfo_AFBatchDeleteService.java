package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfo_AFForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfo_AFDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：申请表-楼幢变更
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfo_AFBatchDeleteService
{
	@Autowired
	private Empj_BuildingInfo_AFDao empj_BuildingInfo_AFDao;

	public Properties execute(Empj_BuildingInfo_AFForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_BuildingInfo_AF empj_BuildingInfo_AF = (Empj_BuildingInfo_AF)empj_BuildingInfo_AFDao.findById(tableId);
			if(empj_BuildingInfo_AF == null)
			{
				return MyBackInfo.fail(properties, "'Empj_BuildingInfo_AF(Id:" + tableId + ")'不存在");
			}
		
			empj_BuildingInfo_AF.setTheState(S_TheState.Deleted);
			empj_BuildingInfo_AFDao.save(empj_BuildingInfo_AF);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
