package zhishusz.housepresell.util.fileupload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;

/*
* 主要功能：支持单文件上传的类
* 
*/
public class SingleFileUpload extends FileUploadBase {
    
	private static SingleFileUpload sf = null;
	private FileItem fileItem;
    
    private SingleFileUpload(){
    	super();
    }
    
    public static SingleFileUpload getInstance(){
    	sf = new SingleFileUpload();
    	return sf;
    }
    /**
     * @param request
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
	public void parseRequest(HttpServletRequest request)
            throws UnsupportedEncodingException{

        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(sizeThreshold);
        
        if (repository != null) factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(encoding);
        
        try{
            List items = upload.parseRequest(request);

            for(int i=0;i<items.size();i++){
            	FileItem item = (FileItem)items.get(i);
            	
                if (item.isFormField()){
                    String fieldName = item.getFieldName();
                    String value = item.getString(encoding);
                    parameters.put(fieldName, value);
                } else {
                    if (!super.isValidFile(item)){
                        continue;
                    }
                    if (fileItem == null)
                        fileItem = item;
                }
            }
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    /**
     * 保存上传的文件到指定路径
     * @param request
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
	public void saveUploadFile(HttpServletRequest request,String savePath)
            throws UnsupportedEncodingException{

        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(sizeThreshold);
        if (repository != null)
            factory.setRepository(repository);

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(encoding);

        try{
            List items = upload.parseRequest(request);

            for(int i=0;i<items.size();i++){
            	FileItem item = (FileItem)items.get(i);
            	
                if (item.isFormField()){
                    String fieldName = item.getFieldName();
                    String value = item.getString(encoding);
                    parameters.put(fieldName, value);
                } else {
                    if (!super.isValidFile(item)){
                        continue;
                    }
                    if (fileItem == null)
                        fileItem = item;
                    try {
                    	String fileName = fileItem.getName();
                    	if (fileName != null) {                       //获取上传的文件名，不管包不包含上传的文件路径
							fileName = FilenameUtils.getName(fileName);
						}
						BufferedInputStream bis = new BufferedInputStream(fileItem.getInputStream());
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(savePath+fileName)));
						Streams.copy(bis, bos, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }
    /** 
     * 上传文件, 调用该方法之前必须先调用 parseRequest(HttpServletRequest request)
     * @param fileName 完整文件路径
     * @throws Exception
     * @return
     */
    public File upload(String fileName) throws Exception{
        File file = new File(fileName);
        uploadFile(file);
        return file;
    }

    /**
     * 上传文件, 调用该方法之前必须先调用 parseRequest(HttpServletRequest request)
     * @param parent 存储的目录
     * @throws Exception
     */
    public File upload(File parent) throws Exception{
        if (fileItem == null)  return null;
        
        String fileName = fileItem.getName();

    	if (fileName != null) {              	//获取上传的文件名，不管包不包含上传的文件路径
			fileName = FilenameUtils.getName(fileName);
		}
    	
        File file = new File(parent, fileName);
        uploadFile(file);
        return file;
    }
    
    private void uploadFile(File file) throws Exception {
        if (fileItem == null)  return;
        
        long fileSize = fileItem.getSize();
        if (sizeMax > -1 && fileSize > super.sizeMax){
        	//上传文件的大小为 超过服务器最大限制大小 
            String message = "The size of file being uploaded cant't over "+sizeMax;//String.format("上传文件的大小为 (%1$s) 超过服务器最大限制大小 (%2$s)", fileSize, super.sizeMax);
            
            throw new org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException(message, fileSize, super.sizeMax);
        }
        
        try {
        	fileItem.write(file);
		} catch (Exception e) {
			e.printStackTrace();
			file.getParentFile().mkdirs();
			fileItem.write(file);
		}
    }
    
    /**
     * 获取文件信息
     * 必须先调用 parseRequest(HttpServletRequest request)
     * @return
     */
    public FileItem getFileItem(){
        return fileItem;
    }
    /**
     * 获取文件的字节数组
     * 必须先调用 parseRequest(HttpServletRequest request)
     * @return
     */
    public byte[] getFileItemBytes(){
    	try {
			InputStream is = getFileItemStream();
			if (is != null) {
				int length = is.available();
    			byte[] bytes = new byte[length];
    			is.read(bytes);
    			is.close();
    			return bytes;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 获取上传的文件流
     * 必须先调用 parseRequest(HttpServletRequest request)
     * @return
     */
    public InputStream getFileItemStream(){
    	try {
    		if (fileItem!=null) {
    			return fileItem.getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    /**
     * 清空缓存
     */
    public void clearData(){
    	parameters.clear();
    	parameters = null;
    	fileItem = null;
    }   
}

