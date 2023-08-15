package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tgpf_FundAppropriated_AFExcelVO;
import zhishusz.housepresell.exportexcelvo.Tgpf_SpecialFundAppropriated_AFExcelVO;
import zhishusz.housepresell.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFForm;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.state.S_NormalFlag;

/*
 * Service列表查询：申请-用款-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_FundAppropriated_AFListService {
    @Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
    @Autowired
    private Sm_UserDao sm_userDao;
    private MyDatetime myDatetime = MyDatetime.getInstance();

    private static final String excelName = "用款申请报表";

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpf_FundAppropriated_AFForm model) {
        Properties properties = new MyProperties();

        Long userId = model.getUserId();
        if (userId == null || userId < 1) {
            return MyBackInfo.fail(properties, "登录用户不能为空");
        }
        Sm_User userStart = sm_userDao.findById(userId);
        if (userStart == null) {
            return MyBackInfo.fail(properties, "登录用户不能为空");
        }
        if (userStart.getCompany() == null) {
            return MyBackInfo.fail(properties, "用户所属企业不能为空");
        }

        Emmp_CompanyInfo emmp_companyInfo = userStart.getCompany();

        if (emmp_companyInfo.getTheType() == null || emmp_companyInfo.getTheType().length() == 0) {
            return MyBackInfo.fail(properties, "机构类型不能为空");
        }

        if (emmp_companyInfo.getTheType() != null && !emmp_companyInfo.getTheType().equals(S_CompanyType.Zhengtai)) {
            Long developCompanyId = userStart.getCompany().getTableId();
            if (developCompanyId > 0) {
                model.setDevelopCompanyId(developCompanyId);
            }
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());


        //关键字
        String keyword = model.getKeyword();
        if (keyword == null || keyword.length() == 0) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        String applyType = model.getApplyType();
        if (StringUtils.isBlank(applyType)) {
            model.setApplyType(null);
        }

        //申请状态
        if (model.getApplyState() == null || model.getApplyState() < 1) {
            model.setApplyState(null);
        } else {
            model.setPayoutState1(null);
            model.setPayoutState2(null);
            model.setPayoutState3(null);
        }

        //审批状态
        if (model.getApprovalState() == null || model.getApprovalState().length() == 0) {
            model.setApprovalState(null);
        }

        // 用款申请日期
        if (model.getApplyDate() == null || model.getApplyDate().length() == 0) {
            model.setApplyDate(null);
        }

        //新增统筹 - 用款申请日期
        if (model.getFundOverallPlanApplyDate() == null || model.getFundOverallPlanApplyDate().length() == 0) {
            model.setFundOverallPlanApplyDate(null);
        } else {
            String[] applyDateArray = model.getFundOverallPlanApplyDate().split(" - ");
            model.setStartTimeStamp(myDatetime.stringToLong(applyDateArray[0]));
            Long dayTimeStamp = 24L * 60 * 60 * 1000 - 1;
            Long endTimeStamp = myDatetime.stringToLong(applyDateArray[1]) + dayTimeStamp;
            model.setEndTimeStamp(endTimeStamp);
        }

        //资金拨付 - 用款申请日期
        if (model.getFundAppropriatedApplyDate() == null || model.getFundAppropriatedApplyDate().length() == 0) {
            model.setFundAppropriatedApplyDate(null);
        }

        //统筹日期
        if (model.getFundOverallPlanDate() == null || model.getFundOverallPlanDate().length() == 0) {
            model.setFundOverallPlanDate(null);
        }

        //排序
        String orderBy = model.getOrderBy();
        if (orderBy == null || orderBy.length() == 0) {

            //用款申请默认排序 ： 用款申请日期
            //资金拨付默认排序 ： 用款申请日期 ，统筹日期
            if (model.getPayoutState1() == null || model.getPayoutState1() < 1) {
                model.setOrderBy(" DECODE(approvalState ,'待提交',-2,'审核中',-1,'已完结',0,'不通过',1 ) ,applyState, createTimeStamp desc");
            } else {
                model.setOrderBy(" DECODE(approvalState ,'待提交',-2,'审核中',-1,'已完结',0,'不通过',1 ) ,applyState, createTimeStamp desc , fundOverallPlan.createTimeStamp desc"); //TODO order by 关联查询
            }
        }
        if (orderBy != null && orderBy.length() > 0) {
            if (orderBy.equals("fundOverallPlanDate asc") || orderBy.equals("fundOverallPlanDate desc")) {
                model.setOrderBy("fundOverallPlan." + orderBy);
            }
        }


        Integer totalCount = tgpf_FundAppropriated_AFDao.findByPage_Size(tgpf_FundAppropriated_AFDao.getQuery_Size(tgpf_FundAppropriated_AFDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0) totalPage++;
        if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;

        List<Tgpf_FundAppropriated_AF> tgpf_FundAppropriated_AFList;
        if (totalCount > 0) {
            tgpf_FundAppropriated_AFList = tgpf_FundAppropriated_AFDao.findByPage(tgpf_FundAppropriated_AFDao.getQuery(tgpf_FundAppropriated_AFDao.getBasicHQL(), model), pageNumber, countPerPage);
        } else {
            tgpf_FundAppropriated_AFList = new ArrayList<Tgpf_FundAppropriated_AF>();
        }

        properties.put("tgpf_FundAppropriated_AFList", tgpf_FundAppropriated_AFList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }


    /**
     * 报表导出
     *
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties reportExportExecute(Tgpf_FundAppropriated_AFForm model) {

        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long userId = model.getUserId();
        if (userId == null || userId < 1) {
            return MyBackInfo.fail(properties, "登录用户不能为空");
        }
        Sm_User userStart = sm_userDao.findById(userId);
        if (userStart == null) {
            return MyBackInfo.fail(properties, "登录用户不能为空");
        }
        if (userStart.getCompany() == null) {
            return MyBackInfo.fail(properties, "用户所属企业不能为空");
        }

        Emmp_CompanyInfo emmp_companyInfo = userStart.getCompany();

        if (emmp_companyInfo.getTheType() == null || emmp_companyInfo.getTheType().length() == 0) {
            return MyBackInfo.fail(properties, "机构类型不能为空");
        }

        if (emmp_companyInfo.getTheType() != null && !emmp_companyInfo.getTheType().equals(S_CompanyType.Zhengtai)) {
            Long developCompanyId = userStart.getCompany().getTableId();
            if (developCompanyId > 0) {
                model.setDevelopCompanyId(developCompanyId);
            }
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());


        //关键字
        String keyword = model.getKeyword();
        if (keyword == null || keyword.length() == 0) {
            model.setKeyword(null);
        } else {
            model.setKeyword("%" + keyword + "%");
        }

        String applyType = model.getApplyType();
        if (StringUtils.isBlank(applyType)) {
            model.setApplyType(null);
        }

        //申请状态
        if (model.getApplyState() == null || model.getApplyState() < 1) {
            model.setApplyState(null);
        } else {
            model.setPayoutState1(null);
            model.setPayoutState2(null);
            model.setPayoutState3(null);
        }

        //审批状态
        if (model.getApprovalState() == null || model.getApprovalState().length() == 0) {
            model.setApprovalState(null);
        }

        // 用款申请日期
        if (model.getApplyDate() == null || model.getApplyDate().length() == 0) {
            model.setApplyDate(null);
        }

        //新增统筹 - 用款申请日期
        if (model.getFundOverallPlanApplyDate() == null || model.getFundOverallPlanApplyDate().length() == 0) {
            model.setFundOverallPlanApplyDate(null);
        } else {
            String[] applyDateArray = model.getFundOverallPlanApplyDate().split(" - ");
            model.setStartTimeStamp(myDatetime.stringToLong(applyDateArray[0]));
            Long dayTimeStamp = 24L * 60 * 60 * 1000 - 1;
            Long endTimeStamp = myDatetime.stringToLong(applyDateArray[1]) + dayTimeStamp;
            model.setEndTimeStamp(endTimeStamp);
        }

        //资金拨付 - 用款申请日期
        if (model.getFundAppropriatedApplyDate() == null || model.getFundAppropriatedApplyDate().length() == 0) {
            model.setFundAppropriatedApplyDate(null);
        }

        //统筹日期
        if (model.getFundOverallPlanDate() == null || model.getFundOverallPlanDate().length() == 0) {
            model.setFundOverallPlanDate(null);
        }

        //排序
        String orderBy = model.getOrderBy();
        if (orderBy == null || orderBy.length() == 0) {

            //用款申请默认排序 ： 用款申请日期
            //资金拨付默认排序 ： 用款申请日期 ，统筹日期
            if (model.getPayoutState1() == null || model.getPayoutState1() < 1) {
                model.setOrderBy(" DECODE(approvalState ,'待提交',-2,'审核中',-1,'已完结',0,'不通过',1 ) ,applyState, createTimeStamp desc");
            } else {
                model.setOrderBy(" DECODE(approvalState ,'待提交',-2,'审核中',-1,'已完结',0,'不通过',1 ) ,applyState, createTimeStamp desc , fundOverallPlan.createTimeStamp desc"); //TODO order by 关联查询
            }
        }
        if (orderBy != null && orderBy.length() > 0) {
            if (orderBy.equals("fundOverallPlanDate asc") || orderBy.equals("fundOverallPlanDate desc")) {
                model.setOrderBy("fundOverallPlan." + orderBy);
            }
        }


        Integer totalCount = tgpf_FundAppropriated_AFDao.findByPage_Size(tgpf_FundAppropriated_AFDao.getQuery_Size(tgpf_FundAppropriated_AFDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0) totalPage++;
        if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;

        List<Tgpf_FundAppropriated_AF> tgpf_FundAppropriated_AFList;
        List<Tgpf_FundAppropriated_AFExcelVO> list = new ArrayList<Tgpf_FundAppropriated_AFExcelVO>();
        if (totalCount > 0) {
            tgpf_FundAppropriated_AFList = tgpf_FundAppropriated_AFDao.findByPage(tgpf_FundAppropriated_AFDao.getQuery(tgpf_FundAppropriated_AFDao.getBasicHQL(), model), pageNumber, countPerPage);
            int i = 0;
            for (Tgpf_FundAppropriated_AF af : tgpf_FundAppropriated_AFList) {
                Tgpf_FundAppropriated_AFExcelVO pro = new Tgpf_FundAppropriated_AFExcelVO();
                pro.setOrdinal(++i);
                //开发企业名称
                pro.setTheNameOfDevelopCompany(StringUtils.isBlank(af.getTheNameOfDevelopCompany()) ? "" : af.getTheNameOfDevelopCompany());
                pro.setTheNameOfProject(StringUtils.isBlank(af.getTheNameOfProject()) ? "" : af.getTheNameOfProject());
                pro.setEcode(StringUtils.isBlank(af.geteCode()) ? "" : af.geteCode());
                //申请日期
                pro.setApplydate(StringUtils.isBlank(af.getApplyDate()) ? "" : af.getApplyDate());
                //统筹日期
                pro.setFundOverallPlanDate(StringUtils.isBlank(af.getFundOverallPlan().getFundOverallPlanDate()) ? "" : af.getFundOverallPlan().getFundOverallPlanDate());
                if (af.getApplyState() != null) {
                    if (6 == af.getApplyState().intValue()) {
                        pro.setApplystate("已拨付");
                    } else {
                        pro.setApplystate("未拨付");
                    }
                } else {
                    pro.setApplystate("");
                }
                pro.setTotalapplyamount(af.getTotalApplyAmount());
                list.add(pro);
            }
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Tgpf_FundAppropriated_AFListService");// 文件在项目中的相对路径
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
            writer.addHeaderAlias("ecode", "用款申请单号");
            writer.addHeaderAlias("theNameOfDevelopCompany", "开发企业名称");
            writer.addHeaderAlias("theNameOfProject", "项目名称");
            writer.addHeaderAlias("totalapplyamount", "申请金额（元）");
            writer.addHeaderAlias("applydate", "用款申请日期");
            writer.addHeaderAlias("fundOverallPlanDate", "拨付日期");
            writer.addHeaderAlias("applystate", "支付状态");


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

            writer.close();

            properties.put("fileURL", relativeDir + strNewFileName);

        } else {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }

        return properties;
    }


}
