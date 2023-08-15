package zhishusz.housepresell.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_NoticeForm;
	
/*
 * 触发后台推送
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_PushletService
{
	@Autowired
	private Sm_NoticeAddService sm_NoticeAddService;
	
	public void execute(String theTitle, String theMessage, Long tableId)
	{
		try
		{
			//后台推送 --> 通知ID为1的用户，告诉他查看了楼幢信息列表
			Sm_NoticeForm noticeForm = new Sm_NoticeForm();
			noticeForm.setTheTitle(theTitle);
			noticeForm.setTheMessage(theMessage);
			noticeForm.setTableId(tableId);
			sm_NoticeAddService.executeForAdmin(noticeForm);
		}
		catch (Exception e)
		{
			System.out.println("推送失败");
		}
	}
}
