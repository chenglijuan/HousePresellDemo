package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.controller.form.extra.Tgxy_JumpForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_contractDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_personofcontractDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：从楼盘表户信息，直接跳转签署三方协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_RoomJumpTripleService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Tb_b_contractDao tb_b_contractDao;
	@Autowired
	private Tb_b_personofcontractDao tb_b_personofcontractDao;

	public Properties execute(Tgxy_JumpForm model)
	{
		Properties properties = new MyProperties();

		// return MyBackInfo.fail(properties, "该合同已签署有效的三方协议信息");
		
		/*
		 * 检查当前登录人员是否具有提取合同的权限
		 * 1.由合同加载出户室信息，根据户室信息查询所属楼幢、项目、开发企业
		 * 2.再根据当前登录人员的授权信息查询比较是否具有权限提取
		 * 
		 */
		Sm_User user = model.getUser();
		if(null == user)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}
		Emmp_CompanyInfo company = user.getCompany();
		if(null == company)
		{
			return MyBackInfo.fail(properties, "未查询到当前登录人员所属企业信息！");
		}

		Tgxy_JumpForm vo = new Tgxy_JumpForm();

		/*
		 * xsz by time 2018-9-28 17:24:24
		 * 通过户室信息查询相关信息，用于签署三方协议
		 * 1.查询户室基本信息
		 * 2.查询楼幢、项目信息
		 * 3.查询关联的合同及买受人信息
		 * 
		 */
		String roomid = model.getRoomid();

		if (null == roomid || roomid.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择有效的户室信息");
		}
		Long tableId = new Long(roomid);

		/*
		 * 查询户室基本信息
		 * 1.校验次户室信息是否已签署有效的三方协议
		 * 2.查询该户室有效的合同信息
		 * 3.校验关联的合同信息是否已签署有效三方协议
		 */
		Empj_HouseInfo houseInfo = empj_HouseInfoDao.findById(tableId);
		if (null == houseInfo || houseInfo.getTheState() == S_TheState.Deleted)
		{
			return MyBackInfo.fail(properties, "未查询到有效的户室信息");
		}

		vo.setHouseId(tableId);
		vo.setHouseRoom(houseInfo.getRoomId());
		vo.setPosition(houseInfo.getPosition());

		/*
		 * 校验是否存在与此户室信息相关且有效的三方协议
		 * 
		 * 三方协议有效字段：0,1,2
		 * 0-为空（默认）
		 * 1-生效（代理公司上传三方协议和商品房买卖合同签字页后）
		 * 2-退房退款待处理（退房退款流程发起时标记三方协议状态为待处理，）
		 * 3-失效：
		 */
		Tgxy_TripleAgreementForm tgxy_TripleAgreementmodel = new Tgxy_TripleAgreementForm();
		tgxy_TripleAgreementmodel.setHouse(houseInfo);
		tgxy_TripleAgreementmodel.setTheState(S_TheState.Normal);

		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao
				.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementmodel));

		if (totalCount > 0)
		{
			return MyBackInfo.fail(properties, "该户室已存在有效的三方协议");
		}

		/*
		 * 查询相关的楼幢、项目、开发企业信息
		 */
		Empj_BuildingInfo building = houseInfo.getBuilding();
		if (null == building)
		{
			return MyBackInfo.fail(properties, "未查询到该户室相关楼幢信息");
		}

		vo.setBuildingInfoId(building.getTableId());
		vo.setECodeFromConstruction(building.geteCodeFromConstruction());
		vo.setECodeFromPublicSecurity(building.geteCodeFromPublicSecurity());
		vo.setECodeFromPresellSystem(building.geteCodeFromPresellSystem());
		vo.setECodeOfBuilding(building.geteCodeFromPresellSystem());

		Empj_ProjectInfo project = building.getProject();
		if (null == project)
		{
			return MyBackInfo.fail(properties, "未查询到该户室相关项目信息");
		}

		vo.setProjectId(project.getTableId());
		vo.setProjectName(project.getTheName());

		Emmp_CompanyInfo company1 = project.getDevelopCompany();
		if (null == company1)
		{
			return MyBackInfo.fail(properties, "未查询到该户室相关开发企业信息");
		}
		
		//校验提取权限
		properties = getIsExtract(model, properties, company, houseInfo,project,building);
		if(!MyBackInfo.isSuccess(properties))
		{
			return properties;
		}

		vo.setSellerName(company1.getTheName());

		
		/*
		 * xsz by time 2018-11-27 16:38:57
		 * 户室信息提取合同从中间库提取：
		 * 根据户室的externalId从中间库取合同信息
		 */
		Tb_b_contract tb_b_contractDetail;

		try
		{
			tb_b_contractDetail = tb_b_contractDao.getContractByRoomId(houseInfo.getExternalId());
		}
		catch (Exception e)
		{
			return MyBackInfo.fail(properties, "查询合同异常，请稍后重试");
		}

		if (null == tb_b_contractDetail || null == tb_b_contractDetail.getHtbh()
				|| tb_b_contractDetail.getHtbh().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "未查询到相关合同信息，请稍后重试");
		}

		// 处理签约日期格式
		tb_b_contractDetail.setQdwctime(tb_b_contractDetail.getQdwctime());

		vo.setECodeOfContractRecord(tb_b_contractDetail.getHtbh());
		vo.setBuildingArea(Double.valueOf(tb_b_contractDetail.getMj()));
		vo.setContractSumPrice(Double.valueOf(tb_b_contractDetail.getContractprice()));
		vo.setContractSignDate(tb_b_contractDetail.getQdtime());
		vo.setPaymentMethod(tb_b_contractDetail.getFkfs());
		vo.setPayDate(tb_b_contractDetail.getJfrq());

		/*
		 * xsz by time 2018-9-9 17:23:55
		 * 查询与合同信息相关的买受人信息
		 * 
		 */
		Tb_b_personofcontract tb_b_personofcontract;
		List<Tb_b_personofcontract> tb_b_personofcontractList;
		List<Tb_b_personofcontract> tb_b_personofcontractDetail;
		tb_b_personofcontractDetail = tb_b_personofcontractDao
				.getTb_b_personofcontractDetail2(tb_b_contractDetail.getHtbh());

		if (null == tb_b_personofcontractDetail || tb_b_personofcontractDetail.size() == 0)
		{
			// 未查询到相关买受人信息
			tb_b_personofcontractList = new ArrayList<Tb_b_personofcontract>();
		}
		else
		{
			tb_b_personofcontract = new Tb_b_personofcontract();
			tb_b_personofcontractList = new ArrayList<Tb_b_personofcontract>();

			/*
			 * 查询到买受人信息，进行拆分处理
			 * （可能存在多个买受人信息为一条记录，需要手动拆分）
			 */
			if (tb_b_personofcontractDetail.size() == 1)
			{

				tb_b_personofcontract = tb_b_personofcontractDetail.get(0);

				// 新的分割方式
				// 定义分割符
				boolean isSplit = true;
				String[] splitSign = new String[] {
						"、", ",", "，", "/", "\\", " ", "  "
				};

				for (String s : splitSign)
				{
					// 如果姓名或者证件号有被分割
					/*
					 * xsz by time 2018-11-27 09:04:58
					 * 增加电话号码分割
					 */
					if (tb_b_personofcontract.getBuyerName().indexOf(s) != -1
							|| tb_b_personofcontract.getECodeOfcertificate().indexOf(s) != -1)
					{
						// 如果存在分割字段，则对数据进行分割
						String[] names = tb_b_personofcontract.getBuyerName().split(s);// 分割姓名
						String[] cardnos = tb_b_personofcontract.getECodeOfcertificate().split(s);// 分割证件号

						String[] phones = tb_b_personofcontract.getContactPhone().split(s);

						// 默认以姓名分割
						boolean flag = true;

						if (names.length == 1)
						{
							for (String n : splitSign)
							{
								if (tb_b_personofcontract.getBuyerName().indexOf(n) != -1)
								{
									names = tb_b_personofcontract.getBuyerName().split(n);// 分割姓名
								}

							}
						}
						else
						{
							flag = false;
						}

						if (cardnos.length == 1)
						{
							for (String c : splitSign)
							{
								if (tb_b_personofcontract.getECodeOfcertificate().indexOf(c) != -1)
								{
									cardnos = tb_b_personofcontract.getECodeOfcertificate().split(c);// 分割证件号
								}
							}
						}

						if (phones.length == 1)
						{
							for (String c : splitSign)
							{
								if (tb_b_personofcontract.getContactPhone().indexOf(c) != -1)
								{
									phones = tb_b_personofcontract.getContactPhone().split(c);// 分割手机号
								}
							}
						}

						// 将拆分信息录入
						for (int i = 0; i < names.length; i++)
						{
							Tb_b_personofcontract person = new Tb_b_personofcontract();

							/*
							 * xsz by time 2018-11-27 09:16:48
							 * 录入之前判断手机号是否有被分割
							 * 根据手机号的分割情况进行数据录入
							 */

							person.setContactPhone(tb_b_personofcontract.getContactPhone());
							person.setContactAdress(tb_b_personofcontract.getContactAdress());

							person.setAgentAddress(tb_b_personofcontract.getAgentAddress());
							person.setAgentCertNumber(tb_b_personofcontract.getAgentCertNumber());
							person.setAgentCertType(tb_b_personofcontract.getAgentCertType());
							person.setAgentName(tb_b_personofcontract.getAgentName());
							person.setAgentPhone(tb_b_personofcontract.getAgentPhone());
							person.setBuyerName(tb_b_personofcontract.getBuyerName());
							person.setCertificateType(tb_b_personofcontract.getCertificateType());
							person.setContactAdress(tb_b_personofcontract.getContactAdress());
							person.setContactPhone(tb_b_personofcontract.getContactPhone());
							person.setContractId(tb_b_personofcontract.getContractId());
							person.setECodeOfcertificate(tb_b_personofcontract.getECodeOfcertificate());
							person.setECodeOfContract(tb_b_personofcontract.getECodeOfContract());

							person.setBuyerName(names[i]);

							if (cardnos.length == names.length)
							{
								person.setECodeOfcertificate(cardnos[i]);
							}
							if (phones.length == names.length)
							{
								person.setContactPhone(phones[i]);
							}

							tb_b_personofcontractList.add(person);
						}

						isSplit = false;
						break;

					}

				}

				if (isSplit)
					tb_b_personofcontractList.add(tb_b_personofcontract);
			}
		}

		// 买受人信息
		List<Tgxy_BuyerInfo> buyerList = new ArrayList<Tgxy_BuyerInfo>();
		if (null != tb_b_personofcontractList && tb_b_personofcontractList.size() > 0)
		{
			Tgxy_BuyerInfo buyerInfo;
			for (Tb_b_personofcontract personofcontract : tb_b_personofcontractList)
			{
				buyerInfo = new Tgxy_BuyerInfo();
				buyerInfo.setBuyerName(personofcontract.getBuyerName());
				buyerInfo.setCertificateType(personofcontract.getCertificateType());
				buyerInfo.seteCodeOfcertificate(personofcontract.getECodeOfcertificate());
				buyerInfo.setContactPhone(personofcontract.getContactPhone());
				buyerInfo.setContactAdress(personofcontract.getContactAdress());
				
				buyerList.add(buyerInfo);
				
			}
		}
		
		 if (null != buyerList && buyerList.size() > 0)
		{
			String buyname = "";

			for (Tgxy_BuyerInfo buyer : buyerList)
			{
				buyname += "，" + buyer.getBuyerName();
			}

			vo.setBuyerName(buyname.substring(1, buyname.length()));

		}

		/*
		 * 查询合同
		 */
		// Tgxy_ContractInfoForm conForm = new Tgxy_ContractInfoForm();
		// conForm.setHouseInfo(houseInfo);
		// conForm.setTheState(S_TheState.Normal);
		//
		// Tgxy_ContractInfo contract = (Tgxy_ContractInfo) tgxy_ContractInfoDao
		// .findOneByQuery(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(),
		// conForm));
		//
		// List<Tgxy_BuyerInfo> buyerList = new ArrayList<Tgxy_BuyerInfo>();
		//
		// if (null == contract)
		// {
		// return MyBackInfo.fail(properties, "该户室未签署合同信息");
		// }
		// else
		// {
		// vo.setECodeOfContractRecord(contract.geteCodeOfContractRecord());
		// vo.setBuildingArea(contract.getBuildingArea());
		// vo.setContractSumPrice(contract.getContractSumPrice());
		// vo.setContractSignDate(contract.getContractSignDate());
		// vo.setPaymentMethod(contract.getPaymentMethod());
		// vo.setPayDate(contract.getPayDate());
		//
		// /*
		// * 根据合同查询买受人
		// */
		// Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
		// buyForm.seteCodeOfContract(contract.geteCodeOfContractRecord());
		// buyForm.setTheState(S_TheState.Normal);
		//
		// buyerList = tgxy_BuyerInfoDao
		// .findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(),
		// buyForm));
		//
		// if (null != buyerList && buyerList.size() > 0)
		// {
		// String buyname = "";
		//
		// for (Tgxy_BuyerInfo buyer : buyerList)
		// {
		// buyname += "，" + buyer.getBuyerName();
		// }
		//
		// vo.setBuyerName(buyname.substring(1, buyname.length()));
		//
		// }
		//
		// }

		properties.put("tgxy_TripleAgreement", vo);
		properties.put("buyerList", buyerList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	private Properties getIsExtract(Tgxy_JumpForm model, Properties properties, Emmp_CompanyInfo company,
			Empj_HouseInfo houseInfo,Empj_ProjectInfo projectInfo,Empj_BuildingInfo buildingInfo)
	{
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		
		if(S_CompanyType.Agency.equals(company.getTheType()))
		{
			//代理公司，根据授权判断
			Long[] projectInfoIdArr = model.getProjectInfoIdArr();//项目权限
			
			boolean isExtract = false;
			
			if(projectInfoIdArr.length > 0)
			{
				for (Long long1 : projectInfoIdArr)
				{
					if(long1.equals(houseInfo.getBuilding().getProject().getTableId()))
					{
						isExtract = true;
						break;
					}
				}
			}
			if(!isExtract)
			{
				Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();//楼幢权限
				if(buildingInfoIdIdArr.length  > 0)
				{
					for (Long long1 : buildingInfoIdIdArr)
					{
						if(long1.equals(houseInfo.getBuilding().getTableId()))
						{
							isExtract = true;
							break;
						}
					}
				}
			}
			
			if(!isExtract)
			{
				Long[] cityRegionInfoIdArr = model.getCityRegionInfoIdArr();//区域权限
				if(projectInfoIdArr.length > 0)
				{
					for (Long long1 : cityRegionInfoIdArr)
					{
						if(long1.equals(houseInfo.getBuilding().getCityRegion().getTableId()))
						{
							isExtract = true;
							break;
						}
					}
				}
			}
			
			if(!isExtract)
			{
				return MyBackInfo.fail(properties, "该用户无权限提取此合同");
			}
			
		}
		else if(S_CompanyType.Development.equals(company.getTheType()))
		{
			//开发企业，加载本企业下的信息
			if(!company.getTableId().equals(projectInfo.getDevelopCompany().getTableId()))
			{
				return MyBackInfo.fail(properties, "该用户无权限提取此合同");
			}
		}
		else if(S_CompanyType.Zhengtai.equals(company.getTheType()))
		{
			//正泰用户，加载全部
			
		}
		else 
		{
			return MyBackInfo.fail(properties, "该用户无权限提取此合同");
		}
		
		
		return properties;
	}
	
}
