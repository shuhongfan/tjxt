# 天机学堂 - 天机学堂
#### 项目简介
> 由学成在线项目升级 为天机学堂 主要包含课程录播、销售（优惠券）、评论、笔记、等相关功能实现。升级之后包含用户端和管理后台两个部分
#### 技术栈应用

- Vue3 + vite + Tdesign + pinia + vue-router
#### 项目迭代日志：
    - v1.1 迭代时间2022.7月11日 - 2022.7.31号
    - 主要实现主要流程 包含用户（老师）、课程、搜索、购买、课程学习、优惠券 、财务 等相关功能
    - v1.2 迭代时间2022.8.4 - 2022.8.12
    - 主要实现 免费课程课程播放 包含点击课程购买(免费-加入学习计划)、视频播放（目录-实现学习计划及目录对应、问答、笔记-随着播放时间记录当前笔记）
    - v1.3 迭代时间2022.8.12 - 2022.9.20
    - 主要实现 个人中心【包含我的课程、学习计划、考试、订单、优惠券、积分、设置(2.x内容接口未联调)】、课程购买相关内容【购物车、支付、优惠券】
 
#### 安装运行

``` bash
## 安装依赖 
npm install || yarn 

## 启动项目 

# 启动链接mock
npm run dev
# 启动链接测试环境
npm run start

## 构建正式环境 - 打包 松松的环境打包 配置文件子啊config > proxy
npm run build 

```
#### 产品原型及设计

- 产品原型
    - 用户端 - 魏帅明： https://codesign.qq.com/app/design/ALwE9V58VljX1Dp/axure/O6ym7ZRVvqZAYED
    - 管理后台 - 刘烁： https://codesign.qq.com/s/zm5q0XqWX7ZRBb6/preview/prototype/bOEvq0r2Bm03PAY

- 设计稿：
    - 用户端 - 李芳华： https://codesign.qq.com/app/design/ALwE9V58VljX1Dp/Yyg5Zp2QdxN92lK/inspect

    - 管理后台 - 王静： https://codesign.qq.com/s/zm5q0XqWX7ZRBb6/GPEpZGAprra9w3z/inspect

#### 访问地址
- 用户端 
    - 测试地址一：http://172.17.2.134/tianji-test 松松（自己申请服务器部署）项目结束释放
    - 测试地址二：https://tjxt-user-t.itheima.net 运维部署
- 管理后台
    - 测试地址一：http://172.17.2.134/tianji-admin-test
    - 测试地址二：https://tjxt-admin-t.itheima.net 运维部署

#### 运行环境 - 初始开发环境及工具

- 项目开发环境: Mac + node: v17.8.0 + npm: 8.5.5 || pnpm: 6.32.8

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
│   ├── conponents                          公用组件
│   │   ├── Result                          - 请求结果404、500 等
│   │   ├── AskChapterItems.vue             - 章节栏-问答模块、笔记
│   │   ├── Breadcrumb.vue                  - 公用面包屑-所有详情页面使用
│   │   ├── classCards.vue                  - 课程展示卡片 - 首页、搜索页
│   │   ├── ClassCatalogue.vue              - 课程目录模块
│   │   ├── MainTitle.vue                   - 标题组件
│   │   ├── TableSwitchBar.vue              - table切换头部
│   │   ├── Header.vue                      - 公用头部组件
│   │   └── Footer.vue                      - 公用底部组件
│   ├── pages                               页面展示目录
│   │   ├── adk 
│   │   │   ├── index.tsx                   - 电子围栏挂你首页 列表展示
│   │   │   └── details.tsx                 - 电子围栏挂详情  展示围栏地图及
│   │   ├── classDetails                           
│   │   ├── classList
│   │   │   ├── index.tsx                   - 服务端管理首页
│   │   │   └── details.tsx                 - 服务端管理详情页面 - 展示该服务下的终端列表
│   │   ├── classSearch                     - 系统配置页面
│   │   ├── layouts                         - 系统配置页面
│   │   ├── learning                        - 系统配置页面
│   │   ├── login                           - 系统配置页面
│   │   ├── main                            - 系统配置页面
│   │   ├── pay                             - 系统配置页面
│   │   ├── rsult                           - 系统配置页面
│   │   └── personal                        - 个人中心
│   │       ├── components                  - 组件
│   │       ├── style                       - 样式包 包含index.scss中的所引用的样式
│   │       ├── index.scss                  - 主样式文件
│   │       ├── myClass                     - 我的课程
│   │       ├── myCollect                   - 我的收藏
│   │       ├── myCcoupon                   - 我的优惠券
│   │       ├── myCcouponExplain            - 我的优惠券使用说明
│   │       ├── myExam                      - 我的考试
│   │       ├── myExamDetails               - 我的考试详情
│   │       ├── myIntegral                  - 我的积分
│   │       ├── myIntegralRanking           - 我的积分 - 天梯榜
│   │       ├── myMessage                   - 我的消息
│   │       ├── myOrder                     - 个人中心-我的订单
│   │       ├── myOrder Details             - 个人中心-我的订单详情
│   │       └── mySet.tsx                   - 我的设置
│   ├── router     路由页面
│   │   ├── modules                     - 路由配置
│   │   └── index.js                    - 路由配置
│   ├── store      pinia（类似vuex）状态管理数据处理
│   │   ├── modules                     - pinia配置
│   │   └── index.js                    - pinia配置
│   ├── style       公用样式
│   │   ├── element                     - element样式处理 
│   │   ├── font_style                  - icon字体
│   │   ├── theme                       - 主体样式定义
│   │   └── index.scss                  - 公用样式 
│   ├── utils       工具函数
│   │   ├── request.js                  - 请求封装
│   │   └── tool.js                     - 工具函数封装 
│   ├── .gitignore
│   ├── package.json
│   └── README.md

```

#### 相关资料
Vue3: https://cn.vuejs.org/guide/introduction.html
#### 参考文档（项目开发过程中用到的所有技术难点 涉及到的相关参考）
- vite配置: https://vitejs.dev/config/
#### 包含的三方技术 简介（如果有的话需要写明）
- 腾讯视频播放器：https://cloud.tencent.com/document/product/266/58773
- https://tcplayer.vcube.tencent.com/
