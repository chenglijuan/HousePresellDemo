package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.po.Emmp_QualificationInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.dao.Emmp_QualificationInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;

/*
 * Service更新操作：机构成员
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Emmp_OrgMemberUpdateService
{
	@Autowired
	private Emmp_OrgMemberDao emmp_OrgMemberDao;
	@Autowired
	private Sm_BaseParameterDao	sm_BaseParameterDao;
	
	public Properties execute(Emmp_OrgMemberForm model)
	{
		Properties properties = new MyProperties();
		
		String theNameOfDepartment = model.getTheNameOfDepartment();
		String theName = model.getTheName();
		String idType = model.getIdType();
		String idNumber = model.getIdNumber();
		Long parameterId = model.getParameterId();
		String positionName = model.getPositionName();
		String phoneNumber = model.getPhoneNumber();
		String email = model.getEmail();
		String weixin = model.getWeixin();
		String qq = model.getQq();
		
		if(theNameOfDepartment == null || theNameOfDepartment.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theNameOfDepartment'不能为空");
		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theName'不能为空");
		}
		if(idType == null || idType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'idType'不能为空");
		}
		if(idNumber == null || idNumber.length() == 0)
		{
			return MyBackInfo.fail(properties, "'idNumber'不能为空");
		}

		if(positionName == null || positionName.length() == 0)
		{
			return MyBackInfo.fail(properties, "'theType'不能为空");
		}
		if(phoneNumber == null || phoneNumber.length() == 0)
		{
			return MyBackInfo.fail(properties, "'phoneNumber'不能为空");
		}
		if(email == null || email.length() == 0)
		{
			return MyBackInfo.fail(properties, "'email'不能为空");
		}
		if(weixin == null || weixin.length() == 0)
		{
			return MyBackInfo.fail(properties, "'weixin'不能为空");
		}
		if(qq == null || qq.length() == 0)
		{
			return MyBackInfo.fail(properties, "'qq'不能为空");
		}
		
		Sm_BaseParameter parameter = (Sm_BaseParameter)sm_BaseParameterDao.findById(parameterId);
//		if(parameter == null)
//		{
//			return MyBackInfo.fail(properties, "'Sm_BaseParameter(Id:" + parameterId + ")'不存在");
//		}
		
		Long emmp_OrgMemberId = model.getTableId();
		Emmp_OrgMember emmp_OrgMember = (Emmp_OrgMember)emmp_OrgMemberDao.findById(emmp_OrgMemberId);
		if(emmp_OrgMember == null)
		{
			return MyBackInfo.fail(properties, "'Emmp_OrgMember(Id:" + emmp_OrgMemberId + ")'不存在");
		}
		
		emmp_OrgMember.setTheNameOfDepartment(theNameOfDepartment);
		emmp_OrgMember.setTheName(theName);
		emmp_OrgMember.setIdType(idType);
		emmp_OrgMember.setIdNumber(idNumber);
		emmp_OrgMember.setParameter(parameter);
		emmp_OrgMember.setPositionName(positionName);
		emmp_OrgMember.setPhoneNumber(phoneNumber);
		emmp_OrgMember.setEmail(email);
		emmp_OrgMember.setWeixin(weixin);
		emmp_OrgMember.setQq(qq);
	
		emmp_OrgMemberDao.save(emmp_OrgMember);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
