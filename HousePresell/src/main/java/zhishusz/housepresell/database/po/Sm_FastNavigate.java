package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：快捷导航信息
 * */
@ITypeAnnotation(remark="快捷导航信息")
public class Sm_FastNavigate implements Serializable
{
	private static final long serialVersionUID = -4370176786599382065L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	//---------公共字段-End---------//
	@Getter @Setter @IFieldAnnotation(remark="用户Id")
	private Long userTableId;
	
	@Getter @Setter @IFieldAnnotation(remark="菜单Id")
	private Long menuTableId;
	
	@Getter @Setter @IFieldAnnotation(remark="菜单名称")
	private String theNameOfMenu;
	
	@Getter @Setter @IFieldAnnotation(remark="菜单链接地址")
	private String theLinkOfMenu;
	
	@Getter @Setter @IFieldAnnotation(remark="排序")
	private Integer orderNumber;
	
	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
