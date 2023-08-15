package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：机构成员
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_OrgMemberDetailService
{
	@Autowired
	private Emmp_OrgMemberDao emmp_OrgMemberDao;

	public Properties execute(Emmp_OrgMemberForm model)
	{
		Properties properties = new MyProperties();

		Long emmp_OrgMemberId = model.getTableId();
		Emmp_OrgMember emmp_OrgMember = (Emmp_OrgMember)emmp_OrgMemberDao.findById(emmp_OrgMemberId);
		if(emmp_OrgMember == null || S_TheState.Deleted.equals(emmp_OrgMember.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Emmp_OrgMember(Id:" + emmp_OrgMemberId + ")'不存在");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("emmp_OrgMember", emmp_OrgMember);

		return properties;
	}
}
