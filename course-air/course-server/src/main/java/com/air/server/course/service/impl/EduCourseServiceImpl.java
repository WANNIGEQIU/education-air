package com.air.server.course.service.impl;

import com.air.common.constants.CourseConstant;
import com.air.common.enums.ResultEnum;
import com.air.common.exception.MyException;
import com.air.common.util.SnowFlake;
import com.air.common.vo.CourseVo;
import com.air.server.course.entity.*;
import com.air.server.course.entity.dto.*;
import com.air.server.course.exception.CourseException;
import com.air.server.course.mapper.*;
import com.air.server.course.service.EduChapterService;
import com.air.server.course.service.EduCourseDescriptionService;
import com.air.server.course.service.EduCourseService;
import com.air.server.course.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@Service
@Transactional
@Slf4j
public class EduCourseServiceImpl implements EduCourseService {

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduCourseDescriptionService descriptionService;

    @Autowired
    private EduCourseDescriptionMapper descriptionMapper;

    @Autowired
    private EduChapterMapper chapterMapper;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduVideoMapper videoMapper;

    @Autowired
    private EduCategoryMapper categoryMapper;


    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private StringRedisTemplate redisTemplate;








    @Override
    public List<CourseVo> selectCouse8Lid(String id) {
        List<CourseVo> courseVos = courseMapper.selectCourse8Lid(id);
        if (!CollectionUtils.isEmpty(courseVos)) {
            return courseVos;
        }
        return null;
    }

    @Override
    public String saveCouseInfo(CourseInfoDto info) {
        // 添加基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(info, course);
        int insert = courseMapper.insert(course);
        // 基本信息添加成功才能添加描述信息
        if (insert == 0) {
            throw new CourseException(ResultEnum.COURSE_SAVE_ERROR);
        }
        // 添加描述信息

        CourseInfoDto desc = new CourseInfoDto();
        desc.setDescription(info.getDescription());
        desc.setId(course.getId());
        Boolean r = descriptionService.insertCourseDesc(desc);
        return r == true ? desc.getId() : null;

    }

    @Override
    public List a() {
        List<EduCourse> eduCourses = this.courseMapper.selectList(null);
        return eduCourses;
    }

    @Override
    public CourseInfoDto getCourseInfo(String id) {
        CourseInfoDto courseInfoDto = courseMapper.getCourseInfo(id);
        if (courseInfoDto == null) {
            throw new CourseException(ResultEnum.NO_COURSE_INFO);
        }

        return courseInfoDto;
    }

    @Override
    public Boolean uploadCourse(CourseInfoDto info) {

        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(info, course);
        int i = this.courseMapper.updateById(course);
        if (i == 0) {
            throw new CourseException(ResultEnum.FAIL_COURSE_UPLOAD);
        }
        EduCourseDescription description = new EduCourseDescription();
        description.setId(info.getId());
        description.setDescription(info.getDescription());
        Boolean r = descriptionService.uploadDesc(description);
        return r;
    }

    @Override
    public Page selectCondtion(Page p, CourseInfoDto info) {
        Page<CourseCondtionDto> condtion = courseMapper.selectCondtion(p, info);
        if (condtion == null) {
            throw new CourseException(ResultEnum.QUERY_ERROR);
        }

        return condtion;
    }

    @Override
    public Boolean courseDelete(String id) {


        List<EduChapter> chapters = chapterMapper.queryList(id);
        if (!CollectionUtils.isEmpty(chapters)) {
            chapters.forEach(ch -> {
                String chid = ch.getId();
                // 查询章节中所有小节
                List<EduVideo> eduVideos = videoMapper.queryList(chid);
                    eduVideos.forEach(video->{
                        videoService.deleteVideo(video.getId());

                    });

            });
            //删章节
            QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
            wrapper.eq("course_id", id);
            int delete = chapterMapper.delete(wrapper);

            return this.judge(id);

        } else {
            //不存在章节删描述
            return this.judge(id);
        }


    }

    @Override
    public CourseInfoDto queryInfo(String id) {

        if (StringUtils.isEmpty(id)) {
            throw new MyException(ResultEnum.PARAM_ERROR);
        }

        CourseInfoDto queryInfo = courseMapper.queryInfo(id);

        if (queryInfo != null) {
            log.info("查询课程详情成功！{}", id);
            return queryInfo;
        }

        throw new CourseException(ResultEnum.QUERY_ERROR);
    }

    @Override
    public Boolean statePut(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
         eduCourse.setStatus(CourseConstant.COURSE_PUBLISH);

        int i = courseMapper.updateById(eduCourse);
        return i>0;
    }

    @Override
    public Boolean changeStatus(String id, String status) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(status)) {
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        if (CourseConstant.COURSE_PUBLISH.equals(status)) {
            eduCourse.setStatus(CourseConstant.COURSE_LOWER);
        }else {
            eduCourse.setStatus(CourseConstant.COURSE_PUBLISH);
        }
        int i = courseMapper.updateById(eduCourse);
        return i>0;
    }

    // web 获取课程信息
    @Override
    public Page getCourseList(Page<EduCourse> p,CourseVo bean) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("status","Publish");
        wrapper.orderByDesc("edu_create");
        if (!(StringUtils.isEmpty(bean.getCategoryPid()))) {
            wrapper.eq("category_pid",bean.getCategoryPid());
        }
        if (!(StringUtils.isEmpty(bean.getSearch()))){
            wrapper.like("title",bean.getSearch());
        }
        Page<EduCourse> coursePage = courseMapper.selectPage(p, wrapper);

        return coursePage;
    }

    @Override
    public Map getCourseDetails(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        // 课程描述信息.讲师.分类
        CourseInfoDto courseInfo = courseService.queryInfo(id);
        // 记录访问次数
        Long viewCount = courseInfo.getViewCount();
        viewCount +=1;
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setViewCount(viewCount);
        courseMapper.updateById(eduCourse);


        // 章节小节信息
        Map<String, List<ChapterDto>> listMap = chapterService.queryChapter(id);
        List<ChapterDto> all = listMap.get("all");
         hashMap.put("courseInfo",courseInfo);
         hashMap.put("chapterInfo",all);
        return hashMap;
    }

    @Override
    public Map queryCourseInfo(Page<EduCourse> returnPage, String id) {
        Map<String, Object> hashMap = new HashMap<>();
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("category_pid",id);
        wrapper.eq("status","Publish");
        // 课程基本信息
        Page<EduCourse> coursePage = courseMapper.selectPage(returnPage, wrapper);
            hashMap.put("records",coursePage.getRecords());
            hashMap.put("total",coursePage.getTotal());
            hashMap.put("pages",coursePage.getPages());
            hashMap.put("size",coursePage.getSize());
            hashMap.put("current",coursePage.getCurrent());
            hashMap.put("previous",coursePage.hasPrevious());
            hashMap.put("next",coursePage.hasNext());
            // 一级列表名称
        EduSubject eduSubject = categoryMapper.selectById(id);
        hashMap.put("oneName",eduSubject.getTitle());
        // 二级列表集合
        QueryWrapper<EduSubject> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("parent_id",id);
        List<EduSubject> subjects = categoryMapper.selectList(categoryWrapper);
        hashMap.put("categoryList2",subjects);



        return hashMap;
    }

    @Override
    public Map queryCourseInfo1(Page<EduCourse> returnPage, String id) {
        Map<String, Object> hashMap = new HashMap<>();
        // 二级类别名称
        EduSubject eduSubject = categoryMapper.selectById(id);
        hashMap.put("twoName",eduSubject.getTitle());
        // 一级类别名称
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
          wrapper.eq("parent_id","0");
        EduSubject subject = categoryMapper.selectById(eduSubject.getParentId());
        hashMap.put("oneName",subject.getTitle());
        hashMap.put("oneID",subject.getId());
        // 二级类别课程
        QueryWrapper<EduCourse> courseWrapper = new QueryWrapper<>();
        courseWrapper.eq("category_id",id);
        Page<EduCourse> coursePage = courseMapper.selectPage(returnPage, courseWrapper);
        hashMap.put("records",coursePage.getRecords());
        hashMap.put("total",coursePage.getTotal());
        hashMap.put("pages",coursePage.getPages());
        hashMap.put("size",coursePage.getSize());
        hashMap.put("current",coursePage.getCurrent());
        hashMap.put("previous",coursePage.hasPrevious());
        hashMap.put("next",coursePage.hasNext());


        return hashMap;
    }

    @Override
    public List<EduCourse> queryPopularCourse() {
        List<EduCourse> eduCourses = courseMapper.queryPopularCourse();
        if (CollectionUtils.isEmpty(eduCourses)) {
            return new ArrayList<>();
        }
        return eduCourses;
    }

    @Override
    public VideoDto queryVideoInfo(String id) {
        VideoDto videoDto = videoMapper.queryVideoInfo(id);
            if (videoDto == null) {
                return  new VideoDto();
            }
        return videoDto;
    }

    @Override
    public Boolean userIsBuy(String cid, String username) {
        //查询mysql 有无购买此课程


        int i = this.courseMapper.userIsBuy(cid, username);
        return i > 0 ? true : false;




//        AtomicReference<Boolean> result = new AtomicReference<>(false);
//          // 查询redis是否有购买数据 redis 插入id有问题
//        List<String> list = this.redisTemplate.opsForList().range(username, 0, -1);
//            if (list.size() == 0) {
//                return result.get();
//            }else {
//                list.forEach(s -> {
//                    if (cid.equals(s)) {
//                        result.set(true);
//                    }else {
//                        result.set(false);
//                    }
//                });
//            }
//
//        return result.get();
    }

    @Override
    public CourseVo getOrderInfo(String cid) {
        EduCourse eduCourse = this.courseMapper.selectById(cid);
        if (eduCourse== null) {
            throw new CourseException(ResultEnum.QUERY_ERROR);
        }
        CourseVo courseVo = new CourseVo();
        BeanUtils.copyProperties(eduCourse,courseVo);
        return courseVo;
    }

    @Override
    public Boolean addUcourse(String courseId, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        long l = SnowFlake.nextId();
        String id = String.valueOf(l);
        System.out.println(id);
        hashMap.put("id",id);
        hashMap.put("courseId",courseId);
         hashMap.put("username",username);
        int i = this.courseMapper.addUcourse(hashMap);

        return i> 0 ? true: false;
    }

    @Override
    public Page getMyCourse(Page page,String username) {

        Page myCourse = this.courseMapper.selectMyCourse(page, username);
        return myCourse;


    }

    @Override
    public Integer queryCourse(String day) {

        Integer integer = this.courseMapper.queryCourse(day);
        return integer;
    }

    @Override
    public  List<TwoSubjectDto> getSecondListByFirsrId(String firstId) {
        List<TwoSubjectDto> list = new ArrayList<>();
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",firstId);
        List<EduSubject> category = this.categoryMapper.selectList(wrapper);
        category.forEach(x->{
            TwoSubjectDto two = new TwoSubjectDto();
            two.setId(x.getId());
            two.setTitle(x.getTitle());
            list.add(two);

        });

        return list;
    }

    @Override
    public CourseVo getcourse(String cid) {
        CourseVo courseVo = new CourseVo();
        EduCourse eduCourse = this.courseMapper.selectById(cid);
         if (eduCourse !=null) {

             BeanUtils.copyProperties(eduCourse,courseVo);
         }

        return courseVo;
    }


    //删除描述和课程
    public boolean judge(String param) {

        int i = descriptionMapper.deleteById(param);

        if (i > 0) {
            //删课程
            return courseMapper.deleteById(param) > 0;

        } else {
            log.error("删除课程描述失败: 课程idID[{}]", param);
            throw new CourseException(ResultEnum.DELETE_ERROR);
        }


    }

}