<script setup>
import { useLayoutStore } from '@/stores/modules/layout'
import { storeToRefs } from 'pinia'

const layoutStore = useLayoutStore()
const { path } = storeToRefs(layoutStore)
</script>

<template>
  <div class="wrapper">
    <!-- 引入页面头header-fragment -->
    <nav class="main-header navbar navbar-expand bg-white navbar-light border-bottom">
      <!-- Left navbar links -->
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" data-widget="pushmenu" href="#"><i class="fa fa-bars"></i></a>
        </li>
        <li class="nav-item d-none d-sm-inline-block">
          <a href="/" class="nav-link">Dashboard</a>
        </li>
      </ul>
      <!-- Right navbar links -->
      <ul class="navbar-nav ml-auto">
        <li class="nav-item dropdown">
          <a class="nav-link" href="/">
            <i class="fa fa-paper-plane">&nbsp;&nbsp;博客首页</i>
          </a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link" data-toggle="dropdown" href="#">
            <i class="fa fa-user">&nbsp;&nbsp;作者</i>
          </a>
          <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
              <i class="fa fa-user-o mr-2"></i> 姓名
              <span class="float-right text-muted text-sm">十三 / 13</span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
              <i class="fa fa-user-secret mr-2"></i> 身份
              <span class="float-right text-muted text-sm">Java开发工程师</span>
            </a>
            <div class="dropdown-divider"></div>
            <a href="#" class="dropdown-item">
              <i class="fa fa-address-card mr-2"></i> 邮箱
              <span class="float-right text-muted text-sm">2449207463@qq.com</span>
            </a>
          </div>
        </li>
      </ul>
    </nav>
    <!-- 引入工具栏sidebar-fragment -->
    <aside class="main-sidebar sidebar-dark-primary elevation-4">
      <!-- Brand Logo -->
      <a href="/" class="brand-link">
        <img
          src="../../../public/dist/img/logo.png"
          alt="ssm-cluster Logo"
          class="brand-image img-circle elevation-3"
          style="opacity: 0.8"
        />
        <span class="brand-text font-weight-light">my blog</span>
      </a>
      <!-- Sidebar -->
      <div class="sidebar">
        <!-- Sidebar user panel (optional) -->
        <div class="user-panel mt-3 pb-3 mb-3 d-flex">
          <div class="image">
            <img
              src="../../../public/dist/img/avatar5.png"
              class="img-circle elevation-2"
              alt="User Image"
            />
          </div>
          <div class="info">
            <!-- TODO：这里要从登录后的用户信息中获取用户名 -->
            <a href="#" class="d-block" th:text="${session.loginUser}">十三</a>
          </div>
        </div>
        <!-- Sidebar Menu -->
        <nav class="mt-2">
          <ul
            class="nav nav-pills nav-sidebar flex-column"
            data-widget="treeview"
            role="menu"
            data-accordion="false"
          >
            <!-- Add icons to the links using the .nav-icon class
                       with font-awesome or any other icon font library -->
            <li class="nav-header">Dashboard</li>
            <li class="nav-item">
              <a href="/" class="nav-link" :class="{ active: path === 'index' }">
                <i class="nav-icon fa fa-dashboard"></i>
                <p>Dashboard</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/blogs/edit" class="nav-link" :class="{ active: path === 'edit' }">
                <i class="nav-icon fa fa fa-pencil-square-o"></i>
                <p>发布博客</p>
              </a>
            </li>
            <li class="nav-header">管理模块</li>
            <li class="nav-item">
              <a href="/admin/blogs" class="nav-link" :class="{ active: path === 'blogs' }">
                <i class="fa fa-list-alt nav-icon" aria-hidden="true"></i>
                <p>博客管理</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/comments" class="nav-link" :class="{ active: path === 'comments' }">
                <i class="fa fa-comments nav-icon" aria-hidden="true"></i>
                <p>评论管理</p>
              </a>
            </li>
            <li class="nav-item">
              <a
                href="/admin/categories"
                class="nav-link"
                :class="{ active: path === 'categories' }"
              >
                <i class="fa fa-bookmark nav-icon" aria-hidden="true"></i>
                <p>分类管理</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/tags" class="nav-link" :class="{ active: path === 'tags' }">
                <i class="fa fa-tags nav-icon" aria-hidden="true"></i>
                <p>标签管理</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/links" class="nav-link" :class="{ active: path === 'links' }">
                <i class="fa fa-heart nav-icon" aria-hidden="true"></i>
                <p>友情链接</p>
              </a>
            </li>
            <li class="nav-header">系统管理</li>
            <li class="nav-item">
              <a
                href="/admin/configurations"
                class="nav-link"
                :class="{ active: path === 'configurations' }"
              >
                <i class="fa fa-wrench nav-icon"></i>
                <p>系统配置</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/profile" class="nav-link" :class="{ active: path === 'profile' }">
                <i class="fa fa-user-secret nav-icon"></i>
                <p>修改密码</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/admin/logout" class="nav-link">
                <i class="fa fa-sign-out nav-icon"></i>
                <p>安全退出</p>
              </a>
            </li>
          </ul>
        </nav>
        <!-- /.sidebar-menu -->
      </div>
      <!-- /.sidebar -->
    </aside>
    <!-- Content Wrapper. Contains page content -->
    <router-view></router-view>
    <!-- /.content-wrapper -->
    <!-- 引入页脚footer-fragment -->
    <footer class="main-footer">
      <strong>Copyright &copy; 2018 <a href="##">13blog.site</a>.</strong>
      All rights reserved.
      <div class="float-right d-none d-sm-inline-block"><b>my personal blog #Version</b> 1.0</div>
    </footer>
  </div>
</template>

<style scoped></style>
