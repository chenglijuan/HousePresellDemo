package zhishusz.housepresell.service.emmp;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_ApprovalProcess_DeleteService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：监理机构 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Emmp_CompanySupervisionBatchDeleteService {
    @Autowired
    private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
    @Autowired
    private Sm_ApprovalProcess_DeleteService deleteService;

    public Properties batchDeleteExecute(Emmp_CompanyInfoForm model) {
        Properties properties = new MyProperties();

        Long[] idArr = model.getIdArr();
        String busiCode = S_BusiCode.busiCode_020131;
        if (idArr == null || idArr.length < 1) {
            return MyBackInfo.fail(properties, "没有需要删除的信息");
        }

        for (Long tableId : idArr) {
            Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(tableId);
            if (emmp_CompanyInfo == null) {
                return MyBackInfo.fail(properties, "监理机构不存在");
            }

            emmp_CompanyInfo.setTheState(S_TheState.Deleted);
            emmp_CompanyInfoDao.save(emmp_CompanyInfo);

            // 删除审批流
            deleteService.execute(tableId, busiCode);
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    public Properties deleteExecute(Emmp_CompanyInfoForm model) {
        Properties properties = new MyProperties();

        Long emmp_CompanyInfoId = model.getTableId();
        String busiCode = model.getBusiCode();
        if (busiCode == null || busiCode.length() == 0) {
            busiCode = S_BusiCode.busiCode_020131;
        }
        Emmp_CompanyInfo emmp_CompanyInfo = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
        if (emmp_CompanyInfo == null) {
            return MyBackInfo.fail(properties, "监理机构不存在");
        }

        emmp_CompanyInfo.setTheState(S_TheState.Deleted);
        emmp_CompanyInfoDao.save(emmp_CompanyInfo);

        // 删除审批流
        deleteService.execute(emmp_CompanyInfoId, busiCode);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
}
