package com.air.server.course.consumer;


import com.air.server.course.entity.EduCourse;
import com.air.server.course.entity.EduVideo;
import com.air.server.course.mapper.EduCourseMapper;
import com.air.server.course.mapper.EduVideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@Slf4j
public class CourseConsumer {

    @Autowired
    private EduCourseMapper courseMapper;

    @Autowired
    private EduVideoMapper videoMapper;

    /**
     * 消费订单支付成功后的消息
     * @param courseId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "H-ORDER",durable = "true"),
            exchange = @Exchange(value = "ORDER",ignoreDeclarationExceptions = "true",
            type = ExchangeTypes.TOPIC),
            key = {"order"}

    ))
    public void sendMessage(String courseId) {
        log.info("mq消费的id：{}",courseId);
            if (StringUtils.isEmpty(courseId)) {
                return;
            }
        EduCourse eduCourse = this.courseMapper.selectById(courseId);
            eduCourse.setBuyCount(eduCourse.getBuyCount()+1L);
        int i =  this.courseMapper.updateById(eduCourse);
        log.info("order-mq 更新课程购买数量影响行数: {}",i);


    }

    /**
     * 消费视频上传消息
     * @param map  视频凭证
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "H-CLOUD",durable = "true"),
            exchange = @Exchange(value = "VIDEO",ignoreDeclarationExceptions = "true",
            type = ExchangeTypes.TOPIC),
            key = {"video"}
    ))
    public void sendVideoMsg(Map map) {
        if (CollectionUtils.isEmpty(map)) {
            log.info("视频凭证消费失败");
            return;
        }
        String proof = (String) map.get("proof");
        String vid = (String) map.get("vid");
        log.info("课程mq消费: 凭证{}, 视频id{}",proof,vid);
        EduVideo eduVideo = new EduVideo();
        eduVideo.setId(vid);
        eduVideo.setVideoSourceId(proof);
        int i = this.videoMapper.updateById(eduVideo);
        log.info("VIDEO-MQ 影响行数: {}",i);


    }
}
