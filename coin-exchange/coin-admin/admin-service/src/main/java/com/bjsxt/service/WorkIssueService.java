package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.WorkIssue;
import com.baomidou.mybatisplus.extension.service.IService;
public interface WorkIssueService extends IService<WorkIssue>{


    Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime);
}
