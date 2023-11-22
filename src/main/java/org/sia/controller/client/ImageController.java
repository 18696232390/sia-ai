package org.sia.controller.client;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.dto.Task;
import org.sia.handler.RequestDataHandler;
import org.sia.service.ImagePromptService;
import org.sia.service.ImageService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.ImageActionCostResVo;
import org.sia.vo.response.ImageGallerySearchResVo;
import org.sia.vo.response.ImageTaskSearchResVo;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImagePromptService promptService;


    /**
     * 绘画模型积分规则
     * @return
     */
    @GetMapping("/model/list")
    public R<List<ImageActionCostResVo>> modelList() {
        return R.success(imageService.modelList());
    }


    /**
     * 提交任务
     *
     * @param reqVo
     * @return
     */
    @PostMapping("/task/submit")
    public R<String> submitTask(@RequestBody @Validated ImageTaskSubmitReqVo reqVo) {
        return R.success(imageService.submitTask(reqVo));
    }


    /**
     * 访问国外图片
     *
     * @param url
     * @return
     */
    @GetMapping("proxy")
    public String proxy(@RequestParam String url) {
        // 将图片字节数组写入输出流
        return StrUtil.format("data:{};base64,{}",MediaType.IMAGE_PNG_VALUE,Base64.encode(imageService.getProxyImage(url)));
    }

    /**
     * 查看任务进度
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/task/{id}")
    public R<ImageTaskSearchResVo> task(@PathVariable String id) {
        return R.success(imageService.getDbTask(id));
    }

    /**
     * 任务列表
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/task/searchList")
    public R<Page<ImageTaskSearchResVo>> searchTaskList(@RequestBody @Validated PageReqVo<ImageTaskSearchReqVo> reqVo) {
        String userId = RequestDataHandler.getUserId();
        if (StrUtil.isBlank(userId)) {
            return R.success(Page.of(0, 0));
        }
        if (ObjectUtil.isNull(reqVo.getCondition())) {
            reqVo.setCondition(new ImageTaskSearchReqVo());
        }
        reqVo.getCondition().setUserId(userId);
        return R.success(imageService.searchTaskList(reqVo));
    }


    /**
     * 画廊列表
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/gallery/searchList")
    public R<Page<ImageGallerySearchResVo>> searchGalleryList(@RequestBody @Validated PageReqVo reqVo) {
        return R.success(imageService.searchGalleryList(reqVo));
    }


    /**
     * 切换公开/私有
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/public/change")
    public R<Void> changePublic(@RequestBody @Validated ImagePublicChangeReqVo reqVo) {
        imageService.changePublic(reqVo);
        return R.success();
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/task/delete")
    public R<Void> deleteTask(@RequestParam String id) {
        imageService.deleteTask(id);
        return R.success();
    }

    /**
     * 保存更新自定义配置
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/prompt/config")
    public R<Void> configPrompt(@RequestBody @Validated ImagePromptConfigReqVo reqVo) {
        promptService.config(reqVo);
        return R.success();
    }


    /**
     * 自定义配置列表
     *
     * @param reqVo
     * @return
     */
    @PostMapping(value = "/prompt/searchList")
    public R<Page<JSONObject>> searchPromptList(@RequestBody @Validated PageReqVo<ImagePromptSearchReqVo> reqVo) {
        return R.success(promptService.searchList(reqVo));
    }

    /**
     * 删除配置
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/prompt/remove")
    public R<Void> removePrompt(@RequestParam Long id) {
        promptService.removeById(id);
        return R.success();
    }

    /**
     * 获取配置
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/prompt/info")
    public R<JSONObject> getPromptInfo(@RequestParam Long id, @RequestParam Boolean isPublic) {
        return R.success(promptService.getConfigJson(isPublic, id));
    }

    /**
     * Midjourney绘画服务回调
     * @param task
     * @return
     */
    @PostMapping("notify")
    public R<Void> notifyHook(@RequestBody Task task){
        imageService.notifyHook(task);
        return R.success();
    }

}
