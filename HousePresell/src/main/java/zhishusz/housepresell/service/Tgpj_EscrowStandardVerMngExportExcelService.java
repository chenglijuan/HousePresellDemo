package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpj_EscrowStandardVerMngForm;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExportToExcelUtil;
import zhishusz.housepresell.util.excel.model.Tgpj_EscrowStandardVerMngTemplate;

/*
 * Service列表查询：托管标准信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpj_EscrowStandardVerMngExportExcelService
{
    @Autowired
    private Tgpj_EscrowStandardVerMngDao tgpj_escrowStandardVerMngDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpj_EscrowStandardVerMngForm model)
    {
        Properties properties = new MyProperties();

        Long[] idArr = model.getIdArr();
        if (model.getKeyword() == null || "".equals(model.getKeyword()))
        {
            model.setKeyword(null);
        }
        if ("0".equals(model.getBusiState()))
        {
            model.setBusiState(null);
        }
        model.setTheState(S_TheState.Normal);

        List<Tgpj_EscrowStandardVerMng> tgpj_escrowStandardVerMngList;
        if (idArr == null || idArr.length < 1)
        {
            tgpj_escrowStandardVerMngList = tgpj_escrowStandardVerMngDao.findByPage(tgpj_escrowStandardVerMngDao.getQuery(tgpj_escrowStandardVerMngDao.getBasicHQL(), model), null, null);
        }
        else
        {
            tgpj_escrowStandardVerMngList = tgpj_escrowStandardVerMngDao.findByPage(tgpj_escrowStandardVerMngDao.getQuery(tgpj_escrowStandardVerMngDao.getExcelListHQL(), model), null, null);
        }

        ExportToExcelUtil exportToExcelUtil = new ExportToExcelUtil();

        Properties propertiesExport = exportToExcelUtil.execute(tgpj_escrowStandardVerMngList, Tgpj_EscrowStandardVerMngTemplate.class, "托管标准管理信息");

        if(S_NormalFlag.fail.equals(propertiesExport.get(S_NormalFlag.result)))
        {
            return properties;
        }

        properties.put("fileDownloadPath", model.getServerBasePath()+propertiesExport.get("fileRelativePath"));
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
