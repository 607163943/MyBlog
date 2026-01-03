package com.my.blog.common.exception.admin;

import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.MyBlogException;

public class AdminDictException extends MyBlogException {
    public AdminDictException(ExceptionEnums exceptionEnums) {
        super(exceptionEnums);
    }
}
