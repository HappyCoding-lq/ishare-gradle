<template>
  <a-locale-provider :locale="locale">
    <div id="app">
      <router-view/>
    </div>
  </a-locale-provider>
</template>

<script>
// import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
import { AppDeviceEnquire } from '@/utils/mixin'
// 引用cdn还可以这么写
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'

export default {
  mixins: [AppDeviceEnquire],
  data () {
    return {
      locale: zhCN
    }
  },
  mounted () {
  //  this.$store.dispatch('tool/allUrls')
  }
}
</script>
<style>
  #app {
    height: 100%;
  }
  .list-total-info{
    margin: 20px 0;
  }
</style>
