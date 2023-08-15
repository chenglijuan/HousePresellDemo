package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：流水号
 * */
@ITypeAnnotation(remark = "流水号")
public class Tgpf_SerialNumber implements Serializable, IApprovable
{
	private static final long serialVersionUID = 6437830905721561136L;

	// ---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark = "业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark = "编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark = "创建人")
	private Sm_User userStart;

	@Getter @Setter @IFieldAnnotation(remark = "创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark = "修改人")
	private Sm_User userUpdate;

	@Getter @Setter @IFieldAnnotation(remark = "最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark = "备案人")
	private Sm_User userRecord;

	@Getter @Setter @IFieldAnnotation(remark = "备案日期")
	private Long recordTimeStamp;
	// ---------公共字段-end---------//

	@Getter @Setter @IFieldAnnotation(remark = "业务类型")
	private String businessType;

	@Getter @Setter @IFieldAnnotation(remark = "流水号")
	private Integer serialNumber;

	@Getter @Setter @IFieldAnnotation(remark = "流水日期yyyy-MM-dd")
	private String serialDate;

	@Override
	public String getSourceType() {
		return null;
	}

	@Override
	public Long getSourceId() {
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
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
