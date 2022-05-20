<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="12">
            <a-form-item label="匹配路由类型">
              <a-select placeholder="请选择" allowClear v-model="queryParam.routeType" default-value="0" @change="routeTypeQueryChange($event)">
                <a-select-option v-for="(value, key) in routeTypeMap" :key="key" :value="key">{{ key }}-{{ value }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item label="转发的微服务">
              <a-select placeholder="请选择" showSearch allowClear v-model="queryParam.serviceId" @change="serviceIdChange($event)">
                <a-select-option v-for="(value, index) in serviceIdList" :key="index" :value="value">
                  <a-tooltip v-if="value.length>24" :title="value">
                    {{ value.substring(0, 24) + '...' }}
                  </a-tooltip>
                  <span v-if="value.length<=24">{{ value }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="请求地址">
              <a-select
                v-if="queryParam.routeType && queryParam.routeType==='openAPI'"
                placeholder="请选择"
                v-model="queryParam.path"
                showSearch
                allowClear
                :loading="loading"
                :disabled="loading"
              >
                <a-select-option v-for="(value, index) in pathInfoList" :key="index" :value="value['apiPath']">
                  <a-tooltip v-if="value['apiPath'].length>30" :title="value['apiPath']">
                    {{ value['apiPath'].substring(0, 30) + '...' }}
                  </a-tooltip>
                  <span v-if="value['apiPath'].length<=30">{{ value['apiPath'] }}</span>
                </a-select-option>
              </a-select>
              <a-input v-if="!queryParam.routeType || queryParam.routeType!=='openAPI'" allowClear placeholder="请输入，支持模糊查询" v-model="queryParam.path"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="7" :sm="12">
            <a-form-item label="请求参数">
              <a-input allowClear placeholder="请输入，支持模糊查询" v-model="queryParam.requestInfo"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="15">
            <a-form-item label="响应信息">
              <a-input allowClear placeholder="请输入，支持模糊查询" v-model="queryParam.responseInfo"/>
            </a-form-item>
          </a-col>
          <a-col :md="11" :sm="15">
            <a-form-item label="请求时间">
              <a-range-picker
                showTime
                style="width:100%;"
                :placeholder="['请求时间起', '请求时间止']"
                format="YYYY-MM-DD HH:mm:ss"
                :value="requestTimeScope"
                @change="requestTimeScopeChange"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="12">
            <a-form-item label="客户端ID">
              <a-select placeholder="请选择" showSearch allowClear v-model="queryParam.appId" default-value="0">
                <a-select-option v-for="(item, index) in clientList" :key="index" :value="item['appId']">
                  <a-tooltip v-if="(item['appId'] + '(' + item['appName'] + ')').length>23" :title="item['appId'] + '(' + item['appName'] + ')'">
                    {{ (item['appId'] + '(' + item['appName'] + ')').substring(0, 23) + '...' }}
                  </a-tooltip>
                  <span v-if="(item['appId'] + '(' + item['appName'] + ')').length<=23">{{ item['appId'] + '(' + item['appName'] + ')' }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="12">
            <a-form-item label="登录用户">
              <a-input allowClear :disabled="loginNameDisabled()" :placeholder="'请输入登录用户名查询'" v-model="queryParam.loginName"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="24" :sm="15" align="center">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQueryParam">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-popconfirm v-has="'gateway:request:log:clean60'" title="确认清空吗？" @confirm="clean(60)">
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="danger" ghost icon="close">清空60天之前的日志</a-button>
      </a-popconfirm>
      <a-popconfirm v-has="'gateway:request:log:clean30'" title="确认清空吗？" @confirm="clean(30)">
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="danger" ghost icon="close">清空30天之前的日志</a-button>
      </a-popconfirm>
      <!--
      <a-popconfirm v-has="'gateway:request:log:clean1'" title="确认清空吗？" @confirm="clean(1)">
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="danger" ghost icon="close">清空当天之前的日志</a-button>
      </a-popconfirm>
      <a-popconfirm v-has="'gateway:request:log:clean0'" title="确认清空吗？" @confirm="clean(0)">
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="danger" ghost icon="close">清空所有日志</a-button>
      </a-popconfirm>-->
      <!--
      <a-popconfirm v-if="selectedRowKeys.length > 0" title="确认删除吗？" @confirm="delByIds(selectedRowKeys)">
        <a-icon slot="icon" type="question-circle-o" style="color: red" />
        <a-button type="danger" icon="delete">删除</a-button>
      </a-popconfirm>
      -->
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="logId"
      :columns="columns"
      :data="loadData"
      :pagination="{showTotal : total => {return '总共 ' + total + ' 条 '}}"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleDetail(record)">详细</a>
        <!--
        <a-divider type="vertical" />
        <a-popconfirm title="确认删除吗？" @confirm="delByIds([record.logId])">
          <a-icon slot="icon" type="question-circle-o" style="color: red" />
          <a type="danger" ghost icon="close">删除</a>
        </a-popconfirm>
        -->
      </span>
    </s-table>
    <requestLog-modal ref="modal"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getRequestLogList, getRequestLogDetail, delRequestLog, cleanRequestLog } from '@/api/gateway/requestLog'
import { getPathInfoList } from '@/api/gateway/route'
import RequestLogModal from './modules/RequestLogModal.vue'

export default {
  name: 'TableList',
  components: {
    STable,
    RequestLogModal
  },
  data () {
    return {
      visible: false,
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
      permissions: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: {
        routeType: this.$route.params.routeType,
        path: this.$route.params.path,
        serviceId: this.$route.params.serviceId,
        appId: this.$route.params.appId
      },
      // 表头
      columns: [
        {
          title: '请求地址',
          dataIndex: 'path',
          align: 'center',
          customRender: (text, record, index) => {
            const finalText = text
            const length = 30
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '请求客户端',
          dataIndex: 'appId',
          align: 'center',
          customRender: (text, record, index) => {
            if (!text) {
              return ''
            }
            const finalText = text
            const length = 20
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '请求时间',
          dataIndex: 'requestTime',
          align: 'center'
        },
        {
          title: '响应时间',
          dataIndex: 'responseTime',
          align: 'center'
        },
        {
          title: '请求IP',
          dataIndex: 'requestIp',
          align: 'center'
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      requestTimeScope: [],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        // 排序方式 请求时间倒序
        parameter.orderBy = '{"requestTime": -1}'
        return getRequestLogList(this.queryParam, parameter).then(res => {
          return res
        })
      },
      selectedRowKeys: [],
      selectedRows: [],
      lastRouteType: '',
      pathInfoList: [],
      loading: false
    }
  },
  mounted () {
    this.$store.dispatch('gateway/getClientList')
    this.$store.dispatch('gateway/getRouteTypeMap')
    this.$store.dispatch('gateway/getServiceIdList')
    if (this.$route.params.routeType && this.$route.params.routeType === 'openAPI') {
      this.loading = true
      this.loadPathInfoList()
      this.loading = false
    }
  },
  computed: {
    clientList () {
      return this.$store.state.gateway.clientList
    },
    routeTypeMap () {
      return this.$store.state.gateway.routeTypeMap
    },
    serviceIdList () {
      return this.$store.state.gateway.serviceIdList
    }
  },
  methods: {
    routeTypeQueryChange (routeType) {
      if (routeType === 'openAPI' || this.lastRouteType === 'openAPI') {
        delete this.queryParam.path
      }
      this.lastRouteType = routeType
      if (routeType === 'openAPI') {
        this.loading = true
        this.loadPathInfoList()
        this.loading = false
      }
    },
    loadPathInfoList () {
      getPathInfoList({ serviceId: this.queryParam.serviceId }).then(res => {
        if (res.code === '200') {
          this.pathInfoList = res.data
        } else {
          this.$message.error(res.message | '获取接口列表失败')
        }
      }).catch(() => {
        this.$message.error('系统错误，请稍后再试')
      })
    },
    serviceIdChange (serviceId) {
      if (this.queryParam.routeType && this.queryParam.routeType === 'openAPI') {
        if (serviceId) {
          delete this.queryParam.path
        }
        this.loading = true
        this.loadPathInfoList()
        this.loading = false
      }
    },
    loginNameDisabled () {
      if (this.queryParam.appId === 'token') {
        return false
      } else {
        delete this.queryParam.loginName
        return true
      }
    },
    resetQueryParam () {
      this.queryParam = {}
      this.lastRouteType = ''
      this.requestTimeScope = []
      delete this.queryParam.requestTimeBegin
      delete this.queryParam.requestTimeEnd
    },
    requestTimeScopeChange (dates, dateStrings) {
      if (dates) {
        this.requestTimeScope = dates
      } else {
        this.requestTimeScope = []
      }
      if (dateStrings[0] && dateStrings[1]) {
        this.queryParam.requestTimeBegin = dateStrings[0]
        this.queryParam.requestTimeEnd = dateStrings[1]
      } else {
        delete this.queryParam.requestTimeBegin
        delete this.queryParam.requestTimeEnd
      }
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    handleDetail (record) {
      getRequestLogDetail({ logId: record.logId }).then(res => {
        if (res.code === '200') {
          const client = this.clientList.filter(function (item) {
            return item.appId === res.data.appId
          })
          res.data.appName = client && client.length > 0 ? client[0].appName : ''
          this.$refs.modal.detail(res.data)
        } else {
          this.$message.error(res.message | '获取详情失败')
        }
      }).catch(() => {
        this.$message.error('系统错误，请稍后再试')
      })
    },
    handleOk () {
      this.$refs.table.refresh(true)
    },
    delByIds (logIds) {
      delRequestLog({ logIds: logIds.join(',') }).then(res => {
        if (res.code === '200') {
          this.$message.success(`删除成功`)
          this.handleOk()
        } else {
          this.$message.error(res.message)
        }
        this.selectedRowKeys = []
      })
    },
    clean (days) {
      cleanRequestLog({ days: days }).then(res => {
        if (res.code === '200') {
          this.$message.success(`清空成功`)
          this.handleOk()
        } else {
          this.$message.error(res.message)
        }
        this.selectedRowKeys = []
      })
    }
  },
  watch: {
  }
}
</script>
