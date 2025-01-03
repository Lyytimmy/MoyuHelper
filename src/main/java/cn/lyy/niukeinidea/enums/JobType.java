package cn.lyy.niukeinidea.enums;

import lombok.Getter;

/**
 * 牛客岗位枚举类（不全）
 */
@Getter
public enum JobType {
    BACKEND(11200,"软件开发/后端开发"),
    FRONTEND(11201,"软件开发/前端开发"),
    SERVER(11202,"软件开发/客户端开发"),
    ;
    private final Integer jobId;
    private final String jobName;
    JobType(Integer jobId, String jobName){
        this.jobId = jobId;
        this.jobName = jobName;
    }
    public static Integer getJobId(String jobName){
        for(JobType jobType : JobType.values()){
            if(jobType.getJobName().equals(jobName)){
                return jobType.getJobId();
            }
        }
        return null;
    }
}
