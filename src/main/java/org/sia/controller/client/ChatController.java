package org.sia.controller.client;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.sia.model.ChatMessage;
import org.sia.model.ChatSession;
import org.sia.service.AiProxyService;
import org.sia.service.ChatMessageService;
import org.sia.service.ChatModelService;
import org.sia.service.ChatSessionService;
import org.sia.vo.PageReqVo;
import org.sia.vo.R;
import org.sia.vo.request.*;
import org.sia.vo.response.ChatMessageResVo;
import org.sia.vo.response.ChatModelShowResVo;
import org.sia.vo.response.ChatSessionListResVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/10 17:10
 */
@RestController
@RequestMapping("v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatSessionService sessionService;
    private final ChatMessageService messageService;
    private final ChatModelService modelService;
    private final AiProxyService aiProxyService;

    /**
     * 余额不足检查
     *
     * @return
     */
    @GetMapping("quota/check")
    public R<Void> checkQuota(@RequestParam String model) {
        messageService.checkQuota(model);
        return R.success();
    }

    /**
     * 对话列表
     *
     * @return
     */
    @GetMapping("session/list")
    public R<List<ChatSessionListResVo>> sessionList(@RequestParam(value = "keyword", required = false) String keyword) {
        return R.success(sessionService.getSessionList(keyword));
    }


    /**
     * 置顶对话
     *
     * @return
     */
    @GetMapping("session/pinned")
    public R<Void> pinned(@RequestParam Long id,@RequestParam Integer isPinned) {
        sessionService.pinned(id,isPinned);
        return R.success();
    }


    /**
     * 新建对话
     *
     * @return
     */
    @PostMapping("session/create")
    public R<Long> createSession(@RequestBody(required = false) Map<String,Long> body) {
        ChatSession session = sessionService.createSession(body);
        messageService.initHeaderMsg(session.getId());
        return R.success(session.getId());
    }

    /**
     * 编辑对话
     *
     * @return
     */
    @PostMapping("session/update")
    public R<String> updateSession(@RequestBody @Validated ChatSessionReqVo reqVo) {
        sessionService.updateSession(reqVo);
        return R.success();
    }

    /**
     * 删除对话
     *
     * @return
     */
    @GetMapping("session/delete")
    public R<String> deleteSession(@RequestParam Long id) {
        // 清空会话记录
        messageService.remove(Wrappers.<ChatMessage>lambdaQuery().eq(ChatMessage::getSessionId, id));
        // 删除会话
        sessionService.removeById(id);
        return R.success();
    }


    /**
     * 清空对话记录
     *
     * @return
     */
    @GetMapping("/session/clear")
    public R<String> clearSession(@RequestParam Long id) {
        messageService.remove(Wrappers.<ChatMessage>lambdaQuery().eq(ChatMessage::getSessionId, id));
        return R.success();
    }


    /**
     * 删除对话记录
     *
     * @return
     */
    @GetMapping("/delete")
    public R<String> deleteChat(@RequestParam Long id) {
        messageService.removeById(id);
        return R.success();
    }

    /**
     * 对话记录
     *
     * @param reqVo
     * @return
     */
    @PostMapping("messageList")
    public R<Page<ChatMessageResVo>> messageList(@RequestBody PageReqVo<ChatMessageReqVo> reqVo) {
        return R.success(messageService.getList(reqVo));
    }

    /**
     * topN对话记录
     *
     * @param sessionId
     * @param limit
     * @return
     */
    @GetMapping("message/topList")
    public R<List<ChatMessageResVo>> topMessageList(@RequestParam Long sessionId,@RequestParam Long limit) {
        return R.success(messageService.topList(sessionId,limit));
    }

    /**
     * 上拉加载对话/下拉刷新
     *
     * @param reqVo
     * @return
     */
    @PostMapping("message/load")
    public R<List<ChatMessageResVo>> loadMessage(@RequestBody @Validated ChatMessageLoadReqVo reqVo) {
        return R.success(messageService.load(reqVo));
    }

    /**
     * 流式对话(消耗积分)
     *
     * @param reqVo
     * @return
     */
    @PostMapping("completions")
    public SseEmitter completions(@RequestBody @Validated ChatCompletionReqVo reqVo) {
        return messageService.completions(reqVo);
    }

    /**
     * 流式对话(消耗积分)
     *
     * @param reqVo
     * @return
     */
    @PostMapping("aiproxy")
    public SseEmitter aiproxy(@RequestBody @Validated AiProxyChatReqVo reqVo) {
        return aiProxyService.chat(reqVo);
    }

    /**
     * 可用模型列表
     * @return
     */
    @GetMapping("model/list")
    public R<List<ChatModelShowResVo>> showModelList() {
        return R.success(modelService.showList());
    }



}
