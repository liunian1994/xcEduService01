package com.xuecheng.manage_cms.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.CourseView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_COURSE)
public interface CourseClient {

    @GetMapping("/course/courseview/{id}")
    CourseView courseview(@PathVariable("id") String id);

}
