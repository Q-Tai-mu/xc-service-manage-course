package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
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


}
