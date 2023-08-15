package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Empj_PjDevProgressForcastTemplate;

/*
 * Service列表查询：工程进度预测信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_PjDevProgressForcastExportExcelService
{
    @Autowired
    private Empj_PjDevProgressForcastDao empj_pjDevProgressForcastDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Empj_PjDevProgressForcastForm model)
    {
        Properties properties = new MyProperties();

        Long[] idArr = model.getIdArr();
        String keyword = model.getKeyword();
        String busiState = model.getBusiState();
        if (keyword != null && !"".equals(keyword))
        {
            model.setKeyword("%"+keyword+"%");
        }
        else
        {
            model.setKeyword(null);
        }
        if ("".equals(busiState) || "0".equals(busiState) || "全部".equals(busiState))
        {
            model.setBusiState(null);
        }

        List<Empj_PjDevProgressForcast> empj_pjDevProgressForcastList;
        if (idArr == null || idArr.length < 1)
        {
            empj_pjDevProgressForcastList =
                    empj_pjDevProgressForcastDao.findByPage(empj_pjDevProgressForcastDao.createCriteriaForList(model,
                            null), null, null);
        }
        else
        {
            empj_pjDevProgressForcastList = empj_pjDevProgressForcastDao.findByPage(empj_pjDevProgressForcastDao.getQuery(empj_pjDevProgressForcastDao.getExcelListHQL(), model), null, null);
        }

        ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();
        Properties propertiesExport = exportToExcelUtil.execute(empj_pjDevProgressForcastList, Empj_PjDevProgressForcastTemplate.class, "项目列表信息");

        if (S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
        {
            return properties;
        }

        properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
        properties.put("Empj_PjDevProgressForcastList", empj_pjDevProgressForcastList);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
