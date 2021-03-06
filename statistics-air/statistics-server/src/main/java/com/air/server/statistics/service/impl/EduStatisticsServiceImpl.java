package com.air.server.statistics.service.impl;

import com.air.common.enums.ResultEnum;
import com.air.server.statistics.client.CourseFeign;
import com.air.server.statistics.client.OrderFeign;
import com.air.server.statistics.client.UserFeign;
import com.air.server.statistics.entity.EduStatistics;
import com.air.server.statistics.exception.StatisticsException;
import com.air.server.statistics.mapper.EduStatisticsMapper;
import com.air.server.statistics.service.EduStatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
@Service
@Slf4j
public class EduStatisticsServiceImpl implements EduStatisticsService {
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private EduStatisticsMapper statisticsMapper;

    @Autowired
    private CourseFeign courseFeign;

    @Override
    public int dayRecord(String day) {

        Integer count = userFeign.queryNums(day);
        log.info(day + " 注册人数: {}", count);

        Integer orderNum = orderFeign.queryOrderNum(day);
        log.info(day+"订单量: {}",orderNum);

        Integer courseNum = courseFeign.queryCourse(day);
        log.info(day+"课程销量: {}",courseNum);


        EduStatistics eduStatistics = new EduStatistics();
        // 注册人数
        eduStatistics.setRegisterNum(count);
        // 订单量
        if (orderNum == -1) {
            eduStatistics.setOrderNum(0);
        }else {
            eduStatistics.setOrderNum(orderNum);
        }
        // 记录统计的日期
        eduStatistics.setRecordDate(day);
        eduStatistics.setCourseNum(RandomUtils.nextInt(10, 20));
        //eduStatistics.setBuyNum(RandomUtils.nextInt(10,100));
        eduStatistics.setBuyNum(courseNum);
        // 有记录就删除
        QueryWrapper<EduStatistics> wrapper = new QueryWrapper<>();
        wrapper.eq("record_date", day);
        Integer i = statisticsMapper.selectCount(wrapper);
        if (i > 0) {
            statisticsMapper.delete(wrapper);
        }
        int insert = statisticsMapper.insert(eduStatistics);

        return insert;
    }

    @Override
    public Map showChart(String type, String beginTime, String endTime) {

        if (StringUtils.isEmpty(type)) {
            throw new StatisticsException(ResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(beginTime) && StringUtils.isEmpty(endTime)) {
            throw new StatisticsException(ResultEnum.ERROR_NO_TIME);
        }

        // 存结果
        Map<String, Object> map = new HashMap<>();

        // 存时间
        List<String> dayList = new ArrayList<>();

        // 存数据
        ArrayList<Object> dataList = new ArrayList<>();

        // 查询 统计表数据
        QueryWrapper<EduStatistics> wrapper = new QueryWrapper<>();
        wrapper.between("record_date", beginTime, endTime);
        wrapper.orderByAsc("record_date");
        List<EduStatistics> list = statisticsMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(list)) {
            log.error("查询统计表失败 {},{},{}", type, beginTime, endTime);
            throw new StatisticsException(ResultEnum.QUERY_ERROR);
        }

        // 判断查询的类型
        list.forEach(l -> {

            // 获取统计时间
            String recordDate = l.getRecordDate();
            dayList.add(recordDate);
            // 判断获取的数据
            switch (type) {
                case "1": {
                    dataList.add(l.getRegisterNum());
                    break;
                }
                case "2": {
                     dataList.add(l.getOrderNum());
                     break;
                }
                case "3": {
                    dataList.add(l.getBuyNum());
                    break;
                }
                case "4": {
                    dataList.add(l.getCourseNum());
                    break;
                }
                default:
                    return;
            }


        });
        map.put("daylist",dayList);
        map.put("datalist",dataList);
        return map;
    }
}
