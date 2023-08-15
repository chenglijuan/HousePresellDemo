package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：项目信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_ApprovalProcess_BacklogUpdateService
{
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		
		Long[] idArr = model.getIdArr();
		
		for(int i = 0 ; i< idArr.length ;i++)
		{
			Long sm_CommonMessageDtlId = idArr[i];

			Sm_CommonMessageDtl sm_commonMessageDtl = (Sm_CommonMessageDtl)sm_CommonMessageDtlDao.findById(sm_CommonMessageDtlId);
			if(sm_commonMessageDtl == null || S_TheState.Deleted.equals(sm_commonMessageDtl.getTheState()))
			{
				return MyBackInfo.fail(properties, "待办事项不存在");
			}
			sm_commonMessageDtl.setReadTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			sm_commonMessageDtl.setUserUpdate(user);
			sm_commonMessageDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
//			sm_commonMessageDtl.setBusiState("1"); // 0 : 未读 1: 已读
			sm_commonMessageDtl.setIsReader(S_IsReaderState.ReadMesg);
			sm_CommonMessageDtlDao.save(sm_commonMessageDtl);
		}
	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
