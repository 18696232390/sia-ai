package org.sia.vo.response;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.sia.model.ImageTask;

import java.io.Serializable;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/18 17:28
 */
@Getter
public class ImageTaskSearchResVo extends ImageTask{
    private JSONObject stateInfo;

    @Override
    public void setState(String state) {
        if (StrUtil.isNotBlank(state)) {
            this.stateInfo = JSONUtil.parseObj(state);
        }
    }
}
