module.exports = {
    title: '林秃秃',
    description: '有术无道，止于术；有道无术，术尚可求。',
    base: '/whistle/',
    theme: 'reco',
    themeConfig: {
        lastUpdated: '最近更新', //上次更新
        smoothScroll: true, //页面滚动
        type: 'blog',
        sidebar: 'auto',
        logo: '/logo.svg', //导航栏logo
        author: '林秃秃', //全局作者名
        authorAvatar: '/avatar.png', //设置首页右侧信息栏头像
        //noFoundPageByTencent: false, //404腾讯公益
        nav: [
            { text: '主页', link: '/' },
            { text: '时间线', link: '/timeline/', icon: 'reco-date' }
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