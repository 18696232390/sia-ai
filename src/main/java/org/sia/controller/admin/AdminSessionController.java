package org.sia.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.sia.service.*;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:56
 */
@RestController
@RequestMapping("v1/admin/session")
@RequiredArgsConstructor
public class AdminSessionController {

    private final ChatSessionService sessionService;
    private final ChatMessageService messageService;
    private final ChatAccountService configService;
    private final ChatModelService modelService;

    /**
     * 消息列表
     * @return
     */
    @PostMapping("message/searchList")
    public R<Page<AdminSessionMessageResVo>> searchMessageList(@RequestBody @Validated PageReqVo<AdminSessionMessageSearchReqVo> reqVo){
        return R.success(messageService.searchMessageList(reqVo));
    }

    /**
     * 删除消息
     * @return
     */
    @GetMapping("message/remove")
    public R<Void> removeMessage(@RequestParam Long id){
        messageService.removeById(id);
        return R.success();
    }

    /**
     * 编辑消息
     * @return
     */
    @PostMapping("message/edit")
    public R<Void> editMessage(@RequestBody @Validated AdminSessionMessageEditReqVo reqVo){
        messageService.edit(reqVo);
        return R.success();
    }

    /**
     * 会话列表
     * @return
     */
    @PostMapping("searchList")
    public R<Page<AdminSessionResVo>> searchSessionList(@RequestBody @Validated PageReqVo<AdminSessionSearchReqVo> reqVo){
        return R.success(sessionService.searchList(reqVo));
    }

    /**
     * 删除会话
     * @return
     */
    @GetMapping("remove")
    public R<Void> removeSession(@RequestParam Long id){
        sessionService.removeById(id);
        return R.success();
    }

    /**
     * 编辑会话
     * @return
     */
    @PostMapping("edit")
    public R<Void> editSession(@RequestBody @Validated AdminSessionEditReqVo reqVo){
        sessionService.edit(reqVo);
        return R.success();
    }

    /**
     * 会话详情
     * @return
     */
    @GetMapping("info")
    public R<AdminSessionResVo> sessionInfo(@RequestParam Long id){
        return R.success(sessionService.info(id));
    }

    /**
     * 对话平台
     * @return
     */
    @GetMapping("platform/list")
    public R<List<AdminChatPlatformResVo>> platformList(){
        return R.success(configService.platformList());
    }


    /**
     * 对话账号池列表
     * @return
     */
    @PostMapping("account/searchList")
    public R<Page<AdminChatAccountResVo>> searchAccountList(@RequestBody @Validated PageReqVo<AdminChatAccountSearchReqVo> reqVo){
        return R.success(configService.searchList(reqVo));
    }

    /**
     * 对话账号详情
     * @return
     */
    @GetMapping("account/info")
    public R<AdminChatAccountResVo> accountInfo(@RequestParam Long id){
        return R.success(configService.info(id));
    }

    /**
     * 创建对话账号
     * @return
     */
    @PostMapping("account/create")
    public R<Void> createAccount(@RequestBody @Validated AdminChatAccountCreateReqVo reqVo){
        configService.create(reqVo);
        return R.success();
    }


    /**
     * 编辑对话账号
     * @return
     */
    @PostMapping("account/edit")
    public R<Void> editAccount(@RequestBody @Validated AdminChatAccountEditReqVo reqVo){
        configService.edit(reqVo);
        return R.success();
    }

    /**
     * 测试对话账号
     * @return
     */
    @GetMapping("account/test")
    public R<Void> testAccount(@RequestParam Long id){
        configService.test(id);
        return R.success();
    }


    /**
     * 可用变更
     * @return
     */
    @GetMapping("account/updateValid")
    public R<Void> updateValidAccount(@RequestParam Long id){
        configService.updateValid(id);
        return R.success();
    }



    /**
     * 删除对话账号
     * @return
     */
    @GetMapping("account/remove")
    public R<Void> removeAccount(@RequestParam Long id){
        configService.removeById(id);
        return R.success();
    }

    /**
     * 模型列表
     * @return
     */
    @GetMapping("model/list")
    public R<List<AdminChatModelInfoResVO>> modelList(){
        return R.success(modelService.usableList());
    }


}
