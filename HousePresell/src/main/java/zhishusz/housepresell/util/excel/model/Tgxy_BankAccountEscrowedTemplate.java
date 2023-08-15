package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.util.MyDatetime;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Tgxy_BankAccountEscrowedTemplate implements IExportExcel<Tgxy_BankAccountEscrowed>
{
	@Getter @Setter
	private String eCode;

	@Getter @Setter
	private String theName;

	@Getter @Setter
	private String bankBranchShortName;

	@Getter @Setter
	private String bankBranchName;

	@Getter @Setter
	private String userStartName;

	@Getter @Setter
	private String userStartDate;

	@Getter @Setter
	private String busiState;


	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "托管账号");
		map.put("theName", "托管账号名称");
		map.put("bankBranchShortName", "银行简称");
		map.put("bankBranchName", "开户行名称");
		map.put("userStartName", "操作人");
		map.put("userStartDate", "操作日期");
		map.put("busiState", "状态");

		return map;
	}

	@Override
	public void init(Tgxy_BankAccountEscrowed fromClass)
	{
		this.setECode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		Emmp_BankBranch bankBranch = fromClass.getBankBranch();
		if (bankBranch != null)
		{
			this.setBankBranchShortName(bankBranch.getShortName());
			this.setBankBranchName(bankBranch.getTheName());
		}
		Sm_User userStart = fromClass.getUserStart();
		if (userStart != null)
		{
			this.setUserStartName(userStart.getTheName());
		}
		Long createTimeStamp = fromClass.getCreateTimeStamp();
		String date = MyDatetime.getInstance().dateToSimpleString(createTimeStamp);
		this.setUserStartDate(date);
		this.setBusiState(fromClass.getBusiState());
	}
}