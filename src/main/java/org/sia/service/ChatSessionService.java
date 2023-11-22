package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.sia.enums.ChatPlatformEnum;
import org.sia.enums.ConfigKeyEnum;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.ChatSessionMapper;
import org.sia.model.AppInfo;
import org.sia.model.ChatMessage;
import org.sia.model.ChatSession;
import org.sia.vo.AdminChatBaseInfoVo;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.AdminSessionEditReqVo;
import org.sia.vo.request.AdminSessionMessageSearchReqVo;
import org.sia.vo.request.AdminSessionSearchReqVo;
import org.sia.vo.request.ChatSessionReqVo;
import org.sia.vo.response.AdminSessionMessageResVo;
import org.sia.vo.response.AdminSessionResVo;
import org.sia.vo.response.ChatSessionListResVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionService extends ServiceImpl<ChatSessionMapper, ChatSession> {

    private final AppInfoService appInfoService;


    /**
     * 当前登录用户会话列表
     * @return
     */
    public List<ChatSessionListResVo> getSessionList(String keyword) {
        return this.baseMapper.getSessionList(keyword, RequestDataHandler.getUserId());
    }

    /**
     * 新建对话
     */
    public ChatSession createSession(Map<String,Long> body) {
        if (ObjectUtil.isNotNull(body) && body.containsKey("appId")){
            return this.createSessionByApp(body.get("appId"));
        }
        ChatSession session = new ChatSession();
        session.setTitle("新对话");
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());
        session.setModel(ChatCompletion.Model.GPT_3_5_TURBO.getName());
        session.setUserId(RequestDataHandler.getUserId());
        session.setContext(5);
        session.setFresh(0F);
        session.setMaxToken(0);
        session.setRepeat(0F);
        session.setRandom(1F);
        session.setIsPinned(0);
        this.save(session);
        return session;
    }

    public ChatSession createSessionByApp(Long appId){
        AppInfo app = appInfoService.getById(appId);
        ChatSession session = new ChatSession();
        session.setTitle(app.getName());
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());
        session.setModel(app.getModel());
        session.setUserId(RequestDataHandler.getUserId());
        session.setContext(5);
        session.setFresh(0F);
        session.setMaxToken(0);
        session.setRepeat(0F);
        session.setRandom(1F);
        session.setIsPinned(0);
        session.setRole(app.getPrompt());
        this.save(session);
        return session;
    }


    public void updateSession(ChatSessionReqVo reqVo) {
        ChatSession session = BeanUtil.toBean(reqVo, ChatSession.class);
        session.setUpdateTime(LocalDateTime.now());
        session.setUserId(RequestDataHandler.getUserId());
        this.updateById(session);
    }


    public Page<AdminSessionResVo> searchList(PageReqVo<AdminSessionSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(),reqVo.getCondition());
    }

    public void edit(AdminSessionEditReqVo reqVo) {
        ChatSession session = BeanUtil.toBean(reqVo, ChatSession.class);
        session.setUpdateTime(LocalDateTime.now());
        this.updateById(session);
    }

    public AdminSessionResVo info(Long id) {
        ChatSession session = this.getById(id);
        return BeanUtil.toBean(session,AdminSessionResVo.class);
    }

    public void pinned(Long id,Integer isPinned) {
        this.update(Wrappers.<ChatSession>lambdaUpdate()
                .eq(ChatSession::getId,id)
                .set(ChatSession::getIsPinned,isPinned)
        );
    }

}
