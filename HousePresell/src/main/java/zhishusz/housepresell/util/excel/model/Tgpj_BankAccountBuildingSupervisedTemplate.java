package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.util.MyDatetime;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Tgpj_BankAccountBuildingSupervisedTemplate implements IExportExcel<Tgpj_BankAccountSupervised>
{
	@Getter @Setter
	private String eCode;
	@Getter @Setter
	private String theName;
	@Getter @Setter
	private String companyName;
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
		map.put("theName", "监管账号名称");
		map.put("eCode", "账号");
		map.put("companyName", "开发企业");
		map.put("bankBranchName", "开户行");
		map.put("userStartName", "操作人");
		map.put("userStartDate", "操作日期");
		map.put("busiState", "状态");

		return map;
	}

	@Override
	public void init(Tgpj_BankAccountSupervised fromClass)
	{
		this.setECode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		Emmp_CompanyInfo developCompany = fromClass.getDevelopCompany();
		if (developCompany != null)
		{
			this.setCompanyName(developCompany.getTheName());
		}
		Emmp_BankBranch bankBranch = fromClass.getBankBranch();
		if (bankBranch != null)
		{
			this.setBankBranchName(bankBranch.getTheName());
		}
		this.setBusiState(fromClass.getBusiState());
		Sm_User userStart = fromClass.getUserStart();
		if (userStart != null)
		{
			setUserStartName(userStart.getTheName());
		}
		Long createTimeStamp = fromClass.getCreateTimeStamp();
		if (createTimeStamp != null)
		{
			setUserStartDate(MyDatetime.getInstance().dateToSimpleString(createTimeStamp));
		}
	}
}