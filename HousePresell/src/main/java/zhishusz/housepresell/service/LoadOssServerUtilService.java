package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgDataInfo;
import com.xiaominfo.oss.sdk.OSSClientProperty;

/*
 * Service 预加载：加载 Oss-Server 配置信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class LoadOssServerUtilService
{
	@Autowired
	private OSSClientProperty oss;
	
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		
		
		//获取eCode
		String eCode = model.geteCode();//附件类型eCode
		
		Sm_AttachmentCfgDataInfo dataInfo = new Sm_AttachmentCfgDataInfo();
		
		dataInfo.setExtra(eCode);
		dataInfo.setAppid(oss.getAppid());
		dataInfo.setAppsecret(oss.getAppsecret());
		dataInfo.setRemote(oss.getRemote());
		dataInfo.setProject(oss.getProject());
		String remote = oss.getRemote();
		String subremote = remote.substring(0, remote.lastIndexOf("/")+1)+oss.getProject()+"/uploadMaterial";		
		dataInfo.setUpLoadUrl(subremote);
		
		properties.put("ossLoader", dataInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
