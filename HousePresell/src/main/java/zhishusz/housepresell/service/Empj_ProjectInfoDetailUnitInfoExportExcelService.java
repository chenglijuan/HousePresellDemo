package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_ProjectInfoDetailUnitInfoTemplate;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_ProjectInfoDetailUnitInfoExportExcelService {
    @Autowired
    private Empj_UnitInfoDao empj_unitInfoDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Empj_UnitInfoForm model) {
        Properties properties = new MyProperties();

        Long[] idArr = model.getIdArr();
        String keyword = model.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            model.setKeyword(null);
        }

        List<Empj_UnitInfo> empj_unitInfoList;
        if (idArr == null || idArr.length < 1) {
            empj_unitInfoList = empj_unitInfoDao.findByPage(empj_unitInfoDao.getQuery(empj_unitInfoDao.getBasicHQL(), model), null, null);
        } else {
            empj_unitInfoList = empj_unitInfoDao.findByPage(empj_unitInfoDao.getQuery(empj_unitInfoDao.getExcelListHQL(), model), null, null);
        }

        ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
        Properties propertiesExport = exportToExcelUtil.execute(empj_unitInfoList, Empj_ProjectInfoDetailUnitInfoTemplate.class,
                "项目详情——单元信息");

        if (S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result))) {
            return properties;
        }

        properties.put("fileDownloadPath", model.getServerBasePath() + propertiesExport.get("fileRelativePath"));

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
