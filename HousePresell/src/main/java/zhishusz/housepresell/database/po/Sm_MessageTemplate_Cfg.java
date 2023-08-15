package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="消息模板配置")
public class Sm_MessageTemplate_Cfg implements Serializable
{
	private static final long serialVersionUID = 4263364436689915597L;

	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;

	@Getter @Setter @IFieldAnnotation(remark="关联参数定义")
	private Sm_BaseParameter sm_baseParameter;

	@Getter @Setter @IFieldAnnotation(remark="消息模板名称")
	public String theName;

	@Getter @Setter @IFieldAnnotation(remark="模板描述")
	public String theDescribe;

	@Getter @Setter @IFieldAnnotation(remark="消息标题")
	public String theTitle;

	@Getter @Setter @IFieldAnnotation(remark="消息内容")
	public String theContent;

	@Getter @Setter @IFieldAnnotation(remark="备注")
	public String remark;

	@Getter @Setter @IFieldAnnotation(remark="角色列表")
	public List<Sm_Permission_Role> sm_permission_roleList;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
