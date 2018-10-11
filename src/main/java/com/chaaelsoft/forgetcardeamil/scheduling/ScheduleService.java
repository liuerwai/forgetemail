package com.chaaelsoft.forgetcardeamil.scheduling;

import com.alibaba.fastjson.JSON;
import com.chaaelsoft.forgetcardeamil.factory.Factory;
import com.chaaelsoft.forgetcardeamil.po.CardLogPo;
import com.chaaelsoft.forgetcardeamil.po.WorkerPo;
import com.chaaelsoft.forgetcardeamil.utils.CrawlCradLogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleService {

    @Autowired
    MailSender mailSender;

    @Scheduled(cron = "0 0 11 * * ?")
    public void validateWorkCardLog() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf2.format(new Date());
        WorkerPo worker = Factory.createWorker("14015");
        CrawlCradLogUtils.login(worker);
        CrawlCradLogUtils.setExactUserId(worker);
        String cardLog = CrawlCradLogUtils.getWorkerCardLog(worker, date, date);
        // 处理打卡记录
        List<CardLogPo> listCardLog = JSON.parseArray(cardLog, CardLogPo.class);
        if (listCardLog != null && listCardLog.size() > 0) {
            Comparator<CardLogPo> comparator = (cardLog1, cardLog2) -> {
                try {
                    return sdf.parse(cardLog1.getTTime()).compareTo(
                            sdf.parse(cardLog2.getTTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            };
            listCardLog.sort(comparator.reversed());
            Calendar lastLog = Calendar.getInstance();
            lastLog.setTime(sdf.parse(listCardLog.get(0).getTTime()));
            if (lastLog.get(Calendar.HOUR_OF_DAY) < 18) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("liuxg@channelsoft.com");
                message.setTo("liuxg@channelsoft.com");
                message.setSubject("下班忘记打卡啦!!!");
                StringBuffer text = new StringBuffer("");
                text.append("下班忘记打卡啦！！！！！！！\n")
                        .append("赶紧回去打卡算加班，加班费到手美滋滋！！！！！！！\n")
                        .append(getCardLogStr(listCardLog));
                message.setText(text.toString());
                mailSender.send(message);
            }
        }
    }

    /**
     * 打卡记录toString
     *
     * @param list
     * @return
     */
    private String getCardLogStr(List<CardLogPo> list) {

        if (list != null && list.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer("");
            stringBuffer.append("打卡情况：\n");
            for (CardLogPo item : list) {
                stringBuffer.append(item.getTTime()).append("\n");
            }
            return stringBuffer.toString();
        }
        return "";
    }
}
