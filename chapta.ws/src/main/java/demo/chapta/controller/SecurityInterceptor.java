package demo.chapta.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import demo.chapta.security.CertificateCoder;
import demo.chapta.security.Licence;

public class SecurityInterceptor implements WebRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	private ObjectMapper objMapper = new ObjectMapper();
	private Licence licence;
	private byte[] certificate;
	
	public void setCertificatePath(String certPath) throws IOException{ 
		
		FileInputStream fis = new FileInputStream(new File(certPath));
		certificate = new byte[fis.available()];//新建一个字节数组
		fis.read(certificate);//将文件中的内容读取到字节数组中
		fis.close();
	}
	
	public void setLicencePath(String licencePath) throws Exception {
		
		FileInputStream fis = new FileInputStream(new File(licencePath));
		byte[] content=new byte[fis.available()];//新建一个字节数组
		fis.read(content);//将文件中的内容读取到字节数组中
		fis.close();
		this.licence = this.objMapper.readValue(content, Licence.class);
	}
	
	@Override
	public void preHandle(WebRequest request) throws Exception {

		logger.debug("preHandle");
		byte[] period = null;
		try{
			period = this.objMapper.writeValueAsBytes(licence.getPeriod());
		}catch(Exception ex){
			logger.debug("拒绝服务：Licence文件格式错误！");
			throw new Exception("拒绝服务：Licence文件格式错误！");
		}
		if(!CertificateCoder.verify(period, licence.getSignature(), this.certificate)){
			logger.error("拒绝服务：没有有效Licence！");
			throw new Exception("拒绝服务：没有有效Licence！");
		}
		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(now.before(licence.getPeriod().getFrom()) || now.after(licence.getPeriod().getTo())){
			String error = String.format("拒绝服务：不在有效期，当前日期:%s. {From:%s - To:%s}", format.format(now), 
					format.format(licence.getPeriod().getFrom()), format.format(licence.getPeriod().getTo()));
			logger.error(error);
			throw new Exception(error);
		}
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		logger.debug("postHandle");
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		logger.debug("afterCompletion");
	}
}
