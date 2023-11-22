package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sia.dto.*;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.TaskAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.enums.TaskStatus;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.ImageMapper;
import org.sia.model.ImageAccount;
import org.sia.model.ImageTask;
import org.sia.vo.AdminImageCommonInfoVo;
import org.sia.vo.AdminImageConsumeInfoVo;
import org.sia.vo.ImageTaskStateDto;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.ImagePublicChangeReqVo;
import org.sia.vo.request.ImageTaskSearchReqVo;
import org.sia.vo.request.ImageTaskSubmitReqVo;
import org.sia.vo.response.ImageActionCostResVo;
import org.sia.vo.response.ImageGallerySearchResVo;
import org.sia.vo.response.ImageServiceInfoResVo;
import org.sia.vo.response.ImageTaskSearchResVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/17 21:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService extends ServiceImpl<ImageMapper, ImageTask> {

    private final MidjourneyService midjourneyService;
    private final ConfigService configService;
    private final UserPackageService userPackageService;
    private final ImageAccountService accountService;


    private Long getCostQuota(TaskAction action) {
        AdminImageConsumeInfoVo config = configService.getConfigObject(ConfigKeyEnum.IMAGE_CONSUME).toBean(AdminImageConsumeInfoVo.class);
        Integer integral = config.getIntegralByTaskAction(action);
        return integral.longValue();
    }

    public ImageTask submitTask(ImageTaskSubmitReqVo reqVo) {
        // 是否已停用维护
        AdminImageCommonInfoVo config = configService.getConfigObject(ConfigKeyEnum.IMAGE_COMMON).toBean(AdminImageCommonInfoVo.class);
        if (config.getStop()){
            throw new BusinessException(ResultEnum.ERROR,"绘画服务停用维护中");
        }
        // 是否过滤--fast --relax --turbo 
        if (config.getFilterParam() && StrUtil.isNotEmpty(reqVo.getPrompt())){
            reqVo.setPrompt(StrUtil.removeAny(reqVo.getPrompt(),"--fast","--relax","--turbo"));
        }
        SubmitResultVO taskResult;
        String userId = RequestDataHandler.getUserId();
        // 查询Model扣除的积分
        Long quota = this.getCostQuota(reqVo.getAction());
        // 扣除积分额度
        Map<Long, Long> pkgCostMap = Maps.newHashMap();
        userPackageService.decrease(userId, quota, pkgCostMap);
        // 自定义参数
        String state = JSONUtil.toJsonStr(ImageTaskStateDto.builder()
                .userId(userId)
                .isPublic(reqVo.getIsPublic())
                .cost(quota.intValue())
                .pkgCostMap(pkgCostMap)
                .taskId(reqVo.getTaskId())
                .index(reqVo.getIndex())
                .build());

        if (TaskAction.IMAGINE.equals(reqVo.getAction())) {
            // 生成图片
            if (StrUtil.isBlank(reqVo.getPrompt())) {
                throw new BusinessException(ResultEnum.ERROR, "请填写提示词");
            }
            SubmitImagineDTO imagineReqDto = new SubmitImagineDTO();
            imagineReqDto.setPrompt(reqVo.getPrompt());
            if (CollUtil.isNotEmpty(reqVo.getImageList())){
                imagineReqDto.setBase64Array(reqVo.getImageList().stream().filter(ObjectUtil::isNotNull).collect(Collectors.toList()));
            }
            imagineReqDto.setState(state);
            taskResult = midjourneyService.imagine(imagineReqDto);
        } else if (TaskAction.BLEND.equals(reqVo.getAction())) {
            // 混图
            List<String> imageList = reqVo.getImageList();
            if (CollUtil.isEmpty(imageList) || imageList.size() < 2) {
                throw new BusinessException(ResultEnum.ERROR, "请上传图片");
            }
            SubmitBlendDTO imagineReqDto = new SubmitBlendDTO();
            imagineReqDto.setBase64Array(imageList);
            imagineReqDto.setState(state);
            taskResult = midjourneyService.blend(imagineReqDto);
        } else if (TaskAction.DESCRIBE.equals(reqVo.getAction())) {
            // 咒语解析
            List<String> imageList = reqVo.getImageList();
            if (CollUtil.isEmpty(imageList)) {
                throw new BusinessException(ResultEnum.ERROR, "请上传图片");
            }
            SubmitDescribeDTO imagineReqDto = new SubmitDescribeDTO();
            imagineReqDto.setBase64(imageList.get(0));
            imagineReqDto.setState(state);
            taskResult = midjourneyService.describe(imagineReqDto);
        } else if (TaskAction.UPSCALE.equals(reqVo.getAction())
                || TaskAction.VARIATION.equals(reqVo.getAction())
                || TaskAction.REROLL.equals(reqVo.getAction())) {
            // 放大U|微调V|重新生成
            if (StrUtil.isEmpty(reqVo.getTaskId())) {
                throw new BusinessException(ResultEnum.ERROR, "请填写任务ID");
            }
            SubmitChangeDTO imagineReqDto = new SubmitChangeDTO();
            imagineReqDto.setTaskId(reqVo.getTaskId());
            imagineReqDto.setAction(reqVo.getAction());
            imagineReqDto.setIndex(reqVo.getIndex());
            imagineReqDto.setState(state);
            taskResult = midjourneyService.change(imagineReqDto);
        } else {
            throw new BusinessException(ResultEnum.ERROR, "不支持的操作类型");
        }
        // 状态码: 1(提交成功), 21(已存在), 22(排队中), other(错误)
        if (taskResult.getCode() != 1) {
            throw new BusinessException(ResultEnum.ERROR, taskResult.getDescription());
        }
        return this.getTaskInfo(taskResult.getResult());
    }

    /**
     * 图片反代
     *
     * @param url
     */
    public byte[] getProxyImage(String url) {
        return HttpUtil.createGet(url)
                .setHttpProxy("127.0.0.1", 7890)
                .execute()
                .bodyBytes();
    }

    public ImageTask getTaskInfo(String id) {
        ImageTask task = BeanUtil.toBean(midjourneyService.fetchTask(id), ImageTask.class);
        task.setMiniUrl(this.handleMiniUrl(task.getMiniUrl()));
        return task;
    }

    public ImageTaskSearchResVo getDbTask(String id) {
        ImageTask task = this.getById(id);
        if (ObjectUtil.isNull(task)) {
            task = this.getTaskInfo(id);
        }
        // 图片处理
        task.setMiniUrl(this.handleMiniUrl(task.getMiniUrl()));
        return BeanUtil.toBean(task, ImageTaskSearchResVo.class);
    }


    private String handleMiniUrl(String url) {
        // OSS缩略图访问处理
        if (StrUtil.isNotBlank(url) && url.startsWith("http")) {
            return SpringUtil.getBean(OssService.class).getExpireTimeUrl(url);
        }
        return url;
    }


    public Page<ImageTaskSearchResVo> searchTaskList(PageReqVo<ImageTaskSearchReqVo> reqVo) {
        if (ObjectUtil.isNull(reqVo.getCondition())) {
            reqVo.setCondition(new ImageTaskSearchReqVo());
        }
        Page<ImageTaskSearchResVo> result = this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
        result.getRecords().forEach(record -> {
            record.setMiniUrl(this.handleMiniUrl(record.getMiniUrl()));
        });
        return result;
    }

    public Page<ImageGallerySearchResVo> searchGalleryList(PageReqVo reqVo) {
        Page<ImageGallerySearchResVo> result = this.baseMapper.searchGalleryList(reqVo.getPage());
        result.getRecords().forEach(record -> {
            record.setMiniUrl(this.handleMiniUrl(record.getMiniUrl()));
        });
        return result;
    }

    public void changePublic(ImagePublicChangeReqVo reqVo) {
        this.update(Wrappers.<ImageTask>lambdaUpdate()
                .eq(ImageTask::getUserId, RequestDataHandler.getUserId())
                .eq(ImageTask::getId, reqVo.getTaskId())
                .set(ImageTask::getIsPublic, reqVo.getIsPublic()));
    }

    public void deleteTask(String id) {
        this.remove(Wrappers.<ImageTask>lambdaQuery()
                .eq(ImageTask::getUserId, RequestDataHandler.getUserId())
                .eq(ImageTask::getId, id)
        );
    }

    public ImageServiceInfoResVo getServiceInfo() {
        ImageServiceInfoResVo result = new ImageServiceInfoResVo();
        // MJ绘图任务状态信息
        Map<String, Long> data = Maps.newHashMap();
        for (TaskStatus status : TaskStatus.values()) {
            data.put(status.name(), this.count(Wrappers.<ImageTask>lambdaQuery().eq(ImageTask::getStatus, status)));
        }
        result.setData(data);
        // 运行状态
        result.setState(true);
        // 获取配置
        return result;

    }

    public List<ImageActionCostResVo> modelList() {
        AdminImageConsumeInfoVo config = configService.getConfigObject(ConfigKeyEnum.IMAGE_CONSUME).toBean(AdminImageConsumeInfoVo.class);
        return Lists.newArrayList(
                new ImageActionCostResVo("生成图片", TaskAction.IMAGINE.name(), config.getImagineIntegral()),
                new ImageActionCostResVo("放大", TaskAction.UPSCALE.name(), config.getUpscaleIntegral()),
                new ImageActionCostResVo("微调", TaskAction.VARIATION.name(), config.getVariateIntegral()),
                new ImageActionCostResVo("重新生成", TaskAction.REROLL.name(), config.getResetIntegral()),
                new ImageActionCostResVo("咒语解析", TaskAction.DESCRIBE.name(), config.getDescribeIntegral()),
                new ImageActionCostResVo("多图混合", TaskAction.BLEND.name(), config.getBlendIntegral())
        );
    }

    public void checkState() {
        List<ImageTask> taskList = this.list(Wrappers.<ImageTask>lambdaQuery().isNull(ImageTask::getFinishTime));
        long time = DateUtil.date().getTime();
        taskList = taskList.stream()
                .filter(task -> NumberUtil.sub(task.getSubmitTime().longValue(), time) > 300000)
                .peek(task -> {
                    task.setFinishTime(time);
                    task.setStatus(TaskStatus.FAILURE);
                    task.setFailReason("检测任务超时,中断执行");
                })
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(taskList)) {
            return;
        }
        this.updateBatchById(taskList);
    }

    public void notifyHook(Task task) {
        log.info(">>> Midjourney绘画服务 任务状态:{}", JSONUtil.toJsonPrettyStr(task));
        ImageTask imageTask = BeanUtil.toBean(task, ImageTask.class);
        ImageTaskStateDto state = JSONUtil.toBean(task.getState(), ImageTaskStateDto.class);
        imageTask.setIsPublic(state.getIsPublic());
        imageTask.setUserId(state.getUserId());
        imageTask.setCost(state.getCost());
        // 生成失败退还积分
        if (TaskStatus.FAILURE.equals(task.getStatus())) {
            imageTask.setBack(state.getCost());
            // 退还积分处理
            SpringUtil.getBean(UserPackageService.class).increase(state.getPkgCostMap());
        } else if (TaskStatus.SUCCESS.equals(task.getStatus())) {
            // 图片处理完成
            // 1.上传缩略图
            imageTask.setMiniUrl(SpringUtil.getBean(FileService.class).uploadMidImage(imageTask.getImageUrl()));
            // 2.根据操作类型组装前端按钮
            imageTask.setState(this.installState(task.getAction(), state, task.getPrompt()));
        }
        SpringUtil.getBean(ImageService.class).saveOrUpdate(imageTask);
    }

    private String installState(TaskAction action, ImageTaskStateDto state, String prompt) {
        JSONObject stateJson = JSONUtil.createObj();
        List<JSONObject> methodList = SpringUtil.getBean(ConfigService.class).getConfigList(ConfigKeyEnum.KEY_MJ_METHOD);

        if (TaskAction.IMAGINE.equals(action)
                || TaskAction.REROLL.equals(action)
                || TaskAction.VARIATION.equals(action)
                || TaskAction.BLEND.equals(action)) {
            // 生成图片||重新生成||微调||混图
            stateJson.set("methodList", methodList);
        } else if (TaskAction.UPSCALE.equals(action)) {
            // 放大
            List<JSONObject> optList = methodList.stream()
                    .filter(method -> action.name().equals(method.getStr("action")))
                    .findFirst()
                    .get()
                    .getBeanList("opts", JSONObject.class);
            stateJson.set("taskId", state.getTaskId())
                    .set("content", StrUtil.format("放大{}", optList.stream().filter(opt -> state.getIndex().equals(opt.getInt("index"))).findFirst().get().getStr("label")));
        } else if (TaskAction.DESCRIBE.equals(action)) {
            // 咒语解析
            stateJson.set("prompt", prompt);
        }
        return stateJson.toString();
    }


    public void checkAccountState() {
        List<DiscordAccount> accountList = midjourneyService.listAccount();
        accountList.forEach(account -> {
            accountService.update(Wrappers.<ImageAccount>lambdaUpdate()
                    .eq(ImageAccount::getId, account.getChannelId())
                    .set(ImageAccount::getIsValid, account.isEnable() ? 1 : 0)
            );
        });

    }
}
