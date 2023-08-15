package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：三方协议
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_TripleAgreementRebuild extends RebuilderBase<Tgxy_TripleAgreement>
{
	@Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	
	@Override
	public Properties getSimpleInfo(Tgxy_TripleAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());// 三方协议号
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());// 合同备案号
		properties.put("buyerName", object.getBuyerName());// 买受人名称
		properties.put("sellerName", object.getSellerName());// 开发企业名称
		properties.put("theNameOfProject", object.getTheNameOfProject());// 项目名称
		properties.put("theStateOfTripleAgreement", object.getTheStateOfTripleAgreement());// 协议状态
		properties.put("firstPaymentAmount", object.getFirstPayment());// 首付款
		// 0-未打印（新增）
		// 1-已打印未上传（打印三方协议）
		// 2-已上传（代理公司上传三方协议和商品房买卖合同签字页）
		// 3-已备案（项目部备案）
		// 4-备案退回(项目部门备案退回）
		properties.put("theStateOfTripleAgreementFiling", object.getTheStateOfTripleAgreementFiling());// 归档状态
		// 0-为空（默认）
		// 1-待归档（三方协议状态为已备案）
		// 2-已归档（档案人员归档成功）
		
		//审批状态
		properties.put("approvalState", object.getApprovalState());

		return properties;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Properties getDetail(Tgxy_TripleAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();
		//审批状态
		properties.put("approvalState", object.getApprovalState());
		properties.put("tableId", object.getTableId());// 协议主键
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());// 三方协议号
		properties.put("tripleAgreementTimeStamp", object.getTripleAgreementTimeStamp());// 协议日期
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());// 合同备案号
		properties.put("sellerName", object.getSellerName());// 出卖人
		properties.put("buyerName", object.getBuyerName());// 买受人名称
		properties.put("firstPaymentAmount", object.getFirstPayment());// 首付款

		properties.put("theNameOfProject", object.getTheNameOfProject());// 项目名称
		properties.put("projectId", object.getProject().getTableId());// 项目主键
		
		properties.put("buildingId", object.getBuildingInfo().getTableId());// 楼幢主键

		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
		properties.put("eCodeOfUnit", object.geteCodeOfUnit());// 单元unitInfo
		if (null != object.getUnitInfo())
		{
			properties.put("eCodeOfUnit", object.getUnitInfo().getTableId());// 单元主键
		}
		else
		{
			properties.put("eCodeOfUnit", "-");// 单元主键
		}

		properties.put("unitRoom", object.getUnitRoom());// 户室号
		properties.put("houseId", object.getHouse().getTableId());// 户室号Id
		properties.put("position", null == object.getRoomlocation() ? "" : object.getRoomlocation());
		

		properties.put("theStateOfTripleAgreement", object.getTheStateOfTripleAgreement());// 协议状态
		// 0-未打印（新增）
		// 1-已打印未上传（打印三方协议）
		// 2-已上传（代理公司上传三方协议和商品房买卖合同签字页）
		// 3-已备案（项目部备案）
		// 4-备案退回(项目部门备案退回）
		properties.put("theStateOfTripleAgreementFiling", object.getTheStateOfTripleAgreementFiling());// 归档状态
		// 0-为空（默认）
		// 1-待归档（三方协议状态为已备案）
		// 2-已归档（档案人员归档成功）
		properties.put("theStateOfTripleAgreementEffect", object.getTheStateOfTripleAgreementEffect());// 效力状态
		// 0-为空（默认）
		// 1-生效（代理公司上传三方协议和商品房买卖合同签字页后）
		// 2-退房退款待处理（退房退款流程发起时标记三方协议状态为待处理，）
		// 3-失效：
		// a、退房退款流程走完之后自动反写为失效；
		// b、同一户有新的三方协议备案成功（自动失效）
		// c、长时间没有入账，暂定半年协议状态为失效，手工发起失效流程
		properties.put("printMethod", object.getPrintMethod());// 打印方式
		properties.put("userUpdate", object.getUserUpdate().getTheName());// 操作人
		if (null != object.getUserRecord())
		{
			properties.put("userRecord", object.getUserRecord().getTheName());// 备案人
		}
		else
		{
			properties.put("userRecord", "-");// 备案人
		}
		// 处理时间格式yyyy-MM-dd
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));// 最后修改日期

		if (null != object.getRecordTimeStamp())
		{
			properties.put("recordTimeStamp", MyDatetime.getInstance().dateToString2(object.getRecordTimeStamp()));
		}
		else
		{
			properties.put("recordTimeStamp", "-");
		}
		
		
		properties.put("loanAmount", object.getLoanAmount());
		properties.put("contractSumPrice", object.getContractAmount());
		properties.put("firstPaymentAmount", object.getFirstPayment());
		
		
		/**
         * xsz by time 2019-4-30 16:22:49
         * 操作时间取申请单的提交时间
         * 
         */
        Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06110301");
        afModel.setSourceId(object.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if(null == afList || afList.size() == 0)
        {
        	properties.put("lastUpdateTimeStamp", "-");
            properties.put("lastUpdateTimeStampString", "-");
        }
        else
        {
        	Sm_ApprovalProcess_AF sm_ApprovalProcess_AF = afList.get(0);
        	properties.put("lastUpdateTimeStamp",
    				MyDatetime.getInstance().dateToString2(sm_ApprovalProcess_AF.getStartTimeStamp()));// 最后修改日期
        }
		

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_TripleAgreement> tgxy_TripleAgreementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_TripleAgreementList != null)
		{
			for (Tgxy_TripleAgreement object : tgxy_TripleAgreementList)
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
				properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
				properties.put("tripleAgreementTimeStamp", object.getTripleAgreementTimeStamp());
				properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
				properties.put("sellerName", object.getSellerName());
				properties.put("escrowCompany", object.getEscrowCompany());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("buildingInfo", object.getBuildingInfo());
				properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("unitInfo", object.getUnitInfo());
				properties.put("unitInfoId", object.getUnitInfo().getTableId());
				properties.put("eCodeOfUnit", object.geteCodeOfUnit());
				properties.put("unitRoom", object.getUnitRoom());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("contractAmount", object.getContractAmount());
				properties.put("firstPayment", object.getFirstPayment());
				properties.put("loanAmount", object.getLoanAmount());
				properties.put("buyerInfoSet", object.getBuyerInfoSet());
				properties.put("theStateOfTripleAgreement", object.getTheStateOfTripleAgreement());
				properties.put("theStateOfTripleAgreementFiling", object.getTheStateOfTripleAgreementFiling());
				properties.put("theStateOfTripleAgreementEffect", object.getTheStateOfTripleAgreementEffect());
				properties.put("printMethod", object.getPrintMethod());
				properties.put("theAmountOfRetainedEquity", object.getTheAmountOfRetainedEquity());
				properties.put("theAmountOfInterestRetained", object.getTheAmountOfInterestRetained());
				properties.put("theAmountOfInterestUnRetained", object.getTheAmountOfInterestUnRetained());
				properties.put("totalAmountOfHouse", object.getTotalAmountOfHouse());
				//审批状态
				properties.put("approvalState", object.getApprovalState());
				list.add(properties);
			}
		}
		return list;
	}
}
