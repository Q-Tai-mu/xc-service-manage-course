package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wuangjing
 * @create 2020/11/6-19:12
 * @Description:
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;


    /**
     * 查询课程计划
     *
     * @param courseId
     * @return
     */

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplanList(teachplan);
    }

    /**
     * 修改课程计划
     *
     * @param teachplan
     * @return
     */
    @Override
    @PutMapping("/teachplan/update")
    public ResponseResult updateTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.updateTeachplan(teachplan);
    }

    /**
     * 添加课程基本信息
     *
     * @param courseBase
     * @return
     */
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult courseBaseAdd(@RequestBody CourseBase courseBase) {
        return courseService.coursebaseAdd(courseBase);
    }
    //coursebase/list/1/7?

    /**
     * 查询课程基本信息
     *
     * @param page 页码
     * @param size 条数
     * @param map  条件
     * @return
     */
    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseInfo(@PathVariable("page") Integer page, @PathVariable("size") Integer size, Map<String, Object> map) {
        return courseService.findCourseInfo(page, size, map);
    }

    /**
     * 获取课程基本信息
     *
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    @Override
    @GetMapping("/courseview/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseService.getCourseBaseById(courseId);
    }

    /**
     * 修改课程基本信息
     *
     * @param courseId
     * @param courseBase
     * @return
     * @throws RuntimeException
     */
    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String courseId, @RequestBody CourseBase courseBase) throws RuntimeException {
        return courseService.updateCourseBase(courseId, courseBase);
    }

    /**
     * 查询课程图片
     *
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    @Override
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePicImage(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseService.findCoursePicImage(courseId);
    }

    /**
     * 添加课程图片(文件系统中已经存在相关图片，这里只需要绑定信息即可)
     *
     * @param courseId
     * @param pic
     * @return
     */
    @Override
    @PostMapping("coursepic/add")
    public ResponseResult addCoursePicImage(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.addCoursePicImage(courseId, pic);
    }

    /**
     * 删除课程图片（文件系统中的文件没权删除。因为其他微服务可能需要使用）
     *
     * @param courseId
     * @return
     */
    @Override
    @DeleteMapping("coursepic/delete")
    public ResponseResult deleteCoursePicImage(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePicImage(courseId);
    }

    /**
     * 获取课程营销信息
     *
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/coursemarket/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    /**
     * 需改课程营销信息
     *
     * @param courseId
     * @param courseMarket
     * @return
     */
    @Override
    @PutMapping("/coursemarket/update/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId, courseMarket);
    }

    /**
     * 课程视图查询（包含以下4点数据）
     * 课程基本信息
     * 课程营销
     * 课程图片
     * 课程计划
     *
     * @param id
     * @return 返回的是课程详情页所需数据
     */
    @Override
    @GetMapping("/coursevieew/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    /**
     * 课程预览
     * 前端最终得到的url是由CMS微服务CmsPagePreViewController提供真正的预览
     *
     * @param id 课程id
     * @return 返回一个包含课程预览地址的url（一个配置好的publish_dataUrlPre值+页面id）和操作代码
     */
    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id) {
        return courseService.publish(id);
    }


}
