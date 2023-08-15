package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.CommonForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_NodePredictionForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_DTLForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_NodePredictionDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_DTLDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_NodePrediction;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgInspection_DTL;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢预测节点 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_NodePredictionService {
    @Autowired
    private Empj_NodePredictionDao empj_NodePredictionDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;
    @Autowired
    private Empj_BldAccountGetLimitAmountVerService bldAccountGetLimitAmountVerService;
    @Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao bldLimitAmountVerAfDtlDao;
    @Autowired
    private Empj_ProjProgInspection_AFDao empj_ProjProgInspection_AFDao;
    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;

    public Properties execute(CommonForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 加载楼幢进度节点列表
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties loadingListExecute(CommonForm model) {
        Properties properties = new MyProperties();

        Long buildingId = model.getBuildingId();
        if (null == buildingId) {
            return MyBackInfo.fail(properties, "请选择维护的楼幢信息！");
        }

        Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(buildingId);
        if (null == buildingInfo) {
            return MyBackInfo.fail(properties, "未查询到楼幢信息！");
        }

        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if (null == buildingAccount) {
            return MyBackInfo.fail(properties, "该楼幢未维护进度节点信息！");
        }

        Empj_BuildingInfoForm buildingInfoForm = new Empj_BuildingInfoForm();
        buildingInfoForm.setTableId(buildingId);
        buildingInfoForm.setNowLimitRatio(buildingAccount.getCurrentLimitedRatio().toString());
        Properties execute = bldAccountGetLimitAmountVerService.execute(buildingInfoForm);

        properties.put("versionList", new ArrayList<Map<String, Object>>());
        List<Map<String, Object>> listMap = new ArrayList<>();

        Empj_NodePredictionForm empj_NodePredictionForm;
        List<Empj_NodePrediction> nodeList;
        if (null != execute) {
            listMap = (List<Map<String, Object>>)execute.get("versionList");
            // listMap.remove(0);
            for (Map<String, Object> map : listMap) {

                empj_NodePredictionForm = new Empj_NodePredictionForm();
                empj_NodePredictionForm.setTheState(S_TheState.Normal);
                empj_NodePredictionForm.setBuildingId(buildingId);
                empj_NodePredictionForm.setExpectFigureProgressId(MapUtils.getLong(map, "tableId"));
                nodeList = empj_NodePredictionDao.findByPage(empj_NodePredictionDao
                    .getQuery(empj_NodePredictionDao.getBasicHQL(), empj_NodePredictionForm));
                if (null != nodeList && !nodeList.isEmpty()) {
                    map.put("completeDate", nodeList.get(0).getCompleteDate());
                } else {
                    map.put("completeDate", "");
                }
            }

            properties.put("versionList", listMap);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    /**
     * 保存预测楼幢进度节点
     * 
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    public Properties saveListExecute(CommonForm model) {
        Properties properties = new MyProperties();

        Sm_User user = model.getUser();

        Long buildingId = model.getBuildingId();
        if (null == buildingId) {
            return MyBackInfo.fail(properties, "请选择维护的楼幢信息！");
        }

        Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(buildingId);
        if (null == buildingInfo) {
            return MyBackInfo.fail(properties, "未查询到楼幢信息！");
        }

        Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
        if (null == buildingAccount) {
            return MyBackInfo.fail(properties, "该楼幢未维护进度节点信息！");
        }

        Empj_NodePrediction nodePrediction;

        List<Empj_NodePredictionForm> versionList = model.getVersionList();
        Long tableId;
        Tgpj_BldLimitAmountVer_AFDtl limitAmountVer_AFDtl;
        List<Empj_NodePrediction> nodeList;
        
        Empj_ProjProgInspection_DTLForm dtlModel;
        List<Empj_ProjProgInspection_DTL> dtlList;
        
        List<Empj_ProjProgInspection_AF> listAf;
        Empj_ProjProgInspection_AFForm afModel;

        Date completeDate;
        if (null != versionList && versionList.size() > 0) {
            
            for(int i = 0; i < versionList.size(); i++){
                
                completeDate = versionList.get(i).getCompleteDate();
                
                if (null == versionList.get(i).getCompleteDate()) {
                    return MyBackInfo.fail(properties, "请维护预测完成时间！");
                }
                
                for(int j = i+1; j < versionList.size(); j++){
                    if(completeDate.before(versionList.get(j).getCompleteDate())){
                        return MyBackInfo.fail(properties, versionList.get(i).getTheName()+"预测完成时间必须大于"+versionList.get(j).getTheName()+"预测完成时间!");
                    }
                }
                
            }
            
            
            for (Empj_NodePredictionForm empj_NodePredictionForm : versionList) {

                tableId = empj_NodePredictionForm.getTableId();
                limitAmountVer_AFDtl = bldLimitAmountVerAfDtlDao.findById(tableId);
                if (null == limitAmountVer_AFDtl) {
                    return MyBackInfo.fail(properties, "未查询到节点信息！");
                }

                // 判断是新增还是更新
                empj_NodePredictionForm.setTheState(S_TheState.Normal);
                empj_NodePredictionForm.setBuildingId(buildingId);
                empj_NodePredictionForm.setExpectFigureProgressId(tableId);
                empj_NodePredictionForm.setExpectFigureProgress(limitAmountVer_AFDtl);
                nodeList = empj_NodePredictionDao.findByPage(
                    empj_NodePredictionDao.getQuery(empj_NodePredictionDao.getBasicHQL(), empj_NodePredictionForm));
                if (null != nodeList && !nodeList.isEmpty()) {
                    nodePrediction = nodeList.get(0);
                    nodePrediction.setBuilding(buildingInfo);
                    nodePrediction.setDevelopCompany(buildingInfo.getDevelopCompany());
                    nodePrediction.setCompanyName(buildingInfo.getDevelopCompany().getTheName());
                    nodePrediction.setProject(buildingInfo.getProject());
                    nodePrediction.setProjectName(buildingInfo.getProject().getTheName());
                    nodePrediction.setBuilding(buildingInfo);
                    nodePrediction.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());

                    nodePrediction.setExpectFigureProgress(limitAmountVer_AFDtl);
                    nodePrediction.setExpectLimitedName(limitAmountVer_AFDtl.getStageName());
                    nodePrediction.setExpectLimitedRatio(limitAmountVer_AFDtl.getLimitedAmount());
                    nodePrediction.setCompleteDate(empj_NodePredictionForm.getCompleteDate());

                    empj_NodePredictionDao.update(nodePrediction);
                } else {
                    nodePrediction = new Empj_NodePrediction();
                    nodePrediction.setUserStart(user);
                    nodePrediction.setUserUpdate(user);
                    nodePrediction.setCreateTimeStamp(System.currentTimeMillis());
                    nodePrediction.setLastUpdateTimeStamp(System.currentTimeMillis());
                    nodePrediction.setTheState(S_TheState.Normal);
                    nodePrediction.setBusiState("0");

                    nodePrediction.setBuilding(buildingInfo);
                    nodePrediction.setDevelopCompany(buildingInfo.getDevelopCompany());
                    nodePrediction.setCompanyName(buildingInfo.getDevelopCompany().getTheName());
                    nodePrediction.setProject(buildingInfo.getProject());
                    nodePrediction.setProjectName(buildingInfo.getProject().getTheName());
                    nodePrediction.setBuilding(buildingInfo);
                    nodePrediction.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());

                    nodePrediction.setExpectFigureProgress(limitAmountVer_AFDtl);
                    nodePrediction.setExpectLimitedName(limitAmountVer_AFDtl.getStageName());
                    nodePrediction.setExpectLimitedRatio(limitAmountVer_AFDtl.getLimitedAmount());
                    nodePrediction.setCompleteDate(empj_NodePredictionForm.getCompleteDate());

                    empj_NodePredictionDao.save(nodePrediction);
                }
                
                /*
                 * 更新项目进度巡查预测完成时间
                 */
                dtlModel = new Empj_ProjProgInspection_DTLForm();
                dtlModel.setTheState(S_TheState.Normal);
                dtlModel.setBuildInfo(buildingInfo);
                dtlModel.setForecastNode(limitAmountVer_AFDtl);
                dtlList = empj_ProjProgInspection_DTLDao.findByPage(
                    empj_ProjProgInspection_DTLDao.getQuery(empj_ProjProgInspection_DTLDao.getBasicHQL(), dtlModel));
                for (Empj_ProjProgInspection_DTL dtl : dtlList) {
                    dtl.setForecastCompleteDate(MyDatetime.getInstance().dateToSimpleString(empj_NodePredictionForm.getCompleteDate()));
                    dtl.setLastUpdateTimeStamp(System.currentTimeMillis());
                    dtl.setUserUpdate(user);
                    dtl.setDataSources("进度更新");
                    empj_ProjProgInspection_DTLDao.update(dtl);
                }
                
                afModel = new Empj_ProjProgInspection_AFForm();
                afModel.setTheState(S_TheState.Normal);
                afModel.setBuildInfo(buildingInfo);
                listAf = empj_ProjProgInspection_AFDao.findByPage(empj_ProjProgInspection_AFDao.getQuery(empj_ProjProgInspection_AFDao.getBasicHQLUpdate(), afModel));
                for (Empj_ProjProgInspection_AF afInfo : listAf) {
                    afInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
                    afInfo.setUserUpdate(model.getUser());
                    afInfo.setDataSources("进度更新");
                    afInfo.setUpdateDateTime(System.currentTimeMillis());
                    empj_ProjProgInspection_AFDao.update(afInfo);
                }

            }
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

}
