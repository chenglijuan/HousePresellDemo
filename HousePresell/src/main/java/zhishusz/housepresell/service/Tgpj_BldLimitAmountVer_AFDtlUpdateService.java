package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/*
 * Service更新操作：受限额度设置
 * Company：ZhiShuSZ
 * */
@Service @Transactional public class Tgpj_BldLimitAmountVer_AFDtlUpdateService
{
	@Autowired private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;
	@Autowired private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;


	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
	public Properties execute(Tgpj_BldLimitAmountVer_AFDtlForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		Long bldLimitAmountVerMngId = model.getBldLimitAmountVerMngId();
		String stageName = model.getStageName();
		Double limitedAmount = model.getLimitedAmount();
//		String nodeTypeName = model.getNodeTypeName();
		String remark = model.getRemark();
		//		if(theState == null || theState < 1)
		//		{
		//			return MyBackInfo.fail(properties, "'theState'不能为空");
		//		}
		if (bldLimitAmountVerMngId == null || bldLimitAmountVerMngId < 1)
		{
			return MyBackInfo.fail(properties, "'bldLimitAmountVerMng'不能为空");
		}
		//		if(stageName == null || stageName.length() == 0)
		//		{
		//			return MyBackInfo.fail(properties, "'stageName'不能为空");
		//		}
		//		if(limitedAmount == null || limitedAmount < 1)
		//		{
		//			return MyBackInfo.fail(properties, "'limitedAmount'不能为空");
		//		}
		if(stageName==null || stageName.length()==0){
			return MyBackInfo.fail(properties, "受限额度节点名称不能为空");
		}
		if(limitedAmount == null || limitedAmount < 1)
		{
			return MyBackInfo.fail(properties, "受限比例不能为空");
		}
		Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng = (Tgpj_BldLimitAmountVer_AF) tgpj_BldLimitAmountVer_AFDao
				.findById(bldLimitAmountVerMngId);
		if (bldLimitAmountVerMng == null)
		{
			return MyBackInfo.fail(properties, "'bldLimitAmountVerMng(Id:" + bldLimitAmountVerMngId + ")'不存在");
		}
		String theType = bldLimitAmountVerMng.getTheType();
//		if (theType != null)
//		{
//			if (theType.equals("0"))
//			{//毛坯房
//				HashMap<Integer, Tg_HouseStage> buildingStage = S_HouseStage.BUILDING_STAGE;
//				Tg_HouseStage tg_houseStage = buildingStage.get(Integer.parseInt(nodeTypeName));
//				stageName = tg_houseStage.getStageName();
//				limitedAmount = tg_houseStage.getPercent();
//			}
//			else
//			{//成品房
//				HashMap<Integer, Tg_HouseStage> completeStage = S_HouseStage.COMPLETE_STAGE;
//				Tg_HouseStage tg_houseStage = completeStage.get(Integer.parseInt(nodeTypeName));
//				stageName = tg_houseStage.getStageName();
//				limitedAmount = tg_houseStage.getPercent();
//			}
//		}

		Long tgpj_BldLimitAmountVer_AFDtlId = model.getTableId();
		Tgpj_BldLimitAmountVer_AFDtl tgpj_BldLimitAmountVer_AFDtl = (Tgpj_BldLimitAmountVer_AFDtl) tgpj_BldLimitAmountVer_AFDtlDao
				.findById(tgpj_BldLimitAmountVer_AFDtlId);
		if (tgpj_BldLimitAmountVer_AFDtl == null)
		{
			return MyBackInfo
					.fail(properties, "'Tgpj_BldLimitAmountVer_AFDtl(Id:" + tgpj_BldLimitAmountVer_AFDtlId + ")'不存在");
		}

		tgpj_BldLimitAmountVer_AFDtl.setTheState(theState);
		tgpj_BldLimitAmountVer_AFDtl.setBldLimitAmountVerMng(bldLimitAmountVerMng);
		tgpj_BldLimitAmountVer_AFDtl.setStageName(stageName);
		tgpj_BldLimitAmountVer_AFDtl.setLimitedAmount(limitedAmount);
		tgpj_BldLimitAmountVer_AFDtl.setRemark(remark);

		tgpj_BldLimitAmountVer_AFDtlDao.save(tgpj_BldLimitAmountVer_AFDtl);	
		sm_AttachmentBatchAddService.execute(model,model.getTableId());



		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_BldLimitAmountVer_AFDtl.getTableId());

		return properties;
	}
}
