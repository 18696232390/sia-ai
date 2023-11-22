package org.sia.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ConfigKeyEnum;
import org.sia.vo.AdminSettingBaseInfoVo;
import org.sia.vo.AdminSettingStyleInfoVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/14 22:58
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SeoService implements InitializingBean {

    private final ConfigService configService;
    private final String META_DESCRIPTION = "<meta name=\"description\" content=\"{}\">";
    private final String META_KEYWORD = "<meta name=\"keywords\" content=\"{}\">";
    private final String STAT_CODE = "{}</head>";

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initSeo();
    }


    public void initSeo() {
        log.info(">>> 初始化SEO");
        AdminSettingBaseInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_BASE).toBean(AdminSettingBaseInfoVo.class);
        String frontPath = config.getFrontPath();
        if (StrUtil.isBlank(frontPath)) {
            return;
        }
        // SEO
        String description = config.getDescription();
        String keyword = config.getKeyword();
        String indexFile = StrUtil.format("{}/index.html", frontPath);
        String content;
        try {
            content = FileUtil.readUtf8String(indexFile);
        }catch (Exception ex){
            log.error(">>> 获取前端项目路径异常",ex);
            return;
        }

        if (StrUtil.isNotBlank(description)){
            content = ReUtil.replaceAll(content, StrUtil.format(META_DESCRIPTION, "(.*?)"), StrUtil.format(META_DESCRIPTION, description));
        }
        if (StrUtil.isNotBlank(keyword)){
            content = ReUtil.replaceAll(content, StrUtil.format(META_KEYWORD, "(.*?)"), StrUtil.format(META_KEYWORD, keyword));
        }
        // 流量统计
        AdminSettingStyleInfoVo styleConfig = configService.getConfigObject(ConfigKeyEnum.SETTING_STYLE).toBean(AdminSettingStyleInfoVo.class);
        String code = styleConfig.getHeaderPvCode();
        if (StrUtil.isNotBlank(code)) {
            content = ReUtil.replaceAll(content, StrUtil.format(STAT_CODE, ""), StrUtil.format(STAT_CODE, code));
        }
        FileUtil.writeUtf8String(content, indexFile);
    }
}
