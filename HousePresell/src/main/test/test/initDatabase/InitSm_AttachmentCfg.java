package test.initDatabase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class InitSm_AttachmentCfg extends BaseJunitTest 
{
	private static final String BUSI_CODE = "010201";//档案配置
	
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	@Test
	public void execute() throws Exception 
	{
		//初始化材料配置
		Sm_AttachmentCfg sm_AttachmentCfg = new Sm_AttachmentCfg();
		
		sm_AttachmentCfg.setTheState(S_TheState.Normal);
		sm_AttachmentCfg.setBusiState("1");
		sm_AttachmentCfg.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		sm_AttachmentCfg.setBusiType("030102");
		sm_AttachmentCfg.setTheName("附件材料");//营业执照，资质认证
		sm_AttachmentCfg.setAcceptFileType("image/jpeg,image/gif,image/png");
		sm_AttachmentCfg.setAcceptFileCount(2);
		sm_AttachmentCfg.setMaxFileSize(300);
		sm_AttachmentCfg.setMinFileSize(30);
		sm_AttachmentCfg.setIsImage(true);
		sm_AttachmentCfg.setIsNeeded(true);
		sm_AttachmentCfg.setListType("picture-card");//text
		
		sm_AttachmentCfgDao.save(sm_AttachmentCfg);
	}
}
