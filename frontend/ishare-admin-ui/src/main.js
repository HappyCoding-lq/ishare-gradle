// ie polyfill
import '@babel/polyfill'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/'
import { VueAxios } from './utils/request'
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'

// mock
// import './mock'

import bootstrap from './core/bootstrap'
import './core/lazy_use'
import './permission' // permission control
import './utils/filter' // global filter
import 'ant-design-vue/dist/antd.less'
import i18n from './locales'

Vue.config.productionTip = false
if (process.env.NODE_ENV !== 'production') {
  Vue.config.devtools = true
}

// mount axios Vue.$http and this.$http
Vue.use(VueAxios)
Vue.use(Viewer)
Viewer.setDefaults({
  Options: { 'inline': true, 'button': true, 'navbar': true, 'title': true, 'toolbar': true, 'tooltip': true, 'movable': true, 'zoomable': true, 'rotatable': true, 'scalable': true, 'transition': true, 'fullscreen': true, 'keyboard': true, 'url': 'data-source' }
})
new Vue({
  router,
  store,
  i18n,
  created: bootstrap,
  render: h => h(App)
}).$mount('#app')
