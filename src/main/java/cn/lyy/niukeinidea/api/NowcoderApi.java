package cn.lyy.niukeinidea.api;

import cn.lyy.niukeinidea.model.request.MianJinRequest;
import cn.lyy.niukeinidea.service.MainJinService;
import cn.lyy.niukeinidea.service.impl.MainJinServiceImpl;

import java.io.IOException;

/**
 * 接口层
 */
public class NowcoderApi {
    private final MainJinService mainJinService = new MainJinServiceImpl();
    public String getMainJin(MianJinRequest request) throws IOException, InterruptedException {
        return mainJinService.getMainJin(request);
    }
}
