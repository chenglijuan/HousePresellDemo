package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service添加操作：项目信息
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjectInfoAddService
{
	private static final String BUSI_CODE = "03010101";// 具体业务编码参看SVN文
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	// @Autowired
	// private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		// String busiState = S_BusiState.NoRecord; //model.getBusiState() 未备案
		// String approvalState = S_ApprovalState.WaitSubmit; //待提交
		String eCode = sm_BusinessCodeGetService.execute(BUSI_CODE); // 自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
		Long createTimeStamp = System.currentTimeMillis();
		Long developCompanyId = model.getDevelopCompanyId();
		String theType = model.getTheType();
		String zoneCode = model.getZoneCode();
		Long cityRegionId = model.getCityRegionId();
		Long streetId = model.getStreetId();
		String address = model.getAddress();
		String doorNumber = model.getDoorNumber();
		String doorNumberAnnex = model.getDoorNumberAnnex();
		String introduction = model.getIntroduction();
		Double longitude = model.getLongitude();
		Double latitude = model.getLatitude();
		String propertyType = model.getPropertyType();
		String theName = model.getTheName();
		String legalName = model.getLegalName();
		String buildYear = model.getBuildYear();
		Boolean isPartition = model.getIsPartition();
		Integer theProperty = model.getTheProperty();
		String projectLeader = model.getProjectLeader();
		String leaderPhone = model.getLeaderPhone();
		Double landArea = model.getLandArea();
		Integer obtainMethod = model.getObtainMethod();
		Double investment = model.getInvestment();
		Double landInvest = model.getLandInvest();
		Double coverArea = model.getCoverArea();
		Double houseArea = model.getHouseArea();
		Double siteArea = model.getSiteArea();
		Double planArea = model.getPlanArea();
		Double agArea = model.getAgArea();
		Double ugArea = model.getUgArea();
		Double greenRatio = model.getGreenRatio();
		Double capacity = model.getCapacity();
		Double parkRatio = model.getParkRatio();
		Integer unitCount = model.getUnitCount();
		Integer buildingCount = model.getBuildingCount();
		Long payDate = model.getPayDate();
		String planStartDate = model.getPlanStartDate();
		String planEndDate = model.getPlanEndDate();
		String developDate = model.getDevelopDate();
		// Long designCompanyId = model.getDesignCompanyId();
		String remark = model.getRemark();
		Integer developProgress = model.getDevelopProgress();
		String eastAddress = model.getEastAddress();
		Double eastLongitude = model.getEastLongitude();
		Double eastLatitude = model.getEastLatitude();
		String westAddress = model.getWestAddress();
		Double westLongitude = model.getWestLongitude();
		Double westLatitude = model.getWestLatitude();
		String southAddress = model.getSouthAddress();
		Double southLongitude = model.getSouthLongitude();
		Double southLatitude = model.getSouthLatitude();
		String northAddress = model.getNorthAddress();
		Double northLongitude = model.getNorthLongitude();
		Double northLatitude = model.getNorthLatitude();

		if (developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if (theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目名称不能为空");
		}
		if (null == longitude || null == latitude)
		{
			return MyBackInfo.fail(properties, "请输入经纬度");
		}
		// if(theType == null || theType.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'theType'不能为空");
		// }
		// if(zoneCode == null || zoneCode.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'zoneCode'不能为空");
		// }
		if (cityRegionId == null || cityRegionId < 1)
		{
			return MyBackInfo.fail(properties, "所属区域不能为空");
		}
		if (streetId == null || streetId < 1)
		{
			return MyBackInfo.fail(properties, "街道不能为空");
		}
		if (address == null || address.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目地址不能为空");
		}
		// if(doorNumber == null || doorNumber.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'doorNumber'不能为空");
		// }
		// if(doorNumberAnnex == null || doorNumberAnnex.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'doorNumberAnnex'不能为空");
		// }
		// if(propertyType == null || propertyType.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'propertyType'不能为空");
		// }
		// if(legalName == null || legalName.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'legalName'不能为空");
		// }
		// if(buildYear == null || buildYear.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'buildYear'不能为空");
		// }
		// if(isPartition == null)
		// {
		// return MyBackInfo.fail(properties, "'isPartition'不能为空");
		// }
		// if(theProperty == null || theProperty < 1)
		// {
		// return MyBackInfo.fail(properties, "'theProperty'不能为空");
		// }
		// if(longitude == null || longitude < 1)
		// {
		// return MyBackInfo.fail(properties, "经度不能为空");
		// }
		// if(latitude == null || latitude < 1)
		// {
		// return MyBackInfo.fail(properties, "纬度不能为空");
		// }
		if (projectLeader == null || projectLeader.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目负责人不能为空");
		}
		if (leaderPhone == null || leaderPhone.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目负责人联系电话不能为空");
		}
		if (leaderPhone != null && !MyString.getInstance().checkPhoneNumber(leaderPhone))
		{
			return MyBackInfo.fail(properties, "项目负责人联系电话格式不正确");
		}
		if (introduction == null || introduction.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目简介不能为空");
		}
		if (introduction != null && introduction.length() > 200)
		{
			return MyBackInfo.fail(properties, "项目简介长度不能超过200字");
		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "项目备注长度不能超过200字");
		}
		// if(landArea == null || landArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'landArea'不能为空");
		// }
		// if(obtainMethod == null || obtainMethod < 1)
		// {
		// return MyBackInfo.fail(properties, "'obtainMethod'不能为空");
		// }
		// if(investment == null || investment < 1)
		// {
		// return MyBackInfo.fail(properties, "'investment'不能为空");
		// }
		// if(landInvest == null || landInvest < 1)
		// {
		// return MyBackInfo.fail(properties, "'landInvest'不能为空");
		// }
		// if(coverArea == null || coverArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'coverArea'不能为空");
		// }
		// if(houseArea == null || houseArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'houseArea'不能为空");
		// }
		// if(siteArea == null || siteArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'siteArea'不能为空");
		// }
		// if(planArea == null || planArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'planArea'不能为空");
		// }
		// if(agArea == null || agArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'agArea'不能为空");
		// }
		// if(ugArea == null || ugArea < 1)
		// {
		// return MyBackInfo.fail(properties, "'ugArea'不能为空");
		// }
		// if(greenRatio == null || greenRatio < 1)
		// {
		// return MyBackInfo.fail(properties, "'greenRatio'不能为空");
		// }
		// if(capacity == null || capacity < 1)
		// {
		// return MyBackInfo.fail(properties, "'capacity'不能为空");
		// }
		// if(parkRatio == null || parkRatio < 1)
		// {
		// return MyBackInfo.fail(properties, "'parkRatio'不能为空");
		// }
		// if(unitCount == null || unitCount < 1)
		// {
		// return MyBackInfo.fail(properties, "'unitCount'不能为空");
		// }
		// if(buildingCount == null || buildingCount < 1)
		// {
		// return MyBackInfo.fail(properties, "'buildingCount'不能为空");
		// }
		// if(payDate == null || payDate < 1)
		// {
		// return MyBackInfo.fail(properties, "'payDate'不能为空");
		// }
		// if(planStartDate == null || planStartDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'planStartDate'不能为空");
		// }
		// if(planEndDate == null || planEndDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'planEndDate'不能为空");
		// }
		// if(developDate == null || developDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'developDate'不能为空");
		// }
		// if(designCompanyId == null || designCompanyId < 1)
		// {
		// return MyBackInfo.fail(properties, "'designCompany'不能为空");
		// }
		// if(eCodeOfDesignCompany == null || eCodeOfDesignCompany.length() ==
		// 0)
		// {
		// return MyBackInfo.fail(properties, "'eCodeOfDesignCompany'不能为空");
		// }
		// if(developProgress == null || developProgress < 1)
		// {
		// return MyBackInfo.fail(properties, "'developProgress'不能为空");
		// }
		// if(eastAddress == null || eastAddress.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'eastAddress'不能为空");
		// }
		// if(eastLongitude == null || eastLongitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'eastLongitude'不能为空");
		// }
		// if(eastLatitude == null || eastLatitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'eastLatitude'不能为空");
		// }
		// if(westAddress == null || westAddress.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'westAddress'不能为空");
		// }
		// if(westLongitude == null || westLongitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'westLongitude'不能为空");
		// }
		// if(westLatitude == null || westLatitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'westLatitude'不能为空");
		// }
		// if(southAddress == null || southAddress.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'southAddress'不能为空");
		// }
		// if(southLongitude == null || southLongitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'southLongitude'不能为空");
		// }
		// if(southLatitude == null || southLatitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'southLatitude'不能为空");
		// }
		// if(northAddress == null || northAddress.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'northAddress'不能为空");
		// }
		// if(northLongitude == null || northLongitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'northLongitude'不能为空");
		// }
		// if(northLatitude == null || northLatitude < 1)
		// {
		// return MyBackInfo.fail(properties, "'northLatitude'不能为空");
		// }

		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if (!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

		Sm_User userStart = (Sm_User) model.getUser();
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在，请先登录");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo) emmp_CompanyInfoDao.findById(developCompanyId);
		if (developCompany == null)
		{
			return MyBackInfo.fail(properties, "开发企业不存在");
		}
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo) sm_CityRegionInfoDao.findById(cityRegionId);
		if (cityRegion == null)
		{
			return MyBackInfo.fail(properties, "区域不存在");
		}
		Sm_StreetInfo street = (Sm_StreetInfo) sm_StreetInfoDao.findById(streetId);
		if (street == null)
		{
			return MyBackInfo.fail(properties, "街道不存在");
		}

		Integer totalCount = empj_ProjectInfoDao.findByPage_Size(
				empj_ProjectInfoDao.getQuery_Size(empj_ProjectInfoDao.getSameProjectNameListHQL(), model));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "项目名称重复");
		}

		// Emmp_CompanyInfo designCompany =
		// (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(designCompanyId);
		// if(designCompany == null)
		// {
		// return MyBackInfo.fail(properties, "'designCompany'不存在");
		// }

		Empj_ProjectInfo empj_ProjectInfo = new Empj_ProjectInfo();
		empj_ProjectInfo.setTheState(theState);
		empj_ProjectInfo.seteCode(eCode);
		empj_ProjectInfo.setUserStart(userStart);
		empj_ProjectInfo.setCreateTimeStamp(createTimeStamp);
		empj_ProjectInfo.setDevelopCompany(developCompany);
		empj_ProjectInfo.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_ProjectInfo.setTheType(theType);
		empj_ProjectInfo.setZoneCode(zoneCode);
		empj_ProjectInfo.setCityRegion(cityRegion);
		empj_ProjectInfo.setStreet(street);
		empj_ProjectInfo.setAddress(address);
		empj_ProjectInfo.setDoorNumber(doorNumber);
		empj_ProjectInfo.setDoorNumberAnnex(doorNumberAnnex);
		empj_ProjectInfo.setIntroduction(introduction);
		empj_ProjectInfo.setLongitude(longitude);
		empj_ProjectInfo.setLatitude(latitude);
		empj_ProjectInfo.setPropertyType(propertyType);
		empj_ProjectInfo.setTheName(theName);
		empj_ProjectInfo.setLegalName(legalName);
		empj_ProjectInfo.setBuildYear(buildYear);
		empj_ProjectInfo.setIsPartition(isPartition);
		empj_ProjectInfo.setTheProperty(theProperty);
		empj_ProjectInfo.setProjectLeader(projectLeader);
		empj_ProjectInfo.setLeaderPhone(leaderPhone);
		empj_ProjectInfo.setLandArea(landArea);
		empj_ProjectInfo.setObtainMethod(obtainMethod);
		empj_ProjectInfo.setInvestment(investment);
		empj_ProjectInfo.setLandInvest(landInvest);
		empj_ProjectInfo.setCoverArea(coverArea);
		empj_ProjectInfo.setHouseArea(houseArea);
		empj_ProjectInfo.setSiteArea(siteArea);
		empj_ProjectInfo.setPlanArea(planArea);
		empj_ProjectInfo.setAgArea(agArea);
		empj_ProjectInfo.setUgArea(ugArea);
		empj_ProjectInfo.setGreenRatio(greenRatio);
		empj_ProjectInfo.setCapacity(capacity);
		empj_ProjectInfo.setParkRatio(parkRatio);
		empj_ProjectInfo.setUnitCount(unitCount);
		empj_ProjectInfo.setBuildingCount(buildingCount);
		empj_ProjectInfo.setPayDate(payDate);
		empj_ProjectInfo.setPlanStartDate(planStartDate);
		empj_ProjectInfo.setPlanEndDate(planEndDate);
		empj_ProjectInfo.setDevelopDate(developDate);
		// empj_ProjectInfo.setDesignCompany(designCompany);
		// empj_ProjectInfo.seteCodeOfDesignCompany(eCodeOfDesignCompany);
		empj_ProjectInfo.setRemark(remark);
		empj_ProjectInfo.setDevelopProgress(developProgress);
		empj_ProjectInfo.setEastAddress(eastAddress);
		empj_ProjectInfo.setEastLongitude(eastLongitude);
		empj_ProjectInfo.setEastLatitude(eastLatitude);
		empj_ProjectInfo.setWestAddress(westAddress);
		empj_ProjectInfo.setWestLongitude(westLongitude);
		empj_ProjectInfo.setWestLatitude(westLatitude);
		empj_ProjectInfo.setSouthAddress(southAddress);
		empj_ProjectInfo.setSouthLongitude(southLongitude);
		empj_ProjectInfo.setSouthLatitude(southLatitude);
		empj_ProjectInfo.setNorthAddress(northAddress);
		empj_ProjectInfo.setNorthLongitude(northLongitude);
		empj_ProjectInfo.setNorthLatitude(northLatitude);

		// 审批流
		properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
		// 没有配置审批流程无需走审批流直接保存数据库
		if ("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
		{
			empj_ProjectInfo.setUserRecord(userStart);
			empj_ProjectInfo.setRecordTimeStamp(createTimeStamp); // 已备案的添加备案人、备案日期
			empj_ProjectInfo.setBusiState(S_BusiState.HaveRecord); // 已备案
			empj_ProjectInfo.setApprovalState(S_ApprovalState.Completed); // 已完结
			empj_ProjectInfoDao.save(empj_ProjectInfo);

			sm_AttachmentBatchAddService.execute(model, empj_ProjectInfo.getTableId());
		}
		else
		{
			// 判断是否满足审批条件（有审批角色，单审批流程）
			if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
			{
				return properties;
			}

			empj_ProjectInfo.setBusiState(S_BusiState.NoRecord); // 未备案
			empj_ProjectInfoDao.save(empj_ProjectInfo);

			sm_AttachmentBatchAddService.execute(model, empj_ProjectInfo.getTableId());

			// 审批操作
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");
			sm_approvalProcessService.execute(empj_ProjectInfo, model, sm_approvalProcess_cfg);
		}

		// 范围授权
		// Sm_Permission_RangeAuthorizationForm
		// sm_Permission_RangeAuthorizationForm = new
		// Sm_Permission_RangeAuthorizationForm();
		//
		// sm_Permission_RangeAuthorizationForm.setSponsorId(model.getUserId());
		// sm_Permission_RangeAuthorizationForm.setProjectInfoId(empj_ProjectInfo.getTableId());
		//
		// properties =
		// s_P_RAAOUFProjectService.execute(sm_Permission_RangeAuthorizationForm);

		properties.put("tableId", empj_ProjectInfo.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
