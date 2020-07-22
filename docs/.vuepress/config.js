module.exports = {
    title: 'Whistle',
    description: 'Just play for Fun',
    logo: './logo.svg',
    lastUpdated: 'Last Updated',
    themeConfig: {
        nav: [
            { text: 'Home', link: '/' },
            { text: 'Guide', link: '/guide/' },
            { text: 'External', link: 'https://google.com' },
        ]
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@img': 'public/assets/img'
            }
        }
    }
}