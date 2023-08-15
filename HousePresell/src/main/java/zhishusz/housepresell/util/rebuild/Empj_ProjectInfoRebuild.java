package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PresellDocumentInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：项目信息
 * Company：ZhiShuSZ
 */
@Service
public class Empj_ProjectInfoRebuild extends RebuilderBase<Empj_ProjectInfo>
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_BuildingInfoRebuild empj_BuildingInfoRebuild;

	@SuppressWarnings({
			"unchecked", "static-access"
	})
	@Override
	public Properties getSimpleInfo(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());
		if (object.getDevelopCompany() != null)
		{
			properties.put("developCompanyName", object.getDevelopCompany().getTheName());
		}
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());
		}
		properties.put("address", object.getAddress());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("leaderPhone", object.getLeaderPhone());

		properties.put("longitude", object.getLongitude());
		properties.put("latitude", object.getLatitude());
		properties.put("address", object.getAddress());

		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setProjectId(object.getTableId());
		empj_BuildingInfoForm.setTheState(S_TheState.Normal);

		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao
				.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));

		if (empj_BuildingInfoList != null && !empj_BuildingInfoList.isEmpty())
			;
		{
			Double escrowArea = 0.0;
			for (Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if (empj_BuildingInfo.getEscrowArea() != null && empj_BuildingInfo.getEscrowArea() > 0)
				{
					escrowArea += empj_BuildingInfo.getEscrowArea();
				}
			}
			properties.put("escrowArea", MyDouble.getInstance().pointTOThousandths(escrowArea));
		}

		return properties;
	}

	@SuppressWarnings({
			"rawtypes", "unchecked", "static-access"
	})
	public List getSimpleInfoForTable(List<Empj_ProjectInfo> empj_ProjectInfoList, Empj_ProjectInfoForm model)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (empj_ProjectInfoList != null)
		{
			for (Empj_ProjectInfo object : empj_ProjectInfoList)
			{
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("busiState", object.getBusiState());
				properties.put("approvalState", object.getApprovalState());
				properties.put("eCode", object.geteCode());
				properties.put("theName", object.getTheName());
				if (object.getDevelopCompany() != null)
				{
					properties.put("developCompanyName", object.getDevelopCompany().getTheName());
				}
				if (object.getCityRegion() != null)
				{
					properties.put("cityRegionName", object.getCityRegion().getTheName());
				}
				properties.put("address", object.getAddress());
				properties.put("projectLeader", object.getProjectLeader());
				properties.put("leaderPhone", object.getLeaderPhone());

				properties.put("longitude", object.getLongitude());
				properties.put("latitude", object.getLatitude());
				properties.put("address", object.getAddress());

				Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
				empj_BuildingInfoForm.setProjectId(object.getTableId());
				empj_BuildingInfoForm.setTheState(S_TheState.Normal);
				empj_BuildingInfoForm.setBusiState(S_BusiState.HaveRecord);
				empj_BuildingInfoForm.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
				empj_BuildingInfoForm.setProjectInfoIdArr(model.getProjectInfoIdArr());
				empj_BuildingInfoForm.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
				empj_BuildingInfoForm.setUserId(model.getUserId());

				List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(
						empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));

				if (empj_BuildingInfoList != null && !empj_BuildingInfoList.isEmpty())
					;
				{
					Double escrowArea = 0.0;
					for (Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
					{
						if (empj_BuildingInfo.getEscrowArea() != null && empj_BuildingInfo.getEscrowArea() > 0)
						{
							escrowArea += empj_BuildingInfo.getEscrowArea();
						}
					}
					properties.put("escrowArea", MyDouble.getInstance().pointTOThousandths(escrowArea));
				}

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Properties getDetail(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		if (object.getDevelopCompany() != null)
		{
			properties.put("developCompanyName", object.getDevelopCompany().getTheName());// 开发企业名称
			properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		}
		properties.put("eCode", object.geteCode());
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());// 所属区域
			properties.put("cityRegionId", object.getCityRegion().getTableId());
		}
		if (object.getStreet() != null)
		{
			properties.put("streetName", object.getStreet().getTheName());
			properties.put("streetId", object.getStreet().getTableId());
		}
		properties.put("longitude", object.getLongitude());
		properties.put("latitude", object.getLatitude());
		properties.put("eastLongitude", object.getEastLongitude());
		properties.put("eastLatitude", object.getEastLatitude());
		properties.put("westLongitude", object.getWestLongitude());
		properties.put("westLatitude", object.getWestLatitude());
		properties.put("southLongitude", object.getSouthLongitude());
		properties.put("southLatitude", object.getSouthLatitude());
		properties.put("northLongitude", object.getNorthLongitude());
		properties.put("northLatitude", object.getNorthLatitude());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("leaderPhone", object.getLeaderPhone());

		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate == null)
		{
			userUpdate = object.getUserStart();
		}
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userStartName", userUpdate.getTheName());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{
			properties.put("userRecordId", userRecord.getTableId());
			properties.put("userRecordName", userRecord.getTheName());
		}
		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToSimpleString(operationTimeStamp));
		properties.put("recordTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getRecordTimeStamp()));

		properties.put("theName", object.getTheName());// 项目名称
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());
		}
		properties.put("address", object.getAddress());// 项目地址
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("remark", object.getRemark());
		properties.put("introduction", object.getIntroduction());

		// 楼盘表详情使用
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setProjectId(object.getTableId());
		empj_BuildingInfoForm.setBusiState(S_BusiState.HaveRecord);
		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao
				.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getProjectHQL(), empj_BuildingInfoForm));

		if (empj_BuildingInfoList != null && !empj_BuildingInfoList.isEmpty())
			;
		{
			properties.put("empj_BuildingInfoList",
					empj_BuildingInfoRebuild.executeForBuildingTableDetail(empj_BuildingInfoList));// 楼幢列表

			Double escrowArea = 0.0;
			Double buildingArea = 0.0;
			for (Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if (empj_BuildingInfo.getEscrowArea() != null && empj_BuildingInfo.getEscrowArea() > 0)
				{
					escrowArea += empj_BuildingInfo.getEscrowArea();
				}

				if (empj_BuildingInfo.getBuildingArea() != null && empj_BuildingInfo.getBuildingArea() > 0)
				{
					buildingArea += empj_BuildingInfo.getBuildingArea();
				}
			}
			properties.put("escrowArea", escrowArea);// 楼幢总托管面积
			properties.put("buildingArea", buildingArea);// 楼幢总建筑面积
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties getDetailForTableDetail(Empj_ProjectInfo object, Empj_ProjectInfoForm model)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		if (object.getDevelopCompany() != null)
		{
			properties.put("developCompanyName", object.getDevelopCompany().getTheName());// 开发企业名称
			properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		}
		properties.put("eCode", object.geteCode());
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());// 所属区域
			properties.put("cityRegionId", object.getCityRegion().getTableId());
		}
		if (object.getStreet() != null)
		{
			properties.put("streetName", object.getStreet().getTheName());
			properties.put("streetId", object.getStreet().getTableId());
		}
		properties.put("longitude", object.getLongitude());
		properties.put("latitude", object.getLatitude());
		properties.put("eastLongitude", object.getEastLongitude());
		properties.put("eastLatitude", object.getEastLatitude());
		properties.put("westLongitude", object.getWestLongitude());
		properties.put("westLatitude", object.getWestLatitude());
		properties.put("southLongitude", object.getSouthLongitude());
		properties.put("southLatitude", object.getSouthLatitude());
		properties.put("northLongitude", object.getNorthLongitude());
		properties.put("northLatitude", object.getNorthLatitude());
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("leaderPhone", object.getLeaderPhone());

		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate == null)
		{
			userUpdate = object.getUserStart();
		}
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userStartName", userUpdate.getTheName());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{
			properties.put("userRecordId", userRecord.getTableId());
			properties.put("userRecordName", userRecord.getTheName());
		}
		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToSimpleString(operationTimeStamp));
		properties.put("recordTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getRecordTimeStamp()));

		properties.put("theName", object.getTheName());// 项目名称
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());
		}
		properties.put("address", object.getAddress());// 项目地址
		properties.put("contactPerson", object.getContactPerson());
		properties.put("contactPhone", object.getContactPhone());
		properties.put("remark", object.getRemark());
		properties.put("introduction", object.getIntroduction());

		// 楼盘表详情使用
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setProjectId(object.getTableId());
		empj_BuildingInfoForm.setBusiState(S_BusiState.HaveRecord);
		empj_BuildingInfoForm.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
		empj_BuildingInfoForm.setProjectInfoIdArr(model.getProjectInfoIdArr());
		empj_BuildingInfoForm.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
		empj_BuildingInfoForm.setUserId(model.getUserId());

		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao
				.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getProjectHQL(), empj_BuildingInfoForm));

		if (empj_BuildingInfoList != null && !empj_BuildingInfoList.isEmpty())
			;
		{
			properties.put("empj_BuildingInfoList",
					empj_BuildingInfoRebuild.executeForBuildingTableDetail(empj_BuildingInfoList));// 楼幢列表

			Double escrowArea = 0.0;
			Double buildingArea = 0.0;
			for (Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				if (empj_BuildingInfo.getEscrowArea() != null && empj_BuildingInfo.getEscrowArea() > 0)
				{
					escrowArea += empj_BuildingInfo.getEscrowArea();
				}

				if (empj_BuildingInfo.getBuildingArea() != null && empj_BuildingInfo.getBuildingArea() > 0)
				{
					buildingArea += empj_BuildingInfo.getBuildingArea();
				}
			}
			properties.put("escrowArea", escrowArea);// 楼幢总托管面积
			properties.put("buildingArea", buildingArea);// 楼幢总建筑面积
		}

		return properties;
	}

	public Properties getDetailForApprovalProcess(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 先取出PO中数据，在从审批流中取出覆盖部分
		properties.put("tableId", object.getTableId());
		properties.put("busiState", object.getBusiState());
		properties.put("approvalState", object.getApprovalState());
		if (object.getDevelopCompany() != null)
		{
			properties.put("developCompanyName", object.getDevelopCompany().getTheName());// 开发企业名称
			properties.put("developCompanyId", object.getDevelopCompany().getTableId());
		}
		properties.put("eCode", object.geteCode());
		properties.put("theName", object.getTheName());// 项目名称
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());// 所属区域
			properties.put("cityRegionId", object.getCityRegion().getTableId());
		}
		if (object.getStreet() != null)
		{
			properties.put("streetName", object.getStreet().getTheName());
			properties.put("streetId", object.getStreet().getTableId());
		}
		properties.put("longitude", object.getLongitude());
		properties.put("latitude", object.getLatitude());
		properties.put("address", object.getAddress());// 项目地址
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("leaderPhone", object.getLeaderPhone());

		Sm_User userUpdate = object.getUserUpdate();
		if (userUpdate == null)
		{
			userUpdate = object.getUserStart();
		}
		if (userUpdate != null)
		{
			properties.put("userUpdateId", userUpdate.getTableId());
			properties.put("userStartName", userUpdate.getTheName());
			properties.put("userUpdateName", userUpdate.getTheName());
		}
		Sm_User userRecord = object.getUserRecord();
		if (userRecord != null)
		{
			properties.put("userRecordId", userRecord.getTableId());
			properties.put("userRecordName", userRecord.getTheName());
		}
		Long operationTimeStamp = object.getLastUpdateTimeStamp();
		if (operationTimeStamp == null || operationTimeStamp < 1)
		{
			operationTimeStamp = object.getCreateTimeStamp();
		}
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToSimpleString(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(operationTimeStamp));
		properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		properties.put("introduction", object.getIntroduction());
		properties.put("remark", object.getRemark());

		// if (S_BusiState.NoRecord.equals(object.getBusiState()))
		// {
		// return properties;
		// }

		// //查待提交的申请单
		// Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new
		// Sm_ApprovalProcess_AFForm();
		// sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
		// sm_ApprovalProcess_AFForm.setBusiState("待提交");
		// sm_ApprovalProcess_AFForm.setSourceId(object.getTableId());
		// if(S_BusiState.HaveRecord.equals(object.getBusiState()))
		// {
		// sm_ApprovalProcess_AFForm.setBusiCode(S_BusiCode.busiCode_03010102);
		// }
		// if(S_BusiState.NoRecord.equals(object.getBusiState()))
		// {
		// sm_ApprovalProcess_AFForm.setBusiCode(S_BusiCode.busiCode_03010101);
		// }
		// Sm_ApprovalProcess_AF sm_ApprovalProcess_AF =
		// sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
		// sm_ApprovalProcess_AFForm));
		//
		// if(sm_ApprovalProcess_AF == null)
		// {
		// sm_ApprovalProcess_AFForm.setBusiState("审核中");
		// sm_ApprovalProcess_AF =
		// sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(),
		// sm_ApprovalProcess_AFForm));
		//
		// if(sm_ApprovalProcess_AF == null)
		// {
		// return properties;
		// }
		// }
		//
		// Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
		// Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow =
		// sm_approvalProcess_workflowDao.findById(currentNode);
		// Boolean isNeedBackup = null;
		// if(sm_approvalProcess_workflow.getNextWorkFlow() == null)
		// {
		// if(S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup()))
		// {
		// isNeedBackup = true;
		// }
		// }
		// else
		// {
		// isNeedBackup = false;
		// }
		//
		// properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮
		//
		// String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
		// if(jsonNewStr != null && jsonNewStr.length() > 0)
		// {
		// Empj_ProjectInfoForm empj_projectInfoForm = gson.fromJson(jsonNewStr,
		// Empj_ProjectInfoForm.class);
		//
		// properties.put("oldProject", getDetailForOld(object));//修改前的对象
		//
		// properties.put("theName", empj_projectInfoForm.getTheName());
		// Long cityRegionId = empj_projectInfoForm.getCityRegionId();
		// Sm_CityRegionInfo cityRegion =
		// (Sm_CityRegionInfo)sm_CityRegionInfoDao.findById(cityRegionId);
		// if(cityRegion != null)
		// {
		// properties.put("cityRegionName", cityRegion.getTheName());//所属区域
		// properties.put("cityRegionId", cityRegion.getTableId());
		// }
		// Long streetId = empj_projectInfoForm.getStreetId();
		// Sm_StreetInfo street =
		// (Sm_StreetInfo)sm_StreetInfoDao.findById(streetId);
		// if(street != null)
		// {
		// properties.put("streetName", street.getTheName());
		// properties.put("streetId", street.getTableId());
		// }
		// properties.put("address", empj_projectInfoForm.getAddress());
		// properties.put("longitude", empj_projectInfoForm.getLongitude());
		// properties.put("latitude", empj_projectInfoForm.getLatitude());
		// properties.put("projectLeader",
		// empj_projectInfoForm.getProjectLeader());
		// properties.put("leaderPhone", empj_projectInfoForm.getLeaderPhone());
		// properties.put("introduction",
		// empj_projectInfoForm.getIntroduction());
		// properties.put("remark", empj_projectInfoForm.getRemark());
		// }

		return properties;
	}

	public Properties getDetailForOld(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theName", object.getTheName());// 项目名称
		if (object.getCityRegion() != null)
		{
			properties.put("cityRegionName", object.getCityRegion().getTheName());// 所属区域
			properties.put("cityRegionId", object.getCityRegion().getTableId());
		}
		if (object.getStreet() != null)
		{
			properties.put("streetName", object.getStreet().getTheName());
			properties.put("streetId", object.getStreet().getTableId());
		}

		properties.put("longitude", object.getLongitude());
		properties.put("latitude", object.getLatitude());
		properties.put("address", object.getAddress());// 项目地址
		properties.put("projectLeader", object.getProjectLeader());
		properties.put("leaderPhone", object.getLeaderPhone());
		properties.put("introduction", object.getIntroduction());
		properties.put("remark", object.getRemark());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Empj_ProjectInfo> empj_ProjectInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (empj_ProjectInfoList != null)
		{
			for (Empj_ProjectInfo object : empj_ProjectInfoList)
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
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("theType", object.getTheType());
				properties.put("zoneCode", object.getZoneCode());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("street", object.getStreet());
				properties.put("streetId", object.getStreet().getTableId());
				properties.put("address", object.getAddress());
				properties.put("doorNumber", object.getDoorNumber());
				properties.put("doorNumberAnnex", object.getDoorNumberAnnex());
				properties.put("introduction", object.getIntroduction());
				properties.put("longitude", object.getLongitude());
				properties.put("latitude", object.getLatitude());
				properties.put("propertyType", object.getPropertyType());
				properties.put("theName", object.getTheName());
				properties.put("legalName", object.getLegalName());
				properties.put("buildYear", object.getBuildYear());
				properties.put("isPartition", object.getIsPartition());
				properties.put("theProperty", object.getTheProperty());
				properties.put("contactPerson", object.getContactPerson());
				properties.put("contactPhone", object.getContactPhone());
				properties.put("projectLeader", object.getProjectLeader());
				properties.put("leaderPhone", object.getLeaderPhone());
				properties.put("landArea", object.getLandArea());
				properties.put("obtainMethod", object.getObtainMethod());
				properties.put("investment", object.getInvestment());
				properties.put("landInvest", object.getLandInvest());
				properties.put("coverArea", object.getCoverArea());
				properties.put("houseArea", object.getHouseArea());
				properties.put("siteArea", object.getSiteArea());
				properties.put("planArea", object.getPlanArea());
				properties.put("agArea", object.getAgArea());
				properties.put("ugArea", object.getUgArea());
				properties.put("greenRatio", object.getGreenRatio());
				properties.put("capacity", object.getCapacity());
				properties.put("parkRatio", object.getParkRatio());
				properties.put("unitCount", object.getUnitCount());
				properties.put("buildingCount", object.getBuildingCount());
				properties.put("payDate", object.getPayDate());
				properties.put("planStartDate", object.getPlanStartDate());
				properties.put("planEndDate", object.getPlanEndDate());
				properties.put("developDate", object.getDevelopDate());
				properties.put("designCompany", object.getDesignCompany());
				properties.put("designCompanyId", object.getDesignCompany().getTableId());
				properties.put("eCodeOfDesignCompany", object.geteCodeOfDesignCompany());
				properties.put("remark", object.getRemark());
				properties.put("developProgress", object.getDevelopProgress());
				properties.put("eastAddress", object.getEastAddress());
				properties.put("eastLongitude", object.getEastLongitude());
				properties.put("eastLatitude", object.getEastLatitude());
				properties.put("westAddress", object.getWestAddress());
				properties.put("westLongitude", object.getWestLongitude());
				properties.put("westLatitude", object.getWestLatitude());
				properties.put("southAddress", object.getSouthAddress());
				properties.put("southLongitude", object.getSouthLongitude());
				properties.put("southLatitude", object.getSouthLatitude());
				properties.put("northAddress", object.getNorthAddress());
				properties.put("northLongitude", object.getNorthLongitude());
				properties.put("northLatitude", object.getNorthLatitude());

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public List executeForSelectList(List<Empj_ProjectInfo> empj_ProjectInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (empj_ProjectInfoList != null)
		{
			for (Empj_ProjectInfo object : empj_ProjectInfoList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());
				properties.put("eCode", object.geteCode());
				properties.put("eCodeOfProject", object.geteCode());

				if (object.getDevelopCompany() != null)
				{
					properties.put("developCompanyId", object.getDevelopCompany().getTableId());
					properties.put("developCompanyName", object.getDevelopCompany().getTheName());
				}
				Sm_CityRegionInfo cityRegionInfo = object.getCityRegion();
				if (cityRegionInfo != null)
				{
					properties.put("cityRegionName", object.getCityRegion().getTheName());
				}
				properties.put("address", object.getAddress());

				list.add(properties);
			}
		}
		return list;
	}

	public Properties getByDetail(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 开发企业名称
		if (object.getDevelopCompany() != null)
		{
			properties.put("developCompanyName", object.getDevelopCompany().getTheName());
		}
		else
		{
			properties.put("developCompanyName", "--");
		}
		// 项目对应主键
		properties.put("tableId", object.getTableId());
		// 项目名称
		properties.put("theName", object.getTheName());
		// 项目编号
		properties.put("eCode", object.geteCode());
		// 托管面积
		properties.put("totalArea", null == object.getLandArea() ? 0.00 : object.getLandArea());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List executeForHomePageList(List<Empj_ProjectInfo> empj_ProjectInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (empj_ProjectInfoList != null)
		{
			for (Empj_ProjectInfo object : empj_ProjectInfoList)
			{
				Properties properties = new MyProperties();
				properties.put("tableId", object.getTableId());
				properties.put("theName", object.getTheName());

				properties.put("address", object.getAddress());

				list.add(properties);
			}
		}
		return list;
	}

	public Properties getHomePageByDetail(Empj_ProjectInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 项目对应主键
		properties.put("tableId", object.getTableId());
		// 项目名称
		properties.put("theName", object.getTheName());
		// 项目编号
		properties.put("eCode", object.geteCode());
		// 托管面积
		properties.put("totalArea", null == object.getLandArea() ? 0.00 : object.getLandArea());
		// 项目地址
		properties.put("address", object.getAddress());

		return properties;
	}

	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;

	@SuppressWarnings({
			"unchecked"
	})
	public List<Map<String, Object>> getDepositProject(List<Empj_ProjectInfo> empj_ProjectInfoList)
	{
		MyDatetime myTime = MyDatetime.getInstance();

		List<Map<String, Object>> listMap = new ArrayList<>();

		for (Empj_ProjectInfo project : empj_ProjectInfoList)
		{
			Map<String, Object> map = new HashMap<>();
			
			//查询预售证判断项目是否预售
			Empj_PresellDocumentInfoForm presellDocumentInfoForm = new Empj_PresellDocumentInfoForm();
			presellDocumentInfoForm.setTheState(S_TheState.Normal);
			presellDocumentInfoForm.setProject(project);
			
			int count = empj_PresellDocumentInfoDao.findByPage_Size(empj_PresellDocumentInfoDao.getQuery_Size(empj_PresellDocumentInfoDao.getBasicHQL(), presellDocumentInfoForm));
			
			if(count > 0){
				// 查询托管合作协议获取最新的合同备案日期
				Tgxy_EscrowAgreementForm form = new Tgxy_EscrowAgreementForm();
				form.setTheState(S_TheState.Normal);
				form.setDevelopCompany(project.getDevelopCompany());
				form.setProject(project);

				List<Tgxy_EscrowAgreement> escrowAgreementList = tgxy_EscrowAgreementDao
						.findByPage(tgxy_EscrowAgreementDao.getQuery(tgxy_EscrowAgreementDao.getRecordTime2(), form));

				if (null != escrowAgreementList && escrowAgreementList.size() > 0)
				{
					Long recordTimeStamp = escrowAgreementList.get(0).getRecordTimeStamp();
					Long startTimeStamp = escrowAgreementList.get(escrowAgreementList.size()-1).getRecordTimeStamp();

					String simpleString = "";
					String startString = "";

					if (null != recordTimeStamp && recordTimeStamp > 0)
					{
						simpleString = myTime.dateToSimpleString(recordTimeStamp);
						
						startString = myTime.dateToSimpleString(startTimeStamp);
						
						Empj_BuildingInfo empj_BuildingInfo = escrowAgreementList.get(0).getBuildingInfoList().get(0);
						String buildingEscrowAgreement = "";
						if(null != empj_BuildingInfo)
						{
//							String[] split = simpleString.split("-");
//							buildingEscrowAgreement = split[0]+"年"+split[1]+"月"+split[2]+"日 "+empj_BuildingInfo.geteCodeFromConstruction()+"（施工编号）签约完成";
							buildingEscrowAgreement = empj_BuildingInfo.geteCodeFromConstruction()+" 签约完成";
						}
						map.put("buildingInfo", buildingEscrowAgreement);
						
						map.put("tableId", project.getTableId());
						map.put("depositTime", simpleString);
						map.put("projectName", project.getTheName());

						// 项目地址
						map.put("address", project.getAddress());

						map.put("longitude", project.getLongitude());// 经度

						map.put("latitude", project.getLatitude());// 纬度

						listMap.add(map);
					}
				}
			}
		}

		Collections.sort(listMap, new Comparator<Map<String, Object>>()
		{
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2)
			{
				Long longTime1 = myTime.stringToLong((String) o1.get("depositTime"));

				Long longTime2 = myTime.stringToLong((String) o2.get("depositTime"));

				long i = longTime1 - longTime2;

				if (i == 0)
				{
					return 0;
				}
				else if (i > 0)
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
		});
		return listMap;
	}
}
