package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service添加操作：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceAddService {
    private static final String BUSI_CODE = "03010301";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
    private static final String ADD_BUSI_CODE = "03010301";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;

    //附件相关
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Tgpj_BuildingAccountLogCalculateService calculateService;
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

        Integer theState = S_TheState.Normal;
        String busiState = model.getBusiState();
        Long userStartId = model.getUserStartId();
        Long createTimeStamp = System.currentTimeMillis();
        Double recordAveragePrice = model.getRecordAveragePrice();
        Long buildingInfoId = model.getBuildingInfoId();
//		Long averagePriceRecordDate = model.getAveragePriceRecordDate();
        String averagePriceRecordDateString = model.getAveragePriceRecordDateString();
        Double recordAveragePriceFromPresellSystem = model.getRecordAveragePriceFromPresellSystem();
        String remark = model.getRemark();
        if (buildingInfoId == null || buildingInfoId < 1) {
            return MyBackInfo.fail(properties, "施工编号不能为空");
        }
        if (recordAveragePrice == null || recordAveragePrice < 1) {
            return MyBackInfo.fail(properties, "楼幢备案均价不能为空");
        }
        if (averagePriceRecordDateString == null || averagePriceRecordDateString.length() == 0) {
            return MyBackInfo.fail(properties, "物价备案日期不能为空");
        }
        Long averagePriceRecordDate = MyDatetime.getInstance().stringToLong(averagePriceRecordDateString);
        if(averagePriceRecordDate>System.currentTimeMillis()){
            return MyBackInfo.fail(properties, "物价备案日期不能超过今天");
        }
        Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
        Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingInfoId);
        if (buildingInfo == null) {
            return MyBackInfo.fail(properties, "该楼幢不存在");
        }
        MsgInfo buildingInEscrow = escrowStandardUtil.isBuildingInEscrow(buildingInfo);
        if (!buildingInEscrow.isSuccess()) {
            return MyBackInfo.fail(properties, buildingInEscrow.getInfo());
        }

        boolean uniqueBuilding = tgpj_BuildingAvgPriceDao.isUniqueBuilding(model);
        if (!uniqueBuilding) {
            return MyBackInfo.fail(properties, "该楼幢已经存在备案均价，无法新增");
        }
        MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
        if(!msgInfo.isSuccess()){
            return MyBackInfo.fail(properties, msgInfo.getInfo());
        }

        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();

        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = new Tgpj_BuildingAvgPrice();
        tgpj_BuildingAvgPrice.setTheState(theState);
        tgpj_BuildingAvgPrice.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
        tgpj_BuildingAvgPrice.setUserStart(userStart);
        tgpj_BuildingAvgPrice.setCreateTimeStamp(createTimeStamp);
        tgpj_BuildingAvgPrice.setUserStart(model.getUser());
        tgpj_BuildingAvgPrice.setUserUpdate(model.getUser());
        tgpj_BuildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpj_BuildingAvgPrice.setRecordAveragePrice(recordAveragePrice);
        tgpj_BuildingAvgPrice.setBuildingInfo(buildingInfo);
        tgpj_BuildingAvgPrice.setAveragePriceRecordDate(averagePriceRecordDate);
        tgpj_BuildingAvgPrice.setRecordAveragePriceFromPresellSystem(recordAveragePriceFromPresellSystem);
        tgpj_BuildingAvgPrice.setRemark(remark);
        tgpj_BuildingAvgPrice.setBusiState(S_BusiState.NoRecord);
        tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);

        sm_AttachmentBatchAddService.execute(model, tgpj_BuildingAvgPrice.getTableId());

//        sm_AttachmentBatchAddService.execute(model, tgpj_BuildingAvgPrice.getTableId());
        Long tableId = tgpj_BuildingAvgPrice.getTableId();
        if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
            buildingAccountLogUtil.changeAndCaculate(buildingInfo, tableId, ADD_BUSI_CODE, buildingAccountLog -> {
                setChanges(recordAveragePrice, recordAveragePriceFromPresellSystem, buildingAccountLog);
            });

//            Tgpj_BuildingAccountLog buildingAccountLog = buildingAccountLogUtil.getInitBuildingAccountLog(buildingInfo, tgpj_BuildingAvgPrice.getTableId(), ADD_BUSI_CODE);
//            buildingAccountLog.setCurrentLimitedRatio(100d);
//            buildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
//            calculateService.execute(buildingAccountLog);
        }
        //没有配置审批流程无需走审批流直接保存数据库
        if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");
            //审批操作
            sm_approvalProcessService.execute(tgpj_BuildingAvgPrice, model, sm_approvalProcess_cfg);
        } else {
            buildingAccountLogUtil.calculateWithoutApproval(buildingInfo,tableId,ADD_BUSI_CODE,tgpj_BuildingAccountLog -> {
                setChanges(recordAveragePrice, recordAveragePriceFromPresellSystem, tgpj_BuildingAccountLog);
//                tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
//                tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(recordAveragePriceFromPresellSystem);
            });
//            buildingAccountLogUtil.callBackChange(buildingInfo, properties, tableId, ADD_BUSI_CODE, (tgpj_BuildingAccountLog, accountLogForm) -> {
//                tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
//            });
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
    	//TODO
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
