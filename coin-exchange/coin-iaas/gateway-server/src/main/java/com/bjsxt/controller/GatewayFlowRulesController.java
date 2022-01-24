package com.bjsxt.controller;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class GatewayFlowRulesController {

    /**
     * get current system restrict traffic
     */
    @GetMapping("/gw/flow/rules")
    public Set<GatewayFlowRule> getCurrentGatewayFlowRules() {
        return GatewayRuleManager.getRules();
    }

    /**
     * get api groups
     * @return
     */
    @GetMapping("/gw/api/group")
    public Set<ApiDefinition> getApiGroups() {
        return GatewayApiDefinitionManager.getApiDefinitions();
    }


}
