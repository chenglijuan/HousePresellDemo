package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zhishusz.housepresell.controller.form.Sm_FastNavigateForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Sm_FastNavigateDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Sm_FastNavigate;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_Permission_UIResource;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UIResourceType;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.comparator.MyUIResourceComparator;

/*
 * Service列表查询：快捷导航信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_FastNavigateListsByUserService
{

	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Sm_FastNavigateDao Sm_FastNavigateDao;

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
			List<Sm_Permission_UIResource> Sm_Permission_UIResourceList3=null;
			//记住最大的一个序列
			int index=0;
			//获取UI权限资源中最多的一个
			for(int i=0;i<lists.size();i++){				
				Sm_Permission_UIResourceList1=(List<Sm_Permission_UIResource>) lists.get(0);
				List<Sm_Permission_UIResource> Sm_Permission_UIResourceList2=(List<Sm_Permission_UIResource>) lists.get(i);
				int max=Sm_Permission_UIResourceList1.size();
				if(Sm_Permission_UIResourceList2.size()>max){
					Sm_Permission_UIResourceList1=Sm_Permission_UIResourceList2;
					index=i;
				}
			}
			//根据最多的一个进行比较，并且合并
			if(!Sm_Permission_UIResourceList1.isEmpty()) {
				for(int i=0;i<lists.size();i++) {
					if(i!=index) {
						Sm_Permission_UIResourceList3=(List<Sm_Permission_UIResource>) lists.get(i);
						for(Sm_Permission_UIResource SmPermissionUIResource:Sm_Permission_UIResourceList3) {
							if(!Sm_Permission_UIResourceList1.contains(SmPermissionUIResource)) {
								Sm_Permission_UIResourceList1.add(SmPermissionUIResource);
							}
						}
					}
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
		//排序（根据 theIndex 和 tableId排序）
		//先按theIndex排序（小的在前面）
		//若值一样，按照tableId小的排上面
		Collections.sort(outSm_Permission_UIResourceList, new MyUIResourceComparator());
	
		//查询当前登录着的 列表
		Long tableId=currentuser.getTableId();
		Sm_FastNavigateForm fastNavigateForm=new Sm_FastNavigateForm();
		fastNavigateForm.setUserTableId(tableId);
		List<Sm_FastNavigate> Sm_FastNavigateList=Sm_FastNavigateDao.getQuery(Sm_FastNavigateDao.getBasicHQL(), fastNavigateForm).getResultList();
		String idArrs="";
		for(Sm_FastNavigate Sm_FastNavigate:Sm_FastNavigateList){
			idArrs+=Sm_FastNavigate.getMenuTableId()+",";
		}
		if(idArrs.length()>0){
			idArrs=idArrs.substring(0, idArrs.length()-1);			
		}
	
		
		properties.put("idArr", idArrs);									
		properties.put("sm_FastNavigateList", outSm_Permission_UIResourceList);				
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
