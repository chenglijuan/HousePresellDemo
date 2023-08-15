package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.json.JSONUtil;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastDtlForm;
import zhishusz.housepresell.controller.form.Empj_PjDevProgressForcastForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDao;
import zhishusz.housepresell.database.dao.Empj_PjDevProgressForcastDtlDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.Empj_PjDevProgressForcastDtl;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExcelUtil2;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

/*
 * Service添加操作：excel导入工程进度预测
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_PjDevProgressForcastImportAddService
{
	private static final String BUSI_CODE = "03030201";//具体业务编码参看SVN文
	
	private final String XLSFILEPATH = "abc.xls";
	private final String XLSXFILEPATH = "abc.xlsx";
	private final String CSVFILEPATH = "abc.csv";
	
	@Autowired
	private Empj_PjDevProgressForcastDao empj_PjDevProgressForcastDao;
	@Autowired
	private Empj_PjDevProgressForcastDtlDao empj_pjDevProgressForcastDtlDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Empj_PjDevProgressForcastDtlAddService empj_pjDevProgressForcastDtlAddService;
	@Autowired
	private Empj_BldAccountGetLimitAmountVerService empj_BldAccountGetLimitAmountVerService;//楼幢进度节点
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_PjDevProgressForcastForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * 前提：
		 * 1.同一个楼幢只会存在一条记录，多个进度节点依次添加
		 * 2.存在相同的进度节点预测时，更新“预测完成日期（原）（新）”
		 * TODO
		 * 3.‘当前建设进度’对应系统内‘当前进度节点’字段、‘巡查人’抓取 导入的操作用户、‘进度更新时间’对应系统内‘巡查日期’
		 * 4.预测变更节点 匹配 进度详情中的‘预测进度节点’，然后预测变更时间 对应系统内‘预测完成时期（原）’，下次导入的时候，对应‘预测完成时期（原） 一个订单可能会多次导入，每次会重更新‘预测完成日期（新）’，上一次的则会变成“预测完成日期（原）”
		 * 
		 * 通过解析上传的Excel文件，实现自动批量新增工程进度预测数据
		 * 1.根据解析的“项目名称”和“楼幢编号”
		 * ==》查询对应的开发企业
		 * ==》查询该楼幢下的可编辑的进度节点
		 * 
		 * 2.根据解析的“预测变更节点”
		 * ==》通过名称，与查询出的进度节点匹配
		 */
		
		//TODO ： 解析上传的Excel文件
		Sm_User user = model.getUser();
		String json = model.getSm_attachment();
		if (null == json)
		{
			return MyBackInfo.fail(properties, "上传的附件不能为空");
		}
		Sm_Attachment sm_attachment = JSONUtil.toBean(json, Sm_Attachment.class);
		
		// 获取文件保存路径
		String path = this.getClass().getClassLoader().getResource("").getPath();
		
		if (path.contains("%20"))
		{
			path = path.replace("%20", " ");
		}
		
		// 获取附件地址
		String httpPath = sm_attachment.getTheLink();
		
		if (null == httpPath || httpPath.length() == 0)
		{
			return MyBackInfo.fail(properties, "Sm_Attachment(TheLink：'" + httpPath + "')不能为空");
		}
	
		String upath = UUID.randomUUID().toString();
		
		// 文件保存绝对地址
		String saveFilePath = "";

		if (httpPath.endsWith(".xls"))
		{
			saveFilePath = path + "\\" + upath + ".xls";
		}
		else if (httpPath.endsWith(".xlsx"))
		{
			saveFilePath = path + "\\" + upath + ".xlsx";
		}
		else if (httpPath.endsWith(".csv"))
		{
			saveFilePath = path + "\\" + upath + ".csv";
		}
		
		//下载上传的文件到指定位置
		OssServerUtil ossUtil = new OssServerUtil();
		ossUtil.method(httpPath, saveFilePath);
		
		//解析上传的Excel，返回解析后的数据
		List<List<String>> readList = ExcelUtil2.read(saveFilePath);
		if(null == readList || readList.size() < 1){
			return MyBackInfo.fail(properties, "解析excel文件失败");
		}
		
		//TODO 针对解析的数据，进行数据的处理（从第三行开始取有效数据）
		int m = 2;//定义初始有值的变量
		String sheetIndex;//序号
		String sheetProject;//项目名称
		String sheetBuliding;//托管楼幢施工编号
		String sheetFloor;//地上总层数
		String sheetNowProcess;//当前进度
		String sheetProcessDate;//进度更新时间
		String sheetProcessChange;//预测变更节点
		String sheetChangeDate;//预测变更时间
		String sheetRemark;//备注
		
		for(int i = 2;i<readList.size();i++)
		{
			List<String> list = readList.get(i);
			if(null != list.get(0) && !list.get(0).trim().isEmpty())
			{
				m = i;
			}
			
			sheetIndex = list.get(0);
			sheetProject = list.get(1);
			sheetBuliding = list.get(2);
			sheetFloor = list.get(3);
			sheetNowProcess = list.get(4);
			sheetProcessDate = list.get(5);
			sheetProcessChange = list.get(6);
			sheetChangeDate = list.get(7);
			sheetRemark = list.get(8);
			
			//处理被合并的单元格
			if(null == sheetIndex || sheetIndex.trim().isEmpty())
			{
				sheetIndex = readList.get(m).get(0);
			}
			if(null == sheetProject || sheetProject.trim().isEmpty())
			{
				sheetProject = readList.get(m).get(1);
			}
			
			//校验字符非空
			Checker check = Checker.getInstance();
			if(check.checkNullOrEmpty(sheetIndex))
			{
				return MyBackInfo.fail(properties, "存在序号为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetProject))
			{
				return MyBackInfo.fail(properties, "存在项目为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetBuliding))
			{
				return MyBackInfo.fail(properties, "存在楼幢施工编号为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetFloor))
			{
				return MyBackInfo.fail(properties, "存在地上总层数为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetNowProcess))
			{
				return MyBackInfo.fail(properties, "存在当前进度为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetProcessDate))
			{
				return MyBackInfo.fail(properties, "存在预测变更时间为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetProcessChange))
			{
				return MyBackInfo.fail(properties, "存在预测变更节点为空的记录");
			}
			
			if(check.checkNullOrEmpty(sheetChangeDate))
			{
				return MyBackInfo.fail(properties, "存在预测变更时间为空的记录");
			}
			
			/*1.根据解析的“项目名称”和“楼幢编号”
			==》查询对应的开发企业
			==》查询该楼幢下的可编辑的进度节点*/
			Empj_BuildingInfo building = new Empj_BuildingInfo();
			Empj_BuildingInfoForm builingModel = new Empj_BuildingInfoForm();
			builingModel.setTheNameOfProject(sheetProject);
			builingModel.seteCodeFromConstruction(sheetBuliding);
			List<Empj_BuildingInfo> buildingList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), builingModel));
			if(null == buildingList || buildingList.size()<1)
			{
				return MyBackInfo.fail(properties, "系统中不存在项目名称："+sheetProject+"托管楼幢："+sheetBuliding+"的记录信息！");
			}
			
			for (Empj_BuildingInfo empj_BuildingInfo : buildingList)
			{
				if(empj_BuildingInfo.getTheState() == 0)
				{
					building = empj_BuildingInfo;
					break;
				}
			}
			
			if(null == building.getTableId())
			{
				building = buildingList.get(0);
			}
			
			//楼幢账户（获取楼幢的当前进度节点）
			/*Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
			if (buildingAccount != null)
			{
				properties.put("currentFigureProgress", buildingAccount.getCurrentFigureProgress()); // 当前形象进度
				properties.put("currentLimitedRatio", buildingAccount.getCurrentLimitedRatio()); // 当前受限比例
				Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVer_afDtl = buildingAccount.getBldLimitAmountVerDtl();
				if (bldLimitAmountVer_afDtl != null)
				{
					properties.put("currentBldLimitAmountVerAfDtlId", bldLimitAmountVer_afDtl.getTableId()); // 受限额度节点ID
					properties.put("newCurrentFigureProgress", bldLimitAmountVer_afDtl.getStageName()); // 当前形象进度
					properties.put("newCurrentLimitedRatio", bldLimitAmountVer_afDtl.getLimitedAmount()); // 当前受限比例
				}
			}*/
			
			//根据楼幢tableId和当前进度节点加载可预测的进度节点
			Empj_BuildingInfoForm limitModel = new Empj_BuildingInfoForm();
			limitModel.setTableId(building.getTableId());
			Properties limitExecute = empj_BldAccountGetLimitAmountVerService.execute(limitModel);
			ArrayList<HashMap<String,String>> versionList = (ArrayList<HashMap<String, String>>) limitExecute.get("versionList");
			
			/*
			 * 构建新增主表信息
			 * 开发企业
			 * 项目
			 * 楼幢-施工编号
			 * 地上层数
			 * 楼幢座落
			 * 公安编号
			 * 当前进度节点（excel-当前建设进度）
			 * 巡查人（登录用户）
			 * 巡查日期（excel-进度更新时间）
			 * 备注（excel-备注）
			 */
			Emmp_CompanyInfo developCompany = building.getDevelopCompany();
			Empj_ProjectInfo project = building.getProject();
			
			/*buildingId: 102398					--楼幢
			busiState: "维护中"						--默认（维护中）
			buttonType: 1							--默认（1）
			currentBuildProgress: "主体结构封顶"	--当前进度节点 excel-sheetNowProcess
			developCompanyId: 20095					--开发企业Id
			
			eCodeFromConstruction: "4幢"			--楼幢-施工编号
			eCodeFromPublicSecurity: "4幢"			--楼幢-公安编号
			interfaceVersion: 19000101				--版本号
			patrolPerson: "巡查人"					--巡查人（当前登录人）
			patrolTimestamp: "2019-04-16"			--巡查时间 excel-sheetProcessDate
			projectId: 1136							--项目Id
			remark: "备注"							--备注 excel- sheetRemark
			*/
			
			Empj_PjDevProgressForcastForm pjDevModel = new Empj_PjDevProgressForcastForm();
			pjDevModel.setBuildingId(building.getTableId());
			pjDevModel.setBusiState("维护中");
			pjDevModel.setButtonType("1");
			pjDevModel.setCurrentBuildProgress(sheetNowProcess);
			pjDevModel.setDevelopCompanyId(developCompany.getTableId());
			pjDevModel.seteCodeFromConstruction(building.geteCodeFromConstruction());
			pjDevModel.seteCodeFromPublicSecurity(building.geteCodeFromPublicSecurity());
			pjDevModel.setPatrolPerson(user.getTheName());
			pjDevModel.setPatrolTimestamp(sheetProcessDate);
			pjDevModel.setProjectId(project.getTableId());
			pjDevModel.setRemark(sheetRemark);
			
			/*dtlFormList: [{userStartId: 652, buildingId: 102398, beforeBldLimitAmountVerAfDtlId: 9,…}]
			0: {userStartId: 652, buildingId: 102398, beforeBldLimitAmountVerAfDtlId: 9,…}

				beforeBldLimitAmountVerAfDtlId: 9
				bldLimitAmountVerAfDtlId: "10"
				buildingId: 102398
				causeDescriptionForDelay: "原因"
				ogPredictedFinishDatetime: ""
				operationTimeStamp: 1555396067000
				predictedFinishDatetime: "2019-04-17"
				progressJudgement: 0
				userStartId: 652*/
			//子表明细信息
			List<Empj_PjDevProgressForcastDtlForm> dtlFormList = new ArrayList<Empj_PjDevProgressForcastDtlForm>();
			Empj_PjDevProgressForcastDtlForm pjDevDtlModel = new Empj_PjDevProgressForcastDtlForm();
			
			
		}
		
		
		Integer theState = S_TheState.Normal;
		String busiState = model.getBusiState();
		String eCode = sm_BusinessCodeGetService.execute(S_BusiCode.busiCode_03030201); //自动编号：TGZZ+YY+MM+DD+四位流水号（按年度流水）
		Long createTimeStamp = System.currentTimeMillis();
		Long developCompanyId = model.getDevelopCompanyId();
		Long projectId = model.getProjectId();
		Long buildingId = model.getBuildingId();
		String eCodeFromConstruction = model.geteCodeFromConstruction();
		String eCodeFromPublicSecurity = model.geteCodeFromPublicSecurity();
		String currentBuildProgress = model.getCurrentBuildProgress();
		String patrolPerson = model.getPatrolPerson();
		String remark = model.getRemark();
		Long[] idArr = model.getIdArr(); // 进度详情明细id

		String patrolTimestampStr = model.getPatrolTimestamp();


		if(developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目名称不能为空");
		}
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "施工编号不能为空");
		}
		if(patrolPerson == null || patrolPerson.length() == 0)
		{
			return MyBackInfo.fail(properties, "巡查人不能为空");
		}
		if(patrolTimestampStr == null || "".equals(patrolTimestampStr))
		{
			return MyBackInfo.fail(properties, "巡查日期不能为空");
		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "备注长度不能超过200字");
		}

		Long patrolTimestamp = MyDatetime.getInstance().stringToLong(patrolTimestampStr);

		Sm_User userStart = (Sm_User)model.getUser();
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在, 请重新登录");
		}
		Emmp_CompanyInfo developCompany = (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		if(developCompany == null)
		{
			return MyBackInfo.fail(properties, "开发企业不存在");
		}
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "项目不存在");
		}
		Empj_BuildingInfo building = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingId);
		if(building == null)
		{
			return MyBackInfo.fail(properties, "楼幢不存在");
		}

		List<Empj_PjDevProgressForcast> empj_PjDevProgressForcastList =
				empj_PjDevProgressForcastDao.findByPage(empj_PjDevProgressForcastDao.getPjDevProgressForcastCountForList(model)
						, null, null);

		Integer totalCount =
				empj_PjDevProgressForcastDao.findByPage_Size(empj_PjDevProgressForcastDao.getPjDevProgressForcastCountForList(model));
		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "已存在该楼幢的工程进度巡查预测");
		}

		List<Empj_PjDevProgressForcastDtl> empj_pjDevProgressForcastDtlList = new ArrayList<>();
		if (model.getDtlFormList() != null)
		{
			for (Empj_PjDevProgressForcastDtlForm empjPjDevProgressForcastDtlForm : model.getDtlFormList())
			{
				if (empjPjDevProgressForcastDtlForm == null)
				{
					return MyBackInfo.fail(properties, "进度节点信息不能为空");
				}
				Properties detailProperties = empj_pjDevProgressForcastDtlAddService.execute(empjPjDevProgressForcastDtlForm);
				if (S_NormalFlag.success.equals(detailProperties.getProperty(S_NormalFlag.result)))
				{
					empj_pjDevProgressForcastDtlList.add((Empj_PjDevProgressForcastDtl) detailProperties.get(
							"empj_PjDevProgressForcastDtl"));
				}
				else
				{
					return MyBackInfo.fail(properties, detailProperties.getProperty(S_NormalFlag.result));
				}
			}
		}

		Empj_PjDevProgressForcast empj_PjDevProgressForcast = new Empj_PjDevProgressForcast();
		empj_PjDevProgressForcast.setTheState(theState);
		empj_PjDevProgressForcast.setBusiState(busiState);
		empj_PjDevProgressForcast.seteCode(eCode);
		empj_PjDevProgressForcast.setUserStart(userStart);
		empj_PjDevProgressForcast.setCreateTimeStamp(createTimeStamp);
		empj_PjDevProgressForcast.setDevelopCompany(developCompany);
		empj_PjDevProgressForcast.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_PjDevProgressForcast.setProject(project);
		empj_PjDevProgressForcast.setTheNameOfProject(project.getTheName());
		empj_PjDevProgressForcast.seteCodeOfProject(project.geteCode());
		empj_PjDevProgressForcast.setBuilding(building);
		empj_PjDevProgressForcast.seteCodeOfBuilding(building.geteCode());
		empj_PjDevProgressForcast.seteCodeFromConstruction(building.geteCodeFromConstruction());
		empj_PjDevProgressForcast.seteCodeFromPublicSecurity(building.geteCodeFromPublicSecurity());
		empj_PjDevProgressForcast.setCurrentBuildProgress(currentBuildProgress);
		empj_PjDevProgressForcast.setPatrolPerson(patrolPerson);
		empj_PjDevProgressForcast.setPatrolTimestamp(patrolTimestamp);
		empj_PjDevProgressForcast.setRemark(remark);
		empj_PjDevProgressForcast.setDetailList(empj_pjDevProgressForcastDtlList);
		empj_PjDevProgressForcastDao.save(empj_PjDevProgressForcast);

		properties.put("tableId", empj_PjDevProgressForcast.getTableId());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
