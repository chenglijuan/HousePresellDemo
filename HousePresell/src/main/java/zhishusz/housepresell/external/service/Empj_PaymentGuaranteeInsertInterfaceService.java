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
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_PaymentGuaranteeChild;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.PaymentGuaranteeModel;
import zhishusz.housepresell.service.Tgpf_SocketServerHandler;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 支付保证审批通过接口
 * @author Administrator
 *
 */
@Service
@Transactional
public class Empj_PaymentGuaranteeInsertInterfaceService
{
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;//基础参数
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;//支付保证子表
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;//接口报文表
	
	public static Logger log = LoggerFactory.getLogger(Empj_PaymentGuaranteeInsertInterfaceService.class);

	@SuppressWarnings({ "unchecked", "static-access" })
	public synchronized Properties execute(Empj_PaymentGuarantee paymentGuarantee,BaseForm from)
	{
		Properties properties = new MyProperties();
		
		/*qymc	String	必填	企业名称
		xmmc	String	必填	项目名称
		cbjg	String	必填	承保机构
		cblz	String	必填	承保楼幢
		cbje	String	必填	承保金额
		cbsj	String	必填	承保时间
		fjlb	String	必填	附件类别
		fjm	String	必填	附件名
		bdhh	String	必填	保单涵号
		*/
		
		//基础信息
		Empj_ProjectInfo project = paymentGuarantee.getProject();
		Emmp_CompanyInfo developCompany = project.getDevelopCompany();
		Empj_PaymentGuaranteeChildForm paymentModel = new Empj_PaymentGuaranteeChildForm();
		paymentModel.setTheState(S_TheState.Normal);
		paymentModel.setEmpj_PaymentGuarantee(paymentGuarantee);
		
		StringBuffer buildBuffer = new StringBuffer();
		List<Empj_PaymentGuaranteeChild> paymentList = new ArrayList<>();
		paymentList = empj_PaymentGuaranteeChildDao.findByPage(empj_PaymentGuaranteeChildDao.getQuery(empj_PaymentGuaranteeChildDao.getBasicHQL(), paymentModel));
		if(!paymentList.isEmpty())
		{
			for (int i = 0; i < paymentList.size() ; i++) {
				if(i == 0)
				{
					buildBuffer.append(paymentList.get(i).geteCodeFromConstruction());
				}
				else
				{
					buildBuffer.append(","+paymentList.get(i).geteCodeFromConstruction());
				}
			}
		}
		
		String guaranteeCompany = paymentGuarantee.getGuaranteeCompany();
		
		PaymentGuaranteeModel model = new PaymentGuaranteeModel();
		model.setQymc(developCompany.getTheName());
		model.setXmmc(project.getTheName());
		model.setCbjg("1".equals(guaranteeCompany)?"银行":"".equals(guaranteeCompany)?"保险公司":"担保公司");
		model.setCblz(buildBuffer.toString());
		model.setCbje(paymentGuarantee.getActualAmount().toString());
		model.setCbsj(paymentGuarantee.getApplyDate());
		model.setBdhh(paymentGuarantee.getGuaranteeNo());
		
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		//开发企业附件信息
		sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setSourceId(String.valueOf(paymentGuarantee.getTableId()));
		sm_AttachmentForm.setBusiType("06120401");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 加载所有相关附件信息
		List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
				.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
		// 查询同一附件类型下的所有附件信息（附件信息归类）
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setBusiType("06120401");
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
							sb.append(sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink());
						}
						else
						{
							sb.append(","+sm_AttachmentCfg.getTheName()+"#"+sm_AttachmentList.get(i).getTheLink());
						}
						
					}
				}
			}
		}
		
		model.setFjlb(sb.toString());
		model.setFjm(sb.toString());
		
		/*参数名	类型	描述
		--	Int	返回值“1”表示数据插入成功
		返回值“0”表示数据插入异常*/
		//构建参数，发送
		String query = model.toString();
		
		Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("70");
		paraModel.setTheValue("700004");
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
	
}
