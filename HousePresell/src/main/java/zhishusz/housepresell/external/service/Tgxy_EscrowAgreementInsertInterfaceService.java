package zhishusz.housepresell.external.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.EscrowAgreementModel;
import zhishusz.housepresell.service.Tgpf_SocketServerHandler;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 合作协议审批通过接口
 * @author Administrator
 *
 */
@Service
@Transactional
public class Tgxy_EscrowAgreementInsertInterfaceService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;//基础参数
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;//备案价格
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;//接口报文表
	
	public static Logger log = LoggerFactory.getLogger(Tgxy_EscrowAgreementInsertInterfaceService.class);

	@SuppressWarnings({ "unchecked", "static-access" })
	public synchronized Properties execute(Tgxy_EscrowAgreement escrowAgreement,BaseForm from)
	{
		Properties properties = new MyProperties();
		
		/*xybh	String	必填	协议编号
		qymc	String	必填	企业名称
		xmmc	String	必填	项目名称
		tglz	String	必填	托管楼幢
		qyrq	String	必填	签约日期
		ssqy	String	必填	所属区域
		qtydsx	String	必填	其他约定事项
		fjm	String	必填	附件名
		xmdm	String	必填	项目代码
		*/
		
		//基础信息
		Empj_ProjectInfo project = escrowAgreement.getProject();
		Emmp_CompanyInfo developCompany = project.getDevelopCompany();
		List<Empj_BuildingInfo> buildingInfoList = escrowAgreement.getBuildingInfoList();
		
		EscrowAgreementModel model = new EscrowAgreementModel();
		model.setXybh(escrowAgreement.geteCode());
		model.setQymc(escrowAgreement.getTheNameOfDevelopCompany());
		model.setXmmc(escrowAgreement.getTheNameOfProject());
		model.setQyrq(escrowAgreement.getContractApplicationDate());
		model.setTglz(escrowAgreement.getBuildingInfoCodeList());
		model.setSsqy(escrowAgreement.getCityRegion().getTheName());
		model.setQtydsx(null == escrowAgreement.getOtherAgreedMatters()?"":escrowAgreement.getOtherAgreedMatters());
		model.setXmdm(String.valueOf(escrowAgreement.getProject().getTableId()));
//		model.setXmdm("789456");
		
		
		/*备    注	附件包括：
		（1）合作协议关联开发企业的附件；
		（2）合作协议关联开发项目的附件；
		（3）合作协议关联楼幢的附件；	
		（4）合作协议关联楼幢的备案价格附件；
		（5）合作协议电子文本及合作协议上传的附件。*/
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		//开发企业附件信息
		Long developCompanyId = developCompany.getTableId();
		sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(developCompanyId));
		sm_AttachmentForm.setBusiType("020101");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
		// 查询同一附件类型下的所有附件信息（附件信息归类）
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setBusiType("020101");
		form.setTheState(S_TheState.Normal);

		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));
		
		StringBuffer sb = new StringBuffer();
		
		if(!smAttachmentCfgList.isEmpty()&&!sm_AttachmentList.isEmpty())
		{
			for (int i = 0;i< sm_AttachmentList.size();i++)
			{
				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) 
				{
					if(sm_AttachmentCfg.geteCode().equals(sm_AttachmentList.get(i).getSourceType()))
					{
						if(i == 0)
						{
							sb.append("开发企业附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#1");
						}
						else
						{
							sb.append(",开发企业附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#1");
						}
						
					}
				}
			}
		}
		//开发项目附件信息
		Long projectId = project.getTableId();
		sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(projectId));
		sm_AttachmentForm.setBusiType("03010101");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		sm_AttachmentList = new ArrayList<>();
		sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
		// 查询同一附件类型下的所有附件信息（附件信息归类）
		form = new Sm_AttachmentCfgForm();
		form.setBusiType("03010101");
		form.setTheState(S_TheState.Normal);

		smAttachmentCfgList = new ArrayList<>();
		smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));
		
		if(!smAttachmentCfgList.isEmpty()&&!sm_AttachmentList.isEmpty())
		{
			for (int i = 0;i< sm_AttachmentList.size();i++)
			{
				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) 
				{
					if(sm_AttachmentCfg.geteCode().equals(sm_AttachmentList.get(i).getSourceType()))
					{
						sb.append(",项目附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#1");
					}
				}
			}
		}
		
		//楼幢附件信息
		for (Empj_BuildingInfo empj_BuildingInfo : buildingInfoList) {
			sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(String.valueOf(empj_BuildingInfo.getTableId()));
			sm_AttachmentForm.setBusiType("03010201");
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有相关附件信息
			sm_AttachmentList = new ArrayList<>();
			sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
			// 查询同一附件类型下的所有附件信息（附件信息归类）
			form = new Sm_AttachmentCfgForm();
			form.setBusiType("03010201");
			form.setTheState(S_TheState.Normal);

			smAttachmentCfgList = new ArrayList<>();
			smAttachmentCfgList = sm_AttachmentCfgDao
					.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));
			
			if(!smAttachmentCfgList.isEmpty()&&!sm_AttachmentList.isEmpty())
			{
				for (int i = 0;i< sm_AttachmentList.size();i++)
				{
					for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) 
					{
						if(sm_AttachmentCfg.geteCode().equals(sm_AttachmentList.get(i).getSourceType()))
						{
							sb.append(",施工楼幢"+empj_BuildingInfo.geteCodeFromConstruction()+"附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#0");
						}
					}
				}
			}
		}
		//楼幢备案价格附件信息
		for (Empj_BuildingInfo empj_BuildingInfo : buildingInfoList) {
			Tgpj_BuildingAvgPriceForm avgPriceModel = new Tgpj_BuildingAvgPriceForm();
			avgPriceModel.setTheState(S_TheState.Normal);
			avgPriceModel.setBuildingInfo(empj_BuildingInfo);
			
			List<Tgpj_BuildingAvgPrice> list = new ArrayList<>();
			list = tgpj_BuildingAvgPriceDao.findByPage(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), avgPriceModel));
			if(!list.isEmpty())
			{
				for (Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice : list) {
					
					sm_AttachmentForm = new Sm_AttachmentForm();
					sm_AttachmentForm.setSourceId(String.valueOf(tgpj_BuildingAvgPrice.getTableId()));
					sm_AttachmentForm.setBusiType("03010301");
					sm_AttachmentForm.setTheState(S_TheState.Normal);

					// 加载所有相关附件信息
					sm_AttachmentList = new ArrayList<>();
					sm_AttachmentList = sm_AttachmentDao
							.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
					// 查询同一附件类型下的所有附件信息（附件信息归类）
					form = new Sm_AttachmentCfgForm();
					form.setBusiType("03010301");
					form.setTheState(S_TheState.Normal);

					smAttachmentCfgList = new ArrayList<>();
					smAttachmentCfgList = sm_AttachmentCfgDao
							.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));
					
					if(!smAttachmentCfgList.isEmpty()&&!sm_AttachmentList.isEmpty())
					{
						for (int i = 0;i< sm_AttachmentList.size();i++)
						{
							for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) 
							{
								if(sm_AttachmentCfg.geteCode().equals(sm_AttachmentList.get(i).getSourceType()))
								{
									sb.append(",施工楼幢"+empj_BuildingInfo.geteCodeFromConstruction()+"备案价格附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#0");
								}
							}
						}
					}
					
				}
			}
			
		}
		
		//合作协议信息及电子签章文件信息
		sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(escrowAgreement.getTableId()));
		sm_AttachmentForm.setBusiType("06110201");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		sm_AttachmentList = new ArrayList<>();
		sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
		
		// 查询同一附件类型下的所有附件信息（附件信息归类）
		form = new Sm_AttachmentCfgForm();
		form.setBusiType("06110201");
		form.setTheState(S_TheState.Normal);

		smAttachmentCfgList = new ArrayList<>();
		smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));
		
		if(!smAttachmentCfgList.isEmpty()&&!sm_AttachmentList.isEmpty())
		{
			for (int i = 0;i< sm_AttachmentList.size();i++)
			{
				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList) 
				{
					if(sm_AttachmentCfg.geteCode().equals(sm_AttachmentList.get(i).getSourceType()))
					{
						sb.append(",合作协议附件"+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink()+"#0");
					}
				}
			}
		}
		
		//查询合作协议电子PDF
		Sm_Attachment attachment = isSaveAttachment("06110201", String.valueOf(escrowAgreement.getTableId()));
		if(null == attachment)
		{
			sb.append(",合作协议电子文本#");
		}
		else
		{
			sb.append(",合作协议电子文本#"+attachment.getTheLink()+"#0");
		}
		
		model.setFjm(sb.toString());
		
		/*参数名	类型	描述
		--	Int	返回值“1”表示数据插入成功
		返回值“0”表示数据插入异常*/
		//构建参数，发送
		String query = model.toString();
		
		Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("70");
		paraModel.setTheValue("700003");
		List<Sm_BaseParameter> list = new ArrayList<>();
		list = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
		if(list.isEmpty())
		{
			return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
		}
		String url = list.get(0).getTheName();
		
		log.info("modelquery:"+query);
		
		//正式接口请求
		int restFul = 0;
		try {
			restFul = SocketUtil.getInstance().getRestFul(url, query);
		} catch (Exception e) {
			restFul = 0;
			log.error("exception-query:"+query);
		}
		
		//记录接口交互信息
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
		tgpf_SocketMsg.setUserStart(from.getUser());// 创建人
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
		tgpf_SocketMsg.setUserUpdate(from.getUser());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
		tgpf_SocketMsg.setMsgStatus(1);// 发送状态
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
		
		tgpf_SocketMsg.setRemark(url);
		tgpf_SocketMsg.setMsgDirection("DAOUT");// 报文方向
//		tgpf_SocketMsg.setMsgContent(query);// 报文内容
		tgpf_SocketMsg.setMsgContentArchives(query);// 报文内容
		tgpf_SocketMsg.setReturnCode(String.valueOf(restFul));// 返回码
		
		log.info("modeltgpf_SocketMsg:"+tgpf_SocketMsg.toString());
		
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);
		
		if(restFul == 0)
		{
			return MyBackInfo.fail(properties, "提交异常，请稍后再试！");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}
	
	/**
	 * 是否存在PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment(String sourceBusiCode, String sourceId)
	{
		//合作协议打印编码
		String attacmentcfg = "240104";
		
		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);
		
		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		
		Sm_AttachmentForm form = new Sm_AttachmentForm();
		form.setSourceId(sourceId);
		form.setBusiType(sourceBusiCode);
		form.setSourceType(sm_AttachmentCfg.geteCode());
		form.setTheState(S_TheState.Normal);

		Sm_Attachment attachment = sm_AttachmentDao
				.findOneByQuery_T(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), form));

		if (null == attachment)
		{
			return null;
		}
		return attachment;
	}
	
	/**
	 * 是否进行档案配置
	 * 
	 * @param attacmentcfg
	 *            档案类型编码
	 * @return
	 */
	Sm_AttachmentCfg isSaveAttachmentCfg(String attacmentcfg)
	{
		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = sm_AttachmentCfgDao
				.findOneByQuery_T(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		return sm_AttachmentCfg;
	}
}
