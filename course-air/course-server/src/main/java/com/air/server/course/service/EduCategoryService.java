package com.air.server.course.service;


import com.air.server.course.entity.EduSubject;
import com.air.server.course.entity.dto.OneSubjectDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCategoryService {

    List<String> impoetExcel(MultipartFile file);

    List<OneSubjectDto> getSubjectTree();

    boolean deleteId(String id);

    boolean addOneCategory(EduSubject subject);

    boolean addTwoCategory(EduSubject subject);


}
