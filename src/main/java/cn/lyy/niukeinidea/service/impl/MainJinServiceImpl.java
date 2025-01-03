package cn.lyy.niukeinidea.service.impl;

import cn.lyy.niukeinidea.enums.RequestUrl;
import cn.lyy.niukeinidea.model.MianJinModel;


import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import cn.lyy.niukeinidea.model.request.MianJinRequest;
import cn.lyy.niukeinidea.service.MainJinService;
import cn.lyy.niukeinidea.utils.MarkdownUtil;
import cn.lyy.niukeinidea.utils.WebUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



public final class MainJinServiceImpl implements MainJinService {
    private final WebUtil webUtil = new WebUtil();
    private final RequestUrl requestUrl = RequestUrl.MIANJIN;

    @Override
    public String getMainJin(MianJinRequest request) throws IOException, InterruptedException {
        // 请求数据
        ArrayList<MianJinModel> list = getData(request);
        // 转换markdown格式
        return MarkdownUtil.MainJinMarkdown(list);
    }

    private ArrayList<MianJinModel> getData(MianJinRequest request) throws IOException, InterruptedException {
        // 获取当前时间的时间戳
        long nowTime = Instant.now().toEpochMilli();
        String url = requestUrl.getUrl() + nowTime;

        // 请求体
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("companyList", request.getCompanyList());
        jsonBody.put("jobId", request.getJobType());
        jsonBody.put("level", 2);
        jsonBody.put("order", request.getOrder());
        jsonBody.put("page", request.getPage());
        jsonBody.put("isNewJob", true);

        HttpResponse<String> response = webUtil.sendRequest(url, jsonBody);

        // 处理响应
        ArrayList<MianJinModel> MianJinData = new ArrayList<>();
        if (response.statusCode() == 200) {
            // 解析响应
            JSONObject responseData = JSON.parseObject(response.body());
            JSONArray records = responseData.getJSONObject("data").getJSONArray("records");
            for (int i = 0; i < records.size(); i++) {
                JSONObject record = records.getJSONObject(i);
                MianJinModel model = new MianJinModel();
                JSONObject contentData = record.getJSONObject("contentData");
                if (contentData == null){
                    contentData = record.getJSONObject("momentData");
                }
                JSONObject userBrief = record.getJSONObject("userBrief");
                model.setName(userBrief.getString("nickname"));
                model.setEducationInfo(userBrief.getString("educationInfo"));
                model.setTitle(contentData.getString("title"));
                model.setContent(contentData.getString("content"));
                model.setDate(timeTransform((Long) contentData.get("showTime")));
                model.setContentId(record.getLong("contentId"));
                System.out.println(model);
                MianJinData.add(model);
            }
        }
        return MianJinData;
    }

    private String timeTransform(long time) {
        // 创建一个SimpleDateFormat对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd HH:mm:ss");

        // 创建Date对象
        Date date = new Date(time);

        // 格式化日期
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

}
