package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_contractDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_personofcontractDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库查询合同信息
 * 备份2018-9-27 18:49:44
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
public class Tb_b_contractDetailDetailServiceCopy
{
	@Autowired
	private Tb_b_contractDao tb_b_contractDao;
	@Autowired
	private Tb_b_personofcontractDao tb_b_personofcontractDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 三方协议
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室

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
			tgxy_TripleAgreementmodel.setTheStateOfTripleAgreementEffect("(0,1,2)");

			Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(tgxy_TripleAgreementDao
					.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), tgxy_TripleAgreementmodel));

			if (totalCount > 0)
			{
				return MyBackInfo.fail(properties, "合同备案号：" + beianno + "，已签署过相关三方协议");
			}

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
			tb_b_contractDetail.setQdwctime(null == tb_b_contractDetail.getQdwctime() ? "-"
					: tb_b_contractDetail.getQdwctime().substring(0, 10));

			// 根据预售系统中关联的项目、楼幢、户室Id查询本系统信息
			String projectid = tb_b_contractDetail.getProjectid();
			String buildingid = tb_b_contractDetail.getBuildingid();
			String roomid = tb_b_contractDetail.getRoomid();

			// 项目
			Empj_ProjectInfo prijectInfo;
			Empj_ProjectInfoForm form1 = new Empj_ProjectInfoForm();
			form1.setExternalId(projectid);
			Object query1 = empj_ProjectInfoDao
					.findOneByQuery(empj_ProjectInfoDao.getQuery(empj_ProjectInfoDao.getDetailHql(), form1));

			if (null != query1)
			{
				prijectInfo = (Empj_ProjectInfo) query1;

				Long tableId = prijectInfo.getTableId();

				tb_b_contractDetail.setProjectid(String.valueOf(tableId));
			}
			else
			{
				return MyBackInfo.fail(properties, "关联项目Id:" + projectid + "，未查询到相关信息");
			}

			// 楼幢
			Empj_BuildingInfo building;
			Empj_BuildingInfoForm form2 = new Empj_BuildingInfoForm();
			form2.setExternalId(buildingid);
			Object query2 = empj_BuildingInfoDao
					.findOneByQuery(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getDetailHql(), form2));
			if (null != query2)
			{
				building = (Empj_BuildingInfo) query2;

				Long tableId = building.getTableId();

				/*
				 * 判断查询出的楼幢信息在托管系统中是否已处于托管状态
				 * busiState:0 未托管态
				 */
				// if ("0".equals(building.getBusiState()))
				// {
				// return MyBackInfo.fail(properties, "施工编号：" +
				// building.geteCodeFromConstruction() + ",未托管");
				// }

				tb_b_contractDetail.setBuildingid(String.valueOf(tableId));

			}
			else
			{
				return MyBackInfo.fail(properties, "关联楼幢Id:" + buildingid + "，未查询到相关信息");
			}

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
			}
			else
			{
				return MyBackInfo.fail(properties, "关联户室Id:" + roomid + "，未查询到相关信息");
			}

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

					// 判断是否是多个联系人拼接，是否需要手动拆分
					if (tb_b_personofcontract.getBuyerName().contains("、"))
					{
						String[] names = tb_b_personofcontract.getBuyerName().split("、");
						String[] cardnos = tb_b_personofcontract.getECodeOfcertificate().split("、");
						String[] phones = tb_b_personofcontract.getContactPhone().split("、");
						String[] adresses = tb_b_personofcontract.getContactAdress().split("、");
						// 将查分信息录入
						for (int i = 0; i < names.length; i++)
						{
							Tb_b_personofcontract person = new Tb_b_personofcontract();

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
							if (adresses.length == names.length)
							{
								person.setContactAdress(adresses[i]);
							}

							tb_b_personofcontractList.add(person);
						}

					}
					else
					{
						tb_b_personofcontractList.add(tb_b_personofcontract);
					}

				}

			}

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			properties.put("tb_b_contract", tb_b_contractDetail);
			properties.put("buyerList", tb_b_personofcontractList);

		}
		catch (Exception e)
		{
			return MyBackInfo.fail(properties, "查询异常：" + e.getMessage() + ";;;" + e);
		}

		return properties;
	}
}
