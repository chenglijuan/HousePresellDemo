package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：管理角色
 * Company：ZhiShuSZ
 * */
@Service
public class Sm_Permission_RoleRebuild extends RebuilderBase<Sm_Permission_Role>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Override
	public Properties getSimpleInfo(Sm_Permission_Role object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//列表页面
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		//业务状态：（1:启用 ，0：停用） S_ValidState
		properties.put("busiType", object.getBusiType());
		properties.put("enableDate",myDatetime.dateToString(object.getEnableTimeStamp()));
		properties.put("downDate",myDatetime.dateToString(object.getDownTimeStamp()));
		properties.put("companyType", object.getCompanyType());
		
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", object.getCompanyType());
		if(sm_BaseParameter != null)
		{
			properties.put("sm_BaseParameter", sm_BaseParameter);
			properties.put("parameterName", sm_BaseParameter.getTheName());
		}

		return properties;
	}

	@Override
	public Properties getDetail(Sm_Permission_Role object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("tableId",object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		if(object.getUserStart() !=null)
		{
			properties.put("userStart",object.getUserStart().getTheName());
		}
		properties.put("busiType",object.getBusiType());
		properties.put("remark", object.getRemark());
		properties.put("theState", object.getTheState());
		properties.put("createTimeStamp",myDatetime.dateToString2(object.getCreateTimeStamp()));
		properties.put("enableTimeStamp",myDatetime.dateToSimpleString(object.getEnableTimeStamp()));
		properties.put("enableDate", myDatetime.dateToSimpleString(object.getEnableTimeStamp()));
		properties.put("downTimeStamp",myDatetime.dateToSimpleString(object.getDownTimeStamp()));
		properties.put("downDate", myDatetime.dateToSimpleString(object.getDownTimeStamp()));
		properties.put("companyType", object.getCompanyType());
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", object.getCompanyType());
		if(sm_BaseParameter != null)
		{
			properties.put("sm_BaseParameter", sm_BaseParameter);
			properties.put("parameterName", sm_BaseParameter.getTheName());
		}
		Sm_Permission_RoleUserForm sm_Permission_RoleUserForm = new Sm_Permission_RoleUserForm();
		sm_Permission_RoleUserForm.setSm_Permission_RoleId(object.getTableId());
		sm_Permission_RoleUserForm.setTheState(S_TheState.Normal);
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_Permission_Role> sm_Permission_RoleList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_RoleList != null)
		{
			for(Sm_Permission_Role object:sm_Permission_RoleList)
			{
				Properties properties = new MyProperties();
				
				//---------公共字段-Start---------//
				Long tableId = object.getTableId();
				String eCode = object.geteCode();
				//---------公共字段-Start---------//
				String	theName = object.getTheName();
				//String uiPermissionJson = object.getUiPermissionJson();
				String busiType = object.getBusiType();//"业务状态：（1:启用 ，0：停用） S_ValidState")
				Long enableTimeStamp = object.getEnableTimeStamp();//启用时间
				Long downTimeStamp = object.getDownTimeStamp();//停用时间
				properties.put("enableTimeStamp", enableTimeStamp);
				properties.put("downTimeStamp", downTimeStamp);

				if(tableId != null)
				{
					properties.put("tableId", tableId);
				}
				if(eCode != null)
				{
					properties.put("eCode", eCode);
				}
				if(theName != null)
				{
					properties.put("theName", theName);
				}
				if(busiType != null)
				{
					properties.put("busiType", busiType);
				}
				if(enableTimeStamp != null)
				{
					properties.put("enableTimeStamp", myDatetime.dateToString(enableTimeStamp));
				}
				if(downTimeStamp != null)
				{
					properties.put("downTimeStamp", myDatetime.dateToString(downTimeStamp));
				}
				
				list.add(properties);
			}
		}
		return list;
	}

	@Override
	public List<Properties> executeForSelectList(List<Sm_Permission_Role> sm_Permission_RoleList) {
		List<Properties> list = new ArrayList<Properties>();
		if(sm_Permission_RoleList != null)
		{
			for(Sm_Permission_Role object:sm_Permission_RoleList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				properties.put("busiType", object.getBusiType());

				list.add(properties);
			}
		}
		return list;
	}
	
	public Properties getDetail3(Sm_Permission_Role object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		//详情页面
		properties.put("tableId",object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}
}
