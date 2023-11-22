package org.sia.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.sia.enums.ResultEnum;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2022/5/30 14:36
 */
@Builder
@Data
public class R<T> {
    @ApiModelProperty(value = "业务状态码",example = "0")
    private Integer code;
    @ApiModelProperty(value = "请求数据")
    private T data;
    @ApiModelProperty(value = "错误信息")
    private String errMsg;
    public static R success() {
        return R.builder().code(ResultEnum.SUCCESS.getCode()).build();
    }

    public static R success(Object data) {
        return R.builder().code(ResultEnum.SUCCESS.getCode()).data(data).build();
    }

    public static R fail(String errMsg) {
        return R.builder().code(ResultEnum.FAIL.getCode()).errMsg(String.format(ResultEnum.FAIL.getMessage(), errMsg)).build();
    }
}
