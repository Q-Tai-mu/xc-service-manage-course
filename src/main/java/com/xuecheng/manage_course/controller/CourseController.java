package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) throws RuntimeException {
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


    @Override
    @PostMapping("coursepic/add")
    public ResponseResult addCoursePicImage(String courseId, String pic) {
        return courseService.addCoursePicImage(courseId,pic);
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


}
