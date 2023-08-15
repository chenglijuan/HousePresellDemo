package zhishusz.housepresell.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.extra.ResponseContract;
import zhishusz.housepresell.database.po.extra.ResponseContractResult;
import zhishusz.housepresell.database.po.extra.ResponseResult;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 定时轮询三方协议同步合同信息
 * 
 * @author ZS
 *
 */
@Slf4j
@Service
@Transactional(transactionManager = "transactionManager")
public class ContractUpdateService {

	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	@SuppressWarnings("unchecked")
	public void execute() {
		log.info("ContractUpdateService.execute()##定时任务开始" + System.currentTimeMillis());
		System.out.println("三方协议定时更新任务开始" + System.currentTimeMillis());

		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("900001");
		baseParameterForm0.setParametertype("90");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));
		if (null == baseParameter0) {
			log.equals("未查询到配置路径！");
			return;
		}

		// 查询token
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("900002");
		baseParameterForm.setParametertype("90");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
		if (null == baseParameter) {
			log.equals("未查询到token信息！");
			return;
		}

		Tgxy_TripleAgreementForm tripleModel = new Tgxy_TripleAgreementForm();
		tripleModel.setTheState(S_TheState.Normal);
		tripleModel.setApprovalState(S_ApprovalState.WaitSubmit);
		List<Tgxy_TripleAgreement> tripleAgreementList = tgxy_TripleAgreementDao
				.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), tripleModel));

		System.out.println("待提交的三方协议数" + tripleAgreementList.size());

		Map<String, String> map = new HashMap<>();
		map.put("token", baseParameter.getTheName());

		String ecodeOfContract;
		String jsonString;
		String httpStringPostRequest;
		ResponseResult response;
		ResponseContractResult contract;
		ResponseContract result;

		Tgxy_ContractInfo contractInfo = new Tgxy_ContractInfo();
		Tgxy_BuyerInfo tgxy_BuyerInfo;

		String contractAmount;
		String downPayment;
		String loanAmount;
		String buyer;
		String position;

		Tgxy_TripleAgreement tgxy_TripleAgreement;

		for (Tgxy_TripleAgreement tripleAgreement : tripleAgreementList) {

			tgxy_TripleAgreement = tripleAgreement;
			ecodeOfContract = tripleAgreement.getEcodeOfContract();

			map.put("ContractNO", ecodeOfContract);
			jsonString = JSONObject.toJSONString(map);

			try {

				httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(baseParameter0.getTheName(),
						jsonString);

				response = JSONObject.parseObject(httpStringPostRequest, ResponseResult.class);
				if ("true".equals(response.getSuccess())) {
					contract = JSONObject.parseObject(httpStringPostRequest, ResponseContractResult.class);
					result = contract.getResult();

					// 比较金额是否需要更新
					if ((tgxy_TripleAgreement.getContractAmount()
							- Double.parseDouble(result.getContractAmount())) != 0) {
						
						System.out.println(tgxy_TripleAgreement.geteCode() + "更新合同金额");
						
						tgxy_TripleAgreement.setContractAmount(Double.parseDouble(result.getContractAmount()));
					}
					if ((tgxy_TripleAgreement.getFirstPayment() - Double.parseDouble(result.getDownPayment())) != 0) {
						
						System.out.println(tgxy_TripleAgreement.geteCode() + "更新首付款金额");
						tgxy_TripleAgreement.setFirstPayment(Double.parseDouble(result.getDownPayment()));
					}

					if ((tgxy_TripleAgreement.getLoanAmount() - Double.parseDouble(result.getLoanAmount())) != 0) {
						System.out.println(tgxy_TripleAgreement.geteCode() + "更新贷款金额");
						tgxy_TripleAgreement.setLoanAmount(Double.parseDouble(result.getLoanAmount()));
					}

					position = result.getPosition();
					tgxy_TripleAgreement.setRoomlocation(position);

					// 比较买受人是否需要更新
					buyer = result.getBuyer();
					if (!tgxy_TripleAgreement.getBuyerName().equals(result.getBuyer())) {
						
						System.out.println(tgxy_TripleAgreement.geteCode() + "更新买受人信息");
						
						tgxy_TripleAgreement.setBuyerName(result.getBuyer());

						Tgxy_BuyerInfoForm buyModel = new Tgxy_BuyerInfoForm();
						buyModel.setTheState(S_TheState.Normal);
						buyModel.seteCodeOfTripleAgreement(tripleAgreement.geteCode());
						buyModel.setTripleAgreement(tripleAgreement);
						List<Tgxy_BuyerInfo> buyerList = tgxy_BuyerInfoDao
								.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), buyModel));

						for (Tgxy_BuyerInfo buyerInfo : buyerList) {
							contractInfo = buyerInfo.getContractInfo();

							buyerInfo.setTheState(S_TheState.Deleted);
							tgxy_BuyerInfoDao.update(buyerInfo);
						}

						if (result.getBuyerName().contains(",")) {
							String[] names = result.getBuyerName().split(",");
							String[] cardnos = result.getBuyerCardNo().split(",");
							String[] phones = result.getBuyerPhone().split(",");
							for (int i = 0; i < names.length; i++) {
								tgxy_BuyerInfo = new Tgxy_BuyerInfo();
								tgxy_BuyerInfo.setTheState(S_TheState.Normal);
								tgxy_BuyerInfo.setBusiState("1");
								tgxy_BuyerInfo.setBuyerType("1");
								tgxy_BuyerInfo.setCertificateType("1");
								tgxy_BuyerInfo.seteCodeOfContract(ecodeOfContract);
								tgxy_BuyerInfo.seteCodeOfTripleAgreement(tripleAgreement.geteCode());
								tgxy_BuyerInfo.setHouseInfo(tripleAgreement.getHouse());
								tgxy_BuyerInfo.setContractInfo(contractInfo);
								tgxy_BuyerInfo.setTripleAgreement(tgxy_TripleAgreement);
								tgxy_BuyerInfo.setContactAdress(result.getBuyerAddress());
								tgxy_BuyerInfo.setBuyerName(names[i]);
								/*tgxy_BuyerInfo.seteCodeOfcertificate(cardnos[i]);
								tgxy_BuyerInfo.setContactPhone(phones[i]);*/
								
								if (cardnos.length < names.length) {
									tgxy_BuyerInfo.seteCodeOfcertificate(cardnos[0]);
								} else {
									tgxy_BuyerInfo.seteCodeOfcertificate(cardnos[i]);
								}

								if (phones.length < names.length) {
									tgxy_BuyerInfo.setContactPhone(phones[0]);
								} else {
									tgxy_BuyerInfo.setContactPhone(phones[i]);
								}

								tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);

							}

						} else {
							tgxy_BuyerInfo = new Tgxy_BuyerInfo();
							tgxy_BuyerInfo.setTheState(S_TheState.Normal);
							tgxy_BuyerInfo.setBusiState("1");
							tgxy_BuyerInfo.setBuyerType("1");
							tgxy_BuyerInfo.setCertificateType("1");
							tgxy_BuyerInfo.seteCodeOfContract(ecodeOfContract);
							tgxy_BuyerInfo.seteCodeOfTripleAgreement(tripleAgreement.geteCode());
							tgxy_BuyerInfo.setHouseInfo(tripleAgreement.getHouse());
							tgxy_BuyerInfo.setContractInfo(contractInfo);
							tgxy_BuyerInfo.setTripleAgreement(tgxy_TripleAgreement);
							tgxy_BuyerInfo.setContactAdress(result.getBuyerAddress());
							tgxy_BuyerInfo.setBuyerName(result.getBuyerName());
							tgxy_BuyerInfo.seteCodeOfcertificate(result.getBuyerCardNo());
							tgxy_BuyerInfo.setContactPhone(result.getBuyerPhone());
							tgxy_BuyerInfoDao.save(tgxy_BuyerInfo);
						}

					}

					tgxy_TripleAgreementDao.update(tgxy_TripleAgreement);

				}

			} catch (Exception e) {

				System.out.println("更新异常" + e.getMessage());
				log.error("exception-jsonString:" + jsonString);
				continue;
			}

		}

		System.out.println("三方协议定时更新任务开始" + System.currentTimeMillis());
		log.info("ContractUpdateService.execute()##定时任务结束" + System.currentTimeMillis());
	}

}