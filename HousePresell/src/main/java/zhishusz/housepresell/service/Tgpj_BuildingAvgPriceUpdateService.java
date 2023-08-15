package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
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
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;
import zhishusz.housepresell.util.project.EscrowStandardUtil;
import com.google.gson.Gson;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

/*
 * Service更新操作：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BuildingAvgPriceUpdateService {
    @Autowired
    private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
    @Autowired
    private Sm_BusiState_LogAddService logAddService;
    @Autowired
    private Sm_UserDao sm_UserDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private Empj_ProjectInfoDao empj_ProjectInfoDao;
    @Autowired
    private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
    @Autowired
    private Gson gson;
    //附件相关
    @Autowired
    private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_PoCompareResult sm_PoCompareResult;
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Tgpj_BuildingAccountLogCalculateService calculateService;
    @Autowired
    private EscrowStandardUtil escrowStandardUtil;
    @Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
    @Autowired
    private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

    private String UPDATE_BUSI_CODE = "03010302";
    private String ADD_BUSI_CODE = "03010301";

    public Properties execute(Tgpj_BuildingAvgPriceForm model) {
        Properties properties = new MyProperties();

        Integer theState = model.getTheState();
        String busiState = model.getBusiState();
        String eCode = model.geteCode();
        Long userStartId = model.getUserStartId();
//		String createTimeStamp = model.getCreateTimeStamp();
        Long lastUpdateTimeStamp = System.currentTimeMillis();
        Long userRecordId = model.getUserRecordId();
        Long recordTimeStamp = model.getRecordTimeStamp();
        Double recordAveragePrice = model.getRecordAveragePrice();
        Long buildingInfoId = model.getBuildingInfoId();
        String averagePriceRecordDateString = model.getAveragePriceRecordDateString();
//        Double recordAveragePriceFromPresellSystem = model.getRecordAveragePriceFromPresellSystem();
        String remark = model.getRemark();
        String buttonType = model.getButtonType();
        Long tableId = model.getTableId();
        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = (Tgpj_BuildingAvgPrice) tgpj_BuildingAvgPriceDao.findByIdWithClear(tableId);
        Sm_User userStart = tgpj_BuildingAvgPrice.getUserStart();
        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPriceOld = ObjectCopier.copy(tgpj_BuildingAvgPrice);
        if (tgpj_BuildingAvgPrice == null) {
            return MyBackInfo.fail(properties, "'Tgpj_BuildingAvgPrice(Id:" + tableId + ")'不存在");
        }

        Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingInfoId);
        if (buildingInfo == null) {
            return MyBackInfo.fail(properties, "'buildingInfo(Id:" + buildingInfoId + ")'不存在");
        }
        MsgInfo buildingInEscrow = escrowStandardUtil.isBuildingInEscrow(buildingInfo);
        if (!buildingInEscrow.isSuccess()) {
            return MyBackInfo.fail(properties, buildingInEscrow.getInfo());
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
        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if(tgpj_BuildingAvgPrice.getBusiState().equals(S_BusiState.HaveRecord)){//已备案 才判断
            if (buildingAccount != null) {
                Double recordAvgPriceOfBuilding = buildingAccount.getRecordAvgPriceOfBuilding();//数据库中的
                if(recordAvgPriceOfBuilding!=null){
                    BigDecimal recordInDb = new BigDecimal(recordAvgPriceOfBuilding);
                    recordInDb=recordInDb.setScale(4,BigDecimal.ROUND_HALF_UP);
                    BigDecimal recordInInput = new BigDecimal(recordAveragePrice);//输入的
                    recordInInput = recordInInput.setScale(4, RoundingMode.HALF_UP);
                    int compare = recordInDb.compareTo(recordInInput);
                    if(compare>0){//数据库中的大于自己填的
                        return MyBackInfo.fail(properties, "物价备案均价变更只能增加，不能减少");
                    }
                }
            }
        }


//        if (buildingAccount != null) {
//            System.out.println("楼幢账户不为空");
//            buildingAccount.setRecordAvgPriceOfBuilding(recordAveragePrice);
//            buildingAccount.setRecordAvgPriceOfBuildingFromPresellSystem(recordAveragePriceFromPresellSystem);
//            tgpj_BuildingAccountDao.save(buildingAccount);
//        } else {
//            System.out.println("楼幢账户为空");
//        }
        if (model.getProjectId() != null) {
            Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(model.getProjectId());
            if (projectInfo == null) {
                return MyBackInfo.fail(properties, "该项目不存在！");
            } else {
                buildingInfo.setProject(projectInfo);
            }
        }
        if (model.getBusiState().equals(S_BusiState.NoRecord)) {
            if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
                buildingAccountLogUtil.changeAndCaculate(buildingInfo, tableId, ADD_BUSI_CODE, buildingAccountLog -> {
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
                });
            }
        } else {
            if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
                buildingAccountLogUtil.changeAndCaculate(buildingInfo, tableId, UPDATE_BUSI_CODE, buildingAccountLog -> {
                    buildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
                });
            }
        }

//        if (model.getBuildingInfoString() != null) {
//            Empj_BuildingInfo empj_buildingInfo = gson.fromJson(model.getBuildingInfoString(), Empj_BuildingInfo.class);
////				buildingInfo.seteCodeFromConstruction(empj_buildingInfo.geteCodeFromConstruction());
//            buildingInfo.seteCodeFromPublicSecurity(empj_buildingInfo.geteCodeFromPublicSecurity());
//            buildingInfo.seteCode(empj_buildingInfo.geteCode());
//        }
//			Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();

//       averagePriceRecordDate = MyDatetime.getInstance().stringToLong(averagePriceRecordDateString);

        tgpj_BuildingAvgPrice.setTheState(theState);
        tgpj_BuildingAvgPrice.setBusiState(busiState);
        tgpj_BuildingAvgPrice.seteCode(eCode);
//		tgpj_BuildingAvgPrice.setUserStart(userStart);
//		tgpj_BuildingAvgPrice.setCreateTimeStamp(createTimeStamp);
        tgpj_BuildingAvgPrice.setUserUpdate(model.getUser());
        tgpj_BuildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());
//		tgpj_BuildingAvgPrice.setUserRecord(userRecord);
        tgpj_BuildingAvgPrice.setRecordTimeStamp(recordTimeStamp);
        tgpj_BuildingAvgPrice.setRecordAveragePrice(recordAveragePrice);
        tgpj_BuildingAvgPrice.setBuildingInfo(buildingInfo);
        if(tgpj_BuildingAvgPrice.getBusiState().equals(S_BusiState.NoRecord)){
            tgpj_BuildingAvgPrice.setAveragePriceRecordDate(averagePriceRecordDate);
        }
//		tgpj_BuildingAvgPrice.setAveragePriceRecordDate(averagePriceRecordDate);
//        tgpj_BuildingAvgPrice.setRecordAveragePriceFromPresellSystem(recordAveragePriceFromPresellSystem);
        tgpj_BuildingAvgPrice.setRemark(remark);
        Tgpj_BuildingAvgPrice tgpj_BuildingAvgPriceNew = ObjectCopier.copy(tgpj_BuildingAvgPrice);

        //审批流开始
        Boolean flag = sm_PoCompareResult.execute(tgpj_BuildingAvgPriceOld, tgpj_BuildingAvgPriceNew);

        System.out.println("结果：" + flag);

        if (flag) {
            for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList()) {
                //如果有form没有tableId，说明有新增
                if (formOSS.getTableId() == null || formOSS.getTableId() == 0) {
                    flag = false;//有新增不一样
                    break;
                }
            }
            if (flag) //如果没有新增再看有没有删除
            {
                Integer totalCountNew = model.getGeneralAttachmentList().length;

                Sm_AttachmentForm theForm = new Sm_AttachmentForm();
                theForm.setTheState(S_TheState.Normal);
                theForm.setBusiType(UPDATE_BUSI_CODE);
                theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));

                Integer totalCount = sm_AttachmentDao.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));

                if (totalCountNew < totalCount) {
                    flag = false;//有删除不一样
                }
            }
        }
        //先判断是否是未备案
        //如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
        if (S_BusiState.NoRecord.equals(tgpj_BuildingAvgPrice.getBusiState())) {
            tgpj_BuildingAvgPriceDao.update(tgpj_BuildingAvgPrice);

            MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
            if(!msgInfo.isSuccess()){
                return MyBackInfo.fail(properties, msgInfo.getInfo());
            }
            sm_AttachmentBatchAddService.execute(model, tgpj_BuildingAvgPrice.getTableId());

            //如果是提交按钮则需要走新增的审批流
            if (S_ButtonType.Submit.equals(buttonType)) {
                properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
                if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                    return properties;
                }

                //没有配置审批流程无需走审批流直接保存数据库
                if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
                    //有相应的审批流程配置才走审批流程
                    Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

                    //审批操作
                    sm_approvalProcessService.execute(tgpj_BuildingAvgPrice, model, sm_approvalProcess_cfg);
                } else {
                    buildingAccountLogUtil.calculateWithoutApproval(buildingInfo, tableId, UPDATE_BUSI_CODE, tgpj_BuildingAccountLog -> {
                        setChanges(recordAveragePrice, tgpj_BuildingAvgPrice.getRecordAveragePriceFromPresellSystem(), tgpj_BuildingAccountLog);
                    });

//                    buildingAccountLogUtil.callBackChange(buildingInfo, properties, tableId, UPDATE_BUSI_CODE, (tgpj_BuildingAccountLog, accountLogForm) -> {
//                        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(recordAveragePrice);
//                    });
                    tgpj_BuildingAvgPrice.setBusiState(S_BusiState.HaveRecord);
                    tgpj_BuildingAvgPrice.setApprovalState(S_ApprovalState.Completed);
                    tgpj_BuildingAvgPriceDao.update(tgpj_BuildingAvgPrice);
                }
            }
        } else if (!flag) {
            properties = sm_ApprovalProcessGetService.execute(UPDATE_BUSI_CODE, model.getUserId());
            if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                return properties;
            }

            //没有配置审批流程无需走审批流直接保存数据库
            if ("noApproval".equals(properties.getProperty(S_NormalFlag.info))) {
                Sm_User user = model.getUser();
                tgpj_BuildingAvgPrice.setBusiState(S_BusiState.HaveRecord);
                tgpj_BuildingAvgPrice.setApprovalState(S_ApprovalState.Completed);
                tgpj_BuildingAvgPrice.setUserUpdate(user);
                tgpj_BuildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());
                tgpj_BuildingAvgPrice.setUserRecord(user);
                tgpj_BuildingAvgPrice.setRecordTimeStamp(System.currentTimeMillis());
                tgpj_BuildingAvgPriceDao.update(tgpj_BuildingAvgPrice);

                MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
                if(!msgInfo.isSuccess()){
                    return MyBackInfo.fail(properties, msgInfo.getInfo());
                }

                sm_AttachmentBatchAddService.execute(model, model.getTableId());
                buildingAccountLogUtil.calculateWithoutApproval(buildingInfo, tableId, UPDATE_BUSI_CODE, tgpj_BuildingAccountLog -> {
                    tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAvgPrice.getRecordAveragePrice());
                });

//                buildingAccountLogUtil.callBackChange(buildingInfo,properties,tableId,UPDATE_BUSI_CODE,(tgpj_BuildingAccountLog, accountLogForm) -> {
//                   tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(tgpj_BuildingAvgPrice.getRecordAveragePrice());
//                });

                //日志，备案人，备案日期
//                logAddService.addLog(model, tableId, empj_BuildingInfoTemplateOld, empj_BuildingInfoTemplateNew);
            } else {
                //做一个还原操作
                try {
                    PropertyUtils.copyProperties(tgpj_BuildingAvgPrice, tgpj_BuildingAvgPriceOld);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                tgpj_BuildingAvgPrice.setUserUpdate(model.getUser());
                tgpj_BuildingAvgPrice.setLastUpdateTimeStamp(System.currentTimeMillis());

                //有相应的审批流程配置才走审批流程
                Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

                //审批操作
                sm_approvalProcessService.execute(tgpj_BuildingAvgPrice, model, sm_approvalProcess_cfg);
            }
        }
        //审批结束


//		tgpj_BuildingAvgPriceDao.save(tgpj_BuildingAvgPrice);
//		logAddService.addLog(model, tableId, tgpj_BuildingAvgPriceOld, tgpj_BuildingAvgPrice);
//		sm_AttachmentBatchAddService.execute(model,model.getTableId());

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        properties.put("tableId", tgpj_BuildingAvgPrice.getTableId());
        properties.remove("sm_approvalProcess_cfg");


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
