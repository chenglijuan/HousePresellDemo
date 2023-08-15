package zhishusz.housepresell.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectoryUtil
{
	public String createRelativePathWithDate(String rootProfileName)
	{
		String autoCreatedDateDirByParttern = "yyyy/MM/dd/";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(autoCreatedDateDirByParttern);
		String autoCreatedDateDir = simpleDateFormat.format(new Date());
		String strDirectory = rootProfileName + "/" + autoCreatedDateDir;
		
		return strDirectory;
	}
	
	public String getProjectRoot()
	{
		String strDirectory = Thread.currentThread().getContextClassLoader().getResource("").toString();
		if(strDirectory.indexOf("file:/") > -1)
		{//windows system
			if(strDirectory.indexOf("WEB-INF") > -1){
				strDirectory = strDirectory.substring(5, strDirectory.indexOf("WEB-INF"));
			}else{
				return strDirectory;
			}
			//System.out.println("1------:"+strDirectory);
			if(strDirectory.indexOf(":/") > -1)
			{
				strDirectory = strDirectory.substring(1, strDirectory.length());
			}
			//System.out.println("2------:"+strDirectory);
		}
		return strDirectory;
	}
	
	public void mkdir(String strDir)
	{
		File savePath = new File(strDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
	}
}
