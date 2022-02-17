package com.bjsxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.WorkIssue;
import com.bjsxt.model.R;
import com.bjsxt.service.WorkIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/workIssues")
@Api(tags = "客户工单的控制器")
public class WorkIssueController {


    @Autowired
    private WorkIssueService workIssueService;


    @GetMapping
    @ApiOperation(value = "分页条件查询工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "工单当前的处理状态"),
            @ApiImplicitParam(name = "startTime", value = "工单创建的起始时间"),
            @ApiImplicitParam(name = "endTime", value = "工单创建的截至时间"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
    })
    @PreAuthorize("hasAuthority('work_issue_query')")
    public R<Page<WorkIssue>> findByPage(@ApiIgnore Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<WorkIssue> workIssuePage = workIssueService.findByPage(page, status, startTime, endTime);
        return R.ok(workIssuePage);
    }

    @PatchMapping
    @ApiOperation("回复工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "工单id"),
            @ApiImplicitParam(name = "answer", value = "回复内容")
    })
    @PreAuthorize("hasAuthority('work_issue_update')")
    public R workIssueUpdate(Long id, String answer) {
        WorkIssue workIssue = new WorkIssue();
        workIssue.setId(id);
        workIssue.setAnswer(answer);
        workIssue.setStatus(2);
        boolean b = workIssueService.updateById(workIssue);
        if (b) {
            return R.ok();
        }
        return R.fail("回复工单失败");
    }

}