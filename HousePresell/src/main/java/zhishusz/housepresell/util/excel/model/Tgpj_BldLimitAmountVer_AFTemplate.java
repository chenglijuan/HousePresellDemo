package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.util.MyDatetime;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Tgpj_BldLimitAmountVer_AFTemplate implements IExportExcel<Tgpj_BldLimitAmountVer_AF>
{
	@Getter @Setter
	private String eCode;
	@Getter @Setter
	private String theName;
	@Getter @Setter
	private String theType;
	@Getter @Setter
	private String beginExpirationDate;
	@Getter @Setter
	private String endExpirationDate;
	@Getter @Setter
	private String busiState;

	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "受限额度节点版本号");
		map.put("theName", "版本名称");
		map.put("theType", "交付类型");
		map.put("beginExpirationDate", "启用时间");
		map.put("endExpirationDate", "停用时间");
		map.put("busiState", "状态");

		return map;
	}

	@Override
	public void init(Tgpj_BldLimitAmountVer_AF fromClass)
	{
		this.setECode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		setTheType(fromClass.getTheType());
		Long beginExpirationDate = fromClass.getBeginExpirationDate();
		if (beginExpirationDate != null)
		{
			setBeginExpirationDate(MyDatetime.getInstance().dateToSimpleString(beginExpirationDate));
		}
		Long endExpirationDate = fromClass.getEndExpirationDate();
		if (endExpirationDate != null)
		{
			setEndExpirationDate(MyDatetime.getInstance().dateToSimpleString(endExpirationDate));
		}
		setBusiState(fromClass.getBusiState());
	}
}