package com.site.blog.my.core.controller.user;

import com.site.blog.my.core.entity.BlogLink;
import com.site.blog.my.core.pojo.vo.user.BlogLinkVO;
import com.site.blog.my.core.result.Result;
import com.site.blog.my.core.service.ConfigService;
import com.site.blog.my.core.service.LinkService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "友情链接")
@RestController("user-link-controller")
@RequestMapping("/link")
public class LinkController {
    public static String theme = "amaze";
    @Resource
    private LinkService linkService;
    @Resource
    private ConfigService configService;

    /**
     * 友情链接页
     *
     * @return
     */
    @GetMapping
    public Result<BlogLinkVO> link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        BlogLinkVO blogLinkVO = BlogLinkVO.builder()
                .pageName("友情链接")
                .configurations(configService.getAllConfigs())
                .build();
        Map<Byte, List<BlogLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                blogLinkVO.setFavoriteLinks(linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                blogLinkVO.setRecommendLinks(linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                blogLinkVO.setPersonalLinks(linkMap.get((byte) 2));
            }
        }
        return Result.success(blogLinkVO);
    }
}
