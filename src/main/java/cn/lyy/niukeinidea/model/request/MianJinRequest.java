package cn.lyy.niukeinidea.model.request;

import lombok.Data;

import java.util.List;

/**
 * 牛客面经请求实体类
 */
@Data
public class MianJinRequest {
    /**
     * 公司列表
     */
    List<Integer> companyList;
    /**
     * 岗位类型,默认后端开发
     */
    Integer jobType = 11200;
    /**
     * 页数
     * 默认 = 1
     */
    Integer page = 1;
    /**
     * 排序方式
     * 1 - 综合
     * 3 - 最新
     * 默认最新
     */
    Integer order = 3;

    /**
     *
     * 暂不清楚 默认true
     */
    Boolean isNewJob = true;

}
