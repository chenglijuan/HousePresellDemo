package zhishusz.housepresell.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightImportExcelForm;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：机构信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_RemainRightImportExcelService
{

	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RemainRightImportExcelForm model)
	{
		Properties properties = new MyProperties();
//		System.out.println("url:" + model.getUrl());
		ExcelReader reader = ExcelUtil.getReader(FileUtil.file(model.getUrl()));
		List<Tgpf_RemainRight> tgpf_RemainRights = new ArrayList<Tgpf_RemainRight>();
		List<List<Object>> readAll = reader.read(1);
		String date = DateUtil.format(new Date(), "yyyyMMdd");
		for (int j = 0; j < readAll.size(); j++) {
			List<Object> objects = readAll.get(j);
			Tgpf_RemainRight tgpf_RemainRight = new Tgpf_RemainRight();
			for (int i = 0; i < objects.size(); i++) {
				boolean isRigthData = true;
				
				Object object = objects.get(i);
				if (i == 0) {
					if (object == null) {
						break;
					} else if (object.toString().trim().isEmpty()) {
						break;
					} else if (!Character.isDigit(object.toString().trim().charAt(0))) {
						break;
					}
				}
				switch (i) {
				case 0:
					// 序号
					String indexOfRemainRight = object.toString();
					while (indexOfRemainRight.length() < 4) {
						indexOfRemainRight = "0" + indexOfRemainRight;
					}
					tgpf_RemainRight.seteCode("LCQY" + date + indexOfRemainRight);
					break;
				case 1:
					// 日期
					if (object.toString().trim().length() == 0) {
						isRigthData = false;
						break;
					}
					try {
						if (model.getBillTimeStamp() != null) {
//							if (DateUtil.parse(model.getBillTimeStamp()).getTime() != DateUtil.parse(object.toString()).getTime()) {
////							isRigthData = false;
////							break;
//
//								properties.put(S_NormalFlag.result, S_NormalFlag.fail);
//								properties.put(S_NormalFlag.info, "第" + (j + 2) + "行日期和记账日期不一致");
//								return properties;
//							}
						}

						tgpf_RemainRight.setEnterTimeStamp(DateUtil.parse(object.toString()).getTime());
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行日期格式错误");
						return properties;
					}
					break;
				case 2:
					// 买受人姓名
					tgpf_RemainRight.setBuyer(object.toString());
					break;
				case 3:
					// 借款人姓名
					tgpf_RemainRight.setTheNameOfCreditor(object.toString());
					break;
				case 4:
					// 借款人证件号码
					tgpf_RemainRight.setIdNumberOfCreditor(object.toString());
					break;
				case 5:
					// 合同备案号
					tgpf_RemainRight.seteCodeOfContractRecord(object.toString());
					break;
				case 6:
					// 项目名称
					tgpf_RemainRight.setTheNameOfProject(object.toString());
					break;
				case 7:
					// 楼幢号
					tgpf_RemainRight.seteCodeOfBuilding(object.toString());
					break;
				case 8:
					// 单元号
					tgpf_RemainRight.seteCodeOfBuildingUnit(object.toString());
					break;
				case 9:
					// 房间号
					tgpf_RemainRight.seteCodeFromRoom(object.toString());
					break;
				case 10:
					// 实际入账金额
					try {
						tgpf_RemainRight.setActualDepositAmount(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行入账金额错误");
						return properties;
					}
					break;
				case 11:
					// 按揭贷款入账金额
					try {
						tgpf_RemainRight.setDepositAmountFromLoan(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行按揭贷款入账金额错误");
						return properties;
					}
					break;
				case 12:
					// 贷款账号
					tgpf_RemainRight.setTheAccountFromLoan(object.toString());
					break;
				case 13:
					// 资金性质
					// TODO
//					tgpf_RemainRight.setFundProperty(object.toString());
					break;
				case 14:
					// 入账银行
					tgpf_RemainRight.setTheNameOfBankPayedIn(object.toString());
					break;
				case 15:
					// 留存权益系数
					try {
						tgpf_RemainRight.setTheRatio(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行留存权益系数错误");
						return properties;
					}
					break;
				case 16:
					// 留存权益总金额
					try {
						tgpf_RemainRight.setTheAmount(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行留存权益总金额错误");
						return properties;
					}
					break;
				case 17:
					// 受限权益
					try {
						tgpf_RemainRight.setLimitedRetainRight(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行受限权益错误");
						return properties;
					}
					break;
				case 18:
					// 可支取权益
					try {
						tgpf_RemainRight.setWithdrawableRetainRight(MyDouble.getInstance().getShort(Double.valueOf(object.toString()), 2));
					} catch (Exception e) {
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "第" + (j + 2) + "行可支取权益错误");
						return properties;
					}
					break;
				case 20:
					// 备注
					if (object != null) {
						tgpf_RemainRight.setRemark(object.toString());
					}
					tgpf_RemainRights.add(tgpf_RemainRight);
					break;

				default:
					break;
				}
				if (!isRigthData) {
					break;
				}
			}
		}
		
		properties.put("tgpf_RemainRightList", tgpf_RemainRights);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
