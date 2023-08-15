package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.ApprovalDetailEchoUtil;
import zhishusz.housepresell.util.project.HouseInfoUtil;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service详情：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceDetailService
{
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	@Autowired
	private Gson gson;
	@Autowired
	private Sm_ApprovalProcess_WorkflowDao sm_approvalProcess_workflowDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private HouseInfoUtil houseInfoUtil;
	@Autowired
	private ApprovalDetailEchoUtil<Tgpj_BuildingAvgPrice,Tgpj_BuildingAvgPriceForm> approvalDetailEchoUtil;

	public Properties execute(Tgpj_BuildingAvgPriceForm model)
	{
		Properties properties = new MyProperties();

        Long tgpj_BuildingAvgPriceId = model.getTableId();
        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = (Tgpj_BuildingAvgPrice) tgpj_BuildingAvgPriceDao.findById(tgpj_BuildingAvgPriceId);
        if (tgpj_BuildingAvgPrice == null || S_TheState.Deleted.equals(tgpj_BuildingAvgPrice.getTheState())) {
            return MyBackInfo.fail(properties, "'Tgpj_BuildingAvgPrice(Id:" + tgpj_BuildingAvgPriceId + ")'不存在");
        }
		Sm_User user = model.getUser();
		if (user == null) {
			return  MyBackInfo.fail(properties, "获取备案均价详情请先登录");
		}
		Integer theType = user.getTheType();
		if (theType != null) {
			if(theType.equals(S_UserType.ZhengtaiUser)){//如果是正泰用户才需要请求预售系统备案价格
				MsgInfo msgInfo = houseInfoUtil.calculatePresellSystemPrice(tgpj_BuildingAvgPrice.getBuildingInfo().getTableId());
				if (!msgInfo.isSuccess()) {
				}
				try {
					tgpj_BuildingAvgPrice.setRecordAveragePriceFromPresellSystem(Double.parseDouble((String) msgInfo.getExtra()));
				} catch (Exception e) {
//					tgpj_BuildingAvgPrice.setRecordAveragePriceFromPresellSystem(null);
					e.printStackTrace();
				}
			}else{//非正泰用户

			}
		}

//        MyProperties newProperties = new MyProperties();
		if(tgpj_BuildingAvgPrice.getBusiState().equals(S_BusiState.NoRecord)){
			properties.put("busiCode", S_BusiCode.busiCode_03010301);
		}else{
			properties.put("busiCode", S_BusiCode.busiCode_03010302);
		}
        approvalDetailEchoUtil.getEcho(tgpj_BuildingAvgPrice, model, properties, "tgpj_BuildingAvgPrice", "tgpj_BuildingAvgPriceNew", new ApprovalDetailEchoUtil.OnSetChangeMap<Tgpj_BuildingAvgPrice, Tgpj_BuildingAvgPriceForm>() {
			@Override
			public void onSetOrgMap(Tgpj_BuildingAvgPrice copyPo, Tgpj_BuildingAvgPriceForm jsonFromOssForm) {
				copyPo.setRecordAveragePrice(jsonFromOssForm.getRecordAveragePrice());
				copyPo.setRemark(jsonFromOssForm.getRemark());
			}

			@Override
			public void onSetNewMap(Tgpj_BuildingAvgPriceForm newValueForm) {
				newValueForm.setRecordAveragePrice(tgpj_BuildingAvgPrice.getRecordAveragePrice());
				newValueForm.setRemark(tgpj_BuildingAvgPrice.getRemark());
			}
		});
//        approvalDetailEchoUtil.getEcho(tgpj_BuildingAvgPrice, model, new ApprovalDetailEchoUtil.OnSetGoalObj<Tgpj_BuildingAvgPrice>() {
//            @Override
//            public void setOrgMap(Tgpj_BuildingAvgPrice orgPo) {
//                if(orgPo==null){
//                    properties.put("tgpj_BuildingAvgPrice", tgpj_BuildingAvgPrice);
//                }else{
//                    Tgpj_BuildingAvgPrice copy = ObjectCopier.copy(tgpj_BuildingAvgPrice);
//                    copy.setRecordAveragePrice(orgPo.getRecordAveragePrice());
//                    copy.setRemark(orgPo.getRemark());
////                    detail.put("recordAveragePrice",orgPo.getRecordAveragePrice());
////                    detail.put("remark",orgPo.getRemark());
//                    properties.put("tgpj_BuildingAvgPrice", copy);
//                }
//            }
//
//            @Override
//            public void setExpectMap(Tgpj_BuildingAvgPrice expectPo) {
////                Properties detail = rebuild.getDetail(tgpj_BuildingAvgPrice);
////                detail.put("recordAveragePrice",expectPo.getRecordAveragePrice());
////                detail.put("remark",expectPo.getRemark());
//                properties.put("tgpj_BuildingAvgPriceNew", expectPo);
//            }
//        });
//            @Override
//            public void setOrgMap(String orgJson) {
//                if (orgJson == null) {
//                    properties.put("tgpj_BuildingAvgPrice", tgpj_BuildingAvgPrice);
//                } else {
//                    properties.put("tgpj_BuildingAvgPrice", gson.fromJson(orgJson, Tgpj_BuildingAvgPrice.class));
//                }
//            }
//
//            @Override
//            public void setExpectMap(String expectJson) {
//                properties.put("tgpj_BuildingAvgPriceNew", gson.fromJson(expectJson, Tgpj_BuildingAvgPrice.class));
//            }



//		if(!"详情".equals(model.getReqSource()))
//		{
//			boolean isNewObj=false;
//			String busiState = tgpj_BuildingAvgPrice.getBusiState();
//			if(busiState.equals(S_BusiState.NoRecord)){
////				isNewObj=true;
//				newProperties.put("recordAveragePrice", tgpj_BuildingAvgPrice.getRecordAveragePrice());
//				newProperties.put("remark", tgpj_BuildingAvgPrice.getRemark());
//			}else if(busiState.equals(S_BusiState.HaveRecord)){
//////				isNewObj=false;
//			}
//			//查待提交的申请单
//			Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
//			sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
//			sm_ApprovalProcess_AFForm.setBusiState("待提交");
//			sm_ApprovalProcess_AFForm.setSourceId(tgpj_BuildingAvgPrice.getTableId());
//			if(S_BusiState.HaveRecord.equals(tgpj_BuildingAvgPrice.getBusiState())){
//				sm_ApprovalProcess_AFForm.setBusiCode("03010302");
//			}else{
//				sm_ApprovalProcess_AFForm.setBusiCode("03010301");
//			}
//			Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//
//			boolean isHasApproveProcess=true;
//
//			if(sm_ApprovalProcess_AF == null)
//			{
//				sm_ApprovalProcess_AFForm.setBusiState("审核中");
//				sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));
//
//				if(sm_ApprovalProcess_AF == null)
//				{
//					isHasApproveProcess=false;
////					properties.setProperty(S_NormalFlag.result, S_NormalFlag.fail);
////					properties.setProperty(S_NormalFlag.info, "审批流程不存在！");
////					return properties;
//				}
//			}
//			if (isHasApproveProcess)
//			{
//				Long currentNode = sm_ApprovalProcess_AF.getCurrentIndex();
//
//				Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = sm_approvalProcess_workflowDao
//						.findById(currentNode);
//				Boolean isNeedBackup = null;
//				if (sm_approvalProcess_workflow.getNextWorkFlow() == null)
//				{
//					if (S_IsNeedBackup.Yes.equals(sm_ApprovalProcess_AF.getIsNeedBackup()))
//					{
//						isNeedBackup = true;
//					}
//				}
//				else
//				{
//					isNeedBackup = false;
//				}
//
//				properties.put("isNeedBackup", isNeedBackup);//是否显示备案按钮
//
//				String jsonNewStr = HttpUtil.get(sm_ApprovalProcess_AF.getExpectObjJsonFilePath());
//				System.out.println(jsonNewStr);
////				String jsonNewStr = sm_ApprovalProcess_AF.getExpectObjJson();
//				if (jsonNewStr != null && jsonNewStr.length() > 0)
//				{
//
//						Tgpj_BuildingAvgPriceForm form = gson.fromJson(jsonNewStr, Tgpj_BuildingAvgPriceForm.class);
//						Double recordAveragePrice = form.getRecordAveragePrice();
//						String remark = form.getRemark();
//						newProperties.put("recordAveragePrice", recordAveragePrice);
//						newProperties.put("remark", remark);
//
//
//				}
//			}
//
//		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//		properties.put("tgpj_BuildingAvgPrice", tgpj_BuildingAvgPrice);
//		properties.put("tgpj_BuildingAvgPriceNew", newProperties);

		return properties;
	}
}
