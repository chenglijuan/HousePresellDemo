package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementDeleteInterfaceService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementBatchDeleteService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;//合同
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;//买受人
	@Autowired
	private Tgxy_TripleAgreementDeleteInterfaceService deleteInterfaceService;
	
	

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		// 获取操作人Id
		Long userId = model.getUserId();

		if (userId == null || userId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		// 查询操作人信息
		Sm_User sm_User = (Sm_User) sm_UserDao.findById(userId);
		if (sm_User == null)
		{
			return MyBackInfo.fail(properties, "操作人信息查询为空");
		}

		for (Long tableId : idArr)
		{
			Tgxy_TripleAgreement tgxy_TripleAgreement = (Tgxy_TripleAgreement) tgxy_TripleAgreementDao
					.findById(tableId);
			if (tgxy_TripleAgreement == null)
			{
				return MyBackInfo.fail(properties, "'三方协议(Id:" + tableId + ")'不存在");
			}

			/*
			 * xsz by time 2019-4-2 19:28:16
			 * 判断是否有入账金额
			 */
			Double totalAmount = tgxy_TripleAgreement.getTotalAmount();
			if(null != totalAmount && totalAmount > 0)
			{
				return MyBackInfo.fail(properties, "协议编号："+tgxy_TripleAgreement.geteCodeOfTripleAgreement()+"已存在入账资金，不可删除！");
			}
			
			// 状态设置为删除状态 1,同时更新最后操作时间
			tgxy_TripleAgreement.setTheState(S_TheState.Deleted);
			tgxy_TripleAgreement.setUserUpdate(sm_User);
			tgxy_TripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
			
			//删除三方协议相关买受人信息
			Tgxy_BuyerInfoForm buyerModel = new Tgxy_BuyerInfoForm();
			buyerModel.seteCodeOfContract(tgxy_TripleAgreement.geteCodeOfContractRecord());
			List<Tgxy_BuyerInfo> buyList = tgxy_BuyerInfoDao.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), buyerModel));
			if(null != buyList && buyList.size()>0)
			{
				for (Tgxy_BuyerInfo tgxy_BuyerInfo : buyList)
				{
					tgxy_BuyerInfoDao.delete(tgxy_BuyerInfo);
				}
			}
			
			//删除三方协议相关合同信息
			Tgxy_ContractInfoForm contractModel = new Tgxy_ContractInfoForm();
			contractModel.seteCodeOfContractRecord(tgxy_TripleAgreement.geteCodeOfContractRecord());
			List<Tgxy_ContractInfo> contractList = tgxy_ContractInfoDao.findByPage(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), contractModel));
			if(null != contractList && contractList.size()>0)
			{
				for (Tgxy_ContractInfo tgxy_ContractInfo : contractList)
				{
//					tgxy_ContractInfo.setTheState(S_TheState.Deleted);
					tgxy_ContractInfoDao.delete(tgxy_ContractInfo);
				}
			}		

			// 根据查询是否存在附件信息设置是否删除附件 (先使用 A 方案 后期使用B方案)
			// 设置附件model条件信息，进行附件条件查询
			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(tableId));
			sm_AttachmentForm.setBusiType("06110301");
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有相关附件信息,for循环执行删除
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

			if (null != sm_AttachmentList && sm_AttachmentList.size() > 0)
			{

				for (Sm_Attachment sm_Attachment : sm_AttachmentList)
				{
					sm_Attachment.setUserUpdate(sm_User);
					sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					sm_Attachment.setTheState(S_TheState.Deleted);
					sm_AttachmentDao.save(sm_Attachment);
				}

			}
			
			//删除审批流
			deleteService.execute(tableId, model.getBusiCode());
			
			/**
			 * xsz by time 2019-7-8 17:17:43
			 * 与档案系统接口对接
			 * ====================start==================
			 */
			
			Properties execute = deleteInterfaceService.execute(tgxy_TripleAgreement, "0", model);
			if(execute.isEmpty()|| S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result)))
			{
				return MyBackInfo.fail(properties, "与档案系统对接失败，请稍后重试！");
			}
			
			/**
			 * xsz by time 2019-7-8 17:17:43
			 * 与档案系统接口对接
			 * ====================end==================
			 */
			
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
