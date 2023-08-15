package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoBatchDeletesService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;

	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Empj_HouseInfo empj_HouseInfo = (Empj_HouseInfo)empj_HouseInfoDao.findById(tableId);
			if(empj_HouseInfo == null)
			{
				return MyBackInfo.fail(properties, "'Empj_HouseInfo(Id:" + tableId + ")'不存在");
			}
		
			empj_HouseInfo.setTheState(S_TheState.Deleted);
			if(empj_HouseInfo.getBuilding()!=null){
				if(null==empj_HouseInfo.getBuilding().getSumFamilyNumber()){
					empj_HouseInfo.getBuilding().setSumFamilyNumber(0);
				}else if(empj_HouseInfo.getBuilding().getSumFamilyNumber()>0){
					empj_HouseInfo.getBuilding().setSumFamilyNumber(empj_HouseInfo.getBuilding().getSumFamilyNumber()-1);
				}				
			}
			empj_HouseInfoDao.save(empj_HouseInfo);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
