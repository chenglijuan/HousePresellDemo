package zhishusz.housepresell.dingding.service;

import zhishusz.housepresell.database.po.Empj_BldLimitAmount;

/**
 * @Description 攀云钉钉推送数据服务
 * @Author jxx
 * @Date 2020/9/28 20:09
 * @Version
 **/
public interface PanYunDingDingPushDataService {
    /**
     * 推送监理公司数据
     * 
     * @param bldLimitAmount
     */
    public boolean pushSupervisorCompanyData(Empj_BldLimitAmount bldLimitAmount);

    /**
     * 推送项目信息
     * 
     * @param bldLimitAmount
     */
    public boolean pushProjectData(Empj_BldLimitAmount bldLimitAmount);

    /**
     * 推送楼幢信息
     * 
     * @param bldLimitAmount
     */
    public boolean pushBuildingData(Empj_BldLimitAmount bldLimitAmount);

    /**
     * 推送变更节点信息
     * 
     * @param bldLimitAmount
     */
    public void pushChangeNodeData(Empj_BldLimitAmount bldLimitAmount);

    /**
     * 任务分派
     * 
     * @param bldLimitAmount
     */
    public boolean assignTask(Empj_BldLimitAmount bldLimitAmount);
}
