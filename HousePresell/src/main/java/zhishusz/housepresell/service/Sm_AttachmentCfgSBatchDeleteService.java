package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgSBatchDeleteService {
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao Sm_BaseParameterDao;
	
	@Autowired
	private Sm_UserDao sm_UserDao;

	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		/*Long userUpdateId = model.getUserId();
		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}

		// 查询关联修改人信息
		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "查询登录人信息为空");
		}*/
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			//获取Sm_BaseParameter表中的TheValue
			Sm_BaseParameter sm_BaseParameter = Sm_BaseParameterDao.findById(tableId);
			if(sm_BaseParameter == null)
			{
				return MyBackInfo.fail(properties, "'Sm_AttachmentCfg(Id:" + tableId + ")'不存在");
			}
			Sm_AttachmentCfgForm sm_AttachmentCfgForm=new Sm_AttachmentCfgForm();
			sm_AttachmentCfgForm.setBusiType(sm_BaseParameter.getTheValue());
		    //根据theName的值获取 Sm_AttachmentCfg表中的信息
			List<Sm_AttachmentCfg> Sm_AttachmentCfgList=sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm).list();			
			for(Sm_AttachmentCfg sm_AttachmentCfg:Sm_AttachmentCfgList){
				/*//设置修改人
				sm_AttachmentCfg.setUserUpdate(userUpdate);*/
				//设置最后一次修改时间
				sm_AttachmentCfg.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentCfg.setTheState(S_TheState.Deleted);
				sm_AttachmentCfgDao.save(sm_AttachmentCfg);				
			}			
		}			
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
