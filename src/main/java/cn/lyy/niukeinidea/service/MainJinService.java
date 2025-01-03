package cn.lyy.niukeinidea.service;

import cn.lyy.niukeinidea.model.request.MianJinRequest;

import java.io.IOException;

public interface MainJinService {
    String getMainJin(MianJinRequest request) throws IOException, InterruptedException;


}
