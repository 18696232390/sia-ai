package org.sia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.sia.model.ImageTask;
import org.sia.vo.request.ImageTaskSearchReqVo;
import org.sia.vo.response.ImageGallerySearchResVo;
import org.sia.vo.response.ImageTaskSearchResVo;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/8/17 21:41
 */
public interface ImageMapper extends BaseMapper<ImageTask> {
    Page<ImageTaskSearchResVo> searchList(Page page, @Param("condition") ImageTaskSearchReqVo condition);

    Page<ImageGallerySearchResVo> searchGalleryList(Page page);
}
