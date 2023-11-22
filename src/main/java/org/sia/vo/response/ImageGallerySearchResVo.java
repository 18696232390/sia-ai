package org.sia.vo.response;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.sia.model.ImageTask;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/18 17:28
 */
@Data
public class ImageGallerySearchResVo {
    private String id;
    private String avatar;
    private String imageUrl;
    private String miniUrl;
    private String nickName;
    private String prompt;
    private String promptEn;
    private Long createTime;
}
