package org.sia.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sia.enums.ResultEnum;
import org.sia.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/12 11:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final ImageService imageService;

    private final static List<String> FILE_SUPPORT = ListUtil.toList("jpg", "png", "jpeg");
    // 文件大小小于10M
    private final static long FILE_SIZE = 10;

    private final static String UPLOAD_PATH = "/upload/{}/{}.{}";

    public String upload(MultipartFile file, String catalog) {
        try (InputStream is = file.getInputStream()) {
            String type = FileTypeUtil.getType(is);
            if (!CollUtil.contains(FILE_SUPPORT, type)) {
                throw new BusinessException(ResultEnum.NOT_SUPPORT_TYPE);
            }
            if (file.getSize() > 1024 * 1024 * FILE_SIZE) {
                throw new BusinessException(ResultEnum.FILE_SIZE_LIMIT, FILE_SIZE + "M");
            }
            String filePath = StrUtil.format(UPLOAD_PATH, catalog, IdUtil.objectId(), type);
            File targetFile = FileUtil.touch(FileUtil.getUserHomePath() + filePath);
            FileUtil.writeBytes(file.getBytes(), targetFile);
            return filePath;
        } catch (IOException e) {
            log.error("文件服务异常", e);
            throw new BusinessException(ResultEnum.ERROR, "文件服务异常");
        }
    }

    /**
     * 上传缩略图
     *
     * @param url
     * @return
     */
    public String uploadMidImage(String url) {
        Image image = ImgUtil.scale(ImgUtil.toImage(imageService.getProxyImage(url)), 0.3f);
        String filePath = StrUtil.format(UPLOAD_PATH, "midjourney", IdUtil.objectId(), ImgUtil.IMAGE_TYPE_JPEG);
        File targetFile = FileUtil.touch(FileUtil.getUserHomePath() + filePath);
        ImgUtil.write(image, targetFile);
        return filePath;
    }

}
