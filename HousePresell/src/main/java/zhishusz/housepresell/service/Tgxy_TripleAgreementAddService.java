package zhishusz.housepresell.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_TripleAgreementAddService
{
	private static final String BUSI_CODE = "06110301";
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;// 同步预售系统合同信息
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 合作协议

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;// 买受人
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;// 三方协议版本控制

	private MyDatetime myTime = MyDatetime.getInstance();
	// 手工增加三方协议
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-30 13:20:49
		 * 新增三方协议
		 * 
		 * 1.三方协议基本信息
		 * 2.三方协议附件信息
		 * 
		 * 接收前端字段：
		 * 协议日期（tripleAgreementTimeStamp）：默认当天、合同备案号（eCodeOfContractRecord）、出卖人（
		 * sellerName）开发企业、买受人（缺少）、创建人Id（userStartId）
		 * 托管机构（escrowCompany）常州正泰房产居间服务有限公司、项目Id（projectId）、楼幢ID（buildingInfoId
		 * ）、户室Id（houseId）
		 * 
		 * 后台默认字段：
		 * 1.三方协议状态（theStateOfTripleAgreement）：theStateOfTA
		 * 自动回写
		 * ==》0-未打印（新增）
		 * ==》1-已打印未上传（打印三方协议）
		 * ==》2-已上传（代理公司上传三方协议和商品房买卖合同签字页）
		 * ==》3-已备案（项目部备案）
		 * ==》4-备案退回(项目部门备案退回）
		 * 
		 * 2.三方协议归档状态（theStateOfTripleAgreementFiling）：theStateOfTAF
		 * 自动回写
		 * ==》0-为空（默认）
		 * ==》1-待归档（三方协议状态为已备案）
		 * ==》2-已归档（档案人员归档成功）
		 * 
		 * 3.三方协议效力状态（theStateOfTripleAgreementEffect）：theStateOfTAE
		 * 自动回写
		 * ==》0-为空（默认）
		 * ==》1-生效（代理公司上传三方协议和商品房买卖合同签字页后）
		 * ==》2-退房退款待处理（退房退款流程发起时标记三方协议状态为待处理，）
		 * ==》3-失效：
		 * ===》a、退房退款流程走完之后自动反写为失效；
		 * ===》b、同一户有新的三方协议备案成功（自动失效）
		 * ===》c、长时间没有入账，暂定半年协议状态为失效，手工发起失效流程
		 * 
		 * 4.打印方式（printMethod）：
		 * 自动反写
		 * ==》0-扫码打印
		 * ==》1-手工打印
		 * 
		 * 
		 * 其他注意字段：
		 * 1.编号（eCode）:新增时自动生成，规则待定
		 * 业务编号+N+YY+MM+DD+日自增长流水号（5位）
		 * 贷款三方托管协议签署
		 * 06110301
		 * 2.三方协议号(eCodeOfTripleAgreement)：自动编号，不可修改，编号规则：
		 * 合作协议号+四位流水号（按天流水）
		 * 
		 * 3.楼幢编号（eCodeOfBuilding）-->根据楼幢信息带出
		 * 4.施工编号（eCodeFromConstruction）-->根据楼幢信息带出
		 * 
		 * 5.项目名称（theNameOfProject）-->由项目信息带出
		 * 6.单元（ eCodeOfUnit） --由户室信息带出
		 * 
		 * thestate 默认正常状态
		 * 其他校验：
		 * 1.户室信息是否存在
		 * 
		 */
		String payDate = model.getPayDate();
		
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String tripleAgreementTimeStamp = formatDate.format(System.currentTimeMillis());// 协议日期
		String eCodeOfContractRecord = model.geteCodeOfContractRecord(); // 合同备案号
		String sellerName = model.getSellerName(); // 出卖人（开发企业名称）
		String buyerName = model.getBuyerName();// 买受人名称
		// Long userStartId = model.getUserStartId();// 创建人id
		Long userStartId = model.getUserId();
		String escrowCompany = model.getEscrowCompany();// 托管机构

		Long projectId = model.getProjectId();// 项目Id
		Long buildingInfoId = model.getBuildingInfoId(); // 楼幢Id
		Long houseId = model.getHouseId();// 户室Id
		
		String printMethod = model.getPrintMethod();//打印方式
		
		/*
		 * xsz by time 2019-1-3 14:03:01
		 * 判断是否存在对应的协议版本
		 *  
		 */
		// 查询楼栋信息
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(buildingInfoId);
		if (buildingInfo == null)
		{
			return MyBackInfo.fail(properties, "查询楼幢信息为空");
		}
		//查询楼栋是否存在
		if (null == buildingInfo.getTheState() || "1".equals(buildingInfo.getTheState()+"")) {
			return MyBackInfo.fail(properties, "该楼栋已经删除，请联系管理员");
		}

		Tgxy_TripleAgreementVerMngForm tripleAgreementVerMngForm = new Tgxy_TripleAgreementVerMngForm();
		//tripleAgreementVerMngForm.setEnableTimeStamp(myTime.stringToLong(tripleAgreementTimeStamp, "yyyy-MM-dd"));
		tripleAgreementVerMngForm.setTheState(S_TheState.Normal);
		tripleAgreementVerMngForm.setBusiState(S_BusiState.HaveRecord);
		Long stringToLong = myTime.stringToLong(tripleAgreementTimeStamp, "yyyy-MM-dd");
		//Integer count = tgxy_TripleAgreementVerMngDao.findByPage_Size(tgxy_TripleAgreementVerMngDao.getQuery_Size(tgxy_TripleAgreementVerMngDao.getBasicHQLByTiem(), tripleAgreementVerMngForm));

		List<Tgxy_TripleAgreementVerMng> tripleAgreementVerMngList = tgxy_TripleAgreementVerMngDao.findByPage(tgxy_TripleAgreementVerMngDao.getQuery(tgxy_TripleAgreementVerMngDao.getTheVersion(), tripleAgreementVerMngForm));
		
		boolean isExist = false;
		if(null == tripleAgreementVerMngList || tripleAgreementVerMngList.size() == 0)
		{
			return MyBackInfo.fail(properties, "未找到有效的三方协议版本信息！");
		}
		else
		{
			//获取最新的一条记录，判断签约时间是否在最新的版本中
			Tgxy_TripleAgreementVerMng verMng = tripleAgreementVerMngList.get(0);
			
			if(verMng.getEnableTimeStamp()>stringToLong)
			{
				
				for(int i = 1;i<tripleAgreementVerMngList.size();i++)
				{
					
					if(tripleAgreementVerMngList.get(i).getEnableTimeStamp()<=stringToLong && tripleAgreementVerMngList.get(i).getDownTimeStamp()>=stringToLong)
					{
						isExist = true;
						break;
					}
					
				}
				
			}
			else
			{
				isExist = true;
			}
			
		}
		
		if(!isExist)
		{
			return MyBackInfo.fail(properties, "未找到有效的三方协议版本信息！");
		}

		// 校验合同编号的唯一性
		if (null != eCodeOfContractRecord && !eCodeOfContractRecord.trim().isEmpty())
		{

			String sql = "select * from Tgxy_TripleAgreement where theState = 0 and theStateOfTAE in ('0','1','2') and eCodeOfContractRecord ='"
					+ eCodeOfContractRecord + "'";

			List<Tgxy_TripleAgreement> list = sessionFactory.getCurrentSession()
					.createNativeQuery(sql, Tgxy_TripleAgreement.class).getResultList();

			if (null != list && list.size() > 0)
			{
				return MyBackInfo.fail(properties, "该合同已签署有效的三方协议信息");
			}

		}
		else
		{
			return MyBackInfo.fail(properties, "请输入合同备案号");
		}

		// 基础非空校验
		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "创建人不能为空");
		}
		if (tripleAgreementTimeStamp == null || tripleAgreementTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "协议日期不能为空");
		}
		if (sellerName == null || sellerName.length() == 0)
		{
			return MyBackInfo.fail(properties, "出卖人不能为空");
		}
		if (escrowCompany == null || escrowCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管机构不能为空");
		}
		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目不能为空");
		}
		if (buildingInfoId == null || buildingInfoId < 1)
		{
			return MyBackInfo.fail(properties, "楼幢不能为空");
		}
		if (houseId == null || houseId < 1)
		{
			return MyBackInfo.fail(properties, "户室不能为空");
		}

		Tgxy_TripleAgreement tgxy_TripleAgreement = new Tgxy_TripleAgreement();
		tgxy_TripleAgreement.setTheState(S_TheState.Normal);

		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "查询创建人信息为空");
		}

		// 创建人、操作人、创建时间、最后修改时间
		tgxy_TripleAgreement.setUserStart(userStart);
		tgxy_TripleAgreement.setUserUpdate(userStart);
		tgxy_TripleAgreement.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_TripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		// 项目信息
		Empj_ProjectInfo project = empj_ProjectInfoDao.findById(projectId);
		if (null == project)
		{
			return MyBackInfo.fail(properties, "查询项目信息为空");
		}
		if (null == project.getTheName() || project.getTheName().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "项目名称为空");
		}
		tgxy_TripleAgreement.setProject(project);
		tgxy_TripleAgreement.setTheNameOfProject(project.getTheName());

		// if (null == buildingInfo.geteCodeOfLand() ||
		// buildingInfo.geteCodeOfLand().trim().isEmpty())
		// {
		// return MyBackInfo.fail(properties, "楼幢号（国土）为空");
		// }
		if (null == buildingInfo.geteCodeFromConstruction() || buildingInfo.geteCodeFromConstruction().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "施工编号为空");
		}
		tgxy_TripleAgreement.setBuildingInfo(buildingInfo);
		tgxy_TripleAgreement.seteCodeOfBuilding(buildingInfo.geteCodeOfLand());
		tgxy_TripleAgreement.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());

		/*
		 * 1.编号（eCode）:新增时自动生成，规则待定
		 * 2.三方协议号(eCodeOfTripleAgreement)：自动编号，不可修改，编号规则：
		 * 合作协议号+四位流水号（按天流水）
		 * 
		 * 合作协议号由楼幢施工编号查询合作协议带出
		 */

		String[] idd = UUID.randomUUID().toString().split("-");

		// 合同eCode
		String eCode = idd[0] + idd[1];
		tgxy_TripleAgreement.setEcodeOfContract(eCode);

		// 审批流程状态
		tgxy_TripleAgreement.setApprovalState(S_ApprovalState.WaitSubmit);

		/*
		 * xsz by time 2018-11-11 15:14:50
		 * 使用编号生成规则
		 * 
		 * =================start==================
		 */

		// 由施工编号查询合作协议信息
		Tgxy_EscrowAgreementForm from = new Tgxy_EscrowAgreementForm();
		from.setBuildingInfoCodeList("%" + buildingInfo.geteCodeFromConstruction() + "%");

		// 查询合作协议信息
		// String sql = "select * from Tgxy_EscrowAgreement where theState = 0
		// and businessProcessState ='7' and buildingInfoCodeList like '"
		// + from.getBuildingInfoCodeList() + "' ";

		/*
		 * xsz by time 2018-11-14 11:47:03
		 * 通过楼幢编号进行查询可能导致返回多条信息
		 * 所以现在更改为根据关联字段查询
		 */
		String sql = "select * from Tgxy_EscrowAgreement where theState = 0 and businessProcessState ='7' and tableId=(select A.TGXY_ESCROWAGREEMENT from Rel_EscrowAgreement_Building A where A.EMPJ_BUILDINGINFO="
				+ buildingInfoId + ")";

		Tgxy_EscrowAgreement tgxy_EscrowAgreement = null;
		try
		{
			tgxy_EscrowAgreement = sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_EscrowAgreement.class)
					.uniqueResult();
		}
		catch (HibernateException e)
		{
			return MyBackInfo.fail(properties, "查询楼幢签署的合作协议异常，请确认后重试");
		}

		if (null == tgxy_EscrowAgreement)
		{
			return MyBackInfo.fail(properties,
					"楼幢施工编号:" + buildingInfo.geteCodeFromConstruction() + "未查询到已审核的合作协议相关信息");
		}
		else
		{

			/*
			 * xsz by time 2018-11-13 10:11:22
			 * 编号生成后判断是否存在相同的编号，否则继续生成
			 */
			String tripleAgreementEcode = "";
			Tgxy_TripleAgreementForm codeModel;
			Integer totalCount = 1;
			while (totalCount > 0)
			{
				tripleAgreementEcode = sm_BusinessCodeGetService
						.getTripleAgreementEcode(tgxy_EscrowAgreement.geteCodeOfAgreement());

				codeModel = new Tgxy_TripleAgreementForm();
				codeModel.seteCodeOfTripleAgreement(tripleAgreementEcode);

				totalCount = tgxy_TripleAgreementDao.findByPage_Size(
						tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), codeModel));

			}

			tgxy_TripleAgreement.seteCode(tripleAgreementEcode);

		}

		tgxy_TripleAgreement.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCode());
		
		tgxy_TripleAgreement.setBusiState("1");

		/*
		 * xsz by time 2018-11-11 15:14:50
		 * 使用编号生成规则
		 * 
		 * =================end==================
		 */

		// 户室信息
		Empj_HouseInfo house = empj_HouseInfoDao.findById(houseId);
		if (null == house)
		{
			return MyBackInfo.fail(properties, "户室ID:" + houseId + "信息为空");
		}
		if (null == house.getRoomId() || house.getRoomId().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "户室ID:" + houseId + "室号为空");
		}

		/*
		 * 校验户室信息是否在协议中已存在
		 * 
		 */
		// Integer totalCount = tgxy_TripleAgreementDao
		// .findByPage_Size(tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(),
		// model));

		tgxy_TripleAgreement.setHouse(house);
		tgxy_TripleAgreement.setUnitRoom(house.getRoomId());

		// 单元
		tgxy_TripleAgreement.setUnitInfo(house.getUnitInfo());

		// 其他信息
		tgxy_TripleAgreement.seteCodeOfContractRecord(eCodeOfContractRecord);// 合同备案号
		tgxy_TripleAgreement.setTripleAgreementTimeStamp(tripleAgreementTimeStamp);// 协议日期
		tgxy_TripleAgreement.setSellerName(sellerName);// 出卖人
		tgxy_TripleAgreement.setBuyerName(buyerName);// 买受人
		tgxy_TripleAgreement.setEscrowCompany(escrowCompany);// 托管机构

		// 协议相关状态信息
		tgxy_TripleAgreement.setTheStateOfTripleAgreement("0");// 三方协议状态
		tgxy_TripleAgreement.setTheStateOfTripleAgreementFiling("0");// 三方协议归档状态
		tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect("0");// 三方协议效力状态
		
		if(null == printMethod || printMethod.trim().isEmpty())
		{
			tgxy_TripleAgreement.setPrintMethod("0");// 打印方式
		}
		else
		{
			tgxy_TripleAgreement.setPrintMethod(printMethod);// 打印方式
		}
		

		tgxy_TripleAgreement.setTotalAmountOfHouse(0.00);// 户托管入账总金额

		// 三方协议存储合同信息
		/*
		 * 合同相关信息
		 */
		Double contractSumPrice = model.getContractSumPrice();// 合同总价
		Double buildingArea = model.getBuildingArea();// 建筑面积
		String position = model.getPosition();// 房屋坐落
		String contractSignDate = model.getContractSignDate();// 合同签订日期
		String paymentMethod = model.getPaymentMethod();// 付款方式
		String contractRecordDate = model.getContractRecordDate();// 合同备案日期
		
		tgxy_TripleAgreement.setRoomlocation(position);

		if (null == contractSumPrice || contractSumPrice < 0)
		{
			tgxy_TripleAgreement.setContractAmount(0.00);
		}
		else
		{
			tgxy_TripleAgreement.setContractAmount(contractSumPrice);
		}

		tgxy_TripleAgreement.setBuildingArea(buildingArea);

		// 首付款
		Double firstPayment = null == model.getFirstPayment() ? 0.00 : model.getFirstPayment();
		// 贷款金额=合同总价-首付款
		/*
		 * double类型计算
		 * 
		 * doubleAddDouble 加
		 * doubleSubtractDouble 减
		 * doubleMultiplyDouble 乘
		 * div 除
		 * getShort() 保留小数位
		 */
		MyDouble dplan = MyDouble.getInstance();
		Double loanAmount = dplan.doubleSubtractDouble(contractSumPrice, firstPayment);

		// 保存首付款和贷款金额
		tgxy_TripleAgreement.setLoanAmount(loanAmount < 0 ? 0.00 : loanAmount);
		tgxy_TripleAgreement.setFirstPayment(firstPayment);

		Serializable tableId = tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);

		// xsz by time 2018-11-16 11:41:34 三方协议保存后将信息存入户室关联
		house.setTripleAgreement(tgxy_TripleAgreement);
		empj_HouseInfoDao.save(house);

		/*
		 * xsz by time 2018-9-18 15:19:02
		 * 后台附件信息整合
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setUserStart(userStart);
				sm_Attachment.setUserUpdate(userStart);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				// sm_Attachment.setBusiType("Tgxy_TripleAgreement");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		/*
		 * xsz by time 2018-9-2 21:45:08
		 * 保存预售系统的合同信息
		 * 
		 * 校验：
		 * 1.根据合同备案号查询是否存在相同合同信息
		 * 
		 */
		if (null != eCodeOfContractRecord && !eCodeOfContractRecord.trim().isEmpty())
		{
			Tgxy_ContractInfoForm tgxy_ContractInfoModel = new Tgxy_ContractInfoForm();
			tgxy_ContractInfoModel.seteCodeOfContractRecord(eCodeOfContractRecord);

			Object query = tgxy_ContractInfoDao.findOneByQuery(
					tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), tgxy_ContractInfoModel));

			if (null == query)
			{

				// 保存合同信息
				Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();

				// 基本信息
				tgxy_ContractInfo.setUserStart(userStart);
				tgxy_ContractInfo.setUserUpdate(userStart);
				tgxy_ContractInfo.setCreateTimeStamp(System.currentTimeMillis());
				tgxy_ContractInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgxy_ContractInfo.setTheState(S_TheState.Normal);
				tgxy_ContractInfo.setSyncPerson(userStart.getTheName());// 同步人
				tgxy_ContractInfo.setSyncDate(System.currentTimeMillis());// 同步时间

				tgxy_ContractInfo.setBuildingInfo(buildingInfo);//楼幢信息
				tgxy_ContractInfo.seteCodeOfContractRecord(eCodeOfContractRecord);// 合同备案号
				tgxy_ContractInfo.setTheNameFormCompany(sellerName);// 开发企业名称
				tgxy_ContractInfo.setTheNameOfProject(project.getTheName());// 项目名称
				tgxy_ContractInfo.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());// 施工编号
				tgxy_ContractInfo.setHouseInfo(house);// 户室信息
				tgxy_ContractInfo.setRoomIdOfHouseInfo(house.getRoomId());// 室号
				tgxy_ContractInfo.setContractSumPrice(contractSumPrice);// 合同总价
				tgxy_ContractInfo.setBuildingArea(buildingArea);// 建筑面积
				tgxy_ContractInfo.setPosition(position);// 房屋坐落
				tgxy_ContractInfo.setContractSignDate(contractSignDate);// 合同签订日期
				tgxy_ContractInfo.setPaymentMethod(paymentMethod);// 付款方式
				tgxy_ContractInfo.seteCodeOfBuilding(buildingInfo.geteCodeFromPresellSystem());// 备案系统楼幢编号
				tgxy_ContractInfo.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());// 公安编号
				tgxy_ContractInfo.setContractRecordDate(contractRecordDate);// 合同备案日期
				// 贷款金额
				tgxy_ContractInfo.setLoanAmount(loanAmount < 0 ? 0.00 : loanAmount);
				// 首付款
				tgxy_ContractInfo.setFirstPaymentAmount(firstPayment < 0 ? 0.00 : firstPayment);
				
				tgxy_ContractInfo.setPayDate(payDate);

				tgxy_ContractInfoDao.save(tgxy_ContractInfo);

				house.setTripleAgreement(tgxy_TripleAgreement);
				house.setContractInfo(tgxy_ContractInfo);
				empj_HouseInfoDao.save(house);
			}
			else
			{
				Tgxy_ContractInfo tgxy_ContractInfo = (Tgxy_ContractInfo) query;

				tgxy_ContractInfo.setPayDate(payDate);
				tgxy_ContractInfo.setTheNameFormCompany(sellerName);// 开发企业名称
				tgxy_ContractInfo.setTheNameOfProject(project.getTheName());// 项目名称
				tgxy_ContractInfo.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());// 施工编号
				tgxy_ContractInfo.setHouseInfo(house);// 户室信息
				tgxy_ContractInfo.setRoomIdOfHouseInfo(house.getRoomId());// 室号
				tgxy_ContractInfo.setContractSumPrice(contractSumPrice);// 合同总价
				tgxy_ContractInfo.setBuildingArea(buildingArea);// 建筑面积
				tgxy_ContractInfo.setPosition(position);// 房屋坐落
				tgxy_ContractInfo.setContractSignDate(contractSignDate);// 合同签订日期
				tgxy_ContractInfo.setPaymentMethod(paymentMethod);// 付款方式
				tgxy_ContractInfo.seteCodeOfBuilding(buildingInfo.geteCodeFromPresellSystem());// 备案系统楼幢编号
				tgxy_ContractInfo.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());// 公安编号
				tgxy_ContractInfo.setContractRecordDate(contractRecordDate);// 合同备案日期
				// 贷款金额
				tgxy_ContractInfo.setLoanAmount(loanAmount < 0 ? 0.00 : loanAmount);
				// 首付款
				tgxy_ContractInfo.setFirstPaymentAmount(firstPayment < 0 ? 0.00 : firstPayment);

				tgxy_ContractInfoDao.save(tgxy_ContractInfo);

				house.setTripleAgreement(tgxy_TripleAgreement);
				house.setContractInfo(tgxy_ContractInfo);
				empj_HouseInfoDao.save(house);
			}
		}
		/*
		 * xsz by time 2018-9-2 21:46:20
		 * 保存预售系统买受人信息
		 * 
		 * 先根据合同备案号查询出买受人信息
		 * 将查询出的买受人信息删除
		 * 再保存
		 */
		/*
		 * 根据合同备案号查询买受人信息
		 * 
		 */
		Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
		buyForm.seteCodeOfContract(eCodeOfContractRecord);
		List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = new ArrayList<Tgxy_BuyerInfo>();
		tgxy_BuyerInfoList = tgxy_BuyerInfoDao
				.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), buyForm));

		if (null != tgxy_BuyerInfoList && tgxy_BuyerInfoList.size() > 0)
		{
			for (Tgxy_BuyerInfo tgxy_BuyerInfo : tgxy_BuyerInfoList)
			{
				tgxy_BuyerInfoDao.delete(tgxy_BuyerInfo);
			}
		}

		String buyerlist = model.getBuyerlist();

		if (null != buyerlist && !buyerlist.trim().isEmpty())
		{

			List<Tgxy_BuyerInfo> list = JSON.parseArray(buyerlist, Tgxy_BuyerInfo.class);
			for (Tgxy_BuyerInfo tgxy_BuyerInfo : list)
			{
				tgxy_BuyerInfo.setTheState(S_TheState.Normal);
				tgxy_BuyerInfo.setUserStart(userStart);
				tgxy_BuyerInfo.setUserUpdate(userStart);
				tgxy_BuyerInfo.setCreateTimeStamp(System.currentTimeMillis());
				tgxy_BuyerInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
				tgxy_BuyerInfo.seteCodeOfContract(eCodeOfContractRecord);// 合同备案号
				tgxy_BuyerInfo.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());// 三方协议号

				tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);

			}
		}

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
