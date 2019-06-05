package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaFileControllerApi;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {
    @Autowired
    MediaFileService mediaFileService;
    @Autowired
    MediaUploadService mediaUploadService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size")
            int size, QueryMediaFileRequest queryMediaFileRequest) {
        //媒资文件查询
        return mediaFileService.findList(page,size,queryMediaFileRequest);
    }

    @Override
    @PostMapping("/process/{id}")
    public ResponseResult process(@PathVariable("id")String mediaId) {
        return mediaUploadService.sendProcessVideoMsg(mediaId);
    }

}
