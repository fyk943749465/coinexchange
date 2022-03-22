package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.dto.UserDto;
import com.bjsxt.feign.UserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.WorkIssue;
import com.bjsxt.mapper.WorkIssueMapper;
import com.bjsxt.service.WorkIssueService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService{

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Override
    public Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {

        Page<WorkIssue> workIssuePage = page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(status != null, WorkIssue::getStatus, status)
                .between(
                        !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime),
                        WorkIssue::getCreated,
                        startTime, endTime + " 23:59:59")
        );
        List<WorkIssue> records = workIssuePage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return workIssuePage;
        }
        List<Long> userIds = records.stream().map(WorkIssue::getUserId).collect(Collectors.toList());
        Map<Long, UserDto> basicUsers = userServiceFeign.getBasicUsers(userIds, null, null);
        records.forEach(workIssue -> {
            UserDto userDto = basicUsers.get(workIssue.getUserId());
            workIssue.setUsername(userDto == null ? "匿名用户" : userDto.getUsername());
            workIssue.setRealName(userDto== null ? "匿名用户" : userDto.getRealName());
        });
        return workIssuePage;
    }

    /**
     * 前台系统查询客户工单
     *
     * @param page
     * @return
     */
    @Override
    public Page<WorkIssue> getIssueList(Page<WorkIssue> page,Long userId) {
        return page(page,new LambdaQueryWrapper<WorkIssue>()
                        .eq(WorkIssue::getUserId,userId)
//                                            .eq(WorkIssue::getStatus,1)
        );
    }
}
