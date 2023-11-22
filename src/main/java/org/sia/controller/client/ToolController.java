package org.sia.controller.client;

import com.volcengine.service.visual.model.request.ImageStyleConversionRequest;
import com.volcengine.service.visual.model.request.VisualGeneralSegmentRequest;
import com.volcengine.service.visual.model.request.VisualPoemMaterialRequest;
import lombok.RequiredArgsConstructor;
import org.sia.model.AiTool;
import org.sia.service.BaiduService;
import org.sia.service.ToolService;
import org.sia.vo.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/21 19:41
 */
@RestController
@RequestMapping("v1/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;
    private final BaiduService baiduService;


    /**
     * 应用商店
     * @return
     */
    @GetMapping("/searchList")
    public R<List<AiTool>> searchList(){
        return R.success(toolService.searchList());
    }

    /**
     * 用户应用列表
     * @return
     */
    @GetMapping("/appList")
    public R<List<AiTool>> appList(){
        return R.success(toolService.appList());
    }

    /**
     * 加入应用列表
     * @return
     */
    @GetMapping("/add")
    public R<List<AiTool>> add(@RequestParam Long id){
        return R.success(toolService.add(id));
    }
    /**
     * 删除应用
     * @return
     */
    @GetMapping("/delete")
    public R<List<AiTool>> delete(@RequestParam Long id){
        return R.success(toolService.delete(id));
    }


    /**
     * 删除应用
     * @return
     */
    @PostMapping("/image/repair")
    public R<String> delete(@RequestBody Map<String,Object> body){
        return R.success(baiduService.repair(body));
    }



    /**
     * 一键抠图
     * @return
     */
    @PostMapping("/image/general/segment")
    public R<String> generalSegment(@RequestBody VisualGeneralSegmentRequest reqVo){
        return R.success(toolService.generalSegment(reqVo));
    }

    /**
     * 老照片修复
     * @return
     */
    @PostMapping("/image/convert/photo")
    public R<String> convertPhoto(@RequestBody VisualGeneralSegmentRequest reqVo){
        return R.success(toolService.convertPhoto(reqVo));
    }

    /**
     * 图片配文
     * @return
     */
    @PostMapping("/image/poem/material")
    public R<String[]> poemMaterial(@RequestBody VisualPoemMaterialRequest reqVo){
        return R.success(toolService.poemMaterial(reqVo));
    }


    /**
     * 图片风格转换
     * @return
     */
    @PostMapping("/image/style/conversion")
    public R<String> imageStyleConversion(@RequestBody ImageStyleConversionRequest reqVo){
        return R.success(toolService.imageStyleConversion(reqVo));
    }


    /**
     * 一键抠图[https://ribbet.ai/]
     * @return
     */
    @PostMapping("/image/ribbet/ai")
    public R<String> imageRibbetAi(MultipartFile file){
        return R.success(toolService.imageRibbetAi(file));
    }

}
