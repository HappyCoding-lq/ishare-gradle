package top.dddpeter.ishare.common.encryption.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.dddpeter.ishare.common.encryption.annotation.*;
import top.dddpeter.ishare.common.encryption.constants.HttpMethodTypePrefixConstant;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApiEncryptDataInit implements ApplicationContextAware {

	/**
	 * 需要对响应内容进行加密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> responseEncryptUriList = new ArrayList<>();
	
	/**
	 * 需要对请求内容进行解密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> requestDecyptUriList = new ArrayList<>();
    
	/**
	 * 忽略加密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> responseEncryptUriIgnoreList = new ArrayList<>();
	
	/**
	 * 忽略对请求内容进行解密的接口URI<br>
	 * 比如：/user/list<br>
	 * 不支持@PathVariable格式的URI
	 */
	public static List<String> requestDecyptUriIgnoreList = new ArrayList<>();
	
	private String contextPath;
	
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
    	this.contextPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
        Map<String, Object> beanMap = ctx.getBeansWithAnnotation(RestController.class);
        initData(beanMap);
        beanMap = ctx.getBeansWithAnnotation(Controller.class);
        initData(beanMap);
    }

	private void initData(Map<String, Object> beanMap) {
		if (beanMap != null) {
            for (Object bean : beanMap.values()) {
                Class<?> clz = bean.getClass();
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                	Encrypt encrypt = AnnotationUtils.findAnnotation(method, Encrypt.class);
                	if (encrypt != null) {
                		// 注解中的URI优先级高
                    	String uri = encrypt.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = getApiUri(clz, method);
						}
                        log.debug("Encrypt URI: {}", uri);
                        responseEncryptUriList.add(uri);
                	}
                	Decrypt decrypt = AnnotationUtils.findAnnotation(method, Decrypt.class);
                    if (decrypt != null) {
                    	String uri = decrypt.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = getApiUri(clz, method);
						}
						log.debug("Decrypt URI: {}", uri);
                        requestDecyptUriList.add(uri);
                    }
					DecryptAndEncrypt decryptAndEncrypt = AnnotationUtils.findAnnotation(method, DecryptAndEncrypt.class);
					if (decryptAndEncrypt != null) {
						String uri = decryptAndEncrypt.value();
						if (!StringUtils.hasText(uri)) {
							uri = getApiUri(clz, method);
						}
						log.debug("DecryptAndEncrypt URI: {}", uri);
						requestDecyptUriList.add(uri);
						responseEncryptUriList.add(uri);
					}
                    EncryptIgnore encryptIgnore = AnnotationUtils.findAnnotation(method, EncryptIgnore.class);
                	if (encryptIgnore != null) {
                		// 注解中的URI优先级高
                    	String uri = encryptIgnore.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = getApiUri(clz, method);
						}
						log.debug("EncryptIgnore URI: {}", uri);
                        responseEncryptUriIgnoreList.add(uri);
                	}
                	DecryptIgnore decryptIgnore = AnnotationUtils.findAnnotation(method, DecryptIgnore.class);
                    if (decryptIgnore != null) {
                    	String uri = decryptIgnore.value();
                    	if (!StringUtils.hasText(uri)) {
                    		uri = getApiUri(clz, method);
						}
						log.debug("DecryptIgnore URI: {}", uri);
                        requestDecyptUriIgnoreList.add(uri);
                    }
					DecryptAndEncryptIgnore decryptAndEncryptIgnore = AnnotationUtils.findAnnotation(method, DecryptAndEncryptIgnore.class);
					if (decryptAndEncryptIgnore != null) {
						// 注解中的URI优先级高
						String uri = decryptAndEncryptIgnore.value();
						if (!StringUtils.hasText(uri)) {
							uri = getApiUri(clz, method);
						}
						log.debug("DecryptAndEncryptIgnore URI: {}", uri);
						requestDecyptUriIgnoreList.add(uri);
						responseEncryptUriIgnoreList.add(uri);
					}
                }
            }
        }
	}
    
    private String getApiUri(Class<?> clz, Method method) {
    	String methodType = "";
        StringBuilder uri = new StringBuilder();
        
        RequestMapping reqMapping = AnnotationUtils.findAnnotation(clz, RequestMapping.class);
        if (reqMapping != null) {
        	uri.append(formatUri(reqMapping.value()[0]));
		}
        
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        
        if (getMapping != null) {
        	methodType = HttpMethodTypePrefixConstant.GET;
            uri.append(formatUri(getMapping.value()[0]));
            
        } else if (postMapping != null) {
        	methodType = HttpMethodTypePrefixConstant.POST;
            uri.append(formatUri(postMapping.value()[0]));
            
        } else if (putMapping != null) {
        	methodType = HttpMethodTypePrefixConstant.PUT;
            uri.append(formatUri(putMapping.value()[0]));
            
        } else if (deleteMapping != null) {
        	methodType = HttpMethodTypePrefixConstant.DELETE;
            uri.append(formatUri(deleteMapping.value()[0]));
            
        } else if (requestMapping != null) {
        	RequestMethod m = requestMapping.method()[0];
        	methodType = m.name().toLowerCase() + ":";
            uri.append(formatUri(requestMapping.value()[0]));
            
        } 
        
        if (StringUtils.hasText(this.contextPath)) {
        	 return methodType + this.contextPath + uri.toString();
		}
        
        return methodType + uri.toString();
    }
    
    private String formatUri(String uri) {
    	if (uri.startsWith("/")) {
			return uri;
		}
    	return "/" + uri;
    }
}
