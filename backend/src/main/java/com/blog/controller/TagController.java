package com.blog.controller;

import com.blog.common.Result;
import com.blog.pojo.dto.TagDTO;
import com.blog.pojo.dto.TagPageQueryDTO;
import com.blog.pojo.vo.PageVO;
import com.blog.pojo.vo.TagVO;
import com.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标签管理接口
 */
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 分页查询标签
     */
    @GetMapping("/page")
    public Result<PageVO<TagVO>> page(TagPageQueryDTO dto) {
        return Result.success(tagService.pageQuery(dto));
    }

    /**
     * 根据 ID 查询标签
     */
    @GetMapping("/{id}")
    public Result<TagVO> getById(@PathVariable Integer id) {
        return Result.success(tagService.getTagById(id));
    }

    /**
     * 新增标签
     */
    @PostMapping
    public Result<Void> add(@RequestBody TagDTO dto) {
        tagService.addTag(dto);
        return Result.success();
    }

    /**
     * 修改标签
     */
    @PutMapping
    public Result<Void> update(@RequestBody TagDTO dto) {
        tagService.updateTag(dto);
        return Result.success();
    }

    /**
     * 删除单个标签
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return Result.success();
    }

    /**
     * 批量删除标签
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteBatch(@RequestBody List<Integer> ids) {
        tagService.deleteTags(ids);
        return Result.success();
    }
}
