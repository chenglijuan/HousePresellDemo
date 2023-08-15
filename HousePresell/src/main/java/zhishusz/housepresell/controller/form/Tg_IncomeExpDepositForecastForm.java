package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：收入预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_IncomeExpDepositForecastForm extends NormalActionForm
{
	private static final long serialVersionUID = 6235362845986901666L;

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
	private Long theDay;//日期(工作日)
	@Getter @Setter
	private Integer theWeek;//星期一到星期日
	@Getter @Setter
	private Double lastDaySurplus;//上日活期结余（元）
	@Getter @Setter
	private Double incomeTotal;//收入合计（元）
	@Getter @Setter
	private Double expTotal;//支出合计（元）
	@Getter @Setter
	private Double todaySurplus;//本日活动结余（元）
	@Getter @Setter
	private Double collocationReference;//托管余额参考值（元）
	@Getter @Setter
	private Double collocationBalance;//扣减参考值后的托管余额（元）
	@Getter @Setter
	private Double canDepositReference1;//可存入参考值1（元）
	@Getter @Setter
	private Double canDepositReference2;//可存入参考值2（元）

	@Getter @Setter
	private String startTimeStr;// 日期选择 开始
	@Getter @Setter
	private Long startTimeStamp;
	@Getter @Setter
	private String endTimeStr;//   日期选择 结束
	@Getter @Setter
	private Long endTimeStamp;


	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
