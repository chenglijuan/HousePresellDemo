package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;

/*
 * Service提交操作：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceApprovalProcessService {
    private static final String ADD_BUSI_CODE = "03010301";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;

    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;

    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

    public Properties execute(Tgpj_BuildingAvgPriceForm model) {
        Properties properties = new MyProperties();
        properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
        if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
            if (properties.getProperty(S_NormalFlag.info).equals("noApproval")) {

            } else {
                return properties;
            }
        }

        Long tableId2 = model.getTableId();
        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findById(tableId2);
        
        Empj_BuildingInfo buildingInfo = tgpj_BuildingAvgPrice.getBuildingInfo();
        if (buildingInfo == null) {
            return MyBackInfo.fail(properties, "该楼幢不存在");
        }

        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();

        Long tableId = tgpj_BuildingAvgPrice.getTableId();
        if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
            buildingAccountLogUtil.changeAndCaculate(buildingInfo, tableId, ADD_BUSI_CODE, buildingAccountLog -> {
                setChanges(tgpj_BuildingAvgPrice.getRecordAveragePrice(), tgpj_BuildingAvgPrice.getRecordAveragePriceFromPresellSystem(), buildingAccountLog);
            });

        }
        //没有配置审批流程无需走审批流直接保存数据库
        if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
            //审批操作
            sm_approvalProcessService.execute(tgpj_BuildingAvgPrice, model, sm_approvalProcess_cfg);
        } else {
            buildingAccountLogUtil.calculateWithoutApproval(buildingInfo,tableId,ADD_BUSI_CODE,tgpj_BuildingAccountLog -> {
                setChanges(tgpj_BuildingAvgPrice.getRecordAveragePrice(), tgpj_BuildingAvgPrice.getRecordAveragePriceFromPresellSystem(), tgpj_BuildingAccountLog);
            });
            //备案人，备案日期，备案状态，审批状态
           tgpj_BuildingAvgPrice.setApprovalState(S_ApprovalState.Completed);
           tgpj_BuildingAvgPrice.setBusiState(S_BusiState.HaveRecord);
           tgpj_BuildingAvgPrice.setUserRecord(model.getUser());
           tgpj_BuildingAvgPrice.setRecordTimeStamp(System.currentTimeMillis());
           tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("tableId", tgpj_BuildingAvgPrice.getTableId());

        return properties;
    }

    private void setChanges(Double recordAveragePrice, Double recordAveragePriceFromPresellSystem, Tgpj_BuildingAccountLog buildingAccountLog) {
    	/*
    	 * xsz by time 2019-2-14 16:13:20
    	 * 判断原来是否存在当前受限比例，如果存在，则不需要更新；不存在，默认100
    	 */
    	if(null == buildingAccountLog.getCurrentLimitedRatio())
    	{
    		buildingAccountLog.setCurrentLimitedRatio(100d);
    	}
        buildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
        buildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(recordAveragePriceFromPresellSystem);
    }
}
