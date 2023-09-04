# 天机学堂
#### 项目简介
> 由学成在线项目升级 为天机学堂 主要包含课程录播、销售（优惠券）、评论、笔记、等相关功能实现。升级之后包含用户端和管理后台两个部分
项目迭代日志：
- v1 迭代时间2022.7月11日 - 2022.7.31号
- 主要实现主要流程 包含用户（老师）、课程、搜索、购买、课程学习、优惠券 、财务 等相关功能
#### 产品原型及设计

- 产品原型
    - 用户端 - 魏帅明： https://codesign.qq.com/app/design/ALwE9V58VljX1Dp/axure/O6ym7ZRVvqZAYED
    - 管理后台 - 刘烁：https://codesign.qq.com/s/zm5q0XqWX7ZRBb6/preview/prototype/bOEvq0r2Bm03PAY

- 设计稿：
    - 用户端 - 李芳华： https://codesign.qq.com/app/design/ALwE9V58VljX1Dp/Yyg5Zp2QdxN92lK/inspect

    - 管理后台 - 王静： https://codesign.qq.com/s/zm5q0XqWX7ZRBb6/GPEpZGAprra9w3z/inspect

#### 访问地址
#### 运行环境 - 初始开发环境及工具

- 项目开发环境: Mac + node: v17.8.0 + npm: 8.5.5 || pnpm: 6.32.8 

#### 技术栈应用

- Vue3 + vite + Tdesign + pinia + vue-router

#### 项目结构
```html
java-eaglemap  
│
└───build - 打包目录
│   
└───public - 公共资源目录
│     
└───src - 源代码
│   ├── api 请求相关
│   ├── assets 公共资源
│   │   ├── images 图片资源
│   ├── api                                 - 接口
│   │   ├── user.js                         - 用户
│   │   ├── teacher.js                      - 教师管理
│   │   ├── curriculum.js                   - 课程管理
│   │   ├── title.js                        - 题目管理
│   │   ├── marketing.js                    - 优惠券
│   │   ├── media.js                        - 媒资
│   │   ├── order.js                        - 订单
│   │   ├── question.js                     - 互动问答
│   │   ├── refund.js                       - 退款审批
│   │   ├── question.js                     - 互动问答
│   ├── conponents                          - 公用组件
│   │   ├── AddButton                       - 添加按钮组件
│   │   ├── Delete                          - 删除组件
│   │   ├── Header                          - 头部组件
│   │   ├── ImageMagnify                    - 图片放大组件
│   │   └── ResetPwd                        - 重置密码组件
│   │   └── UploadImage                     - 上传图片组件
│   ├── pages                               页面展示目录
│   │   ├── main                            首页 
│   │   │   ├── index.vue                   - 工作台
│   │   │   └── index.scss                  - 工作台样式
│   │   │   └── components                  - 组件
│   │   ├── login                           登录页面
│   │   ├── userlist                        用户管理
│   │   │   ├── teacher                     - 教师管理
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   │   ├── student                     - 学员管理---小山
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   │   ├── user                        - 后台用户---小山
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   ├── curriculum                      课程管理
│   │   │   ├── course                      - 课程管理
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   ├── add.vue                 - 添加、编辑
│   │   │   │   ├── details.vue             - 详情---小山
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   │   │   │   └── Search              - 列表搜索
│   │   │   │   │   └── Steps               - 步骤条
│   │   │   │   │   └── TableList           - 目录列表
│   │   │   │   │   └── Title               - 公用标题
│   │   │   │   └── base                    - 基本信息
│   │   │   │   │   └── index               - 基本信息
│   │   │   │   └── catalogue               - 课程目录
│   │   │   │   │   └── index               - 课程目录
│   │   │   │   │   └── sort                - 章排序弹层
│   │   │   │   └── video                   - 课程视频
│   │   │   │   │   └── index               - 视频列表
│   │   │   │   │   └── addVideo            - 添加视频
│   │   │   │   └── topic                   - 课程题目
│   │   │   │   │   └── index               - 题目列表
│   │   │   │   │   └── SetTopic            - 添加题目
│   │   │   │   └── tearch                  - 课程老师
│   │   │   │   │   └── index               - 老师列表
│   │   │   │   │   └── SetTopic            - 添加老师
│   │   │   ├── type                        - 课程分类
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   │   ├── media                       - 媒资管理
│   │   │   │   ├── index.vue               - 列表
│   │   │   │   └── index.scss              - 样式
│   │   │   │   └── components              - 组件
│   │   ├── title                           题目管理
│   │   │   ├── index.vue                   - 列表
│   │   │   ├── add.vue                     - 添加、编辑
│   │   │   ├── details.vue                 - 详情---小山
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   │   ├── marketing                       营销中心
│   │   │   ├── index.vue                   - 优惠券列表
│   │   │   ├── add.vue                     - 添加、编辑
│   │   │   ├── details.vue                 - 详情---小山
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   │   ├── interactive                     互动问答
│   │   │   ├── index.vue                   - 问答管理列表
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   │   ├── order                           订单管理
│   │   │   ├── index.vue                   - 管理列表
│   │   │   ├── details.vue                 - 详情---小山
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   │   ├── note                            笔记
│   │   │   ├── index.vue                   - 管理列表
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   │   ├── my                              个人中心---小山
│   │   │   ├── index.vue                   - 主页
│   │   │   └── index.scss                  - 样式
│   │   │   └── components                  - 组件
│   ├── router                              路由页面
│   │   ├── index.tsx                       - 轨迹管理首页
│   │   └── PrivateRoute.tsx 
│   ├── utils       封装工具目录
│   │   ├── commonData.js                   公用数据       
│   │   ├── request.js                      封装请求模块
│   │   └── validate.js                     表单校验  
│   ├── images                              README.md的图片资源
│   ├── .gitignore
│   ├── package.json
│   ├── tsconfig.json
│    └── README.md

```
#### 数据结构设计
#### 安装运行

``` bash
## 安装依赖
npm install || yarn 

## 启动项目 

# 启动链接mock
npm run dev
# 启动链接测试环境
npm run start

## 构建正式环境 - 打包
npm run build

```
#### 相关资料
#### 参考文档（项目开发过程中用到的所有技术难点 涉及到的相关参考）
- vite配置: https://vitejs.dev/config/
#### 包含的三方技术 简介（如果有的话需要写明）
