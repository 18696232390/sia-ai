package org.sia.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/3/14 22:08
 */
@Data
public class PageReqVo<T> {
    private int current = 1;
    private int size = 20;
    private T condition;

    public Page getPage(){
        return Page.of(current,size);
    }
}
