package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_BaseParameter;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.ApprovalDetailEchoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfoDetailService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private ApprovalDetailEchoUtil<Empj_BuildingInfo,Empj_BuildingInfoForm> approvalDetailEchoUtil;
	@Autowired
	private Sm_BaseParameterGetService baseParameterGetService;

	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		Long empj_BuildingInfoId = model.getTableId();
		Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(empj_BuildingInfoId);
		if(empj_BuildingInfo == null)
		{
			return MyBackInfo.fail(properties, "'Empj_BuildingInfo(Id:" + empj_BuildingInfoId + ")'不存在");
		}
		if(empj_BuildingInfo.getBusiState().equals(S_BusiState.NoRecord)){//新增
			properties.put("busiCode", S_BusiCode.busiCode_03010201);
		}else{//新增
			properties.put("busiCode", S_BusiCode.busiCode_03010202);
		}
		
		approvalDetailEchoUtil.getEcho(empj_BuildingInfo, model, properties, "empj_BuildingInfo", "empj_BuildingInfoNew", new ApprovalDetailEchoUtil.OnSetChangeMap<Empj_BuildingInfo, Empj_BuildingInfoForm>() {
			@Override
			public void onSetOrgMap(Empj_BuildingInfo copyPo, Empj_BuildingInfoForm jsonFromOssForm) {
				copyPo.setEscrowArea(jsonFromOssForm.getEscrowArea());
				copyPo.setUpfloorNumber(jsonFromOssForm.getUpfloorNumber());
				copyPo.setDownfloorNumber(jsonFromOssForm.getDownfloorNumber());
				copyPo.setBuildingArea(jsonFromOssForm.getBuildingArea());
				copyPo.setDeliveryType(jsonFromOssForm.getDeliveryType());
				copyPo.setRemark(jsonFromOssForm.getRemark());
				copyPo.getExtendInfo().setLandMortgagor(jsonFromOssForm.getLandMortgagor());
				copyPo.getExtendInfo().setLandMortgageAmount(jsonFromOssForm.getLandMortgageAmount());
				copyPo.getExtendInfo().setLandMortgageState(jsonFromOssForm.getLandMortgageState());
			}

			@Override
			public void onSetNewMap(Empj_BuildingInfoForm newValueForm) {
				newValueForm.setEscrowArea(empj_BuildingInfo.getEscrowArea());
				newValueForm.setUpfloorNumber(empj_BuildingInfo.getUpfloorNumber());
				newValueForm.setDownfloorNumber(empj_BuildingInfo.getDownfloorNumber());
				newValueForm.setBuildingArea(empj_BuildingInfo.getBuildingArea());
				newValueForm.setDeliveryType(empj_BuildingInfo.getDeliveryType());
				newValueForm.setRemark(empj_BuildingInfo.getRemark());
				newValueForm.setLandMortgagor(empj_BuildingInfo.getExtendInfo().getLandMortgagor());
				newValueForm.setLandMortgageAmount(empj_BuildingInfo.getExtendInfo().getLandMortgageAmount());
				newValueForm.setLandMortgageState(empj_BuildingInfo.getExtendInfo().getLandMortgageState());
				newValueForm.setGeneralAttachmentList(model.getGeneralAttachmentList());
			}
		});
		HashMap<String,Object> empj_buildingInfoNew = (HashMap<String, Object>) properties.get("empj_BuildingInfoNew");
		if(empj_buildingInfoNew!=null){
			String deliveryType = (String) empj_buildingInfoNew.get("deliveryType");
			Sm_BaseParameter parameter = baseParameterGetService.getParameter(S_BaseParameter.HouseType, deliveryType + "");
			if(parameter != null)
			{
				empj_buildingInfoNew.put("parameterName", parameter.getTheName());
			}
		}
		
		Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
		
		if(null == properties.get("empj_BuildingInfo"))
		{
			properties.put("empj_BuildingInfo", empj_BuildingInfo);
		}
		
		properties.put("tgpj_BuildingAccount", buildingAccount);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//		properties.put("sm_BaseParameter", sm_BaseParameter);
//		properties.put("empj_BuildingInfo", empj_BuildingInfo);

		return properties;
	}
}
