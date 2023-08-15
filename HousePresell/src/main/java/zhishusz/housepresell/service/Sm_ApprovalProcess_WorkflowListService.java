package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_RecordForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_WorkflowForm;
import zhishusz.housepresell.controller.form.Sm_BusinessRecordForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmountDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_RecordDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_WorkflowDao;
import zhishusz.housepresell.database.dao.Sm_BusinessRecordDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Record;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BusinessRecord;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_ApprovalModel;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.busicode.BusiCodeRange;

/*
 * Service列表查询：审批流-审批流程 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_ApprovalProcess_WorkflowListService {
    @Autowired
    private Sm_ApprovalProcess_WorkflowDao sm_ApprovalProcess_WorkflowDao;
    @Autowired
    private Sm_Permission_RoleUserDao sm_permission_roleUserDao;
    @Autowired
    private Sm_ApprovalProcess_RecordDao sm_ApprovalProcess_RecordDao;
    @Autowired
    private Sm_BusinessRecordDao sm_businessRecordDao;

    @Autowired
    private Empj_BldLimitAmountDao empj_BldLimitAmountDao;
    @Autowired
    private Empj_ProjProgForcast_AFDao projProgForcast_AFDao;

    private MyDatetime myDatetime = MyDatetime.getInstance();

    private static final Log log = LogFactory.getLog(Sm_ApprovalProcess_WorkflowListService.class.getName());

    @SuppressWarnings("unchecked")
    public Properties execute(Sm_ApprovalProcess_WorkflowForm model) {
        Properties properties = new MyProperties();

        /**
         * 1.通过用户Id查询所属角色 2.根据所属角色查询代办流程
         */
        // Long userId = model.getUserId(); //登录用户id
        Sm_User user = model.getUser();
        if (null == user) {
            return MyBackInfo.fail(properties, "'登录用户'不能为空");
        }

        // 关键字
        String keyword = model.getKeyword();
        if (keyword != null && keyword.length() > 0) {
            model.setKeyword("%" + keyword + "%");
        } else {
            model.setKeyword(null);
        }

        // 业务编码
        if (model.getBusiCode() == null || model.getBusiCode().length() == 0) {
            model.setBusiCode(null);
        }

        // 申请日期
        if (model.getApprovalApplyDate() != null && model.getApprovalApplyDate().length() > 0) {
            String[] applyDate = model.getApprovalApplyDate().split(" - ");
            Long startTimeStamp = myDatetime.stringToLong(applyDate[0]);
            Long dayTime = 24L * 60 * 60 * 1000 - 1;
            Long endTimeStamp = myDatetime.stringToLong(applyDate[1]) + dayTime;
            model.setStartTimeStamp(startTimeStamp);
            model.setEndTimeStamp(endTimeStamp);
        }

        // 排序
        String orderBy = model.getOrderBy();
        String busiState = model.getBusiState();
        if (orderBy == null || orderBy.length() == 0) {
            if (busiState.equals(S_WorkflowBusiState.Examining)) // 待办
            {
                model.setOrderBy("approvalProcess_AF.createTimeStamp asc");
            } else if (busiState.equals(S_WorkflowBusiState.Completed)) // 已办
            {
                model.setOrderBy("operateTimeStamp desc , approvalProcess_AF.createTimeStamp desc");
            }
        }

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

        Integer totalCount;
        List<Sm_ApprovalProcess_Workflow> sm_ApprovalProcess_WorkflowList = new ArrayList<>();

        if (busiState.equals(S_WorkflowBusiState.Completed)) {
            model.setUserUpdate(user);
            totalCount = sm_ApprovalProcess_WorkflowDao.findByPage_Size(
                sm_ApprovalProcess_WorkflowDao.getQuery_Size(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), model));

            sm_ApprovalProcess_WorkflowList = sm_ApprovalProcess_WorkflowDao.findByPage(
                sm_ApprovalProcess_WorkflowDao.getQuery(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), model));

        } else {

            System.out.println("根据用户所属角色筛选" + "start=" + System.currentTimeMillis());

            // 根据用户所属角色筛选
            Sm_Permission_RoleUserForm roleUserForm = new Sm_Permission_RoleUserForm();
            roleUserForm.setTheState(S_TheState.Normal);
            roleUserForm.setSm_UserId(user.getTableId());
            roleUserForm.setSm_User(user);
            // List<Sm_Permission_RoleUser> sm_permission_roleUserList =
            // sm_permission_roleUserDao.findByPage(sm_permission_roleUserDao.getQuery(sm_permission_roleUserDao.getBasicHQL(),
            // roleUserForm));
            List<Sm_Permission_RoleUser> sm_permission_roleUserList =
                sm_permission_roleUserDao.findByPage(sm_permission_roleUserDao.getBasicHQLByCriteria(roleUserForm));

            Long[] roleListId = new Long[sm_permission_roleUserList.size()];
            if (sm_permission_roleUserList == null || sm_permission_roleUserList.isEmpty()) {
                roleListId = new Long[1];
                roleListId[0] = -1L;
                model.setRoleListId(roleListId);
            } else {
                for (int i = 0; i < sm_permission_roleUserList.size(); i++) {
                    roleListId[i] = sm_permission_roleUserList.get(i).getSm_Permission_Role().getTableId();
                }
                model.setRoleListId(roleListId);
            }

            System.out.println("根据用户所属角色筛选" + "end=" + System.currentTimeMillis());

            System.out.println("范围授权过滤 ------ 根据申请单查询业务表  过滤节点信息" + "start=" + System.currentTimeMillis());

            String str = "";
            Long arr[] = model.getRoleListId();
            if (null != model.getRoleListId() && model.getRoleListId()[0] != -1) {
                for (int i = 0; i < arr.length; i++) {
                    str += arr[i].toString() + ",";
                }
            }
            System.out.println("RoleListId1122:  " + str);

            totalCount = sm_ApprovalProcess_WorkflowDao.findByPage_Size(
                sm_ApprovalProcess_WorkflowDao.getQuery_Size(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), model));

            if (totalCount > 0) {
                sm_ApprovalProcess_WorkflowList = sm_ApprovalProcess_WorkflowDao.findByPage(
                    sm_ApprovalProcess_WorkflowDao.getQuery(sm_ApprovalProcess_WorkflowDao.getBasicHQL(), model));

                // ----------------------------范围授权过滤 ------ 根据申请单查询业务表
                // 过滤节点信息-----------//

                System.out.println("范围授权过滤 ------ 根据申请单查询业务表 过滤节点信息12121312" + "start=" + System.currentTimeMillis());

                Iterator<Sm_ApprovalProcess_Workflow> it = sm_ApprovalProcess_WorkflowList.iterator();
                while (it.hasNext()) {
                    Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = it.next();
                    Sm_ApprovalProcess_AF sm_approvalProcess_af = null;
                    if (sm_approvalProcess_workflow.getApprovalProcess_AF() != null) {
                        sm_approvalProcess_af = sm_approvalProcess_workflow.getApprovalProcess_AF();
                    }

                    /*
                     * 特事特办 针对工程进度节点更新业务。
                     * 
                     * 不根据区域授权方式加载，根据该人员是否属于指定的监理机构
                     * 
                     */
                    String busiCode = sm_approvalProcess_af.getBusiCode();
                    String workflowCode = sm_approvalProcess_workflow.geteCode();
                    if (S_BusiCode.busiCode_03030100.equals(busiCode) && "JDJZ".equals(workflowCode)) {
                        Long tableId = sm_approvalProcess_af.getSourceId();
                        System.out.println("tableId="+tableId);
                        Empj_BldLimitAmount limitAmount = empj_BldLimitAmountDao.findById(tableId);
                        if (null != limitAmount && null != limitAmount.getCompanyOne() && null != user.getCompany()) {
                            Long userCompanyId = user.getCompany().getTableId();
                            Long companyOneId = limitAmount.getCompanyOne().getTableId();

                            if (!userCompanyId.equals(companyOneId)) {
                                it.remove();
                            }
                        }
//                        if (null != limitAmount && null != limitAmount.getCompanyOne()
//                                && null != limitAmount.getCompanyTwo() && null != user.getCompany()) {
//                            Long userCompanyId = user.getCompany().getTableId();
//                            Long companyOneId = limitAmount.getCompanyOne().getTableId();
//                            Long companyTwoId = limitAmount.getCompanyTwo().getTableId();
//
//                            if (!userCompanyId.equals(companyOneId) && !userCompanyId.equals(companyTwoId)) {
//                                it.remove();
//                            }
//                        }
                        else {
                            // 普通机构不需要做范围授权过滤
                            String[] NoRange_BusiCode = BusiCodeRange.NoRange_BusiCode();

                            if (!Arrays.asList(NoRange_BusiCode).contains(busiCode)) {
                                /**
                                 * 1:先直接查业务关联记录表条数
                                 */
                                Sm_BusinessRecordForm sm_businessRecordForm1 = new Sm_BusinessRecordForm();
                                sm_businessRecordForm1.setTheState(S_TheState.Normal);
                                sm_businessRecordForm1.setAfId(sm_approvalProcess_af.getTableId());
                                Integer businessRecordCount1 = sm_businessRecordDao.findByPage_Size(sm_businessRecordDao
                                    .getQuery_Size(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm1));

                                // 如果业务记录表没有存该审批业务，则看不到该条待办事项
                                if (businessRecordCount1 == 0) {
                                    it.remove();
                                } else {
                                    /**
                                     * 2:再加上登录用户范围权限去查业务关联记录表条数
                                     */
                                    Sm_BusinessRecordForm sm_businessRecordForm2 = new Sm_BusinessRecordForm();
                                    sm_businessRecordForm2.setTheState(S_TheState.Normal);
                                    sm_businessRecordForm2.setAfId(sm_approvalProcess_af.getTableId());

                                    if (businessRecordCount1 == 1) {
                                        Sm_BusinessRecord sm_businessRecord =
                                            sm_businessRecordDao.findOneByQuery_T(sm_businessRecordDao
                                                .getQuery(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm1));
                                        if (sm_businessRecord.getBuildingInfo() != null) {
                                            sm_businessRecordForm2
                                                .setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
                                        }
                                        if (sm_businessRecord.getProjectInfo() != null) {
                                            sm_businessRecordForm2.setProjectInfoIdArr(model.getProjectInfoIdArr());
                                        }
                                        if (sm_businessRecord.getCityRegion() != null) {
                                            sm_businessRecordForm2
                                                .setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
                                        }
                                    }

                                    // 业务记录关联表如果存子表信息，肯定会有楼幢信息 --> 例如用款申请 ，托管终止
                                    if (businessRecordCount1 > 1) {
                                        sm_businessRecordForm2.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
                                        sm_businessRecordForm2.setProjectInfoIdArr(model.getProjectInfoIdArr());
                                        sm_businessRecordForm2.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
                                    }

                                    Integer businessRecordCount2 =
                                        sm_businessRecordDao.findByPage_Size(sm_businessRecordDao.getQuery_Size(
                                            sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm2));

                                    if (businessRecordCount2 < businessRecordCount1) {
                                        it.remove();
                                    }
                                }
                            }
                        }

                    } else {

                        if ("03030202".equals(busiCode)) {
                            // 针对工程进度巡查，有过图片处理的才显示
                            Long tableId = sm_approvalProcess_af.getSourceId();
                            Empj_ProjProgForcast_AF progForcast_AF = projProgForcast_AFDao.findById(tableId);
                            if (!"1".equals(progForcast_AF.getHandleState())) {
                                it.remove();
                            }
                        }

                        // 普通机构不需要做范围授权过滤
                        String[] NoRange_BusiCode = BusiCodeRange.NoRange_BusiCode();

                        if (!Arrays.asList(NoRange_BusiCode).contains(busiCode)) {
                            /**
                             * 1:先直接查业务关联记录表条数
                             */
                            Sm_BusinessRecordForm sm_businessRecordForm1 = new Sm_BusinessRecordForm();
                            sm_businessRecordForm1.setTheState(S_TheState.Normal);
                            sm_businessRecordForm1.setAfId(sm_approvalProcess_af.getTableId());
                            Integer businessRecordCount1 = sm_businessRecordDao.findByPage_Size(sm_businessRecordDao
                                .getQuery_Size(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm1));

                            // 如果业务记录表没有存该审批业务，则看不到该条待办事项
                            if (businessRecordCount1 == 0) {
                                it.remove();
                            } else {
                                /**
                                 * 2:再加上登录用户范围权限去查业务关联记录表条数
                                 */
                                Sm_BusinessRecordForm sm_businessRecordForm2 = new Sm_BusinessRecordForm();
                                sm_businessRecordForm2.setTheState(S_TheState.Normal);
                                sm_businessRecordForm2.setAfId(sm_approvalProcess_af.getTableId());

                                if (businessRecordCount1 == 1) {
                                    Sm_BusinessRecord sm_businessRecord =
                                        sm_businessRecordDao.findOneByQuery_T(sm_businessRecordDao
                                            .getQuery(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm1));
                                    if (sm_businessRecord.getBuildingInfo() != null) {
                                        sm_businessRecordForm2.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
                                    }
                                    if (sm_businessRecord.getProjectInfo() != null) {
                                        sm_businessRecordForm2.setProjectInfoIdArr(model.getProjectInfoIdArr());
                                    }
                                    if (sm_businessRecord.getCityRegion() != null) {
                                        sm_businessRecordForm2.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
                                    }
                                }

                                // 业务记录关联表如果存子表信息，肯定会有楼幢信息 --> 例如用款申请 ，托管终止
                                if (businessRecordCount1 > 1) {
                                    sm_businessRecordForm2.setCityRegionInfoIdArr(model.getCityRegionInfoIdArr());
                                    sm_businessRecordForm2.setProjectInfoIdArr(model.getProjectInfoIdArr());
                                    sm_businessRecordForm2.setBuildingInfoIdIdArr(model.getBuildingInfoIdIdArr());
                                }

                                Integer businessRecordCount2 = sm_businessRecordDao.findByPage_Size(sm_businessRecordDao
                                    .getQuery_Size(sm_businessRecordDao.getBasicHQL2(), sm_businessRecordForm2));

                                if (businessRecordCount2 < businessRecordCount1) {
                                    it.remove();
                                }
                            }
                        }
                    }

                    /*if (S_TheState.Normal != sm_approvalProcess_af.getTheState()) {
                    	it.remove();
                    }*/

                }
                System.out.println("范围授权过滤 ------ 根据申请单查询业务表  过滤节点信息" + "end=" + System.currentTimeMillis());
                // ---------------------范围授权过滤 ------ 根据申请单查询业务表
                // 过滤节点信息-------------//

                // ------------------------------审批过的用户待办不显示----------------------------------------------//
                // 待办流程，会签模式下，已经审批通过的人，待办流程中不可以看到这条结点信息
                Iterator<Sm_ApprovalProcess_Workflow> it2 = sm_ApprovalProcess_WorkflowList.iterator();
                while (it2.hasNext()) {
                    Sm_ApprovalProcess_Workflow sm_approvalProcess_workflow = it2.next();
                    if (S_WorkflowBusiState.Examining.equals(sm_approvalProcess_workflow.getBusiState())
                        && S_ApprovalModel.SignModel.equals(sm_approvalProcess_workflow.getApprovalModel())) {
                        Sm_ApprovalProcess_RecordForm recordForm = new Sm_ApprovalProcess_RecordForm();
                        recordForm.setTheState(S_TheState.Normal);
                        recordForm.setApprovalProcessId(sm_approvalProcess_workflow.getTableId());
                        if (sm_approvalProcess_workflow.getLastUpdateTimeStamp() != null)// 当前节点的最后更新时间
                        {
                            recordForm.setWorkflowTime(sm_approvalProcess_workflow.getLastUpdateTimeStamp());
                        }

                        List<Sm_ApprovalProcess_Record> sm_approvalProcess_recordList =
                            sm_ApprovalProcess_RecordDao.findByPage(sm_ApprovalProcess_RecordDao
                                .getQuery(sm_ApprovalProcess_RecordDao.getBasicHQL(), recordForm));
                        for (Sm_ApprovalProcess_Record sm_approvalProcess_record : sm_approvalProcess_recordList) {
                            if (sm_approvalProcess_record.getUserOperate().getTableId().equals(user.getTableId())) {
                                it2.remove();
                                break;
                            }
                        }
                    }
                }
                // ------------------------------审批过的用户待办不显示----------------------------------------------//
            } else {
                sm_ApprovalProcess_WorkflowList = new ArrayList<Sm_ApprovalProcess_Workflow>();
            }

        }

        List<Sm_ApprovalProcess_Workflow> approvalProcess_workflows = null;
        totalCount = sm_ApprovalProcess_WorkflowList.size();
        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        if (totalCount > countPerPage) {
            int from = (pageNumber - 1) * countPerPage;
            int to = from + countPerPage;
            if (totalCount % countPerPage > 0 && pageNumber == totalPage) {
                to = from + (totalCount % countPerPage);
            }
            approvalProcess_workflows = sm_ApprovalProcess_WorkflowList.subList(from, to);
        } else {
            approvalProcess_workflows = sm_ApprovalProcess_WorkflowList;
        }

        properties.put("sm_ApprovalProcess_WorkflowList", approvalProcess_workflows);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
