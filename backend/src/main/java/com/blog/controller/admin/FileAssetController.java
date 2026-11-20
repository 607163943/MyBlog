package com.blog.controller.admin;

import com.blog.annotation.OperationLog;
import com.blog.common.Result;
import com.blog.pojo.dto.admin.FileAssetPageQueryDTO;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.admin.FileAssetDetailVO;
import com.blog.pojo.vo.admin.FileAssetVO;
import com.blog.service.admin.FileAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件管理接口
 */
@RestController
@RequestMapping("/admin/file-asset")
@RequiredArgsConstructor
public class FileAssetController {

    private final FileAssetService fileAssetService;

    /**
     * 分页查询文件
     */
    @GetMapping("/page")
    public Result<PageVO<FileAssetVO>> page(FileAssetPageQueryDTO dto) {
        return Result.success(fileAssetService.pageQuery(dto));
    }

    /**
     * 根据 ID 查询文件，返回访问 URL
     */
    @GetMapping("/{id}")
    public Result<FileAssetDetailVO> getById(@PathVariable Integer id) {
        return Result.success(fileAssetService.getFileAssetById(id));
    }

    /**
     * 删除文件：存在引用时拒绝删除
     */
    @OperationLog(module = "file", action = "删除文件", key = "#a0")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        fileAssetService.deleteFileAsset(id);
        return Result.success();
    }
}
