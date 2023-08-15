package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目风险函-接收对象
 */
@ITypeAnnotation(remark="项目风险函-接收对象")
public class Tg_PjRiskLetterReceiver implements Serializable
{
	private static final long serialVersionUID = -302871786804745415L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="关联风险提示函")
	private Tg_PjRiskLetter tg_PjRiskLetter;
	
	@Getter @Setter @IFieldAnnotation(remark="关联机构成员")
	private Sm_User emmp_OrgMember;
	
	@Getter @Setter @IFieldAnnotation(remark="关联机构")
	private Emmp_CompanyInfo emmp_CompanyInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="所属部门名称")
	private String theNameOfDepartment;
	
	@Getter @Setter @IFieldAnnotation(remark="类型")
	private String theType;//正泰、机构
	
	@Getter @Setter @IFieldAnnotation(remark="用户名")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="真实姓名")
	private String realName;
	
	@Getter @Setter @IFieldAnnotation(remark="职称")
	private String positionName;
	
	@Getter @Setter @IFieldAnnotation(remark="邮箱")
	private String email;
	
	@Getter @Setter @IFieldAnnotation(remark="发送方式（0.内部发送 1.邮箱发送）")
	private Integer sendWay;
	
	@Getter @Setter @IFieldAnnotation(remark="发送状态(0.未发送 1.已发送)")
	private Integer sendStatement;
	
	@Getter @Setter @IFieldAnnotation(remark="发送时间")
	private String sendTimeStamp;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}


}
