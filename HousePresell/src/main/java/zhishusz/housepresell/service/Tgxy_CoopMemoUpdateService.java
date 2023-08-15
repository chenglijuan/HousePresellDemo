package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopMemoForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopMemoDao;
import zhishusz.housepresell.database.po.Tgxy_CoopMemo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;

/*
 * Service更新操作：合作备忘录
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopMemoUpdateService
{
	private static final String BUSI_CODE = "06110102";
	@Autowired
	private Tgxy_CoopMemoDao tgxy_CoopMemoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopMemoForm model)
	{
		Properties properties = new MyProperties();
		
		/*
		 * xsz by time 2018-8-14 10:40:42
		 * 1.修改合作备忘录需要传递的非空字段：
		 * 编号、银行名称、签署日期
		 * 
		 * 2.缺少操作人（修改人）字段
		 */

		Integer theState = model.getTheState(); // 状态 默认 0 正常
		// String busiState = model.getBusiState(); // 业务状态
		String eCode = model.geteCode(); // 编号
		// Long userUpdateId = model.getUserUpdateId();
		Long userUpdateId = model.getUserId();
		// Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp(); //最后修改时间
		String eCodeOfCooperationMemo = model.geteCodeOfCooperationMemo(); // 合作备忘录编号（冗余）
		Long bankId = model.getBankId(); // 银行
		// String theNameOfBank = model.getTheNameOfBank(); // 银行名称
		String signDate = model.getSignDate(); // 签署日期

		if (theState == null || theState < 1)
		{
			// 状态默认为 0：正常
			// return MyBackInfo.fail(properties, "'theState'不能为空");
			theState = 0;
		}
		if (userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		// if (eCode == null || eCode.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'eCode'不能为空");
		// }
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "银行信息不能为空");
		}
		// if (theNameOfBank == null || theNameOfBank.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'theNameOfBank'不能为空");
		// }

		/*
		 * xsz by time 2018-8-20 11:10:11
		 * 去除签署日期非空校验：
		 * 业务需求：签署日期非必输字段
		 */
		// if (signDate == null || signDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'signDate'不能为空");
		// }

		// 查询关联关系
		Sm_User userUpdate = (Sm_User) sm_UserDao.findById(userUpdateId);
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "'修改人(Id:" + userUpdateId + ")'不存在");
		}
		Emmp_BankInfo bank = (Emmp_BankInfo) emmp_BankInfoDao.findById(bankId);
		if (bank == null)
		{
			return MyBackInfo.fail(properties, "'银行(Id:" + bankId + ")'不存在");
		}

		// 查询出的银行名称并判断与所选银行名称是否一致，无误执行新增操作
		// String bankName = bank.getTheName();
		//
		// if (!theNameOfBank.equals(bankName))
		// {
		// return MyBackInfo.fail(properties, "所选银行名称无法对应");
		// }
		
		/*
		 * xsz by time 2018-11-21 14:47:06
		 * 判断附件是否必须上传
		 */
		// 判断是否有必传
		Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
		sm_AttachmentCfgForm.setBusiType(BUSI_CODE);
		sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

		// 先判断是否有附件传递
		List<Sm_Attachment> attachmentList;  
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			attachmentList = JSON.parseArray(model.getSmAttachmentList().toString(), Sm_Attachment.class);
		}
		else
		{
			attachmentList = new ArrayList<Sm_Attachment>();
		}
		
		if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
		{

			for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
				// 根据业务判断是否有必传的附件配置
				if (sm_AttachmentCfg.getIsNeeded())
				{
					Boolean isExistAttachment = false;
	
					if (attachmentList.size() > 0)
					{
	
						for (Sm_Attachment sm_Attachment : attachmentList)
						{
							if (sm_AttachmentCfg.geteCode().equals(sm_Attachment.getSourceType()))
							{
								isExistAttachment = true;
								break;
							}
						}
	
					}
	
					if (!isExistAttachment)
					{
						return MyBackInfo.fail(properties, sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
					}
	
				}
			}
		}

		// 获取修改信息主键id
		Long tgxy_CoopMemoId = model.getTableId();
		Tgxy_CoopMemo tgxy_CoopMemo = (Tgxy_CoopMemo) tgxy_CoopMemoDao.findById(tgxy_CoopMemoId);
		if (tgxy_CoopMemo == null)
		{
			return MyBackInfo.fail(properties, "'合作备忘录(Id:" + tgxy_CoopMemoId + ")'不存在");
		}

		// 检查查询出的编号与传递的编号是否一致
		// if (!eCode.equals(tgxy_CoopMemo.geteCode()))
		// {
		// return MyBackInfo.fail(properties, "合作备忘录编号：" + eCode + "不匹配");
		// }

		// 设置修改人
		tgxy_CoopMemo.setUserUpdate(userUpdate);
		tgxy_CoopMemo.setTheState(theState);
		tgxy_CoopMemo.seteCode(eCode);
		tgxy_CoopMemo.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_CoopMemo.seteCodeOfCooperationMemo(eCodeOfCooperationMemo);
		tgxy_CoopMemo.setBank(bank);
		tgxy_CoopMemo.setTheNameOfBank(bank.getTheName());
		tgxy_CoopMemo.setSignDate(signDate);

		tgxy_CoopMemoDao.save(tgxy_CoopMemo);
		
		
		

		/*
		 * xsz by time 2018-8-28 18:22:29
		 * 修改附件
		 * 附件需要先进行删除操作，然后进行重新上传保存功能
		 */
		String smAttachmentJson = null;
		// 查询原本附件信息
		Sm_AttachmentForm from = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tgxy_CoopMemoId);
		from.setTheState(S_TheState.Normal);
		from.setBusiType("06110102");
		from.setSourceId(sourceId);

		// 查询附件
		List<Sm_Attachment> smAttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), from));

		// 删除附件
		if (null != smAttachmentList && smAttachmentList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				sm_Attachment.setUserUpdate(userUpdate);// 操作人
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());// 操作时间
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		// 重新保存附件
		smAttachmentJson = model.getSmAttachmentList().toString();
		if (null != smAttachmentJson && !smAttachmentJson.trim().isEmpty())
		{
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentJson, Sm_Attachment.class);
			for (Sm_Attachment sm_Attachment : gasList)
			{
				//查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
				
				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				
				sm_Attachment.setSourceId(String.valueOf(tgxy_CoopMemoId));
				sm_Attachment.setUserStart(userUpdate);// 创建人
				sm_Attachment.setUserUpdate(userUpdate);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setBusiType("06110102");
				sm_AttachmentDao.save(sm_Attachment);
			}

		}

		properties.put("tableId", tgxy_CoopMemoId);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
