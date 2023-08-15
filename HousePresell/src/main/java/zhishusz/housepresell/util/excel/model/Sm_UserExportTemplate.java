package zhishusz.housepresell.util.excel.model;

import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_IDType;
import zhishusz.housepresell.database.po.state.S_ValidState;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

public class Sm_UserExportTemplate implements IExportExcel<Sm_User>
{
	@IFieldAnnotation(remark="用户号编码")
	private String eCode;
	
	@Getter @Setter @IFieldAnnotation(remark="用户名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="所属机构")
	private String theNameOfCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="证件类型")
	private String idType;
	
	@Getter @Setter @IFieldAnnotation(remark="证件号码")
	private String idNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="是否加密")
	private String isEncrypt;
	
	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="锁定状态")
	private String applyState;
	
	@Override
	public String toString() 
	{
		return "{\"eCode\":\"" + eCode + "\",\"theName\":\"" + theName
				+ "\",\"theNameOfCompany\":\"" + theNameOfCompany + "\",\"idType\":\"" + idType + "\",\"idNumber\":\""
				+ idNumber + "\",\"isEncrypt\":\"" + isEncrypt + "\",\"busiState\":\"" + busiState
				+ "\",\"applyState\":\"" + applyState + "\"}";
	}

	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("eCode", "用户编码");
		map.put("theName", "用户名称");
		map.put("theNameOfCompany", "所属机构");
		map.put("idType", "证件类型");
		map.put("idNumber", "证件号码");
		map.put("isEncrypt", "是否加密");
		map.put("busiState", "是否启用");
		map.put("applyState", "锁定状态");
		
		return map;
	}

	public String geteCode() 
	{
		return eCode;
	}

	public void seteCode(String eCode) 
	{
		this.eCode = eCode;
	}

	@Override
	public void init(Sm_User sm_User) 
	{
		this.seteCode(sm_User.geteCode());
		this.setTheName(sm_User.getTheName());
		if(sm_User.getCompany() != null)
		{
			this.setTheNameOfCompany(sm_User.getCompany().geteCode());
		}
		else
		{
			this.setTheNameOfCompany("");
		}
		this.setIdNumber(sm_User.getIdNumber());
		if(S_IDType.ResidentIdentityCard.equals(sm_User.getIdType()))
		{
			this.setIdType(S_IDType.idTypeArr[0]);
		}
		if(S_IDType.Passport.equals(sm_User.getIdType()))
		{
			this.setIdType(S_IDType.idTypeArr[1]);
		}
		if(S_IDType.HongKongMacaoTaiwan_IdentityCard.equals(sm_User.getIdType()))
		{
			this.setIdType(S_IDType.idTypeArr[2]);
		}
		if(S_IDType.TravelVisa.equals(sm_User.getIdType()))
		{
			this.setIdType(S_IDType.idTypeArr[3]);
		}
		if(S_IDType.Others.equals(sm_User.getIdType()))
		{
			this.setIdType(S_IDType.idTypeArr[4]);
		}
		if(sm_User.getIsEncrypt() != null)
		{
			if(1 == sm_User.getIsEncrypt())
			{
				this.setIsEncrypt("是");
			}
			if(0 == sm_User.getIsEncrypt())
			{
				this.setIsEncrypt("否");
			}
		}
		else
		{
			this.setIsEncrypt("");
		}
		
		if(S_ValidState.InValid.equals(sm_User.getBusiState()))
		{
			this.setBusiState(S_ValidState.ValToStrVal.get(S_ValidState.InValid));
		}
		if(S_ValidState.Valid.equals(sm_User.getBusiState()))
		{
			this.setBusiState(S_ValidState.ValToStrVal.get(S_ValidState.Valid));
		}
		if(sm_User.getLockUntil() != null)
		{
			if(sm_User.getLockUntil() > System.currentTimeMillis())
			{
				this.setApplyState("锁定");
			}
			else
			{
				this.setApplyState("正常");
			}
		}
		else
		{
			this.setApplyState("");
		}
	}
}