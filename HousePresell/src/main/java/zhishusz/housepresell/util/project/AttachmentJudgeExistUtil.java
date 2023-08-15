package zhishusz.housepresell.util.project;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dechert on 2018-11-19.
 * Company: zhishusz
 */
@Service
public class AttachmentJudgeExistUtil {
    @Autowired
    private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

    public <T extends BaseForm> MsgInfo isExist(T baseForm) {
        MsgInfo msgInfo = new MsgInfo();
        //判断是否有必传
        Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
        sm_AttachmentCfgForm.setBusiType(baseForm.getBusiType());
        sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
        List<Sm_AttachmentCfg> sm_AttachmentCfgList = sm_AttachmentCfgDao.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));
        for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList) {
            //根据业务判断是否有必传的附件配置
            if (sm_AttachmentCfg.getIsNeeded()) {
                Boolean isExistAttachment = false;
                Sm_AttachmentForm[] attachmentList = baseForm.getGeneralAttachmentList();
                for (Sm_AttachmentForm sm_AttachmentForm : attachmentList) {
                    if (sm_AttachmentCfg.geteCode().equals(sm_AttachmentForm.getSourceType())) {
                        isExistAttachment = true;
                        break;
                    }
                }
                if (!isExistAttachment) {
                    msgInfo.setSuccess(false);
                    msgInfo.setInfo(sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
                    return msgInfo;
                }
            }
        }
        msgInfo.setSuccess(true);
        msgInfo.setInfo(S_NormalFlag.success);
        return msgInfo;
    }
}
