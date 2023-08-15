package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import cn.hutool.http.HttpUtil;
import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="日志-业务状态")
public class Sm_BusiState_Log implements Serializable
{
	private static final long serialVersionUID = 2218794055853654653L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="操作人员")
	private Sm_User userOperate;
	
	@Getter @Setter @IFieldAnnotation(remark="访问来源IP")
	private String remoteAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="操作时间")
	private Long operateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="数据源Id")
	private Long sourceId;

	/*数据源类型：
	机构 Emmp_CompanyInfo、
	机构成员 Emmp_OrgMember、
	楼栋-户室 Empj_HouseInfo、
	楼栋-单元 Empj_UnitInfo、
	楼栋-账号 Tgpj_BuildingAccount*/
	@Getter @Setter @IFieldAnnotation(remark="数据源类型")
	private String sourceType;
	
	@Getter @Setter @IFieldAnnotation(remark="修改前数据Json文件路径")
	private String orgObjJsonFilePath;
	
	@Getter @Setter @IFieldAnnotation(remark="修改后数据Json文件路径")
	private String newObjJsonFilePath;
	
	//从OSS-Server读取Json文件内容
	public String getOrgObjJson()
	{
		return HttpUtil.get(orgObjJsonFilePath);
	}
	public String getNewObjJson()
	{
		return HttpUtil.get(newObjJsonFilePath);
	}
}
