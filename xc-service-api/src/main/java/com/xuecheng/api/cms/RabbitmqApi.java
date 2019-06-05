package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Rabbitmq管理接口",description = "Rabbitmq信息发布")
public interface RabbitmqApi {

    @ApiOperation("发布页面")
    ResponseResult post(String pageId);

}
