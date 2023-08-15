package zhishusz.housepresell.external.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RangeForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_Permission_Range;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_CommonMessageType;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.service.Sm_PushletService;
import zhishusz.housepresell.util.MyDatetime;

/**
 * 三方协议归档结果反馈接口
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class Tgxy_TripleAgreementAnswerInterfaceService {
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;//接口报文表
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_PushletService sm_PushletService;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// 业务编码
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
    private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;
	@Autowired
	private Sm_Permission_RangeDao sm_Permission_RangeDao;

	@SuppressWarnings("unchecked")
	public Integer execute(TripleAgreementModel model) {

		/*
		 * 托管系统接收处理逻辑：
		 * 处理结果为不通过（0），产生预警消息提示代理公司用户（提交用户）和对应的区域项目负责人，同时更新三方协议状态（备案退回）和备案退回原因，
		 * 允许用户进行附件修改后再提交； 处理结果为不通过（1），更新三方协议状态（已备案）和备案时间；
		 */
		/*
		 * xybh String 必填 协议编号 cljg String 必填 处理结果 0-不通过 1-通过 clyj String 必填
		 * 处理意见
		 */

		/*--	Int	返回值“1”表示数据接收成功
		返回值“0”表示数据接收失败*/
		int responseResult = 0;

		String xybh = model.getXybh();
		String cljg = model.getCljg();
		String clyj = model.getClyj();
		if (xybh.isEmpty()) {
			return responseResult;
		}
		if (cljg.isEmpty()) {
			return responseResult;
		}
		/*
		 * if(clyj.isEmpty()) { return responseResult; }
		 */

		// 查询协议信息
		Tgxy_TripleAgreementForm from = new Tgxy_TripleAgreementForm();
		from.setTheState(S_TheState.Normal);
		from.seteCodeOfTripleAgreement(xybh);

		List<Tgxy_TripleAgreement> list = new ArrayList<>();
		list = tgxy_TripleAgreementDao
				.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), from));
		if (list.isEmpty()) {
			return responseResult;
		}

		Tgxy_TripleAgreement tripleAgreement = list.get(0);

		if ("1".equals(cljg)) {
			// 审批通过处理
		
		//记录接口交互信息
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
		tgpf_SocketMsg.setUserStart(from.getUser());// 创建人
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
		tgpf_SocketMsg.setUserUpdate(from.getUser());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
		tgpf_SocketMsg.setMsgStatus(1);// 发送状态
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
		
		tgpf_SocketMsg.setRemark("..answerSfXy");
		tgpf_SocketMsg.setMsgDirection("DAIN");// 报文方向
		tgpf_SocketMsg.setMsgContent("xybh:"+xybh+";cljg:"+cljg+";clyj:"+clyj);// 报文内容
//		tgpf_SocketMsg.setReturnCode(String.valueOf(restFul));// 返回码
		
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

			/*
			 * 具体的业务逻辑操作: 三方协议状态置为已备案状态
			 */
			tripleAgreement.setTheStateOfTripleAgreement("3");
			// 设置备案时间
			// tripleAgreement.setRecordTimeStamp(System.currentTimeMillis());
			// 设置备案人
			// tripleAgreement.setUserRecord(user);

			// 审批流程状态-已完结
			tripleAgreement.setApprovalState(S_ApprovalState.Completed);

			tripleAgreement.setProcessingResults(cljg);
			tripleAgreement.setProcessingOpinions(clyj);

			tgxy_TripleAgreementDao.save(tripleAgreement);

			responseResult = 1;

		} else if ("0".equals(cljg)) {
			
			//记录接口交互信息
			Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
			tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
			tgpf_SocketMsg.setUserStart(from.getUser());// 创建人
			tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
			tgpf_SocketMsg.setUserUpdate(from.getUser());
			tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
			tgpf_SocketMsg.setMsgStatus(1);// 发送状态
			tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
			
			tgpf_SocketMsg.setRemark("..answerSfXy");
			tgpf_SocketMsg.setMsgDirection("DAIN");// 报文方向
			tgpf_SocketMsg.setMsgContent("xybh:"+xybh+";cljg:"+cljg+";clyj:"+clyj);// 报文内容
//			tgpf_SocketMsg.setReturnCode(String.valueOf(restFul));// 返回码
			
			tgpf_SocketMsgDao.save(tgpf_SocketMsg);

			// 协议相关状态信息
			tripleAgreement.setApprovalState(S_ApprovalState.WaitSubmit);
			tripleAgreement.setTheStateOfTripleAgreement("0");

			tripleAgreement.setProcessingResults(cljg);
			tripleAgreement.setProcessingOpinions(clyj);

			tgxy_TripleAgreementDao.save(tripleAgreement);

			responseResult = 1;

			/**
			 * 处理结果为不通过（0），
			 * 产生预警消息提示代理公司用户（提交用户）和对应的区域项目负责人，
			 * 同时更新三方协议状态（备案退回）和备案退回原因，允许用户进行附件修改后再提交；
			 */
			//构建预警消息主体
		    Sm_User userSend = sm_UserDao.findById(652L);
			Sm_CommonMessage sm_CommonMessage = new Sm_CommonMessage();
			sm_CommonMessage.setBusiCode("SFXY");
			sm_CommonMessage.setUserStart(userSend);
			sm_CommonMessage.setTheState(S_TheState.Normal);
			sm_CommonMessage.setBusiKind("1");
			sm_CommonMessage.setBusiState(0);
			sm_CommonMessage.setCreateTimeStamp(System.currentTimeMillis());
			sm_CommonMessage.seteCode("123");
			sm_CommonMessage.setLastUpdateTimeStamp(System.currentTimeMillis());
			sm_CommonMessage.setMessageType(S_CommonMessageType.UnreadWaring);
			sm_CommonMessage.setSendTimeStamp(MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "YYYY-MM-dd"));
			sm_CommonMessage.setTheTitle("三方协议归档处理失败");
			sm_CommonMessage.setTheContent("协议编号："+xybh+"归档处理失败，处理意见为："+clyj);
			
			sm_CommonMessageDao.save(sm_CommonMessage);
			
			//发送消息
			sendCommanyMesg(tripleAgreement, sm_CommonMessage);
			
		} else {
			// 不在返回参数之内
			return responseResult;
		}

		return responseResult;

	}
	
	
	/**
	 * 推送消息
	 * 
	 * @param userList
	 *            用户列表
	 * @param sm_CommonMessage
	 *            消息主题
	 * @param templateFina1
	 *            主题
	 * @param templateFina2
	 *            内容
	 */
	private void sendCommanyMesg(Tgxy_TripleAgreement tripleAgreement, Sm_CommonMessage sm_CommonMessage)
	{
		List<Long> userList = new ArrayList<>();
		userList = findMessageUser(tripleAgreement);
		// 发送消息
		for (Long userId : userList)
		{
			Sm_User user = sm_UserDao.findById(userId);

			// 更新主表信息
//			sm_CommonMessage.setTheTitle(templateFina1);
//			sm_CommonMessage.setTheContent(templateFina2);
//			sm_CommonMessageDao.save(sm_CommonMessage);

			// 保存消息子表
			Sm_CommonMessageDtl sm_CommonMessageDtl = new Sm_CommonMessageDtl();
			sm_CommonMessageDtl.seteCode(sm_BusinessCodeGetService.execute(sm_CommonMessage.getBusiCode()));
			sm_CommonMessageDtl.setUserStart(user);
			sm_CommonMessageDtl.setCreateTimeStamp(System.currentTimeMillis());

			sm_CommonMessageDtl.setBusiState("0");
			sm_CommonMessageDtl.setTheState(S_TheState.Normal);
			sm_CommonMessageDtl.setIsReader(S_IsReaderState.UnReadMesg);
			sm_CommonMessageDtl.setSendTimeStamp(MyDatetime.getInstance().dateToString(System.currentTimeMillis()));
			sm_CommonMessageDtl.setMessage(sm_CommonMessage);
			sm_CommonMessageDtl.setMessageType(sm_CommonMessage.getMessageType());
			sm_CommonMessageDtl.setReceiver(user);

			sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);

			sm_PushletService.execute(sm_CommonMessage.getTheTitle(), sm_CommonMessage.getTheContent(), userId);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Long> findMessageUser(Tgxy_TripleAgreement tripleAgreement)
	{
		Sm_CityRegionInfo cityRegion = tripleAgreement.getProject().getCityRegion();
		
		List<Long> userList = new ArrayList<Long>();
		//查询接收人(项目部区域负责人)
		Sm_Permission_RoleUserForm roleUserModle = new Sm_Permission_RoleUserForm();
		roleUserModle.setTheState(S_TheState.Normal);
		roleUserModle.setSm_Permission_RoleId(27L);//
		List<Sm_Permission_RoleUser> roleUserList = new ArrayList<Sm_Permission_RoleUser>();
		roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserModle));
		if(null != roleUserList && roleUserList.size()>0)
		{
			Sm_Permission_RangeForm rangModel = null;
			List<Sm_Permission_Range> rangList = null;
			for (Sm_Permission_RoleUser sm_Permission_RoleUser : roleUserList)
			{
				rangModel = new Sm_Permission_RangeForm();
				rangModel.setTheState(S_TheState.Normal);
				rangModel.setTheType(1);
				rangModel.setUser(sm_Permission_RoleUser.getSm_User());
				
				rangList = new ArrayList<>();
				//过滤该协议所属区域的负责人
				rangList = sm_Permission_RangeDao.findByPage(sm_Permission_RangeDao.getQuery(sm_Permission_RangeDao.getBasicHQL(), rangModel));
				for (Sm_Permission_Range sm_Permission_Range : rangList) {
					if(sm_Permission_Range.getCityRegionInfo().getTableId().equals(cityRegion.getTableId()))
					{
						userList.add(sm_Permission_RoleUser.getSm_User().getTableId());
					}
				}
				
			}
		}
		else
		{
			userList = new ArrayList<>();
		}
		
		//查询提交人
		Sm_ApprovalProcess_AFForm afModel = new Sm_ApprovalProcess_AFForm();
        afModel.setTheState(S_TheState.Normal);
        afModel.setBusiCode("06110301");
        afModel.setSourceId(tripleAgreement.getTableId());
        afModel.setOrderBy("createTimeStamp");
        List<Sm_ApprovalProcess_AF> afList = new ArrayList<>();
        afList = sm_ApprovalProcess_AFDao.findByPage(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), afModel));
        if(!afList.isEmpty())
        {
        	userList.add(afList.get(0).getUserStart().getTableId());
        }
		
		return userList;
	}
	
}
