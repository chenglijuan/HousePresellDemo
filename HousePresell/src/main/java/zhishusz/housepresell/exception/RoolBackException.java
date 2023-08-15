package zhishusz.housepresell.exception;

public class RoolBackException extends RuntimeException
{
	private static final long serialVersionUID = -6878202967916953648L;

	public RoolBackException()
	{
		super();
	}

	public RoolBackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RoolBackException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RoolBackException(String message)
	{
		super(message);
	}

	public RoolBackException(Throwable cause)
	{
		super(cause);
	}
}
