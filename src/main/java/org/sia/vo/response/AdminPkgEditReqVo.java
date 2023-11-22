package org.sia.vo.response;

import lombok.Data;
import org.sia.vo.request.AdminPkgCreateReqVo;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/4 16:41
 */
@Data
public class AdminPkgEditReqVo extends AdminPkgCreateReqVo {
    @NotNull(message = "请填写套餐ID")
    private Long id;
}
