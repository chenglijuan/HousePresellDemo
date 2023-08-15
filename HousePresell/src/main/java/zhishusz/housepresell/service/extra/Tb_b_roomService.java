package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_buildingForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_roomForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_buildingDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_roomDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Tb_b_building;
import zhishusz.housepresell.database.po.extra.Tb_b_room;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UnitType;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库-户室取数
 * 
 * @ClassName: Tb_b_roomService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月26日 上午10:57:55
 * @version V1.0
 *
 */
@Service
@Transactional
public class Tb_b_roomService
{
	private static final String BUSI_CODE_H = "03010205";// 户室编码

	@Autowired
	private Tb_b_roomDao dao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;

	public Properties execute(Tb_b_roomForm model)
	{
		Properties properties = new MyProperties();

		// 获取请求类型
		String type = model.getType();

		switch (type)
		{
		case "listByBuild":
			// 根据楼幢rowguid查询楼幢列表
			String buildingid = model.getBUILDINGID();

			if (null == buildingid || buildingid.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择正确的楼幢信息");
			}

			List<Tb_b_room> list = new ArrayList<Tb_b_room>();

			list = dao.getRoomListByBuildingId(buildingid);

			properties.put("roomList", list);

			break;

		case "detail":

			// 根据户室rowguid查询户室详情
			String rowguid = model.getROWGUID();

			if (null == rowguid || rowguid.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择户室");
			}

			Tb_b_room vo = new Tb_b_room();

			vo = dao.getRoomDetail(rowguid);

			properties.put("roomDetail", vo);

			break;

		case "save":

			Sm_User user = model.getUser();
			if (null == user)
			{
				return MyBackInfo.fail(properties, "请先进行登录");
			}

			// 根据楼幢tableId同步预售系统未同步的户室信息
			Long tableId = model.getTableId();
			if (null == tableId || tableId < 1)
			{
				return MyBackInfo.fail(properties, "选择楼幢信息无效");
			}

			String buildingUpdate = empj_BuildingInfoDao.buildingUpdate(tableId, user.getTableId());
			
			/*// 查询楼幢信息
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(tableId);
			if (null == buildingInfo)
			{
				return MyBackInfo.fail(properties, "未查询到有效的楼幢信息");
			}
			if (null == buildingInfo.getExternalId() || buildingInfo.getExternalId().trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "该楼幢未同步预售系统");
			}

			// 根据ExternalId查询预售系统中该楼幢下的户室信息
			List<Empj_HouseInfo> houseList = dao.getHouseList(buildingInfo.getExternalId());

			if (null == houseList || houseList.size() == 0)
			{
				return MyBackInfo.fail(properties, "同步成功");
			}

			// 查询该楼幢本系统中已同步的户室信息
			List<Empj_HouseInfo> houseInfoList = new ArrayList<Empj_HouseInfo>();

			String sql = "select * from empj_houseinfo eh where eh.thestate='0' and eh.building='"
					+ buildingInfo.getTableId() + "' and eh.externalid is not null";

			houseInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Empj_HouseInfo.class)
					.getResultList();

			if (null != houseInfoList && houseInfoList.size() > 0)
			{

				boolean isAdd = true;

				for (Empj_HouseInfo houseVo : houseList)
				{
					for (Empj_HouseInfo housePo : houseInfoList)
					{

						
						 * xsz by time 2018-10-8 16:36:45
						 * 将本地户室的ExternalId与中间库列表户室的ExternalId（rowguid）比较
						 * 如果有相同的，则直接跳过，否则，进行保存至托管系统
						 * 
						 * xsz by time 2018-11-12 10:02:44
						 * 将本地户室的ExternalId与中间库列表户室的ExternalId（roomid）比较
						 
						if (houseVo.getExternalId().equals(housePo.getExternalId()))
						{
							isAdd = false;
							break;
						}
						else
						{
							isAdd = true;
						}

					}

					if (isAdd)
					{

						// 设置关联楼幢信息
						houseVo.setBuilding(buildingInfo);
						houseVo.seteCodeOfBuilding(buildingInfo.geteCodeFromConstruction());

						String remark = houseVo.getRemark();

						Empj_UnitInfo unitInfo;
						// 判断如果字段为空或者是99，则不需要关联单元信息
						if (null != remark && !remark.trim().isEmpty() && !"99".equals(remark))
						{
							String name;
							try
							{
								name = S_UnitType.getMsg(houseVo.getRemark());

								// 设置关联单元信息
								Empj_UnitInfoForm unitForm = new Empj_UnitInfoForm();
								unitForm.setBuilding(buildingInfo);
								unitForm.setTheName(name);

								// 20181009 已确认楼幢信息下的户室关联的单元信息一定存在，所以此时未进行非空校验
								unitInfo = (Empj_UnitInfo) empj_UnitInfoDao.findOneByQuery(
										empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQL(), unitForm));

								houseVo.setUnitInfo(unitInfo);
								houseVo.seteCodeOfUnitInfo(unitInfo.getTheName());

							}
							catch (Exception e)
							{

							}

						}

						houseVo.setCreateTimeStamp(System.currentTimeMillis());
						houseVo.setLastUpdateTimeStamp(System.currentTimeMillis());
						houseVo.setUserStart(user);
						houseVo.setUserUpdate(user);
						houseVo.setTheState(S_TheState.Normal);
						houseVo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE_H));
						houseVo.seteCodeFromEscrowSystem(houseVo.geteCode());

						houseVo.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());

						empj_HouseInfoDao.save(houseVo);

					}

				}

			}*/

			break;

		default:

			return MyBackInfo.fail(properties, "请选择正确的请求类型");

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
