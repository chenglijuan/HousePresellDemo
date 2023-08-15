package zhishusz.housepresell.util.fileupload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/*
* 主要功能：文件上传的基类
* 
*/
public abstract class FileUploadBase {
    protected Map<String, String> parameters = new HashMap<String, String>();     // 保存普通form表单域
    
    protected String encoding = "UTF-8";                                          // 字符编码，当读取上传表单的各部分时会用到该encoding

    protected UploadFileFilter filter = null;                                     // 文件过滤器, 默认为NULL 不过滤
    
    protected int sizeThreshold = DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;

    protected long sizeMax = -1;                                                  //无限制 

    protected File repository;
    
    public String getParameter(String key){
        return parameters.get(key);
    }

    public String getEncoding(){
        return encoding;
    }

    public void setEncoding(String encoding){
        this.encoding = encoding;
    }

    /** 
     * 获取上传文件最大的大小，单位为Byte(字节），为-1时表示无限制
     * @return
     */
    public long getSizeMax(){
        return sizeMax;
    }

    /**
     * 设置上传文件最大的大小，单位为Byte(字节），为-1时表示无限制
     * @param sizeMax
     */
    public void setSizeMax(long sizeMax){
        this.sizeMax = sizeMax;
    }

    /** 
     * 获取临时文件（缓冲区）大小
     */
    public int getSizeThreshold(){
        return sizeThreshold;
    }
    
    /** 
     * 设置向硬盘写数据时所用的零时文件（缓冲区）大小
     */
    public void setSizeThreshold(int sizeThreshold){
        this.sizeThreshold = sizeThreshold;
    }

    /** 
     * 获取存放临时文件（缓冲区）的目录
     */
    public File getRepository() {
        return repository;
    }

    /** 
     * 设置存放零时文件（缓冲区）的目录
     */
    public void setRepository(File repository) {
        this.repository = repository;
    }
    
    /** 
     * 获取普通表单域参数列表
     * @return
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * 获取过滤器
     * @return
     */
    public UploadFileFilter getFilter() {
        return filter;
    }

    /** 
     * 设置文件过滤器，不符合过滤器规则的将不被上传
     * @param filter
     */
    public void setFilter(UploadFileFilter filter) {
        this.filter = filter;
    }
    
    /** 
     * 验证文件是否有效
     * @param item
     * @return
     */
    protected boolean isValidFile(FileItem item){
        return !(item == null || item.getName() == "" || item.getSize() == 0 || (filter != null && !filter.accept(item.getName())));
    }
}