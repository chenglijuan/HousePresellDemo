package zhishusz.housepresell.controller.form;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper=true)
public class TemplateStaticModel extends NormalActionForm
{
	private static final long serialVersionUID = 73132397583537542L;
	
	@Getter @Setter
	private String relativeOutPutFilePath;
	
	@Getter @Setter
	private String dataJsonStr;
	
	@Getter @Setter
	private String templatePath;
	
	@Getter @Setter
	private String fileOutputPath;
	
	@Getter @Setter @SuppressWarnings("rawtypes")
	private Map orgInputMap;
	
	@Getter @Setter @SuppressWarnings("rawtypes")
	private Map currentInputMap;
}
