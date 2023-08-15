package zhishusz.housepresell.util.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.database.po.state.S_RangeAuthType;
import zhishusz.housepresell.service.Sm_CityRegionInfoForRangeAuthListService;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.objectdiffer.model.Sm_Permission_RangeAuthorizationTemplate;
import zhishusz.housepresell.util.rebuild.Sm_CityRegionInfoRebuild;
import zhishusz.housepresell.util.rebuild.Sm_Permission_RangeAuthorizationRebuild;

/**
 * Created by Dechert on 2018-11-22.
 * Company: zhishusz
 */
@Service
public class RangeAuthorizationLogUtil {
    @Autowired
    private Sm_Permission_RangeAuthorizationRebuild rangeRebuild;
    @Autowired
    private Sm_CityRegionInfoRebuild cityRebuild;
    @Autowired
    private Sm_CityRegionInfoForRangeAuthListService sm_CityRegionInfoForRangeAuthListService;
    private MyDatetime myDatetime = MyDatetime.getInstance();

    public Sm_Permission_RangeAuthorizationTemplate getCopyTemplate(Sm_Permission_RangeAuthorization rangeAuthorizationPo, Sm_Permission_RangeAuthorizationTemplate rangeTemplate){
        //获取所有可以选择的列表
        Sm_CityRegionInfoForm cityForm = new Sm_CityRegionInfoForm();
        cityForm.setRangeAuthType(rangeAuthorizationPo.getRangeAuthType());
        Properties proTemp = sm_CityRegionInfoForRangeAuthListService.executeForSelect(cityForm);
        List<Properties> allSelectList = cityRebuild.getDetailForRangeAuth((List<Sm_CityRegionInfo>) proTemp.get("sm_CityRegionInfoList"), cityForm);
        ArrayList<HashMap> allSelectListMap = propertyListToMapList(allSelectList);

        //获取选中的列表
        Properties selectList = rangeRebuild.getDetail(rangeAuthorizationPo);
        HashMap selectListMap = propertyToMap(selectList);

        //处理template并设置值
        rangeTemplate.setRangeInfoAllList(allSelectListMap);
        rangeTemplate.setRangeInfoSelectDetail(selectListMap);
        String authTypeString = S_RangeAuthType.IntKeyToVal.get(cityForm.getRangeAuthType());
        rangeTemplate.setRangeAuthTypeString(authTypeString);
        rangeTemplate.setAuthStartTimeStampString(myDatetime.dateToSimpleString(rangeAuthorizationPo.getAuthStartTimeStamp()));
        rangeTemplate.setAuthEndTimeStampString(myDatetime.dateToSimpleString(rangeAuthorizationPo.getAuthEndTimeStamp()));
        Sm_Permission_RangeAuthorizationTemplate rangeTemplateCopy = ObjectCopier.copy(rangeTemplate);
        return rangeTemplateCopy;
    }

//    public void getNewTemplate(Sm_Permission_RangeAuthorization rangeAuthorizationPo){
//        //获取所有可以选择的列表
//        Sm_CityRegionInfoForm cityFormNew = new Sm_CityRegionInfoForm();
//        cityFormNew.setRangeAuthType(rangeAuthType);
//        Properties proTempNew = sm_CityRegionInfoForRangeAuthListService.executeForSelect(cityFormNew);
//        List<Properties> allSelectListNew = cityRebuild.getDetailForRangeAuth((List<Sm_CityRegionInfo>) proTempNew.get("sm_CityRegionInfoList"), cityFormNew);
//        ArrayList<HashMap> allSelectListMapNew = propertyToMap(allSelectListNew);
//        //获取选中的列表
//        Properties selectListNew = rangeRebuild.getDetail(rangeAuthorizationPo);
//        //处理template并设置值
//
//
//        rangeTemplate.setRangeInfoAllList(allSelectListMapNew);
//        HashMap selectListNewMap = new HashMap();
//        for (Map.Entry<Object, Object> entry : selectListNew.entrySet()) {
//            selectListNewMap.put(entry.getKey(), entry.getValue());
//        }
//        rangeTemplate.setRangeInfoSelectDetail(selectListNewMap);
//        String authTypeNewString = S_RangeAuthType.IntKeyToVal.get(rangeAuthType);
//        rangeTemplate.setRangeAuthTypeString(authTypeNewString);
//        rangeTemplate.setAuthStartTimeStampString(myDatetime.dateToSimpleString(myDatetime.getDateTimeStampMin(authTimeStampRange)));
//        rangeTemplate.setAuthEndTimeStampString(myDatetime.dateToSimpleString(myDatetime.getDateTimeStampMax(authTimeStampRange)));
//
//
//        Sm_Permission_RangeAuthorizationTemplate rangeTemplateNew = ObjectCopier.copy(rangeTemplate);
//    }

    private ArrayList<HashMap> propertyListToMapList(List<Properties> allSelectList) {
        ArrayList<HashMap> allSelectListMap = new ArrayList<>();
        for (Properties propertiesTemp : allSelectList) {
            HashMap hashMap = propertyToMap(propertiesTemp);
            allSelectListMap.add(hashMap);
        }
        return allSelectListMap;
    }

    private HashMap propertyToMap(Properties propertiesTemp) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<Object, Object> entry : propertiesTemp.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }
}
