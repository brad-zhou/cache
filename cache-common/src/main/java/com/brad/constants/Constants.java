package com.brad.constants;

/**
 * 常量
 */
public interface Constants {
    /**
     * 默认的过期时间  单位 秒 s
     */
    int DEFAULT_EXPIRETIME = 1800;
    /**
     * 时间换算单位
     */
    int TIMESCALE = 1000;
    /**
     * 消息头中多租户标志
     */
    String TENANT_ID = "tenantId";
    /**
     * 空值的失效时间
     * 单位 s
     */
    int NULL_EXPIRETIME = 2 * 60;
    /**
     * 通配符字符常量
     */
    String WILDCARD_SEPERATOR = "*";
}
