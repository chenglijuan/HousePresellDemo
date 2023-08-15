package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Emmp_BankInfo;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class Emmp_BankInfoTemplate implements IExportExcel<Emmp_BankInfo>
{
	@Getter @Setter
	private String eCode;

	@Getter @Setter
	private String theName;

	@Getter @Setter
	private String shortName;

	@Getter @Setter
	private String capitalCollectionModel;

	@Getter @Setter
	private String contactPerson;


	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("eCode", "金融机构编号");
		map.put("theName", "银行名称");
		map.put("shortName", "银行简称");
		map.put("capitalCollectionModel", "资金归集模式");
		map.put("contactPerson", "联系人");

		return map;
	}

	@Override
	public void init(Emmp_BankInfo fromClass)
	{
		this.setECode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		this.setShortName(fromClass.getShortName());
		this.setCapitalCollectionModel(fromClass.getCapitalCollectionModel());
		this.setContactPerson(fromClass.getContactPerson());
	}
}