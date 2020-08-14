module.exports = {
    title: '林秃秃',
    description: '有术无道，止于术；有道无术，术尚可求。',
    base: '/whistle/',
    theme: 'reco',
    themeConfig: {

        // // 假定是 GitHub. 同时也可以是一个完整的 GitLab URL
        // repo: 'https://github.com/Gentvel/whistle.git',
        // // 自定义仓库链接文字。默认从 `themeConfig.repo` 中自动推断为
        // // "GitHub"/"GitLab"/"Bitbucket" 其中之一，或是 "Source"。
        // repoLabel: '查看源码',
        // // 以下为可选的编辑链接选项
        // // 假如你的文档仓库和项目本身不在一个仓库：
        // docsRepo: 'Gentvel/whistle',
        // // 假如文档不是放在仓库的根目录下：
        // docsDir: '',
        // // 假如文档放在一个特定的分支下：
        // docsBranch: 'gh-pages',
        // // 默认是 false, 设置为 true 来启用
        // editLinks: true,
        // // 默认为 "Edit this page"
        // editLinkText: '在 GitHub 上编辑此页',
        lastUpdated: '最近更新', //上次更新
        smoothScroll: true, //页面滚动
        type: 'blog',
        sidebar: 'auto',
        logo: '/logo.svg', //导航栏logo
        author: '林秃秃', //全局作者名
        authorAvatar: '/avatar.png', //设置首页右侧信息栏头像
        //noFoundPageByTencent: false, //404腾讯公益
        nav: [
            { text: '主页', link: '/', icon: 'reco-eye' },
            { text: '时间线', link: '/timeline/', icon: 'reco-date' }
        ],
        //侧边栏
        sidebar: [{
                title: '基础', // 必要的
                path: '/foundation/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: '位运算', path: '/foundation/bitewise' },
                    {
                        title: '操作系统',
                        path: '/foundation/system',
                        children: [
                            { title: '运行机制', path: '/foundation/system/runtime' },
                        ]
                    },
                    { title: 'Linux', path: '/foundation/linux' },
                    { title: 'Git', path: '/foundation/git' },
                    { title: 'Markdown', path: '/foundation/markdown' }
                ]
            },
            {
                title: 'JAVA', // 必要的
                path: '/java/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [{
                        title: 'Java基础',
                        path: '/java/javase/',
                        children: [
                            { title: '基本类型与关键字', path: '/java/javase/base' },
                            { title: '面向对象', path: '/java/javase/oop' },
                            { title: '泛型', path: '/java/javase/generics' },
                            { title: '反射', path: '/java/javase/reflection' },
                            { title: '异常', path: '/java/javase/exception' },
                            { title: 'IO', path: '/java/javase/io' },
                            { title: '多线程基础', path: '/java/javase/thread' },
                        ]
                    },
                    {
                        title: 'JVM',
                        path: '/java/jvm/',
                        children: [
                            { title: '类的加载', path: '/java/jvm/classloader' },
                            { title: '运行时数据区', path: '/java/jvm/runtime' },
                            { title: '垃圾回收', path: '/java/jvm/garbage' },
                            { title: '垃圾回收器', path: '/java/jvm/collector' },
                            { title: '内存分配和垃圾回收', path: '/java/jvm/addition' },
                            { title: '运行参数', path: '/java/jvm/parameter' },
                        ]
                    },
                    { title: '多线程', path: '/java/thread/' },
                ]
            },
            {
                title: '中间件', // 必要的
                path: '/middle/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: 'Nginx', path: '/middle/nginx' },
                    { title: '缓存', path: '/middle/cache/' }
                ]
            },

            {
                title: '算法', // 必要的
                path: '/algorithm/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: '排序', path: '/algorithm/sort' },
                    { title: '递归', path: '/algorithm/recursion' },
                ]
            },
            {
                title: '数据结构', // 必要的
                path: '/datastructure/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: '基础', path: '/datastructure/base' },
                    {
                        title: '线性结构',
                        path: '/datastructure/line',
                        children: [
                            { title: '顺序表', path: '/datastructure/line/sequence' },
                            { title: '单向链表', path: '/datastructure/line/link' },
                        ]
                    },
                    {
                        title: '树形结构',
                        path: '/datastructure/tree',
                        children: [
                            { title: '二叉树', path: '/datastructure/tree/binaryTree' },
                            //{ title: '单向链表', path: '/datastructure/line/link' },
                        ]
                    },
                ]
            },
            {
                title: 'Leetcode', // 必要的
                path: '/leetcode/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [{
                        title: 'Tree',
                        path: '/leetcode/tree/',
                        children: [
                            { title: '[95]不同的二叉搜索树 II', path: '/leetcode/tree/95' },
                            //{ title: '单向链表', path: '/datastructure/line/link' },
                        ]
                    },
                    //{ title: '缓存', path: '/middle/cache/' }
                ]
            }
        ],
        // 博客配置
        blogConfig: {
            category: {
                location: 2, // 在导航栏菜单中所占的位置，默认2
                text: '分类' // 默认文案 “分类”
            },
            tag: {
                location: 3, // 在导航栏菜单中所占的位置，默认3
                text: '标签' // 默认文案 “标签”
            }
        },
        //中英文
        locales: {
            '/': {
                recoLocales: {
                    homeBlog: {
                        article: '文章', // 默认 文章
                        tag: '标签', // 默认 标签
                        category: '类别', // 默认 分类
                        friendLink: '友链' // 默认 友情链接
                    },
                    pagation: {
                        prev: '上一页',
                        next: '下一页',
                        go: '前往',
                        jump: '跳转至'
                    }
                }
            }
        }
    },
    //修改默认语言代码
    locales: {
        '/': {
            lang: 'zh-CN'
        }
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@img': 'public/assets/img'
            }
        }
    }
}