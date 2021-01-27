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
                    
                    {
                        title: '操作系统',
                        path: '/foundation/system/',
                        children: [
                            { title: '运行机制', path: '/foundation/system/runtime' },
                            { title: '系统调用', path: '/foundation/system/systemcall' },
                            { title: '进程', path: '/foundation/system/process' },
                            { title: 'Linux', path: '/foundation/system/linux' },
                        ]
                    },
                    {
                        title: 'MySQL',
                        path: '/foundation/mysql/',
                        children: [
                            { title: 'MYSQL分层、存储引擎', path: '/foundation/mysql/engine' },
                            { title: 'MYSQL索引与explain', path: '/foundation/mysql/sql' },
                           
                        ]
                    },
                    {
                        title: 'Redis',
                        path: '/foundation/redis/',
                        children: [
                            { title: '数据类型', path: '/foundation/redis/datatype' },
                            { title: '通用指令', path: '/foundation/redis/command' },
                            { title: 'Lettuce', path: '/foundation/redis/lettuce' },
                            { title: '持久化', path: '/foundation/redis/rdbaof' },
                            { title: '事务', path: '/foundation/redis/transaction' },
                            { title: '过期策略', path: '/foundation/redis/expire' },
                            { title: '高级数据类型', path: '/foundation/redis/seniordata' },
                            { title: '主从复制', path: '/foundation/redis/replication' },
                            { title: '哨兵', path: '/foundation/redis/sentinel' },
                            { title: '集群', path: '/foundation/redis/cluster' },
                            { title: '解决方案', path: '/foundation/redis/solution' },

                           
                        ]
                    },
                    {
                        title: '设计模式',
                        path: '/foundation/designpattern/',
                        // children: [
                        //     { title: '运行机制', path: '/foundation/designpattern/runtime' },
                        //     { title: '系统调用', path: '/foundation/system/systemcall' },
                        // ]
                    },
                    { title: '位运算', path: '/foundation/bitewise' },
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

                    { title: '多线程', path: '/java/thread/' ,
                    children: [
                        { title: '多线程基础', path: '/java/thread/thread' },
                        { title: '内存模型', path: '/java/thread/jmm' },
                        { title: '关键字', path: '/java/thread/keyword' },
                        { title: 'CAS(Compare And Swap)', path: '/java/thread/cas' },
                        { title: 'LockSupport', path: '/java/thread/locksupport' },
                        { title: 'AQS同步队列', path: '/java/thread/aqs' },
                        { title: 'ReentrantLock', path: '/java/thread/reentrantlock' },
                        { title: '公平锁与非公平锁', path: '/java/thread/fair' },
                        { title: '读写锁', path: '/java/thread/reentrantreadwritelock' },
                        { title: '读写锁进阶', path: '/java/thread/stampedlock' },
                        { title: '线程池基础', path: '/java/thread/pool' },
                        { title: '线程池实战', path: '/java/thread/javapool' },
                        
                    ]},
                    {
                        title: 'Spring',
                        path: '/java/spring/',
                        children: [
                             { title: '注解', path: '/java/spring/annotation' },
                             { title: '源码', path: '/java/spring/code' },
                             { title: 'IOC', path: '/java/spring/ioc' },
                             { title: 'AOP', path: '/java/spring/aop' },
                            // { title: '运行时数据区', path: '/java/jvm/runtime' },
                            // { title: '垃圾回收', path: '/java/jvm/garbage' },
                            // { title: '垃圾回收器', path: '/java/jvm/collector' },
                            // { title: '内存分配和垃圾回收', path: '/java/jvm/addition' },
                            // { title: '运行参数', path: '/java/jvm/parameter' },
                        ]
                    },
                    {
                        title: 'SpringMVC',
                        path: '/java/springmvc/',
                        children: [
                             { title: '基础', path: '/java/springmvc/foundation' },
                            // { title: '运行时数据区', path: '/java/jvm/runtime' },
                            // { title: '垃圾回收', path: '/java/jvm/garbage' },
                            // { title: '垃圾回收器', path: '/java/jvm/collector' },
                            // { title: '内存分配和垃圾回收', path: '/java/jvm/addition' },
                            // { title: '运行参数', path: '/java/jvm/parameter' },
                        ]
                    },
                    {
                        title: '单元测试',
                        path: '/java/junit/'
                    },
                ]
            },
            {
                title: '中间件', // 必要的
                path: '/middle/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: 'Netty', path: '/middle/netty/', children: [
                        { title: 'NIO', path: '/middle/netty/nio' },
                        { title: 'NIO', path: '/middle/netty/nio' },
                        { title: 'NIO', path: '/middle/netty/nio' },
                        { title: 'NIO', path: '/middle/netty/nio' },
                    ]},
                    { title: 'Nginx', path: '/middle/nginx' },
                ]
            },
            /* {
                title: '服务网格', // 必要的
                path: '/servicemesh/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    
                    { title: 'Docker', path: '/servicemesh/docker/' ,children:[
                        { title: '安装', path: '/servicemesh/docker/install' },
                            // { title: '运行时数据区', path: '/java/jvm/runtime' },
                    ]},
                    { title: 'Kubernetes', path: '/servicemesh/kubernetes/' ,children:[
                        { title: '安装', path: '/servicemesh/kubernetes/install' },
                            // { title: '运行时数据区', path: '/java/jvm/runtime' },
                    ]},
                    { title: 'Minishift', path: '/servicemesh/minishift/' },
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
                title: 'Leetcode', // 必要的
                path: '/leetcode/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
                    { title: '树', path: '/leetcode/tree/',children:[
                        { title: '中序遍历', path: '/leetcode/tree/94' },
                        { title: '动态规划树', path: '/leetcode/tree/95' },
                    ] },
                ]
            },
            {
                title: '数据结构', // 必要的
                path: '/datastructure/', // 可选的, 标题的跳转链接，应为绝对路径且必须存在
                //collapsable: false, // 可选的, 默认值是 true,
                sidebarDepth: 2, // 可选的, 默认值是 1
                children: [
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
                            { title: '二叉查找树', path: '/datastructure/tree/binarySearchTree' },
                        ]
                    },
                ]
            }, */
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
        // locales: {
        //     '/': {
        //         recoLocales: {
        //             homeBlog: {
        //                 article: '文章', // 默认 文章
        //                 tag: '标签', // 默认 标签
        //                 category: '类别', // 默认 分类
        //                 friendLink: '友链' // 默认 友情链接
        //             },
        //             pagation: {
        //                 prev: '上一页',
        //                 next: '下一页',
        //                 go: '前往',
        //                 jump: '跳转至'
        //             }
        //         }
        //     }
        // }
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