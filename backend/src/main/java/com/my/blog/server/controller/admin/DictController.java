package com.my.blog.server.controller.admin;

import com.my.blog.server.service.IDictService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/admin/dict")
@RestController
public class DictController {
    @Resource
    private IDictService dictService;
}
