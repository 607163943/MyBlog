package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.server.service.TagService;
import com.my.blog.common.utils.PageQueryUtil;
import com.my.blog.common.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
@Api(tags = "标签管理")
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    //@GetMapping("/tags")
    //public String tagPage(HttpServletRequest request) {
    //    request.setAttribute("path", "tags");
    //    return "admin/tag";
    //}

    @ApiOperation("分页查询标签")
    @GetMapping("/tag/list")
    @ResponseBody
    public Result<PageResult> list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return Result.error(400,"参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return Result.success(tagService.getBlogTagPage(pageUtil));
    }


    //@PostMapping("/tags/save")
    //@ResponseBody
    //public Result save(@RequestParam("tagName") String tagName) {
    //    if (!StringUtils.hasText(tagName)) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (tagService.saveTag(tagName)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("标签名称重复");
    //    }
    //}

    //@PostMapping("/tags/delete")
    //@ResponseBody
    //public Result delete(@RequestBody Integer[] ids) {
    //    if (ids.length < 1) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (tagService.deleteBatch(ids)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("有关联数据请勿强行删除");
    //    }
    //}


}
