package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wuangjing
 * @create 2020/11/11-10:17
 * @Description:
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    //删除成功返回1否则返回0
    int deleteByCourseid(String courseId);
}
