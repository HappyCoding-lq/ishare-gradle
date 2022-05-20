<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="7" :sm="15">
            <a-form-item label="路由类型">
              <a-select allowClear placeholder="请选择路由类型" v-model="queryParam.routeType" >
                <a-select-option v-for="(value, key) in routeTypeMap" :key="key" :value="key">{{ value }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="10" :sm="15">
            <a-form-item label="路由目标">
              <a-select placeholder="请选择" style="width: 35%;" v-model="queryParam.routeUriPrefix" @change="routeUriTypeChange()">
                <a-select-option :value="'lb://'">微服务</a-select-option>
                <a-select-option :value="'http://'">http地址</a-select-option>
                <a-select-option :value="'https://'">https地址</a-select-option>
              </a-select>
              <a-select
                v-if="queryParam.routeUriPrefix==='lb://'"
                showSearch
                allowClear
                placeholder="请选择微服务名"
                style="width: 65%;"
                v-model="queryParam.routeUriDetail">
                <a-select-option v-for="(value, index) in serviceIdList" :key="index" :value="value">
                  <a-tooltip v-if="value.length>25" :title="value">
                    {{ value.substring(0, 25) + '...' }}
                  </a-tooltip>
                  <span v-if="value.length<=25">{{ value }}</span>
                </a-select-option>
              </a-select>
              <a-input
                ref="routeUri"
                v-if="queryParam.routeUriPrefix!=='lb://'"
                allowClear
                :placeholder="'请输入地址，支持模糊查询'"
                style="width: 65%;"
                v-model="queryParam.routeUriDetail"/>
            </a-form-item>
          </a-col>
          <a-col :md="7" :sm="15">
            <a-form-item label="路由启用状态">
              <a-select allowClear placeholder="请选择启用状态" v-model="queryParam.isEnable" >
                <a-select-option :value="'YES'">启用</a-select-option>
                <a-select-option :value="'NO'">未启用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="15">
            <a-form-item label="接口编码">
              <a-input
                allowClear
                :disabled="queryParam.routeType && queryParam.routeType!=='openAPI'"
                :placeholder="queryParam.routeType && queryParam.routeType!=='openAPI'?((delete queryParam.apiTransCode)?'只支持开放API路由类型查询':'只支持开放API路由类型查询'):'请输入，支持模糊查询'"
                v-model="queryParam.apiTransCode"
              />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="IP白名单启用状态">
              <a-select allowClear placeholder="请选择IP白名单启用状态" v-model="queryParam.trustedIpsEnable" >
                <a-select-option :value="'YES'">启用</a-select-option>
                <a-select-option :value="'NO'">不启用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="白名单IP">
              <a-input allowClear placeholder="请输入，查询多个逗号分隔" v-model="queryParam.trustedIps"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="15">
            <a-form-item label="登录/授权">
              <a-select allowClear placeholder="请选择是否需要登录/授权" v-model="queryParam.needCheckAuth" >
                <a-select-option :value="'YES'">需要</a-select-option>
                <a-select-option :value="'NO'">不需要</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="记录日志">
              <a-select allowClear placeholder="请选择是否需要记录日志" v-model="queryParam.isLogAble" >
                <a-select-option :value="'YES'">需要</a-select-option>
                <a-select-option :value="'NO'">不需要</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="页面修改操作">
              <a-select allowClear placeholder="请选择是否能页面修改操作" v-model="queryParam.pageManageOperateAble" >
                <a-select-option :value="'YES'">能</a-select-option>
                <a-select-option :value="'NO'">不能</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="15" v-if="queryParam.needCheckAuth==='YES'?true:((delete queryParam.authClients)?false:false)">
            <a-form-item label="授权客户端" >
              <a-select showSearch allowClear placeholder="请选择授权客户端" v-model="queryParam.authClients" >
                <a-select-option v-for="(item, index) in clientList" :key="index" :value="item['appId']" >
                  <a-tooltip v-if="(item['appId'] + '(' + item['appName'] + ')').length>22" :title="item['appId'] + '(' + item['appName'] + ')'">
                    {{ (item['appId'] + '(' + item['appName'] + ')').substring(0, 22) + '...' }}
                  </a-tooltip>
                  <span v-if="(item['appId'] + '(' + item['appName'] + ')').length<=22">{{ item['appId'] + '(' + item['appName'] + ')' }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24" align="center">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => queryParam = {routeUriPrefix: 'lb://'}">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button v-if="addEnable" type="primary" icon="plus" @click="handleAdd">新建</a-button>
      <a-dropdown v-if="deleteEnable&&selectedRowKeys.length > 0">
        <a-button type="danger" icon="delete" @click="delByIds(selectedRowKeys, selectedRows)">删除</a-button>
      </a-dropdown>
      <a-button style="float: right;" v-if="refreshEnable" type="primary" icon="reload" @click="refresh()">重载所有路由</a-button>
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="routeId"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
      :pagination="{showTotal : total => {return '总共 ' + total + ' 条 '}}"
    >
      <span slot="action" slot-scope="text, record">
        <a type="link" v-if="detailEnable" @click="handleDetail(record)">详情</a>
        <a-divider type="vertical" v-if="modifyEnable && record.isEnable!=='YES' && record.pageManageOperateAble === 'YES'" @click="handleEdit(record)"/>
        <a v-if="modifyEnable && record.isEnable!=='YES' && record.pageManageOperateAble === 'YES'" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" v-if="deleteEnable && record.isEnable!=='YES' && record.pageManageOperateAble === 'YES'" @click="handleEdit(record)"/>
        <a v-if="deleteEnable && record.isEnable!=='YES' && record.pageManageOperateAble === 'YES'" @click="delByIds([record.routeId], [record])">删除</a>
        <a-divider v-if="stateChangeEnable" type="vertical"/>
        <a type="link" v-if="stateChangeEnable" @click="handleEnable(record)">{{ record.isEnable==='YES'?'禁用':'启用' }}</a>
        <a-divider type="vertical"/>
        <a type="link" @click="handleLogQuery(record)">请求日志</a>
      </span>
    </s-table>
    <Route-save-modal ref="saveModal" @ok="handleOk"/>
    <Route-detail-modal ref="detailModal" />
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getRouteList, enableRoute, delRoute, refreshRoute } from '@/api/gateway/route'
import RouteDetailModal from './modules/RouteDetailModal.vue'
import RouteSaveModal from './modules/RouteSaveModal.vue'
import { checkPermission } from '@/utils/permissions'
import TagSelectOption from '../../components/TagSelect/TagSelectOption'
import { loadRouter } from '@/utils/routerUtil'

export default {
  name: 'RouteList',
  components: {
    TagSelectOption,
    STable,
    RouteDetailModal,
    RouteSaveModal
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
      form: this.$form.createForm(this),
      mdl: {},
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: { routeUriPrefix: 'lb://' },
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: 'index',
          align: 'center',
          width: '6%',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '路由目标',
          dataIndex: 'routeUri',
          align: 'center',
          width: '19%',
          customRender: (text, record, index) => {
            const finalText = text
            const length = 24
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '路由类型',
          dataIndex: 'routeType',
          width: '9%',
          align: 'center',
          customRender: (text, record, index) => {
            return this.routeTypeMap[text]
          }
        },
        {
          title: '路由描述',
          dataIndex: 'routeDesc',
          align: 'center',
          width: '26%',
          customRender: (text, record, index) => {
            const finalText = text
            const length = 16
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '登录/授权',
          align: 'center',
          dataIndex: 'needCheckAuth',
          width: '10%',
          customRender: (text, record, index) => {
            if (text === 'YES') {
              return '需要'
            } else {
              return '不需要'
            }
          }
        },
        {
          title: '启用状态',
          align: 'center',
          dataIndex: 'isEnable',
          width: '10%',
          customRender: (text, record, index) => {
            if (text === 'YES') {
              return '启用'
            } else {
              return '未启用'
            }
          }
        },
        {
          title: '操作',
          width: '20%',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        if (!this.queryParam.routeUriPrefix || !this.queryParam.routeUriDetail) {
          delete this.queryParam.routeUri
        } else {
          this.queryParam.routeUri = this.queryParam.routeUriPrefix + this.queryParam.routeUriDetail
        }
        return getRouteList(this.queryParam, parameter)
      },
      selectedRowKeys: [],
      selectedRows: [],
      addEnable: checkPermission('gateway:route:add'),
      refreshEnable: checkPermission('gateway:route:refresh'),
      detailEnable: checkPermission('gateway:route:detail'),
      modifyEnable: checkPermission('gateway:route:modify'),
      deleteEnable: checkPermission('gateway:route:delete'),
      stateChangeEnable: checkPermission('gateway:route:stateChange')
    }
  },
  filters: {
  },
  created () {
  },
  mounted () {
    this.$store.dispatch('gateway/getRouteTypeMap')
    this.$store.dispatch('gateway/getCommonYesAndNoMap')
    this.$store.dispatch('gateway/getServiceIdList')
    this.$store.dispatch('gateway/getClientList')
  },
  computed: {
    routeTypeMap () {
      return this.$store.state.gateway.routeTypeMap
    },
    commonYesAndNoMap () {
      return this.$store.state.gateway.commonYesAndNoMap
    },
    serviceIdList () {
      return this.$store.state.gateway.serviceIdList
    },
    clientList () {
      return this.$store.state.gateway.clientList
    }
  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    routeUriTypeChange () {
      delete this.queryParam.routeUriDetail
    },
    handleEnable (record) {
      this.$confirm({
        title: '提示',
        content: '确定要' + (record.isEnable === 'YES' ? '禁用' : '启用') + '路由？(慎重！！！)',
        onOk: () => {
          enableRoute({ routeId: record.routeId }).then(res => {
            if (res.code === '200') {
              this.$message.success('操作成功')
              this.handleOk()
            } else {
              this.$message.error(res.message)
            }
            this.selectedRowKeys = []
          })
        },
        onCancel () {
        }
      })
    },
    handleDetail (routeRecord) {
      this.$refs.detailModal.detail(routeRecord, this.routeTypeMap)
    },
    handleAdd () {
      this.$refs.saveModal.routeAdd(this.routeTypeMap, this.serviceIdList, this.clientList)
    },
    handleEdit (routeRecord) {
      if (routeRecord.routeType === 'openAPI') {
        this.$confirm({
          title: '提示',
          content: '开放API需要前往API接口管理维护！要去吗？',
          onOk: () => {
            const apiManageRoute = loadRouter('gateway/ApiRouteList')
            const filters = JSON.parse(routeRecord.filters)
            apiManageRoute.params = { apiPath: filters[0].args.regexp, editFlag: true }
            this.$router.push(apiManageRoute)
          },
          onCancel: () => {
          }
        })
      } else {
        this.$refs.saveModal.routeEdit(routeRecord, this.routeTypeMap, this.serviceIdList, this.clientList)
      }
    },
    handleLogQuery (routeRecord) {
      const predicates = JSON.parse(routeRecord.predicates)
      const path = predicates[0].args.pattern || predicates[0].args._genkey_0
      const params = {
        routeType: routeRecord.routeType,
        path: path,
        serviceId: routeRecord.routeUri.startsWith('lb://') ? routeRecord.routeUri.substring(routeRecord.routeUri.indexOf('://') + 3) : null
      }
      this.$router.push({ name: loadRouter('gateway/RequestLogList').name, params: params })
    },
    handleOk () {
      this.$refs.table.refresh(false)
    },
    delByIds (ids, records) {
      let deleEnable = true
      records.forEach(record => {
        if (record.isEnable === 'YES') {
          deleEnable = false
        }
      })
      if (!deleEnable) {
        this.$message.error('启用状态的记录不允许删除')
        return
      }
      this.$confirm({
        title: '提示',
        content: '真的要删除吗？慎重！！！',
        onOk: () => {
          delRoute({ routeId: ids.join(',') }).then(res => {
            if (res.code === '200') {
              this.$message.success('删除成功')
              this.handleOk()
            } else {
              this.$message.error(res.message)
            }
            this.selectedRowKeys = []
          })
        },
        onCancel () {
        }
      })
    },
    refresh () {
      this.$confirm({
        title: '提示',
        content: '确定重载所有路由信息？慎重！！！',
        onOk: () => {
          refreshRoute().then(res => {
            if (res.code === '200') {
              this.$message.success('刷新成功')
            } else {
              this.$message.error(res.message)
            }
          })
        },
        onCancel () {
        }
      })
    }
  },
  watch: {
    /*
      'selectedRows': function (selectedRows) {
        this.needTotalList = this.needTotalList.map(item => {
          return {
            ...item,
            total: selectedRows.reduce( (sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
      }
      */
  }
}
</script>
