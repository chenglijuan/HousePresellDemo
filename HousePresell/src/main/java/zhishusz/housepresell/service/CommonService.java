package zhishusz.housepresell.service;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.extra.*;
import zhishusz.housepresell.database.po.state.*;
import zhishusz.housepresell.database.po.toInterface.To_BldEscrowCompleted;
import zhishusz.housepresell.database.po.toInterface.To_ProjProgForcastPhoto;
import zhishusz.housepresell.external.service.GjjService;
import zhishusz.housepresell.util.*;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

/*
 * Service添加操作：公共 Company：ZhiShuSZ
 */
@Service
@Transactional
@Slf4j
public class CommonService {

    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢
    @Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;// 受限额度变更
    @Autowired
    private Tgpj_BuildingAccountLogDao tgpj_BuildingAccountLogDao;// 楼幢账户log
    @Autowired
    private Tgpj_BuildingAccountLimitedUpdateService tgpj_BuildingAccountLimitedUpdateService;// 更新楼幢账户公共方法
    @Autowired
    private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao;// 进度节点申请楼幢
    @Autowired
    private Empj_BldAccountGetLimitAmountVerService bldAccountGetLimitAmountVerService;
    @Autowired
    private Sm_ProjProgParameterDao sm_ProjProgParameterDao;
    @Autowired
    private Empj_ProjProgInspection_AFDao empj_ProjProgInspection_AFDao;
    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;

    @Autowired
    private Empj_ProjProgForcast_AFDao projProgForcast_AFDao;
    @Autowired
    private Empj_ProjProgForcast_DTLDao projProgForcast_DTLDao;

    @Autowired
    private Empj_ProjProgForcast_ManageDao manageDao;
    @Autowired
    private Sm_UserDao userDao;
    @Autowired
    private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
    @Autowired
    private Sm_ApprovalProcessService sm_approvalProcessService;
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    @Autowired
    private Sm_AttachmentCfgDao attacmentcfgDao;
    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;
    @Autowired
    private OssServerUtil ossUtil;
    @Autowired
    private CommonService commonService;

//    @Autowired
//    private Gjj_BulidingRelationDao gjj_bulidingRelationDao;

    private static final Integer NUMBER_ONE = 1;
    private static final Integer NUMBER_HALF = 2;

    public Properties execute(CommonForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 工程进度节点更新楼幢加载
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties bldForBuildingList(CommonForm model) {
        Properties properties = new MyProperties();

        Long projectId = model.getProjectId();

        List<Map<String, Object>> query_Get_buidling_changeList = new ArrayList<>();
        if (null != projectId && projectId > 1) {
            try {
                query_Get_buidling_changeList = empj_BuildingInfoDao.query_Get_buidling_change(projectId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Empj_BuildingInfoForm buildingInfoForm;
            Properties execute;
            List<Map<String, Object>> listMap;

            for (Map<String, Object> map : query_Get_buidling_changeList) {
                map.put("disabled", true);
                /*
                 * 查询每一个楼幢可下拉的拟变更形象进度
                 */
                buildingInfoForm = new Empj_BuildingInfoForm();
                buildingInfoForm.setTableId(Long.parseLong((String) map.get("tableId")));
                buildingInfoForm.setNowLimitRatio((String) map.get("currentLimitedRatio"));
                execute = bldAccountGetLimitAmountVerService.execute(buildingInfoForm);
                if (null != execute) {
                    listMap = new ArrayList<>();
                    listMap = (List<Map<String, Object>>) execute.get("versionList");
                    if (null == listMap || listMap.size() < 1) {
                        continue;
                    } else {
                        listMap.remove(0);
                    }
                    map.put("versionList", listMap);
                } else {
                    map.put("versionList", new ArrayList<Map<String, Object>>());
                }
            }
        }

        properties.put("empj_BuildingInfoList", query_Get_buidling_changeList);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 工程进度节点更新楼幢加载
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties bldForBuildingList1(CommonForm model) {
        Properties properties = new MyProperties();

        Empj_BuildingInfoForm form = new Empj_BuildingInfoForm();
        form.setTheState(S_TheState.Normal);
        form.setOrderBy("eCodeFromConstruction");
        form.setProjectId(model.getProjectId());
        form.setBusiState("已备案");

        List<Empj_BuildingInfo> empj_BuildingInfoList = new ArrayList<Empj_BuildingInfo>();
        empj_BuildingInfoList = empj_BuildingInfoDao
                .findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), form));

        /*
         * 工程进度节点审核中的
         */
        Empj_BldLimitAmount_DtlForm amountForm = new Empj_BldLimitAmount_DtlForm();
        amountForm.setTheState(S_TheState.Normal);
        amountForm.setAfState("审核中");
        List<Empj_BldLimitAmount_Dtl> dtlList = new ArrayList<Empj_BldLimitAmount_Dtl>();
        dtlList = empj_BldLimitAmount_DtlDao.findByPage(
                empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getHQLByBuildingList(), amountForm));

        if (dtlList.size() > 0) {
            for (Empj_BldLimitAmount_Dtl empj_BldLimitAmount_Dtl : dtlList) {
                for (int i = 0; i < empj_BuildingInfoList.size(); i++) {
                    if (empj_BldLimitAmount_Dtl.getBuilding().getTableId() == empj_BuildingInfoList.get(i)
                            .getTableId()) {
                        empj_BuildingInfoList.remove(i);
                        break;
                    }
                }
            }
        }

        /*
         * 受限额度变更审核中的
         */

        properties.put("empj_BuildingInfoList", empj_BuildingInfoList);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 项目部报表-楼幢销售面积查询
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties saleAreaForBuildingList(CommonForm model) {
        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> retmap = new HashMap<String, Object>();

        Integer totalPage = 0;
        Integer totalCount = 0;

        try {

            retmap = empj_BuildingInfoDao.saleAreaForBuildingList(null, model.getKeyword(), model.getCompanyId(),
                    model.getProjectId(), model.getBuildId(), pageNumber, countPerPage);

            totalPage = (Integer) retmap.get("totalPage");
            totalCount = (Integer) retmap.get("totalCount");
            listMap = (List<Map<String, Object>>) retmap.get("saleAreaForBuildingList");
        } catch (SQLException e) {
            listMap = new ArrayList<>();
        }

        properties.put("saleAreaForBuildingList", listMap);
        properties.put(S_NormalFlag.keyword, model.getKeyword());
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 项目部报表-楼幢销售面积查询-导出
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties saleAreaForBuildingList_ExportExcel(CommonForm model) {
        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = 999999;

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> retmap = new HashMap<String, Object>();

        Integer totalPage = 0;
        Integer totalCount = 0;

        try {
            retmap = empj_BuildingInfoDao.saleAreaForBuildingList(null, model.getKeyword(), model.getCompanyId(),
                    model.getProjectId(), model.getBuildId(), pageNumber, countPerPage);

            totalPage = (Integer) retmap.get("totalPage");
            totalCount = (Integer) retmap.get("totalCount");
            listMap = (List<Map<String, Object>>) retmap.get("saleAreaForBuildingList");

            if (null == listMap || listMap.size() < 1) {
                return MyBackInfo.fail(properties, "未查询到有效数据");
            } else {
                // 初始化文件保存路径，创建相应文件夹
                DirectoryUtil directoryUtil = new DirectoryUtil();
                String relativeDir = directoryUtil.createRelativePathWithDate("Qs_BuildingAccount_View");// 文件在项目中的相对路径
                String localPath = directoryUtil.getProjectRoot();// 项目路径

                String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

                if (saveDirectory.contains("%20")) {
                    saveDirectory = saveDirectory.replace("%20", " ");
                }

                directoryUtil.mkdir(saveDirectory);

                String strNewFileName = "楼幢销售面积查询-"
                        + MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

                String saveFilePath = saveDirectory + strNewFileName;

                // 通过工具类创建writer
                ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

                // 自定义字段别名
                writer.addHeaderAlias("tableId", "序号");
                writer.addHeaderAlias("theNameOfCompany", "开发企业");
                writer.addHeaderAlias("theNameOfProject", "项目");
                writer.addHeaderAlias("theNameOfBuilding", "施工编号");
                writer.addHeaderAlias("price", "楼幢备案均价（元）");
                writer.addHeaderAlias("escrowarea", "楼幢托管面积");
                writer.addHeaderAlias("salemj", "楼幢已销售面积");
                writer.addHeaderAlias("mj", "楼幢未销售面积");

                // 一次性写出内容，使用默认样式
                writer.write(listMap);

                // 关闭writer，释放内存
                writer.flush();

                writer.autoSizeColumn(0, true);
                writer.autoSizeColumn(1, true);
                writer.autoSizeColumn(2, true);
                writer.autoSizeColumn(3, true);
                writer.autoSizeColumn(4, true);
                writer.autoSizeColumn(5, true);
                writer.autoSizeColumn(6, true);
                writer.autoSizeColumn(7, true);

                writer.close();

                properties.put("fileURL", relativeDir + strNewFileName);
            }

        } catch (SQLException e) {
            return MyBackInfo.fail(properties, "未查询到有效数据");
        }

        properties.put(S_NormalFlag.keyword, model.getKeyword());
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    public Properties bldLimitAmount_AFHandler(CommonForm model) {
        Properties properties = new MyProperties();

        long nowDateStample = System.currentTimeMillis();

        Empj_BldLimitAmount_AF bldLimitAmount_AF = empj_BldLimitAmount_AFDao.findById(model.getTableId());

        Empj_BuildingInfo building = bldLimitAmount_AF.getBuilding();
        Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();

        Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = bldLimitAmount_AF.getExpectFigureProgress();

        /*
         * 保存楼幢账户log表
         */
        // 不发生修改的字段
        Tgpj_BuildingAccountLog tgpj_BuildingAccountLog = new Tgpj_BuildingAccountLog();
        tgpj_BuildingAccountLog.setTheState(S_TheState.Normal);
        tgpj_BuildingAccountLog.setBusiState("0");
        tgpj_BuildingAccountLog.seteCode(buildingAccount.geteCode());
        tgpj_BuildingAccountLog.setUserStart(buildingAccount.getUserStart());
        tgpj_BuildingAccountLog.setCreateTimeStamp(buildingAccount.getCreateTimeStamp());
        tgpj_BuildingAccountLog.setUserUpdate(buildingAccount.getUserUpdate());
        tgpj_BuildingAccountLog.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpj_BuildingAccountLog.setUserRecord(buildingAccount.getUserRecord());
        tgpj_BuildingAccountLog.setRecordTimeStamp(buildingAccount.getRecordTimeStamp());
        tgpj_BuildingAccountLog.setDevelopCompany(buildingAccount.getDevelopCompany());
        tgpj_BuildingAccountLog.seteCodeOfDevelopCompany(buildingAccount.geteCodeOfDevelopCompany());
        tgpj_BuildingAccountLog.setProject(buildingAccount.getProject());
        tgpj_BuildingAccountLog.setTheNameOfProject(buildingAccount.getTheNameOfProject());
        tgpj_BuildingAccountLog.setBuilding(buildingAccount.getBuilding());
        tgpj_BuildingAccountLog.setPayment(buildingAccount.getPayment());
        tgpj_BuildingAccountLog.seteCodeOfBuilding(buildingAccount.geteCodeOfBuilding());
        tgpj_BuildingAccountLog.setEscrowStandard(buildingAccount.getEscrowStandard());
        tgpj_BuildingAccountLog.setEscrowArea(buildingAccount.getEscrowArea());
        tgpj_BuildingAccountLog.setBuildingArea(buildingAccount.getBuildingArea());
        tgpj_BuildingAccountLog.setOrgLimitedAmount(buildingAccount.getOrgLimitedAmount());
        tgpj_BuildingAccountLog.setTotalGuaranteeAmount(buildingAccount.getTotalGuaranteeAmount());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
        tgpj_BuildingAccountLog.setTotalAccountAmount(buildingAccount.getTotalAccountAmount());
        tgpj_BuildingAccountLog.setPayoutAmount(buildingAccount.getPayoutAmount());
        tgpj_BuildingAccountLog.setAppliedNoPayoutAmount(buildingAccount.getAppliedNoPayoutAmount());
        tgpj_BuildingAccountLog.setApplyRefundPayoutAmount(buildingAccount.getApplyRefundPayoutAmount());
        tgpj_BuildingAccountLog.setRefundAmount(buildingAccount.getRefundAmount());
        tgpj_BuildingAccountLog.setCurrentEscrowFund(buildingAccount.getCurrentEscrowFund());
        tgpj_BuildingAccountLog.setAppropriateFrozenAmount(buildingAccount.getAppropriateFrozenAmount());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuildingFromPresellSystem(
                buildingAccount.getRecordAvgPriceOfBuildingFromPresellSystem());

        tgpj_BuildingAccountLog.setLogId(buildingAccount.getLogId());
        tgpj_BuildingAccountLog.setActualAmount(buildingAccount.getActualAmount());
        tgpj_BuildingAccountLog.setPaymentLines(buildingAccount.getPaymentLines());
        tgpj_BuildingAccountLog.setRelatedBusiCode("03030101");
        tgpj_BuildingAccountLog.setRelatedBusiTableId(bldLimitAmount_AF.getTableId());
        tgpj_BuildingAccountLog.setTgpj_BuildingAccount(buildingAccount);
        tgpj_BuildingAccountLog.setVersionNo(buildingAccount.getVersionNo());
        tgpj_BuildingAccountLog.setPaymentProportion(buildingAccount.getPaymentProportion());
        tgpj_BuildingAccountLog.setBuildAmountPaid(buildingAccount.getBuildAmountPaid());
        tgpj_BuildingAccountLog.setBuildAmountPay(buildingAccount.getBuildAmountPay());
        tgpj_BuildingAccountLog.setTotalAmountGuaranteed(buildingAccount.getTotalAmountGuaranteed());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());
        tgpj_BuildingAccountLog.setEffectiveLimitedAmount(buildingAccount.getEffectiveLimitedAmount());
        tgpj_BuildingAccountLog.setSpilloverAmount(buildingAccount.getSpilloverAmount());
        tgpj_BuildingAccountLog.setAllocableAmount(buildingAccount.getAllocableAmount());
        tgpj_BuildingAccountLog.setRecordAvgPriceOfBuilding(buildingAccount.getRecordAvgPriceOfBuilding());

        tgpj_BuildingAccountLog.setNodeLimitedAmount(buildingAccount.getNodeLimitedAmount());
        tgpj_BuildingAccountLog.setCashLimitedAmount(buildingAccount.getCashLimitedAmount());

        // 修改产生了变更的字段
        tgpj_BuildingAccountLog.setCurrentFigureProgress(expectFigureProgress.getStageName()); // 当前形象进度
        tgpj_BuildingAccountLog.setCurrentLimitedRatio(expectFigureProgress.getLimitedAmount()); // 当前受限比例
        tgpj_BuildingAccountLog.setBldLimitAmountVerDtl(expectFigureProgress);

        tgpj_BuildingAccountLogDao.save(tgpj_BuildingAccountLog);

        tgpj_BuildingAccountLimitedUpdateService.execute(tgpj_BuildingAccountLog);

        bldLimitAmount_AF.setBusiState("已备案");
        bldLimitAmount_AF.setApprovalState("已完结");
        bldLimitAmount_AF.setRecordTimeStamp(nowDateStample);
        bldLimitAmount_AF.setUserRecord(model.getUser());
        bldLimitAmount_AF.setUserUpdate(model.getUser());
        bldLimitAmount_AF.setLastUpdateTimeStamp(nowDateStample);
        empj_BldLimitAmount_AFDao.update(bldLimitAmount_AF);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 楼幢节点预测
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties ProjProgForecastExecute(CommonForm model) {
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        MyDatetime dateUtil = MyDatetime.getInstance();

        Long buildId = model.getBuildId();
        Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(buildId);
        if (null == buildingInfo) {
            return MyBackInfo.fail(properties, "未查询到有效楼幢信息！");
        }

        String forcastTime = model.getForcastTime();
        if (StrUtil.isBlank(forcastTime)) {
            return MyBackInfo.fail(properties, "未选择有效的巡查时间！");
        }

        Date parseForcastTime = dateUtil.parse(forcastTime);
        if (null == parseForcastTime) {
            return MyBackInfo.fail(properties, "巡查时间无效！");
        }

        String buildProgress = model.getBuildProgress();
        String buildProgressType = model.getBuildProgressType();

        // 当前建设楼层数
        Integer nowBuildNum = model.getNowBuildNum();

        if (0 == nowBuildNum) {
            return properties;
        }
        // 总楼层数
        Integer buildCountNum = model.getBuildCountNum();
        // 当前进度节点数据
        Tgpj_BldLimitAmountVer_AFDtl nowNode = model.getNowNode();
        if (null == nowNode) {
            return properties;
        }
        Double nowLimitedAmount = nowNode.getLimitedAmount();

        // 1/2建设楼层数 = (总楼层 + 1 )/2 + 1

        // 计算方式一
        // Integer halfNum = ((buildCountNum + NUMBER_ONE) / NUMBER_HALF) +
        // NUMBER_ONE;

        // 计算方式二
        Integer halfNum = (buildCountNum + NUMBER_ONE) / NUMBER_HALF;

        // 1/2计算楼层 = 1/2建设楼层数 - 当前建设楼层数
        Integer caNum = halfNum - nowBuildNum;

        // 主体结构封顶计算楼层 = 总楼层数 - 1/2建设楼层数 + 1
        Integer coNum = buildCountNum - halfNum + NUMBER_ONE;

        // 查询预测参数信息
        Empj_ProjectInfo project = buildingInfo.getProject();
        CommonForm form = new CommonForm();
        form.setTheState(S_TheState.Normal);
        form.setProject(project);
        List<Sm_ProjProgParameter> listParameter = sm_ProjProgParameterDao
                .findByPage(sm_ProjProgParameterDao.getQuery(sm_ProjProgParameterDao.getBasicHQL(), form));
        if (null == listParameter || listParameter.isEmpty()) {
            return MyBackInfo.fail(properties, "未查询到有效的预测参数信息！");
        }

        Sm_ProjProgParameter sm_ProjProgParameter = listParameter.get(0);
        Integer parameterOne = sm_ProjProgParameter.getParameterOne();
        Integer parameterTwo = sm_ProjProgParameter.getParameterTwo();
        Integer parameterThree = sm_ProjProgParameter.getParameterThree();
        if (null == parameterOne || 0 == parameterOne) {
            return MyBackInfo.fail(properties, "请配置完整的预测参数！");
        }

        if (null == parameterTwo || 0 == parameterTwo) {
            return MyBackInfo.fail(properties, "请配置完整的预测参数！");
        }

        if (null == parameterThree || 0 == parameterThree) {
            return MyBackInfo.fail(properties, "请配置完整的预测参数！");
        }

        // Empj_ProjProgInspection_AF afInfo = new Empj_ProjProgInspection_AF();
        // 查询需要预测的楼幢节点信息
        Empj_ProjProgInspection_DTLForm dtlModel = new Empj_ProjProgInspection_DTLForm();
        dtlModel.setTheState(S_TheState.Normal);
        dtlModel.setBuildInfo(buildingInfo);
        List<Empj_ProjProgInspection_DTL> dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));
        if (null != dtlList && !dtlList.isEmpty()) {

            String dateAddDate;
            Map<String, Object> map = new HashMap<>();

            /*
             * 计算方式一
             */
            /*
             * // 正负零节点预测完成时间 String forecastCompleteDate =
             * dtlList.get(1).getForecastCompleteDate(); if
             * (StrUtil.isBlank(forecastCompleteDate)) { forecastCompleteDate =
             * dateUtil.getCurrentDate(); }
             *
             * Map<String, Object> map = new HashMap<>(); for
             * (Empj_ProjProgInspection_DTL dtl : dtlList) {
             *
             * Double limitedAmount = dtl.getForecastNode().getLimitedAmount();
             * if (dtl.getForecastNodeName().contains("1/2")) { // 完成主体1/2 =
             * 1/2计算楼层 * 参数1 map.put("1/2",
             * StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" :
             * dtl.getForecastCompleteDate()); if (nowLimitedAmount -
             * limitedAmount >= 0) { dateAddDate =
             * dateUtil.getDateAddDate(forecastCompleteDate, caNum *
             * parameterOne);
             *
             * map.put("1/2", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate);
             *
             * } }
             *
             * if (dtl.getForecastNodeName().contains("封顶")) { // 完成主体结构封顶
             * map.put("封顶", StrUtil.isBlank(dtl.getForecastCompleteDate()) ? ""
             * : dtl.getForecastCompleteDate()); if (nowLimitedAmount -
             * limitedAmount >= 0) {
             *
             * if ("2".equals(buildProgressType) ||
             * "3".equals(buildProgressType)) { dateAddDate =
             * dateUtil.getDateAddDate( null == map.get("1/2") ?
             * dateUtil.getCurrentDate() : (String)map.get("1/2"), coNum *
             * parameterOne);
             *
             * map.put("封顶", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); } else { dateAddDate =
             * dateUtil.getDateAddDate( null == map.get("1/2") ?
             * dateUtil.getCurrentDate() : (String)map.get("1/2"), coNum *
             * parameterOne);
             *
             * map.put("封顶", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); }
             *
             * } }
             *
             * if (dtl.getForecastNodeName().contains("外立面")) { // 完成外立面装饰
             * map.put("外立面", StrUtil.isBlank(dtl.getForecastCompleteDate()) ?
             * "" : dtl.getForecastCompleteDate()); if (nowLimitedAmount -
             * limitedAmount >= 0) {
             *
             * if ("2".equals(buildProgressType) ||
             * "3".equals(buildProgressType)) { dateAddDate =
             * dateUtil.getDateAddDate( null == map.get("封顶") ?
             * dateUtil.getCurrentDate() : (String)map.get("封顶"), (parameterTwo
             * * (100 - nowBuildNum) / 100));
             *
             * map.put("外立面", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); } else { dateAddDate =
             * dateUtil.getDateAddDate( null == map.get("封顶") ?
             * dateUtil.getCurrentDate() : (String)map.get("封顶"), parameterTwo);
             *
             * map.put("外立面", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); }
             *
             * } }
             *
             * if (dtl.getForecastNodeName().contains("室内")) { // 完成室内装修
             * map.put("室内", StrUtil.isBlank(dtl.getForecastCompleteDate()) ? ""
             * : dtl.getForecastCompleteDate()); if (nowLimitedAmount -
             * limitedAmount >= 0) {
             *
             * if ("2".equals(buildProgressType) ||
             * "3".equals(buildProgressType)) { dateAddDate =
             * dateUtil.getDateAddDate( null == (String)map.get("外立面") ?
             * dateUtil.getCurrentDate() : (String)map.get("外立面"),
             * (parameterThree * (100 - nowBuildNum) / 100));
             *
             * map.put("室内", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); } else {
             *
             * dateAddDate = dateUtil.getDateAddDate( null ==
             * (String)map.get("外立面") ? dateUtil.getCurrentDate() :
             * (String)map.get("外立面"), parameterTwo);
             *
             * map.put("室内", dateAddDate);
             * dtl.setForecastCompleteDate(dateAddDate); }
             *
             * } }
             *
             * dtl.setBuildProgress(String.valueOf(nowBuildNum));
             * dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
             * dtl.setUserUpdate(model.getUser()); dtl.setDataSources("工程巡查");
             * empj_ProjProgInspection_DTLDao.update(dtl);
             *
             * }
             */

            /*
             * 计算方式二 parseForcastTime : 项目巡查时间
             */

            // 预测天数-主体结构1/2
            Integer one = 0;
            // 预测天数-主体结构封顶
            Integer two = 0;
            // 预测天数-外立面装饰完成
            Integer three = 0;
            // 预测天数-室内装修完成
            Integer four = 0;

            for (Empj_ProjProgInspection_DTL dtl : dtlList) {
                map.put("1/2", StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" : dtl.getForecastCompleteDate());

                Double limitedAmount = dtl.getForecastNode().getLimitedAmount();
                if (dtl.getForecastNodeName().contains("1/2")) {
                    // 完成主体1/2 = 1/2计算楼层 * 参数1
                    if (nowLimitedAmount - limitedAmount > 0) {

                        one = caNum * parameterOne;

                        dateAddDate = dateUtil.getDateAddDate(forcastTime, one);

                        map.put("1/2", dateAddDate);
                        dtl.setForecastCompleteDate(dateAddDate);

                    }
                }

                if (dtl.getForecastNodeName().contains("封顶")) {
                    map.put("封顶", StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" : dtl.getForecastCompleteDate());
                    /*
                     * 完成主体结构封顶 楼层数：（楼层-当前建设楼层） * 参数一
                     */
                    if (nowLimitedAmount - limitedAmount > 0) {

                        two = (buildCountNum - nowBuildNum) * parameterOne;

                        dateAddDate = dateUtil.getDateAddDate(forcastTime, two);

                        dtl.setForecastCompleteDate(dateAddDate);

                        map.put("封顶", dateAddDate);

                    }
                }

                if (dtl.getForecastNodeName().contains("外立面")) {
                    map.put("外立面", StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" : dtl.getForecastCompleteDate());
                    /*
                     * 完成外立面装饰 楼层数：封顶时间 + 参数二 百分比：巡查时间 +（1-百分比）* 参数二
                     *
                     */
                    if (nowLimitedAmount - limitedAmount > 0) {

                        if ("2".equals(buildProgressType)) {

                            three = parameterTwo * (100 - nowBuildNum) / 100;
                            dateAddDate = dateUtil.getDateAddDate(forcastTime, three);

                            map.put("外立面", dateAddDate);
                            dtl.setForecastCompleteDate(dateAddDate);

                        } else {

                            three = two + parameterTwo;
                            dateAddDate = dateUtil.getDateAddDate(forcastTime, three);

                            map.put("外立面", dateAddDate);
                            dtl.setForecastCompleteDate(dateAddDate);
                        }

                    }
                }

                if (dtl.getForecastNodeName().contains("室内")) {
                    /*
                     * 完成室内装修 楼层数：外立面装饰时间 + 参数三 百分比：巡查时间 +（1-百分比）* 参数三
                     */
                    if (nowLimitedAmount - limitedAmount > 0) {

                        if ("3".equals(buildProgressType)) {

                            if (nowNode.getStageName().contains("外立面")) {
                                four = parameterThree * (100 - nowBuildNum) / 100;
                            } else {
                                four = (parameterThree * (100 - nowBuildNum) / 100) + parameterTwo;
                            }

                            dateAddDate = dateUtil.getDateAddDate(forcastTime, four);

                            dtl.setForecastCompleteDate(dateAddDate);
                        } else {

                            four = three + parameterThree;

                            dateAddDate = dateUtil.getDateAddDate(forcastTime, four);

                            dtl.setForecastCompleteDate(dateAddDate);
                        }

                    }
                }

                dtl.setBuildProgress(String.valueOf(nowBuildNum));
                dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
                dtl.setUserUpdate(model.getUser());
                dtl.setDataSources("工程巡查");
                empj_ProjProgInspection_DTLDao.update(dtl);

            }

        }

        Empj_ProjProgInspection_AFForm afModel = new Empj_ProjProgInspection_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBuildInfo(buildingInfo);
        List<Empj_ProjProgInspection_AF> listAf = empj_ProjProgInspection_AFDao.findByPage(
                empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQLUpdate(), afModel));
        for (Empj_ProjProgInspection_AF afInfo : listAf) {

            afInfo.setBuildProgress(String.valueOf(nowBuildNum));
            /*
             * a)当勾选主体结构时，输入框维护楼层数，与现有校验一致 b)当勾选外面装饰时，输入框维护百分比数字 例：输入数字10 显示10%。
             * c)当勾选室内装修时，输入框维护百分比数字 例：输入数字10 显示10%。
             */
            if ("1".equals(buildProgressType)) {
                afInfo.setBuildProgress("主体结构施工：" + buildProgress + "层");
            } else if ("2".equals(buildProgressType)) {
                afInfo.setBuildProgress("外立面装饰施工：" + buildProgress + "%");
            } else if ("3".equals(buildProgressType)) {
                afInfo.setBuildProgress("室内装修施工：" + buildProgress + "%");
            } else {
                afInfo.setBuildProgress(buildProgress);
            }

            afInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
            afInfo.setUserUpdate(model.getUser());
            afInfo.setDataSources("工程巡查");
            afInfo.setUpdateDateTime(System.currentTimeMillis());
            empj_ProjProgInspection_AFDao.update(afInfo);
        }

        return properties;
    }

    /**
     * 推送工程进度巡查航拍信息
     *
     * @param dtl
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public Boolean pushProjProgForcastPhoto(Empj_ProjProgForcast_AF progForcast, Empj_ProjProgForcast_DTL dtl) {
        /*
         * { "action":"add,edit,del", "cate":"jindu", "ts_bld_id":"托管系统楼栋ID",
         * "ts_id ":"托管系统该条记录ID", "jdtime":"航拍日期", "news_title":"节点日期+楼层+上传类型",
         * "news_title1":"项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型",
         * "smallpic":"批量上传缩略图(4张,分割，首张为总平图)",
         * "image2":"批量上传高清大图(4张,分割，首张为总平图)", "dqlc":"当前楼层（整数类型）" } 报文解释： action
         * 为add,edit,del三种分别为增加、修改和删除三种操作 smallpic和image2如有多张照片通过“，”进行间隔。
         *
         * news_title1： （1）如果楼栋无公安编号形式为：项目名称+施工编号：楼栋号+楼层+上传类型
         *
         * （2）如果楼栋有公安编号形式为：项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型
         *
         * 上传类型目前分类：
         *
         * 常规施工（不显示，未达到外立面装饰进度），外立面装饰，室内装修施工 形式可参考正泰官网：
         * http://www.czzhengtai.com
         * http://www.czzhengtai.com/showld.asp?lpid=1&id=365
         */

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.info("未查询到配置路径！");
            return false;
        }
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        MyDatetime dateUtil = MyDatetime.getInstance();

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setAction("add");
        pushVo.setCate("jindu");
        pushVo.setTs_bld_id(String.valueOf(dtl.getBuildInfo().getTableId()));
        pushVo.setTs_id(String.valueOf(progForcast.getTableId()));
        pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

        // 节点日期+楼层+上传类型
        String news_title = "";
        // 项目名称+施工编号：楼栋号+楼层+上传类型
        String news_title1 = "项目名称+施工编号：";

        Empj_BuildingInfo buildInfo = dtl.getBuildInfo();

        // 施工编号
        String geteCodeFromConstruction = StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? ""
                : buildInfo.geteCodeFromConstruction();
        // 公安编号
        String geteCodeFromPublicSecurity = StrUtil.isBlank(buildInfo.geteCodeFromPublicSecurity()) ? ""
                : buildInfo.geteCodeFromPublicSecurity();

        Double floorUpNumber = null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber();
        String floorUpNumberStr = String.valueOf(floorUpNumber.intValue());

        String dqlc = "";
        if ("1".equals(dtl.getBuildProgressType())) {
            dqlc = dtl.getBuildProgress();
            news_title = "主体结构施工：" + dqlc + "层";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工：" + dqlc + "层";
        } else if ("2".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "外立面装饰施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
                    + dtl.getBuildProgress() + "%";
        } else if ("3".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "室内装修施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
                    + dtl.getBuildProgress() + "%";
        }

        pushVo.setDqlc(dqlc);
        pushVo.setNews_title(news_title);
        pushVo.setNews_title1(news_title1);

        StringBuffer smallBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm), 1, 4);
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        String md5Info;
        String[] split;
        // 遍历总平图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {

            md5Info = sm_AttachmentList.get(i).getMd5Info();
            if (StrUtil.isBlank(md5Info)) {
                return false;
            }

            split = md5Info.split("##");
            if (split.length < 3) {
                return false;
            }

            if (buffer.length() > 0) {
                buffer.append("," + split[1]);
            } else {
                buffer.append(split[1]);
            }

            if (smallBuffer.length() > 0) {
                smallBuffer.append("," + split[2]);
            } else {
                smallBuffer.append(split[2]);
            }

        }

        /*
         * 进行图片相关处理 批量上传缩略图(4张,分割，首张为总平图) 批量上传高清大图(4张,分割，首张为总平图)
         */

        pushVo.setSmallpic(smallBuffer.toString());
        pushVo.setImage2(buffer.toString());

        Gson gson = new Gson();

        String jsonMap = gson.toJson(pushVo);

        System.out.println(jsonMap);

        String decodeStr = Base64Encoder.encode(jsonMap);

        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return true;

    }

    /**
     * 推送楼幢信息提交审核
     *
     * @param dtl
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public Boolean pushSumitProjProgForcast(Empj_ProjProgForcast_AF progForcast, Empj_ProjProgForcast_DTL dtl) {
        /*
         * { "action":"add,edit,del", "cate":"jindu", "ts_bld_id":"托管系统楼栋ID",
         * "ts_id ":"托管系统该条记录ID", "jdtime":"航拍日期", "news_title":"节点日期+楼层+上传类型",
         * "news_title1":"项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型",
         * "smallpic":"批量上传缩略图(4张,分割，首张为总平图)",
         * "image2":"批量上传高清大图(4张,分割，首张为总平图)", "dqlc":"当前楼层（整数类型）" } 报文解释： action
         * 为add,edit,del三种分别为增加、修改和删除三种操作 smallpic和image2如有多张照片通过“，”进行间隔。
         *
         * news_title1： （1）如果楼栋无公安编号形式为：项目名称+施工编号：楼栋号+楼层+上传类型
         *
         * （2）如果楼栋有公安编号形式为：项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型
         *
         * 上传类型目前分类：
         *
         * 常规施工（不显示，未达到外立面装饰进度），外立面装饰，室内装修施工 形式可参考正泰官网：
         * http://www.czzhengtai.com
         * http://www.czzhengtai.com/showld.asp?lpid=1&id=365
         */

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.info("未查询到配置路径！");
            return false;
        }
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        MyDatetime dateUtil = MyDatetime.getInstance();

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setAction("add");
        pushVo.setCate("jindu");
        pushVo.setTs_pj_id(String.valueOf(progForcast.getProject().getTableId()));
        pushVo.setTs_bld_id(String.valueOf(dtl.getBuildInfo().getTableId()));
        pushVo.setTs_id(String.valueOf(dtl.getTableId()));
        pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

        // 节点日期+楼层+上传类型
        String news_title = "";
        // 项目名称+施工编号：楼栋号+楼层+上传类型
        String news_title1 = "项目名称+施工编号：";

        Empj_BuildingInfo buildInfo = dtl.getBuildInfo();

        // 施工编号
        String geteCodeFromConstruction = StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? ""
                : buildInfo.geteCodeFromConstruction();
        // 公安编号
        String geteCodeFromPublicSecurity = StrUtil.isBlank(buildInfo.geteCodeFromPublicSecurity()) ? ""
                : buildInfo.geteCodeFromPublicSecurity();

        Double floorUpNumber = null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber();
        String floorUpNumberStr = String.valueOf(floorUpNumber.intValue());

        String dqlc = "";
        if ("1".equals(dtl.getBuildProgressType())) {
            dqlc = dtl.getBuildProgress();
            news_title = "主体结构施工：" + dqlc + "层";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工：" + dqlc + "层";
        } else if ("2".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "外立面装饰施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
                    + dtl.getBuildProgress() + "%";
        } else if ("3".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "室内装修施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
                    + dtl.getBuildProgress() + "%";
        }

        pushVo.setDqlc(dqlc);
        pushVo.setNews_title(news_title);
        pushVo.setNews_title1(news_title1);

        StringBuffer smallBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm), 1, 4);
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        String md5Info;
        String[] split;
        // 遍历总平图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {

            md5Info = sm_AttachmentList.get(i).getMd5Info();
            if (StrUtil.isBlank(md5Info)) {
                return false;
            }

            split = md5Info.split("##");
            if (split.length < 3) {
                return false;
            }

            if (buffer.length() > 0) {
                buffer.append("," + split[1]);
            } else {
                buffer.append(split[1]);
            }

            if (smallBuffer.length() > 0) {
                smallBuffer.append("," + split[2]);
            } else {
                smallBuffer.append(split[2]);
            }

        }

        /*
         * 进行图片相关处理 批量上传缩略图(4张,分割，首张为总平图) 批量上传高清大图(4张,分割，首张为总平图)
         */

        pushVo.setSmallpic(smallBuffer.toString());
        pushVo.setImage2(buffer.toString());

        Gson gson = new Gson();

        String jsonMap = gson.toJson(pushVo);

        System.out.println(jsonMap);

        String decodeStr = Base64Encoder.encode(jsonMap);

        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 更新楼幢推送状态-已推送
        dtl.setWebPushState("2");
        projProgForcast_DTLDao.update(dtl);

        Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
        manageModel.setTheState(S_TheState.Normal);
        manageModel.setDtlEntity(dtl);
        Empj_ProjProgForcast_Manage forcast_Manage = manageDao
                .findOneByQuery_T(manageDao.getQuery(manageDao.getBasicHQL(), manageModel));
        if (null != forcast_Manage) {
            // 2-已推送
            forcast_Manage.setWebPushState("2");
            forcast_Manage.setLastUpdateTimeStamp(System.currentTimeMillis());
            manageDao.update(forcast_Manage);
        }

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_submit");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return true;

    }

    /**
     * 推送楼幢信息提交审核
     *
     * @param dtl
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public Boolean pushSumitBuildProjProgForcast(Empj_ProjProgForcast_AF progForcast, Empj_ProjProgForcast_DTL dtl) {
        /*
         * { "action":"add,edit,del", "cate":"jindu", "ts_bld_id":"托管系统楼栋ID",
         * "ts_id ":"托管系统该条记录ID", "jdtime":"航拍日期", "news_title":"节点日期+楼层+上传类型",
         * "news_title1":"项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型",
         * "smallpic":"批量上传缩略图(4张,分割，首张为总平图)",
         * "image2":"批量上传高清大图(4张,分割，首张为总平图)", "dqlc":"当前楼层（整数类型）" } 报文解释： action
         * 为add,edit,del三种分别为增加、修改和删除三种操作 smallpic和image2如有多张照片通过“，”进行间隔。
         *
         * news_title1： （1）如果楼栋无公安编号形式为：项目名称+施工编号：楼栋号+楼层+上传类型
         *
         * （2）如果楼栋有公安编号形式为：项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型
         *
         * 上传类型目前分类：
         *
         * 常规施工（不显示，未达到外立面装饰进度），外立面装饰，室内装修施工 形式可参考正泰官网：
         * http://www.czzhengtai.com
         * http://www.czzhengtai.com/showld.asp?lpid=1&id=365
         */

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.info("未查询到配置路径！");
            return false;
        }
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        MyDatetime dateUtil = MyDatetime.getInstance();

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setAction("add");
        pushVo.setCate("jindu");
        pushVo.setTs_pj_id(String.valueOf(progForcast.getProject().getTableId()));
        pushVo.setTs_bld_id(String.valueOf(dtl.getBuildInfo().getTableId()));
        pushVo.setTs_id(String.valueOf(dtl.getTableId()));
        pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

        // 节点日期+楼层+上传类型
        String news_title = "";
        // 项目名称+施工编号：楼栋号+楼层+上传类型
        String news_title1 = "项目名称+施工编号：";

        Empj_BuildingInfo buildInfo = dtl.getBuildInfo();

        // 施工编号
        String geteCodeFromConstruction = StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? ""
                : buildInfo.geteCodeFromConstruction();
        // 公安编号
        String geteCodeFromPublicSecurity = StrUtil.isBlank(buildInfo.geteCodeFromPublicSecurity()) ? ""
                : buildInfo.geteCodeFromPublicSecurity();

        Double floorUpNumber = null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber();
        String floorUpNumberStr = String.valueOf(floorUpNumber.intValue());

        String dqlc = "";
        if ("1".equals(dtl.getBuildProgressType())) {
            dqlc = dtl.getBuildProgress();
            news_title = "主体结构施工：" + dqlc + "层";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工：" + dqlc + "层";
        } else if ("2".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "外立面装饰施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
                    + dtl.getBuildProgress() + "%";
        } else if ("3".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = "室内装修施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
                    + dtl.getBuildProgress() + "%";
        }

        pushVo.setDqlc(dqlc);
        pushVo.setNews_title(news_title);
        pushVo.setNews_title1(news_title1);

        StringBuffer smallBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm), 1, 4);
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        String md5Info;
        String[] split;
        // 遍历总平图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {

            md5Info = sm_AttachmentList.get(i).getMd5Info();
            if (StrUtil.isBlank(md5Info)) {
                return false;
            }

            split = md5Info.split("##");
            if (split.length < 3) {
                return false;
            }

            if (buffer.length() > 0) {
                buffer.append("," + split[1]);
            } else {
                buffer.append(split[1]);
            }

            if (smallBuffer.length() > 0) {
                smallBuffer.append("," + split[2]);
            } else {
                smallBuffer.append(split[2]);
            }

        }

        /*
         * 进行图片相关处理 批量上传缩略图(4张,分割，首张为总平图) 批量上传高清大图(4张,分割，首张为总平图)
         */

        pushVo.setSmallpic(smallBuffer.toString());
        pushVo.setImage2(buffer.toString());

        Gson gson = new Gson();

        String jsonMap = gson.toJson(pushVo);

        System.out.println(jsonMap);

        String decodeStr = Base64Encoder.encode(jsonMap);

        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_submitBuild");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return true;

    }

    /**
     * 推送项目信息审核
     *
     * @param progForcast
     * @param
     * @return 1、接口描述 楼盘提交时向网站发送提交信息。 2、发送URL和约定参数
     * URL: http://apits.czzhengtai.com:811/   3、报文格式 发送报文格式    {
     * "cate":"pjsptj", "ts_id":"ts_id  单据号", "ts_pj_id":"托管系统项目号" }
     * 4、报文加密 参考接口加密文档
     */
    public Boolean pushProjProgProject(Empj_ProjProgForcast_AF progForcast) {

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.info("未查询到配置路径！");
            return false;
        }
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setCate("pjsptj");
        pushVo.setTs_pj_id(String.valueOf(progForcast.getProject().getTableId()));
        pushVo.setTs_id(String.valueOf(progForcast.getTableId()));
        pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

        Gson gson = new Gson();
        String jsonMap = gson.toJson(pushVo);
        System.out.println(jsonMap);
        String decodeStr = Base64Encoder.encode(jsonMap);
        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_PROJECT");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return true;

    }

    /**
     * 推送工程进度巡查航拍信息
     *
     * @param dtl
     * @return
     */
    @SuppressWarnings({"unchecked", "static-access"})
    public Boolean pushProjProgForcastPhotoBak(Empj_ProjProgForcast_AF progForcast, Empj_ProjProgForcast_DTL dtl) {
        /*
         * { "action":"add,edit,del", "cate":"jindu", "ts_bld_id":"托管系统楼栋ID",
         * "ts_id ":"托管系统该条记录ID", "jdtime":"航拍日期", "news_title":"节点日期+楼层+上传类型",
         * "news_title1":"项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型",
         * "smallpic":"批量上传缩略图(4张,分割，首张为总平图)",
         * "image2":"批量上传高清大图(4张,分割，首张为总平图)", "dqlc":"当前楼层（整数类型）" } 报文解释： action
         * 为add,edit,del三种分别为增加、修改和删除三种操作 smallpic和image2如有多张照片通过“，”进行间隔。
         *
         * news_title1： （1）如果楼栋无公安编号形式为：项目名称+施工编号：楼栋号+楼层+上传类型
         *
         * （2）如果楼栋有公安编号形式为：项目名称+施工编号：楼栋号+公安编号：楼栋号+楼层+上传类型
         *
         * 上传类型目前分类：
         *
         * 常规施工（不显示，未达到外立面装饰进度），外立面装饰，室内装修施工 形式可参考正泰官网：
         * http://www.czzhengtai.com
         * http://www.czzhengtai.com/showld.asp?lpid=1&id=365
         */
        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            return false;
        }

        // 查询水印图片位置
        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("69003");
        baseParameterForm.setParametertype("69");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

        if (null == baseParameter) {
            log.equals("未查询到水印图片位置！");

            return false;
        }

        // 查询缩放比列
        Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
        baseParameterForm2.setTheState(S_TheState.Normal);
        baseParameterForm2.setTheValue("69001");
        baseParameterForm2.setParametertype("69");
        Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

        if (null == baseParameter2) {
            log.equals("未查询到缩放限定宽度！");

            return false;
        }

        Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
        baseParameterForm3.setTheState(S_TheState.Normal);
        baseParameterForm3.setTheValue("69002");
        baseParameterForm3.setParametertype("69");
        Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

        if (null == baseParameter3) {
            log.equals("未查询到缩放限定高度！");

            return false;
        }

        MyDatetime dateUtil = MyDatetime.getInstance();

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setAction("add");
        pushVo.setCate("jindu");
        pushVo.setTs_bld_id(String.valueOf(dtl.getBuildInfo().getTableId()));
        pushVo.setTs_id(String.valueOf(progForcast.getTableId()));
        pushVo.setJdtime(StrUtil.isBlank(progForcast.getForcastTime()) ? "" : progForcast.getForcastTime());

        // 节点日期+楼层+上传类型
        String news_title = "";
        // 项目名称+施工编号：楼栋号+楼层+上传类型
        String news_title1 = "项目名称+施工编号：";

        Empj_BuildingInfo buildInfo = dtl.getBuildInfo();

        // 施工编号
        String geteCodeFromConstruction = StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? ""
                : buildInfo.geteCodeFromConstruction();
        // 公安编号
        String geteCodeFromPublicSecurity = StrUtil.isBlank(buildInfo.geteCodeFromPublicSecurity()) ? ""
                : buildInfo.geteCodeFromPublicSecurity();

        Double floorUpNumber = null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber();
        String floorUpNumberStr = String.valueOf(floorUpNumber.intValue());

        String dqlc = "";
        if ("1".equals(dtl.getBuildProgressType())) {
            dqlc = dtl.getBuildProgress();
            news_title = progForcast.getForcastTime() + " 主体结构施工： " + floorUpNumberStr + "层";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工： "
                    + floorUpNumberStr + "层";
        } else if ("2".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = progForcast.getForcastTime() + " 外立面装饰施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
                    + dtl.getBuildProgress() + "%";
        } else if ("3".equals(dtl.getBuildProgressType())) {
            dqlc = floorUpNumberStr;
            news_title = progForcast.getForcastTime() + " 室内装修施工：" + dtl.getBuildProgress() + "%";
            news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
                    + dtl.getBuildProgress() + "%";
        }

        pushVo.setDqlc(dqlc);
        pushVo.setNews_title(news_title);
        pushVo.setNews_title1(news_title1);

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        List<String> attachmentList = new ArrayList<>();

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm));
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        // 遍历总平图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {
            if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
                attachmentList.add(sm_AttachmentList.get(i).getTheLink());
            }
        }

        // 遍历其他图片（总共四张）
        for (int i = 0; i < sm_AttachmentList.size(); i++) {

            if (attachmentList.contains(sm_AttachmentList.get(i).getTheLink())) {
                continue;
            }

            if (attachmentList.size() > 3) {
                break;
            }

            attachmentList.add(sm_AttachmentList.get(i).getTheLink());
        }

        /*
         * 进行图片相关处理 批量上传缩略图(4张,分割，首张为总平图) 批量上传高清大图(4张,分割，首张为总平图)
         */
        MatrixUtil picUtil = new MatrixUtil();
        StringBuffer smallBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();

        /*
         * 高清图加水印 缩略图加水印并压缩
         */
        String[] deskURL;
        for (String theLink : attachmentList) {

            deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(), baseParameter2.getTheName(),
                    baseParameter3.getTheName());

            if (StrUtil.isNotBlank(deskURL[0])) {
                ReceiveMessage upload = ossUtil.upload(deskURL[0]);
                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                    if (buffer.length() > 0) {
                        buffer.append("," + upload.getData().get(0).getUrl());
                    } else {
                        buffer.append(upload.getData().get(0).getUrl());
                    }

                    picUtil.deleteFile(deskURL[0]);
                }
            }

            if (StrUtil.isNotBlank(deskURL[1])) {
                ReceiveMessage upload = ossUtil.upload(deskURL[1]);
                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                    if (smallBuffer.length() > 0) {
                        smallBuffer.append("," + upload.getData().get(0).getUrl());
                    } else {
                        smallBuffer.append(upload.getData().get(0).getUrl());
                    }

                    picUtil.deleteFile(deskURL[1]);
                }
            }

        }

        pushVo.setSmallpic(smallBuffer.toString());
        pushVo.setImage2(buffer.toString());

        Gson gson = new Gson();

        String jsonMap = gson.toJson(pushVo);

        System.out.println(jsonMap);

        String decodeStr = Base64Encoder.encode(jsonMap);

        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return true;

    }

    /**
     * 推送交付备案数据
     *
     * @param bldEscrowCompleted
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean pushBldEscrowCompleted(Empj_BldEscrowCompleted bldEscrowCompleted) {

        To_BldEscrowCompleted pushVo;
        Gson gson = new Gson();

        String jdtime = MyDatetime.getInstance().dateToString(bldEscrowCompleted.getRecordTimeStamp());
        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            System.out.println("未查询到配置路径！");

            return false;
        }

        String pdfUrl = getPdfUrl(String.valueOf(bldEscrowCompleted.getTableId()));
        if (StrUtil.isBlank(pdfUrl)) {
            System.out.println("未查询到PDF路径！");
            return false;
        }

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(bldEscrowCompleted.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030102");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        String gswz_picurl = "";
        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            System.out.println("未查询到公示截图！");
            return false;
        }

        // 遍历公示截图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {
            if (sm_AttachmentList.get(i).getAttachmentCfg().getTheName().contains("截图")) {
                gswz_picurl = sm_AttachmentList.get(i).getTheLink();
            }
        }

        List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList = bldEscrowCompleted
                .getEmpj_BldEscrowCompleted_DtlList();
        if (null == empj_BldEscrowCompleted_DtlList || empj_BldEscrowCompleted_DtlList.isEmpty()) {
            System.out.println("子表信息为空！");
            return false;
        }

        for (Empj_BldEscrowCompleted_Dtl empj_BldEscrowCompleted_Dtl : empj_BldEscrowCompleted_DtlList) {

            pushVo = new To_BldEscrowCompleted();

            pushVo.setAction("add");
            pushVo.setCate("ldjd");
            pushVo.setJdzt("托管终止交付备案");
            pushVo.setJdtime(jdtime);

            pushVo.setTs_bld_id(String.valueOf(empj_BldEscrowCompleted_Dtl.getBuilding().getTableId()));
            pushVo.setTs_id(String.valueOf(empj_BldEscrowCompleted_Dtl.getTableId()));
            pushVo.setGswz_url(bldEscrowCompleted.getWebSite());

            // 公安编号对照表
            pushVo.setGabhdzb(pdfUrl);

            // 公示截图
            pushVo.setGswz_picurl(gswz_picurl);

            String jsonMap = gson.toJson(pushVo);
            System.out.println(jsonMap);
            String decodeStr = Base64Encoder.encode(jsonMap);
            System.out.println(decodeStr);
            ToInterface toFace = new ToInterface();
            boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());
            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
            tgpf_SocketMsg.setTheState(S_TheState.Normal);
            tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setMsgStatus(1);
            tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
            tgpf_SocketMsg.setMsgDirection("ZT_TO_MH——Empj_BldEscrowCompleted");
            tgpf_SocketMsg.setMsgContentArchives(jsonMap);
            if (interfaceUtil) {
                tgpf_SocketMsg.setReturnCode("200");
            } else {
                tgpf_SocketMsg.setReturnCode("300");
            }
            tgpf_SocketMsgDao.save(tgpf_SocketMsg);

            if (!interfaceUtil) {
                return false;
            }
        }

        return true;
    }

    public String getPdfUrl(String sourceId) {
        String attacmentcfg = "240180";

        // 根据业务编号查询配置文件
        Sm_AttachmentCfgForm form1 = new Sm_AttachmentCfgForm();
        form1.setTheState(S_TheState.Normal);
        form1.setBusiType(attacmentcfg);

        Sm_AttachmentCfg sm_AttachmentCfg = attacmentcfgDao
                .findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form1));

        if (null == sm_AttachmentCfg) {
            return null;
        }

        Sm_AttachmentForm form = new Sm_AttachmentForm();
        form.setSourceId(sourceId);
        form.setBusiType("240180");
        form.setSourceType(sm_AttachmentCfg.geteCode());
        form.setTheState(S_TheState.Normal);
        form.setAttachmentCfg(sm_AttachmentCfg);

        Sm_Attachment attachment = sm_AttachmentDao
                .findOneByQuery_T(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), form));

        if (null == attachment) {
            return null;
        }
        return attachment.getTheLink();
    }

    /**
     * 测试图片在服务器能否正常处理
     *
     * @param model
     * @return
     */
    @SuppressWarnings({"static-access", "unchecked"})
    public Properties photoHandle(CommonForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            return MyBackInfo.fail(properties, "1111");
        }

        // 查询水印图片位置
        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("69003");
        baseParameterForm.setParametertype("69");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

        if (null == baseParameter) {
            return MyBackInfo.fail(properties, "2222");
        }

        // 查询缩放比列
        Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
        baseParameterForm2.setTheState(S_TheState.Normal);
        baseParameterForm2.setTheValue("69001");
        baseParameterForm2.setParametertype("69");
        Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

        if (null == baseParameter2) {
            return MyBackInfo.fail(properties, "3333");
        }

        Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
        baseParameterForm3.setTheState(S_TheState.Normal);
        baseParameterForm3.setTheValue("69002");
        baseParameterForm3.setParametertype("69");
        Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

        if (null == baseParameter3) {
            return MyBackInfo.fail(properties, "44444");
        }

        MyDatetime dateUtil = MyDatetime.getInstance();

        // 节点日期+楼层+上传类型
        String news_title = "";
        // 项目名称+施工编号：楼栋号+楼层+上传类型
        String news_title1 = "项目名称+施工编号：";

        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(model.getTableId().toString());
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        List<String> attachmentList = new ArrayList<>();

        // 加载所有楼幢下的相关附件信息
        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm));
        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
            sm_AttachmentList = new ArrayList<Sm_Attachment>();
        }

        // 遍历总平图
        for (int i = 0; i < sm_AttachmentList.size(); i++) {
            if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
                attachmentList.add(sm_AttachmentList.get(i).getTheLink());
            }
        }

        // 遍历其他图片（总共四张）
        for (int i = 0; i < sm_AttachmentList.size(); i++) {

            if (attachmentList.contains(sm_AttachmentList.get(i).getTheLink())) {
                continue;
            }

            if (attachmentList.size() > 3) {
                break;
            }

            attachmentList.add(sm_AttachmentList.get(i).getTheLink());
        }

        /*
         * 进行图片相关处理 批量上传缩略图(4张,分割，首张为总平图) 批量上传高清大图(4张,分割，首张为总平图)
         */
        MatrixUtil picUtil = new MatrixUtil();
        StringBuffer smallBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();

        /*
         * 高清图加水印 缩略图加水印并压缩
         */
        String[] deskURL;
        for (String theLink : attachmentList) {

            deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(), baseParameter2.getTheName(),
                    baseParameter3.getTheName());

            if (StrUtil.isNotBlank(deskURL[0])) {
                ReceiveMessage upload = ossUtil.upload(deskURL[0]);
                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                    if (buffer.length() > 0) {
                        buffer.append("," + upload.getData().get(0).getUrl());
                    } else {
                        buffer.append(upload.getData().get(0).getUrl());
                    }

                    picUtil.deleteFile(deskURL[0]);
                }
            }

            if (StrUtil.isNotBlank(deskURL[1])) {
                ReceiveMessage upload = ossUtil.upload(deskURL[1]);
                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                    if (smallBuffer.length() > 0) {
                        smallBuffer.append("," + upload.getData().get(0).getUrl());
                    } else {
                        smallBuffer.append(upload.getData().get(0).getUrl());
                    }

                    picUtil.deleteFile(deskURL[1]);
                }
            }

        }

        properties.put("smallpic", smallBuffer.toString());
        properties.put("Image2", buffer.toString());

        return properties;

    }

    /**
     * 获取预售合同
     *
     * @param model
     * @return
     */
    public Properties getContract(CommonForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("900001");
        baseParameterForm0.setParametertype("90");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));
        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            return MyBackInfo.fail(properties, "未查询到配置路径！");
        }

        // 查询token
        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("900002");
        baseParameterForm.setParametertype("90");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
        if (null == baseParameter) {
            log.equals("未查询到token信息！");

            return MyBackInfo.fail(properties, "未查询到token信息！");
        }

        String contractNO = model.getContractNO();
        if (StrUtil.isBlank(contractNO)) {
            return MyBackInfo.fail(properties, "请输入合同备案号查询！");
        }

        Map<String, String> map = new HashMap<>();
        map.put("ContractNO", contractNO);
        map.put("token", baseParameter.getTheName());
        String jsonString = JSONObject.toJSONString(map);

        System.out.println(jsonString);

        String httpStringPostRequest = "";
        try {
            httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(baseParameter0.getTheName(),
                    jsonString);
        } catch (Exception e) {
            httpStringPostRequest = "fail";
            log.error("exception-jsonString:" + jsonString);
        }

        System.out.println(httpStringPostRequest);

        ResponseResult response = JSONObject.parseObject(httpStringPostRequest, ResponseResult.class);

        System.out.println(response.toString());
        System.out.println(response.getResult());
        System.out.println(response.getSuccess());

        if ("true".equals(response.getSuccess())) {

            ResponseContractResult contract = JSONObject.parseObject(httpStringPostRequest,
                    ResponseContractResult.class);

            ResponseContract result = contract.getResult();

            System.out.println("合同信息：" + result.toString());
        } else {
            return MyBackInfo.fail(properties, (String) response.getResult());
        }

        return properties;

    }

    @Autowired
    private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
    @Autowired
    private Empj_HouseInfoDao empj_HouseInfoDao;

    /**
     * 获取预售合同
     *
     * @param model
     * @return
     */
    public Properties queryContractInfo(Tb_b_contractFrom model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String contractNO = model.getBeianno();
        if (StrUtil.isBlank(contractNO)) {
            return MyBackInfo.fail(properties, "请输入合同备案号查询！");
        }

        /*
         * 校验是否存在与此合同备案号相关且有效的三方协议
         *
         * 三方协议有效字段：0,1,2 0-为空（默认） 1-生效（代理公司上传三方协议和商品房买卖合同签字页后）
         * 2-退房退款待处理（退房退款流程发起时标记三方协议状态为待处理，） 3-失效：
         */
        Tgxy_TripleAgreementForm tgxy_TripleAgreementmodel = new Tgxy_TripleAgreementForm();
        tgxy_TripleAgreementmodel.seteCodeOfContractRecord(contractNO);
        tgxy_TripleAgreementmodel.setTheState(S_TheState.Normal);

        Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao
                .getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementmodel));
        if (totalCount > 0) {
            return MyBackInfo.fail(properties, "合同备案号：" + contractNO + "，已签署过相关三方协议");
        }

        /*
         * 检查当前登录人员是否具有提取合同的权限 1.由合同加载出户室信息，根据户室信息查询所属楼幢、项目、开发企业
         * 2.再根据当前登录人员的授权信息查询比较是否具有权限提取
         *
         */
        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
        }
        Emmp_CompanyInfo company = user.getCompany();
        if (null == company) {
            return MyBackInfo.fail(properties, "未查询到当前登录人员所属企业信息！");
        }

        // 合同信息
        Tb_b_contract tb_b_contractDetail;

        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("900001");
        baseParameterForm0.setParametertype("90");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));
        if (null == baseParameter0) {
            log.equals("未查询到配置路径！");

            return MyBackInfo.fail(properties, "未查询到配置路径！");
        }

        // 查询token
        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("900002");
        baseParameterForm.setParametertype("90");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
        if (null == baseParameter) {
            log.equals("未查询到token信息！");

            return MyBackInfo.fail(properties, "未查询到token信息！");
        }

        Map<String, String> map = new HashMap<>();
        map.put("contractNO", contractNO);
        map.put("token", baseParameter.getTheName());
        String jsonString = JSONObject.toJSONString(map);

        System.out.println(jsonString);

        String httpStringPostRequest = "";
        try {
            httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(baseParameter0.getTheName(),
                    jsonString);
        } catch (Exception e) {
            httpStringPostRequest = "fail";
            log.error("exception-jsonString:" + jsonString);
        }

        System.out.println(httpStringPostRequest);

        ResponseResult response = JSONObject.parseObject(httpStringPostRequest, ResponseResult.class);

        System.out.println(response.toString());
        System.out.println(response.getResult());
        System.out.println(response.getSuccess());

        if ("true".equals(response.getSuccess())) {
            try {
                ResponseContractResult contract = JSONObject.parseObject(httpStringPostRequest,
                        ResponseContractResult.class);

                ResponseContract result = contract.getResult();

                tb_b_contractDetail = new Tb_b_contract();
                tb_b_contractDetail.setBeianno(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                tb_b_contractDetail.setRoomlocation(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
//                tb_b_contractDetail.setMsr(StrUtil.isBlank(result.getBuyer()) ? "" : result.getBuyer());
                tb_b_contractDetail.setMsr(StrUtil.isBlank(result.getBuyer()) ? "" : result.getBuyer().replaceAll("[\\t\\n\\r]", ""));
                tb_b_contractDetail.setCmr(StrUtil.isBlank(result.getSeller()) ? "" : result.getSeller());
                tb_b_contractDetail.setProjectid(StrUtil.isBlank(result.getProjectId()) ? "" : result.getProjectId());
                tb_b_contractDetail
                        .setBuildingid(StrUtil.isBlank(result.getBuildingId()) ? "" : result.getBuildingId());
                tb_b_contractDetail.setRoomid(StrUtil.isBlank(result.getRoomId()) ? "" : result.getRoomId());
                tb_b_contractDetail
                        .setQdwctime(StrUtil.isBlank(result.getSigningDate()) ? "" : result.getSigningDate());
                tb_b_contractDetail.setQdtime(StrUtil.isBlank(result.getSigningDate()) ? "" : result.getSigningDate());
                tb_b_contractDetail.setContractprice(
                        StrUtil.isBlank(result.getContractAmount()) ? "" : result.getContractAmount());
                tb_b_contractDetail.setMj(StrUtil.isBlank(result.getRoomArea()) ? "" : result.getRoomArea());

                /*
                 * {tableId:"1",theName:"一次性付款"}, {tableId:"2",theName:"分期付款"},
                 * {tableId:"3",theName:"贷款方式付款"}, {tableId:"4",theName:"其他方式"}
                 */
                String fkfs = StrUtil.isBlank(result.getPaymentMethod()) ? "" : result.getPaymentMethod();
                switch (fkfs) {
                    case "一次性付款":
                        tb_b_contractDetail.setFkfs("1");
                        break;
                    case "分期付款":
                        tb_b_contractDetail.setFkfs("2");
                        break;
                    case "贷款方式付款":
                        tb_b_contractDetail.setFkfs("3");
                        break;
                    case "其他方式":
                        tb_b_contractDetail.setFkfs("4");
                        break;

                    default:
                        tb_b_contractDetail.setFkfs("3");
                        break;
                }
                tb_b_contractDetail.setHtbh(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                tb_b_contractDetail.setJfrq(StrUtil.isBlank(result.getDeliveryDate()) ? "" : result.getDeliveryDate());
                tb_b_contractDetail.setSfk(StrUtil.isBlank(result.getDownPayment()) ? "0" : result.getDownPayment());

                String roomid = tb_b_contractDetail.getRoomid();

                // 户室信息
                Empj_HouseInfo house;
                Empj_HouseInfoForm form3 = new Empj_HouseInfoForm();
                form3.setExternalId(roomid);
                Object query3 = empj_HouseInfoDao
                        .findOneByQuery(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getDetailHQL(), form3));
                if (null != query3) {
                    house = (Empj_HouseInfo) query3;

                    Long tableId = house.getTableId();

                    tb_b_contractDetail.setRoomid(String.valueOf(tableId));

                    // 通过查询出的户室信息查询出楼幢id、项目id
                    Empj_BuildingInfo building = house.getBuilding();
                    if (null == building || null == building.getTableId() || building.getTableId() <= 0) {
                        return MyBackInfo.fail(properties, "未查询到楼幢信息");
                    }
                    //查询楼栋是否存在
                    if (null == building.getTheState() || "1".equals(building.getTheState()+"")) {
                        return MyBackInfo.fail(properties, "未查询到楼幢信息");
                    }

                    // 判断楼幢的托管状态
                    Empj_BuildingExtendInfo extendInfo = building.getExtendInfo();
                    if (S_EscrowState.UnEscrowState.equals(extendInfo.getEscrowState())) {
                        return MyBackInfo.fail(properties, "楼幢信息未托管");
                    }

                    tb_b_contractDetail.setBuildingid(String.valueOf(building.getTableId()));

                    Empj_ProjectInfo project = building.getProject();
                    if (null == project || null == project.getTableId() || project.getTableId() <= 0) {
                        return MyBackInfo.fail(properties, "未查询到项目信息");
                    }
                    tb_b_contractDetail.setProjectid(String.valueOf(project.getTableId()));

                    // 校验提取权限
                    properties = getIsExtract(model, properties, company, house, project, building);
                    if (!MyBackInfo.isSuccess(properties)) {
                        return properties;
                    }

                } else {
                    return MyBackInfo.fail(properties, "楼盘表有变更，关联户室Id:" + roomid + "，未查询到相关信息");
                }

                properties.put("tb_b_contract", tb_b_contractDetail);

                /*
                 * 买受人信息
                 */
                List<Tb_b_personofcontract> tb_b_personofcontractList = new ArrayList<>();
                if (result.getBuyerName().contains(",")) {
                    String[] names = result.getBuyerName().split(",");
                    String[] cardnos = result.getBuyerCardNo().split(",");
                    String[] phones = result.getBuyerPhone().split(",");
                    for (int i = 0; i < names.length; i++) {
                        Tb_b_personofcontract person = new Tb_b_personofcontract();
                        /*
                         * xsz by time 2018-11-27 09:16:48 录入之前判断手机号是否有被分割
                         * 根据手机号的分割情况进行数据录入
                         */
                        person.setContactAdress(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
                        person.setAgentAddress(
                                StrUtil.isBlank(result.getBuyerAddress()) ? "" : result.getBuyerAddress());
                        person.setCertificateType("1");
                        person.setContractId(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                        person.setECodeOfContract(
                                StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());

                        person.setBuyerName(names[i]);

                        if (cardnos.length < names.length) {
                            person.setECodeOfcertificate(cardnos[0]);
                        } else {
                            person.setECodeOfcertificate(cardnos[i]);
                        }

                        if (phones.length < names.length) {
                            person.setContactPhone(phones[0]);
                        } else {
                            person.setContactPhone(phones[i]);
                        }

                        tb_b_personofcontractList.add(person);
                    }

                } else {
                    Tb_b_personofcontract person = new Tb_b_personofcontract();
                    /*
                     * xsz by time 2018-11-27 09:16:48 录入之前判断手机号是否有被分割
                     * 根据手机号的分割情况进行数据录入
                     */
                    person.setContactAdress(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
                    person.setAgentAddress(StrUtil.isBlank(result.getBuyerAddress()) ? "" : result.getBuyerAddress());
                    person.setCertificateType("1");
                    person.setContractId(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                    person.setECodeOfContract(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());

                    person.setBuyerName(StrUtil.isBlank(result.getBuyerName()) ? "" : result.getBuyerName());

                    person.setECodeOfcertificate(
                            StrUtil.isBlank(result.getBuyerCardNo()) ? "" : result.getBuyerCardNo());
                    person.setContactPhone(StrUtil.isBlank(result.getBuyerPhone()) ? "" : result.getBuyerPhone());

                    tb_b_personofcontractList.add(person);
                }

                properties.put("buyerList", tb_b_personofcontractList);

                System.out.println("合同信息：" + result.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return MyBackInfo.fail(properties, e.getMessage());
            }

        } else {

            if ("查无此记录！".equals((String) response.getResult())) {
                return MyBackInfo.fail(properties, "未查询到合同，请核实合同是否输入有误。");
            } else {
                return MyBackInfo.fail(properties, (String) response.getResult());
            }

        }

        String info = (String) properties.get(S_NormalFlag.info);
        if (info.contains("not return a unique result")) {
            properties.put(S_NormalFlag.info, "楼盘表信息重复，请联系维护人员");
        }

        return properties;

    }

    private Properties getIsExtract(Tb_b_contractFrom model, Properties properties, Emmp_CompanyInfo company,
                                    Empj_HouseInfo houseInfo, Empj_ProjectInfo projectInfo, Empj_BuildingInfo buildingInfo) {

        properties.put(S_NormalFlag.result, S_NormalFlag.success);

        if (S_CompanyType.Agency.equals(company.getTheType())) {
            // 代理公司，根据授权判断
            Long[] projectInfoIdArr = model.getProjectInfoIdArr();// 项目权限

            boolean isExtract = false;

            if (projectInfoIdArr.length > 0) {
                for (Long long1 : projectInfoIdArr) {
                    if (long1.equals(houseInfo.getBuilding().getProject().getTableId())) {
                        isExtract = true;
                        break;
                    }
                }
            }
            if (!isExtract) {
                Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();// 楼幢权限
                if (buildingInfoIdIdArr.length > 0) {
                    for (Long long1 : buildingInfoIdIdArr) {
                        if (long1.equals(houseInfo.getBuilding().getTableId())) {
                            isExtract = true;
                            break;
                        }
                    }
                }
            }

            if (!isExtract) {
                Long[] cityRegionInfoIdArr = model.getCityRegionInfoIdArr();// 区域权限
                if (projectInfoIdArr.length > 0) {
                    for (Long long1 : cityRegionInfoIdArr) {
                        if (long1.equals(houseInfo.getBuilding().getCityRegion().getTableId())) {
                            isExtract = true;
                            break;
                        }
                    }
                }
            }

            if (!isExtract) {
                return MyBackInfo.fail(properties, "该用户无权限提取此合同");
            }

        } else if (S_CompanyType.Development.equals(company.getTheType())) {
            // 开发企业，加载本企业下的信息
            if (!company.getTableId().equals(projectInfo.getDevelopCompany().getTableId())) {
                return MyBackInfo.fail(properties, "该用户无权限提取此合同");
            }
        } else if (S_CompanyType.Zhengtai.equals(company.getTheType())) {
            // 正泰用户，加载全部

        } else {
            return MyBackInfo.fail(properties, "该用户无权限提取此合同");
        }

        return properties;
    }

    /**
     * 初始化审批待办
     *
     * @param
     * @return
     */
    public Properties updateAfStateExecute() {
        Properties properties = new MyProperties();

        try {
            sm_BaseParameterDao.updateAfState();
        } catch (SQLException e) {
            System.out.println("Handler ApprovalProcess Exception :" + e.getMessage());
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 获取预售合同 直接以json字符串输入
     *
     * @param model
     * @return
     */
    public Properties queryContractInfoForJson(Tb_b_contractFrom model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String contractNO = model.getBeianno();
        if (StrUtil.isBlank(contractNO)) {
            return MyBackInfo.fail(properties, "请输入合同备案号查询！");
        }

        /*
         * 检查当前登录人员是否具有提取合同的权限 1.由合同加载出户室信息，根据户室信息查询所属楼幢、项目、开发企业
         * 2.再根据当前登录人员的授权信息查询比较是否具有权限提取
         *
         */
        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
        }
        Emmp_CompanyInfo company = user.getCompany();
        if (null == company) {
            return MyBackInfo.fail(properties, "未查询到当前登录人员所属企业信息！");
        }

        // 合同信息
        Tb_b_contract tb_b_contractDetail;

        try {
            ResponseContractResult contract = JSONObject.parseObject(model.getJsonStr(), ResponseContractResult.class);

            ResponseContract result = contract.getResult();

            tb_b_contractDetail = new Tb_b_contract();
            tb_b_contractDetail.setBeianno(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
            tb_b_contractDetail.setRoomlocation(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
            tb_b_contractDetail.setMsr(StrUtil.isBlank(result.getBuyer()) ? "" : result.getBuyer());
            tb_b_contractDetail.setCmr(StrUtil.isBlank(result.getSeller()) ? "" : result.getSeller());
            tb_b_contractDetail.setProjectid(StrUtil.isBlank(result.getProjectId()) ? "" : result.getProjectId());
            tb_b_contractDetail.setBuildingid(StrUtil.isBlank(result.getBuildingId()) ? "" : result.getBuildingId());
            tb_b_contractDetail.setRoomid(StrUtil.isBlank(result.getRoomId()) ? "" : result.getRoomId());
            tb_b_contractDetail.setQdwctime(StrUtil.isBlank(result.getSigningDate()) ? "" : result.getSigningDate());
            tb_b_contractDetail.setQdtime(StrUtil.isBlank(result.getSigningDate()) ? "" : result.getSigningDate());
            tb_b_contractDetail
                    .setContractprice(StrUtil.isBlank(result.getContractAmount()) ? "" : result.getContractAmount());
            tb_b_contractDetail.setMj(StrUtil.isBlank(result.getRoomArea()) ? "" : result.getRoomArea());

            String fkfs = StrUtil.isBlank(result.getPaymentMethod()) ? "" : result.getPaymentMethod();
            switch (fkfs) {
                case "一次性付款":
                    tb_b_contractDetail.setFkfs("1");
                    break;
                case "分期付款":
                    tb_b_contractDetail.setFkfs("2");
                    break;
                case "贷款方式付款":
                    tb_b_contractDetail.setFkfs("3");
                    break;
                case "其他方式":
                    tb_b_contractDetail.setFkfs("4");
                    break;

                default:
                    tb_b_contractDetail.setFkfs("3");
                    break;
            }
            tb_b_contractDetail.setHtbh(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
            tb_b_contractDetail.setJfrq(StrUtil.isBlank(result.getDeliveryDate()) ? "" : result.getDeliveryDate());
            tb_b_contractDetail.setSfk(StrUtil.isBlank(result.getDownPayment()) ? "0" : result.getDownPayment());

            String roomid = tb_b_contractDetail.getRoomid();

            // 户室信息
            Empj_HouseInfo house;
            Empj_HouseInfoForm form3 = new Empj_HouseInfoForm();
            form3.setExternalId(roomid);
            Object query3 = empj_HouseInfoDao
                    .findOneByQuery(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getDetailHQL(), form3));
            if (null != query3) {
                house = (Empj_HouseInfo) query3;

                Long tableId = house.getTableId();

                tb_b_contractDetail.setRoomid(String.valueOf(tableId));

                // 通过查询出的户室信息查询出楼幢id、项目id
                Empj_BuildingInfo building = house.getBuilding();
                if (null == building || null == building.getTableId() || building.getTableId() <= 0) {
                    return MyBackInfo.fail(properties, "未查询到楼幢信息");
                }

                // 判断楼幢的托管状态
                Empj_BuildingExtendInfo extendInfo = building.getExtendInfo();
                if (S_EscrowState.UnEscrowState.equals(extendInfo.getEscrowState())) {
                    return MyBackInfo.fail(properties, "楼幢信息未托管");
                }

                tb_b_contractDetail.setBuildingid(String.valueOf(building.getTableId()));

                Empj_ProjectInfo project = building.getProject();
                if (null == project || null == project.getTableId() || project.getTableId() <= 0) {
                    return MyBackInfo.fail(properties, "未查询到项目信息");
                }
                tb_b_contractDetail.setProjectid(String.valueOf(project.getTableId()));

                // 校验提取权限
                properties = getIsExtract(model, properties, company, house, project, building);
                if (!MyBackInfo.isSuccess(properties)) {
                    return properties;
                }

            } else {
                return MyBackInfo.fail(properties, "楼盘表有变更，关联户室Id:" + roomid + "，未查询到相关信息");
            }

            properties.put("tb_b_contract", tb_b_contractDetail);

            /*
             * 买受人信息
             */
            List<Tb_b_personofcontract> tb_b_personofcontractList = new ArrayList<>();
            if (result.getBuyerName().contains(",")) {
                String[] names = result.getBuyerName().split(",");
                String[] cardnos = result.getBuyerCardNo().split(",");
                String[] phones = result.getBuyerPhone().split(",");
                for (int i = 0; i < names.length; i++) {
                    Tb_b_personofcontract person = new Tb_b_personofcontract();
                    /*
                     * xsz by time 2018-11-27 09:16:48 录入之前判断手机号是否有被分割
                     * 根据手机号的分割情况进行数据录入
                     */
                    person.setContactAdress(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
                    person.setAgentAddress(StrUtil.isBlank(result.getBuyerAddress()) ? "" : result.getBuyerAddress());
                    person.setCertificateType("1");
                    person.setContractId(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                    person.setECodeOfContract(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());

                    person.setBuyerName(names[i]);

                    if (cardnos.length < names.length) {
                        person.setECodeOfcertificate(cardnos[0]);
                    } else {
                        person.setECodeOfcertificate(cardnos[i]);
                    }

                    if (phones.length < names.length) {
                        person.setContactPhone(phones[0]);
                    } else {
                        person.setContactPhone(phones[i]);
                    }

                    tb_b_personofcontractList.add(person);
                }

            } else {
                Tb_b_personofcontract person = new Tb_b_personofcontract();
                /*
                 * xsz by time 2018-11-27 09:16:48 录入之前判断手机号是否有被分割
                 * 根据手机号的分割情况进行数据录入
                 */
                person.setContactAdress(StrUtil.isBlank(result.getPosition()) ? "" : result.getPosition());
                person.setAgentAddress(StrUtil.isBlank(result.getBuyerAddress()) ? "" : result.getBuyerAddress());
                person.setCertificateType("1");
                person.setContractId(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());
                person.setECodeOfContract(StrUtil.isBlank(result.getContractNo()) ? "" : result.getContractNo());

                person.setBuyerName(StrUtil.isBlank(result.getBuyerName()) ? "" : result.getBuyerName());

                person.setECodeOfcertificate(StrUtil.isBlank(result.getBuyerCardNo()) ? "" : result.getBuyerCardNo());
                person.setContactPhone(StrUtil.isBlank(result.getBuyerPhone()) ? "" : result.getBuyerPhone());

                tb_b_personofcontractList.add(person);
            }

            properties.put("buyerList", tb_b_personofcontractList);

            System.out.println("合同信息：" + result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return MyBackInfo.fail(properties, e.getMessage());
        }

        String info = (String) properties.get(S_NormalFlag.info);
        if (info.contains("not return a unique result")) {
            properties.put(S_NormalFlag.info, "楼盘表信息重复，请联系维护人员");
        }

        return properties;

    }

    /**
     * 网站对接SSO验证 2、发送URL和约定参数 URL: http://apits.czzhengtai.com:811/ 3、报文格式
     * 发送报文格式 { "cate":"sso_login", "userid":"userid", "stim ":"开始登录实际",
     * "etim":"登录失效时间" }
     * <p>
     * 接收报文格式
     * <p>
     * { "userid":"userid", "aeecss_token":"token字符串", }
     */
    @SuppressWarnings("unchecked")
    public Properties loginVerification(CommonForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Map<String, Object> pushVo = new HashMap<String, Object>();
        pushVo.put("cate", "sso_login");
        pushVo.put("userid", model.getUser().getTableId().toString());
        pushVo.put("stim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis()));
        pushVo.put("etim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis() + 3600000));

        Gson gson = new Gson();

        String jsonMap = gson.toJson(pushVo);

        System.out.println(jsonMap);

        String decodeStr = Base64Encoder.encode(jsonMap);

        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
       // String str = toFace.commonInterface("http://apits.czzhengtai.com:811/", decodeStr);
        String str = toFace.commonInterface("http://172.21.105.15:811/index.asp", decodeStr);
        //String str = toFace.commonInterface("http://172.21.105.15:8003/", decodeStr);
        System.out.println(str);

        if (StrUtil.isNotBlank(str)) {
            Map<String, String> fromJson = gson.fromJson(str, Map.class);
            properties.put("aeecss_token", null == fromJson.get("aeecss_token") ? "" : fromJson.get("aeecss_token"));
        } else {
            return MyBackInfo.fail(properties, "未接收到返回参数！");
        }

        return properties;
    }

    /**
     * 网站审核反馈接口
     */
    public Properties approvalFeedback(CommonForm model) {

        Properties properties = new MyProperties();

        // 业务编码
        String businessCode = model.getBusinessCode();

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("MH-TO-ZT-Feedback-" + businessCode);
        tgpf_SocketMsg.setMsgContentArchives(model.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        String ts_id = model.getTs_id();
        if (StrUtil.isBlank(ts_id)) {
            return MyBackInfo.fail(properties, "请传递ts_id");
        }

        String ts_pj_id = model.getTs_pj_id();
        if (StrUtil.isBlank(ts_pj_id)) {
            return MyBackInfo.fail(properties, "请传递ts_id");
        }

        // 0：不通过 1：审核通过
        String actionType = model.getActionType();
        if (!"0".equals(actionType) && !"1".equals(actionType)) {
            return MyBackInfo.fail(properties, "请传递正确的审核结果（0-不通过 1-通过）！");
        }

        Sm_User sm_User = userDao.findById(652L);
        String ts_bld_id = model.getTs_bld_id();
        if (StrUtil.isBlank(ts_bld_id) || "0".equals(ts_bld_id.trim())) {
            /*
             * 没有楼幢-项目信息审核 1、更新主表审核状态 2、更新管理表状态 3、根据审核结果判断是否发起新的审批流程
             */
            // 查询原单据
            Empj_ProjProgForcast_AF progForcast_AF = projProgForcast_AFDao.findById(Long.valueOf(ts_id));
            if (null == progForcast_AF) {
                return MyBackInfo.fail(properties, "未查询到有效的单据！");
            }

            // 查询管理单据
            Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
            manageModel.setTheState(S_TheState.Normal);
            manageModel.setAfEntity(progForcast_AF);
            Empj_ProjProgForcast_Manage forcast_Manage = manageDao
                    .findOneByQuery_T(manageDao.getQuery(manageDao.getBasicHQL(), manageModel));
            if (null == forcast_Manage) {
                return MyBackInfo.fail(properties, "未查询到有效的管理单据信息！");
            }

            // 已审核
            progForcast_AF.setWebHandelState(actionType);
            progForcast_AF.setApprovalState(S_ApprovalState.Completed);
            progForcast_AF.setRecordTimeStamp(System.currentTimeMillis());
            progForcast_AF.setUserRecord(sm_User);
            projProgForcast_AFDao.update(progForcast_AF);

            forcast_Manage.setWebHandelInfo(model.getApprovalInfo());
            forcast_Manage.setWebHandelState(actionType);
            forcast_Manage.setRecordTimeStamp(System.currentTimeMillis());
            forcast_Manage.setUserRecord(sm_User);
            forcast_Manage.setApprovalState(S_ApprovalState.Completed);
            manageDao.update(forcast_Manage);

            /*
             * 审核不通过 发起新的审批流程
             */
            if ("0".equals(actionType)) {

                model.setButtonType("2");
                model.setUser(sm_User);
                model.setUserId(sm_User.getTableId());

                Properties propertie = sm_ApprovalProcessGetService.execute("03030206", sm_User.getTableId());

                if ("noApproval".equals(propertie.getProperty("info"))) {
                    // TODO 自动审核通过
                    return MyBackInfo.fail(properties, "未查询到配置的系统审批流程！");
                } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                    // 判断当前登录用户是否有权限发起审批
                    return properties;
                } else {

                    Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) propertie
                            .get("sm_approvalProcess_cfg");

                    // 审批操作
                    properties = sm_approvalProcessService.execute(forcast_Manage, model, sm_approvalProcess_cfg);

                    forcast_Manage.setApprovalState(S_ApprovalState.Examining);
                    forcast_Manage.setLastUpdateTimeStamp(System.currentTimeMillis());
                    forcast_Manage.setUserUpdate(sm_User);
                    manageDao.update(forcast_Manage);

                }

            }

        } else {
            /*
             * 存在楼幢-楼幢信息审核反馈 1、更新主表审核状态 2、更新管理表状态 3、根据审核结果判断是否发起新的审批流程
             */
            Empj_ProjProgForcast_DTL progForcast_DTL = projProgForcast_DTLDao.findById(Long.valueOf(ts_id));
            if (null == progForcast_DTL) {
                return MyBackInfo.fail(properties, "未查询到有效单据的单据！");
            }

            Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
            manageModel.setTheState(S_TheState.Normal);
            manageModel.setDtlEntity(progForcast_DTL);
            Empj_ProjProgForcast_Manage forcast_Manage = manageDao
                    .findOneByQuery_T(manageDao.getQuery(manageDao.getBasicHQL(), manageModel));
            if (null == forcast_Manage) {
                return MyBackInfo.fail(properties, "为查询到有效的管理单据信息！");
            }

            progForcast_DTL.setWebHandelState(actionType);
            progForcast_DTL.setUserRecord(sm_User);
            progForcast_DTL.setRecordTimeStamp(System.currentTimeMillis());
            progForcast_DTL.setApprovalState(S_ApprovalState.Completed);
            projProgForcast_DTLDao.update(progForcast_DTL);

            forcast_Manage.setWebHandelInfo(model.getApprovalInfo());
            forcast_Manage.setWebHandelState(actionType);
            forcast_Manage.setRecordTimeStamp(System.currentTimeMillis());
            forcast_Manage.setUserRecord(sm_User);
            forcast_Manage.setApprovalState(S_ApprovalState.Completed);
            manageDao.update(forcast_Manage);

            /*
             * 审核不通过 发起新的审批流程
             */
            if ("0".equals(actionType)) {

                model.setButtonType("2");
                model.setUser(sm_User);
                model.setUserId(sm_User.getTableId());

                Properties propertie = sm_ApprovalProcessGetService.execute("03030206", sm_User.getTableId());

                if ("noApproval".equals(propertie.getProperty("info"))) {
                    // TODO 自动审核通过
                    return MyBackInfo.fail(properties, "未查询到配置的系统审批流程！");
                } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                    // 判断当前登录用户是否有权限发起审批
                    return properties;
                } else {

                    Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) propertie
                            .get("sm_approvalProcess_cfg");

                    // 审批操作
                    properties = sm_approvalProcessService.execute(forcast_Manage, model, sm_approvalProcess_cfg);

                    forcast_Manage.setApprovalState(S_ApprovalState.Examining);
                    forcast_Manage.setLastUpdateTimeStamp(System.currentTimeMillis());
                    forcast_Manage.setUserUpdate(sm_User);
                    manageDao.update(forcast_Manage);

                }

            }

        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        model.getBusinessCode();

        return properties;
    }

    /**
     * 巡查图片处理
     */
    @SuppressWarnings({"unchecked", "unchecked", "static-access"})
    public Properties handlerPicForProjProgForcast(CommonForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        // 查询水印图片位置
        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("69005");
        baseParameterForm.setParametertype("69");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

        if (null == baseParameter) {
            return MyBackInfo.fail(properties, "baseParameter");
        }

        // 查询缩放比列
        Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
        baseParameterForm2.setTheState(S_TheState.Normal);
        baseParameterForm2.setTheValue("69001");
        baseParameterForm2.setParametertype("69");
        Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

        if (null == baseParameter2) {
            return MyBackInfo.fail(properties, "baseParameter2");
        }

        Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
        baseParameterForm3.setTheState(S_TheState.Normal);
        baseParameterForm3.setTheValue("69002");
        baseParameterForm3.setParametertype("69");
        Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

        if (null == baseParameter3) {
            return MyBackInfo.fail(properties, "baseParameter3");
        }

        String businessCode = model.getBusinessCode();
        if (StrUtil.isBlank(businessCode)) {
            return MyBackInfo.fail(properties, "请输入处理编号！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请输入单据Id!");
        }

        Empj_ProjProgForcast_AF projProgForcast_AF;
        Empj_ProjProgForcast_DTLForm dtlModel;
        List<Empj_ProjProgForcast_DTL> dtlList;

        // 附件信息
        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setBusiType("03030202");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 需处理的附件信息
        List<String> attachmentList;

        // 附件信息
        List<Sm_Attachment> sm_AttachmentList;
        Sm_Attachment attachment;

        MatrixUtil picUtil = new MatrixUtil();

        String[] deskURL;

        String theLink;
        String pic;
        String smallPic;

        ReceiveMessage upload;

        try {

            switch (businessCode) {
                case "ProgForcastId":

                    projProgForcast_AF = projProgForcast_AFDao.findById(tableId);

                    dtlModel = new Empj_ProjProgForcast_DTLForm();
                    dtlModel.setTheState(S_TheState.Normal);
                    dtlModel.setHandleState("0");

                    dtlModel.setAfEntity(projProgForcast_AF);
                    dtlModel.setAfId(projProgForcast_AF.getTableId());
                    dtlList = projProgForcast_DTLDao.findByPage(
                            projProgForcast_DTLDao.getQuery(projProgForcast_DTLDao.getHandlerPicHQL(), dtlModel));

                    for (Empj_ProjProgForcast_DTL dtl : dtlList) {
                        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());

                        attachmentList = new ArrayList<>();
                        // 加载所有楼幢下的相关附件信息
                        sm_AttachmentList = sm_AttachmentDao.findByPage(
                                sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));

                        System.out.println("楼幢： " + dtl.getBuildCode() + "满足的附件数据数" + sm_AttachmentList.size());

                        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
                            sm_AttachmentList = new ArrayList<Sm_Attachment>();
                        }

                        // 遍历总平图
                        for (int i = 0; i < 4; i++) {

                            attachment = sm_AttachmentList.get(i);
                            theLink = sm_AttachmentList.get(i).getTheLink();
                            pic = "";
                            smallPic = "";

                            deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(),
                                    baseParameter2.getTheName(), baseParameter3.getTheName());

                            // 高清图
                            if (StrUtil.isNotBlank(deskURL[0])) {
                                upload = ossUtil.upload(deskURL[0]);
                                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                                    pic = upload.getData().get(0).getUrl();
                                    picUtil.deleteFile(deskURL[0]);
                                }
                            }

                            // 缩略图
                            if (StrUtil.isNotBlank(deskURL[1])) {
                                upload = ossUtil.upload(deskURL[1]);
                                if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                        && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                                    smallPic = upload.getData().get(0).getUrl();
                                    picUtil.deleteFile(deskURL[1]);
                                }
                            }

                            if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
                                // 总平图
                                attachment.setSortNum("1");
                            } else {
                                // 其他附件
                                attachment.setSortNum("2");
                            }

                            attachment.setTheLink(pic);
                            attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
                            attachment.setRecordTimeStamp(System.currentTimeMillis());
                            sm_AttachmentDao.update(attachment);
                        }

                        // 更新图片处理状态
                        dtl.setHandleState("1");
                        projProgForcast_DTLDao.update(dtl);
                    }

                    break;

                case "ProgForcastDtlId":

                    Empj_ProjProgForcast_DTL progForcast_DTL = projProgForcast_DTLDao.findById(tableId);

                    sm_AttachmentForm.setSourceId(progForcast_DTL.getTableId().toString());

                    attachmentList = new ArrayList<>();
                    // 加载所有楼幢下的相关附件信息
                    sm_AttachmentList = sm_AttachmentDao.findByPage(
                            sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));

                    System.out.println("楼幢： " + progForcast_DTL.getBuildCode() + "满足的附件数据数" + sm_AttachmentList.size());

                    if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
                        sm_AttachmentList = new ArrayList<Sm_Attachment>();
                    }

                    // 遍历总平图
                    for (int i = 0; i < 4; i++) {

                        attachment = sm_AttachmentList.get(i);
                        theLink = sm_AttachmentList.get(i).getTheLink();
                        pic = "";
                        smallPic = "";

                        deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(),
                                baseParameter2.getTheName(), baseParameter3.getTheName());

                        // 高清图
                        if (StrUtil.isNotBlank(deskURL[0])) {
                            upload = ossUtil.upload(deskURL[0]);
                            if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                    && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                                pic = upload.getData().get(0).getUrl();
                                picUtil.deleteFile(deskURL[0]);
                            }
                        }

                        // 缩略图
                        if (StrUtil.isNotBlank(deskURL[1])) {
                            upload = ossUtil.upload(deskURL[1]);
                            if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                    && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                                smallPic = upload.getData().get(0).getUrl();
                                picUtil.deleteFile(deskURL[1]);
                            }
                        }

                        if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
                            // 总平图
                            attachment.setSortNum("1");
                        } else {
                            // 其他附件
                            attachment.setSortNum("2");
                        }

                        attachment.setTheLink(pic);
                        attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
                        attachment.setRecordTimeStamp(System.currentTimeMillis());
                        sm_AttachmentDao.update(attachment);
                    }

                    break;

                case "BuildId":

                    break;

                case "PicId":

                    Sm_Attachment sm_Attachment = sm_AttachmentDao.findById(tableId);

                    theLink = sm_Attachment.getTheLink();

                    pic = "";
                    smallPic = "";

                    deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(),
                            baseParameter2.getTheName(), baseParameter3.getTheName());

                    // 高清图
                    if (StrUtil.isNotBlank(deskURL[0])) {
                        upload = ossUtil.upload(deskURL[0]);
                        if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                            pic = upload.getData().get(0).getUrl();
                            picUtil.deleteFile(deskURL[0]);
                        }
                    }

                    // 缩略图
                    if (StrUtil.isNotBlank(deskURL[1])) {
                        upload = ossUtil.upload(deskURL[1]);
                        if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
                                && StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

                            smallPic = upload.getData().get(0).getUrl();
                            picUtil.deleteFile(deskURL[1]);
                        }
                    }

                    if ("总平图".equals(sm_Attachment.getAttachmentCfg().getTheName())) {
                        // 总平图
                        sm_Attachment.setSortNum("1");
                    } else {
                        // 其他附件
                        sm_Attachment.setSortNum("2");
                    }

                    sm_Attachment.setTheLink(pic);
                    sm_Attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
                    sm_Attachment.setRecordTimeStamp(System.currentTimeMillis());
                    sm_AttachmentDao.update(sm_Attachment);

                    break;

                default:

                    return MyBackInfo.fail(properties, "请输入处理类型!");

            }

        } catch (Exception e) {
            System.out.println(e);
            return MyBackInfo.fail(properties, e.getMessage());
        }

        return properties;
    }

    public Properties pushPicForProjProgForcast(CommonForm model) {
        Properties properties = new MyProperties();

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请输入主表id!");
        }
        Empj_ProjProgForcast_AF projProgForcast_AF = projProgForcast_AFDao.findById(tableId);
        if (null == projProgForcast_AF) {
            return MyBackInfo.fail(properties, "未查询到主表有效信息！");
        }

        Long buildId = model.getBuildId();
        if (null == buildId) {
            return MyBackInfo.fail(properties, "请输入子表id!");
        }

        Empj_ProjProgForcast_DTL forcast_DTL = projProgForcast_DTLDao.findById(buildId);
        if (null == forcast_DTL) {
            return MyBackInfo.fail(properties, "未查询到子表有效信息！");
        }

        pushProjProgForcastPhoto(projProgForcast_AF, forcast_DTL);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 工程进度巡查楼幢提交
     */
    @SuppressWarnings("unchecked")
    public Properties pushSumitForProjProgForcast(CommonForm model) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请输入主表id!");
        }
        Empj_ProjProgForcast_AF projProgForcast_AF = projProgForcast_AFDao.findById(tableId);
        if (null == projProgForcast_AF) {
            return MyBackInfo.fail(properties, "未查询到主表有效信息！");
        }

        Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
        dtlModel.setTheState(S_TheState.Normal);
        dtlModel.setAfCode(projProgForcast_AF.geteCode());
        dtlModel.setAfEntity(projProgForcast_AF);
        List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList = projProgForcast_DTLDao
                .findByPage(projProgForcast_DTLDao.getQuery(projProgForcast_DTLDao.getBasicHQL(), dtlModel));

        for (Empj_ProjProgForcast_DTL empj_ProjProgForcast_DTL : empj_ProjProgForcast_DTLList) {
            pushSumitProjProgForcast(projProgForcast_AF, empj_ProjProgForcast_DTL);
        }

        return properties;
    }

    /**
     * 工程进度巡查楼幢提交
     */
    @SuppressWarnings("unchecked")
    public Properties projProgForcastApproval(CommonForm model) {

        /*
         * { "cate":"ts_review", "ts_id":"ts_id  单据号", "ts_bld_id":"托管系统楼栋号",
         * "actionType":"0/1  0:未审，1:已审" }
         */
        Properties properties = new MyProperties();
        // 查询地址
        Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
        baseParameterForm0.setTheState(S_TheState.Normal);
        baseParameterForm0.setTheValue("69004");
        baseParameterForm0.setParametertype("69");
        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

        if (null == baseParameter0) {
            log.info("未查询到配置路径！");
            return MyBackInfo.fail(properties, "未查询到配置路径！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请输入主表id!");
        }

        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();

        Empj_ProjProgForcast_Manage manage = manageDao.findById(tableId);

        Empj_ProjProgForcast_DTL dtlEntity = manage.getDtlEntity();

        Empj_ProjProgForcast_AF afEntity = manage.getAfEntity();

        pushVo.setCate("ts_review");
        pushVo.setTs_pj_id(String.valueOf(afEntity.getProject().getTableId()));
        pushVo.setActionType("1");

        if (null != dtlEntity) {
            pushVo.setTs_bld_id(String.valueOf(dtlEntity.getBuildInfo().getTableId()));
            pushVo.setTs_id(String.valueOf(dtlEntity.getTableId()));
        } else {
            pushVo.setTs_id(String.valueOf(afEntity.getTableId()));
        }

        Gson gson = new Gson();
        String jsonMap = gson.toJson(pushVo);
        System.out.println(jsonMap);
        String decodeStr = Base64Encoder.encode(jsonMap);
        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, baseParameter0.getTheName());

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("ZT_TO_MH_APPROVAL");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        if (interfaceUtil) {
            tgpf_SocketMsg.setReturnCode("200");
        } else {
            tgpf_SocketMsg.setReturnCode("300");
        }
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        return properties;
    }

    /**
     * 网站审核反馈接口
     */
    public Properties approvalFeedback1(HttpServletRequest request, JSONObject obj) {

        Properties properties = new MyProperties();

        // 业务编码
        String json = obj.toJSONString();

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("MH-TO-ZT-Feedback-1");
        tgpf_SocketMsg.setMsgContentArchives(json);
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return properties;
    }

    /**
     * 网站审核反馈接口
     */
    public Properties approvalFeedback2(HttpServletRequest request, JSONObject obj) {

        Properties properties = new MyProperties();

        // 业务编码
        String json = obj.toJSONString();

        System.out.println(json);

        Map<String, String[]> parameterMap = request.getParameterMap();
        String string = parameterMap.toString();

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("MH-TO-ZT-Feedback-2");
        tgpf_SocketMsg.setMsgContentArchives(string);
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        return properties;
    }

    /**
     * 网站审核反馈接口
     */
    public Properties approvalFeedback3(HttpServletRequest request) {

        Properties properties = new MyProperties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        // 业务编码
        String json = request.getParameter("json");
        System.out.println(json);

        ApprovalForm model = JSONObject.parseObject(json, ApprovalForm.class);

        String businessCode = model.getBusinessCode();

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("MH-TO-ZT-Feedback-3-" + businessCode);
        tgpf_SocketMsg.setMsgContentArchives(model.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);


        System.out.println("保存日志记录");

        String ts_id = model.getTs_id();
        if (StrUtil.isBlank(ts_id)) {
            return MyBackInfo.fail(properties, "请传递ts_id");
        }

        String ts_pj_id = model.getTs_pj_id();
        if (StrUtil.isBlank(ts_pj_id)) {
            return MyBackInfo.fail(properties, "请传递ts_id");
        }

        // 0：不通过 1：审核通过
        String actionType = model.getActionType();
        if (!"0".equals(actionType) && !"1".equals(actionType)) {
            return MyBackInfo.fail(properties, "请传递正确的审核结果（0-不通过 1-通过）！");
        }

        Sm_User sm_User = userDao.findById(652L);
        String ts_bld_id = model.getTs_bld_id();
        if (StrUtil.isBlank(ts_bld_id) || "0".equals(ts_bld_id.trim())) {

            System.out.println("没有楼幢-项目信息审核");
            /*
             * 没有楼幢-项目信息审核 1、更新主表审核状态 2、更新管理表状态 3、根据审核结果判断是否发起新的审批流程
             */
            // 查询原单据
            Empj_ProjProgForcast_AF progForcast_AF = projProgForcast_AFDao.findById(Long.valueOf(ts_id));
            if (null == progForcast_AF) {
                return MyBackInfo.fail(properties, "未查询到有效的单据！");
            }

            // 查询管理单据
            Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
            manageModel.setTheState(S_TheState.Normal);
            manageModel.setAfEntity(progForcast_AF);
            Empj_ProjProgForcast_Manage forcast_Manage = manageDao
                    .findOneByQuery_T(manageDao.getQuery(manageDao.getAfBasicHQL(), manageModel));
            if (null == forcast_Manage) {
                return MyBackInfo.fail(properties, "未查询到有效的管理单据信息！");
            }

            // 已审核
            progForcast_AF.setWebHandelState(actionType);
            progForcast_AF.setApprovalState(S_ApprovalState.Completed);
            progForcast_AF.setRecordTimeStamp(System.currentTimeMillis());
            progForcast_AF.setUserRecord(sm_User);
            projProgForcast_AFDao.update(progForcast_AF);

            forcast_Manage.setWebHandelInfo(model.getApprovalInfo());
            forcast_Manage.setWebHandelState(actionType);
            forcast_Manage.setRecordTimeStamp(System.currentTimeMillis());
            forcast_Manage.setUserRecord(sm_User);
            forcast_Manage.setApprovalState(S_ApprovalState.Completed);
            manageDao.update(forcast_Manage);

            /*
             * 审核不通过 发起新的审批流程
             */
            if ("0".equals(actionType)) {

                System.out.println("没有楼幢-项目信息审核 审核不通过 发起新的审批流程");
                model.setButtonType("2");
                model.setUser(sm_User);
                model.setUserId(sm_User.getTableId());

                Properties propertie = sm_ApprovalProcessGetService.execute("03030206", sm_User.getTableId());

                if ("noApproval".equals(propertie.getProperty("info"))) {
                    // TODO 自动审核通过
                    return MyBackInfo.fail(properties, "未查询到配置的系统审批流程！");
                } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                    // 判断当前登录用户是否有权限发起审批
                    return properties;
                } else {

                    Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) propertie
                            .get("sm_approvalProcess_cfg");

                    // 审批操作
                    properties = sm_approvalProcessService.execute(forcast_Manage, model, sm_approvalProcess_cfg);

                    forcast_Manage.setApprovalState(S_ApprovalState.Examining);
                    forcast_Manage.setLastUpdateTimeStamp(System.currentTimeMillis());
                    forcast_Manage.setUserUpdate(sm_User);
                    manageDao.update(forcast_Manage);

                }

            }

        } else {
            /*
             * 存在楼幢-楼幢信息审核反馈 1、更新主表审核状态 2、更新管理表状态 3、根据审核结果判断是否发起新的审批流程
             */
            System.out.println("存在楼幢-楼幢信息审核反馈");
            Empj_ProjProgForcast_DTL progForcast_DTL = projProgForcast_DTLDao.findById(Long.valueOf(ts_id));
            if (null == progForcast_DTL) {
                return MyBackInfo.fail(properties, "未查询到有效单据的单据！");
            }

            Empj_ProjProgForcast_ManageForm manageModel = new Empj_ProjProgForcast_ManageForm();
            manageModel.setTheState(S_TheState.Normal);
            manageModel.setDtlEntity(progForcast_DTL);
            Empj_ProjProgForcast_Manage forcast_Manage = manageDao
                    .findOneByQuery_T(manageDao.getQuery(manageDao.getBasicHQL(), manageModel));
            if (null == forcast_Manage) {
                return MyBackInfo.fail(properties, "未查询到有效的管理单据信息！");
            }

            progForcast_DTL.setWebHandelState(actionType);
            progForcast_DTL.setUserRecord(sm_User);
            progForcast_DTL.setRecordTimeStamp(System.currentTimeMillis());
            progForcast_DTL.setApprovalState(S_ApprovalState.Completed);
            projProgForcast_DTLDao.update(progForcast_DTL);

            forcast_Manage.setWebHandelInfo(model.getApprovalInfo());
            forcast_Manage.setWebHandelState(actionType);
            forcast_Manage.setRecordTimeStamp(System.currentTimeMillis());
            forcast_Manage.setUserRecord(sm_User);
            forcast_Manage.setApprovalState(S_ApprovalState.Completed);
            manageDao.update(forcast_Manage);

            /*
             * 审核不通过 发起新的审批流程
             */
            if ("0".equals(actionType)) {

                System.out.println("存在楼幢-楼幢信息审核反馈 审核不通过 ");
                model.setButtonType("2");
                model.setUser(sm_User);
                model.setUserId(sm_User.getTableId());

                Properties propertie = sm_ApprovalProcessGetService.execute("03030206", sm_User.getTableId());

                System.out.println("存在楼幢-楼幢信息审核反馈 2222222 ");
                if ("noApproval".equals(propertie.getProperty("info"))) {
                    System.out.println("存在楼幢-楼幢信息审核反馈 noApproval ");
                    // TODO 自动审核通过
                    return MyBackInfo.fail(properties, "未查询到配置的系统审批流程！");
                } else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
                    System.out.println("存在楼幢-楼幢信息审核反馈 fail ");
                    // 判断当前登录用户是否有权限发起审批
                    return properties;
                } else {
                    System.out.println("存在楼幢-楼幢信息审核反馈 sm_approvalProcess_cfg ");
                    Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) propertie
                            .get("sm_approvalProcess_cfg");

                    System.out.println("存在楼幢-楼幢信息审核反馈 审批操作 1111111");
                    // 审批操作
                    sm_approvalProcess_cfg.setBusiCode("03030206");
                    properties = sm_approvalProcessService.execute(forcast_Manage, model, sm_approvalProcess_cfg);

                    System.out.println("存在楼幢-楼幢信息审核反馈 审批操作 22222222");
                    forcast_Manage.setApprovalState(S_ApprovalState.Examining);
                    forcast_Manage.setLastUpdateTimeStamp(System.currentTimeMillis());
                    forcast_Manage.setUserUpdate(sm_User);
                    manageDao.update(forcast_Manage);

                    System.out.println("存在楼幢-楼幢信息审核反馈 审批操作 33333333");

                }

            } else {
                /*
                 * 更新预测时间
                 */
                CommonForm commonModel = new CommonForm();
                commonModel.setForcastTime(forcast_Manage.getAfEntity().getForcastTime());
                commonModel.setBuildId(progForcast_DTL.getBuildInfo().getTableId());
                commonModel.setNowBuildNum(Integer.valueOf(
                        null == progForcast_DTL.getBuildProgress() ? "0" : progForcast_DTL.getBuildProgress()));
                commonModel.setBuildCountNum(
                        Integer.parseInt(new DecimalFormat("0").format(progForcast_DTL.getFloorUpNumber())));
                commonModel.setUser(sm_User);
                commonModel.setNowNode(progForcast_DTL.getNowNode());

                commonModel.setBuildProgressType(StrUtil.isBlank(progForcast_DTL.getBuildProgressType()) ? ""
                        : progForcast_DTL.getBuildProgressType());
                commonModel.setBuildProgress(
                        StrUtil.isBlank(progForcast_DTL.getBuildProgress()) ? "" : progForcast_DTL.getBuildProgress());
                commonService.ProjProgForecastExecute(commonModel);

                //推送公积金
                CommonForm comm = new CommonForm();
                comm.setTableId(progForcast_DTL.getTableId());

                PushApprovalInfo(comm);
            }

        }
        System.out.println("enddddddd ");
        model.getBusinessCode();

        return properties;
    }

//    /**
//     * 工程进度审核完结推送图片相关信息
//     *
//     * @return
//     */
//    public Properties PushApprovalInfo(CommonForm model) {
//
//
//        Properties properties = new MyProperties();
//        properties.put(S_NormalFlag.result, S_NormalFlag.success);
//        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
//
//        Long tableId = model.getTableId();
//        if (null == tableId) {
//            return MyBackInfo.fail(properties, "请输入主键！");
//        }
//
//        Empj_ProjProgForcast_DTL dtl = projProgForcast_DTLDao.findById(tableId);
//        if (null == dtl) {
//            return MyBackInfo.fail(properties, "未查询到相关信息！");
//        }
//
//        // 查询地址
//        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
//        baseParameterForm.setTheState(S_TheState.Normal);
//        baseParameterForm.setTheValue("91001");
//        baseParameterForm.setParametertype("91");
//        Sm_BaseParameter baseParameter = sm_BaseParameterDao
//                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
//        if (null == baseParameter) {
//            return MyBackInfo.fail(properties, "未查询到配置参数！");
//        } else if (StrUtil.isBlank(baseParameter.getTheName()) || !"1".equals(baseParameter.getTheName())) {
//            return MyBackInfo.fail(properties, "未查询到配置参数！");
//        }
//
//        // 查询地址
//        baseParameterForm = new Sm_BaseParameterForm();
//        baseParameterForm.setTheState(S_TheState.Normal);
//        baseParameterForm.setTheValue("91002");
//        baseParameterForm.setParametertype("91");
//        Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
//                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
//        if (null == baseParameter0) {
//            return MyBackInfo.fail(properties, "未查询到配置参数！");
//        }
//
//        System.out.println("PushApprovalInfo： 记录接口交互信息");
//
//        //查询楼幢是否满足推送条件
//        Empj_BuildingInfo buildInfo = dtl.getBuildInfo();
//        System.out.println("PushApprovalInfo： buildInfo");
//
//        //构建基本参数
//        Map<String, Object> map = new HashMap<>();
//        //开发企业名称
//        map.put("companyName", null == buildInfo.getDevelopCompany() ? "" : buildInfo.getDevelopCompany().getTheName());
//        //项目名称
//        map.put("projectName", StrUtil.isBlank(buildInfo.getTheNameOfProject()) ? "" : buildInfo.getTheNameOfProject());
//        //施工编号
//        map.put("eCodeFromConstruction", StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? "" : buildInfo.geteCodeFromConstruction());
//        //在建层数  建设进度类型（1-主体结构 2-外立面装饰 3-室内装修
//        //  如果是主体结构
//        if (dtl.getBuildProgressType().equals("1")) {
//            map.put("currentConstruction", StrUtil.isBlank(dtl.getBuildProgress()) ? "0" : dtl.getBuildProgress());
//        } else {
//            //否则当前在建层数是 总层数
//            map.put("currentConstruction", String.valueOf(dtl.getFloorUpNumber()));
//        }
//        //总层数
//        map.put("floorNumer", String.valueOf(dtl.getFloorUpNumber()));
//        //进度文字说明
//        map.put("remark", StrUtil.isBlank(dtl.getRemark()) ? "" : dtl.getRemark());
//
//        //构建附件参数
//        List<String> smAttachmentList = new ArrayList<>();
//
//        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
//        sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
//        sm_AttachmentForm.setBusiType("03030202");
//        sm_AttachmentForm.setTheState(S_TheState.Normal);
//
//        System.out.println("PushApprovalInfo： 获取附件信息");
//        // 加载所有楼幢下的相关附件信息
//        List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
//                .findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm), 1, 4);
//        if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
//            sm_AttachmentList = new ArrayList<Sm_Attachment>();
//        }
//
//        // 替换地址
//        baseParameterForm = new Sm_BaseParameterForm();
//        baseParameterForm.setTheState(S_TheState.Normal);
//        baseParameterForm.setTheValue("91003");
//        baseParameterForm.setParametertype("91");
//        Sm_BaseParameter baseParameter1 = sm_BaseParameterDao
//                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
//
//        if (null == baseParameter1) {
//            // 遍历总平图
//            for (int i = 0; i < sm_AttachmentList.size(); i++) {
//                smAttachmentList.add(sm_AttachmentList.get(i).getTheLink());
//            }
//        } else {
//            // 遍历总平图
//            for (int i = 0; i < sm_AttachmentList.size(); i++) {
//                smAttachmentList.add(sm_AttachmentList.get(i).getTheLink().replace("fj.czzhengtai.com:19000", baseParameter1.getTheName()));
//            }
//        }
//
//        System.out.println("PushApprovalInfo： 附件数量：" + sm_AttachmentList.size());
//
//
//        //附件信息
//        map.put("smAttachmentCfgList", smAttachmentList);
//
//
//        //托管楼栋对应多个公积金楼栋id
//        Gjj_BuildingForm form = new Gjj_BuildingForm();
//        form.setEmpjBuildingId(dtl.getBuildInfo().getTableId()+"");
//        List<Gjj_BulidingRelation> relations = gjj_bulidingRelationDao.findByPage(gjj_bulidingRelationDao.getQuery(gjj_bulidingRelationDao.getBasicHQL(), form));
//
//        Tgpf_SocketMsg tgpf_SocketMsg = null;
//        if(relations != null && relations.size()  >0){
//            for (Gjj_BulidingRelation relation :relations){
//                // 记录接口交互信息
//                tgpf_SocketMsg = new Tgpf_SocketMsg();
//                tgpf_SocketMsg.setTheState(S_TheState.Normal);
//                tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
//                tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
//                tgpf_SocketMsg.setMsgStatus(1);
//                tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
//                tgpf_SocketMsg.setMsgDirection("ZT_TO_GJJ");
//                tgpf_SocketMsg.setMsgContentArchives(null);
//                tgpf_SocketMsg.setReturnCode("200");
//
//                //公积金楼幢id
//                map.put("gjjTableId", relation.getGjjBuildingId());
//
//
//                Gson gson = new Gson();
//                String jsonMap = gson.toJson(map);
//
//
////                ToInterface toFace = new ToInterface();
////                try {
////                    String json = toFace.doPost(jsonMap, baseParameter0.getTheName());
////                    tgpf_SocketMsg.setMsgContent(json);
////                    System.out.println("PushApprovalInfo：" + json);
////                } catch (Exception e) {
////                    System.out.println("PushApprovalInfo：" + e.getMessage());
////                    e.printStackTrace();
////                    return MyBackInfo.fail(properties, "推送异常！" + e.getMessage());
////                }
//
//                //日志保存
//                tgpf_SocketMsg.setMsgContentArchives(jsonMap);
//                tgpf_SocketMsgDao.save(tgpf_SocketMsg);
//            }
//        }
//
//        return properties;
//    }


	public Properties PushApprovalInfo(CommonForm model){


		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if(null == tableId){
			return MyBackInfo.fail(properties,"请输入主键！");
		}

		Empj_ProjProgForcast_DTL dtl = projProgForcast_DTLDao.findById(tableId);
		if(null == dtl){
			return MyBackInfo.fail(properties,"未查询到相关信息！");
		}

		// 查询地址
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("91001");
		baseParameterForm.setParametertype("91");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
		if (null == baseParameter) {
			return MyBackInfo.fail(properties,"未查询到配置参数！");
		}else if (StrUtil.isBlank(baseParameter.getTheName()) || !"1".equals(baseParameter.getTheName())){
			return MyBackInfo.fail(properties,"未查询到配置参数！");
		}

		// 查询地址
		baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("91002");
		baseParameterForm.setParametertype("91");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
		if (null == baseParameter0) {
			return MyBackInfo.fail(properties,"未查询到配置参数！");
		}

		System.out.println("PushApprovalInfo： 记录接口交互信息");
		// 记录接口交互信息
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgStatus(1);
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgDirection("ZT_TO_GJJ");
		tgpf_SocketMsg.setMsgContentArchives(null);
		tgpf_SocketMsg.setReturnCode("200");

		//查询楼幢是否满足推送条件
		Empj_BuildingInfo buildInfo = dtl.getBuildInfo();
		System.out.println("PushApprovalInfo： buildInfo");
		if(StrUtil.isNotBlank(buildInfo.getGjjTableId()) && StrUtil.isNotBlank(buildInfo.getGjjState()) && !"1".equals(buildInfo.getGjjState())){

			/*ZT_TO_GJJ_005
			String dqlc = "";
			if ("1".equals(dtl.getBuildProgressType())) {
				dqlc = dtl.getBuildProgress();
				news_title = "主体结构施工：" + dqlc + "层";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 主体结构施工：" + dqlc + "层";
			} else if ("2".equals(dtl.getBuildProgressType())) {
				dqlc = floorUpNumberStr;
				news_title = "外立面装饰施工：" + dtl.getBuildProgress() + "%";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 外立面装饰施工："
						+ dtl.getBuildProgress() + "%";
			} else if ("3".equals(dtl.getBuildProgressType())) {
				dqlc = floorUpNumberStr;
				news_title = "室内装修施工：" + dtl.getBuildProgress() + "%";
				news_title1 = progForcast.getProjectName() + " 施工编号：" + geteCodeFromConstruction + " 室内装修施工："
						+ dtl.getBuildProgress() + "%";
			}
			*/

			//构建基本参数
			Map<String , Object> map = new HashMap<>();
			//开发企业名称
			map.put("companyName" , null == buildInfo.getDevelopCompany() ? "" : buildInfo.getDevelopCompany().getTheName());
			//项目名称
			map.put("projectName" , StrUtil.isBlank(buildInfo.getTheNameOfProject()) ? "" : buildInfo.getTheNameOfProject());
			//公积金楼幢id
			//map.put("gjjBuildingId" , buildInfo.getGjjTableId());
			//公积金楼幢id
			map.put("gjjTableId" , buildInfo.getGjjTableId());
			//施工编号
			map.put("eCodeFromConstruction" , StrUtil.isBlank(buildInfo.geteCodeFromConstruction()) ? "" : buildInfo.geteCodeFromConstruction());
			//在建层数  建设进度类型（1-主体结构 2-外立面装饰 3-室内装修
			//  如果是主体结构
			if(dtl.getBuildProgressType().equals("1") ){
				map.put("currentConstruction" , StrUtil.isBlank(dtl.getBuildProgress()) ? "0" : dtl.getBuildProgress());
			} else{
				//否则当前在建层数是 总层数
				map.put("currentConstruction" , String.valueOf(dtl.getFloorUpNumber()));
			}
			//总层数
			map.put("floorNumer" , String.valueOf(dtl.getFloorUpNumber()));
			//进度文字说明
			map.put("remark" , StrUtil.isBlank(dtl.getRemark()) ? "" : dtl.getRemark());

			//构建附件参数
			List<String> smAttachmentList = new ArrayList<>();

			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(dtl.getTableId().toString());
			sm_AttachmentForm.setBusiType("03030202");
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			System.out.println("PushApprovalInfo： 获取附件信息");
			// 加载所有楼幢下的相关附件信息
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getPushPhotoHQL(), sm_AttachmentForm), 1, 4);
			if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
				sm_AttachmentList = new ArrayList<Sm_Attachment>();
			}

			// 替换地址
			baseParameterForm = new Sm_BaseParameterForm();
			baseParameterForm.setTheState(S_TheState.Normal);
			baseParameterForm.setTheValue("91003");
			baseParameterForm.setParametertype("91");
			Sm_BaseParameter baseParameter1 = sm_BaseParameterDao
					.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

			if (null == baseParameter1) {
				// 遍历总平图
				for (int i = 0; i < sm_AttachmentList.size(); i++) {
					smAttachmentList.add(sm_AttachmentList.get(i).getTheLink());
				}
			}else {
				// 遍历总平图
				for (int i = 0; i < sm_AttachmentList.size(); i++) {
					smAttachmentList.add(sm_AttachmentList.get(i).getTheLink().replace(GjjService.replaceUrl, baseParameter1.getTheName()));
				}
			}

			System.out.println("PushApprovalInfo： 附件数量：" + sm_AttachmentList.size());


			//附件信息
			map.put("smAttachmentCfgList" , smAttachmentList);



			Gson gson = new Gson();
			String jsonMap = gson.toJson(map);
            System.out.println("PushApprovalInfo"+ jsonMap);
            System.out.println("PushApprovalInfo"+ baseParameter0.getTheName());

			ToInterface toFace = new ToInterface();

			try {
				String json = toFace.doPost(jsonMap, baseParameter0.getTheName());
				tgpf_SocketMsg.setMsgContent(json);
				System.out.println("PushApprovalInfo：" + json);
			} catch (Exception e) {
				System.out.println("PushApprovalInfo：" + e.getMessage());
				e.printStackTrace();
				return MyBackInfo.fail(properties,"推送异常！" + e.getMessage());
			}

			//日志保存
			tgpf_SocketMsg.setMsgContentArchives(jsonMap);
			tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		}

		return properties;
	}

}
