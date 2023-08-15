package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：托管合作协议
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_EscrowAgreementRebuild extends RebuilderBase<Tgxy_EscrowAgreement>
{
	@Autowired
	private Empj_BuildingInfoRebuild buildingInfoRebuild;
	@Override
	public Properties getSimpleInfo(Tgxy_EscrowAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		// 设置列表返回字段

		properties.put("tableId", object.getTableId());
		// properties.put("theState", object.getTheState());
		// properties.put("busiState", object.getBusiState());
		// properties.put("eCode", object.geteCode());
		// properties.put("userStart", object.getUserStart());
		// properties.put("userStartId", object.getUserStart().getTableId());
		// properties.put("createTimeStamp", object.getCreateTimeStamp());
		// properties.put("lastUpdateTimeStamp",
		// object.getLastUpdateTimeStamp());
		// properties.put("userRecord", object.getUserRecord());
		// properties.put("userRecordId", object.getUserRecord().getTableId());
		// properties.put("recordTimeStamp", object.getRecordTimeStamp());
		// properties.put("escrowCompany", object.getEscrowCompany());
		properties.put("agreementVersion", object.getAgreementVersion());// 协议版本
		properties.put("eCodeOfAgreement", object.geteCodeOfAgreement());// 托管合作协议编号
		properties.put("contractApplicationDate", object.getContractApplicationDate());// 签约申请日期
		// properties.put("developCompany", object.getDevelopCompany());
		// properties.put("developCompanyId",
		// object.getDevelopCompany().getTableId());
		// properties.put("eCodeOfDevelopCompany",
		// object.geteCodeOfDevelopCompany());
		if (null == object.getTheNameOfDevelopCompany())
		{
			properties.put("theNameOfDevelopCompany", " ");// 开发企业名称
		}
		else
		{
			properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());// 开发企业名称

		}

		// properties.put("cityRegion", object.getCityRegion());
		// properties.put("cityRegionId", object.getCityRegion().getTableId());
		// properties.put("project", object.getProject());
		// properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());// 项目名称
		properties.put("theNameOfCityRegion", object.getCityRegion().getTheName());// 区域名称
		// properties.put("buildingInfoList", object.getBuildingInfoList());
		// properties.put("OtherAgreedMatters", object.getOtherAgreedMatters());
		// properties.put("disputeResolution", object.getDisputeResolution());
		/*
		 * 1-已保存（保存）
		 * 2-已申请（提交）
		 * 3-项目部门已受理（项目部审核）
		 * 4-项目部门退回
		 * 5-受理完成（法务部审核）
		 * 6-法务部门退回
		 * 7-已备案（项目部门备案）
		 */
		properties.put("businessProcessState", object.getBusinessProcessState());// 申请状态
		properties.put("agreementState", object.getAgreementState());// 协议状态
																		// 0--正常
																		// 1--已终止
		properties.put("buildingInfoCodeList", object.getBuildingInfoCodeList());// 楼幢编号
		
		//审批状态
		properties.put("approvalState", object.getApprovalState());

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_EscrowAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();
		//审批状态
		properties.put("approvalState", object.getApprovalState());
		properties.put("tableId", object.getTableId());

		// 获取相关创建人和修改人等关联信息
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());

		if (null == object.getUserRecord() || null == object.getUserRecord().getTableId())
		{
			properties.put("userRecord", new Sm_User());
			properties.put("userRecordId", " ");
		}
		else
		{
			properties.put("userRecord", object.getUserRecord());
			properties.put("userRecordId", object.getUserRecord().getTableId());
		}

		// 处理时间格式yyyy-MM-dd
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		if (null == object.getRecordTimeStamp())
		{
			properties.put("recordTimeStamp", " ");
		}
		else
		{
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		}

		if (null == object.geteCodeOfAgreement() || object.geteCodeOfAgreement().trim().isEmpty())
		{
			properties.put("eCodeOfAgreement", " ");// 托管合作协议编号
		}
		else
		{
			properties.put("eCodeOfAgreement", object.geteCodeOfAgreement());// 托管合作协议编号
		}

		if (null == object.getTheNameOfDevelopCompany())
		{
			properties.put("theNameOfDevelopCompany", " ");// 开发企业名称
		}
		else
		{
			properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());// 开发企业名称

		}
		properties.put("escrowCompany", object.getEscrowCompany());// 托管机构名称
		properties.put("theNameOfProject", object.getTheNameOfProject());// 项目名称
		properties.put("agreementVersion", object.getAgreementVersion());// 协议版本
		properties.put("contractApplicationDate", object.getContractApplicationDate());// 签约申请日期
		properties.put("buildingInfoCodeList", object.getBuildingInfoCodeList());// 楼幢编号
		properties.put("disputeResolution", object.getDisputeResolution());// 争议解决方式

		/*
		 * 1-已保存（保存）
		 * 2-已申请（提交）
		 * 3-项目部门已受理（项目部审核）
		 * 4-项目部门退回
		 * 5-受理完成（法务部审核）
		 * 6-法务部门退回
		 * 7-已备案（项目部门备案）
		 */
		properties.put("businessProcessState", object.getBusinessProcessState());// 申请状态
		properties.put("agreementState", object.getAgreementState());// 协议状态
																		// 0--正常
																		// 1--已终止
		properties.put("OtherAgreedMatters", object.getOtherAgreedMatters());//其他约定事项
		// 所属区域
		properties.put("cityRegion", object.getCityRegion());
		properties.put("cityRegionId", object.getCityRegion().getTableId());
		
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		
		List<Empj_BuildingInfo> infoList = object.getBuildingInfoList();
		/*
		 * xsz by time 2018-12-27 13:36:16
		 * 已与zcl确认地址取项目地址
		 * ----start-------
		 */
//		for (Empj_BuildingInfo empj_BuildingInfo : infoList) {
//			empj_BuildingInfo.setPosition(object.getProject().getAddress());
//		}
		/*
		 * xsz by time 2018-12-27 13:36:16
		 * 已与zcl确认地址取项目地址
		 * ----end-------
		 */
		//楼幢信息
		properties.put("buildingInfoList", buildingInfoRebuild.executeSuperviseBuildingList(infoList,object.getAgreementVersion()));

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_EscrowAgreement> tgxy_EscrowAgreementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_EscrowAgreementList != null)
		{
			for (Tgxy_EscrowAgreement object : tgxy_EscrowAgreementList)
			{
				Properties properties = new MyProperties();
				//审批状态
				properties.put("approvalState", object.getApprovalState());
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
				properties.put("escrowCompany", object.getEscrowCompany());
				properties.put("agreementVersion", object.getAgreementVersion());
				properties.put("eCodeOfAgreement", object.geteCodeOfAgreement());
				properties.put("contractApplicationDate", object.getContractApplicationDate());
				properties.put("developCompany", object.getDevelopCompany());
				properties.put("developCompanyId", object.getDevelopCompany().getTableId());
				properties.put("eCodeOfDevelopCompany", object.geteCodeOfDevelopCompany());
				properties.put("theNameOfDevelopCompany", object.getTheNameOfDevelopCompany());
				properties.put("cityRegion", object.getCityRegion());
				properties.put("cityRegionId", object.getCityRegion().getTableId());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("buildingInfoList", object.getBuildingInfoList());
				properties.put("OtherAgreedMatters", object.getOtherAgreedMatters());
				properties.put("disputeResolution", object.getDisputeResolution());
				properties.put("businessProcessState", object.getBusinessProcessState());
				properties.put("agreementState", object.getAgreementState());

				list.add(properties);
			}
		}
		return list;
	}
}
