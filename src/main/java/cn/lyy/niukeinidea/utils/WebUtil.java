package cn.lyy.niukeinidea.utils;

import cn.lyy.niukeinidea.config.RequestConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * 请求工具类
 */
// TODO 使用OKHttp库
@Data
public class WebUtil {
    /**
     * 连接
     */
    private static final HttpClient client = HttpClient.newHttpClient();
    /**
     * 配置
     */
    private static final RequestConfig config = new RequestConfig();

    /**
     * 返回一个设置好请求头的请求类
     * @return
     */
    public HttpRequest getRequest(String url, JSONObject requestBody) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", config.getContentType())
                .header("User-Agent", config.getUserAgent())
                .header("Cookie", config.getCookie())
                .header("Accept", config.getAccept())
                .header("Cache-Control", config.getCacheControl());

        if (requestBody != null) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString()));
        } else {
            requestBuilder.GET();
        }

        return requestBuilder.build();
    }

    /**
     * 发送 GET 或 POST 请求
     * @param url 请求地址
     * @param requestBody 请求体（如果为 null，则发送 GET 请求）
     * @return 响应的 HttpResponse 对象
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> sendRequest(String url, JSONObject requestBody) throws IOException, InterruptedException {
        HttpRequest request = getRequest(url, requestBody);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
