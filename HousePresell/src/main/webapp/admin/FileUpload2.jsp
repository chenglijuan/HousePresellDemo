<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="com.tongzhi.tools.PDFTools"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
            System.out.println("=====================");
	        try {
	            //获取表单
	            FileItemFactory factory = new DiskFileItemFactory();
	            ServletFileUpload upload = new ServletFileUpload(factory);
	            upload.setHeaderEncoding("utf-8");
	            List items = null;
	            try {
	                items = upload.parseRequest(request);
	            } catch (Exception ex) {
	            }
	            Iterator iter = items.iterator();
	            Map<String, Object> PostEntitys = new HashMap<String, Object>();
	            while (iter.hasNext()) {
	                FileItem item   =  (FileItem)iter.next();
	                String datatype =  item.getContentType();
	                System.out.println("==datatype="+datatype);
	                boolean isFormField=item.isFormField();
	                if(isFormField)
	                {
	                   System.out.println("==isFormField:true");
	                   PostEntitys.put(item.getFieldName(), item.getString());
	                }
	                else
	                {
	                   System.out.println("==isFormField:false");
	                   PostEntitys.put(item.getFieldName(), item.getInputStream());
	                }

	            }
	             
	            
	            //获取上传的文件
	            InputStream upFileStream = PostEntitys.get("upFile") != null ? (InputStream) PostEntitys.get("upFile") : null;
	            if(upFileStream!=null)
	            {
	            	String FileUniqueId;
	              //获取其他数据 例如 aaa
	              if(PostEntitys.get("name")!=null)
	              {
	                     FileUniqueId = (String) PostEntitys.get("name");
	                     System.out.println("name="+aaa);
	              }else{
	            	  FileUniqueId=UUID.randomUUID().toString().toUpperCase();
	              }
	            	
	            	
	            	String FileUniName=FileUniqueId+".pdf";
	            	String AppPath    =request.getRealPath("/");
	            	String DocPath    =AppPath+"document"+File.separator;
	            	File   file       =new File(DocPath);
	            	if(!file.exists())
	            		file.mkdirs();
	            	String FilePath   =DocPath+FileUniName;
	            	System.out.println(FilePath);
	   
                    OutputStream os = new FileOutputStream(new File(FilePath));
	                byte buffer[] = new byte[4096];
	                int len = 0;
	                while ((len = upFileStream.read(buffer)) != -1) {
	                    os.write(buffer, 0, len);
	                }
	                os.flush();
	                os.close();
	                
	                //short  ret=PDFTools.TZVerifySignatures(FilePath);
	                //String strInfo=String.format("%s,%s","上传成功;",ret==0?"不存在签章":ret==1?"验证通过":"验证失败");
	            	//response.getWriter().print(strInfo);
	            	response.getWriter().print("上传成功");
	            }
	            else
	            	response.getWriter().print("上传失败 ");
	            	
	        } catch (Exception Err) {
	            Err.printStackTrace();
	        }
%>