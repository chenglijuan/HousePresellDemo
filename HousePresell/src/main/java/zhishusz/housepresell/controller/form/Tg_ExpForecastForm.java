package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：支出预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_ExpForecastForm extends NormalActionForm
{
	private static final long serialVersionUID = 798891301513165532L;
	
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
	private Double payTrendForecast;//支出资金趋势预测（元）
	@Getter @Setter
	private Double applyAmount;//已申请资金拨付（元）
	@Getter @Setter
	private Double payableFund;//可拨付金额（元）
	@Getter @Setter
	private Double nodeChangePayForecast;//节点变更拨付预测（元）
	@Getter @Setter
	private Double handlingFixedDeposit;//正在办理中的定期存款（元）
	@Getter @Setter
	private Double payForecast1;//支出预测1（元）
	@Getter @Setter
	private Double payForecast2;//支出预测2（元）
	@Getter @Setter
	private Double payForecast3;//支出预测3（元）
	@Getter @Setter
	private Double payTotal;//支出合计（元）

	@Getter @Setter
	private String startTimeStr;// 日期选择 开始
	@Getter @Setter
	private Long startTimeStamp;
	@Getter @Setter
	private String endTimeStr;//   日期选择 结束
	@Getter @Setter
	private Long endTimeStamp;

	@Getter @Setter
	private Integer configurationTime; //收支存预测传过来的参数

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
