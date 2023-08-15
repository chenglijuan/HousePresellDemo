package test.rabbitMQ;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import zhishusz.housepresell.util.MQConnectionUtil;

import test.api.BaseJunitTest;

@Rollback(value=false)
@Transactional(transactionManager="transactionManager")
public class TestSend extends BaseJunitTest
{
	@Test
	public void execute() throws Exception 
	{
		MQConnectionUtil mqConnectionUtil = new MQConnectionUtil();
		mqConnectionUtil.setPort(5672);
		mqConnectionUtil.setHost("192.168.1.155");
		mqConnectionUtil.setUserName("zachary");
		mqConnectionUtil.setPassword("123456");
		mqConnectionUtil.setVirtalHost("zacharyHousePresell");
		
		mqConnectionUtil.sendMessage(MQKey_EventType.BUYER_BUY_HOUSE, MQKey_OrgType.DEVELOPER,"哈哈哈");
	}
	
	@Test
	public void execute1() throws Exception 
	{
		MQConnectionUtil mqConnectionUtil = new MQConnectionUtil();
		mqConnectionUtil.setPort(5672);
		mqConnectionUtil.setHost("127.0.0.1");
		mqConnectionUtil.setUserName("yangyu123");
		mqConnectionUtil.setPassword("123456");
		mqConnectionUtil.setVirtalHost("/housepersell");
		
		Tg_EarlyWarningFrom model = new Tg_EarlyWarningFrom();
		
		mqConnectionUtil.sendMessage(MQKey_EventType.EARLY_WARNING_SENDER, MQKey_OrgType.EARLY,"预警发送提醒");
		
	}
	
	@Test
	public void MQSendMessage() throws Exception
	{
		Emmp_BankBranchForm model = new Emmp_BankBranchForm();
		model.setInterfaceVersion(DefaultInterfaceVersion);
		

		MultiValueMap<String, String> multiValueMap = transBeanToMap(model);
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post("/Test_SendMessage2").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.params(multiValueMap)
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();
		
		System.out.println(mvcResult.getResponse().getContentAsString());
	}
	
}
