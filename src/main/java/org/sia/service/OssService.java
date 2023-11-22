package org.sia.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class OssService {

    private final OSS oss;
    private final ImageService imageService;

    @Value("${oss.bucket}")
    private String bucket;

    /**
     * 上传OSS缩略图
     *
     * @param url
     * @return
     */
    public String uploadMiniImage(String url) {
        Image image = ImgUtil.scale(ImgUtil.toImage(imageService.getProxyImage(url)), 0.3f);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, StrUtil.format("image/{}.png", IdUtil.getSnowflakeNextIdStr()), ImgUtil.toStream(image, ImgUtil.IMAGE_TYPE_PNG));
        putObjectRequest.setProcess("true");
        // 创建PutObject请求。
        PutObjectResult result = oss.putObject(putObjectRequest);
        // 如果上传成功，则返回200。
        if (result.getResponse().getStatusCode() != 200) {
            throw new BusinessException(ResultEnum.OSS_ERROR, result.getResponse().getErrorResponseAsString());
        }
        return this.getExpireTimeUrl(result.getResponse().getUri());
    }

    public String upload(MultipartFile file, String catalog) {
        String originalFilename = file.getOriginalFilename();
        try (InputStream is = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, StrUtil.format("{}/{}.{}", catalog, IdUtil.objectId(), FileUtil.getSuffix(originalFilename)), is);
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = oss.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            if (result.getResponse().getStatusCode() != 200) {
                throw new BusinessException(ResultEnum.OSS_ERROR, result.getResponse().getErrorResponseAsString());
            }
            return this.getExpireTimeUrl(result.getResponse().getUri());
        } catch (IOException e) {
            throw new BusinessException(ResultEnum.OSS_ERROR, e.getMessage());
        }
    }



    public String getExpireTimeUrl(String ossUrl) {
        if (!ossUrl.contains(bucket)) {
            return ossUrl;
        }
        try {
            // 生成签名URL。
            URL url = oss.generatePresignedUrl(bucket, StrUtil.subSuf(URLUtil.getPath(ossUrl), 1), DateUtil.offset(DateUtil.date(), DateField.SECOND, 60));
            return url.toString();
        } catch (OSSException e) {
            log.error("STS临时凭证获取OSS资源异常", e);
            return ossUrl;
        }
    }
}
