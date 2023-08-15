package zhishusz.housepresell.util;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyProtocolDecoder
  extends CumulativeProtocolDecoder
{
  public static final int DEFAULT_PREFIX_LENGTH = 4;
  public static final int DEFAULT_MAX_DATA_LENGTH = 2048;
  private final Charset charset;
  private int prefixLength = 4;
  private int maxDataLength = 2048;
  
  public MyProtocolDecoder(Charset charset, int prefixLength, int maxDataLength)
  {
    this.charset = charset;
    this.prefixLength = prefixLength;
    this.maxDataLength = maxDataLength;
  }
  
  public MyProtocolDecoder(Charset charset, int prefixLength)
  {
    this(charset, prefixLength, 2048);
  }
  
  public MyProtocolDecoder(Charset charset)
  {
    this(charset, 4);
  }
  
  public void setPrefixLength(int prefixLength)
  {
    this.prefixLength = prefixLength;
  }
  
  public int getPrefixLength()
  {
    return this.prefixLength;
  }
  
  public void setMaxDataLength(int maxDataLength)
  {
    this.maxDataLength = maxDataLength;
  }
  
  public int getMaxDataLength()
  {
    return this.maxDataLength;
  }
  
  protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
    throws Exception
  {
    String msg = in.getString(this.charset.newDecoder());
    out.write(msg);
    return true;
  }
}
