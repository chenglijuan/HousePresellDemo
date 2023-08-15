package zhishusz.housepresell.util.fileupload;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.oreilly.servlet.multipart.FileRenamePolicy;

/*
* 主要功能：文件重命名策略的类
* 
*/
public class FileRenamePolicyUtil implements FileRenamePolicy {
	
	/*
	* 主要功能：得到新文件的名字,被rename调用用于重命名文件
	* 输入参数：字符串
	* 输出参数：字符串
	* 
	*/
	public String getNewFileName(String oldFileName)
	{
		return StringUtils.join(new String[] { java.util.UUID.randomUUID().toString(), ".", FilenameUtils.getExtension(oldFileName) });
	}
	
	/*
	* 主要功能：重新命名
	* 输入参数：文件
	* 输出参数：文件
	* 
	* 最近修改时间：2013-04-06
	* 最近修改人：YangJinpeng
	* 修改原因：
	*/
	
	public File rename(File file) {
		return new File(file.getParentFile(), getNewFileName(file.getName()));
	}
}