
package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;

/*
 * Service添加操作：版本管理-受限节点设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpj_BldLimitAmountVer_AFAddService
{
	private static final String BUSI_CODE = "06010102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	private static final String ADD_BUSI_CODE = "06010102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_UserDao sm_UserDao;

	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	
	//附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao afDtlDao;
	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	public Properties execute(Tgpj_BldLimitAmountVer_AFForm model)
	{
		Properties properties = new MyProperties();
		properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
		if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
			if(properties.getProperty(S_NormalFlag.info).equals("noApproval")){

			}else{
				return properties;
			}
		}

		Integer theState = S_TheState.Normal;
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = System.currentTimeMillis();
		String theName = model.getTheName();
		String theVerion = model.getTheVerion();
		String theType = model.getTheType();
//		String limitedAmountInfoJSON = model.getLimitedAmountInfoJSON();
		String beginExpirationDateString = model.getBeginExpirationDateString();
		Long beginExpirationDate = MyDatetime.getInstance().stringToLong(beginExpirationDateString);
		model.setBeginExpirationDate(beginExpirationDate);
//		String endExpirationDateString = model.getEndExpirationDateString();
//		Long endExpirationDate = MyDatetime.getInstance().stringToLong(endExpirationDateString);
//		model.setEndExpirationDate(endExpirationDate);
		Integer isUsing=model.getIsUsing();
		Tgpj_BldLimitAmountVer_AFDtl[] nodeVersionList = model.getNodeVersionList();

		boolean uniqueWaitSubmitLimitAmount = tgpj_BldLimitAmountVer_AFDao.isUniqueWaitSubmitLimitAmount(model);
		if(!uniqueWaitSubmitLimitAmount){
			return MyBackInfo.fail(properties, "已有待提交的受限额度节点，无法继续添加");
		}

		//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "状态 S_TheState 初始为Normal不能为空");
//		}
		if(busiState == null || busiState.length()< 1)
		{
			busiState = "0";
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "节点版本号不能为空");
//			eCode = "SXEDJD" + new Random().nextInt(100000);
		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "创建人不能为空");
//		}

//		if(userUpdateId == null || userUpdateId < 1)
//		{
//			return MyBackInfo.fail(properties, "最后修改人不能为空");
//		}
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "版本名称不能为空");
		}
//		if(theVerion == null || theVerion.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "版本号不能为空");
//		}
		if(theType == null || theType.length() == 0)
		{
			return MyBackInfo.fail(properties, "交付类型不能为空");
		}
		if(isUsing == null)
		{
			return MyBackInfo.fail(properties, "请选择是否启用");
		}
//		if(limitedAmountInfoJSON == null || limitedAmountInfoJSON.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "受限额度数据不能为空");
//		}
		if(beginExpirationDateString == null || beginExpirationDateString.length() ==0)
		{
			return MyBackInfo.fail(properties, "启用日期不能为空");
		}
		if(nodeVersionList==null){
			return MyBackInfo.fail(properties, "受限额度版本节点不能为空");
		}
//		if(endExpirationDateString == null || endExpirationDateString.length()==0)
//		{
//			return MyBackInfo.fail(properties, "停用日期不能为空");
//		}
//		if(beginExpirationDate>endExpirationDate){
//			return MyBackInfo.fail(properties, "日期选择错误");
//		}
//		if(beginExpirationDate==endExpirationDate){
//			return MyBackInfo.fail(properties, "启用日期和停用日期不能是同一天");
//		}
		if(tgpj_BldLimitAmountVer_AFDao.isInRange(model)){
			return MyBackInfo.fail(properties, "启用日期在上一个版本的时间段内，无法添加");
		}
		Tgpj_BldLimitAmountVer_AF tgpj_BldLimitAmountVer_AF = new Tgpj_BldLimitAmountVer_AF();
		if(userStartId!=null){
			Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
			if(userStart == null)
			{
				return MyBackInfo.fail(properties, "创建人不能为空");
			}else{
				tgpj_BldLimitAmountVer_AF.setUserStart(userStart);
			}
		}
		boolean repeatEcode = tgpj_BldLimitAmountVer_AFDao.isRepeatEcode(model);
		if(repeatEcode){
			return MyBackInfo.fail(properties, "版本号重复无法添加");
		}
		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if (!msgInfo.isSuccess()) {
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}


		tgpj_BldLimitAmountVer_AF.setTheState(theState);
		tgpj_BldLimitAmountVer_AF.setBusiState(S_BusiState.NoRecord);
//		tgpj_BldLimitAmountVer_AF.setBusiState(busiState);
//		tgpj_BldLimitAmountVer_AF.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgpj_BldLimitAmountVer_AF.seteCode(eCode);

		tgpj_BldLimitAmountVer_AF.setCreateTimeStamp(createTimeStamp);
		tgpj_BldLimitAmountVer_AF.setUserStart(model.getUser());
		tgpj_BldLimitAmountVer_AF.setUserUpdate(model.getUser());
		tgpj_BldLimitAmountVer_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpj_BldLimitAmountVer_AF.setTheName(theName);
		tgpj_BldLimitAmountVer_AF.setTheVerion(theVerion);
		tgpj_BldLimitAmountVer_AF.setTheType(theType);
//		tgpj_BldLimitAmountVer_AF.setLimitedAmountInfoJSON(limitedAmountInfoJSON);
		tgpj_BldLimitAmountVer_AF.setBeginExpirationDate(beginExpirationDate);
//		tgpj_BldLimitAmountVer_AF.setEndExpirationDate(endExpirationDate);
		tgpj_BldLimitAmountVer_AF.setIsUsing(isUsing);

//		//设置之前的那个结束时间
//		Tgpj_BldLimitAmountVer_AFForm afFormOld = new Tgpj_BldLimitAmountVer_AFForm();
//		afFormOld.setTheState(S_TheState.Normal);
//		afFormOld.setTheType(theType);
//		afFormOld.setOrderBy("beginExpirationDate desc");
//		List<Tgpj_BldLimitAmountVer_AF> tgpj_BldLimitAmountVerList = tgpj_BldLimitAmountVer_AFDao.findByPage(
//				tgpj_BldLimitAmountVer_AFDao.getQuery(tgpj_BldLimitAmountVer_AFDao.getBasicHQL(), afFormOld));
//		if(tgpj_BldLimitAmountVerList.size()>0){
//			Tgpj_BldLimitAmountVer_AF oldVersion = tgpj_BldLimitAmountVerList.get(0);
//			if (oldVersion != null)
//			{
//				MyDatetime datetime = MyDatetime.getInstance();
//				oldVersion.setEndExpirationDate(datetime.theDayBeforeSpecifiedDayTimeStamp(beginExpirationDate));
//				tgpj_BldLimitAmountVer_AFDao.save(oldVersion);
//			}
//		}


		tgpj_BldLimitAmountVer_AFDao.save(tgpj_BldLimitAmountVer_AF);
		//保存列表节点信息
		for (Tgpj_BldLimitAmountVer_AFDtl afDtl : nodeVersionList) {
			afDtl.setTheState(S_TheState.Normal);
			afDtl.setUserStart(model.getUserStart());
			afDtl.setUserUpdate(model.getUserUpdate());
			afDtl.setCreateTimeStamp(System.currentTimeMillis());
			afDtl.setLastUpdateTimeStamp(System.currentTimeMillis());
			afDtl.setBldLimitAmountVerMng(tgpj_BldLimitAmountVer_AF);
			afDtlDao.save(afDtl);
		}

		sm_AttachmentBatchAddService.execute(model, tgpj_BldLimitAmountVer_AF.getTableId());

		//没有配置审批流程无需走审批流直接保存数据库
		if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info))) {

			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");

			//审批操作
			sm_approvalProcessService.execute(tgpj_BldLimitAmountVer_AF, model, sm_approvalProcess_cfg);
		}else{
			tgpj_BldLimitAmountVer_AF.setApprovalState(S_ApprovalState.Completed);
			tgpj_BldLimitAmountVer_AF.setBusiState(S_BusiState.HaveRecord);
			tgpj_BldLimitAmountVer_AF.setUserRecord(model.getUser());
			tgpj_BldLimitAmountVer_AF.setRecordTimeStamp(System.currentTimeMillis());
			tgpj_BldLimitAmountVer_AFDao.save(tgpj_BldLimitAmountVer_AF);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", tgpj_BldLimitAmountVer_AF.getTableId());

		return properties;
	}
}
