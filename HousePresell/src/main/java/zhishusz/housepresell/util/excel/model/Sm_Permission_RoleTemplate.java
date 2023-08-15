package zhishusz.housepresell.util.excel.model;

import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.state.S_ValidState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyString;

import lombok.Getter;
import lombok.Setter;

public class Sm_Permission_RoleTemplate implements IExportExcel<Sm_Permission_Role>
{
	@IFieldAnnotation(remark="角色编码")
	private String eCode;
	
	@Getter @Setter @IFieldAnnotation(remark="角色名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="启用日期")
	private String enableDateTime;
	
	@Getter @Setter @IFieldAnnotation(remark="停用日期")
	private String downDateTime;

	@Getter @Setter @IFieldAnnotation(remark="状态：（1:启用 ，0：停用） S_ValidState")
	private String busiType;
	
	private MyString  myString = MyString.getInstance();
	private MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public String toString()
	{
		return "{\"eCode\":\"" + eCode + "\",\"theName\":\"" + theName + "\",\"enableDateTime\":\"" + enableDateTime
				+ "\",\"downDateTime\":\"" + downDateTime + "\",\"busiType\":\"" + busiType + "\"}";
	}

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("eCode", "角色编码");
		map.put("theName", "角色名称");
		map.put("enableDateTime", "启用日期");
		map.put("downDateTime", "终止日期");
		map.put("busiType", "状态");
		
		return map;
	}
	
	@Override
	public void init(Sm_Permission_Role object)
	{
		this.seteCode(myString.parseForExport(object.geteCode()));
		this.setTheName(myString.parseForExport(object.getTheName()));
		
		this.setEnableDateTime(myString.parseForExport(myDatetime.dateToString(object.getEnableTimeStamp(), "yyyy-MM-dd")));
		this.setDownDateTime(myString.parseForExport(myDatetime.dateToString(object.getDownTimeStamp(), "yyyy-MM-dd")));
		this.setBusiType(myString.parseForExport(S_ValidState.ValToStrVal.get(object.getBusiType())));
	}

	public String geteCode() 
	{
		return eCode;
	}

	public void seteCode(String eCode) 
	{
		this.eCode = eCode;
	}
}