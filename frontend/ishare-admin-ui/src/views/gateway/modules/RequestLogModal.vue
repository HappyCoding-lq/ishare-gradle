<template>
  <a-modal
    title="日志详情"
    style="top: 20px;"
    :width="800"
    :footer="null"
    v-model="visible"
  >
    <a-card :bordered="false">
      <detail-list size="small" :col="2" >
        <detail-list-item term="请求地址" >{{ mdl.path }}</detail-list-item>
        <detail-list-item term="API接口编码" v-if="mdl.apiTransCode">{{ mdl.apiTransCode }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="匹配路由描述" v-if="mdl.routeDesc">{{ mdl.routeDesc }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="转发的微服务" v-if="mdl.serviceId">{{ mdl.serviceId }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="3" >
        <detail-list-item term="请求客户端" v-if="mdl.appId">{{ mdl.appId }}</detail-list-item>
        <detail-list-item term="登录用户名" v-if="mdl.loginName">{{ mdl.loginName }}</detail-list-item>
        <detail-list-item term="请求IP" v-if="mdl.requestIp">{{ mdl.requestIp }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1" >
        <detail-list-item term="请求方法" v-if="mdl.requestContentType || mdl.requestMethod">{{ mdl.requestContentType? mdl.requestContentType + '&#8195;' : '' }}{{ mdl.requestMethod }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="3" >
        <detail-list-item term="请求时间">{{ mdl.requestTime }}</detail-list-item>
        <detail-list-item term="响应时间" v-if="mdl.responseTime">{{ mdl.responseTime }}</detail-list-item>
        <detail-list-item term="响应耗时" v-if="mdl.accessTime">{{ mdl.accessTime }}毫秒</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1" >
        <detail-list-item term="User-Agent" v-if="mdl.userAgent">{{ mdl.userAgent }}</detail-list-item>
      </detail-list>
      <a-divider/>
      <detail-list title="请求参数：" size="small" >
        <a-textarea :rows="15" v-model="mdl.requestInfo" disabled style="color:#000"/>
      </detail-list>
      <detail-list title="响应参数：" size="small" >
        <a-textarea :rows="15" v-model="mdl.responseInfo" disabled style="color:#000"/>
      </detail-list>
    </a-card>
  </a-modal>
</template>

<script>
import DetailList from '@/components/tools/DetailList'
const DetailListItem = DetailList.Item

export default {
  name: 'RequestLogModal',
  components: {
    DetailList,
    DetailListItem
  },
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      visible: false,
      mdl: {}
    }
  },
  created () {
  },
  methods: {
    detail (record) {
      this.mdl = Object.assign({}, record)
      this.visible = true
    }
  }
}
</script>

<style scoped>

</style>
