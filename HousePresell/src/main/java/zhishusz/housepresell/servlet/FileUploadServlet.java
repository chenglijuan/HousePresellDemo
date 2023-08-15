package zhishusz.housepresell.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.fileupload.FileUploadUtil;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 4441194537928346767L;

	private String fileSizeLimit;

	public void init(ServletConfig config) throws ServletException {
		this.fileSizeLimit = config.getInitParameter("fileSizeLimit");
	}

	public void destroy() {
		this.fileSizeLimit = null;
		super.destroy();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int maxPostSize = MyInteger.getInstance().parse(this.fileSizeLimit);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("p1", request);
		paramMap.put("p2", maxPostSize);
		
		FileUploadUtil uploadFileUtil = new FileUploadUtil();
		Properties properties = uploadFileUtil.execute(paramMap);
		if(S_NormalFlag.success.equals(properties.get(S_NormalFlag.result)))
		{
			String filePath = properties.get("filePath").toString();
			String fileFullPath = properties.get("fileFullPath").toString();
			String fileNewName = properties.get("fileNewName").toString();
			String newFileNetworkPath = properties.get("newFileNetworkPath").toString();
			String data = "{\"result\":\"success\",\"fileFullPath\":\""+fileFullPath+"\",\"fileNewName\":\""+fileNewName+"\",\"newFileNetworkPath\":\""+newFileNetworkPath+"\",\"filePath\":\""+filePath+"\",\"imgFileExt\":\""+filePath.subSequence(filePath.indexOf("."), filePath.length())+"\"}";
			writeOut(response, data);
		}
		else
		{
			writeOut(response,"{\"result\":\"fail\",\"info\":\""+ properties.get(S_NormalFlag.info)+"\"}");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public String getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(String fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}
	
	protected void writeOut(HttpServletResponse response, String msg) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			out.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}

