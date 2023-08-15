package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：风控月度小结
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_RiskRoutineMonthSumForm extends NormalActionForm
{
	private static final long serialVersionUID = 2406487433686376094L;
	
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
	private String dateStr;//统计时间段字符串
	@Getter @Setter
	private Long startDateTimeStamp;//统计开始时间时间戳
	@Getter @Setter
	private Long endDateTimeStamp;//统计结束时间时间戳
	@Getter @Setter
	private Integer type;//类型
	@Getter @Setter
	private String bigBusiType;//业务大类

	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
