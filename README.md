# MyBlog

> 本仓库为个人博客系统 **MyBlog**（前后端分离架构）。  
> 早期在功能方向与页面形态上参考了开源项目 **ZHENFENG13/My-Blog**，现已完成较大范围的架构与技术栈重构：前台/后台前端分离、后端接口化、数据库与核心代码自建实现。

---

## 项目简介

- **定位**：个人博客系统 / 内容管理（文章、分类、标签、站点配置、评论等）
- **架构**：前后端分离  
  - 管理端（Admin）：Vite + Vue3 + Ant Design Vue
  - 门户端（Web）：Vite + Vue3（UI 规划中）
  - 后端（Server）：Spring Boot + MyBatis-Plus（REST API）
- **工程化目标**：本地开发友好、可部署（Docker 按需）、接口规范、权限认证、资源存储与清理闭环

---

## 核心特性

### 内容管理
- ✅ 文章：新增/编辑/发布/下线/草稿
- ✅ 分类：新增/编辑/启用/禁用
- ✅ 标签：新增/编辑/绑定文章

### 权限与认证
- ✅ Shiro 认证
- ✅ Redis：验证码/缓存

### 资源与文件
- ✅ 阿里云 OSS 存储图片（封面/内容图等）
- ✅ Spring Task 定时清理：清理“无引用/过期”的上传资源（避免 OSS 垃圾文件堆积）

---

## 技术栈

### 后端（Server）
- Spring Boot
- MyBatis-Plus
- MySQL
- Redis
- Shiro
- Spring Task（定时任务）

### 前端
- 管理端（Admin）
  - Vite + Vue3
  - Ant Design Vue
- 门户端（Web）
  - Vite + Vue3（进行中）

---

## 快速开始

### 1）环境准备
- JDK：17（或与你的 Spring Boot 版本匹配）
- Maven：3.8+
- Node.js：18+
- MySQL：8.x
- Redis：6.x/7.x
- （可选）Docker / Docker Compose

### 2）配置说明
> 下列配置项按你项目实际 `application-dev.yml` / `.env.*` 等为准。

- **数据库**：MySQL 连接信息
- **Redis**：地址、密码、DB、序列化策略（如有）
- **OSS**：endpoint、bucket、accessKeyId、accessKeySecret、目录策略等
- **安全**：Shiro 相关配置（token、过滤器链、匿名路径等）
- **定时任务**：清理策略（过期天数/扫描频率/批次大小等）

### 4）管理端启动（示例）

```bash
cd admin-web
npm install
npm run dev
```

### 5）门户端启动（示例）

```bash
cd web
npm install
npm run dev
```

## 定时清理说明（OSS 图片）

- 目标：清理“无业务引用/已过期”的上传记录与 OSS 文件，避免存储成本上涨
    
- 实现：Spring Task + 数据库引用表（或引用计数/关联关系）+ OSS 删除
    

## 接口与规范

- 统一响应体（code/message/data）
    
- 统一异常处理（业务异常 + 参数校验 + 权限异常）
    
- 接口文档（可选：Swagger/Knife4j）
    
- 日志规范与审计（可选）
    

---

## 致谢与 License

- 感谢开源项目 **ZHENFENG13/My-Blog** 提供早期功能方向与页面参考。
    
- 本仓库为个人学习与作品集项目，当前版本已进行较大范围的自主设计与实现。
    

---

## 维护者

- 维护者：607163943
    
- 用途：个人学习 / 私人定制 / 作品集展示