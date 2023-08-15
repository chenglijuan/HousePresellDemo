package zhishusz.housepresell.util;

import zhishusz.housepresell.messagequeue.producer.MQKey_EventType;
import zhishusz.housepresell.messagequeue.producer.MQKey_OrgType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MQConnectionUtil
{
	public static String GENERAL_EXCHANGE = "HOUSE_GENERAL_EXCHANGE";
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	private Gson gson = new GsonBuilder().create();
	
	private String host;
	private String virtalHost;
	private String userName;
	private String password;
	private Integer port;

	public Connection getConnection() throws Exception
	{
		//定义连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		//设置服务地址
		factory.setHost(host);
		//端口
		factory.setPort(port);
		//设置账号信息，用户名、密码、vhost
		factory.setVirtualHost(virtalHost);
		factory.setUsername(userName);
		factory.setPassword(password);
		// 通过工程获取连接
		Connection connection = factory.newConnection();
		return connection;
	}

	/**
	 * 在程序开始时执行一次即可，不需要每次都执行
	 */
	public void initSend()
	{
		try
		{
			Connection connection = getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(GENERAL_EXCHANGE, "topic");
			channel.basicPublish(GENERAL_EXCHANGE, "init", null, "init".getBytes());
			channel.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 发送消息的方法
	 *
	 * @param eventTag 枚举类型：这个方法的类型，例如买受方卖房
	 * @param type     枚举类型：接受者，例如正泰，开发商
	 * @param object   一般为继承baseform的表单类型，用于传递数据
	 */
	public void sendMessage(MQKey_EventType eventTag, MQKey_OrgType type, Object object)
	{
		Connection connection;
		try
		{
			String info = gson.toJson(object);
			connection = getConnection();
			//			connection = MQConnectionUtil.getInstance().getConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(GENERAL_EXCHANGE, "topic");
			String routingKey = eventTag.name() + "." + type.name() + ".#";
			System.out.println("routingKey is " + routingKey);
			if (info == null)
			{
				info = "";
			}
			channel.basicPublish(GENERAL_EXCHANGE, routingKey, null, info.getBytes());
			channel.close();
			connection.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @param eventTag 枚举类型：这个方法的类型，例如买受方卖房
	 * @param type     枚举类型：接受者，例如正泰，开发商
	 * @param onGet    收到消息后的回调，在回调中写主要逻辑
	 */
	public void getMessage(MQKey_EventType eventTag, MQKey_OrgType type, Action onGet)
	{
		threadPool.submit(() -> {
			try
			{
				threadGetInfo(eventTag.name(), type.name(), onGet);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	private void threadGetInfo(String eventTag, String orgType, Action onGet) throws Exception
	{
		String queueName = "Queue." + eventTag + "." + orgType;
		//		Connection connection = MQConnectionUtil.getInstance().getConnection();
		Connection connection = getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(queueName, true, false, false, null);
		String rountingKey = eventTag + "." + orgType + ".#";
		System.out.println("routingKey is " + rountingKey + " queue name is " + queueName);
		channel.queueBind(queueName, GENERAL_EXCHANGE, rountingKey);
		channel.basicQos(1);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);
		while (true)
		{
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			try {
				onGet.onAction(message);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
	}

	public interface Action
	{
		void onAction(String info);
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getVirtalHost()
	{
		return virtalHost;
	}

	public void setVirtalHost(String virtalHost)
	{
		this.virtalHost = virtalHost;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}
}
