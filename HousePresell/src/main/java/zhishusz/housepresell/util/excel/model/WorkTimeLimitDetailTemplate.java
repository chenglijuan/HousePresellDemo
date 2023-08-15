package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.service.Tg_WorkTimeLimitCheckDetailService;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

public class WorkTimeLimitDetailTemplate implements IExportExcel<Tg_WorkTimeLimitCheckDetailService.WorkTimeLimitDetail>
{
	@Getter @Setter @IFieldAnnotation(remark="序号")
	private Integer index;
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;
	@Getter @Setter @IFieldAnnotation(remark="提交申请日期")
	private String applyDate;
	@Getter @Setter @IFieldAnnotation(remark="审核完成日期")
	private String completeDate;
	@Getter @Setter @IFieldAnnotation(remark="办理业务所用天数")
	private Integer days;
	@Getter @Setter @IFieldAnnotation(remark="超时天数")
	private Integer timeOutDays;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("index", "序号");
		map.put("eCode", "编号");
		map.put("applyDate", "提交申请日期");
		map.put("completeDate", "审核完成日期");
		map.put("days", "办理业务所用天数");
		map.put("timeOutDays", "超时天数");
		return map;
	}

	@Override
	public void init(Tg_WorkTimeLimitCheckDetailService.WorkTimeLimitDetail fromClass)
	{
		this.setIndex(fromClass.getIndex());
		this.seteCode(fromClass.geteCode());
		this.setApplyDate(fromClass.getApplyDate());
		this.setCompleteDate(fromClass.getCompleteDate());
		this.setDays(fromClass.getDays());
		this.setTimeOutDays(fromClass.getTimeOutDays());
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}