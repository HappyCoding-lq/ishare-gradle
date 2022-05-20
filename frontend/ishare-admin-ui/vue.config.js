const path = require('path')
const webpack = require('webpack')
const createThemeColorReplacerPlugin = require('./config/plugin.config')
const CompressionPlugin = require('compression-webpack-plugin')

function resolve (dir) {
  return path.join(__dirname, dir)
}

// eslint-disable-next-line no-unused-vars
const assetsCDN = {
  // main.js里引入了对应的less以使 webpack-theme-color-replacer工作
  // https://cdn.jsdelivr.net/npm/ant-design-vue@1.3.9/dist/antd.min.css
  css: [],
  js: [
    'https://cdn.bootcdn.net/ajax/libs/vue/2.6.10/vue.min.js',
    'https://cdn.jsdelivr.net/npm/axios@0.19.0/dist/axios.min.js',
    'https://cdn.jsdelivr.net/npm/vue-router@3.1.2/dist/vue-router.min.js',
    'https://cdn.jsdelivr.net/npm/vuex@3.1.1/dist/vuex.min.js',
    'https://cdn.jsdelivr.net/npm/moment@2.24.0/moment.min.js',
    'https://cdn.jsdelivr.net/npm/moment@2.24.0/locale/zh-cn.js',
    'https://cdn.jsdelivr.net/npm/@antv/g2@3.5.7/dist/g2.min.js',
    'https://cdn.jsdelivr.net/npm/@antv/data-set@0.10.2/dist/data-set.min.js',
    'https://cdn.jsdelivr.net/npm/ant-design-vue@1.4.6/dist/antd-with-locales.min.js'
  ]
}
// webpack build externals
// eslint-disable-next-line no-unused-vars
const prodExternals = {
  // key表示包名(import foo from 'xx' 里的xx)
  // value表示window下的全局变量名(库暴露出来的namespace,可查lib对应的webpack配置里的library字段)
  'vue': 'Vue',
  'axios': 'axios',
  'vue-router': 'VueRouter',
  'vuex': 'Vuex',
  'moment': 'moment',
  '@antv/g2': 'G2',
  '@antv/data-set': 'DataSet',
  'ant-design-vue': 'antd'
}

// vue.config.js
const vueConfig = {
  publicPath: process.env.VUE_APP_PUBLIC_PATH,
  configureWebpack: (config) => {
    config.plugins = [
      ...config.plugins,
      new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
      createThemeColorReplacerPlugin(),
      new webpack.IgnorePlugin(/moment\//)
    ]
    // config.externals = prodExternals
    if (process.env.NODE_ENV === 'production') {
      // 为生产环境修改配置...
      config.mode = 'production'
      config.plugins = [
        ...config.plugins,
        new CompressionPlugin({
          test: /\.js$|\.html$|.\css/, // 匹配文件名
          threshold: 10240, // 对超过10k的数据压缩
          deleteOriginalAssets: false // 不删除源文件
        })
      ]
      // 将每个依赖包打包成单独的js文件
      const optimization = {
        /* runtimeChunk: 'single',
        splitChunks: {
          chunks: 'all',
          maxInitialRequests: Infinity,
          minSize: 20000, // 依赖包超过20000bit将被单独打包
          cacheGroups: {
            vendor: {
              test: /[\\/]node_modules[\\/]/,
              name (module) {
                // get the name. E.g. node_modules/packageName/not/this/part.js
                // or node_modules/packageName
                const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1]
                // npm package names are URL-safe, but some servers don't like @ symbols
                return `npm.${packageName.replace('@', '')}`
              }
            }
          }
        } */
      }
      Object.assign(config, {
        optimization
      })
    } else {
      // 为开发环境修改配置...
      config.mode = 'development'
    }
  },

  chainWebpack: config => {
    config.resolve.alias.set('@$', resolve('src'))

    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule
      .oneOf('inline')
      .resourceQuery(/inline/)
      .use('vue-svg-icon-loader')
      .loader('vue-svg-icon-loader')
      .end()
      .end()
      .oneOf('external')
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'assets/[name].[hash:8].[ext]'
      })
    const imagesRule = config.module.rule('images')
    imagesRule
      .use('image-webpack-loader')
      .loader('image-webpack-loader')
      .options({
        bypassOnDebug: true
      })
      .end()
    // assets require on cdn
    /* config.plugin('html').tap(args => {
      args[0].cdn = assetsCDN
      return args
    }) */
  },

  css: {
    extract: true, // 是否使用css分离插件 ExtractTextPlugin
    sourceMap: false, // 开启 CSS source maps?
    modules: false, // 启用 CSS modules for all css / pre-processor files.
    loaderOptions: {
      less: {
        modifyVars: {
          // less vars，customize ant design theme
          // 'primary-color': '#F5222D',
          // 'link-color': '#F5222D',
          // 'border-radius-base': '4px'
        },
        javascriptEnabled: true
      }
    }
  },
  parallel: require('os').cpus().length > 1, // 是否为 Babel 或 TypeScript 使用 thread-loader。该选项在系统的 CPU 有多于一个内核时自动启用，仅作用于生产构建。
  devServer: {
    // development server port 8000
    port: 8000,
    proxy: {
      '/api': {
        // target: 'http://172.16.15.103:9527',
        target: 'http://localhost:9527',
        pathRewrite: { '^/api': '' },
        ws: false,
        changeOrigin: true
      }
    }
  },

  // disable source map in production
  productionSourceMap: false,
  lintOnSave: undefined,
  // babel-loader no-ignore node_modules/*
  transpileDependencies: []
}

// 如果你不想在生产环境开启换肤功能，请打开下面注释
// if (process.env.VUE_APP_PREVIEW === 'true') {
// add `ThemeColorReplacer` plugin to webpack plugins
/* vueConfig.configureWebpack.plugins.push() */
// }

module.exports = vueConfig
