package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_DTLForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：工程进度巡查-子 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjProgForcast_DTLService {
    @Autowired
    private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
    @Autowired
    private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;

    public Properties execute(Empj_ProjProgForcast_DTLForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
