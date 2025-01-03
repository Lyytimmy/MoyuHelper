package cn.lyy.niukeinidea.model;

import lombok.Data;

import java.util.Date;

/**
 * 牛客面经实体类
 */
@Data
public class MianJinModel {
    private Long contentId;
    private String name;
    private String date;
    private String title;
    private String content;
    private String educationInfo;
}
