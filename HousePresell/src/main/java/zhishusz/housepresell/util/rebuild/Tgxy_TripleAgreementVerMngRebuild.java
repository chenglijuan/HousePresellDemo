package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
public class Tgxy_TripleAgreementVerMngRebuild extends RebuilderBase<Tgxy_TripleAgreementVerMng>
{
	@Override
	public Properties getSimpleInfo(Tgxy_TripleAgreementVerMng object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_TripleAgreementVerMng object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("theName", object.getTheName());
		properties.put("theVersion", object.getTheVersion());
		properties.put("theType", object.getTheType());
		properties.put("eCodeOfCooperationAgreement", object.geteCodeOfCooperationAgreement());
		properties.put("theNameOfCooperationAgreement", object.getTheNameOfCooperationAgreement());
		properties.put("enableTimeStamp", object.getEnableTimeStamp());
		properties.put("downTimeStamp", object.getDownTimeStamp());
		properties.put("templateContentStyle", object.getTemplateContentStyle());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMngList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_TripleAgreementVerMngList != null)
		{
			for(Tgxy_TripleAgreementVerMng object:tgxy_TripleAgreementVerMngList)
			{
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
				properties.put("theVersion", object.getTheVersion());
				properties.put("theType", object.getTheType());
				properties.put("eCodeOfCooperationAgreement", object.geteCodeOfCooperationAgreement());
				properties.put("theNameOfCooperationAgreement", object.getTheNameOfCooperationAgreement());
				properties.put("enableTimeStamp", object.getEnableTimeStamp());
				properties.put("downTimeStamp", object.getDownTimeStamp());
				properties.put("templateContentStyle", object.getTemplateContentStyle());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List getFormList(List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMngList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_TripleAgreementVerMngList != null)
		{
			for(Tgxy_TripleAgreementVerMng object:tgxy_TripleAgreementVerMngList)
			{
				Properties properties = new MyProperties();				
				if(object.getTableId()!=null){
					properties.put("tableId", object.getTableId());
				}
				if(object.getTheName()!=null){
					properties.put("theName", object.getTheName());
				}
				if(object.getTheVersion()!=null){
					properties.put("theVersion", object.getTheVersion());
				}
				if(object.getTheType()!=null){
					properties.put("theType", object.getTheType());
				}
				if(object.getTheState1()!=null){
					properties.put("theState1", object.getTheState1());
				}
				if(object.getEnableTimeStamp()!=null){
					
					properties.put("enableTimeStamp",MyDatetime.getInstance().dateToSimpleString(object.getEnableTimeStamp()));
				}
				if(object.getDownTimeStamp()!=null){
					properties.put("downTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getDownTimeStamp()));
				}
				if(object.getUserStart()!=null){
					properties.put("operatorUser", object.getUserStart().getTheName());
				}
				if(object.getCreateTimeStamp()!=null){
					properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
				}
				if(object.getUserRecord()!=null){
					properties.put("userRecord", object.getUserRecord().getTheName());
				}
				if(object.getRecordTimeStamp()!=null){
					properties.put("recordTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getRecordTimeStamp()));
				}
				//根据approvalState判断状态 //待提交/审核中/已完结
				String busiState="";
				if(object.getApprovalState()!=null){
					if("待提交".equals(object.getApprovalState())){
						busiState="1";
					}else if("审核中".equals(object.getApprovalState())){
						busiState="2";
					}else if("已完结".equals(object.getApprovalState())){
						busiState="3";
					}						
				}				
				properties.put("busiState", busiState);

				if(object.geteCodeOfCooperationAgreement()!=null){
					properties.put("eCodeOfCooperationAgreement", object.geteCodeOfCooperationAgreement());
				}
				if(object.getTheNameOfCooperationAgreement()!=null){
					properties.put("theNameOfCooperationAgreement", object.getTheNameOfCooperationAgreement());				
				}
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public Properties getSimpleInfo2(Tgxy_TripleAgreementVerMng object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
			
		if(object.getTableId()!=null){
			properties.put("tableId", object.getTableId());
		}
		if(object.getTheName()!=null){
			properties.put("theName", object.getTheName());
		}
		if(object.getTheVersion()!=null){
			properties.put("theVersion", object.getTheVersion());
		}
		if(object.getTheType()!=null){
			properties.put("theType", object.getTheType());
		}
		if(object.getTheState1()!=null){
			properties.put("theState1", object.getTheState1());
		}
		if(object.getEnableTimeStamp()!=null){
			
			properties.put("enableTimeStamp",MyDatetime.getInstance().dateToSimpleString(object.getEnableTimeStamp()));
		}
		if(object.getDownTimeStamp()!=null){
			properties.put("downTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getDownTimeStamp()));
		}
		if(object.getUserStart()!=null){
			properties.put("operatorUser", object.getUserStart().getTheName());
		}
		if(object.getCreateTimeStamp()!=null){
			properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		}
		if(object.getUserRecord()!=null){
			properties.put("userRecord", object.getUserRecord().getTheName());
		}
		if(object.getRecordTimeStamp()!=null){
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getRecordTimeStamp()));
		}
		if(object.geteCodeOfCooperationAgreement()!=null){
			properties.put("eCodeOfCooperationAgreement", object.geteCodeOfCooperationAgreement());
		}
		if(object.getTheNameOfCooperationAgreement()!=null){
			properties.put("theNameOfCooperationAgreement", object.getTheNameOfCooperationAgreement());				
		}
		//根据approvalState判断状态 //待提交/审核中/已完结
		String busiState="";
		if(object.getApprovalState()!=null){
			if("待提交".equals(object.getApprovalState())){
				busiState="1";
			}else if("审核中".equals(object.getApprovalState())){
				busiState="2";
			}else if("已完结".equals(object.getApprovalState())){
				busiState="3";
			}						
		}
		properties.put("busiState", busiState);
		return properties;		
	}
}
