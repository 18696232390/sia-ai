package org.sia.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.volcengine.service.visual.IVisualService;
import com.volcengine.service.visual.impl.VisualServiceImpl;
import com.volcengine.service.visual.model.request.ImageStyleConversionRequest;
import com.volcengine.service.visual.model.request.VisualConvertPhotoV2Request;
import com.volcengine.service.visual.model.request.VisualGeneralSegmentRequest;
import com.volcengine.service.visual.model.request.VisualPoemMaterialRequest;
import com.volcengine.service.visual.model.response.ImageStyleConversionResponse;
import com.volcengine.service.visual.model.response.VisualConvertPhotoV2Response;
import com.volcengine.service.visual.model.response.VisualGeneralSegmentResponse;
import com.volcengine.service.visual.model.response.VisualPoemMaterialResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.sia.enums.ConfigKeyEnum;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.sia.handler.RequestDataHandler;
import org.sia.mapper.ToolMapper;
import org.sia.model.AiTool;
import org.sia.vo.AdminSettingToolInfoVo;
import org.sia.vo.response.ToolResVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/21 20:26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ToolService extends ServiceImpl<ToolMapper, AiTool> {

    private final ConfigService configService;

    public List<AiTool> appList() {
        String userId = RequestDataHandler.getUserId();
        return this.baseMapper.appList(userId);
    }

    public List<AiTool> add(Long id) {
        String userId = RequestDataHandler.getUserId();
        if (this.baseMapper.hasTool(userId,id)>0){
            throw new BusinessException(ResultEnum.ERROR,"请勿重复添加应用");
        }
        this.baseMapper.addUser(userId, id);
        return this.appList();
    }

    public List<AiTool> delete(Long id) {
        String userId = RequestDataHandler.getUserId();
        this.baseMapper.deleteUser(userId, id);
        return this.appList();
    }

    public List<ToolResVo> searchList() {
        List<ToolResVo> result = Lists.newArrayList();

        this.list(Wrappers.<AiTool>lambdaQuery().orderByAsc(AiTool::getSort))
                .stream()
                .collect(Collectors.groupingBy(AiTool::getCategory))
                .forEach((k, v) -> {
                    ToolResVo vo = new ToolResVo();
                    vo.setCategory(k);
                    vo.setAppList(v);
                    result.add(vo);
                });
        return result;
    }


    public String generalSegment(VisualGeneralSegmentRequest reqVo) {
        AdminSettingToolInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_TOOL).toBean(AdminSettingToolInfoVo.class);
        IVisualService visualService = VisualServiceImpl.getInstance();
        visualService.setAccessKey(config.getKey());
        visualService.setSecretKey(config.getSecret());
        try {
            reqVo.setReturnForegroundImage(1);
            VisualGeneralSegmentResponse response = visualService.generalSegment(reqVo);
            if (response.getCode() != 10000) {
                throw new BusinessException(ResultEnum.ERROR, response.getMessage());
            }
            return response.getData().getForegroundImage();
        } catch (Exception e) {
            log.error(">>> 火山引擎API异常", e);
            throw new BusinessException(ResultEnum.ERROR, e.getMessage());
        }
    }

    public String convertPhoto(VisualGeneralSegmentRequest reqVo) {
        AdminSettingToolInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_TOOL).toBean(AdminSettingToolInfoVo.class);
        IVisualService visualService = VisualServiceImpl.getInstance();
        visualService.setAccessKey(config.getKey());
        visualService.setSecretKey(config.getSecret());
        try {
            VisualConvertPhotoV2Request request = new VisualConvertPhotoV2Request();
            request.setBinaryDataBase64(Lists.newArrayList(reqVo.getImageBase64()));
            request.setIfColor(2);
            VisualConvertPhotoV2Response response = visualService.convertPhotoV2(request);
            if (response.getCode() != 10000) {
                throw new BusinessException(ResultEnum.ERROR, response.getMessage());
            }
            return response.getData().getBinaryDataBase64().get(0);
        } catch (Exception e) {
            log.error(">>> 火山引擎API异常", e);
            throw new BusinessException(ResultEnum.ERROR, e.getMessage());
        }
    }

    public String[] poemMaterial(VisualPoemMaterialRequest request) {
        AdminSettingToolInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_TOOL).toBean(AdminSettingToolInfoVo.class);
        IVisualService visualService = VisualServiceImpl.getInstance();
        visualService.setAccessKey(config.getKey());
        visualService.setSecretKey(config.getSecret());
        try {
            VisualPoemMaterialResponse response = visualService.poemMaterial(request);
            if (response.getCode() != 10000) {
                throw new BusinessException(ResultEnum.ERROR, response.getMessage());
            }
            return response.getData().getPoems();
        } catch (Exception e) {
            log.error(">>> 火山引擎API异常", e);
            throw new BusinessException(ResultEnum.ERROR, e.getMessage());
        }
    }


    public String imageStyleConversion(ImageStyleConversionRequest request) {
        AdminSettingToolInfoVo config = configService.getConfigObject(ConfigKeyEnum.SETTING_TOOL).toBean(AdminSettingToolInfoVo.class);
        IVisualService visualService = VisualServiceImpl.getInstance();
        visualService.setAccessKey(config.getKey());
        visualService.setSecretKey(config.getSecret());
        try {
            ImageStyleConversionResponse response = visualService.imageStyleConversion(request);
            if (response.getCode() != 10000) {
                throw new BusinessException(ResultEnum.ERROR, response.getMessage());
            }
            return response.getData().getImage();
        } catch (Exception e) {
            log.error(">>> 火山引擎API异常", e);
            throw new BusinessException(ResultEnum.ERROR, e.getMessage());
        }
    }


    public String imageRibbetAi(MultipartFile file) {
        File tempFile = FileUtil.createTempFile();
        try {
            String result = HttpUtil.createPost("https://ribbet.ai/ai?a=background")
                    .header("authority", "ribbet.ai")
                    .header("accept", "*/*")
                    .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("origin", "https://ribbet.ai")
                    .header("referer", "https://ribbet.ai/")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "macOS")
                    .header("sec-fetch-dest", "empty")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-site", "same-origin")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36")
                    .header("x-requested-with", "XMLHttpRequest")
                    .form("file",FileUtil.writeBytes(file.getBytes(), tempFile) )
                    .execute()
                    .body();
            return JSONUtil.parseObj(result).getJSONObject("data").getStr("imageUrl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
