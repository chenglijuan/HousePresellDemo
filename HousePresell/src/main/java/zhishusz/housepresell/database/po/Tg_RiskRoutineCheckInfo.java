package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：风控例行抽查表
 */
@ITypeAnnotation(remark="风控例行抽查表")
public class Tg_RiskRoutineCheckInfo implements Serializable,IApprovable
{
	private static final long serialVersionUID = -3839007557500155229L;
	
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
	//---------公共字段-End---------//
	
	@Getter @Setter @IFieldAnnotation(remark="风控例行抽查单号")
	private String checkNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="抽查日期")
	private Long spotTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="业务大类")
	private String bigBusiType;
	
	@Getter @Setter @IFieldAnnotation(remark="业务小类")
	private String smallBusiType;
	
	@IFieldAnnotation(remark="单据号")
	private String eCodeOfBill;
	
	@Getter @Setter @IFieldAnnotation(remark="核查结果 S_YesNoStr")
	private String checkResult;
	
	@Getter @Setter @IFieldAnnotation(remark="不合格原因")
	private String unqualifiedReasons;
	
	@Getter @Setter @IFieldAnnotation(remark="是否选择推送 S_YesNoStr")
	private String isChoosePush;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已经推送 S_YesNoStr")
	private String isDoPush;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已反馈 S_YesNoStr")
	private String isModify;
	
	@Getter @Setter @IFieldAnnotation(remark="是否已处理 S_YesNoStr")
	private String isHandle;
	
	@Getter @Setter @IFieldAnnotation(remark="整改反馈")
	private String modifyFeedback;
	
	@Getter @Setter @IFieldAnnotation(remark="法务确认")
	private String forensicConfirmation;

	@Getter @Setter @IFieldAnnotation(remark="整改状态 S_RectificationState")
	private String rectificationState;
	
	@Getter @Setter @IFieldAnnotation(remark="录入状态 S_EntryState")
	private String entryState;
	
	@Getter @Setter @IFieldAnnotation(remark="关联主键")
	private Long relatedTableId;
	
	@Getter @Setter @IFieldAnnotation(remark="月统计")
	private Tg_RiskCheckMonthSum monthSummary;
	
	@Getter @Setter @IFieldAnnotation(remark="业务统计")
	private Tg_RiskCheckBusiCodeSum busiCodeSummary;
	
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

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfBill() {
		return eCodeOfBill;
	}

	public void seteCodeOfBill(String eCodeOfBill) {
		this.eCodeOfBill = eCodeOfBill;
	}
}
