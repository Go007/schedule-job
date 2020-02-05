package com.hong.job.controller;

import com.hong.job.domain.QuartzTaskInfo;
import com.hong.job.service.QuartzService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author wanghong
 * @Description quartz controller主要逻辑
 * @date 2020/02/04 15:31
 **/
@Controller
@RequestMapping("/quartz")
public class QuartzController {

    private static final Logger logger = LoggerFactory.getLogger(QuartzController.class);

    @Autowired
    private QuartzService quartzService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String listTasks(Model model, @RequestParam(value = "currentPage", required = false, defaultValue = "1") String currentPage,
                            @RequestParam(value = "taskNo", required = false) String taskNo) {
        try {
            List<QuartzTaskInfo> taskList = quartzService.getTaskList(taskNo, currentPage);
            model.addAttribute("taskList", taskList);
            model.addAttribute("size", taskList.size());
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPage", 1);
            model.addAttribute("taskNo", taskNo);
        } catch (Exception e) {
            logger.error("首页跳转发生异常exceptions-->" + e.toString());
        }
        return "/index";
    }
    
    @RequestMapping(value = "/add/taskpage", method = RequestMethod.GET)
    public String addTaskpage() {
        return "addtask";
    }

    @ResponseBody
    @RequestMapping(value = "/add/task", method = RequestMethod.POST)
    public String addTask(QuartzTaskInfo taskInfo) {
        try {
            String result = quartzService.addTask(taskInfo);
            return result;
        } catch (Exception e) {
            logger.error("/add/task exception={}", e);
            return "fail";
        }
    }

    @RequestMapping(value = "/edit/taskpage", method = RequestMethod.GET)
    public String editTaskpage(Model model, String id) {
        QuartzTaskInfo taskInfo = quartzService.getTaskById(id);
        model.addAttribute("taskInfo", taskInfo);
        return "updatetask";
    }

    @ResponseBody
    @RequestMapping(value = "/edit/task", method = RequestMethod.POST)
    public String editTask(QuartzTaskInfo taskInfo) {
        try {
            String result = quartzService.updateTask(taskInfo);
            return result;
        } catch (Exception e) {
            logger.error("/edit/task exception={}", e);
            return "fail";
        }
    }

    /**
     * 启动 或者 暂定定时任务
     *
     * @param taskNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list/optionjob", method = RequestMethod.GET)
    public String optionJob(@RequestParam(name = "taskNo") String taskNo) {
        logger.info("");
        if (StringUtils.isEmpty(taskNo)) {
            return "success";
        }
        try {
            return quartzService.startJob(taskNo);
        } catch (Exception e) {
            logger.error("/list/optionjob exception={}", e);
            return "fail";
        }
    }

    /**
     * 立即运行一次定时任务
     *
     * @param taskNo
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/runtask/rightnow", method = RequestMethod.GET)
    public String runTaskRightNow(@RequestParam(value = "taskNo", required = false) String taskNo, Model model) {
        logger.info("");
        try {
            if (StringUtils.isEmpty(taskNo)) {
                return "success";
            }
            return quartzService.runTaskRightNow(taskNo);
        } catch (Exception e) {
            logger.error("");
            return "fail";
        }
    }

}

