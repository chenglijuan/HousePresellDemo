package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@Service
public class Emmp_BankBranchRebuild extends RebuilderBase<Emmp_BankBranch>
{
	@Override
	public Properties getSimpleInfo(Emmp_BankBranch object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		/*
		 * xsz by time 2018-8-15 14:13:28
		 * 设置列表展示字段 用于预加载
		 * 
		 * 作用于：
		 * 新增合作协议（Tgxy_CoopAgreement）预加载开户行信息
		 * 
		 */
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}

	@Override
	public Properties getDetail(Emmp_BankBranch object)
	{
		MyDatetime myDatetime = MyDatetime.getInstance();

		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		if(object.getUserStart()!=null){
			properties.put("userStartId", object.getUserStart().getTableId());
		}
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("createTimeStampString", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));
//		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("userRecord", object.getUserRecord());
		if(object.getUserRecord()!=null){
			properties.put("userRecordId", object.getUserRecord().getTableId());
		}
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("recordTimeStampString",myDatetime.dateToSimpleString(object.getRecordTimeStamp()) );

		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString",myDatetime.dateToString2(object.getLastUpdateTimeStamp()) );
		properties.put("theName", object.getTheName());
		properties.put("shortName", object.getShortName());
		properties.put("address", object.getAddress());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("leader", object.getLeader());
		properties.put("generalBankName",object.getBank().getTheName());
		properties.put("bankId",object.getBank().getTableId());
		properties.put("isUsing",object.getIsUsing());
		properties.put("subjCode",object.getSubjCode());

		properties.put("desubjCode",object.getDesubjCode());
		properties.put("bblcsubjCode",object.getBblcsubjCode());
		properties.put("jgcksubjCode",object.getJgcksubjCode());
		
		properties.put("interbankCode",null == object.getInterbankCode()?"":object.getInterbankCode());
		properties.put("isDocking",null == object.getIsDocking()?0:object.getIsDocking());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_BankBranch> emmp_BankBranchList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_BankBranchList != null)
		{
			for(Emmp_BankBranch object:emmp_BankBranchList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				Emmp_BankInfo bank = object.getBank();
				if (bank != null)
				{
					properties.put("bankName",bank.getTheName());
					properties.put("bankId",bank.getTableId());
				}
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
//				properties.put("recordTimeStamp", object.getRecordTimeStamp());

//				properties.put("bankId",object.getBank().getTableId());
				properties.put("theName", object.getTheName());
				properties.put("shortName", object.getShortName());
				properties.put("address", object.getAddress());
				properties.put("contactPerson", object.getContactPerson());
				properties.put("contactPhone", object.getContactPhone());
				properties.put("isUsing", object.getIsUsing());
//				properties.put("leader", object.getLeader());
				
				properties.put("desubjCode",object.getDesubjCode());
				properties.put("bblcsubjCode",object.getBblcsubjCode());
				properties.put("jgcksubjCode",object.getJgcksubjCode());
				
				properties.put("interbankCode",null == object.getInterbankCode()?"":object.getInterbankCode());
				properties.put("isDocking",null == object.getIsDocking()?0:object.getIsDocking());
				
				list.add(properties);
			}
		}
		return list;
	}

	@Override
	public List<Properties> executeForSelectList(List<Emmp_BankBranch> emmp_BankBranchList){
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_BankBranchList != null)
		{
			for(Emmp_BankBranch object:emmp_BankBranchList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				list.add(properties);
			}
		}
		return list;
	}
}
