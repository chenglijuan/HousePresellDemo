package zhishusz.housepresell.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_DTLForm;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_DTLDao;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_DTL;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Empj_ProjProgInspection_AFExcleVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目进度巡查-主 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjProgInspection_AFService {
    @Autowired
    private Empj_ProjProgInspection_AFDao empj_ProjProgInspection_AFDao;
    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;
    @Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;

    private static final String excelName = "托项目进度巡查";

    private static final String BUSI_CODE = "03030203";

    public Properties execute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 列表加载
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties listExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        String keyword = model.getKeyword();

        Long areaId = model.getAreaId();
        if (null == areaId) {
            model.setAreaId(null);
        }

        Long projectId = model.getProjectId();
        if (null == projectId) {
            model.setProjectId(null);
        }

        String approvalState = model.getApprovalState();
        if (StringUtils.isBlank(approvalState)) {
            model.setApprovalState(null);
        }

        if (StringUtils.isBlank(keyword)) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        model.setTheState(S_TheState.Normal);

        Integer totalCount = empj_ProjProgInspection_AFDao.findByPage_Size(
            empj_ProjProgInspection_AFDao.getQuery_Size(empj_ProjProgInspection_AFDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Empj_ProjProgInspection_AF> empj_ProjProgInspection_AFList;
        if (totalCount > 0) {
            empj_ProjProgInspection_AFList = empj_ProjProgInspection_AFDao.findByPage(
                empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQL(), model), pageNumber,
                countPerPage);
        } else {
            empj_ProjProgInspection_AFList = new ArrayList<>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;

        Empj_ProjProgInspection_DTLForm dtlModel;
        Tgpj_BldLimitAmountVer_AFDtl nowNode;
        List<Empj_ProjProgInspection_DTL> dtlList;
        for (Empj_ProjProgInspection_AF af : empj_ProjProgInspection_AFList) {
            pro = new MyProperties();

            pro.put("tableId", af.getTableId());
            pro.put("areaName", StrUtil.isBlank(af.getAreaName()) ? "" : af.getAreaName());
            pro.put("projectName", StrUtil.isBlank(af.getProjectName()) ? "" : af.getProjectName());
            pro.put("buildCode", StrUtil.isBlank(af.getBuildCode()) ? "" : af.getBuildCode());
            pro.put("deliveryType", StrUtil.isBlank(af.getDeliveryType()) ? "1" : af.getDeliveryType());
            pro.put("buildProgress", StrUtil.isBlank(af.getBuildProgress()) ? "" : af.getBuildProgress());
            
            pro.put("nowNodeName", af.getBuildInfo().getBuildingAccount().getBldLimitAmountVerDtl().getStageName());
            
            
            //pro.put("nowNodeName", StrUtil.isBlank(af.getNowNodeName()) ? "" : af.getNowNodeName());
            pro.put("dataSources", StrUtil.isBlank(af.getDataSources()) ? "" : af.getDataSources());
            pro.put("updateDateTime", null == af.getUpdateDateTime() ? ""
                : MyDatetime.getInstance().dateToSimpleString(af.getUpdateDateTime()));

            nowNode = af.getNowNode();
            dtlModel = new Empj_ProjProgInspection_DTLForm();
            dtlModel.setTheState(S_TheState.Normal);
            dtlModel.setAfCode(af.geteCode());
            dtlModel.setAfInfo(af);
            dtlModel.setForecastNode(nowNode);
            dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));
            if (!dtlList.isEmpty()) {
                pro.put("forecastCompleteDate", StrUtil.isBlank(dtlList.get(0).getForecastCompleteDate()) ? ""
                    : dtlList.get(0).getForecastCompleteDate());
                pro.put("determine",
                    StrUtil.isBlank(dtlList.get(0).getDetermine()) ? "1" : dtlList.get(0).getDetermine());
            } else {
                pro.put("forecastCompleteDate", "");
                pro.put("determine", "1");
            }

            list.add(pro);
        }

        properties.put("empj_ProjProgInspection_AFList", list);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;
    }

    /**
     * 批量删除
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties batchDeleteExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        long nowDate = System.currentTimeMillis();

        Long[] idArr = model.getIdArr();

        if (idArr == null || idArr.length < 1) {
            return MyBackInfo.fail(properties, "没有需要删除的信息");
        }

        Empj_ProjProgInspection_AF inspection;
        Empj_ProjProgInspection_DTLForm dtlModel;
        List<Empj_ProjProgInspection_DTL> dtlList;
        for (Long tableId : idArr) {

            inspection = empj_ProjProgInspection_AFDao.findById(tableId);
            if (null != inspection) {

                dtlModel = new Empj_ProjProgInspection_DTLForm();
                dtlModel.setTheState(S_TheState.Normal);
                dtlModel.setAfCode(inspection.geteCode());
                dtlModel.setAfInfo(inspection);

                dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                    empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));
                for (Empj_ProjProgInspection_DTL dtl : dtlList) {
                    dtl.setTheState(S_TheState.Deleted);
                    dtl.setUserUpdate(user);
                    dtl.setLastUpdateTimeStamp(nowDate);

                    empj_ProjProgInspection_DTLDao.update(dtl);
                }

                inspection.setTheState(S_TheState.Deleted);
                inspection.setUserUpdate(user);
                inspection.setLastUpdateTimeStamp(nowDate);
                empj_ProjProgInspection_AFDao.update(inspection);

            }

        }

        return properties;
    }

    /**
     * 详情
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties detailExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }

        Empj_ProjProgInspection_AF af = empj_ProjProgInspection_AFDao.findById(tableId);
        if (null == af) {
            return MyBackInfo.fail(properties, "查看的单据信息已失效，请刷新后重试！");
        }

        if (null == af.getBuildInfo()) {
            return MyBackInfo.fail(properties, "单据楼幢信息已失效，请刷新后重试！");
        }

        Properties afPro = new MyProperties();
        afPro.put("tableId", af.getTableId());
        afPro.put("areaName", StrUtil.isBlank(af.getAreaName()) ? "" : af.getAreaName());
        afPro.put("projectName", StrUtil.isBlank(af.getProjectName()) ? "" : af.getProjectName());
        afPro.put("buildCode", StrUtil.isBlank(af.getBuildCode()) ? "" : af.getBuildCode());
        afPro.put("deliveryType", StrUtil.isBlank(af.getDeliveryType()) ? "1" : af.getDeliveryType());
        afPro.put("buildProgress", StrUtil.isBlank(af.getBuildProgress()) ? "" : af.getBuildProgress());
        //afPro.put("nowNodeName", StrUtil.isBlank(af.getNowNodeName()) ? "" : af.getNowNodeName());
        afPro.put("nowNodeName", af.getBuildInfo().getBuildingAccount().getBldLimitAmountVerDtl().getStageName());
        afPro.put("dataSources", StrUtil.isBlank(af.getDataSources()) ? "" : af.getDataSources());
        afPro.put("updateDateTime",
            null == af.getUpdateDateTime() ? "" : MyDatetime.getInstance().dateToSimpleString(af.getUpdateDateTime()));
        afPro.put("buildFlow",
            null == af.getBuildInfo().getUpfloorNumber() ? 0.00 : af.getBuildInfo().getUpfloorNumber());

        properties.put("empj_ProjProgInspection_AF", afPro);

        Empj_ProjProgInspection_DTLForm dtlModel = new Empj_ProjProgInspection_DTLForm();
        dtlModel.setTheState(S_TheState.Normal);
        dtlModel.setAfCode(af.geteCode());
        dtlModel.setAfInfo(af);

        List<Empj_ProjProgInspection_DTL> dtlList = empj_ProjProgInspection_DTLDao.findByPage(
            empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));

        List<Properties> dtlListPro = new ArrayList<>();
        Properties dtlPro;
        for (Empj_ProjProgInspection_DTL dtl : dtlList) {
            dtlPro = new MyProperties();

            if (StrUtil.isBlank(dtl.getForecastNodeName()) || dtl.getForecastNodeName().contains("正负零")) {
                continue;
            }

            dtlPro.put("tableId", dtl.getTableId());
            dtlPro.put("buildingId", dtl.getBuildInfo().getTableId());
            dtlPro.put("buildCode", StrUtil.isBlank(dtl.getBuildCode()) ? "" : dtl.getBuildCode());
            dtlPro.put("buildProgress", StrUtil.isBlank(dtl.getBuildProgress()) ? "" : dtl.getBuildProgress());
            dtlPro.put("dataSources", StrUtil.isBlank(dtl.getDataSources()) ? "" : dtl.getDataSources());
            dtlPro.put("forecastNodeName", StrUtil.isBlank(dtl.getForecastNodeName()) ? "" : dtl.getForecastNodeName());
            dtlPro.put("forecastCompleteDate",
                StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" : dtl.getForecastCompleteDate());
            dtlPro.put("determine", StrUtil.isBlank(dtl.getDetermine()) ? "1" : dtl.getDetermine());
            dtlPro.put("reason", StrUtil.isBlank(dtl.getReason()) ? "" : dtl.getReason());

            dtlListPro.add(dtlPro);
        }

        properties.put("empj_ProjProgInspection_DTLList", dtlListPro);

        return properties;
    }

    /**
     * 保存
     * 
     * @param model
     * @return
     */
    public Properties saveExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        Sm_User user = model.getUser();
        long nowDate = System.currentTimeMillis();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要更新的单据信息！");
        }

        Empj_ProjProgInspection_AF af = empj_ProjProgInspection_AFDao.findById(tableId);
        if (null == af) {
            return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
        }

        String buildProgress = model.getBuildProgress();

        Double floorUpNumber = null == af.getBuildInfo().getFloorNumer() ? 0 : af.getBuildInfo().getFloorNumer();

        Integer floorUpNumberInt = Integer.parseInt(new DecimalFormat("0").format(floorUpNumber));
        if (StrUtil.isBlank(buildProgress)) {
            return MyBackInfo.fail(properties, "请输入楼幢当前建设进度！");
        }

        /*if (!ReUtil.isMatch("^[0-9]*$", buildProgress)) {
            return MyBackInfo.fail(properties, "请填写有效的建设进度（当前建设楼层数）！");
        }

        if (Integer.valueOf(buildProgress.trim()) > floorUpNumberInt) {
            return MyBackInfo.fail(properties, "建设进度不能大于地上层数(" + floorUpNumberInt + ")！");
        }*/

        af.setBuildProgress(buildProgress);
        af.setUserUpdate(user);
        af.setLastUpdateTimeStamp(nowDate);
        af.setUpdateDateTime(nowDate);
        empj_ProjProgInspection_AFDao.update(af);

        List<Empj_ProjProgInspection_DTLForm> dtlFormList = model.getEmpj_ProjProgInspection_DTL();

        Long dtlId;
        Empj_ProjProgInspection_DTL dtl;
        if (!dtlFormList.isEmpty()) {
            for (Empj_ProjProgInspection_DTLForm dtlForm : dtlFormList) {
                dtlId = dtlForm.getTableId();
                dtl = empj_ProjProgInspection_DTLDao.findById(dtlId);
                if (null != dtl) {
                    dtl.setBuildProgress(buildProgress);
                    dtl.setForecastCompleteDate(dtlForm.getForecastCompleteDate());
                    dtl.setDetermine(dtlForm.getDetermine());
                    dtl.setReason(dtlForm.getReason());
                    dtl.setUserUpdate(user);
                    dtl.setLastUpdateTimeStamp(nowDate);

                    empj_ProjProgInspection_DTLDao.update(dtl);
                }

            }
        }

        properties.put("tableId", tableId);

        return properties;
    }

    /**
     * 导出
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties exeportExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        String keyword = model.getKeyword();

        Long areaId = model.getAreaId();
        if (null == areaId) {
            model.setAreaId(null);
        }

        Long projectId = model.getProjectId();
        if (null == projectId) {
            model.setProjectId(null);
        }

        String approvalState = model.getApprovalState();
        if (StringUtils.isBlank(approvalState)) {
            model.setApprovalState(null);
        }

        if (StringUtils.isBlank(keyword)) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        model.setTheState(S_TheState.Normal);

        List<Empj_ProjProgInspection_AF> empj_ProjProgInspection_AFList = empj_ProjProgInspection_AFDao
            .findByPage(empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQL(), model));

        if (empj_ProjProgInspection_AFList.isEmpty()) {
            return MyBackInfo.fail(properties, "未查询到有效数据");
        } else {
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Qs_EscrowBankFunds_ViewExportExcelService");// 文件在项目中的相对路径
            String localPath = directoryUtil.getProjectRoot();// 项目路径

            String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

            if (saveDirectory.contains("%20")) {
                saveDirectory = saveDirectory.replace("%20", " ");
            }

            directoryUtil.mkdir(saveDirectory);

            String strNewFileName = excelName + "-"
                + MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

            String saveFilePath = saveDirectory + strNewFileName;

            // 通过工具类创建writer
            ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

            // 自定义字段别名
            writer.addHeaderAlias("ordinal", "序号");
            writer.addHeaderAlias("areaName", "区域");
            writer.addHeaderAlias("projectName", "项目");
            writer.addHeaderAlias("buildCode", "楼幢");
            writer.addHeaderAlias("deliveryType", "交付类型");
            writer.addHeaderAlias("buildProgress", "当前建设进度");
            writer.addHeaderAlias("nowNodeName", "当前节点名称");
            writer.addHeaderAlias("forecastCompleteDate", "预测完成日期");
            writer.addHeaderAlias("determine", "进度判定");
            writer.addHeaderAlias("dataSources", "数据来源");
            writer.addHeaderAlias("updateDateTime", "更新日期");

            List<Empj_ProjProgInspection_AFExcleVO> list = formart(empj_ProjProgInspection_AFList);

            // 一次性写出内容，使用默认样式
            writer.write(list);

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
            writer.autoSizeColumn(8, true);
            writer.autoSizeColumn(9, true);
            writer.autoSizeColumn(10, true);

            writer.close();

            properties.put("fileURL", relativeDir + strNewFileName);

        }

        return properties;
    }

    /**
     * 预测
     * 
     * @param model
     * @return
     */
    public Properties forecastExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    @SuppressWarnings("unchecked")
    List<Empj_ProjProgInspection_AFExcleVO> formart(List<Empj_ProjProgInspection_AF> volist) {

        List<Empj_ProjProgInspection_AFExcleVO> list = new ArrayList<Empj_ProjProgInspection_AFExcleVO>();
        int ordinal = 0;
        Empj_ProjProgInspection_AFExcleVO vo;
        Tgpj_BldLimitAmountVer_AFDtl nowNode;
        List<Empj_ProjProgInspection_DTL> dtlList;
        Empj_ProjProgInspection_DTLForm dtlModel;
        for (Empj_ProjProgInspection_AF po : volist) {
            ++ordinal;
            vo = new Empj_ProjProgInspection_AFExcleVO();

            vo.setOrdinal(ordinal);
            vo.setAreaName(po.getAreaName());
            vo.setProjectName(po.getProjectName());
            vo.setBuildCode(po.getBuildCode());
            if ("1".equals(po.getDeliveryType())) {
                vo.setDeliveryType("毛坯房");
            } else {
                vo.setDeliveryType("成品房");
            }
            vo.setBuildProgress(po.getBuildProgress());
            vo.setNowNodeName(po.getNowNodeName());

            nowNode = po.getNowNode();
            dtlModel = new Empj_ProjProgInspection_DTLForm();
            dtlModel.setTheState(S_TheState.Normal);
            dtlModel.setAfCode(po.geteCode());
            dtlModel.setAfInfo(po);
            dtlModel.setForecastNode(nowNode);
            dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));
            if (!dtlList.isEmpty()) {
                vo.setForecastCompleteDate(StrUtil.isBlank(dtlList.get(0).getForecastCompleteDate()) ? ""
                    : dtlList.get(0).getForecastCompleteDate());

                switch (StrUtil.isBlank(dtlList.get(0).getDetermine()) ? "1" : dtlList.get(0).getDetermine()) {
                    case "1":
                        vo.setDetermine("正常");
                        break;
                    case "2":
                        vo.setDetermine("延期");
                        break;
                    case "3":
                        vo.setDetermine("滞后");
                        break;
                    case "4":
                        vo.setDetermine("停工");
                        break;
                    default:
                        vo.setDetermine("正常");
                        break;
                }
            } else {
                vo.setForecastCompleteDate("");
                vo.setDetermine("正常");
            }
            vo.setDataSources(po.getDataSources());
            vo.setUpdateDateTime(null == po.getUpdateDateTime() ? ""
                : MyDatetime.getInstance().dateToSimpleString(po.getUpdateDateTime()));

            list.add(vo);
        }

        return list;

    }

    /**
     * 列表加载
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties viewListExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "请先登录！");
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        String keyword = model.getKeyword();

        Long areaId = model.getAreaId();
        if (null == areaId) {
            model.setAreaId(null);
        }

        Long projectId = model.getProjectId();
        if (null == projectId) {
            model.setProjectId(null);
        }

        String approvalState = model.getApprovalState();
        if (StringUtils.isBlank(approvalState)) {
            model.setApprovalState(null);
        }

        if (StringUtils.isBlank(keyword)) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        model.setTheState(S_TheState.Normal);

        Integer totalCount = empj_ProjProgInspection_AFDao.findByPage_Size(
            empj_ProjProgInspection_AFDao.getQuery_Size(empj_ProjProgInspection_AFDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Empj_ProjProgInspection_AF> empj_ProjProgInspection_AFList;
        if (totalCount > 0) {
            empj_ProjProgInspection_AFList = empj_ProjProgInspection_AFDao.findByPage(
                empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQL(), model), pageNumber,
                countPerPage);
        } else {
            empj_ProjProgInspection_AFList = new ArrayList<>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;

        Tgpj_BuildingAccount buildingAccount;
        Empj_PaymentBondChildForm paymentBondChildModel;
        for (Empj_ProjProgInspection_AF af : empj_ProjProgInspection_AFList) {
            pro = new MyProperties();

            pro.put("tableId", af.getTableId());
            pro.put("companyName", StrUtil.isBlank(af.getProject().getDevelopCompany().getTheName()) ? ""
                : af.getProject().getDevelopCompany().getTheName());
            pro.put("projectName", StrUtil.isBlank(af.getProjectName()) ? "" : af.getProjectName());
            pro.put("buildCode", StrUtil.isBlank(af.getBuildCode()) ? "" : af.getBuildCode());

            buildingAccount = af.getBuildInfo().getBuildingAccount();

            // 当前托管余额
            pro.put("currentEscrowFund", buildingAccount.getCurrentEscrowFund());
            // 初始受限额度
            pro.put("orgLimitedAmount", buildingAccount.getOrgLimitedAmount());
            // 保函金额

            paymentBondChildModel = new Empj_PaymentBondChildForm();
            paymentBondChildModel.setTheState(S_TheState.Normal);
            paymentBondChildModel.setBuildingId(af.getBuildInfo().getTableId());
            paymentBondChildModel.setEmpj_BuildingInfo(af.getBuildInfo());

            Double guaranteeAmount = MyDouble.getInstance()
                .parse(empj_PaymentBondChildDao.findByPage_DoubleSum(empj_PaymentBondChildDao.getQuery_Sum(
                    empj_PaymentBondChildDao.getBasicHQLNoRecord(), "paymentBondAmount", paymentBondChildModel)));
            pro.put("guaranteeAmount", null == guaranteeAmount ? 0.00 : guaranteeAmount);
            // 现金受限额度
            pro.put("cashLimitedAmount", buildingAccount.getCashLimitedAmount());
            // 当前形象进度
            pro.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress());
            // 当前受限比例
            pro.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio());
            // 节点受限额度
            pro.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());
            // 有效受限额度
            pro.put("effectiveLimitedAmount", buildingAccount.getEffectiveLimitedAmount());

            list.add(pro);
        }

        properties.put("empj_ProjProgInspection_AFList", list);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;
    }

    /**
     * 详情
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties viewDetailExecute(Empj_ProjProgInspection_AFForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据信息！");
        }

        Empj_ProjProgInspection_AF af = empj_ProjProgInspection_AFDao.findById(tableId);
        if (null == af) {
            return MyBackInfo.fail(properties, "查看的单据信息已失效，请刷新后重试！");
        }

        if (null == af.getBuildInfo()) {
            return MyBackInfo.fail(properties, "单据楼幢信息已失效，请刷新后重试！");
        }

        Properties afPro = new MyProperties();

        afPro.put("tableId", af.getTableId());
        afPro.put("companyName", StrUtil.isBlank(af.getProject().getDevelopCompany().getTheName()) ? ""
            : af.getProject().getDevelopCompany().getTheName());
        afPro.put("projectName", StrUtil.isBlank(af.getProjectName()) ? "" : af.getProjectName());
        afPro.put("buildCode", StrUtil.isBlank(af.getBuildCode()) ? "" : af.getBuildCode());

        Tgpj_BuildingAccount buildingAccount = af.getBuildInfo().getBuildingAccount();

        // 当前托管余额
        afPro.put("currentEscrowFund", buildingAccount.getCurrentEscrowFund());
        // 初始受限额度
        afPro.put("orgLimitedAmount", buildingAccount.getOrgLimitedAmount());
        // 保函金额
        Empj_PaymentBondChildForm paymentBondChildModel = new Empj_PaymentBondChildForm();
        paymentBondChildModel.setTheState(S_TheState.Normal);
        paymentBondChildModel.setBuildingId(af.getBuildInfo().getTableId());
        paymentBondChildModel.setEmpj_BuildingInfo(af.getBuildInfo());

        Double guaranteeAmount = MyDouble.getInstance()
            .parse(empj_PaymentBondChildDao.findByPage_DoubleSum(empj_PaymentBondChildDao.getQuery_Sum(
                empj_PaymentBondChildDao.getBasicHQLNoRecord(), "paymentBondAmount", paymentBondChildModel)));
        afPro.put("guaranteeAmount", null == guaranteeAmount ? 0.00 : guaranteeAmount);
        // 现金受限额度
        afPro.put("cashLimitedAmount", buildingAccount.getCashLimitedAmount());
        // 当前形象进度
        afPro.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress());
        // 当前受限比例
        afPro.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio());
        // 节点受限额度
        afPro.put("nodeLimitedAmount", buildingAccount.getNodeLimitedAmount());
        // 有效受限额度
        afPro.put("effectiveLimitedAmount", buildingAccount.getEffectiveLimitedAmount());

        properties.put("empj_ProjProgInspection_AF", afPro);

        Empj_ProjProgInspection_DTLForm dtlModel = new Empj_ProjProgInspection_DTLForm();
        dtlModel.setTheState(S_TheState.Normal);
        dtlModel.setAfCode(af.geteCode());
        dtlModel.setAfInfo(af);

        List<Empj_ProjProgInspection_DTL> dtlList = empj_ProjProgInspection_DTLDao.findByPage(
            empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));

        List<Properties> dtlListPro = new ArrayList<>();
        Properties dtlPro;
        for (Empj_ProjProgInspection_DTL dtl : dtlList) {
            dtlPro = new MyProperties();

            if (StrUtil.isBlank(dtl.getForecastNodeName()) || dtl.getForecastNodeName().contains("正负零")) {
                continue;
            }

            dtlPro.put("tableId", dtl.getTableId());
            // 形象进度
            dtlPro.put("buildProgress", StrUtil.isBlank(dtl.getForecastNodeName()) ? "" : dtl.getForecastNodeName());
            // 预测时间
            dtlPro.put("forecastCompleteDate",
                StrUtil.isBlank(dtl.getForecastCompleteDate()) ? "" : dtl.getForecastCompleteDate());

            // 受限比例（%）
            dtlPro.put("limitedRatio", dtl.getForecastNode().getLimitedAmount());
            // 现金受限额度（元）
            Double cashLimitedAmount = buildingAccount.getCashLimitedAmount();
            dtlPro.put("cashLimitAmount", cashLimitedAmount);
            // 节点受限额度（元）
            Double nodeLimitAmount = buildingAccount.getOrgLimitedAmount() * dtl.getForecastNode().getLimitedAmount() / 100;
            dtlPro.put("nodeLimitAmount", nodeLimitAmount);
            // 有效受限额度（元）
            Double effLimitAmount;
            if (cashLimitedAmount - nodeLimitAmount > 0) {
                effLimitAmount = nodeLimitAmount;
            } else {
                effLimitAmount = cashLimitedAmount;
            }
            dtlPro.put("effLimitAmount", effLimitAmount);

            // 预测需拨付金额（元）
            Double applyAmount = buildingAccount.getCurrentEscrowFund() - effLimitAmount;
            if (applyAmount < 0){
                applyAmount = 0.00;
            }
            dtlPro.put("applyAmount", applyAmount);

            dtlListPro.add(dtlPro);
        }

        properties.put("empj_ProjProgInspection_DTLList", dtlListPro);

        return properties;
    }

}
