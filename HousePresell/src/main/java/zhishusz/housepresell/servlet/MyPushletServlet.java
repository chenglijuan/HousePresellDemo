package zhishusz.housepresell.servlet;

import zhishusz.housepresell.messagequeue.consumer.InitAllConsumer;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.justobjects.pushlet.Version;
import nl.justobjects.pushlet.core.Command;
import nl.justobjects.pushlet.core.Config;
import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventParser;
import nl.justobjects.pushlet.core.EventSourceManager;
import nl.justobjects.pushlet.core.Protocol;
import nl.justobjects.pushlet.core.Session;
import nl.justobjects.pushlet.core.SessionManager;
import nl.justobjects.pushlet.util.Log;
import nl.justobjects.pushlet.util.Servlets;

public class MyPushletServlet extends HttpServlet implements Protocol
{
	private static final long serialVersionUID = -5848851768948795209L;
	
	@Autowired
	private InitAllConsumer initAllConsumer;
	
	public void init() throws ServletException
	{
		try
		{
			String webInfPath = getServletContext().getRealPath("/") + "/WEB-INF";
			Config.load(webInfPath);

			Log.init();

			Log.info("init() Pushlet Webapp - version=" + Version.SOFTWARE_VERSION + " built=" + Version.BUILD_DATE);

			SessionManager.getInstance().start();

			Dispatcher.getInstance().start();
			
			//手动开启监听：解决spring启动时接收消息获取pushlet空指针
			initAllConsumer.onInit();
			
			if (Config.getBoolProperty("sources.activate"))
				EventSourceManager.start(webInfPath);
			else
				Log.info("Not starting local event sources");
		}
		catch (Throwable t)
		{
			throw new ServletException("Failed to initialize Pushlet framework " + t, t);
		}
	}

	public void destroy()
	{
		Log.info("destroy(): Exit Pushlet webapp");

		if (Config.getBoolProperty("sources.activate"))
		{
			EventSourceManager.stop();
		}
		else
			Log.info("No local event sources to stop");

		Dispatcher.getInstance().stop();

		SessionManager.getInstance().stop();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Event event = null;
		Enumeration e;
		try
		{
			String eventType = Servlets.getParameter(request, "p_event");

			if (eventType == null)
			{
				Log.warn("Pushlet.doGet(): bad request, no event specified");
				response.sendError(400, "No eventType specified");
				return;
			}

			event = new Event(eventType);
			for (e = request.getParameterNames(); e.hasMoreElements();)
			{
				String nextAttribute = (String) e.nextElement();
				event.setField(nextAttribute, request.getParameter(nextAttribute));
			}

		}
		catch (Throwable t)
		{
			Log.warn("Pushlet: Error creating event in doGet(): ", t);
			response.setStatus(400);
			return;
		}

		doRequest(event, request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Event event = null;
		try
		{
			event = EventParser.parse(new InputStreamReader(request.getInputStream()));

			if (event.getEventType() == null)
			{
				Log.warn("Pushlet.doPost(): bad request, no event specified");
				response.sendError(400, "No eventType specified");
				return;
			}

		}
		catch (Throwable t)
		{
			Log.warn("Pushlet:  Error creating event in doPost(): ", t);
			response.setStatus(400);
			return;
		}

		doRequest(event, request, response);
	}

	protected void doRequest(Event anEvent, HttpServletRequest request, HttpServletResponse response)
	{
		String eventType = anEvent.getEventType();
		try
		{
			Session session = null;
			if (eventType.startsWith("join"))
			{
				session = SessionManager.getInstance().createSession(anEvent);

				String userAgent = request.getHeader("User-Agent");
				if (userAgent != null)
					userAgent = userAgent.toLowerCase();
				else
				{
					userAgent = "unknown";
				}
				session.setUserAgent(userAgent);
			}
			else
			{
				String id = anEvent.getField("p_id");

				if (id == null)
				{
					response.sendError(400, "No id specified");
					Log.warn("Pushlet: bad request, no id specified event=" + eventType);
					return;
				}

				session = SessionManager.getInstance().getSession(id);

				if (session == null)
				{
					response.sendError(400, "Invalid or expired id: " + id);
					Log.warn("Pushlet:  bad request, no session found id=" + id + " event=" + eventType);
					return;
				}

			}

			Command command = Command.create(session, anEvent, request, response);
			session.getController().doCommand(command);
		}
		catch (Throwable t)
		{
			Log.warn("Pushlet:  Exception in doRequest() event=" + eventType, t);
			t.printStackTrace();
			response.setStatus(500);
		}
	}
}
