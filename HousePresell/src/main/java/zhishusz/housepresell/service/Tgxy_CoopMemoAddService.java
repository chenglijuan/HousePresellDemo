package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
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
 * Service添加操作：合作备忘录
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopMemoAddService
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
		 * xsz by time 2018-8-13 14:15:35
		 * 1.新增合作备忘录需要字段：
		 * 编号、银行id、银行名称、签署日期、创建人id、创建日期、修改人id、最后修改时间
		 * 
		 * 2.传递过来的编号唯一，如果已存在则提示不可新增
		 * 
		 */

		String eCode = model.geteCode();// 编号
		// Long userStartId = model.getUserStartId();// 创建人id

		Long userStartId = model.getUserId();

		Long bankId = model.getBankId();// 银行id
		String signDate = model.getSignDate();// 签署日期yyyy-MM-dd

		if (eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "合作备忘录编号不能为空");
		}

		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "请选择银行");
		}

		/*
		 * xsz by time 2018-8-20 11:10:11
		 * 去除签署日期非空校验：
		 * 业务需求：签署日期非必输字段
		 */
		// if (signDate == null || signDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "'signDate'不能为空");
		// }

		// 通过传递过来的合作备忘录编号(eCode)判断是否已存在，如果存在，则提示不可新增
		if (null != eCode && !eCode.trim().isEmpty())
		{
			Tgxy_CoopMemoForm eCodeModel = new Tgxy_CoopMemoForm();
			eCodeModel.seteCode(eCode);
			eCodeModel.setTheState(S_TheState.Normal);
			Integer totalCount = tgxy_CoopMemoDao
					.findByPage_Size(tgxy_CoopMemoDao.getQuery_Size(tgxy_CoopMemoDao.getBasicHQL(), eCodeModel));

			if (totalCount > 0)
			{
				return MyBackInfo.fail(properties, "合作备忘录编号：" + eCode + "已存在，无法继续新增");
			}
		}
		// 查询关联关系
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "创建人信息查询为空");
		}
		Emmp_BankInfo bank = (Emmp_BankInfo) emmp_BankInfoDao.findById(bankId);
		if (bank == null)
		{
			return MyBackInfo.fail(properties, "银行ID:" + bankId + "未查询到相关信息");
		}
		
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

		Tgxy_CoopMemo tgxy_CoopMemo = new Tgxy_CoopMemo();
		tgxy_CoopMemo.setTheState(S_TheState.Normal);
		tgxy_CoopMemo.seteCode(eCode);
		tgxy_CoopMemo.setUserStart(userStart);
		tgxy_CoopMemo.setUserUpdate(userStart);
		// 时间取系统当前时间
		tgxy_CoopMemo.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_CoopMemo.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_CoopMemo.seteCodeOfCooperationMemo(eCode);
		tgxy_CoopMemo.setBank(bank);
		tgxy_CoopMemo.setTheNameOfBank(bank.getTheName());
		tgxy_CoopMemo.setSignDate(signDate);
		Serializable tableId = tgxy_CoopMemoDao.save(tgxy_CoopMemo);

		/*
		 * xsz by time 2018-9-18 10:19:03
		 * 附件信息后台整合
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			// "[{\"sourseType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"},{\"sourseType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"}]";
			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				sm_Attachment.setBusiType("06110102");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setUserStart(userStart);// 创建人
				sm_Attachment.setUserUpdate(userStart);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
