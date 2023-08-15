package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.dao.Sm_Permission_UIResourceDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_FastNavigateAddByUserService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;
	@Autowired
	private Sm_Permission_UIResourceDao sm_Permission_UIResourceDao;
	
	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();	
		//获取当前登录者
		Sm_User currentuser=model.getUser();
		if(currentuser == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登陆！");
		}
		//查询当前登录着的 列表
		Long tableId=currentuser.getTableId();
		Sm_FastNavigateForm fastNavigateForm=new Sm_FastNavigateForm();
		fastNavigateForm.setUserTableId(tableId);
		List<Sm_FastNavigate> Sm_FastNavigateList=sm_FastNavigateDao.getQuery(sm_FastNavigateDao.getBasicHQL(), fastNavigateForm).getResultList();		
		if(Sm_FastNavigateList!=null){
			for(Sm_FastNavigate Sm_FastNavigate:Sm_FastNavigateList){
				//首先删除这个用户的  快捷导航信息
				sm_FastNavigateDao.delete(Sm_FastNavigate);
			}
		}
		//获取前台传过来的id
		Long[] idArrs= model.getIdArr();
		
		if(idArrs!=null && idArrs.length>0){							
			for(int i=0;i<idArrs.length;i++){
				Sm_FastNavigate fastNavigate=new Sm_FastNavigate();
				Sm_Permission_UIResource sm_Permission_UIResource= sm_Permission_UIResourceDao.findById(idArrs[i]);
				//菜单链接地址
				String theLinkOfMenu=sm_Permission_UIResource.getTheResource();				
				if(sm_Permission_UIResource.getTheType()==1)
				{
					fastNavigate.setTheLinkOfMenu(theLinkOfMenu);								
					//菜单名称
					fastNavigate.setTheNameOfMenu(sm_Permission_UIResource.getTheName());
					//菜单Id
					fastNavigate.setMenuTableId(sm_Permission_UIResource.getTableId());
					//创建人
					fastNavigate.setUserStart(model.getUser());
					//用户的id
					fastNavigate.setUserTableId(model.getUser().getTableId());
					//创建时间
					fastNavigate.setCreateTimeStamp(System.currentTimeMillis());
					
					sm_FastNavigateDao.save(fastNavigate);
				}
			}
		}
				
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
