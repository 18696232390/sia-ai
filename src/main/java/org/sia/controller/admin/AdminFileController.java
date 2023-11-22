package org.sia.controller.admin;

import lombok.RequiredArgsConstructor;
import org.sia.service.FileService;
import org.sia.service.OssService;
import org.sia.vo.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/admin/file")
@RequiredArgsConstructor
public class AdminFileController {

    private final FileService fileService;

    /**
     * 上传文件
     *
     * @return
     */
    @PostMapping("upload/{catalog}")
    public R<String> upload(@PathVariable String catalog, MultipartFile file) {
        return R.success(fileService.upload(file, catalog));
    }

}
