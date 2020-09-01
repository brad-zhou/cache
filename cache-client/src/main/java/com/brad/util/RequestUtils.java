package com.brad.util;

import com.brad.constants.Constants;
import com.brad.utils.string.StringUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求Request处理操作类
 */
public class RequestUtils {

    public static final String AUTHORIZATION = "Authorization";

    /**
     * 获取request
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 获取消息头中的锐信号信息
     *
     * @return
     */
    public static String getTenantId(boolean saasEnable) {
        if (!saasEnable) {
            return "";
        }
        String tenantId = getRequest().getHeader(Constants.TENANT_ID);
        if (StringUtil.isNullOrEmpty(tenantId)) {
            throw new RuntimeException("多租户下，无法从请求中获取租户标识，拒绝访问。");
        }
        return tenantId;
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getRequest().getHeader(AUTHORIZATION);
    }

}
