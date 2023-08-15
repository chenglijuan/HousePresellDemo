package zhishusz.housepresell.util.fileupload;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import zhishusz.housepresell.database.po.state.S_FileType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import com.google.gson.JsonObject;

import cn.hutool.json.JSONObject;

/**
 * 文件工具类
 * 
 */
public class FileUtil
{
	private static FileUtil instance;
	private FileUtil()
	{
		
	}
	
	public static FileUtil getInstance()
	{
		if(instance == null) instance = new FileUtil();
		
		return instance;
	}
	
	/**
	 * 根据指定路径，读取该路径下的文件和文件夹
	 * @param filepath 文件物理路径
	 * @param pId 父级文件的ID
	 * @param jsonList 用于存放读取到的文件参数
	 * @param filterFileRegex 想要获取文件的正则表达式，为null则获取所有
	 * @param filterFolderRegex 排除不需要的文件夹的正则表达式，为null则获取所有
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readFileList(String filepath,String pId,List<JsonObject> jsonList,String filterFileRegex,String filterWithoutFolderRegex) throws FileNotFoundException, IOException
	{
		File file = new File(filepath);
		//System.out.println("初次调用："+file.isDirectory());
		
		//若指定路径是一个文件，则直接返回文件路径和文件名称
		if (!file.isDirectory())
		{
			if(filterFileRegex != null )
			{
				if(!Pattern.compile(filterFileRegex).matcher(file.getName()).find())
				{
					return;
				}
			}
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", 1);
			jsonObject.addProperty("pId", pId);
			jsonObject.addProperty("name", file.getName());
			jsonObject.addProperty("thePath", file.getPath());
			jsonObject.addProperty("isParent", false);
			jsonList.add(jsonObject);
		}
		//若指定路径是一个文件夹，
		else if (file.isDirectory())
		{
			//获取指定文件夹下的所有子文件（含子文件夹）
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++)
			{
				File readfile = new File(filepath + "/" + filelist[i]);
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", pId+"_"+(i+1));
				jsonObject.addProperty("pId", pId);
				jsonObject.addProperty("name", readfile.getName());
				jsonObject.addProperty("thePath", readfile.getPath());//物理路径
				
				Boolean isDirectory = readfile.isDirectory();
				
				if (isDirectory)
				{
					jsonObject.addProperty("isParent", true);
					
					if(filterWithoutFolderRegex != null )
					{
						if(Pattern.compile(filterWithoutFolderRegex).matcher(readfile.getName()).find())
						{
							continue;
						}
					}
				}
				else
				{
					jsonObject.addProperty("isParent", false);
					
					if(filterFileRegex != null )
					{
						if(!Pattern.compile(filterFileRegex).matcher(readfile.getName()).find())
						{
							continue;
						}
					}
				}
				
				jsonList.add(jsonObject);
				
				//System.out.println("id：" + (pId+""+(i+1)) + " pId：" + pId + " name："+ readfile.getName()+" thePath："+ readfile.getPath());
				
				if (isDirectory)
				{
					readFileList(filepath + "/" + filelist[i],pId+"_"+(i+1),jsonList,filterFileRegex,filterWithoutFolderRegex);
				}
			}
		}
	}
	
	/**
     * 根据文件所在的路径，从文本文件中读取内容
     * 
     * @param path
     * @return 从文本文件中读取内容
	 * @throws IOException 
     */
    public String readFileContent(String path) 
    {
    	BufferedReader br = null;
    	String read = "";
        String readStr = "";
        File file = new File(path);
        
        try
		{
        	if(file.isDirectory())
        	{
        		return readStr;
        	}
        	
        	br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
        	while ((read = br.readLine()) != null) {
        		if("".equals(readStr))
        		{
        			readStr = read;
        		}
        		else
        		{
        			readStr = readStr + "\n" +read;
        		}
        	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        finally
        {
        	if(br != null)
        	{
        		try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
        	}
        }
        
        return readStr;
    }
    
    /**
     * 创建新文件/文件夹
     * @param filePath
     * @return
     */
    public JsonObject addFile(String filePath,Integer fileType)
    {	
    	File file = new File(filePath);
    	JsonObject jsonObject = new JsonObject();
    	
    	if(fileType == null)
    	{
    		jsonObject.addProperty("result", "fail");
			jsonObject.addProperty("info", "File Type Error, we can't create the file");//文件类型参数错误，创建文件失败
			return jsonObject;
    	}
    	else if(S_FileType.Folder.equals(fileType) && !file.exists())
		{
			file.mkdirs();//create folder
		}
		else
		{
			try
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}
				if (!file.exists())
				{
					file.createNewFile();//create file
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				jsonObject.addProperty("result", "fail");
				jsonObject.addProperty("info", "Create the file Error");
				return jsonObject;
			}
		}
    	
    	jsonObject.addProperty("result", "success");
		jsonObject.addProperty("info", S_NormalFlag.info_Success);
		return jsonObject;
    }
    
    /**
     * 删除文件/文件夹
     * @param filePath
     * @return
     */
    public JsonObject deleteFile(String filePath) 
    {  
	    File file = new File(filePath);  
	    JsonObject jsonObject = new JsonObject();
	    
	    if (file == null || !file.exists())
	    {
	    	jsonObject.addProperty("result", "fail");
			jsonObject.addProperty("info", "File is not existed");
			return jsonObject;
	    }
    	if (file.isDirectory()) 
	    {  
	        File[] children = file.listFiles();  
	        for (int i = 0; i < children.length; i++) {  
	            deleteFile(children[i].getPath());  
	        }  
	    }
    	
    	//删除最后的父级文件
    	file.delete();
	    
	    jsonObject.addProperty("result", "success");
		jsonObject.addProperty("info", S_NormalFlag.info_Success);
		return jsonObject;
	}
    
    /**
     * 将文件内容写入到指定路径
     * @param filePath
     * @param theContent
     */
    public JsonObject writeFileContent(String filePath,String theContent)
    {
    	File file = new File(filePath);
    	JsonObject jsonObject = new JsonObject(); 
		
    	if(file.isDirectory())
		{
			jsonObject.addProperty("result", "fail");
			jsonObject.addProperty("info", "Update error, the file is a folder");//该文件是文件夹，无法更新
			return jsonObject;
		}
		
    	if (!file.exists())
		{
			jsonObject.addProperty("result", "fail");
			jsonObject.addProperty("info", "We can't find the file");
			return jsonObject;
		}
		
		//写入
		BufferedWriter bw = null;
		try
		{
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
			
			file.createNewFile();
			
			//true:追加“写模式” false:从头“写模式”
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),"utf-8"));
			bw.write(theContent);
			bw.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(bw != null)
				{
					bw.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		jsonObject.addProperty("result", "success");
		jsonObject.addProperty("info", S_NormalFlag.info_Success);
		return jsonObject;
    }
    
    /**
     * 根据文件的物理路径，获取网络路径
     * @param projectPath 项目物理路径
     * @param serverBasePath 服务器网络根路径
     * @param physicalPath 文件的网络路径
     * @return
     */
    public String getFileNetworkPath(String projectPath,String serverBasePath,String physicalPath)
    {
    	if(physicalPath != null && physicalPath.length() > 0)
		{
			physicalPath = physicalPath.replaceAll("\\\\", "/");
		}
		if(physicalPath != null && physicalPath.startsWith(projectPath))
		{
			physicalPath = physicalPath.substring(projectPath.length(), physicalPath.length());
			physicalPath = serverBasePath + physicalPath;
		}
		else if(physicalPath != null)
		{
			physicalPath = serverBasePath + physicalPath;
		}
		
		return physicalPath;
    }
    
    /**
     * 根据文件的网络路径，获取物理路径
     * @param projectPath 项目物理路径
     * @param serverBasePath 服务器网络根路径
     * @param networkPath 文件的物理路径
     * @return
     */
    public String getFilePhysicalPath(String projectPath,String serverBasePath,String networkPath)
    {
    	if(networkPath != null && networkPath.length() > 0)
    	{
    		networkPath = networkPath.substring(serverBasePath.length(), networkPath.length());
    		networkPath = projectPath + networkPath;
    	}
		return networkPath;
    }
    
    /**
     * 根据文件名称，判断是否是文件夹，还是文件
     */
    public Boolean checkIsDirectory(String fileName)
    {
    	if(fileName == null) return null;
    	if(Pattern.compile("(\\.)(\\w*)$").matcher(fileName).find())
    	{
    		return false;
    	}
    	return true;
    }
    
    /** 
     * 判断文件是否是图片 
     * @param file 
     * @return 
     * @throws IOException  
     */  
    public Boolean isImage(String path)
    {  
        File file = new File(path);
        
        if(file.isDirectory())
    	{
    		return false;
    	}
        
		try
		{
			BufferedImage bi = ImageIO.read(file);
			if(bi == null)
			{  
				return false;  
			}
			
			return true;  
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return false;
    }
    
    /**
     * 判断指定路径的文件是否存在
     * @param newFilePhysicalPath
     * @return
     */
    public Boolean checkFileIsExists(String newFilePhysicalPath)
    {
    	if(newFilePhysicalPath != null && newFilePhysicalPath.length() > 0)
    	{
    		File file = new File(newFilePhysicalPath);
    		if(file.exists())
        	{
        		return true;
        	}
    	}
    	
    	return false;
    }
	
	/**
     * 将文件内容写入到指定路径
     * @param filePath
     * @param theContent
     */
    public JSONObject writeFileContentStr(String filePath,String theContent)
    {
    	JSONObject jsonObject = new JSONObject(); 

		//写入
		BufferedWriter bw = null;
		try
		{
			File file = new File(filePath);
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
			
			if (!file.exists()) {
				file.createNewFile();
			}
			
			file.createNewFile();
			
			//true:追加“写模式” false:从头“写模式”
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false),"utf-8"));
			bw.write(theContent);
			bw.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(bw != null)
				{
					bw.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		jsonObject.put("result", "success");
		jsonObject.put("info", S_NormalFlag.info_Success);
		return jsonObject;
    }
    
    public String readFileContentStr(String path) 
    {
    	BufferedReader br = null;
    	String read = "";
        String readStr = "";
        File file = new File(path);
        
        try
		{
        	if(file.isDirectory())
        	{
        		return readStr;
        	}
        	
        	br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
        	while ((read = br.readLine()) != null) {
        		if("".equals(readStr))
        		{
        			readStr = read;
        		}
        		else
        		{
        			readStr = readStr + "\n" +read;
        		}
        	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        finally
        {
        	if(br != null)
        	{
        		try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
        	}
        }
        
        return readStr;
    }
}
