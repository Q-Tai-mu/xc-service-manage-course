package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wuangjing
 * @create 2020/11/7-8:47
 * @Description:
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    //定义方法根据课程id和父节点id可以查询出节点列表,此方法可以作为查询根节点
    public List<Teachplan> findByCourseidAndParentid(String courseid, String parentid);
}
