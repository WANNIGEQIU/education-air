package com.air.server.lecturer.controller;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.lecturer.dto.LecturerDto;
import com.air.server.lecturer.entity.EduLecturer;
import com.air.server.lecturer.exception.LecturerException;
import com.air.server.lecturer.service.EduLecturerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */

@RestController
@RequestMapping("/lecturer")

public class EduLecturerController   {


    @Autowired
    private EduLecturerService lecturerService;

    @GetMapping("/queryAll")
    public ResultCommon<List<EduLecturer>> queryAll() {
        return this.lecturerService.queryAllLecturer();
    }

    /**
     * 根据id删除讲师
     * @param id
     * @return
     */
   @DeleteMapping("{id}")
    public ResultCommon deleteId(@PathVariable("id") String id) {

        int i = this.lecturerService.deleteId(id);
         return ResultCommon.resultOk(i);

    }

    @PostMapping("/real/{page}/{limit}")
    public ResultCommon queryDeleted(@PathVariable("page") Long page,@PathVariable("limit") Long limit){

        return this.lecturerService.queryDeleted(page,limit);
    }



    /**
     * 条件分页查询
     * @param lecturerDto
     * @return
     */
    @PostMapping("/condtion/{page}/{limit}")
    public ResultCommon queryCondtion(@PathVariable("page") Long page,
                                      @PathVariable("limit") Long limit,@RequestBody(required = false) LecturerDto lecturerDto) {
        if (page < 0 || limit <0) {
            throw new LecturerException(ResultEnum.PARAM_ERROR);
        }
        Page<EduLecturer> p = new Page<>(page,limit);
       return this.lecturerService.queryCondtion(p,lecturerDto);
    }

 @PostMapping("/save")
    public ResultCommon save(@RequestBody LecturerDto lecturerDto) {
        boolean add = this.lecturerService.add(lecturerDto);
        if (add) {
            return ResultCommon.resultOk();
        } else {
            return ResultCommon.resultFail("添加失败啦啊 笨蛋");
        }
    }

    @DeleteMapping("/real/{id}")
    public ResultCommon realDelete(@PathVariable("id") String id) {
        boolean b = this.lecturerService.realDelete(id);
        if (b) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    @PutMapping("/recoverLecturer/{id}")
    public ResultCommon recoverLecturer(@PathVariable("id") String id) {
        boolean b = this.lecturerService.recoverLecturer(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail(false);
        }
    }

    @GetMapping("/queryid/{id}")
    public ResultCommon<EduLecturer> queryid(@PathVariable String id) {
        EduLecturer queryid = this.lecturerService.queryid(id);
        return ResultCommon.resultOk(queryid);

    }

    @PutMapping("/modfiy/{id}")
    public ResultCommon modfiy(@PathVariable String id, @RequestBody LecturerDto lecturerDto) {
        boolean b = this.lecturerService.modfiy(id, lecturerDto);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail("修改失败了奥",false);
        }
    }

    /**
     * 模拟登录
     */
    //{"code":20000,"data":{"roles":["admin"],"name":"admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}

    @PostMapping("/login")
    public ResultCommon login(){
      Map<String, Object> map = new HashMap<>();
        map.put("token","hahahha");
        return ResultCommon.resultOk(map);
    }

    @GetMapping("/info")
    public ResultCommon info(){
        Map<String, Object> map = new HashMap<>();
            map.put("roles","[admin]");
            map.put("name","admin");
            map.put("avatar","https://edu-test123.oss-cn-beijing.aliyuncs.com/2020/01/13/33eae38a-6907-488f-ae8e-1ddca8572de0file.png");
            return ResultCommon.resultOk(map);
    }
    //https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif
    //https://edu-test123.oss-cn-beijing.aliyuncs.com/1a.jpg
    // https://heimao-1301106277.cos.ap-beijing-fsi.myqcloud.com/qa.jpg

    @PostMapping("/logout")
    public ResultCommon logout(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader("X-Token");
        System.out.println(header);
        response.setHeader("X-Token","");
        return ResultCommon.resultOk();

    }
}

