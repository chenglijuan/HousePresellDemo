package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import zhishusz.housepresell.util.excel.model.Empj_ProjectInfoDetailBuildingInfoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_ProjectInfoDetailBuildingInfoExportExcelService
{
    @Autowired
    private Empj_BuildingInfoDao empj_buildingInfoDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Empj_BuildingInfoForm model)
    {
        Properties properties = new MyProperties();

        Long[] idArr = model.getIdArr();
        String keyword = model.getKeyword();
        if (keyword == null || "".equals(keyword))
        {
            model.setKeyword(null);
        }

        List<Empj_BuildingInfo> empj_buildingInfoList;
        if (idArr == null || idArr.length < 1)
        {
            empj_buildingInfoList = empj_buildingInfoDao.findByPage(empj_buildingInfoDao.getQuery(empj_buildingInfoDao.getBasicHQL(), model), null, null);
        }
        else
        {
            empj_buildingInfoList = empj_buildingInfoDao.findByPage(empj_buildingInfoDao.getQuery(empj_buildingInfoDao.getExcelListHQL(), model), null, null);
        }

        ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
        Properties propertiesExport = exportToExcelUtil.execute(empj_buildingInfoList, Empj_ProjectInfoDetailBuildingInfoTemplate.class,
                "项目详情——楼幢信息");

        if (S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
        {
            return properties;
        }

        properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}