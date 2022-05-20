<template>
  <a-modal
    title="路由详情"
    style="top: 20px;"
    :width="800"
    :footer="null"
    v-model="visible"
  >
    <a-card :bordered="false">
      <detail-list size="small" :col="3">
        <detail-list-item term="路由目标">{{ mdl.routeUri }}</detail-list-item>
        <detail-list-item term="路由类型">{{ routeTypeMap[mdl.routeType] }}</detail-list-item>
        <detail-list-item term="路由匹配优先级">{{ mdl.routeOrder }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="路由描述">{{ mdl.routeDesc }}</detail-list-item>
      </detail-list>
      <detail-list title="" size="small" :col="3">
        <detail-list-item term="路由启用状态">{{ mdl.isEnable==='YES'?'启用':'未启用' }}</detail-list-item>
        <detail-list-item term="是否需要登录/授权">{{ mdl.needCheckAuth==='NO'?'否':'是' }}</detail-list-item>
        <detail-list-item term="是否API接口要素验证" style="float: right">{{ mdl.apiFactorsVerify==='YES'?'是':'否' }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1">
        <detail-list-item term="授权客户端" v-if="clientIdList && clientIdList.length>0">
          <a-tag v-for="(item, index) in clientIdList" :key="index">
            {{ item }}
          </a-tag>
        </detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1" v-if="mdl.routeType == 'openAPI'">
        <detail-list-item term="对外api接口交易编码">{{ mdl.apiTransCode }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="3">
        <detail-list-item term="是否启用IP白名单">{{ mdl.trustedIpsEnable==='YES'?'是':'否' }}</detail-list-item>
        <detail-list-item term="是否记录请求日志">{{ mdl.isLogAble==='YES'?'是':'否' }}</detail-list-item>
        <detail-list-item term="是否能做修改操作">{{ mdl.pageManageOperateAble==='NO'?'否':'是' }}</detail-list-item>
      </detail-list>
      <detail-list size="small" :col="1" v-if="this.ipList.length>0">
        <detail-list-item term="白名单IP">
          <a-tag v-for="(item, index) in ipList" :key="index">
            {{ item }}
          </a-tag>
        </detail-list-item>
      </detail-list>
      <a-divider style="margin-bottom: 12px"/>
      <detail-list title="断言字符串:" size="small">
        <a-textarea :rows="8" v-model="mdl.predicates" disabled style="color:#000"/>
      </detail-list>
      <a-divider/>
      <detail-list title="过滤器字符串:" size="small">
        <a-textarea :rows="8" v-model="mdl.filters" disabled style="color:#000"/>
      </detail-list>
      <a-divider style="margin-bottom: 32px"/>
    </a-card>
  </a-modal>
</template>

<script>
import DetailList from '@/components/tools/DetailList'

const DetailListItem = DetailList.Item

export default {
  name: 'RouteDetailModal',
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
    detail (record, routeTypeMap) {
      this.ipList = []
      this.clientIdList = []
      this.routeTypeMap = routeTypeMap
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
      this.visible = true
    }
  }
}
</script>
