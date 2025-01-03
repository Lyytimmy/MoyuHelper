package cn.lyy.niukeinidea.enums;

import lombok.Getter;

@Getter
public enum RequestUrl {
    MIANJIN("https://gw-c.nowcoder.com/api/sparta/job-experience/experience/job/list?_=", "请求面经，还需要接一个时间戳"),
    ;
    private final String url;
    private final String description;
    RequestUrl(String url, String description){
        this.url = url;
        this.description = description;
    }
}
