package zhishusz.housepresell.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_NoticeForm;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

/*
 * 后台推送消息添加
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_NoticeAddService
{
	public void executeForAdmin(Sm_NoticeForm model)
	{
		Long tableId = model.getTableId();
		
		try
		{
			Event event = Event.createDataEvent("/noty/"+tableId);
			
			String theTitle = URLEncoder.encode(model.getTheTitle(), "UTF-8");//推送消息标题
			String theMessage = URLEncoder.encode(model.getTheMessage(), "UTF-8");//推送消息内容
			event.setField("theTitle", theTitle);
			event.setField("theMessage", theMessage);
			
			Dispatcher.getInstance().multicast(event);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
