# ZJK 云图库后端

一个支持公共图片浏览、用户投稿和管理员审核的云图库后端。MySQL 保存用户和图片信息，腾讯云 COS 存储图片文件。

## 已实现功能

- **账号与权限** · 注册、登录、注销和用户管理。使用 Session 保存登录状态，使用 Spring AOP 校验管理员权限。
- **公共图库** · 分页浏览、按关键词和分类筛选，以及查看图片详情。公开浏览只展示审核通过的图片，图片作者和管理员可查看对应的待审核或未通过图片。
- **图片投稿与管理** · 支持本地文件上传和 URL 导入。图片作者或管理员可编辑、删除图片，管理员可审核和批量导入。

## 本地运行

准备 JDK 21、Maven 3、MySQL 8 和可用的腾讯云 COS 存储桶。

1. 在 MySQL 中执行 [初始化脚本](sql/create_table.sql)，创建 `zjk_picture` 库及用户、图片表。此脚本用于首次初始化，已有表结构的数据库无需重复执行。
2. 将 [配置模板](src/main/resources/application-local.yml.example) 复制为同目录下的 `application-local.yml`。填入自己的 MySQL 连接信息和 COS 配置，也可以保留占位符并为运行环境设置对应的环境变量。Git 会忽略本地配置文件，提交时保留模板即可。
3. 用 IntelliJ IDEA 打开包含 [pom.xml](pom.xml) 的目录，导入 Maven 项目，将项目 SDK 设为 JDK 21，然后运行 `ZjkPictureBackendApplication`。

启动后可打开 [健康检查](http://127.0.0.1:8123/api/health) 验证服务，并在 [Knife4j 接口文档](http://127.0.0.1:8123/api/doc.html) 查看和调试接口。需要登录的操作可以先调用 `POST /api/user/login`，再在同一浏览器会话里调用对应接口。

## 运行测试

在包含 `pom.xml` 的目录下运行 Maven 测试。

```bash
mvn test
```

测试覆盖 Spring 启动、用户接口的角色校验与密码字段脱敏，以及不同审核状态下的图片详情访问权限。运行这些测试无需填写真实的 MySQL 或 COS 凭据。

## 技术栈

Spring Boot 2.7、Spring AOP、MyBatis-Plus、MySQL、腾讯云 COS、Knife4j、Maven。
