<template>
  <a-modal
    title="接口详情"
    style="top: 20px;"
    :width="800"
    :footer="null"
    v-model="visible"
  >
    <a-card :bordered="false">
      <detail-list size="small" :col="1">
        <detail-list-item term="对外api接口路径">{{ mdl.apiPath }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="对外api接口交易编码">{{ mdl.apiTransCode }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="接口描述">{{ mdl.routeDesc }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="2">
        <detail-list-item term="微服务">{{ mdl.routeUri && mdl.routeUri.substring(5) }}</detail-list-item>
        <detail-list-item term="微服务接口路径">{{ mdl.servicePath }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="3">
        <detail-list-item term="接口启用状态">{{ mdl.isEnable==='YES'?'启用':'未启用' }}</detail-list-item>
        <detail-list-item term="是否需要登录/授权">{{ mdl.needCheckAuth==='NO'?'否':'是' }}</detail-list-item>
        <detail-list-item term="是否API要素验证">{{ mdl.apiFactorsVerify==='YES'?'是':'否' }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="授权客户端" v-if="clientIdList && clientIdList.length>0">
          <a-tag v-for="(item, index) in clientIdList" :key="index">
            {{ item }}
          </a-tag>
        </detail-list-item>
      </detail-list>
      <detail-list size="small" :col="3">
        <detail-list-item term="是否启用IP白名单">{{ mdl.trustedIpsEnable==='YES'?'是':'否' }}</detail-list-item>
        <detail-list-item term="是否记录请求日志">{{ mdl.isLogAble==='YES'?'是':'否' }}</detail-list-item>
        <detail-list-item term="是否能做修改操作">{{ mdl.pageManageOperateAble==='NO'?'否':'是' }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="白名单IP" v-if="ipList && ipList.length>0">
          <a-tag v-for="(item, index) in ipList" :key="index">
            {{ item }}
          </a-tag>
        </detail-list-item>
      </detail-list>
    </a-card>
  </a-modal>
</template>

<script>
import DetailList from '@/components/tools/DetailList'

const DetailListItem = DetailList.Item

export default {
  name: 'ApiRouteDetailModal',
  components: {
    DetailList,
    DetailListItem
  },
  props: {},
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
      mdl: {},
      ipList: [],
      clientIdList: [],
      routeTypeMap: {}
    }
  },
  created () {
  },
  methods: {
    detail (record) {
      this.ipList = []
      this.clientIdList = []
      this.mdl = Object.assign(record)
      if (record.trustedIps) {
        record.trustedIps.split(',').forEach(item => {
          this.ipList.push(item)
        })
      }
      if (record.needCheckAuth && record.needCheckAuth === 'YES' && record.authClients) {
        record.authClients.split(',').forEach(item => {
          this.clientIdList.push(item)
        })
      }
      const filters = JSON.parse(record.filters)
      this.mdl.apiPath = filters[0].args.regexp
      this.mdl.servicePath = filters[0].args.replacement
      this.visible = true
    }
  }
}
</script>
