package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：快捷导航信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_FastNavigateForm extends NormalActionForm
{
	private static final long serialVersionUID = 4090486759676602398L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Long userTableId;//用户Id
	@Getter @Setter
	private Long menuTableId;//菜单Id
	@Getter @Setter
	private String theNameOfMenu;//菜单名称
	@Getter @Setter
	private String theLinkOfMenu;//菜单链接地址
	@Getter @Setter
	private Integer orderNumber;//排序
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
