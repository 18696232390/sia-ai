package org.sia.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.AbstractStreamListener;
import com.plexpt.chatgpt.util.SseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.bdqf.QfGPTStream;
import org.sia.bdqf.QfGptCompletion;
import org.sia.bdqf.QfGptListener;
import org.sia.bdqf.QfGptStreamListener;
import org.sia.enums.ChatPlatformEnum;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.listener.ChatGptStreamListener;
import org.sia.mapper.ChatMessageMapper;
import org.sia.model.*;
import org.sia.spark.SparkChatRequestParam;
import org.sia.spark.SparkGPTStream;
import org.sia.spark.SparkGptListener;
import org.sia.spark.SparkGptStreamListener;
import org.sia.vo.*;
import org.sia.vo.request.*;
import org.sia.vo.response.AdminSessionMessageResVo;
import org.sia.vo.response.ChatMessageResVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/9 21:12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> implements InitializingBean {

    private final ChatSessionService sessionService;
    private final AppInfoService appInfoService;
    private final UserPackageService userPackageService;
    private final ConfigService configService;
    private final ChatModelService modelService;
    private final ChatAccountService accountService;
    private final ContentSafeService contentSafeService;

    private List<SparkGPTStream> sparkGPTStreamList = Lists.newArrayList();
    private List<QfGPTStream> qfGPTStreamList = Lists.newArrayList();
    private Map<ChatPlatformEnum, List<ChatGPTStream>> chatGPTMap = Maps.newHashMap();

    private ChatGPTStream getChatGPT(ChatPlatformEnum platformEnum) {
        List<ChatGPTStream> chatGPTStreamList = chatGPTMap.get(platformEnum);
        if (CollUtil.isEmpty(chatGPTStreamList)) {
            throw new BusinessException(ResultEnum.ERROR, "AI对话服务正在维护中");
        }
        ChatGPTStream stream  = chatGPTStreamList.get(RandomUtil.randomInt(chatGPTStreamList.size()));
        log.info(">>> GPT API:{} KEY:{}", stream.getApiHost(),stream.getApiKey());
        return stream;
    }

    private SparkGPTStream getSparkGPT() {
        int size = this.sparkGPTStreamList.size();
        if (size == 0) {
            throw new BusinessException(ResultEnum.ERROR, "AI对话服务正在维护中");
        }
        return this.sparkGPTStreamList.get(RandomUtil.randomInt(size));
    }

    private QfGPTStream getQfGPT() {
        int size = this.qfGPTStreamList.size();
        if (size == 0) {
            throw new BusinessException(ResultEnum.ERROR, "AI对话服务正在维护中");
        }
        return this.qfGPTStreamList.get(RandomUtil.randomInt(size));
    }

    @Override
    public void afterPropertiesSet() {
        this.initChatGptConfig();
    }

    /**
     * 动态加载GPT配置
     */
    public void initChatGptConfig() {
        List<ChatAccount> accountList = accountService.list(Wrappers.<ChatAccount>lambdaQuery().eq(ChatAccount::getIsValid, 1));
        if (CollUtil.isEmpty(accountList)) {
            log.warn(">>>> AI对话账号池为空,请及时补充 <<<<");
            return;
        }
        log.warn(">>>> AI对话账号池初始化 <<<<");
        sparkGPTStreamList.clear();
        qfGPTStreamList.clear();
        chatGPTMap.clear();
        for (ChatPlatformEnum value : ChatPlatformEnum.values()) {
            chatGPTMap.put(value, Lists.newArrayList());
        }
        JSONObject config = configService.getConfigObject(ConfigKeyEnum.CHAT_BASE);
        for (ChatAccount chatAccount : accountList) {
            if (ChatPlatformEnum.XFYUN.getKey().equals(chatAccount.getPlatform())) {
                List<String> split = StrUtil.split(chatAccount.getKey(), '-');
                sparkGPTStreamList.add(SparkGPTStream.builder()
                        .timeout(600)
                        .appid(split.get(0))
                        .apiKey(split.get(1))
                        .apiSecret(split.get(2))
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.BAIDU.getKey().equals(chatAccount.getPlatform())) {
                List<String> split = StrUtil.split(chatAccount.getKey(), '-');
                qfGPTStreamList.add(QfGPTStream.builder()
                        .timeout(600)
                        .apiKey(split.get(0))
                        .apiSecret(split.get(1))
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.OTHER.getKey().equals(chatAccount.getPlatform())) {
                String domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()));
                chatGPTMap.get(ChatPlatformEnum.OTHER).add(ChatGPTStream.builder()
                        .timeout(600)
                        .apiKey(chatAccount.getKey())
                        .apiHost(domain)
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.OPENAI.getKey().equals(chatAccount.getPlatform())) {
                String domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()));
                if (StrUtil.isBlank(domain)) {
                    domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()) + "Proxy");
                }
                chatGPTMap.get(ChatPlatformEnum.OPENAI).add(ChatGPTStream.builder()
                        .timeout(600)
                        .apiKey(chatAccount.getKey())
                        .apiHost(domain)
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.API2D.getKey().equals(chatAccount.getPlatform())) {
                String domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()));
                if (StrUtil.isBlank(domain)) {
                    domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()) + "Proxy");
                }
                chatGPTMap.get(ChatPlatformEnum.API2D).add(ChatGPTStream.builder()
                        .timeout(600)
                        .apiKey(chatAccount.getKey())
                        .apiHost(domain)
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.OPENAI_SB.getKey().equals(chatAccount.getPlatform())) {
                String domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()));
                if (StrUtil.isBlank(domain)) {
                    domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()) + "Proxy");
                }
                chatGPTMap.get(ChatPlatformEnum.OPENAI_SB).add(ChatGPTStream.builder()
                        .timeout(600)
                        .apiKey(chatAccount.getKey())
                        .apiHost(domain)
                        .build()
                        .init());
            }
            if (ChatPlatformEnum.ONEAPI.getKey().equals(chatAccount.getPlatform())) {
                String domain = config.getStr(ChatPlatformEnum.getDomainKey(chatAccount.getPlatform()));
                chatGPTMap.get(ChatPlatformEnum.ONEAPI).add(ChatGPTStream.builder()
                        .timeout(600)
                        .apiKey(chatAccount.getKey())
                        .apiHost(domain)
                        .build()
                        .init());
            }
        }
        log.info(">>> GPT对话账号池:{}", JSONUtil.toJsonPrettyStr(chatGPTMap));
        log.info(">>> 星火对话账号池:{}", JSONUtil.toJsonPrettyStr(sparkGPTStreamList));
        log.info(">>> 千帆对话账号池:{}", JSONUtil.toJsonPrettyStr(qfGPTStreamList));

    }

    public Page<ChatMessageResVo> getList(PageReqVo<ChatMessageReqVo> reqVo) {
        if (ObjectUtils.isNull(reqVo.getCondition()) || ObjectUtils.isNull(reqVo.getCondition().getSessionId())) {
            throw new BusinessException(ResultEnum.ERROR, "请填写对话ID");
        }
        reqVo.getCondition().setUserId(RequestDataHandler.getUserId());
        return this.baseMapper.pageList(reqVo.getPage(), reqVo.getCondition());
    }

    /**
     * 流式对话
     *
     * @param reqVo
     * @return
     */
    public SseEmitter completions(ChatCompletionReqVo reqVo) {
        if (ObjectUtil.isNull(reqVo.getSessionId()) && ObjectUtil.isNull(reqVo.getAppId())) {
            throw new BusinessException(ResultEnum.ERROR, "会话ID或应用ID不能为空");
        }

        if (ObjectUtil.isNotNull(reqVo.getSessionId())) {
            return this.completionSession(reqVo.getSessionId(), reqVo.getText());
        } else {
            return this.completionApp(reqVo.getAppId(), reqVo.getText());
        }
    }

    private void completionByChatGPT(ChatPlatformEnum platform, ChatCompletion param, Consumer<String> consumer) {
        AbstractStreamListener listener = new AbstractStreamListener() {
            @Override
            public void onMsg(String message) {
            }

            @Override
            public void onError(Throwable throwable, String response) {
            }
        };
        // 对话结束回调
        listener.setOnComplate(consumer);
        // 配置请求信息
        this.getChatGPT(platform).streamChatCompletion(
                param,
                listener);
    }

    private static Map<String,SseEmitter> sseMap = MapUtil.newConcurrentHashMap();

    private SseEmitter completionSessionByChatGPT(ChatPlatformEnum platformEnum, ChatSession session, String text) {
        String userId = RequestDataHandler.getUserId();
        SseEmitter sseEmitter = sseMap.getOrDefault(userId,new SseEmitter(-1L));
        ChatGptStreamListener listener = new ChatGptStreamListener(sseEmitter);
        Map<Long, Long> temp = Maps.newHashMap();
        ChatMessage message = new ChatMessage();
        // 建立连接后检查
        listener.setOnOpen(eventSource -> {
            if (!contentSafeService.hasSafeText(text)){
                throw new BusinessException(ResultEnum.ERROR,"安全检测:存在敏感词,请检查");
            }
            this.costIntegral(session, message, userId, temp, text);
        });
        // 异常处理
        listener.setOnError(error -> {
            // 退还积分
            log.error(">>> 对话异常:{}", error.getMessage());
            this.backIntegral(temp, message, error);
        });
        // 对话结束回调
        listener.setOnComplate(msg -> {
            // 回答完成，可以做一些事情
            SseHelper.send(sseEmitter, "[DONE]");
            message.setAiText(msg);
            message.setUpdateTime(LocalDateTime.now());
            this.updateById(message);
        });

        List<Message> messageList = StrUtil.isNotEmpty(session.getRole()) ? Lists.newArrayList(Message.ofSystem(session.getRole())) : Lists.newArrayList();
        // 支持上下文对话数
        if (session.getContext() > 0) {
            this.baseMapper.getContextList(session.getId(), NumberUtil.round(NumberUtil.div(session.getContext().intValue(), 2), 0, RoundingMode.UP).intValue())
                    .forEach(chatMessage -> {
                        String userText = chatMessage.getUserText();
                        String aiText = chatMessage.getAiText();
                        if (StrUtil.isNotBlank(userText)) {
                            messageList.add(Message.of(userText));
                        }
                        if (StrUtil.isNotBlank(aiText)) {
                            messageList.add(Message.ofAssistant(aiText));
                        }
                    });
        }
        messageList.add(Message.of(text));
        Integer maxToken = session.getMaxToken() == 0 ? null : session.getMaxToken();
        // 自动设置会话标题
        ThreadUtil.execute(() -> this.autoGenerateTitle(session));

        // 配置请求信息
        this.getChatGPT(platformEnum).streamChatCompletion(
                ChatCompletion
                        .builder()
                        // 对话模型
                        .model(session.getModel())
                        // 角色预设&用户问题&支持上下文
                        .messages(messageList)
                        // 最大支持token
                        .maxTokens(maxToken)
                        // 随机性 越大越随机 范围：0～2
                        .temperature(session.getRandom())
                        // 话题新鲜度 越大话题越新  范围：-2～2
                        .presencePenalty(session.getFresh())
                        // 重复度 越大重复内容越少 范围：-2～2
                        .frequencyPenalty(session.getRepeat())
                        .build(),
                listener);
        return sseEmitter;
    }

    private void costIntegral(ChatSession session, ChatMessage message, String userId, Map<Long, Long> temp, String text) {
        if (ObjectUtil.isNull(session)) {
            throw new BusinessException(ResultEnum.DATA_EMPTY, "对话");
        }
        // 查询Model扣除的积分
        Long quota = modelService.getCostQuota(session.getModel());
        log.info(">>> 模型:{},积分:{}", session.getModel(), quota);
        // 扣除积分额度
        userPackageService.decrease(userId, quota, temp);
        // 保存消息记录
        message.setUserId(userId);
        message.setModel(session.getModel());
        message.setSessionId(session.getId());
        message.setCreateTime(LocalDateTime.now());
        message.setUserText(text);
        message.setAiText("正在思考中...");
        message.setCost(quota.intValue());
        message.setBack(0);
        this.save(message);
    }

    private void backIntegral(Map<Long, Long> temp, ChatMessage message, Throwable ex) {
        if (MapUtil.isEmpty(temp)) {
            return;
        }
        userPackageService.increase(temp);
        message.setBack(temp.values().stream().mapToInt(Long::intValue).sum());
        message.setAiText(ex.getMessage());
        message.setUpdateTime(LocalDateTime.now());
        this.updateById(message);
    }


    private SseEmitter completionSession(Long sessionId, String text) {
        // 查询会话配置
        ChatSession session = sessionService.getById(sessionId);
        // 根据模型获取平台信息
        ChatPlatformEnum platform = accountService.getPlatform(session.getModel());
        log.info(">>> GPT模型:{}",platform.getValue());
        if (ChatPlatformEnum.XFYUN.equals(platform)) {
            return this.completionSessionBySpark(session, text);
        } else if (ChatPlatformEnum.BAIDU.equals(platform)) {
            return this.completionSessionByQf(session, text);
        } else {
            return this.completionSessionByChatGPT(platform, session, text);
        }
    }

    private void completionByQf(QfGptCompletion param, Consumer<String> consumer) {
        QfGptListener listener = new QfGptListener();
        // 对话结束回调
        listener.setOnComplate(consumer);
        this.getQfGPT().streamChatCompletion(
                param,
                listener);
    }

    private SseEmitter completionSessionByQf(ChatSession session, String text) {
        String userId = RequestDataHandler.getUserId();
        SseEmitter sseEmitter = sseMap.getOrDefault(userId,new SseEmitter(-1L));
        QfGptStreamListener listener = new QfGptStreamListener(sseEmitter);
        Map<Long, Long> temp = Maps.newHashMap();
        ChatMessage message = new ChatMessage();
        // 建立连接后检查
        listener.setOnOpen(webSocket -> {
            if (!contentSafeService.hasSafeText(text)){
                throw new BusinessException(ResultEnum.ERROR,"安全检测:存在敏感词,请检查");
            }
            this.costIntegral(session, message, userId, temp, text);
        });
        // 异常处理
        listener.setOnError(error -> {
            // 退还积分
            log.error(">>> 对话异常:{}", error.getMessage());
            this.backIntegral(temp, message, error);
        });
        // 对话结束回调
        listener.setOnComplate(msg -> {
            log.error(">>> 对话结束回调:{}", msg);
            // 回答完成，可以做一些事情
            SseHelper.send(sseEmitter, "[DONE]");
            message.setAiText(msg);
            message.setUpdateTime(LocalDateTime.now());
            this.updateById(message);
        });

        List<QfGptCompletion.Message> messageList = StrUtil.isNotEmpty(session.getRole()) ? ListUtil.of(QfGptCompletion.Message.ofUser(session.getRole())) : Lists.newArrayList();
        // 支持上下文对话数
        if (session.getContext() > 0) {
            this.baseMapper.getContextList(session.getId(), NumberUtil.round(NumberUtil.div(session.getContext().intValue(), 2), 0, RoundingMode.UP).intValue())
                    .stream()
                    .filter(chatMessage -> StrUtil.isNotBlank(chatMessage.getModel()))
                    .forEach(chatMessage -> {
                        messageList.add(QfGptCompletion.Message.ofUser(chatMessage.getUserText()));
                        messageList.add(QfGptCompletion.Message.ofAssistant(chatMessage.getAiText()));
                    });
        }
        messageList.add(QfGptCompletion.Message.ofUser(text));
        // 自动设置会话标题
        ThreadUtil.execute(() -> this.autoGenerateTitle(session));

        ThreadUtil.execute(() -> {
            ThreadUtil.safeSleep(1000);
            // 配置请求信息
            this.getQfGPT().streamChatCompletion(
                    QfGptCompletion
                            .builder()
                            .userId(userId)
                            // 对话模型
                            .model(session.getModel())
                            // 角色预设&用户问题&支持上下文
                            .messages(messageList)
                            // 随机性 越大越随机 范围：0～2
                            .temperature(session.getRandom())
                            // 取值范围：[1.0, 2.0]
                            .penaltyScore(session.getRepeat() < 1 ? 1 : session.getRepeat() > 2 ? 2 : session.getRepeat())
                            .build(),
                    listener);
        });
        return sseEmitter;
    }


    private void completionBySpark(SparkChatRequestParam param, Consumer<String> consumer) {
        SparkGptListener listener = new SparkGptListener();
        // 对话结束回调
        listener.setOnComplate(consumer);
        // 配置请求信息
        this.getSparkGPT().streamChatCompletion(
                param,
                listener);
    }

    private SseEmitter completionSessionBySpark(ChatSession session, String text) {
        String userId = RequestDataHandler.getUserId();
        SseEmitter sseEmitter = sseMap.getOrDefault(userId,new SseEmitter(-1L));
        SparkGptStreamListener listener = new SparkGptStreamListener(sseEmitter);
        Map<Long, Long> temp = Maps.newHashMap();
        ChatMessage message = new ChatMessage();
        // 建立连接后检查
        listener.setOnOpen(webSocket -> {
            if (!contentSafeService.hasSafeText(text)){
                throw new BusinessException(ResultEnum.ERROR,"安全检测:存在敏感词,请检查");
            }
            this.costIntegral(session, message, userId, temp, text);
        });
        // 异常处理
        listener.setOnError(error -> {
            // 退还积分
            log.error(">>> 对话异常:{}", error.getMessage());
            this.backIntegral(temp, message, error);
        });
        // 对话结束回调
        listener.setOnComplate(msg -> {
            log.error(">>> 对话结束回调:{}", msg);
            // 回答完成，可以做一些事情
            SseHelper.send(sseEmitter, "[DONE]");
            message.setAiText(msg);
            message.setUpdateTime(LocalDateTime.now());
            this.updateById(message);
        });

        List<SparkChatRequestParam.Message> messageList = StrUtil.isNotEmpty(session.getRole()) ? ListUtil.of(SparkChatRequestParam.Message.ofUser(session.getRole())) : Lists.newArrayList();
        // 支持上下文对话数
        if (session.getContext() > 0) {
            this.baseMapper.getContextList(session.getId(), NumberUtil.round(NumberUtil.div(session.getContext().intValue(), 2), 0, RoundingMode.UP).intValue())
                    .stream()
                    .filter(chatMessage -> StrUtil.isNotBlank(chatMessage.getModel()))
                    .forEach(chatMessage -> {
                        String userText = chatMessage.getUserText();
                        String aiText = chatMessage.getAiText();
                        if (StrUtil.isNotBlank(userText)) {
                            messageList.add(SparkChatRequestParam.Message.ofUser(userText));
                        }
                        if (StrUtil.isNotBlank(aiText)) {
                            messageList.add(SparkChatRequestParam.Message.ofAssistant(aiText));
                        }
                    });
        }
        messageList.add(SparkChatRequestParam.Message.ofUser(text));
        Integer maxToken = session.getMaxToken() == 0 ? null : session.getMaxToken();
        // 自动设置会话标题
        ThreadUtil.execute(() -> this.autoGenerateTitle(session));


        // 配置请求信息
        this.getSparkGPT().streamChatCompletion(
                SparkChatRequestParam
                        .builder()
                        .uid(userId)
                        .chatId(session.getId().toString())
                        // 对话模型
                        .domain(session.getModel())
                        // 角色预设&用户问题&支持上下文
                        .messageList(messageList)
                        // 最大支持token
                        .maxTokens(maxToken)
                        // 随机性 越大越随机 范围：0～2
                        .temperature(session.getRandom())
                        .build(),
                listener);
        return sseEmitter;
    }


    private SseEmitter completionApp(Long appId, String text) {
        String userId = RequestDataHandler.getUserId();
        SseEmitter sseEmitter = sseMap.getOrDefault(userId,new SseEmitter(-1L));

        // 查询应用配置
        AppInfo appInfo = appInfoService.getById(appId);
        Map<Long, Long> temp = Maps.newHashMap();
        ChatGptStreamListener listener = new ChatGptStreamListener(sseEmitter);
        listener.setOnOpen(eventSource -> {
            if (ObjectUtil.isNull(appInfo)) {
                throw new BusinessException(ResultEnum.DATA_EMPTY, "应用");
            }
            // 查询Model扣除的积分
            Long quota = modelService.getCostQuota(appInfo.getModel());
            // 扣除积分额度
            userPackageService.decrease(userId, quota, temp);
        });
        // 对话结束回调
        listener.setOnComplate(msg -> {
            // 回答完成，可以做一些事情
            SseHelper.send(sseEmitter, "[DONE]");
        });
        // 异常处理
        listener.setOnError(error -> {
            // 退还积分
            log.error(">>> 对话异常:{}", error.getMessage());
            userPackageService.increase(temp);
        });
        ChatPlatformEnum platform = accountService.getPlatform(appInfo.getModel());
        this.getChatGPT(platform).streamChatCompletion(
                ChatCompletion
                        .builder()
                        // 对话模型
                        .model(appInfo.getModel())
                        // 角色预设&用户问题&支持上下文
                        .messages(Lists.newArrayList(
                                Message.ofSystem(appInfo.getPrompt()),
                                Message.of(text)))
                        // 最大支持token
                        .maxTokens(appInfo.getMaxLength().intValue())
                        // 随机性 越大越随机 范围：0～2
                        .temperature(appInfo.getTemperature())
                        // 话题新鲜度 越大话题越新  范围：-2～2
                        .presencePenalty(appInfo.getPresencePenalty())
                        // 重复度 越大重复内容越少 范围：-2～2
                        .frequencyPenalty(appInfo.getFrequencyPenalty())
                        .build(),
                listener);
        return sseEmitter;
    }


    public Page<AdminSessionMessageResVo> searchMessageList(PageReqVo<AdminSessionMessageSearchReqVo> reqVo) {
        return this.baseMapper.searchList(reqVo.getPage(), reqVo.getCondition());
    }


    public void edit(AdminSessionMessageEditReqVo reqVo) {
        ChatMessage message = BeanUtil.toBean(reqVo, ChatMessage.class);
        message.setUpdateTime(LocalDateTime.now());
        this.updateById(message);
    }

    public void testGPT(String model) {

    }

    public void initHeaderMsg(Long sessionId) {
        // 初始化欢迎语
        ChatMessage message = new ChatMessage();
        message.setUserId(RequestDataHandler.getUserId());
        message.setSessionId(sessionId);
        message.setCreateTime(LocalDateTime.now());
        AdminChatBaseInfoVo config = configService.getConfigObject(ConfigKeyEnum.CHAT_BASE).toBean(AdminChatBaseInfoVo.class);
        message.setAiText(config.getDefaultMsg());
        this.save(message);
    }

    public List<ChatMessageResVo> load(ChatMessageLoadReqVo reqVo) {
        reqVo.setUserId(RequestDataHandler.getUserId());
        List<ChatMessageResVo> messageList = this.baseMapper.load(reqVo);
        messageList.sort(Comparator.comparing(ChatMessageResVo::getCreateTime));
        return messageList;
    }

    public List<ChatMessageResVo> topList(Long sessionId, Long size) {
        List<ChatMessageResVo> messageList = this.baseMapper.topList(sessionId, size, RequestDataHandler.getUserId());
        messageList.sort(Comparator.comparing(ChatMessageResVo::getCreateTime));
        return messageList;
    }

    /**
     * 自动生成会话标题
     */
    private void autoGenerateTitle(ChatSession session) {
        // 对话标题是否需要自动生成
        if (!"新对话".equals(session.getTitle())) {
            return;
        }
        // 自动生成开关是否打开
        AdminChatBaseInfoVo config = configService.getConfigObject(ConfigKeyEnum.CHAT_BASE).toBean(AdminChatBaseInfoVo.class);
        if (config.getAutoTitle() == 0) {
            return;
        }
        // 对话问答记录>=3次
        long count = this.count(Wrappers.<ChatMessage>lambdaQuery()
                .eq(ChatMessage::getSessionId, session.getId())
                .isNotNull(ChatMessage::getModel));
        if (count < 3) {
            return;
        }
        // 最近三条对话记录
        List<ChatMessage> messageList = this.list(Wrappers.<ChatMessage>lambdaQuery()
                .eq(ChatMessage::getSessionId, session.getId())
                .isNotNull(ChatMessage::getModel));

        // 获取模型对应的平台
        ChatPlatformEnum platform = accountService.getPlatform(config.getModel());

        // 对话结束回调函数
        Consumer<String> consumer = (msg) -> {
            log.info(">>>> 自动生成会话标题:{}", msg);
            session.setTitle(StrUtil.subPre(msg,14));
            sessionService.updateById(session);
        };
        // 标题生成提示词
        AdminChatPreSetInfoVo chatPresetConfig = configService.getConfigObject(ConfigKeyEnum.CHAT_PRESET).toBean(AdminChatPreSetInfoVo.class);

        // 调用completion
        if (ChatPlatformEnum.XFYUN.equals(platform)) {
            List<SparkChatRequestParam.Message> messages = messageList.stream()
                    .flatMap(message -> Lists.newArrayList(
                                    SparkChatRequestParam.Message.ofUser(message.getUserText()),
                                    SparkChatRequestParam.Message.ofAssistant(message.getAiText()))
                            .stream())
                    .collect(Collectors.toList());
            messages.add(SparkChatRequestParam.Message.ofUser(chatPresetConfig.getTitlePrompt()));
            SparkChatRequestParam param = SparkChatRequestParam.builder()
                    .domain(config.getModel())
                    .messageList(messages)
                    .build();
            this.completionBySpark(param, consumer);
        } else if (ChatPlatformEnum.BAIDU.equals(platform)) {
            List<QfGptCompletion.Message> messages = messageList.stream()
                    .flatMap(message -> Lists.newArrayList(
                                    QfGptCompletion.Message.ofUser(message.getUserText()),
                                    QfGptCompletion.Message.ofAssistant(message.getAiText()))
                            .stream())
                    .collect(Collectors.toList());
            messages.add(QfGptCompletion.Message.ofUser(chatPresetConfig.getTitlePrompt()));
            this.completionByQf(
                    QfGptCompletion.builder()
                            .model(config.getModel())
                            .messages(messages)
                            .build(),
                    consumer
            );
        } else {
            List<Message> messages = messageList.stream()
                    .flatMap(message -> Lists.newArrayList(
                                    Message.of(message.getUserText()),
                                    Message.ofAssistant(message.getAiText()))
                            .stream())
                    .collect(Collectors.toList());
            messages.add(Message.of(chatPresetConfig.getTitlePrompt()));
            ChatCompletion param = ChatCompletion
                    .builder()
                    // 对话模型
                    .model(config.getModel())
                    // 角色预设&用户问题&支持上下文
                    .messages(messages)
                    .build();
            log.info(">>> 自动生成会话标题参数:{}", JSONUtil.toJsonPrettyStr(param));
            this.completionByChatGPT(platform, param, consumer);
        }
    }

    public void checkQuota(String model) {
        // 查询Model扣除的积分
        Long quota = modelService.getCostQuota(model);
        log.info(">>> 模型:{},积分:{}", model, quota);
        // 查询用户可用额度
        List<UserPackage> upList = userPackageService.getBaseMapper().getQuotaPkg(RequestDataHandler.getUserId(), quota, DateUtil.now());
        // 套餐包额度已用尽
        if (CollUtil.isEmpty(upList) || upList.stream().mapToLong(UserPackage::getRemain).sum() < quota) {
            AdminSettingPromptInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_PROMPT).toBean(AdminSettingPromptInfoVo.class);
            throw new BusinessException(ResultEnum.GPT_NOT_PKG,config.getQuoteTip());
        }
    }
}
