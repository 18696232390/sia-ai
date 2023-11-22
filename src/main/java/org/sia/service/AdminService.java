package org.sia.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.model.ChatSession;
import org.sia.model.ImageTask;
import org.sia.model.Order;
import org.sia.model.User;
import org.sia.vo.PageReqVo;
import org.sia.vo.request.ImageTaskSearchReqVo;
import org.sia.vo.response.HomeInfoResVo;
import org.sia.vo.response.ImageServiceInfoResVo;
import org.sia.vo.response.ImageTaskSearchResVo;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/23 16:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final ChatSessionService chatSessionService;
    private final ImageService imageService;
    private final OrderService orderService;

    public HomeInfoResVo info() {
        HomeInfoResVo result = new HomeInfoResVo();
        result.setTotal(MapUtil.builder("user", userService.count())
                .put("chat", chatSessionService.count())
                .put("image", imageService.count())
                .put("order", orderService.count())
                .build());
        DateTime today = DateUtil.date();
        DateTime begin = DateUtil.beginOfDay(today);
        DateTime end = DateUtil.endOfDay(today);
        result.setToady(MapUtil.builder("user", userService.count(Wrappers.<User>lambdaQuery().between(User::getCreateTime,begin,end)))
                .put("chat", chatSessionService.count(Wrappers.<ChatSession>lambdaQuery().between(ChatSession::getCreateTime,begin,end)))
                .put("image", imageService.count(Wrappers.<ImageTask>lambdaQuery().between(ImageTask::getSubmitTime,begin,end)))
                .put("order", orderService.count(Wrappers.<Order>lambdaQuery().between(Order::getCreateTime,begin,end)))
                .build());
        return result;
    }

    public Page<ImageTaskSearchResVo> searchImageList(PageReqVo<ImageTaskSearchReqVo> reqVo) {
        return imageService.searchTaskList(reqVo);
    }

    public void deleteImage(String id) {
        imageService.removeById(id);
    }

    public ImageServiceInfoResVo serviceInfo() {
        return imageService.getServiceInfo();
    }

}
