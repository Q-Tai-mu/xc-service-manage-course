package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CourseMarketCode;
import com.xuecheng.framework.domain.course.response.CoursePicCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CourseMarketRepository marketRepository;

    @Autowired
    private CmsPageClient cmsPageClient;
    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_pagePhysicalPath;
    @Value("${course-publish.pageWebPath}")
    private String publish_pageWebPath;
    @Value("${course-publish.previewUrl}")
    private String publish_previewUrl;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.siteId}")
    private String publish_siteid;

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
     *
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult updateTeachplan(Teachplan teachplan) {
        //校验课程id和课程名称
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_ARAM);
        }
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    public ResponseResult coursebaseAdd(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 查询课程基本信息
     *
     * @param page 第几页
     * @param size 多少条
     * @param map  查询条件
     * @return CourseInfo 集合
     */
    public QueryResponseResult findCourseInfo(Integer page, Integer size, Map<String, Object> map) {
        //先不管条件参数，因为不确定条件参数是什么类型
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 7;
        }
        page = page - 1;
        //准备返回集合对象
        List<CourseInfo> courseInfos = new ArrayList<>();
        //分页查询，分页对象 ,得到每个课程
        List<CourseBase> content = courseBaseRepository.findAll(PageRequest.of(page, size)).getContent();
        //遍历每个课程对象，根据课程的id查询课程所对应的图片
        for (CourseBase base : content) {
            //课程图片对象
            Optional<CoursePic> coursePic = coursePicRepository.findById(base.getId());
            CoursePic ic = null;
            //图片对象不存在，则添加图片对象
            if (!coursePic.isPresent()) {
                ic = new CoursePic();
                ic.setCourseid(base.getId());
                ic.setPic("");
                //添加一个课程图片对象
                coursePicRepository.save(ic);

            } else {
                ic = coursePic.get();
            }

            //取出课程图片对象
            CoursePic pic = ic;
            //准备封装对象
            CourseInfo courseInfo = new CourseInfo();
            //拷贝图片对象
            BeanUtils.copyProperties(pic, courseInfo);
            //剩余的值都进行拷贝
            BeanUtils.copyProperties(base, courseInfo);
            //添加到courseInfos
            courseInfos.add(courseInfo);
        }
        //封装结果集对象
        QueryResult<CourseInfo> result = new QueryResult<>();
        //封装数据
        result.setList(courseInfos);
        //封装多少条记录
        result.setTotal(courseInfos.size());
        //返回结果集
        return new QueryResponseResult(CommonCode.SUCCESS, result);
    }

    /**
     * 获取课程基本信息
     *
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    public CourseBase getCourseBaseById(String courseId) throws RuntimeException {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_NOULL_OBJECT);
        }
        return optional.get();
    }

    /**
     * 更新基本课程信息
     *
     * @param courseId
     * @param courseBase
     * @return
     * @throws RuntimeException
     */
    @Transactional
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) throws RuntimeException {
        //参数id判断
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //先根据id查询对象
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        //当对象不存在
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_NOULL_OBJECT);
        }
        //取出对象
        CourseBase base = optional.get();
        //拷贝对象
        BeanUtils.copyProperties(courseBase, base);
        courseBaseRepository.save(base);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据courseId查询图片对象
     *
     * @param courseId
     * @return
     * @throws RuntimeException
     */
    public CoursePic findCoursePicImage(String courseId) throws RuntimeException {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CoursePicCode.COURSE_NOULL_OBJECT);
        }
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CoursePicCode.COURSE_NOULL_OBJECT);
        }
        return optional.get();
    }

    /**
     * 根据courseId查询课程营销信息
     *
     * @param courseId
     * @return
     */
    public CourseMarket getCourseMarketById(String courseId) {
        if (StringUtils.isEmpty(courseId))
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        Optional<CourseMarket> optional = marketRepository.findById(courseId);
        if (!optional.isPresent())
            ExceptionCast.cast(CourseMarketCode.COURSE_NOULL_OBJECT);
        return optional.get();
    }

    /**
     * 更新课程营销信息
     *
     * @param id
     * @param market
     * @return
     */
    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket market) {
        if (StringUtils.isEmpty(id))
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        Optional<CourseMarket> optional = marketRepository.findById(id);
        if (!optional.isPresent())
            marketRepository.save(market);
        else
            market.setId(id);
        marketRepository.save(market);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Transactional
    public ResponseResult addCoursePicImage(String courseId, String pic) {
        if (StringUtils.isEmpty(courseId)) {
            //抛出课程id，为空
            ExceptionCast.cast(CoursePicCode.COURSE_ID_NIULL);
        }
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = new CoursePic();
        if (optional.isPresent()) {
            //有就修改
            BeanUtils.copyProperties(optional.get(), coursePic);
            coursePic.setPic(pic);
        } else {
            //没有则保存
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
        }
        //根据上面的条件自动进行修改或者保存
        coursePicRepository.save(coursePic);

        //返回
        return new ResponseResult(CommonCode.SUCCESS);
    }


    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePicImage(String courseId) {
        //判断课程id是否为空
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CoursePicCode.COURSE_ID_NIULL);
        }
        //根据课程id查询coursePic表中的bean对象
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_NOULL_OBJECT);
        }
        //删除coursePic表中的bean的对象，成功返回1，失败返回0
        int result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //课程视图查询 查询范围包括，课程基本信息，图片，课程营销，课程计划
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (optionalCourseBase.isPresent()) {
            CourseBase courseBase = optionalCourseBase.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程营销信息
        Optional<CourseMarket> optionalCourseMarket = marketRepository.findById(id);
        if (optionalCourseMarket.isPresent()) {
            CourseMarket courseMarket = optionalCourseMarket.get();
            courseView.setCourseMarket(courseMarket);
        }
        //查询课程图片信息
        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        if (optionalCoursePic.isPresent()) {
            CoursePic coursePic = optionalCoursePic.get();
            courseView.setCoursePic(coursePic);
        }
        //查询课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //课程预览
    public CoursePublishResult preview(String id) {
//        //根据课程id查询课程
//        CourseBase courseBase = this.findCourseBaseById(id);
//        //发布课程预览页面
//        CmsPage cmsPage = new CmsPage();
//        //站点
//        cmsPage.setSiteId(publish_siteid);
//        //模板
//        cmsPage.setTemplateId(publish_templateId);
//        //页面名称
//        cmsPage.setPageName(id + ".html");
//        //页面别名
//        cmsPage.setPageAliase(courseBase.getName());
//        //页面访问路径
//        cmsPage.setPageWebPath(publish_pageWebPath);
//        //页面存储路径
//        cmsPage.setPagePhysicalPath(publish_pagePhysicalPath);
//        //数据url信息
//        cmsPage.setDataUrl(publish_dataUrlPre + id);
        CmsPage cmsPage = beanCmsBaseAndPage(id);
        //远程请求cms保存页面
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
        String pageUrl = publish_previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //根基课程id查询课程基本信息
    private CourseBase findCourseBaseById(String id) {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
        if (!optionalCourseBase.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_NOULL_OBJECT);
        }
        return optionalCourseBase.get();
    }

    //根据课程id封装一份cmsPage Bean实例
    public CmsPage beanCmsBaseAndPage(String courseId) {
        //查询课程
        CourseBase courseBase = findCourseBaseById(courseId);
        //准备发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteid);
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(courseBase.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_pageWebPath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_pagePhysicalPath);
        //数据url信息
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        return cmsPage;
    }

    //一键发布
    @Transactional
    public CoursePublishResult publish(String courseId) {
        //通过beanCmsBaseAndPag方法得到一个封装好的CmsPage
        CmsPage cmsPage = beanCmsBaseAndPage(courseId);
        //调用cms一键发布页面接口，将页面信息发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //更改课程的发布状态为：“发布”
        CourseBase courseBase = saveCoursePubState(courseId);
        if (courseBase == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //页面预览url
        String pageUrl = cmsPostPageResult.getPostPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    //更改课程状态 编号202002
    private CourseBase saveCoursePubState(String courseId) {
        CourseBase courseBase = findCourseBaseById(courseId);
        //更新状态
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        return courseBase;
    }


}
