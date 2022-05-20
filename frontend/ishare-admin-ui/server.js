const express = require('express') // 基于node的快速web开发框架
const morgan = require('morgan') // 日志
const path = require('path') // 模块
// const bodyParser = require('body-parser') // 解析
const cookieParser = require('cookie-parser') // 存储客服端信息
const app = express()
// const favicon = require('serve-favicon')
const port = process.env.PORT || 80 // 端口
const proxy = require('express-http-proxy') // 代理中间件
const env = process.env.NODE_ENV // 返回一个包含用户环境信息的对象
console.log(`You are runnning in [ ${env} ] mode`)
const envConfig = require('./config/proxy')
const proxyTable = envConfig[env].proxy
const compression = require('compression')
const history = require('connect-history-api-fallback')
process.env.TZ = 'Asia/Shanghai' // 默认时区
app.set('trust proxy', 1) // trust first proxy
// app.use(favicon(path.join(__dirname, 'src', 'images', 'logo.png')))
// app.use(bodyParser.json()) // json格式的解析
// 返回的对象是一个键值对，当extended为false的时候，键值对中的值就为'String'或'Array'形式，为true的时候，则可为任何数据类型。
// app.use(bodyParser.urlencoded({ extended: true }))
// 设置日志格式
app.use(morgan('combined'))
app.use(cookieParser())
app.use(express.query())
app.use(compression())
app.use(history(
  {
    logger: console.log.bind(console)
  }
))
// 根据环境设置不同的代理

Object.keys(proxyTable).forEach((k) => {
  const url = proxyTable[k].target
  console.log(`proxy address： ${url} -> ${k}`)
  app.use(k, proxy(url, {
    limit: '20mb'
  }))
})
app.use(express.static(path.resolve(__dirname, '.', 'dist')))
// 所有其他的地址交给前端路由进行处理
app.get('*', (req, res) => {
  res.sendFile(path.resolve(__dirname, '.', 'dist/', 'index.html'))
})
const server = app.listen(port, (err) => {
  if (err) {
    console.log(err)
    return
  }
  const host = server.address().address
  const port = server.address().port

  console.log('Listening at http://%s:%s', host, port)
})
