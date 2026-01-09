package com.my.blog.server.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "后台首页")
@RestController
@RequestMapping("/admin/home")
public class HomeController {
}
