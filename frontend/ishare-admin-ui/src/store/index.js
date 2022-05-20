import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import camelcase from 'camelcase'
Vue.use(Vuex)
const context = require.context('./modules', false, /\.js$/)
// 获取moudules文件下所有js文件；
const moduleStores = {}
context.keys().forEach(key => {
  // context.keys()    返回匹配成功模块的名字组成的数组
  const fileName = key.slice(2, -3)
  // 截取名字
  const fileNameInCamelCase = camelcase(fileName)
  // camelcase 是一个驼峰命名包；
  const fileModule = context(key).default
  // 通过 context(key)导出文件内容。在文件中时通过 export.default 导出的，所以后边加.default
  moduleStores[fileNameInCamelCase] = {
    ...fileModule
  }
})

export default new Vuex.Store({
  modules: {
    ...moduleStores,
    strict: process.env.NODE_ENV !== 'production'
  },
  state: {},
  mutations: {},
  actions: {},
  getters
})
