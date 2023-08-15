package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_IsNeedBackup;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_BaseParameterGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import com.google.gson.Gson;

/*
 * Rebuilder：机构信息
 * Company：ZhiShuSZ
 * */
@Service
public class Emmp_CompanyInfoRebuild extends RebuilderBase<Emmp_CompanyInfo>
{
	@Autowired
	private Sm_BaseParameterGetService sm_BaseParameterGetService;
	
	private MyDatetime myDatetime = MyDatetime.getInstance();

	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Gson gson;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;

	@Override
	public Properties getSimpleInfo(Emmp_CompanyInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
	
		//列表内容
		properties.put("tableId", object.getTableId());
		properties.put("theType", object.getTheType());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("address", object.getAddress());
		properties.put("companyGroup", object.getCompanyGroup());
		properties.put("unifiedSocialCreditCode", object.getUnifiedSocialCreditCode());
		properties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
		properties.put("legalPerson", object.getLegalPerson());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("isUsedState", object.getIsUsedState());
		
		return properties;
	}

	@Override
	public Properties getDetail(Emmp_CompanyInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		//详情
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("address", object.getAddress());
		properties.put("unifiedSocialCreditCode", object.getUnifiedSocialCreditCode());
		properties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
		properties.put("qualificationGrade", object.getQualificationGrade());
		properties.put("theType", object.getTheType());
		
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", object.getTheType());
		if(sm_BaseParameter != null)
		{
			properties.put("sm_BaseParameter", sm_BaseParameter);
			properties.put("parameterName", sm_BaseParameter.getTheName());
		}

		properties.put("companyGroup", object.getCompanyGroup());
		properties.put("legalPerson", object.getLegalPerson());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("recordState", object.getRecordState());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());

		properties.put("isUsedState", object.getIsUsedState());

		if(object.getCityRegion() != null)
		{
			properties.put("cityRegionId", object.getCityRegion().getTableId());
			properties.put("cityRegionName", object.getCityRegion().getTheName());
		}
		if(object.getStreet() != null)
		{
			properties.put("streetId", object.getStreet().getTableId());
			properties.put("streetName", object.getStreet().getTheName());
		}

		if(object.getUserStart() != null)
		{
			properties.put("userStartName", object.getUserStart().getTheName());
		}
		properties.put("createTimeStamp", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));

		if(object.getUserUpdate() != null)
		{
			properties.put("userUpdateName", object.getUserUpdate().getTheName());
		}
		properties.put("lastUpdateTimeStamp", myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));

		if (object.getUserRecord() != null)
		{
			properties.put("userRecordName", object.getUserRecord().getTheName());
		}
		properties.put("recordTimeStamp", myDatetime.dateToSimpleString(object.getRecordTimeStamp()));

		return properties;
	}

	public Properties getDetailForApproval(Emmp_CompanyInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		properties.put("address", object.getAddress());
		properties.put("unifiedSocialCreditCode", object.getUnifiedSocialCreditCode());
		properties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
		properties.put("qualificationGrade", object.getQualificationGrade());
		properties.put("theType", object.getTheType());
		Sm_BaseParameter sm_BaseParameter = sm_BaseParameterGetService.getParameter("8", object.getTheType());
		if(sm_BaseParameter != null)
		{
			properties.put("sm_BaseParameter", sm_BaseParameter);
			properties.put("parameterName", sm_BaseParameter.getTheName());
		}
		properties.put("companyGroup", object.getCompanyGroup());
		properties.put("legalPerson", object.getLegalPerson());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("recordState", object.getRecordState());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());

		properties.put("isUsedState", object.getIsUsedState());

		if(object.getCityRegion() != null)
		{
			properties.put("cityRegionId", object.getCityRegion().getTableId());
			properties.put("cityRegionName", object.getCityRegion().getTheName());
		}
		if(object.getStreet() != null)
		{
			properties.put("streetId", object.getStreet().getTableId());
			properties.put("streetName", object.getStreet().getTheName());
		}

		if(object.getUserStart() != null)
		{
			properties.put("userStartName", object.getUserStart().getTheName());
		}
		properties.put("createTimeStamp", myDatetime.dateToSimpleString(object.getCreateTimeStamp()));

		if(object.getUserUpdate() != null)
		{
			properties.put("userUpdateName", object.getUserUpdate().getTheName());
		}
		properties.put("lastUpdateTimeStamp", myDatetime.dateToSimpleString(object.getLastUpdateTimeStamp()));

		if (S_BusiState.NoRecord.equals(object.getBusiState()))
		{
			return properties;
		}

		//审核的申请单
		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_AFForm.setBusiState("待提交");
		sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());

		String busiCode;
		if (S_BusiState.NoRecord.equals(object.getBusiState()))
		{
			switch (object.getTheType())
			{
				case S_CompanyType.Agency:
					busiCode = "020103";
					break;
				case S_CompanyType.Witness:
					busiCode = "020106";
					break;
				case S_CompanyType.Cooperation:
					busiCode = "020108";
					break;
				case S_CompanyType.Supervisor:
                    busiCode = "020131";
                    break;
				case S_CompanyType.Inspection:
                    busiCode = "020141";
                    break;
				default:
					busiCode = "020101";
					break;
			}
		}
		else
		{
			switch (object.getTheType())
			{
				case S_CompanyType.Agency:
					busiCode = "020105";
					break;
				case S_CompanyType.Witness:
					busiCode = "020107";
					break;
				case S_CompanyType.Cooperation:
					busiCode = "020109";
					break;
				case S_CompanyType.Supervisor:
                    busiCode = "020132";
                    break;
                case S_CompanyType.Inspection:
                    busiCode = "020142";
                    break;
				default:
					busiCode = "020102";
					break;
			}
		}

		sm_ApprovalProcess_AFForm.setBusiCode(busiCode);
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

		if (sm_ApprovalProcess_AF == null)
		{
			sm_ApprovalProcess_AFForm.setBusiState("审核中");
			sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

			if (sm_ApprovalProcess_AF == null)
			{
				return properties;
			}
		}

		Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
		Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao.findById(currentNode);
		Boolean isNeedBackup = null;
		if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
		{
			if(S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup()))
			{
				isNeedBackup = true;
			}
		}
		else
		{
			isNeedBackup = false;
		}

		properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮

		String expectObj = sm_ApprovalProcess_AF.getExpectObjJson();
		if (expectObj != null && expectObj.length() > 0 )
		{
			Emmp_CompanyInfoForm emmp_CompanyInfoForm = gson.fromJson(expectObj, Emmp_CompanyInfoForm.class);

			if (object.getTheType().equals(S_CompanyType.Development))
			{

				Properties oldProperties = new MyProperties();

				oldProperties.put("theName", object.getTheName());
				oldProperties.put("address", object.getAddress());
				oldProperties.put("legalPerson", object.getLegalPerson());
				oldProperties.put("projectLeader", object.getProjectLeader());

				oldProperties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));

				if(object.getCityRegion() != null)
				{
					oldProperties.put("cityRegionId", object.getCityRegion().getTableId());
					oldProperties.put("cityRegionName", object.getCityRegion().getTheName());
				}
				if(object.getStreet() != null)
				{
					oldProperties.put("streetId", object.getStreet().getTableId());
					oldProperties.put("streetName", object.getStreet().getTheName());
				}

				oldProperties.put("contactPerson", object.getContactPerson());
				oldProperties.put("contactPhone", object.getContactPhone());

				properties.put("oldObj", oldProperties);//修改前的对象

				properties.put("theName", emmp_CompanyInfoForm.getTheName());
				properties.put("address", emmp_CompanyInfoForm.getAddress());
				properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
				properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

				properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
				properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

				properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());

				Sm_CityRegionInfo cityRegion = sm_CityRegionInfoDao.findById(emmp_CompanyInfoForm.getCityRegionId());
				if (cityRegion != null)
				{
					properties.put("cityRegionId", cityRegion.getTableId());
					properties.put("cityRegionName", cityRegion.getTheName());
				}

				Sm_StreetInfo street = sm_StreetInfoDao.findById(emmp_CompanyInfoForm.getStreetId());
				if (street != null)
				{
					properties.put("streetId", street.getTableId());
					properties.put("streetName", street.getTheName());
				}
			}
			else
			{
				Properties oldProperties = new MyProperties();

				oldProperties.put("theName", object.getTheName());
				oldProperties.put("address", object.getAddress());
				oldProperties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
				oldProperties.put("legalPerson", object.getLegalPerson());
				oldProperties.put("projectLeader", object.getProjectLeader());

				oldProperties.put("contactPerson", object.getContactPerson());
				oldProperties.put("contactPhone", object.getContactPhone());

				properties.put("oldObj", oldProperties);//修改前的对象

				properties.put("theName", emmp_CompanyInfoForm.getTheName());
				properties.put("address", emmp_CompanyInfoForm.getAddress());
				properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
				properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

				properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
				properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

				properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());
			}
		}

		return properties;
	}

	public Properties getDetailForApproval2(Emmp_CompanyInfo object, Emmp_CompanyInfoForm model)
	{

		Properties properties = getDetail(object); //先存放数据库的数据

		Long afId = model.getAfId();//申请单Id
		Sm_ApprovalProcess_AF sm_approvalProcess_af = new Sm_ApprovalProcess_AF();
		if(afId!=null && afId > 0)
		{
			sm_approvalProcess_af = sm_ApprovalProcess_AFDao.findById(afId);
		}

		Long tableId = object.getTableId();
		String busiState = object.getBusiState();

		String busiCode = sm_approvalProcess_af.getBusiCode();

		int busiCodeAddOrUpdate = getBusiCodeAddOrUpdate(busiCode);
		if (busiCodeAddOrUpdate == 1) //如果是初始维护审批详情
		{
			if (busiState.equals(S_BusiState.NoRecord)) //如果是未备案，则是新增待提交的审批流，使用Database数据
			{
				return properties; //直接返回
			}
			else if (busiState.equals(S_BusiState.HaveRecord)) //如果是已备案，则是看的历史数据，在OSS 中
			{
				Emmp_CompanyInfoForm emmp_CompanyInfoForm = gson.fromJson(sm_approvalProcess_af.getExpectObjJson(), Emmp_CompanyInfoForm.class);

				//塞入旧值
				if (object.getTheType().equals(S_CompanyType.Development))
				{
					properties.put("theName", emmp_CompanyInfoForm.getTheName());
					properties.put("address", emmp_CompanyInfoForm.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());

					Sm_CityRegionInfo cityRegion = sm_CityRegionInfoDao.findById(emmp_CompanyInfoForm.getCityRegionId());
					if (cityRegion != null)
					{
						properties.put("cityRegionId", cityRegion.getTableId());
						properties.put("cityRegionName", cityRegion.getTheName());
					}

					Sm_StreetInfo street = sm_StreetInfoDao.findById(emmp_CompanyInfoForm.getStreetId());
					if (street != null)
					{
						properties.put("streetId", street.getTableId());
						properties.put("streetName", street.getTheName());
					}
				}
				else
				{
					properties.put("theName", emmp_CompanyInfoForm.getTheName());
					properties.put("address", emmp_CompanyInfoForm.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());
				}
			}
		}
		else if (busiCodeAddOrUpdate == 2) //如果是变更审批详情
		{
			if (sm_approvalProcess_af.getBusiState().equals(S_ApprovalState.WaitSubmit) || sm_approvalProcess_af.getBusiState().equals(S_ApprovalState.Examining))  //如果是待提交审核中，则是取当前database中的数据和申请单进行对比
			{
				Emmp_CompanyInfoForm emmp_CompanyInfoForm = gson.fromJson(sm_approvalProcess_af.getExpectObjJson(), Emmp_CompanyInfoForm.class);

				//新旧数据 对比
				if (object.getTheType().equals(S_CompanyType.Development))
				{

					Properties oldProperties = new MyProperties();

					oldProperties.put("theName", object.getTheName());
					oldProperties.put("address", object.getAddress());
					oldProperties.put("legalPerson", object.getLegalPerson());
					oldProperties.put("projectLeader", object.getProjectLeader());

					oldProperties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));

					if(object.getCityRegion() != null)
					{
						oldProperties.put("cityRegionId", object.getCityRegion().getTableId());
						oldProperties.put("cityRegionName", object.getCityRegion().getTheName());
					}
					if(object.getStreet() != null)
					{
						oldProperties.put("streetId", object.getStreet().getTableId());
						oldProperties.put("streetName", object.getStreet().getTheName());
					}

					oldProperties.put("contactPerson", object.getContactPerson());
					oldProperties.put("contactPhone", object.getContactPhone());

					properties.put("oldObj", oldProperties);//修改前的对象

					properties.put("theName", emmp_CompanyInfoForm.getTheName());
					properties.put("address", emmp_CompanyInfoForm.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());

					Sm_CityRegionInfo cityRegion = sm_CityRegionInfoDao.findById(emmp_CompanyInfoForm.getCityRegionId());
					if (cityRegion != null)
					{
						properties.put("cityRegionId", cityRegion.getTableId());
						properties.put("cityRegionName", cityRegion.getTheName());
					}

					Sm_StreetInfo street = sm_StreetInfoDao.findById(emmp_CompanyInfoForm.getStreetId());
					if (street != null)
					{
						properties.put("streetId", street.getTableId());
						properties.put("streetName", street.getTheName());
					}
				}
				else
				{
					Properties oldProperties = new MyProperties();

					oldProperties.put("theName", object.getTheName());
					oldProperties.put("address", object.getAddress());
					oldProperties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
					oldProperties.put("legalPerson", object.getLegalPerson());
					oldProperties.put("projectLeader", object.getProjectLeader());

					oldProperties.put("contactPerson", object.getContactPerson());
					oldProperties.put("contactPhone", object.getContactPhone());

					properties.put("oldObj", oldProperties);//修改前的对象

					properties.put("theName", emmp_CompanyInfoForm.getTheName());
					properties.put("address", emmp_CompanyInfoForm.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoForm.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoForm.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoForm.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoForm.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoForm.getRegisteredDateStr());
				}
			}
			else //剩下的是已完结和已删除，则是两个申请单进行对比
			{
				Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
				sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
				sm_ApprovalProcess_AFForm.setSourceId(tableId);
				sm_ApprovalProcess_AFForm.setOrderBy("createTimeStamp asc");
				List<Sm_ApprovalProcess_AF> afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
				int indexOfNow = -1;
				for (int i = 0; i < afList.size(); i++) {
					Sm_ApprovalProcess_AF item = afList.get(i);
					if (item.getTableId().equals(afId)) {
						indexOfNow = i;
						break;
					}
				}
				int oldHistory = indexOfNow - 1;
				Sm_ApprovalProcess_AF oldAf = afList.get(oldHistory);

				Emmp_CompanyInfoForm emmp_CompanyInfoFormOld = gson.fromJson(oldAf.getExpectObjJson(), Emmp_CompanyInfoForm.class);
				Emmp_CompanyInfoForm emmp_CompanyInfoFormNew = gson.fromJson(sm_approvalProcess_af.getExpectObjJson(), Emmp_CompanyInfoForm.class);

				//新旧数据 对比
				if (object.getTheType().equals(S_CompanyType.Development))
				{

					Properties oldProperties = new MyProperties();

					oldProperties.put("theName", emmp_CompanyInfoFormOld.getTheName());
					oldProperties.put("address", emmp_CompanyInfoFormOld.getAddress());
					oldProperties.put("legalPerson", emmp_CompanyInfoFormOld.getLegalPerson());
					oldProperties.put("projectLeader", emmp_CompanyInfoFormOld.getProjectLeader());

					oldProperties.put("registeredDateStr", emmp_CompanyInfoFormOld.getRegisteredDateStr());

					Sm_CityRegionInfo cityRegionOld = sm_CityRegionInfoDao.findById(emmp_CompanyInfoFormOld.getCityRegionId());
					if (cityRegionOld != null)
					{
						oldProperties.put("cityRegionId", cityRegionOld.getTableId());
						oldProperties.put("cityRegionName", cityRegionOld.getTheName());
					}

					Sm_StreetInfo streetOld = sm_StreetInfoDao.findById(emmp_CompanyInfoFormOld.getStreetId());
					if (streetOld != null)
					{
						oldProperties.put("streetId", streetOld.getTableId());
						oldProperties.put("streetName", streetOld.getTheName());
					}

					oldProperties.put("contactPerson", object.getContactPerson());
					oldProperties.put("contactPhone", object.getContactPhone());

					properties.put("oldObj", oldProperties);//修改前的对象

					properties.put("theName", emmp_CompanyInfoFormNew.getTheName());
					properties.put("address", emmp_CompanyInfoFormNew.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoFormNew.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoFormNew.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoFormNew.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoFormNew.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoFormNew.getRegisteredDateStr());

					Sm_CityRegionInfo cityRegion = sm_CityRegionInfoDao.findById(emmp_CompanyInfoFormNew.getCityRegionId());
					if (cityRegion != null)
					{
						properties.put("cityRegionId", cityRegion.getTableId());
						properties.put("cityRegionName", cityRegion.getTheName());
					}

					Sm_StreetInfo street = sm_StreetInfoDao.findById(emmp_CompanyInfoFormNew.getStreetId());
					if (street != null)
					{
						properties.put("streetId", street.getTableId());
						properties.put("streetName", street.getTheName());
					}
				}
				else
				{
					Properties oldProperties = new MyProperties();

					oldProperties.put("theName", emmp_CompanyInfoFormOld.getTheName());
					oldProperties.put("address", emmp_CompanyInfoFormOld.getAddress());
					oldProperties.put("registeredDateStr", emmp_CompanyInfoFormOld.getRegisteredDateStr());
					oldProperties.put("legalPerson", emmp_CompanyInfoFormOld.getLegalPerson());
					oldProperties.put("projectLeader", emmp_CompanyInfoFormOld.getProjectLeader());

					oldProperties.put("contactPerson", emmp_CompanyInfoFormOld.getContactPerson());
					oldProperties.put("contactPhone", emmp_CompanyInfoFormOld.getContactPhone());

					properties.put("oldObj", oldProperties);//修改前的对象

					properties.put("theName", emmp_CompanyInfoFormNew.getTheName());
					properties.put("address", emmp_CompanyInfoFormNew.getAddress());
					properties.put("legalPerson", emmp_CompanyInfoFormNew.getLegalPerson());
					properties.put("projectLeader", emmp_CompanyInfoFormNew.getProjectLeader());

					properties.put("contactPerson", emmp_CompanyInfoFormNew.getContactPerson());
					properties.put("contactPhone", emmp_CompanyInfoFormNew.getContactPhone());

					properties.put("registeredDateStr", emmp_CompanyInfoFormNew.getRegisteredDateStr());
				}
			}
		}

		return properties;
	}

	public int getBusiCodeAddOrUpdate(String busiCode) {
		if (StringUtils.isEmpty(busiCode)) {
			return -1;
		}
		switch (busiCode) {
		case S_BusiCode.busiCode_020101:
		case S_BusiCode.busiCode_020103:
		case S_BusiCode.busiCode_020106:
		case S_BusiCode.busiCode_020108:
		case S_BusiCode.busiCode_020131:
		case S_BusiCode.busiCode_020141:
			return 1;
		case S_BusiCode.busiCode_020102:
		case S_BusiCode.busiCode_020105:
		case S_BusiCode.busiCode_020107:
		case S_BusiCode.busiCode_020109:
		case S_BusiCode.busiCode_020132:
		case S_BusiCode.busiCode_020142:
			return 2;
		default:
			return 1;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_CompanyInfo> emmp_CompanyInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_CompanyInfoList != null)
		{
			for(Emmp_CompanyInfo object:emmp_CompanyInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("recordState", object.getRecordState());
				properties.put("recordRejectReason", object.getRecordRejectReason());
				properties.put("theType", object.getTheType());
				properties.put("companyGroup", object.getCompanyGroup());
				properties.put("theName", object.getTheName());
				properties.put("shortName", object.getShortName());
				properties.put("eCodeFromPresellSystem", object.geteCodeFromPresellSystem());
				properties.put("establishmentDate", object.getEstablishmentDate());
				properties.put("qualificationGrade", object.getQualificationGrade());
				properties.put("unifiedSocialCreditCode", object.getUnifiedSocialCreditCode());
				properties.put("registeredFund", object.getRegisteredFund());
				properties.put("businessScope", object.getBusinessScope());
				properties.put("registeredDateStr", myDatetime.dateToSimpleString(object.getRegisteredDate()));
				properties.put("expiredDate", object.getExpiredDate());
				properties.put("contactPerson", object.getContactPerson());
				properties.put("contactPhone", object.getContactPhone());
				properties.put("projectLeader", object.getProjectLeader());
				properties.put("financialAccount", object.getFinancialAccount());
				properties.put("financialAccountId", object.getFinancialAccount().getTableId());
				properties.put("qualificationInformationList", object.getQualificationInformationList());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("street", object.getStreet());
				properties.put("streetId", object.getStreet().getTableId());
				properties.put("theURL", object.getTheURL());
				properties.put("address", object.getAddress());
				properties.put("email", object.getEmail());
				properties.put("theFax", object.getTheFax());
				properties.put("eCodeOfPost", object.geteCodeOfPost());
				properties.put("introduction", object.getIntroduction());
				properties.put("remark", object.getRemark());
				properties.put("logId", object.getLogId());

				properties.put("busiState", object.getBusiState());
				properties.put("approvalState", object.getApprovalState());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List executeForSelectList(List<Emmp_CompanyInfo> emmp_CompanyInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_CompanyInfoList != null)
		{
			for(Emmp_CompanyInfo object:emmp_CompanyInfoList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theName", object.getTheName());
				properties.put("tableId", object.getTableId());
				properties.put("theType", object.getTheType());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	public Properties getSelectForQuery(Emmp_CompanyInfo object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
	
		//列表内容
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		
		return properties;
	}
}
