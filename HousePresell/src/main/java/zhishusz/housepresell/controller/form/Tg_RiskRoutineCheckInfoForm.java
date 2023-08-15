package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：风控例行抽查表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_RiskRoutineCheckInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = -6042698342365674629L;
	
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
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Long spotTimeStamp;//抽查日期
	@Getter @Setter
	private String spotTimeStr;//抽查日期字符串
	@Getter @Setter
	private String bigBusiType;//业务大类
	@Getter @Setter
	private String smallBusiType;//业务小类
	@Getter @Setter
	private String eCodeOfBill;//单据号
	@Getter @Setter
	private String checkResult;//核查结果
	@Getter @Setter
	private String unqualifiedReasons;//不合格原因
	@Getter @Setter
	private String isChoosePush;//是否选择推送
	@Getter @Setter
	private String modifyFeedback;//整改反馈
	@Getter @Setter
	private String forensicConfirmation;//法务确认
	@Getter @Setter
	private Tg_RiskRoutineCheckInfoForm[] rishCheckList;
	@Getter @Setter
	private Long busiCodeSummaryId;
	@Getter @Setter 
	private String isDoPush;
	@Getter @Setter
	private String dateStr;//统计时间段字符串
	@Getter @Setter
	private Long startDateTimeStamp;//统计开始时间时间戳
	@Getter @Setter
	private Long endDateTimeStamp;//统计结束时间时间戳
	
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
