package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjProgInspection_DTLForm;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgInspection_DTLDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目进度巡查-子 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjProgInspection_DTLService {
    @Autowired
    private Empj_ProjProgInspection_AFDao empj_ProjProgInspection_AFDao;
    @Autowired
    private Empj_ProjProgInspection_DTLDao empj_ProjProgInspection_DTLDao;

    private static final String BUSI_CODE = "03030203";

    public Properties execute(Empj_ProjProgInspection_DTLForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
