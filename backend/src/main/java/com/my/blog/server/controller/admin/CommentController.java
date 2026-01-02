package com.my.blog.server.controller.admin;

import com.my.blog.common.result.Result;
import com.my.blog.server.service.CommentService;
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
@Api(tags = "评论管理")
@Controller
@RequestMapping("/admin")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation("分页查询评论")
    @GetMapping("/comment/list")
    @ResponseBody
    public Result<PageResult> list(@RequestParam Map<String, Object> params) {
        if (ObjectUtils.isEmpty(params.get("page")) || ObjectUtils.isEmpty(params.get("limit"))) {
            return Result.error(400,"参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return Result.success(commentService.getCommentsPage(pageUtil));
    }

    //@PostMapping("/comments/checkDone")
    //@ResponseBody
    //public Result checkDone(@RequestBody Integer[] ids) {
    //    if (ids.length < 1) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (commentService.checkDone(ids)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("审核失败");
    //    }
    //}

    //@PostMapping("/comments/reply")
    //@ResponseBody
    //public Result checkDone(@RequestParam("commentId") Long commentId,
    //                        @RequestParam("replyBody") String replyBody) {
    //    if (commentId == null || commentId < 1 || !StringUtils.hasText(replyBody)) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (commentService.reply(commentId, replyBody)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("回复失败");
    //    }
    //}

    //@PostMapping("/comments/delete")
    //@ResponseBody
    //public Result delete(@RequestBody Integer[] ids) {
    //    if (ids.length < 1) {
    //        return ResultGenerator.genFailResult("参数异常！");
    //    }
    //    if (commentService.deleteBatch(ids)) {
    //        return ResultGenerator.genSuccessResult();
    //    } else {
    //        return ResultGenerator.genFailResult("刪除失败");
    //    }
    //}

    //@GetMapping("/comments")
    //public String list(HttpServletRequest request) {
    //    request.setAttribute("path", "comments");
    //    return "admin/comment";
    //}


}
