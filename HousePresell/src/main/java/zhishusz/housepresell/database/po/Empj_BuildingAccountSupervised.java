package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-09-29.
 * Company: zhishusz
 */
/*
 * 对象名称：楼幢与楼幢监管账号关联表
 * */
@ITypeAnnotation(remark="楼幢与楼幢监管账号关联表")
public class Empj_BuildingAccountSupervised implements Serializable
{

	private static final long serialVersionUID = -7207898230173330872L;
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

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;

	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;

	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;

	@Getter @Setter @IFieldAnnotation(remark="关联监管账号")
	private Tgpj_BankAccountSupervised bankAccountSupervised;

	@Getter @Setter @IFieldAnnotation(remark="启用日期")
	private Long beginTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="终止日期")
	private Long endTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;//0：启用，1：不启用
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName");
	}
}
