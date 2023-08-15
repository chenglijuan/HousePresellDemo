package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_FastNavigateListsService
{
	@Autowired
	private Sm_FastNavigateDao sm_FastNavigateDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	private static final Log log = 
			LogFactory.getLog(Sm_FastNavigateListsService.class.getName());
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_FastNavigateForm model)
	{
		Properties properties = new MyProperties();
		//获取当前登录着
		Sm_User currentuser=model.getUser();
		if(currentuser == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		//根据当前的登录着获取登录着的角色(一个用户有多种角色)
		//获取当前没有删除的角色	
		Sm_Permission_RoleUserForm	sm_Permission_RoleUserForm=new Sm_Permission_RoleUserForm();
		sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
		sm_Permission_RoleUserForm.setUser(currentuser);
		List<Sm_Permission_RoleUser> Sm_Permission_RoleUserList= sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQLByuser(), sm_Permission_RoleUserForm).getResultList();
		List<Object> lists=new ArrayList<Object>();
		List<Sm_Permission_UIResource> outSm_Permission_UIResourceList=new ArrayList<Sm_Permission_UIResource>();
		if(Sm_Permission_RoleUserList.size()>0){
			for(Sm_Permission_RoleUser Sm_Permission_RoleUser:Sm_Permission_RoleUserList){	
				if(Sm_Permission_RoleUser.getSm_Permission_Role().getUiResourceList()!=null){
					List<Sm_Permission_UIResource> Sm_Permission_UIResourceList=Sm_Permission_RoleUser.getSm_Permission_Role().getUiResourceList();		
					lists.add(Sm_Permission_UIResourceList);	
				}				
						
			}
			List<Sm_Permission_UIResource> Sm_Permission_UIResourceList1=null;
			//获取UI权限资源中最多的一个
			for(int i=0;i<lists.size();i++){				
				Sm_Permission_UIResourceList1=(List<Sm_Permission_UIResource>) lists.get(0);
				List<Sm_Permission_UIResource> Sm_Permission_UIResourceList2=(List<Sm_Permission_UIResource>) lists.get(i);
				int max=Sm_Permission_UIResourceList1.size();
				if(Sm_Permission_UIResourceList2.size()>max){
					Sm_Permission_UIResourceList1=Sm_Permission_UIResourceList2;
				}
			}
			if(Sm_Permission_UIResourceList1!=null){
				//排除权限资源中TheType不是菜单的
				for(Sm_Permission_UIResource Sm_Permission_UIResourceList:Sm_Permission_UIResourceList1){
					if(Sm_Permission_UIResourceList.getTheType().equals(S_UIResourceType.RealityMenu)||Sm_Permission_UIResourceList.getTheType().equals(S_UIResourceType.VirtualMenu)){
						outSm_Permission_UIResourceList.add(Sm_Permission_UIResourceList);
					}
				}
			}	   
		}
		//菜单变更后删除多余的
		Long tableId=currentuser.getTableId();
		Sm_FastNavigateForm fastNavigateForm=new Sm_FastNavigateForm();
		fastNavigateForm.setUserTableId(tableId);
		List<Sm_FastNavigate> Sm_FastNavigateList=sm_FastNavigateDao.getQuery(sm_FastNavigateDao.getBasicHQL(), fastNavigateForm).getResultList();		
		if(outSm_Permission_UIResourceList.size()==0){
			
			if(Sm_FastNavigateList!=null){
				for(Sm_FastNavigate Sm_FastNavigate:Sm_FastNavigateList){
					//首先删除这个用户的  快捷导航信息
					sm_FastNavigateDao.delete(Sm_FastNavigate);			
				}
			}		
		}else if(outSm_Permission_UIResourceList.size()<Sm_FastNavigateList.size()){
			for(Sm_FastNavigate sm_FastNavigate:Sm_FastNavigateList ){
				Boolean flag=false;
				for(Sm_Permission_UIResource outSm_Permission_UIResource: outSm_Permission_UIResourceList){
					if(sm_FastNavigate.getTheNameOfMenu().equals(outSm_Permission_UIResource.getTheName())){
						flag=true;
					}
				}
				if(!flag){
					sm_FastNavigateDao.delete(sm_FastNavigate);		
				}
			}
		}
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		model.setUserTableId(model.getUser().getTableId());	
		log.info("UserTableId:  "+model.getUserId());		
		Integer totalCount = sm_FastNavigateDao.findByPage_Size(sm_FastNavigateDao.getQuery_Size(sm_FastNavigateDao.getBasicHQLByList(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_FastNavigate> sm_FastNavigateList;
		if(totalCount > 0)
		{
			sm_FastNavigateList = sm_FastNavigateDao.findByPage(sm_FastNavigateDao.getQuery(sm_FastNavigateDao.getBasicHQLByList(), model), pageNumber, countPerPage);		
		}
		else
		{
			sm_FastNavigateList = new ArrayList<Sm_FastNavigate>();
		}
		
		properties.put("sm_FastNavigateList", sm_FastNavigateList);

		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
