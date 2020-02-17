package com.air.server.lecturer.controller.web;

import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.lecturer.entity.EduLecturer;
import com.air.server.lecturer.service.EduLecturerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/lecturer/fromweb")
public class WebLecturerController {

    @Autowired
    private EduLecturerService lecturerService;

    /**
     * 讲师列表
     */
    @GetMapping("/{page}/{limit}")
    @ApiOperation(value = "web讲师列表")
    public ResultCommon lecturerWebList(@PathVariable Long page,
                                        @PathVariable Long limit){


        Page<EduLecturer> objectPage = new Page<>(page, limit);
        Map map = lecturerService.lecturerWebList(objectPage);
        return ResultCommon.resultOk(map);

    }

    /**
     * 讲师详情和课程
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "讲师详情")
    public ResultCommon lecturerDetail(@PathVariable String id) {
        Map map = lecturerService.lecturerDetail(id);
        return ResultCommon.resultOk(map);


    }

    /**
     * 明星讲师列表
     */
    @GetMapping("/Popular")
    public ResultCommon queryPopularLecturer() {
        Map map = lecturerService.queryPopularLecturer();
        if (map.size() >0) {
            return ResultCommon.resultOk(map);
        }else {
            return ResultCommon.resultFail(ResultEnum.NO_POPULAR_LECTURER);
        }
    }


}
