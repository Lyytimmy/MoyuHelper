package cn.lyy.niukeinidea.enums;

import lombok.Getter;

/**
 * 牛客公司编号枚举
 */
@Getter
public enum Company
{
    ALIBABA(134,"阿里巴巴"),
    TENCENT(138,"腾讯"),
    BAIDU(139,"百度"),
    XIAOMI(147,"小米集团"),
    NETEASE(149,"网易"),
    JD(151,"京东"),
    MEITUAN(179,"美团"),
    HUAWEI(239,"华为"),
    DIODI(652,"滴滴"),
    BYTEDANCE(665,"字节跳动");
    private final Integer id;
    private final String name;
    Company(Integer id, String name){
        this.id = id;
        this.name = name;
    }
    public static Integer getId(String name){
        for(Company company : Company.values()){
            if (company.getName().equals(name)){
                return company.getId();
            }
        }
        return null;
    }

}
