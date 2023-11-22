package org.sia.controller.client;

import lombok.RequiredArgsConstructor;
import org.sia.service.ChatGPTService;
import org.sia.vo.R;
import org.sia.vo.request.ChatGPTAssistantCreateReqVo;
import org.sia.vo.response.ChatGPTAssistantCreateResVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: ChatGPT相关接口
 * @Author: 高灶顺
 * @CreateDate: 2023/11/8 18:24
 */
@RestController
@RequestMapping("v1/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;


    /**
     * 创建助手
     * @return
     */
    @PostMapping("assistant/create")
    public R<ChatGPTAssistantCreateResVo> createAssistant(@RequestBody ChatGPTAssistantCreateReqVo reqVo){
        return R.success();
    }
}
