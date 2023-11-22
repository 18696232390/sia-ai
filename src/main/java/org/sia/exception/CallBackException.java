package org.sia.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sia.enums.ResultEnum;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2022/5/30 14:45
 */
@Getter
@AllArgsConstructor
public class CallBackException extends RuntimeException{
    private Integer code;
    private String message;

    public CallBackException(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public CallBackException(ResultEnum resultEnum, String... placeholder) {
        this.code = resultEnum.getCode();
        this.message = String.format(resultEnum.getMessage(), placeholder);
    }
}
