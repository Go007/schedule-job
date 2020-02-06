package com.hong.job;

import com.hong.job.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wanghong
 * @date 2020/02/05 18:54
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendemailApplicationTests {

    /**
     * 注入发送邮件的接口
     */
    @Autowired
    private IMailService mailService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        String[] to = {"1536054473@qq.com"};
        mailService.sendSimpleMail(to,"主题：你好普通邮件","内容：第一封邮件");
    }

    @Test
    public void sendmailHtml(){
        String[] to = {"1536054473@qq.com"};
        mailService.sendHtmlMail(to,"主题：你好html邮件","<h1>内容：第一封html邮件</h1>");
    }
}
