package com.my.blog.utils;

import cn.hutool.core.util.IdUtil;
import com.my.blog.MyBlogApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MyBlogApplication.class)
public class TestUtils {

    @Test
    void testUUID() {
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.randomUUID());
        System.out.println(IdUtil.simpleUUID());
    }

    @Test
    void testGetFileExt() {
        String file="test.png";
        String ext = file.substring(file.lastIndexOf(".")+1);
        System.out.println(ext);
    }
}
