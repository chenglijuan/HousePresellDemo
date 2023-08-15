package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Emmp_OrgMemberForm;
import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：机构成员
 * Company：ZhiShuSZ
 * */
@Service
public class Emmp_OrgMemberRebuild extends RebuilderBase<Emmp_OrgMember>
{
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Gson gson;

	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;

	@Override
	public Properties getSimpleInfo(Emmp_OrgMember object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theName", object.getTheName());
		properties.put("idType", object.getIdType());
		properties.put("idNumber", object.getIdNumber());
		properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
		Sm_BaseParameter parameter = object.getParameter();
		if (parameter != null) {
			properties.put("parameterName", parameter.getTheValue());
			properties.put("parameterId", parameter.getTableId());
		}
		properties.put("positionName", object.getPositionName());
		properties.put("phoneNumber", object.getPhoneNumber());
		properties.put("email", object.getEmail());
		properties.put("qq", object.getQq());
		properties.put("weixin", object.getWeixin());
		
		return properties;
	}

	public List<Properties> getListForApproval(List<Emmp_OrgMember> emmp_OrgMemberList, Long tableId, String busiCode, String busiState)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_OrgMemberList != null)
		{
			for(Emmp_OrgMember object:emmp_OrgMemberList)
			{
				list.add(getSimpleInfo(object));
			}
		}

		//如果是未备案则OSS上数据和数据库中数据一致
		if (S_BusiState.NoRecord.equals(busiState))
		{
			return list;
		}

		//审核的申请单
		Sm_ApprovalProcess_AFForm sm_ApprovalProcess_AFForm = new Sm_ApprovalProcess_AFForm();
		sm_ApprovalProcess_AFForm.setTheState(S_TheState.Normal);
		sm_ApprovalProcess_AFForm.setBusiState("待提交");
		sm_ApprovalProcess_AFForm.setSourceId(tableId);
		sm_ApprovalProcess_AFForm.setBusiCode(busiCode);
		Sm_ApprovalProcess_AF sm_ApprovalProcess_AF =
				sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

		if (sm_ApprovalProcess_AF == null)
		{
			sm_ApprovalProcess_AFForm.setBusiState("审核中");
			sm_ApprovalProcess_AF = sm_ApprovalProcess_AFDao.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), sm_ApprovalProcess_AFForm));

			if (sm_ApprovalProcess_AF == null)
			{
				return list;
			}
		}

		//清空列表重新存放数据
		list.clear();

		String expectObj = sm_ApprovalProcess_AF.getExpectObjJson();
		if (expectObj != null && expectObj.length() > 0 )
		{
			Emmp_CompanyInfoForm emmp_CompanyInfoForm = gson.fromJson(expectObj, Emmp_CompanyInfoForm.class);
			Emmp_OrgMemberForm[] emmp_OrgMemberArrayOSS = emmp_CompanyInfoForm.getOrgMemberList();

			if (emmp_OrgMemberArrayOSS == null)
			{
				return list;
			}

			for (Emmp_OrgMemberForm theForm:emmp_OrgMemberArrayOSS)  //
			{
				Properties properties = new MyProperties();

				properties.put("tableId", theForm.getTableId());
				properties.put("theName", theForm.getTheName());
				properties.put("idType", theForm.getIdType());
				properties.put("idNumber", theForm.getIdNumber());
				properties.put("theNameOfDepartment", theForm.getTheNameOfDepartment());
				properties.put("parameterId", theForm.getParameterId());
				properties.put("positionName", theForm.getPositionName());
				properties.put("phoneNumber", theForm.getPhoneNumber());
				properties.put("email", theForm.getEmail());
				properties.put("qq", theForm.getQq());
				properties.put("weixin", theForm.getWeixin());
				properties.put("changeState", theForm.getChangeState());

				list.add(properties);
			}


//			List<Emmp_OrgMemberForm> emmp_OrgMemberListOSS = new ArrayList<>();
//
//			if (emmp_OrgMemberArrayOSS != null && emmp_OrgMemberArrayOSS.length > 0)
//			{
//				for (Emmp_OrgMemberForm obj:emmp_OrgMemberArrayOSS)
//				{
//					emmp_OrgMemberListOSS.add(obj);
//				}
//			}
//
//			for (Emmp_OrgMember orgMember1:emmp_OrgMemberList)
//			{
//				int theIndex = -1;
//				for (int i = 0; i < emmp_OrgMemberListOSS.size(); i ++)
//				{
//					Emmp_OrgMemberForm orgMember2 = emmp_OrgMemberListOSS.get(i);
//					if (orgMember1.getTableId().equals(orgMember2.getTableId()))
//					{
//						theIndex = i;
//						break;
//					}
//				}
//
//				if (theIndex == -1)  //如果是-1，说明该成员已经被删了
//				{
//					Properties properties = getSimpleInfo(orgMember1);
//					properties.put("changeState", "del");
//					list.add(properties);
//				}
//				else  //如果不是-1，说明两边都存在无变更，记录的同时删除Form里的数据
//				{
//					list.add(getSimpleInfo(orgMember1));
//					emmp_OrgMemberListOSS.remove(theIndex);
//				}
//			}
//
//			for (Emmp_OrgMemberForm object:emmp_OrgMemberListOSS)  //
//			{
//				Properties properties = new MyProperties();
//
//				properties.put("tableId", object.getTableId());
//				properties.put("theName", object.getTheName());
//				properties.put("idType", object.getIdType());
//				properties.put("idNumber", object.getIdNumber());
//				properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
////				properties.put("parameterName", object.getParameter().getTheValue());
////				properties.put("parameter", object.getParameter().getTableId());
//				properties.put("positionName", object.getPositionName());
//				properties.put("phoneNumber", object.getPhoneNumber());
//				properties.put("email", object.getEmail());
//				properties.put("qq", object.getQq());
//				properties.put("weixin", object.getWeixin());
//				properties.put("changeState", "add");
//
//				list.add(properties);
//			}
		}
		return list;
	}

	public List<Properties> getListForApproval2(List<Emmp_OrgMember> emmp_OrgMemberList, Emmp_CompanyInfoForm model)
	{

		List<Properties> list = new ArrayList<Properties>();

		Long afId = model.getAfId();//申请单Id
		Sm_ApprovalProcess_AF sm_approvalProcess_af = new Sm_ApprovalProcess_AF();
		if(afId!=null && afId > 0)
		{
			sm_approvalProcess_af = sm_ApprovalProcess_AFDao.findById(afId);
		}

		Long emmp_CompanyInfoId = model.getTableId();
		Emmp_CompanyInfo thePo = emmp_CompanyInfoDao.findById(emmp_CompanyInfoId);
		String busiState = thePo.getBusiState();

		String busiCode = sm_approvalProcess_af.getBusiCode();

		int busiCodeAddOrUpdate = getBusiCodeAddOrUpdate(busiCode);
		if (busiCodeAddOrUpdate == 1) //如果是初始维护审批详情
		{
			if (S_BusiState.NoRecord.equals(busiState)) //如果是未备案，则是新增待提交的审批流，使用Database数据
			{
				if(emmp_OrgMemberList != null)
				{
					for(Emmp_OrgMember orgMemberObj:emmp_OrgMemberList)
					{
						list.add(getSimpleInfo(orgMemberObj));
					}
				}
				return list; //直接返回
			}
		}

		Emmp_CompanyInfoForm emmp_CompanyInfoForm = gson.fromJson(sm_approvalProcess_af.getExpectObjJson(), Emmp_CompanyInfoForm.class);
		Emmp_OrgMemberForm[] emmp_OrgMemberArrayOSS = emmp_CompanyInfoForm.getOrgMemberList();

		if (emmp_OrgMemberArrayOSS == null)
		{
			return list;
		}

		for (Emmp_OrgMemberForm theForm:emmp_OrgMemberArrayOSS)  //
		{
			Properties properties = new MyProperties();

			properties.put("tableId", theForm.getTableId());
			properties.put("theName", theForm.getTheName());
			properties.put("idType", theForm.getIdType());
			properties.put("idNumber", theForm.getIdNumber());
			properties.put("theNameOfDepartment", theForm.getTheNameOfDepartment());
			properties.put("parameterId", theForm.getParameterId());
			properties.put("positionName", theForm.getPositionName());
			properties.put("phoneNumber", theForm.getPhoneNumber());
			properties.put("email", theForm.getEmail());
			properties.put("qq", theForm.getQq());
			properties.put("weixin", theForm.getWeixin());
			properties.put("changeState", theForm.getChangeState());

			list.add(properties);
		}

		return list;
	}

	public int getBusiCodeAddOrUpdate(String busiCode) {
		if (StringUtils.isEmpty(busiCode)) {
			return -1;
		}
		switch (busiCode) {
		case S_BusiCode.busiCode_020101:
		case S_BusiCode.busiCode_020103:
		case S_BusiCode.busiCode_020106:
		case S_BusiCode.busiCode_020108:
		case S_BusiCode.busiCode_020131:
		case S_BusiCode.busiCode_020141:
			return 1;
		case S_BusiCode.busiCode_020102:
		case S_BusiCode.busiCode_020105:
		case S_BusiCode.busiCode_020107:
		case S_BusiCode.busiCode_020109:
		case S_BusiCode.busiCode_020132:
		case S_BusiCode.busiCode_020142:
			return 2;
		default:
			return 1;
		}
	}

	@Override
	public Properties getDetail(Emmp_OrgMember object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("company", object.getCompany());
		properties.put("companyId", object.getCompany().getTableId());
		properties.put("eCodeOfDepartment", object.geteCodeOfDepartment());
		properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
		properties.put("theType", object.getTheType());
		properties.put("theName", object.getTheName());
		properties.put("realName", object.getRealName());
		properties.put("idType", object.getIdType());
		properties.put("idNumber", object.getIdNumber());
		properties.put("phoneNumber", object.getPhoneNumber());
		properties.put("email", object.getEmail());
		properties.put("weixin", object.getWeixin());
		properties.put("qq", object.getQq());
		properties.put("address", object.getAddress());
		properties.put("heardImagePath", object.getHeardImagePath());
		properties.put("hasQC", object.getHasQC());
		properties.put("qualificationInformation", object.getQualificationInformation());
		properties.put("qualificationInformationId", object.getQualificationInformation().getTableId());
		properties.put("remark", object.getRemark());
		properties.put("logId", object.getLogId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Emmp_OrgMember> emmp_OrgMemberList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(emmp_OrgMemberList != null)
		{
			for(Emmp_OrgMember object:emmp_OrgMemberList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("company", object.getCompany());
				properties.put("companyId", object.getCompany().getTableId());
				properties.put("eCodeOfDepartment", object.geteCodeOfDepartment());
				properties.put("theNameOfDepartment", object.getTheNameOfDepartment());
				properties.put("theType", object.getTheType());
				properties.put("theName", object.getTheName());
				properties.put("realName", object.getRealName());
				properties.put("idType", object.getIdType());
				properties.put("idNumber", object.getIdNumber());
				properties.put("phoneNumber", object.getPhoneNumber());
				properties.put("email", object.getEmail());
				properties.put("weixin", object.getWeixin());
				properties.put("qq", object.getQq());
				properties.put("address", object.getAddress());
				properties.put("heardImagePath", object.getHeardImagePath());
				properties.put("hasQC", object.getHasQC());
				properties.put("qualificationInformation", object.getQualificationInformation());
				properties.put("qualificationInformationId", object.getQualificationInformation().getTableId());
				properties.put("remark", object.getRemark());
				properties.put("logId", object.getLogId());
				
				list.add(properties);
			}
		}
		return list;
	}
}
