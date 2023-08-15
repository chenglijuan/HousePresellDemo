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
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.service.Tgpf_SocketServerHandler;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 三方协议撤销或删除接口
 * @author Administrator
 *
 */
@Service
@Transactional
public class Tgxy_TripleAgreementDeleteInterfaceService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;//基础参数
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;//接口报文表
	
	public static Logger log = LoggerFactory.getLogger(Tgxy_TripleAgreementDeleteInterfaceService.class);

	@SuppressWarnings({ "unchecked", "static-access" })
	public synchronized Properties execute(Tgxy_TripleAgreement tripleAgreement,String czfs,BaseForm from)
	{
		Properties properties = new MyProperties();
		
		/*xybh	String	必填	协议编号
		czfs	String	必填	操作方式
		gzlx
		0-删除
		1-撤销*/
		
		TripleAgreementModel model = new TripleAgreementModel();
		model.setXybh(tripleAgreement.geteCodeOfTripleAgreement());
		model.setCzfs(czfs);
		
		/*--	Int	返回值“1”表示数据插入成功
		返回值“0”表示数据插入异常*/
		//构建参数，发送
		String query = model.toStringDelete();
		
		Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
		paraModel.setParametertype("70");
		paraModel.setTheValue("700002");
		List<Sm_BaseParameter> list = new ArrayList<>();
		list = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
		if(list.isEmpty())
		{
			return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
		}
		String url = list.get(0).getTheName();
		
		//正式接口请求
		int restFul = SocketUtil.getInstance().getRestFul(url, query);
		
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
		
		log.info("query:"+query);
		log.info("tgpf_SocketMsg:"+tgpf_SocketMsg.toString());
		
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);
		
		if(restFul == 0)
		{
			return MyBackInfo.fail(properties, "请求异常，请稍后再试！");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;

	}
}
