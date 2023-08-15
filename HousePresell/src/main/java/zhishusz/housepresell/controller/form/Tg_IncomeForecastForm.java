package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：收入预测
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_IncomeForecastForm extends NormalActionForm
{
	private static final long serialVersionUID = 3216678625258296040L;
	
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
	private Double incomeTrendForecast;//入账资金趋势预测（元）
	@Getter @Setter
	private Double fixedExpire;//定期到期（元）
	@Getter @Setter
	private Double bankLending;//银行放贷额度（元）
	@Getter @Setter
	private Double incomeForecast1;//收入预测1（元）
	@Getter @Setter
	private Double incomeForecast2;//收入预测2（元）
	@Getter @Setter
	private Double incomeForecast3;//收入预测3（元）
	@Getter @Setter
	private Double incomeTotal;//收入合计（元）

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
