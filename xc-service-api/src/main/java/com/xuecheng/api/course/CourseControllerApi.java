package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Api(value="cms课程管理接口",description = "cms课程管理接口，提供数据模型的管理、查询接口")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    //查询课程列表
    @ApiOperation("查询我的课程列表")
    QueryResponseResult findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest,
            HttpServletRequest request
    );

    @ApiOperation("获取课程基础信息")
    CourseBase getCourseBaseById(String courseId) throws RuntimeException;

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("新增课程基础信息")
    AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("获取课程图片")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView courseview(String id);

    @ApiOperation("预览课程")
    CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    CoursePublishResult publish(@PathVariable String id);

    @ApiOperation("保存媒资信息")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);

}
