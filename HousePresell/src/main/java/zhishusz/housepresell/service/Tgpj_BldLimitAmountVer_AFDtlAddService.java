
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

import java.util.Properties;

import javax.transaction.Transactional;
	
/*
 * Service添加操作：受限额度设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFDtlAddService
{
	private static final String BUSI_CODE = "06010102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	public Properties execute(Tgpj_BldLimitAmountVer_AFDtlForm model)
	{
		model.setOrderBy("limitedAmount asc");
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		Long bldLimitAmountVerMngId = model.getBldLimitAmountVerMngId();
		String stageName = model.getStageName();
		Double limitedAmount = model.getLimitedAmount();
		String remark = model.getRemark();
		String nodeTypeName = model.getNodeTypeName();

		//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "'theState'不能为空");
//		}
		if(bldLimitAmountVerMngId == null || bldLimitAmountVerMngId < 1)
		{
			return MyBackInfo.fail(properties, "'bldLimitAmountVerMng'不能为空");
		}
		//todo 根据node name得出stage和limit
//		if(stageName == null || stageName.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'stageName'不能为空");
//		}

		if(stageName==null || stageName.length()==0){
			return MyBackInfo.fail(properties, "受限额度节点名称不能为空");
		}
		if(limitedAmount == null || limitedAmount < 0)
		{
			return MyBackInfo.fail(properties, "受限比例不能为空");
		}

		Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng = (Tgpj_BldLimitAmountVer_AF)tgpj_BldLimitAmountVer_AFDao.findById(bldLimitAmountVerMngId);
		if(bldLimitAmountVerMng == null)
		{
			return MyBackInfo.fail(properties, "'bldLimitAmountVerMng'不能为空");
		}else{
//			String theType = bldLimitAmountVerMng.getTheType();
//			Tg_HouseStage tg_houseStage=null;
//			if(theType.equals("0")){
//				tg_houseStage= S_HouseStage.BUILDING_STAGE.get(Integer.parseInt(nodeTypeName));
//			}else{
//				tg_houseStage = S_HouseStage.COMPLETE_STAGE.get(Integer.parseInt(nodeTypeName));
//			}
//			if(tg_houseStage!=null){
//				stageName = tg_houseStage.getStageName();
//				limitedAmount = tg_houseStage.getPercent();
//			}

		}
		Tgpj_BldLimitAmountVer_AFDtl tgpj_BldLimitAmountVer_AFDtl = new Tgpj_BldLimitAmountVer_AFDtl();
		tgpj_BldLimitAmountVer_AFDtl.setTheState(theState);
		tgpj_BldLimitAmountVer_AFDtl.setBldLimitAmountVerMng(bldLimitAmountVerMng);
		tgpj_BldLimitAmountVer_AFDtl.setStageName(stageName);
		tgpj_BldLimitAmountVer_AFDtl.setLimitedAmount(limitedAmount);
		tgpj_BldLimitAmountVer_AFDtl.setRemark(remark);
		tgpj_BldLimitAmountVer_AFDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BldLimitAmountVer_AFDtlDao.save(tgpj_BldLimitAmountVer_AFDtl);
		//todo 可能有问题
//		sm_AttachmentBatchAddService.execute(model, tgpj_BldLimitAmountVer_AFDtl.getTableId());


		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_BldLimitAmountVer_AFDtl.getTableId());

		return properties;
	}
}
