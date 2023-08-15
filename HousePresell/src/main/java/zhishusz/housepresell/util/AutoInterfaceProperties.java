package zhishusz.housepresell.util;

import java.util.Properties;

//new AutoInterfaceProperties("Integer", "userId", "会员号", 0, true);
//new AutoInterfaceProperties("List", "userList", "会员列表", list, true);
public class AutoInterfaceProperties extends Properties
{
	private static final long serialVersionUID = -1675436931502952123L;
	
	private String theType;
	private String theName;
	private String remark;
	private Object defaultValue;
	private Boolean isNeeded;
	
	public AutoInterfaceProperties(String theType, String theName, String remark, Object defaultValue, Boolean isNeeded)
	{
		super();
		this.theType = theType;
		this.theName = theName;
		this.remark = remark;
		this.defaultValue = defaultValue;
		this.isNeeded = isNeeded;
	}
	
	public String getTheType()
	{
		return theType;
	}
	public void setTheType(String theType)
	{
		this.theType = theType;
	}
	public String getTheName()
	{
		return theName;
	}
	public void setTheName(String theName)
	{
		this.theName = theName;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public Object getDefaultValue()
	{
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	public Boolean getIsNeeded()
	{
		return isNeeded;
	}
	public void setIsNeeded(Boolean isNeeded)
	{
		this.isNeeded = isNeeded;
	}
}
