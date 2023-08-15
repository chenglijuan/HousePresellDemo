package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_contractDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_personofcontractDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库查询合同信息
 * 
 * @ClassName: Tb_b_ontractDetailDetailService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月2日 下午8:48:17
 * @version V1.0
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tb_b_contractDetailDetailSplitService
{
	@Autowired
	private Tb_b_contractDao tb_b_contractDao;
	@Autowired
	private Tb_b_personofcontractDao tb_b_personofcontractDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;// 本地合同
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;// 买受人信息

	@SuppressWarnings("unchecked")
	public Properties execute(Tb_b_contractFrom model)
	{
		Properties properties = new MyProperties();

		try
		{
			/*
			 * xsz by time 2018-9-2 21:08:40
			 * 从中间库提取合同信息
			 * 
			 * 校验：
			 * 1.此合同备案号是否 已存在且有效的三方协议
			 * 2.查询中间库返回规则信息
			 * ==》与中间库连接异常--->返回错误信息
			 * ==》在中间库中为查询到相关合同信息--->返回提示信息
			 */
			// 获取合同备案号
			String beianno = model.getBeianno();

			/*
			 * 校验是否存在与此合同备案号相关且有效的三方协议
			 * 
			 * 三方协议有效字段：0,1,2
			 * 0-为空（默认）
			 * 1-生效（代理公司上传三方协议和商品房买卖合同签字页后）
			 * 2-退房退款待处理（退房退款流程发起时标记三方协议状态为待处理，）
			 * 3-失效：
			 */
			Tgxy_TripleAgreementForm tgxy_TripleAgreementmodel = new Tgxy_TripleAgreementForm();
			tgxy_TripleAgreementmodel.seteCodeOfContractRecord(beianno);
			tgxy_TripleAgreementmodel.setTheState(S_TheState.Normal);

			Integer totalCount = 0;
			totalCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao
					.getQuery_Size(tgxy_TripleAgreementDao.getTripleAgreementContractHQL(), tgxy_TripleAgreementmodel));

			if (totalCount > 0)
			{
				return MyBackInfo.fail(properties, "合同备案号：" + beianno + "，已签署过相关三方协议");
			}

			/*
			 * xsz by time 2018-9-27 10:33:59
			 * 在进行中间库查询合同之前，先查询本地是否存在合同信息
			 * ==》存在，直接从本系统中提取信息
			 * ==》不存在，再进行中间库的提取
			 */
			Tgxy_ContractInfoForm contractInfomodel = new Tgxy_ContractInfoForm();
			contractInfomodel.seteCodeOfContractRecord(beianno);
			contractInfomodel.setTheState(S_TheState.Normal);

			totalCount = tgxy_ContractInfoDao.findByPage_Size(
					tgxy_ContractInfoDao.getQuery_Size(tgxy_ContractInfoDao.getBasicHQL(), contractInfomodel));

			if (totalCount > 0)
			{
				// 从本地系统获取合同信息
				List<Tgxy_ContractInfo> tgxy_ContractInfoList;
				tgxy_ContractInfoList = tgxy_ContractInfoDao.findByPage(
						tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), contractInfomodel));

				Tgxy_ContractInfo contract = tgxy_ContractInfoList.get(0);

				Tb_b_contract tb_b_contractDetail = new Tb_b_contract();

				// 楼幢
				Empj_BuildingInfo buildingInfo = contract.getBuildingInfo();
				if (null == buildingInfo)
				{
					return MyBackInfo.fail(properties, "未查询到楼幢相关信息");
				}
				tb_b_contractDetail.setBuildingid(String.valueOf(buildingInfo.getTableId()));

				// 项目
				Empj_ProjectInfo project = buildingInfo.getProject();
				if (null == project)
				{
					return MyBackInfo.fail(properties, "未查询到项目相关信息");
				}
				tb_b_contractDetail.setProjectid(String.valueOf(project.getTableId()));

				// 户室
				Empj_HouseInfo houseInfo = contract.getHouseInfo();
				if (null == houseInfo)
				{
					return MyBackInfo.fail(properties, "未查询到户室相关信息");
				}

				// 设置合同信息
				localContractSet(beianno, contract, tb_b_contractDetail, houseInfo);

				// 查询买受人信息
				Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
				buyForm.seteCodeOfContract(beianno);
				buyForm.setTheState(S_TheState.Normal);

				List<Tgxy_BuyerInfo> list = new ArrayList<Tgxy_BuyerInfo>();

				list = tgxy_BuyerInfoDao
						.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), buyForm));

				if (null == list || list.size() == 0)
				{
					tb_b_contractDetail.setMsr("");
					return MyBackInfo.fail(properties, "未查询到买受人相关信息");
				}
				else
				{
					StringBuffer buynamebf = new StringBuffer();
					List<Tb_b_personofcontract> tb_b_personofcontractList = new ArrayList<Tb_b_personofcontract>();
					// 设置本地买受人信息
					localBuyerSet(list, buynamebf, tb_b_personofcontractList);

					// 设置买受人姓名拼接
					tb_b_contractDetail.setMsr(buynamebf.toString().substring(1, buynamebf.toString().length()));

					properties.put("tb_b_contract", tb_b_contractDetail);
					properties.put("buyerList", tb_b_personofcontractList);

				}

			}
			else
			{
				// 从中间库获取合同信息

				/*
				 * 进行中间库查询合同信息
				 * 查询异常
				 * 合同信息为空
				 * 
				 * 1.查询到合同信息后，在本系统中查询所关联的项目、楼幢、户室信息（根据extralId进行查询）
				 * 2.查询合同关联的楼幢信息是否托管（业务状态=1为托管态）
				 * 
				 * 3.根据合同关联号查询买受人信息
				 */
				Tb_b_contract tb_b_contractDetail;
				try
				{

					// tb_b_contractDetail =
					// tb_b_contractDao.getTb_b_ontractDetail(beianno);
					tb_b_contractDetail = tb_b_contractDao.getTb_b_ontractDetail2(beianno);

				}
				catch (Exception e)
				{
					properties.put("log", "异常信息：" + e.getMessage() + ";;;;" + e);
					return MyBackInfo.fail(properties, "查询合同异常，请手动输入合同信息或稍后再试...");
				}

				if (null == tb_b_contractDetail || null == tb_b_contractDetail.getHtbh()
						|| tb_b_contractDetail.getHtbh().trim().isEmpty())
				{
					return MyBackInfo.fail(properties, "合同备案号：" + beianno + "，未查询到相关信息，请手动输入合同信息或检查后重试...");
				}

				// 处理签约日期格式
				tb_b_contractDetail.setQdwctime(tb_b_contractDetail.getQdwctime());

				// 根据预售系统中关联的项目、楼幢、户室Id查询本系统信息
				String roomid = tb_b_contractDetail.getRoomid();

				// 户室信息
				Empj_HouseInfo house;
				Empj_HouseInfoForm form3 = new Empj_HouseInfoForm();
				form3.setExternalId(roomid);
				Object query3 = empj_HouseInfoDao
						.findOneByQuery(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getDetailHQL(), form3));
				if (null != query3)
				{
					house = (Empj_HouseInfo) query3;

					Long tableId = house.getTableId();

					tb_b_contractDetail.setRoomid(String.valueOf(tableId));

					// 通过查询出的户室信息查询出楼幢id、项目id
					Empj_BuildingInfo building = house.getBuilding();
					if (null == building || null == building.getTableId() || building.getTableId() <= 0)
					{
						return MyBackInfo.fail(properties, "未查询到楼幢信息");
					}

					// 判断楼幢的托管状态
					Empj_BuildingExtendInfo extendInfo = building.getExtendInfo();
					if (S_EscrowState.UnEscrowState.equals(extendInfo.getEscrowState()))
					{
						return MyBackInfo.fail(properties, "楼幢信息未托管");
					}

					tb_b_contractDetail.setBuildingid(String.valueOf(building.getTableId()));

					Empj_ProjectInfo project = building.getProject();
					if (null == project || null == project.getTableId() || project.getTableId() <= 0)
					{
						return MyBackInfo.fail(properties, "未查询到项目信息");
					}
					tb_b_contractDetail.setProjectid(String.valueOf(project.getTableId()));

				}
				else
				{
					return MyBackInfo.fail(properties, "关联户室Id:" + roomid + "，未查询到相关信息");
				}

				properties.put("tb_b_contract", tb_b_contractDetail);

				/*
				 * xsz by time 2018-9-9 17:23:55
				 * 查询与合同信息相关的买受人信息
				 * 
				 */
				Tb_b_personofcontract tb_b_personofcontract;
				List<Tb_b_personofcontract> tb_b_personofcontractList;
				List<Tb_b_personofcontract> tb_b_personofcontractDetail;
				// tb_b_personofcontractDetail =
				// tb_b_personofcontractDao.getTb_b_personofcontractDetail(beianno);
				tb_b_personofcontractDetail = tb_b_personofcontractDao.getTb_b_personofcontractDetail2(beianno);

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

				properties.put("buyerList", tb_b_personofcontractList);

			}

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		}
		catch (Exception e)
		{
			return MyBackInfo.fail(properties, "系统异常：" + e.getMessage() + "；" + e);
		}

		return properties;
	}

	/*
	 * xsz by time 2018-11-28 14:42:38
	 * 本地买受人信息设置值
	 */
	private void localBuyerSet(List<Tgxy_BuyerInfo> list, StringBuffer buynamebf,
			List<Tb_b_personofcontract> tb_b_personofcontractList)
	{
		for (Tgxy_BuyerInfo po : list)
		{

			Tb_b_personofcontract person = new Tb_b_personofcontract();

			person.setAgentAddress(po.getAgentAddress());
			person.setAgentCertNumber(po.getAgentCertNumber());
			person.setAgentCertType(po.getAgentCertType());
			person.setAgentName(po.getAgentName());
			person.setAgentPhone(po.getAgentPhone());
			person.setBuyerName(po.getBuyerName());
			person.setCertificateType(po.getCertificateType());
			person.setContactAdress(po.getContactAdress());
			person.setContactPhone(po.getContactPhone());
			person.setECodeOfcertificate(po.geteCodeOfcertificate());
			person.setECodeOfContract(po.geteCodeOfContract());

			buynamebf.append("，" + po.getBuyerName());

			tb_b_personofcontractList.add(person);

		}
	}

	/*
	 * xsz by time 2018-11-28 14:42:21
	 * 本地合同信息设置值
	 */
	private void localContractSet(String beianno, Tgxy_ContractInfo contract, Tb_b_contract tb_b_contractDetail,
			Empj_HouseInfo houseInfo)
	{
		tb_b_contractDetail.setRoomid(String.valueOf(houseInfo.getTableId()));

		// 合同其他信息
		tb_b_contractDetail.setBeianno(beianno);
		tb_b_contractDetail.setRoomlocation(contract.getPosition());
		tb_b_contractDetail.setCmr(contract.getTheNameFormCompany());
		tb_b_contractDetail.setQdwctime(contract.getContractSignDate());
		tb_b_contractDetail.setContractprice(String.valueOf(contract.getContractSumPrice()));
		tb_b_contractDetail.setMj(String.valueOf(contract.getBuildingArea()));
		tb_b_contractDetail.setFkfs(contract.getPaymentMethod());
		tb_b_contractDetail.setHtbh(contract.geteCodeOfBuilding());
		tb_b_contractDetail.setBawctime(contract.getContractRecordDate());
	}

}
