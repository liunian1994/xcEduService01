package com.xuecheng.order.task;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;
    //每隔1分钟扫描消息表，向mq发送消息
    @Scheduled(fixedDelay = 60000)//单位ms
    public void sendChoosecourseTask(){
        LOGGER.info("===============定时查询消息任务开始===============");
        //取出当前时间1分钟之前的时间
        Calendar calendar =new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,-1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 1000);
        System.out.println(taskList);
        for (XcTask x:taskList) {
            //任务id
            String taskId = x.getId();
            //版本号
            Integer version = x.getVersion();
            //调用乐观锁方法校验任务是否可以执行
            if (taskService.getTask(taskId, version) > 0) {
                //发送选课消息
                taskService.publish(x, x.getMqExchange(),x.getMqRoutingkey());
                LOGGER.info("send choose course task id:{}",taskId);
            }
        }
        LOGGER.info("===============定时查询消息任务结束===============");
    }

    //@Scheduled(fixedRate = 5000) //上次执行开始时间后5秒执行
//@Scheduled(fixedDelay = 5000) //上次执行完毕后5秒执行
//@Scheduled(initialDelay=3000, fixedRate=5000) //第一次延迟3秒，以后每隔5秒执行一次
   // @Scheduled(cron = "0/3 * * * * *")//每隔3秒执行一次
    public void task1() {
        LOGGER.info("===============测试定时任务1开始===============");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("===============测试定时任务1结束===============");
    }

    //@Scheduled(fixedRate = 3000) //上次执行开始时间后3秒执行
    public void task2(){
        LOGGER.info("===============测试定时任务2开始===============");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("===============测试定时任务2结束===============");
    }


    /**
     * 接收选课响应结果
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE })
    public void receiveFinishChoosecourseTask(XcTask task, Message message, Channel channel) throws
            IOException {
        LOGGER.info("receiveChoosecourseTask...{}",task.getId());
        //接收到 的消息id
        String id = task.getId();
        //删除任务，添加历史任务
        taskService.finishTask(id);
    }

}