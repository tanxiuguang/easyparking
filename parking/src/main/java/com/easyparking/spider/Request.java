package com.easyparking.spider;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tanxiuguang create on Mar 15, 2012
 */
public class Request {
	
	private static Log logger = LogFactory.getLog(Request.class);

	public static String requestGet(String url) {
		return requestGet(url, 1000);
	}


	public static String requestGet(String url, int timeout) {
		HttpClient client = new HttpClient();
		String result = null;
		GetMethod method = new GetMethod(url);

		// method.setRequestHeader("Accept","*/*");
		// method.setRequestHeader("Connection","close");
		// method.setRequestHeader("Accept-Language","zh-cn");
		// method.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// method.setRequestHeader("Cache-Control","no-cache");
		method.getParams().setSoTimeout(timeout);
		// method.setFollowRedirects(true);
		// method.setRequestHeader("Cookie",cookie);

		// method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.debug("请求url失败:" + url);
			logger.error(e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
		return result;
	}

}
