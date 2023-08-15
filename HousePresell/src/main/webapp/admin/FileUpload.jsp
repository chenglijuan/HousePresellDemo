<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>CH9 - File_Chinese.jsp</title>
</head>
<body>
<%	

	//声明将上传的文件放置到服务器的temp目录中
	// 声明限制上传的文件大小为 110 MB 
	//String saveDirectory = request.getRealPath("/")+"uploaded"; 
	String saveDirectory = "c:/uploaded"; 
	int    maxPostSize = 110 * 1024 * 1024 ;
	
	File file = new File(saveDirectory);
	
	if (file.exists()) {
	  System.out.println("dir not exists, create it ...");
	  file.mkdir();
	}
	
	String FileUniqueId = (String) request.getParameter("filename");
	System.out.println("FileUniqueId="+FileUniqueId);
	// 声明上传文件名称
	String FileName = null;

	// 声明上传文件类型
	String ContentType = null;

	// 声明上传文件内容描述
	String Description = null;
	
	//  计算上传文件的个数
	int count = 0 ;
	
	// 声明上传文件名所使用的编码，默认值为 ISO-8859-1，
	// 若改为GB2312则支持中文名	
	String enCoding = "UTF-8";
	
	// 产生一个新的MultipartRequest 对象，multi
	 MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize, enCoding); 
     
     String doctype= multi.getParameter("doctype");
     String name = multi.getParameter("name");// 获取普通信息  
     String typeCode = multi.getParameter("typeCode");  
     String info = multi.getParameter("info"); 
     
     Enumeration<String> filedFileNames = multi.getFileNames();
     System.out.println("filedFileNames="+filedFileNames);
     
     String filedName = null;
     while (filedFileNames.hasMoreElements()) 
     {  
    	 
         filedName = filedFileNames.nextElement();
         System.out.println("filedName="+filedName);
         
         
         File uploadFile = multi.getFile(filedName);        
         System.out.println("uploadFile="+uploadFile);
         
         if (null != uploadFile && uploadFile.length() > 0)
         {  
             String imgPath = uploadFile.getName(); 
             System.out.println("imgPath="+imgPath);
             
             //imgPath为原文件名  
             int idx = imgPath.lastIndexOf(".");  
             //文件后缀  
             String extention= imgPath.substring(idx);  
             
             //新的文件名
             String newImgPath = FileUniqueId + extention;  
               
             File f = new File(saveDirectory + "/" + newImgPath);  
             uploadFile.renameTo(f);
             
         }
         
         
     } 
     
     
     
      
%>

</body>
</html>