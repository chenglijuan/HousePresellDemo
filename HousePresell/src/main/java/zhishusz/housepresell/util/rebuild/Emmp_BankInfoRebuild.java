package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.CapitalCollectionModelUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：金融机构(承办银行)
 * Company：ZhiShuSZ
 */
@Service
public class Emmp_BankInfoRebuild extends RebuilderBase<Emmp_BankInfo>
{
	@Override
	public Properties getSimpleInfo(Emmp_BankInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-15 14:13:28
		 * 设置列表展示字段 用于预加载
		 * 
		 * 作用于：
		 * 新增合作备忘录（Tgxy_CoopMemo）预加载银行信息
		 * 
		 * 新增合作协议（Tgxy_CoopAgreement）预加载银行信息
		 * 
		 */
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());

		return properties;
	}

	@Override
	public Properties getDetail(Emmp_BankInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
//		properties.put("userStart", object.getUserStart());
//		properties.put("userStartId", object.getUserStart().getTableId());
		if(object.getUserStart()!=null){
			properties.put("userStartName", object.getUserStart().getTheName());
		}
//		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString(object.getCreateTimeStamp()));
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("lastUpdateTimeStampString", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
//		properties.put("userRecord", object.getUserRecord());
//		properties.put("userRecordId", object.getUserRecord().getTableId());
		if(object.getUserRecord()!=null){
			properties.put("userRecordName", object.getUserRecord().getTheName());
		}
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("theName", object.getTheName());
		properties.put("shortName", object.getShortName());
		properties.put("leader", object.getLeader());
		properties.put("address", object.getAddress());
		properties.put("capitalCollectionModel", object.getCapitalCollectionModel());
		properties.put("theType", object.getTheType());
		properties.put("postalAddress", object.getPostalAddress());
		properties.put("postalPort", object.getPostalPort());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("ftpDirAddress", object.getFtpDirAddress());
		properties.put("ftpAddress", object.getFtpAddress());
		properties.put("ftpPort", object.getFtpPort());
		properties.put("ftpUserName", object.getFtpUserName());
		properties.put("ftpPwd", object.getFtpPwd());
		properties.put("financialInstitution", object.getFinancialInstitution());
		properties.put("theTypeOfPOS", object.getTheTypeOfPOS());
		properties.put("eCodeOfSubject", object.geteCodeOfSubject());
		properties.put("eCodeOfProvidentFundCenter", object.geteCodeOfProvidentFundCenter());
		properties.put("remark", object.getRemark());
		properties.put("bankCode", object.getBankCode());
		properties.put("bankNo", object.getBankNo());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_BankInfo> emmp_BankInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (emmp_BankInfoList != null)
		{
			for (Emmp_BankInfo object : emmp_BankInfoList)
			{
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("tableId", object.getTableId());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				//		properties.put("userStart", object.getUserStart());
				//		properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				//		properties.put("userRecord", object.getUserRecord());
				//		properties.put("userRecordId", object.getUserRecord().getTableId());
				//		properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("theName", object.getTheName());
				properties.put("shortName", object.getShortName());
				properties.put("leader", object.getLeader());
				properties.put("address", object.getAddress());
//				properties.put("capitalCollectionModel", object.getCapitalCollectionModel());
				properties.put("capitalCollectionModel", CapitalCollectionModelUtil.number2Name(object.getCapitalCollectionModel()));
				properties.put("theType", object.getTheType());
				//		properties.put("postalAddress", object.getPostalAddress());
				//		properties.put("postalPort", object.getPostalPort());
				properties.put("contactPerson", object.getContactPerson());
				//		properties.put("contactPhone", object.getContactPhone());
				//		properties.put("ftpDirAddress", object.getFtpDirAddress());
				//		properties.put("ftpUserName", object.getFtpUserName());
				//		properties.put("ftpPwd", object.getFtpPwd());
				//		properties.put("financialInstitution", object.getFinancialInstitution());
				//		properties.put("theTypeOfPOS", object.getTheTypeOfPOS());
				//		properties.put("eCodeOfSubject", object.geteCodeOfSubject());
				//		properties.put("eCodeOfProvidentFundCenter", object.geteCodeOfProvidentFundCenter());
				//		properties.put("remark", object.getRemark());

				list.add(properties);
			}
		}
		return list;
	}

	public Properties getListDetail(Emmp_BankInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("theName", object.getTheName());
		properties.put("shortName", object.getShortName());
		properties.put("leader", object.getLeader());
		properties.put("address", object.getAddress());
		properties.put("capitalCollectionModel", object.getCapitalCollectionModel());
		properties.put("theType", object.getTheType());
		properties.put("postalAddress", object.getPostalAddress());
		properties.put("postalPort", object.getPostalPort());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("ftpDirAddress", object.getFtpDirAddress());
		properties.put("ftpUserName", object.getFtpUserName());
		properties.put("ftpPwd", object.getFtpPwd());
		properties.put("financialInstitution", object.getFinancialInstitution());
		properties.put("theTypeOfPOS", object.getTheTypeOfPOS());
		properties.put("eCodeOfSubject", object.geteCodeOfSubject());
		properties.put("eCodeOfProvidentFundCenter", object.geteCodeOfProvidentFundCenter());
		properties.put("remark", object.getRemark());

		return properties;
	}

	@Override
	public List<Properties> executeForSelectList(List<Emmp_BankInfo> emmp_BankInfoList){
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_BankInfoList != null)
		{
			for(Emmp_BankInfo object: emmp_BankInfoList)
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
