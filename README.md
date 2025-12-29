# My-Blog（私人定制版）

> 本仓库为基于开源项目 **ZHENFENG13/My-Blog** 的**私人定制化开发版本**（二次开发 / 重构）。  
> 原项目为：Spring Boot + MyBatis + Thymeleaf 的个人博客系统，主题与页面效果完善。 [GitHub](https://github.com/ZHENFENG13/My-Blog)  
> 本仓库将逐步改造为：**前后端分离**架构（**Vue 前端 + Spring Boot 后端**），以满足个性化功能与工程化需求。

---

## 项目简介

- **定位**：个人博客系统 / 内容管理（文章、分类、标签、评论、友链、站点配置等）
    
- **目标**：
    
    - 前端从 Thymeleaf 模板渲染改为 **Vue SPA**（更灵活的 UI 定制与组件化）
        
    - 后端保留 Spring Boot 技术栈，逐步抽象为 RESTful API
        
    - 支持本地开发、Docker 部署（按需）
        

---

## 功能特性（规划中）

- ✅ 文章：发布 / 编辑 / 草稿 / 分类 / 标签
    
- ✅ 评论：评论管理与展示
    
- ✅ 友链：友情链接展示与管理
    
- ✅ 站点配置：Logo、备案信息、版权文案等（可配置） [GitHub](https://github.com/ZHENFENG13/My-Blog)
    
- 🔁 主题与 UI：逐步替换为 Vue 主题体系（支持暗色/亮色可选）
    
- 🔁 权限与后台：后台管理（管理员登录、内容管理）
    
- 🔁 工程化：前后端分离、接口文档、CI、容器化
    

> 注：上面是“目标态”。如果你当前还没完成接口化/前端改造，可以把未完成项先留着。

---

## 技术栈

### 后端（Spring Boot）

- Spring Boot（原项目分支包含不同 Spring Boot 版本参考） [GitHub](https://github.com/ZHENFENG13/My-Blog)
    
- MyBatis（原项目介绍包含 MyBatis） [GitHub](https://github.com/ZHENFENG13/My-Blog)
    
- MySQL（原项目提供初始化 SQL 文件） [GitHub](https://github.com/ZHENFENG13/My-Blog)
    

### 前端（Vue）

- Vue（建议 Vue3 + Vite；你也可以按自己习惯选择 Vue2/Vue3）
    
- 路由：Vue Router（可选）
    
- 状态管理：Pinia（可选）
    
- UI：按个人喜好选择（Element Plus / AntDV / Naive UI / 自建组件等）
    

---

## 快速开始

### 1）准备环境

- JDK：17（或与你后端选择的 Spring Boot 版本匹配）
    
- Maven：3.8+
    
- Node.js：18+（前端）
    
- MySQL：8.x（或与 SQL 兼容版本）
    
- （可选）Docker / Docker Compose
    

### 2）初始化数据库

原项目数据库初始化文件路径：`static-files/my_blog_db.sql` [GitHub](https://github.com/ZHENFENG13/My-Blog)  
你可以先导入该 SQL 作为基础数据结构，然后在二开过程中再迁移/调整表结构。

### 3）后端启动（示例）

> 按你的实际工程结构修改（例如 `backend/`、`server/` 等目录）。

`# 进入后端目录（示例） cd backend  # 修改 application.yml / application-dev.yml：数据库连接、端口等  # 启动 mvn spring-boot:run`

### 4）前端启动（示例）

`# 进入前端目录（示例） cd frontend  npm install npm run dev`

### 5）默认后台账号（若你沿用了原项目逻辑）

原项目后台默认账号：`admin`  
原项目后台默认密码：`123456` [GitHub](https://github.com/ZHENFENG13/My-Blog)

> 建议你二开后：首次启动强制修改密码 / 使用环境变量注入初始管理员密码。

---

## 目录结构（建议）

> 你可以按此重构（也可改成 monorepo/多模块 Maven）。

`My-Blog/ ├─ backend/                # Spring Boot 后端（REST API） │  ├─ src/main/java/... │  └─ src/main/resources/... ├─ frontend/               # Vue 前端（Vite） │  ├─ src/ │  └─ vite.config.* ├─ static-files/           # （保留）原项目资源，如 SQL、静态文件等 ├─ docs/                   # 文档：接口、部署、设计说明、变更记录 └─ README.md`

---

## 二开说明（本仓库相对原项目的改动方向）

- 前端：Thymeleaf → Vue（前后端分离）
    
- 后端：页面渲染逻辑逐步迁移为 REST API
    
- 工程：补齐开发脚手架、统一配置、日志规范、异常与响应体规范
    
- 部署：补充 Docker Compose 一键启动（按需）
    

---

## 截图 / 演示

- 在线演示（可选）：TODO
    
- 本地截图：TODO
    

> 原项目 README 中包含后台与三套主题效果预览。你完成 UI 改造后建议替换为你自己的截图。 [GitHub](https://github.com/ZHENFENG13/My-Blog)

---

## 致谢与开源协议

- 本项目基于 **ZHENFENG13/My-Blog** 二次开发，感谢原作者与贡献者。 [GitHub](https://github.com/ZHENFENG13/My-Blog)
    
- 原项目 License：**Apache-2.0**，本仓库将遵循并保留原协议与版权声明。 [GitHub](https://github.com/ZHENFENG13/My-Blog)
    

---

## 维护者

- 维护者：沈南冰（可改）
    
- 用途：个人学习 / 私人定制 / 作品集展示（按需）