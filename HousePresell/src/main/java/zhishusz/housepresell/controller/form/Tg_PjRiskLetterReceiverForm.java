package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：项目风险函
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_PjRiskLetterReceiverForm extends NormalActionForm
{
	private static final long serialVersionUID = 7451526978546861994L;
	
	//---------公共字段-Start---------//
	@Getter @Setter
	private Long tableId;//表id	
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态	

	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	@Getter @Setter
	private Sm_User userStart;//创建人	
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	//---------公共字段-Start---------//	
	@Getter @Setter
	private Tg_PjRiskLetter tg_PjRiskLetter;//关联风险提示函	
	@Getter @Setter
	private Sm_User emmp_OrgMember;//关联机构成员
	@Getter @Setter
	private Emmp_CompanyInfo emmp_CompanyInfo;//关联机构
	@Getter @Setter
	private String theNameOfDepartment;//所属部门名称	
	@Getter @Setter
	private String theType;//正泰、机构 类型
	@Getter @Setter
	private String theName;//用户名
	@Getter @Setter
	private String realName;//真实姓名
	@Getter @Setter
	private String positionName;//职称	
	@Getter @Setter
	private String email;//邮箱
	@Getter @Setter
	private Integer sendWay;//发送方式（0.内部发送 1.邮箱发送）	
	@Getter @Setter
	private Integer sendStatement;//发送状态(0.未发送 1.已发送)
	@Getter @Setter
	private String sendTimeStamp;//发送时间

}
