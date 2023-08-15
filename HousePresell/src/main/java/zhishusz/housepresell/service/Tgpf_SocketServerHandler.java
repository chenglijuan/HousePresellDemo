package zhishusz.housepresell.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import javax.annotation.Resource;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 银行端服务处理器
 * 
 * @description TODO 处理管理系统请求
 * @author 苏州智数
 * @date 2018-07-03 10:26:32
 */

@Service
public class Tgpf_SocketServerHandler extends IoHandlerAdapter
{

	public static Logger log = LoggerFactory.getLogger(Tgpf_SocketServerHandler.class);

	private boolean isOutputLog = false;
	
	@Resource
	private Tgpf_SocketServerHangOutService sckHangOutService;

	/**
	 * 设置isOutputLog
	 * 
	 * @param isOutputLog
	 */
	public void setIsOutputLog(boolean isOutputLog)
	{
//		System.out.println("setIsOutputLog");
		this.isOutputLog = isOutputLog;
	}

	/*
	 * 异常处理
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.
	 * mina.core.session.IoSession, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		// TODO 处理异常
		// super.exceptionCaught(session, cause);
		System.out.println("exceptionCaught");
		log.error("服务端:服务通信出错了!即将将关闭会话.", cause);
		super.exceptionCaught(session, cause);
	}

	/*
	 * 建立会话
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.
	 * mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// TODO 建立会话
		// super.sessionCreated(session);
//		System.out.println("sessionCreated");
		if (isOutputLog && log.isInfoEnabled())
			log.info("服务端:建立会话...对方地址:{}", session.getRemoteAddress());
	}

	/*
	 * 打开会话
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache.
	 * mina.core.session.IoSession)
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		// TODO 打开会话
		// super.sessionOpened(session);
//		System.out.println("sessionOpened");
		if (isOutputLog && log.isInfoEnabled())
			log.info("服务端:打开会话...对方地址:{}", session.getRemoteAddress());
	}

	/*
	 * 会话关闭
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.
	 * mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		// TODO 会话关闭
		// super.sessionClosed(session);
//		System.out.println("sessionClosed");
		if (isOutputLog && log.isInfoEnabled())
			log.info("服务端:关闭会话...对方地址:{}", session.getRemoteAddress());
	}

	/*
	 * 会话空闲
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#sessionIdle(org.apache.mina
	 * .core.session.IoSession, org.apache.mina.core.session.IdleStatus)
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception
	{
		// TODO 会话空闲
		// super.sessionIdle(session, status);
//		System.out.println("sessionIdle"+" status");
		super.sessionIdle(session, status);
	}

	/*
	 * 接收消息
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.
	 * mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{ 
		// TODO 接收消息
		// super.messageReceived(session, message);

		if (log.isInfoEnabled())
			log.info("服务端:消息接收...当前接收消息:{}", message);
		
		System.out.println("服务端:消息接收...当前接收消息 :"+message);
			
		String recMsg =  message.toString();     //ioBufferToString(message);
		
		String returnMsg = sckHangOutService.sendMessage(recMsg);


//		Object retMsg = stringToIoBuffer(returnMsg);
		
		System.out.println("服务端:消息返回...当前返回消息 :"+ returnMsg);
				
		session.write(returnMsg);
		session.close(true);
	
	}

	/*
	 * 消息发送
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina
	 * .core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception
	{
		// TODO 消息发送
//		super.messageSent(session, message);
//		System.out.println("messageSent");
		if (isOutputLog && log.isInfoEnabled())
			log.info("服务端:消息已被发送...当前发送消息:" + message);	
	}
	

	/** 
	* 将bytebuffer转换成string   
	* @param str 
	 * @throws UnsupportedEncodingException 
	*/  
	public static IoBuffer stringToIoBuffer(String s) throws UnsupportedEncodingException  
	{

		byte bt[] = s.getBytes();

		IoBuffer ioBuffer = IoBuffer.allocate(bt.length);
		ioBuffer.put(bt, 0, bt.length);
		ioBuffer.setAutoExpand(true);
		ioBuffer.flip();
		return ioBuffer;
	}  
	
	/** 
	* 将IoBuffer转换成string   
	* @param butBuffer 
	 * @throws UnsupportedEncodingException 
	*/  
	public static String ioBufferToString(Object message) throws UnsupportedEncodingException  
	{
		if (!(message instanceof IoBuffer))
		{
			return "";
		}
		IoBuffer ioBuffer = (IoBuffer) message;
	    ByteBuffer buf = ioBuffer.buf();
	    byte[] msg = new byte[buf.limit()];
	    buf.get(msg);
	    String ss = new String(msg,"UTF-8");
		return ss;
	}  
}
/// :~Add by 苏州智数 since 2018-07-03 10:26:32