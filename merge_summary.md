此次合并主要引入了 MyBatis-Plus 替代原有的 MyBatis 框架，并新增了完整的 Vue 3 前端项目。后端模型类添加了 MyBatis-Plus 注解，配置文件也相应更新；前端项目包含登录、首页、仪表盘、任务管理等多个功能模块。
| 文件 | 变更 |
|------|---------|
| pom.xml | - 新增 MyBatis-Plus 版本依赖（3.5.9）<br>- 新增 mybatis-plus-spring-boot3-starter 依赖 |
| xxl-job-admin/pom.xml | - 将 mybatis-spring-boot-starter 替换为 mybatis-plus-spring-boot3-starter |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobGroup.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobInfo.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobLog.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobLogGlue.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobLogReport.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobRegistry.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/java/com/xxl/job/admin/model/XxlJobUser.java | - 添加 MyBatis-Plus 注解（@TableName, @TableId, @TableField）<br>- 字段添加 @TableField 注解指定数据库列名 |
| xxl-job-admin/src/main/resources/application.properties | - 将 mybatis 配置改为 mybatis-plus 配置<br>- 新增 mybatis-plus 相关配置（type-aliases-package, map-underscore-to-camel-case 等） |
| xxl-job-admin-ui/index.html | - 新增前端项目入口 HTML 文件 |
| xxl-job-admin-ui/package.json | - 新增前端项目配置文件，定义了 Vue 3、Vue Router、Pinia、Element Plus 等依赖 |
| xxl-job-admin-ui/src/App.vue | - 新增 Vue 应用根组件 |
| xxl-job-admin-ui/src/main.js | - 新增 Vue 应用入口文件，初始化 Vue 应用并配置路由、状态管理等 |
| xxl-job-admin-ui/src/router/index.js | - 新增前端路由配置，定义了登录、首页、仪表盘等路由 |
| xxl-job-admin-ui/src/views/Dashboard.vue | - 新增仪表盘视图组件 |
| xxl-job-admin-ui/src/views/Home.vue | - 新增首页视图组件 |
| xxl-job-admin-ui/src/views/JobGroup.vue | - 新增任务组管理视图组件 |
| xxl-job-admin-ui/src/views/JobInfo.vue | - 新增任务信息管理视图组件 |
| xxl-job-admin-ui/src/views/JobLog.vue | - 新增任务日志管理视图组件 |
| xxl-job-admin-ui/src/views/Login.vue | - 新增登录视图组件 |
| xxl-job-admin-ui/src/views/UserManage.vue | - 新增用户管理视图组件 |
| xxl-job-admin-ui/vite.config.js | - 新增 Vite 构建配置文件 |