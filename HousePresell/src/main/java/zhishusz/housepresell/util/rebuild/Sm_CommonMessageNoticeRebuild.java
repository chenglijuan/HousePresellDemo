package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：审批流-消息模板配置
 * Company：ZhiShuSZ
 */
@Service
public class Sm_CommonMessageNoticeRebuild extends RebuilderBase<Sm_CommonMessage>
{
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcessAfDao;
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;

	@Override
	public Properties getSimpleInfo(Sm_CommonMessage object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 列表页面
		properties.put("tableId", object.getTableId());
		properties.put("theContent", object.getTheContent()); // 内容
		properties.put("theTitle", object.getTheTitle()); // theTitle
		properties.put("sendTimeStamp", object.getSendTimeStamp().substring(0, 10)); // 消息模板名称
		// properties.put("lastUpdateUser",object.getUserUpdate()); //最后维护人
		// properties.put("lastUpdateTimeStamp",myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));
		// //最后维护时间

		return properties;
	}

	@Override
	public Properties getDetail(Sm_CommonMessage object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 列表页面
		properties.put("tableId", object.getTableId());
		properties.put("theContent", object.getTheContent()); // 内容
		properties.put("theTitle", object.getTheTitle()); // theTitle
		properties.put("sendTimeStamp", object.getSendTimeStamp().substring(0, 10)); // 消息模板名称
		// properties.put("lastUpdateUser",object.getUserUpdate()); //最后维护人
		// properties.put("lastUpdateTimeStamp",myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));
		// //最后维护时间

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Sm_CommonMessage> Sm_CommonMessageList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (Sm_CommonMessageList != null)
		{
			for (Sm_CommonMessage object : Sm_CommonMessageList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());

				List<Sm_CommonMessage> list1 = new ArrayList<Sm_CommonMessage>();
				list1.add(object);
				properties.put("childMenu", getDetailList(list1));

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailList(List<Sm_CommonMessage> Sm_CommonMessageList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (Sm_CommonMessageList != null)
		{
			for (Sm_CommonMessage object : Sm_CommonMessageList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());

				properties.put("busiState", object.getBusiState());
				properties.put("theState", object.getTheState());
				properties.put("theTitle", object.getTheTitle()); // theTitle
				properties.put("sendTimeStamp", object.getSendTimeStamp().substring(0, 10)); // 消息模板名称
				properties.put("sender", "常政办发[]"); // theTitle

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin2(List<Sm_CommonMessageDtl> sm_CommonMessageDtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (sm_CommonMessageDtlList != null)
		{
			for (Sm_CommonMessageDtl object : sm_CommonMessageDtlList)
			{
				Properties properties = new MyProperties();
				

				properties.put("tableId", object.getTableId());
				properties.put("sendTimeStamp", object.getSendTimeStamp().substring(0, 10));
				properties.put("busiState", object.getBusiState());// 0 未确认更新 1 已确认更新
																
				properties.put("isReader", object.getIsReader());// 0 未读  1 已读
				
				properties.put("eCode", object.geteCode());// 业务编码

				if (null == object.getMessage())
				{
					properties.put("theTitle", "");// 主题
					properties.put("theContent", "");// 内容
				}
				else
				{
					
					properties.put("theTitle", object.getMessage().getTheTitle());// 主题
					properties.put("theContent", object.getMessage().getTheContent());// 内容
				}

				list.add(properties);
			}
		}
		return list;
	}



	@SuppressWarnings("rawtypes")
	public List getApproval_BacklogList(List<Sm_CommonMessageDtl> sm_CommonMessageDtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (sm_CommonMessageDtlList != null)
		{
			for (Sm_CommonMessageDtl object : sm_CommonMessageDtlList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("readState", object.getIsReader()); // 0 未读 1 已读

				if(object.getMessage()!=null)
				{
					Sm_CommonMessage sm_commonMessage = object.getMessage();
					properties.put("theme", sm_commonMessage.getTheTitle()); // 主题
					properties.put("theContent", sm_commonMessage.getTheContent()); // 内容
					
					if(S_BusiCode.busiCode_21020102.equals(sm_commonMessage.getBusiCode()) || 
							S_BusiCode.busiCode_21020103.equals(sm_commonMessage.getBusiCode()) || 
							S_BusiCode.busiCode_21020105.equals(sm_commonMessage.getBusiCode()))
					{
						Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter(S_BaseParameter.BusinessCode, sm_commonMessage.getBusiCode());
						
						properties.put("businessNum", sm_commonMessage.getOrgDataCode());//业务单号
						if(sm_BaseParameter != null)
						{
							properties.put("busiType", sm_BaseParameter.getTheName());//业务类型
						}
						if(sm_commonMessage.getUserStart() != null)
						{
							properties.put("applicant",sm_commonMessage.getUserStart().getTheName()); // 申请人
							if(sm_commonMessage.getUserStart().getCompany() != null)
							{
								properties.put("applyAgency", sm_commonMessage.getUserStart().getCompany().getTheName()); //申请机构
							}
						}
						properties.put("applyDate",myDatetime.dateToSimpleString(sm_commonMessage.getCreateTimeStamp()));
					}
					else
					{
						Long afId = Long.valueOf(sm_commonMessage.getOrgDataId());
						Sm_ApprovalProcess_AF sm_approvalProcess_af =sm_ApprovalProcessAfDao.findById(afId);
						if(sm_approvalProcess_af!=null)
						{
							properties.put("afId", sm_approvalProcess_af.getTableId());//申请单Id
							properties.put("businessNum", sm_approvalProcess_af.geteCode());//业务单号
							properties.put("sourceId",sm_approvalProcess_af.getSourceId()); //业务id
							properties.put("currentWorkflowId",sm_approvalProcess_af.getCurrentIndex());
							properties.put("busiType",sm_approvalProcess_af.getBusiType());  //业务类型
							properties.put("busiCode",sm_approvalProcess_af.getBusiCode()); //业务编码

							if(sm_approvalProcess_af.getCompanyInfo() !=null)
							{
								properties.put("applyAgency", sm_approvalProcess_af.getCompanyInfo().getTheName()); //申请机构
							}
							if(sm_approvalProcess_af.getUserStart() != null)
							{
								properties.put("applicant",sm_approvalProcess_af.getUserStart().getTheName()); // 申请人
							}
							properties.put("applyDate",myDatetime.dateToSimpleString(sm_approvalProcess_af.getCreateTimeStamp()));   //申请日期
							properties.put("handleState", S_WorkflowBusiState.WaitSubmit);
						}
					}
				}

				list.add(properties);
			}
		}
		return list;
	}

}
