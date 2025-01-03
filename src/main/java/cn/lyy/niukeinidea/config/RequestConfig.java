package cn.lyy.niukeinidea.config;

import lombok.Data;
import lombok.Getter;

/**
 * 请求配置类
 */
@Getter
public class RequestConfig {
    /**
     * 请求头
     */
    private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

    /**
     * cookie
     */
    private final String cookie = "NOWCODERCLINETID=3FE5F811F38B595AB6650933C74C17F6; NOWCODERUID=f2c70dbafae847f0ab875183df6113e9; acw_tc=7b1aa61089c4e7f9ee94eab93fb5370909e61942e2df11788eff3e6c4ca7f58f";

    /**
     * contentType
     */
    private final String contentType = "application/json";

    /**
     * userAgent
     */
    private final String accept = "application/json, text/plain, */*";

    /**
     * Cache Control
     */
    private final String cacheControl = "no-cache";
}
