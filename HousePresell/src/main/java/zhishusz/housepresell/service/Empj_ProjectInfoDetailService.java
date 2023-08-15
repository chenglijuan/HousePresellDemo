package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.ApprovalDetailEchoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_ProjectInfoDetailService
{
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
//	@Autowired
//	private Sm_ApprovalProcess_AFDao sm_approvalProcess_afDao;
	@Autowired
	private ApprovalDetailEchoUtil<Empj_ProjectInfo,Empj_ProjectInfoForm> approvalDetailEchoUtil;
	@Autowired
	private Sm_BaseParameterGetService baseParameterGetService;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_cityRegionInfoDao;
	@Autowired
	private Sm_StreetInfoDao sm_streetInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;

	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_ProjectInfoId = model.getTableId();
		Empj_ProjectInfo empj_ProjectInfo = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(empj_ProjectInfoId);
		// || S_TheState.Deleted.equals(empj_ProjectInfo.getTheState())
		if(empj_ProjectInfo == null)
		{
			return MyBackInfo.fail(properties, "项目信息不存在");
		}

		if(empj_ProjectInfo.getBusiState().equals(S_BusiState.NoRecord)){
			properties.put("busiCode", S_BusiCode.busiCode_03010101);
		}else{
			properties.put("busiCode", S_BusiCode.busiCode_03010102);
		}

		approvalDetailEchoUtil.getEcho(empj_ProjectInfo, model, properties, "empj_ProjectInfo", "empj_ProjectInfoNew"
				, new ApprovalDetailEchoUtil.OnSetChangeMap<Empj_ProjectInfo, Empj_ProjectInfoForm>() {
			@Override
			public void onSetOrgMap(Empj_ProjectInfo copyPo, Empj_ProjectInfoForm jsonFromOssForm) {
				copyPo.setTheName(jsonFromOssForm.getTheName());
				copyPo.setAddress(jsonFromOssForm.getAddress());
				copyPo.setLongitude(jsonFromOssForm.getLongitude());
				copyPo.setLatitude(jsonFromOssForm.getLatitude());
				copyPo.setProjectLeader(jsonFromOssForm.getProjectLeader());
				copyPo.setLeaderPhone(jsonFromOssForm.getLeaderPhone());
				copyPo.setIntroduction(jsonFromOssForm.getIntroduction());
				copyPo.setRemark(jsonFromOssForm.getRemark());
				Long cityRegionId = jsonFromOssForm.getCityRegionId();
				Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo)sm_cityRegionInfoDao.findById(cityRegionId);
				if (cityRegion != null)
				{
					copyPo.setCityRegion(cityRegion);
				}
				Long streetId = jsonFromOssForm.getStreetId();
				Sm_StreetInfo street = (Sm_StreetInfo)sm_streetInfoDao.findById(streetId);
				if (street != null)
				{
					copyPo.setStreet(street);
				}
			}

			@Override
			public void onSetNewMap(Empj_ProjectInfoForm newValueForm) {
				newValueForm.setTheName(empj_ProjectInfo.getTheName());
				newValueForm.setAddress(empj_ProjectInfo.getAddress());
				newValueForm.setLongitude(empj_ProjectInfo.getLongitude());
				newValueForm.setLatitude(empj_ProjectInfo.getLatitude());
				newValueForm.setProjectLeader(empj_ProjectInfo.getProjectLeader());
				newValueForm.setLeaderPhone(empj_ProjectInfo.getLeaderPhone());
				newValueForm.setIntroduction(empj_ProjectInfo.getIntroduction());
				newValueForm.setRemark(empj_ProjectInfo.getRemark());
				if(empj_ProjectInfo.getCityRegion() != null)
				{
					newValueForm.setCityRegionId(empj_ProjectInfo.getCityRegion().getTableId());
				}
				if(empj_ProjectInfo.getStreet() != null)
				{
					newValueForm.setStreetId(empj_ProjectInfo.getStreet().getTableId());
				}
				newValueForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
			}
		});
		HashMap<String,Object> empj_ProjectInfoNew = (HashMap<String,Object>)properties.get("empj_ProjectInfoNew");
		if (empj_ProjectInfoNew != null)
		{
			Sm_CityRegionInfo cityRegion =
					(Sm_CityRegionInfo)sm_cityRegionInfoDao.findById((Long) empj_ProjectInfoNew.get(
					"cityRegionId"));
			if (cityRegion != null)
			{
				empj_ProjectInfoNew.put("cityRegionName", cityRegion.getTheName());
			}
			Sm_StreetInfo street = (Sm_StreetInfo)sm_streetInfoDao.findById((Long) empj_ProjectInfoNew.get(
					"streetId"));
			if (street != null)
			{
				empj_ProjectInfoNew.put("streetName", street.getTheName());
			}
		}

		Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
		buildingInfoForm.setInterfaceVersion(model.getInterfaceVersion());
		buildingInfoForm.setTheState(S_TheState.Normal);
		buildingInfoForm.setProjectId(empj_ProjectInfoId);

		List<Empj_BuildingInfo> empj_BuildingInfoList =
				empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(),
						buildingInfoForm), null, null);
		List<Long> idArray = new ArrayList<Long>();
		if (empj_BuildingInfoList != null && empj_BuildingInfoList.size() > 0)
		{
			for (Empj_BuildingInfo itemBuildingInfo : empj_BuildingInfoList)
			{
				idArray.add(itemBuildingInfo.getTableId());
			}
			Long[] idArr = new Long[idArray.size()];
			idArray.toArray(idArr);
		}

		properties.put("allBuildingIdArr", idArray);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//		properties.put("empj_ProjectInfo", empj_ProjectInfo);


		return properties;
	}
}
