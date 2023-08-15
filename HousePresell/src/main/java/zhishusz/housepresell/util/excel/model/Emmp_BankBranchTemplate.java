package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Emmp_BankBranchTemplate implements IExportExcel<Emmp_BankBranch>
{
	@Getter @Setter
	private String eCode;

	@Getter @Setter
	private String theName;

	@Getter @Setter
	private String shortName;

	@Getter @Setter
	private String address;

	@Getter @Setter
	private String contactPerson;

	@Getter @Setter
	private String contactPhone;

	@Getter @Setter
	private String bankName;

	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<>();
		
		map.put("eCode", "开户行编号");
		map.put("theName", "开户行名称");
		map.put("shortName", "开户行简称");
		map.put("address", "开户行地址");
		map.put("contactPerson", "联系人");
		map.put("contactPhone", "联系电话");
		map.put("bankName", "银行名称");

		return map;
	}

	@Override
	public void init(Emmp_BankBranch fromClass)
	{
		this.setECode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		this.setShortName(fromClass.getShortName());
		this.setAddress(fromClass.getAddress());
		this.setContactPerson(fromClass.getContactPerson());
		this.setContactPhone(fromClass.getContactPhone());
		Emmp_BankInfo bank = fromClass.getBank();
		if (bank != null)
		{
			bankName=fromClass.getBank().getTheName();
		}

	}
}