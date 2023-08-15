package zhishusz.housepresell.util;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyCodeFactory implements ProtocolCodecFactory
{

	private final MyProtocolEncoder encoder;
	private final MyProtocolDecoder decoder;

	/* final static char endchar = 0x1a; */
	final static char endchar = 0x0d;

	public MyCodeFactory()
	{
		this(Charset.forName("UTF-8"));
	}

	public MyCodeFactory(Charset charset)
	{
		this.encoder = new MyProtocolEncoder(charset);
		this.decoder = new MyProtocolDecoder(charset);
	}

	public int getEncoderMaxDataLength()
	{
		return this.encoder.getMaxDataLength();
	}

	public void setEncoderMaxDataLength(int maxDataLength)
	{
		this.encoder.setMaxDataLength(maxDataLength);
	}

	public int getDecoderMaxDataLength()
	{
		return this.decoder.getMaxDataLength();
	}

	public void setDecoderMaxDataLength(int maxDataLength)
	{
		this.decoder.setMaxDataLength(maxDataLength);
	}

	public void setDecoderPrefixLength(int prefixLength)
	{
		this.decoder.setPrefixLength(prefixLength);
	}

	public int getDecoderPrefixLength()
	{
		return this.decoder.getPrefixLength();
	}

	public void setEncoderPrefixLength(int prefixLength)
	{
		this.encoder.setPrefixLength(prefixLength);
	}

	public int getEncoderPrefixLength()
	{
		return this.encoder.getPrefixLength();
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception
	{
		return this.encoder;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception
	{
		return this.decoder;
	}

}
