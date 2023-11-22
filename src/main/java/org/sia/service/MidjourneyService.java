package org.sia.service;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sia.config.MidjourneyFeginConfig;
import org.sia.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/27 14:51
 */
@FeignClient(name = "midjourneyService",url = "${midjourney.domain}",configuration = MidjourneyFeginConfig.class )
public interface MidjourneyService {
    @ApiOperation(value = "指定ID获取账号")
    @GetMapping("/account/{id}/fetch")
    DiscordAccount fetchAccount(@ApiParam(value = "账号ID") @PathVariable String id);

    @ApiOperation(value = "查询所有账号")
    @GetMapping("/account/list")
    List<DiscordAccount> listAccount();

    @ApiOperation(value = "提交Imagine任务")
    @PostMapping("/submit/imagine")
    SubmitResultVO imagine(@RequestBody SubmitImagineDTO imagineDTO);

    @ApiOperation(value = "绘图变化-simple")
    @PostMapping("/submit/simple-change")
    SubmitResultVO simpleChange(@RequestBody SubmitSimpleChangeDTO simpleChangeDTO);

    @ApiOperation(value = "绘图变化")
    @PostMapping("/submit/change")
     SubmitResultVO change(@RequestBody SubmitChangeDTO changeDTO);

    @ApiOperation(value = "提交Describe任务")
    @PostMapping("/submit/describe")
    SubmitResultVO describe(@RequestBody SubmitDescribeDTO describeDTO);


    @ApiOperation(value = "提交Blend任务")
    @PostMapping("/submit/blend")
    SubmitResultVO blend(@RequestBody SubmitBlendDTO blendDTO);

    @ApiOperation(value = "指定ID获取任务")
    @GetMapping("/task/{id}/fetch")
    Task fetchTask(@ApiParam(value = "任务ID") @PathVariable String id);

    @ApiOperation(value = "查询任务队列")
    @GetMapping("/task/queue")
    List<Task> getTaskQueue();

    @ApiOperation(value = "查询所有任务")
    @GetMapping("/task/list")
    List<Task> geTaskList();

    @ApiOperation(value = "根据ID列表查询任务")
    @PostMapping("/task/list-by-condition")
    List<Task> getTaskListByIds(@RequestBody TaskConditionDTO conditionDTO);
}
