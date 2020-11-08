package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author wuangjing
 * @create 2020/11/6-19:09
 * @Description:
 */
@Service
public class CourseService {

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        if (teachplanNode == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return teachplanNode;
    }

    /**
     * 获取课程根节点，如果没有根节点者添加
     *
     * @param courseId
     * @return
     */
    public String getTeachplanRoot(String courseId) {
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            //返回警告 得不到最根的courseBase所对应的courseID的数据模型(有课程，但是没有课程计划情况)
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //取出课程最根model
        CourseBase courseBase = optional.get();
        //取出课程计划根id
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        //没有根节点
        if (teachplanList == null || teachplanList.size() == 0) {
            //新增一个根节点 往teachplan表中
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");//1级
            teachplanRoot.setStatus("0");//未发布
            //保存
            teachplanRepository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        //有根节点
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

    /**
     * 添加课程计划
     * 使用spring的事务管理注解
     *
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult addTeachplanList(Teachplan teachplan) {
        //校验课程id和课程名称
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_ARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出页面传入的父节点id
        String parentid = teachplan.getParentid();

        //判断父节点是否为空
        if (StringUtils.isEmpty(parentid)) {
            //为空
            //取得根节点
            parentid = getTeachplanRoot(courseid);
        }
        //取得父节点信息
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(parentid);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_ARAM);
        }
        //得到父节点级别
        String NodeGrade = teachplanOptional.get().getGrade();
        //拷贝teachplan信息到新节点teachplanNew
        Teachplan teachplanNew = new Teachplan();

        BeanUtils.copyProperties(teachplan, teachplanNew);

        teachplanNew.setCourseid(courseid);
        teachplanNew.setParentid(parentid);
        //通过判断父节点级别来设置子节点级别
        if (NodeGrade.equals("1")) {
            teachplanNew.setGrade("2");
        } else if (NodeGrade.equals("2")) {
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 修改课程计划
     * @param teachplan
     * @return
     */
    public ResponseResult updateTeachplan(Teachplan teachplan){
        //校验课程id和课程名称
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_ARAM);
        }
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
