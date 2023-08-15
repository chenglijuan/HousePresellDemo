package zhishusz.housepresell.util.fileupload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.Iterator;
import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.OSSClientProperty;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesRequest;
import com.xiaominfo.oss.sdk.upload.IUpload;

public class BytesUploadHandle implements IUpload
{
	public OSSClientProperty ossClientProperty;

	public BytesUploadHandle(OSSClientProperty ossClientProperty) {
		this.ossClientProperty = ossClientProperty;
	}

	@Override
	public ReceiveMessage upload(File file)
	{
		ReceiveMessage ossClientMessage = new ReceiveMessage();
		CloseableHttpResponse closeableHttpResponse = null;
		CloseableHttpClient httpClient = null;

		try {
			String originalName = file.getName();
			String mediaType = "unkown";
			int idx = originalName.lastIndexOf(".");
			if (idx > 0) {
				mediaType = originalName.substring(idx + 1);
			}

			String filebyteString = "";//Base64.encodeBase64String(FileUtils.readFileToByteArray(file));
			FileBytesRequest fileBytesRequest = new FileBytesRequest();
			fileBytesRequest.setFile(filebyteString);
			fileBytesRequest.setMediaType(mediaType);
			fileBytesRequest.setOriginalName(originalName);
			fileBytesRequest.setExtra(this.ossClientProperty.getExtra());
			HttpPost request = new HttpPost(this.ossClientProperty.getRemote());
			this.addDefaultHeader(request);
			httpClient = HttpClients.createDefault();
			List<FileBytesRequest> fileBytesRequests = new ArrayList();
			fileBytesRequests.add(fileBytesRequest);
			this.addRequestParam(request, fileBytesRequests, this.ossClientProperty.getProject());
			System.out.println("request is " + request.toString() + " ");
			closeableHttpResponse = httpClient.execute(request);
			if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
				String content = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
				System.out.println("success");
				System.out.println("content is " + content);
				if (content != null && !"".equals(content)) {
					Gson gson = new Gson();
					ossClientMessage = (ReceiveMessage) gson.fromJson(content, ReceiveMessage.class);
				}
			}
		} catch (Exception var14) {
			System.out.println("in exception");
			this.handleServerExceptionMessage(ossClientMessage, var14);
		}

		return ossClientMessage;
	}
	
	/**
	 * 设置默认请求头
	 * @param request
	 */
	private void addDefaultHeader(HttpUriRequest request) {
		request.addHeader("Content-Encoding", "gzip");
		request.addHeader("Content-type", "application/json");
	}
	
	private void handleServerExceptionMessage(ReceiveMessage ossClientMessage, Exception e) {
		ossClientMessage.setCode("8500");
		ossClientMessage.setMessage(e.getMessage());
	}

	private void addRequestParam(HttpPost request, List<FileBytesRequest> fileBytesRequests, String project) {
		JSONObject param = this.createRequestParams(fileBytesRequests, this.ossClientProperty.getProject());
		request.setEntity(new StringEntity(param.toString(), "UTF-8"));
	}
	
	private JSONObject createRequestParams(List<FileBytesRequest> fileBytesRequests, String project) {
		JSONObject param = new JSONObject();
		param.put("project", project);
		param.put("appid", this.ossClientProperty.getAppid());
		param.put("appsecret", this.ossClientProperty.getAppsecret());
		param.put("extra", this.ossClientProperty.getExtra());
		param.put("files", this.createFileArrs(fileBytesRequests));
		return param;
	}
	
	private JSONArray createFileArrs(List<FileBytesRequest> fileBytesRequests) {
		JSONArray jsonArray = new JSONArray();
		Iterator var3 = fileBytesRequests.iterator();

		while (var3.hasNext()) {
			FileBytesRequest fileBytesRequest = (FileBytesRequest) var3.next();
			JSONObject fileObj = new JSONObject();
			fileObj.put("media_type", fileBytesRequest.getMediaType());
			fileObj.put("file", fileBytesRequest.getFile());
			fileObj.put("original_name", fileBytesRequest.getOriginalName());
			jsonArray.add(fileObj);
		}

		return jsonArray;
	}
}
