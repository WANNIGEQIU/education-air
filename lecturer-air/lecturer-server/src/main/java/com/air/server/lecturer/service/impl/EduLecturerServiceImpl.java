package com.air.server.lecturer.service.impl;

import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.common.exception.MyException;
import com.air.common.vo.CourseVo;
import com.air.server.lecturer.Client.LecturerFeign;
import com.air.server.lecturer.dto.LecturerDto;
import com.air.server.lecturer.entity.EduLecturer;
import com.air.server.lecturer.exception.LecturerException;
import com.air.server.lecturer.mapper.EduLecturerMapper;
import com.air.server.lecturer.service.EduLecturerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */
@Service
@Slf4j
public class EduLecturerServiceImpl implements EduLecturerService {

    @Autowired
    private EduLecturerMapper lecturerMapper;

    @Autowired
    private LecturerFeign lecturerFeign;

    /**
     * 查询所有讲师
     */
    @Override
    public ResultCommon<List<EduLecturer>> queryAllLecturer() {

        try {
            List<EduLecturer> list = this.lecturerMapper.selectList(null);
            if (CollectionUtils.isEmpty(list)) {
                throw new LecturerException(ResultEnum.QUERY_ERROR);
            } else {
                return ResultCommon.resultOk(list);
            }

        } catch (MyException e) {
            log.error(e.getMessage());
            return ResultCommon.resultFail(e.getCode(),e.getMessage());
        }

    }

    /**
     * 根据id删除讲师
     *
     * @param id
     * @return
     */
    @Override
    public int deleteId(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new LecturerException(ResultEnum.PARAM_ERROR);
        }
       return this.lecturerMapper.deleteById(id);


    }

    /**
     * 分页查询
     *
     * @param page 第几页 设置默认第一页
     * @param size 每页多少条 默认20
     * @return
     */
    @Override
    public ResultCommon pageLecturer(Integer page, Integer size) {
        try {
            Page<EduLecturer> objectPage = new Page<>(page, size);
            Page<EduLecturer> selectPage = this.lecturerMapper.selectPage(objectPage, null);
            if (selectPage == null) {
                throw new LecturerException(ResultEnum.EXCEPTION.getCode(), "分页查询失败 selectPage 为null");
            }
            return ResultCommon.resultOk(null);
        } catch (MyException e) {
            log.error(e.getMessage());
            return ResultCommon.resultFail();
        }
    }


    @Override
    public ResultCommon queryCondtion(Page p, LecturerDto lecturerDto) {
        try {
            Page<EduLecturer> condtion = this.lecturerMapper.queryCondtion(p, lecturerDto);
            if (condtion == null) {
                throw new LecturerException(ResultEnum.QUERY_ERROR);
            }
            return ResultCommon.resultOk(condtion);

        } catch (MyException m){
            log.error("接口调用出现异常"+m.getMessage());
            return ResultCommon.resultFail();
        }

    }

    @Override
    public boolean add(LecturerDto lecturerDto) {
        EduLecturer eduLecturer = new EduLecturer();
        try {
            if (StringUtils.isEmpty(lecturerDto.getName())) {
                throw new LecturerException(ResultEnum.NO_NAME);
            }
            if (StringUtils.isEmpty(lecturerDto.getLevel())) {
                throw new LecturerException(ResultEnum.NO_LEVEL);
            }
            if (StringUtils.isEmpty(lecturerDto.getIntro())) {
                throw new LecturerException(ResultEnum.NO_INTRODU);
            }
            BeanUtils.copyProperties(lecturerDto, eduLecturer);
            int insert = this.lecturerMapper.insert(eduLecturer);
            if (insert > 0) {
                return true;
            } else {
                throw new LecturerException(ResultEnum.EXCEPTION.getCode(), "添加失败");
            }

        } catch (MyException m) {
            log.error(m.getMessage() + ">>>>> 输入的参数:[{}]", lecturerDto);
            return false;
        }


    }

    @Override
    public EduLecturer queryid(String id) {

        EduLecturer eduLecturer = this.lecturerMapper.selectById(id);
        return eduLecturer;
    }



    @Override
    public boolean modfiy(String id, LecturerDto lecturerDto) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new LecturerException(ResultEnum.PARAM_ERROR);
        }
        EduLecturer eduLecturer = new EduLecturer();
        BeanUtils.copyProperties(lecturerDto, eduLecturer);
        eduLecturer.setId(id);
        int i = this.lecturerMapper.updateById(eduLecturer);
        //return i > 0 ? true : false;
        return i>0;
    }

    @Override
    public ResultCommon queryDeleted(Long page, Long limit) {
        //page = -1L;
        if (page < 0 || limit < 0) {
            log.error("传入的参数有误: [{}],[{}]",page,limit);
            throw new  LecturerException(ResultEnum.PARAM_ERROR);
        }
        Page<EduLecturer> p = new Page<>(page, limit);
        Page<EduLecturer> eduLecturerPage = this.lecturerMapper.queryDeleted(p);

        return ResultCommon.resultOk(eduLecturerPage);
    }

    @Override
    public boolean realDelete(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new LecturerException(ResultEnum.PARAM_ERROR);
        }
        int i = this.lecturerMapper.realDelete(id);
       return i > 0 ? true : false;

    }

    @Override
    public boolean recoverLecturer( String id) {
        HashMap<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(id)) {
            log.error("传递的参数id错误: [{}]",id);
            throw new LecturerException(ResultEnum.PARAM_ERROR);
        }
        map.put("id",id);
        map.put("time",LocalDateTime.now());
        int i = this.lecturerMapper.recoverLecturer(map);
        return i > 0? true :false;


    }

    @Override
    public Map lecturerWebList(Page<EduLecturer> objectPage) {
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<EduLecturer> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
         lecturerMapper.selectPage(objectPage,wrapper);
         // 讲师列表
        List<EduLecturer> records = objectPage.getRecords();
        // 总记录数
        long total = objectPage.getTotal();
        // 每页记录数
        long size = objectPage.getSize();
        // 总页数
        long pages = objectPage.getPages();
        // 当前页
        long current = objectPage.getCurrent();
        // 是否有下一页
        boolean hasNext = objectPage.hasNext();
        // 是否有上一页
        boolean previous = objectPage.hasPrevious();

        map.put("records",records);
        map.put("total",total);
        map.put("size",size);
        map.put("pages",pages);
        map.put("current",current);
        map.put("hasNext",hasNext);
        map.put("previous",previous);
        return map;
    }

    @Override
    public Map lecturerDetail(String id) {
        Map<String, Object> hashMap = new HashMap<>();
        // 讲师信息
        EduLecturer eduLecturer = lecturerMapper.selectById(id);
        // 课程信息
        List<CourseVo> courseVo = lecturerFeign.queryIds(id);
        hashMap.put("lecturer",eduLecturer);
        hashMap.put("course",courseVo);
        return hashMap;
    }

    @Override
    public Map queryPopularLecturer() {
        Map<String, Object> hashMap = new HashMap<>();
        List<EduLecturer> lecturerList = lecturerMapper.queryPopularLecturer();
        if (CollectionUtils.isEmpty(lecturerList)) {
            return hashMap;
        }
        hashMap.put("lecturerList",lecturerList);
        return hashMap;
    }


}
