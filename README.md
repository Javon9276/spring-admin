## 简介
首页搭建了一个比较简单的后台系统，功能后期再进行增加。

## 前端(app)
### 介绍
前端使用后台集成框架 [vue-element-admin](https://panjiachen.github.io/vue-element-admin-site/zh/)，它基于 [vue](https://cn.vuejs.org/) 和 [element-ui](https://element.eleme.cn/#/zh-CN/) 实现。详细开发说明可以看Element-ui文档。

### 搭建过程
1. 搭建node.js环境，下载地址：[https://nodejs.org/zh-cn/](https://nodejs.org/zh-cn/) 
2. 增加npm环境变量，把目录`C:\Users\{User}\AppData\Roaming\npm`添加到环境变量Path中，`{User}`为自身电脑名称
3. 在app目录下运行
```
# 安装全局Vue cli
npm install --global vue-cli
# 安装依赖包
npm install
# 运行项目
npm run dev
```
如果网速下载环境不行可以使用cnpm命令
```
# 下载cnpm
npm install -g cnpm --registry=https://registry.npm.taobao.org
# 安装全局Vue cli
cnpm install --global vue-cli
# 安装依赖包
cnpm install
# 运行项目
npm run dev
```

## 后端(api)
### 介绍
后端主要是使用SpringBoot集成多种框架进行开发，使用Maven管理依赖包。

### 框架
主要使用到的技术和相关下载：[Mysql](https://dev.mysql.com/downloads/mysql/5.7.html)、[Redis](https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100)、SpringBoot、Jpa、Jwt、swagger

### 环境搭建
1. 修改项目中的Mysql和Redis环境信息：`spring-admin\api\src\main\resources\application-dev.properties`
2. 使用junit运行`spring-admin\api\src\test\java\cn\javon\api\AdminGenerate.java`进行初始化数据
3. Debug模式启动SpringBoot启动类`spring-admin\api\src\main\java\cn\javon\api\ApiApplication.java`

## 运行
前端和后端启动成功后访问地址
```
http://localhost:9527/#/
```

## 开发工具
IntelliJ IDEA社区版、[VS Code](https://code.visualstudio.com/)、Navicat Premium 12等相关软件

## 附录
### VS Code设置
如果使用VS Code进行前端开发，可以下载下列插件：[https://www.jianshu.com/p/14bd412d205b](https://www.jianshu.com/p/14bd412d205b)

VS Code格式化存在不一致的意见，个人认为比较好看的格式化样式。

设置方法：文件>首选项>设置，右上角有个反转的图标，点击后复制下文覆盖
```
{
    "git.ignoreMissingGitWarning": true,
    "terminal.integrated.shell.windows": "C:\\Windows\\System32\\cmd.exe",
    "files.autoSave": "afterDelay",
    "editor.tabSize": 2,
    //  #去掉代码结尾的分号
    "prettier.semi": false,
    //  #使用带引号替代双引号
    "prettier.singleQuote": true,
    "javascript.format.insertSpaceBeforeFunctionParenthesis": true,
    // #让vue中的js按编辑器自带的ts格式进行格式化 
    "vetur.format.defaultFormatter.js": "vscode-typescript",
    "vetur.format.defaultFormatter.html": "js-beautify-html",
    "vetur.format.defaultFormatterOptions": {
        "js-beautify-html": {
            "wrap_attributes": "auto"
        },
        "prettyhtml": {
            "printWidth": 100,
            "singleQuote": false,
            "wrapAttributes": false,
            "sortAttributes": false
        },
        "prettier": {
            "semi": false,
            "singleQuote": true
        }
    },
    "[javascript]": {
        "editor.defaultFormatter": "esbenp.prettier-vscode"
    },
    "files.exclude": {
        "node_modules/": true
    },
    "[vue]": {
        "editor.defaultFormatter": "octref.vetur"
    }
}
```