package zhishusz.housepresell.util.excel.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.util.IFieldAnnotation;

import zhishusz.housepresell.util.MyDatetime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Emmp_CompanyInfoTemplate implements IExportExcel<Emmp_CompanyInfo>
{

	@IFieldAnnotation(remark="企业编号")
	private String eCode;

	@Getter @Setter @IFieldAnnotation(remark="企业名称")
	private String theName;//同步预售系统+手工输入

	@Getter @Setter @IFieldAnnotation(remark="归属集团")
	private String companyGroup;

	@Getter @Setter @IFieldAnnotation(remark="统一社会信用代码")
	private String unifiedSocialCreditCode;//同步预售系统+手工输入

	@Getter @Setter @IFieldAnnotation(remark="成立日期")
	private String registeredDate;

	@Getter @Setter @IFieldAnnotation(remark="法定代表人")
	private String legalPerson;

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("eCode", "企业编号");
		map.put("theName", "企业名称");
		map.put("companyGroup", "归属集团");
		map.put("unifiedSocialCreditCode", "统一社会信用代码");
		map.put("registeredDate", "成立日期");
		map.put("legalPerson", "法定代表人");

		return map;
	}

	@Override
	public void init(Emmp_CompanyInfo fromClass)
	{
		this.seteCode(fromClass.geteCode());
		this.setTheName(fromClass.getTheName());
		this.setCompanyGroup(fromClass.getCompanyGroup());
		this.setUnifiedSocialCreditCode(fromClass.getUnifiedSocialCreditCode());
		this.setRegisteredDate(MyDatetime.getInstance().dateToSimpleString(fromClass.getRegisteredDate()));
		this.setLegalPerson(fromClass.getLegalPerson());
	}

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}