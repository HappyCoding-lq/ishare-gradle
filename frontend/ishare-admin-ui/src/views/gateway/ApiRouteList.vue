<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="15">
            <a-form-item label="处理的微服务">
              <a-select
                showSearch
                allowClear
                placeholder="请选择微服务名"
                v-model="queryParam.routeUriDetail"
                @change="serviceIdChoose($event)"
              >
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
            <a-form-item label="对外接口路径">
              <a-select
                placeholder="请选择接口路径"
                v-model="queryParam.apiPath"
                showSearch
                allowClear
                :loading="loading"
                :disabled="loading || apiPathDisabled"
                @change="pathChange(1, $event)"
              >
                <a-select-option v-for="(value, index) in pathInfoList" :key="index" :value="value['apiPath']">
                  <a-tooltip v-if="value['apiPath'].length>25" :title="value['apiPath']">
                    {{ value['apiPath'].substring(0, 25) + '...' }}
                  </a-tooltip>
                  <span v-if="value['apiPath'].length<=25">{{ value['apiPath'] }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="微服接口路径">
              <a-select
                placeholder="请选择接口路径"
                v-model="queryParam.servicePath"
                showSearch
                allowClear
                :loading="loading"
                :disabled="loading || servicePathDisabled"
                @change="pathChange(2, $event)"
              >
                <a-select-option v-for="(value, index) in pathInfoList" :key="index" :value="value['servicePath']">
                  <a-tooltip v-if="value['servicePath'].length>25" :title="value['servicePath']">
                    {{ value['servicePath'].substring(0, 25) + '...' }}
                  </a-tooltip>
                  <span v-if="value['servicePath'].length<=25">{{ value['servicePath'] }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
          <a-col :md="8" :sm="15">
            <a-form-item label="对外接口编码">
              <a-input allowClear placeholder="请输入，支持模糊查询" v-model="queryParam.apiTransCode"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="接口启用状态">
              <a-select allowClear placeholder="请选择接口启用状态" v-model="queryParam.isEnable" >
                <a-select-option :value="'YES'">启用</a-select-option>
                <a-select-option :value="'NO'">未启用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <a-form-item label="登录/授权">
              <a-select allowClear placeholder="请选择是否需要登录/授权" v-model="queryParam.needCheckAuth" >
                <a-select-option :value="'YES'">需要</a-select-option>
                <a-select-option :value="'NO'">不需要</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="48">
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
          <a-col :md="8" :sm="15">
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
        <a-row :gutter="48">
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
        <a-row>
          <a-col :md="24" :sm="24" align="center">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetParam()">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button v-if="addEnable" type="primary" icon="plus" @click="handleAdd">新建</a-button>
      <a-dropdown v-if="deleteEnable && selectedRowKeys.length > 0">
        <a-button type="danger" icon="delete" @click="delByIds(selectedRowKeys, selectedRows)">删除</a-button>
      </a-dropdown>
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
    <Api-Route-save-modal ref="saveModal" @ok="handleOk"/>
    <Api-Route-detail-modal ref="detailModal" />
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getRouteList, getPathInfoList, enableRoute, delRoute } from '@/api/gateway/route'
import ApiRouteDetailModal from './modules/ApiRouteDetailModal.vue'
import ApiRouteSaveModal from './modules/ApiRouteSaveModal.vue'
import { checkPermission } from '@/utils/permissions'
import TagSelectOption from '../../components/TagSelect/TagSelectOption'
import { loadRouter } from '@/utils/routerUtil'

export default {
  name: 'RouteList',
  components: {
    TagSelectOption,
    STable,
    ApiRouteDetailModal,
    ApiRouteSaveModal
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
      queryParam: {
        apiPath: this.$route.params.apiPath
      },
      editFlag: this.$route.params.editFlag,
      queryData: [],
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
          title: '处理的微服务',
          dataIndex: 'routeUri',
          align: 'center',
          width: '16%',
          customRender: (text, record, index) => {
            const finalText = text.substring(5)
            const length = 20
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '对外接口路径',
          dataIndex: 'filters',
          width: '25%',
          align: 'center',
          customRender: (text, record, index) => {
            const finalText = JSON.parse(text)[0].args.regexp
            const length = 20
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '接口描述',
          dataIndex: 'routeDesc',
          align: 'center',
          width: '23%',
          customRender: (text, record, index) => {
            const finalText = text
            const length = 14
            const flag = finalText.length > length
            if (!flag) {
              return finalText
            }
            return <a-tooltip title={ finalText }>{ finalText.substring(0, length) + '...' }</a-tooltip>
          }
        },
        {
          title: '启用状态',
          align: 'center',
          width: '10%',
          dataIndex: 'isEnable',
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
        this.queryParam.routeType = 'openAPI'
        if (this.queryParam.routeUriDetail) {
          this.queryParam.routeUri = 'lb://' + this.queryParam.routeUriDetail
        } else {
          delete this.queryParam.routeUri
        }
        if (this.queryParam.apiPath) {
          this.queryParam.predicates = this.queryParam.apiPath
          this.queryParam.filters = this.queryParam.apiPath
        } else {
          delete this.queryParam.predicates
          delete this.queryParam.filters
        }
        if (this.queryParam.servicePath) {
          this.queryParam.filters = this.queryParam.servicePath
        } else {
          delete this.queryParam.filters
        }
        return getRouteList(this.queryParam, parameter)
      },
      selectedRowKeys: [],
      selectedRows: [],
      pathInfoList: [],
      loading: true,
      apiPathDisabled: false,
      servicePathDisabled: false,
      addEnable: checkPermission('gateway:route:api:add'),
      detailEnable: checkPermission('gateway:route:api:detail'),
      modifyEnable: checkPermission('gateway:route:api:modify'),
      deleteEnable: checkPermission('gateway:route:api:delete'),
      stateChangeEnable: checkPermission('gateway:route:api:stateChange')
    }
  },
  filters: {
  },
  created () {
  },
  mounted () {
    this.$store.dispatch('gateway/getCommonYesAndNoMap')
    this.$store.dispatch('gateway/getServiceIdList')
    this.$store.dispatch('gateway/getClientList')
    this.$nextTick(() => {
      this.serviceIdChoose()
      this.loading = false
      if (this.editFlag && this.queryParam.apiPath) {
        this.loadData({ pageNum: this.$refs.table.pageNum, pageSize: this.$refs.table.pageSize }).then(res => {
          this.handleEdit(res.rows[0], this.serviceIdList)
        })
      }
    })
  },
  computed: {
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
    serviceIdChoose (serviceId) {
      delete this.queryParam.apiPath
      delete this.queryParam.servicePath
      this.loading = true
      getPathInfoList({ serviceId: serviceId }).then(res => {
        if (res.code === '200') {
          this.pathInfoList = res.data
        } else {
          this.$message.error(res.message | '获取接口列表失败')
        }
      }).catch(() => {
        this.$message.error('系统错误，请稍后再试')
      }).finally(() => {
        this.loading = false
      })
    },
    pathChange (type, path) {
      if (!path) {
        delete this.queryParam.apiPath
        delete this.queryParam.servicePath
        this.apiPathDisabled = false
        this.servicePathDisabled = false
        return
      }
      if (type === 1) {
        this.servicePathDisabled = true
        this.queryParam.servicePath = this.pathInfoList.filter(item => {
          return item.apiPath === path
        })[0].servicePath
      }
      if (type === 2) {
        this.apiPathDisabled = true
        this.queryParam.apiPath = this.pathInfoList.filter(item => {
          return item.servicePath === path
        })[0].apiPath
      }
    },
    resetParam () {
      const routeUriDetail = this.queryParam.routeUriDetail
      this.queryParam = {}
      if (routeUriDetail) {
        this.serviceIdChoose()
      }
      this.apiPathDisabled = false
      this.servicePathDisabled = false
    },
    handleEnable (record) {
      this.$confirm({
        title: '提示',
        content: '确定要' + (record.isEnable === 'YES' ? '禁用' : '启用') + '接口？(慎重！！！)',
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
      this.$refs.detailModal.detail(routeRecord)
    },
    handleAdd () {
      this.$refs.saveModal.routeAdd(this.serviceIdList, this.clientList)
    },
    handleEdit (routeRecord) {
      this.$refs.saveModal.routeEdit(routeRecord, this.serviceIdList, this.clientList)
    },
    handleLogQuery (routeRecord) {
      const filters = JSON.parse(routeRecord.filters)
      const params = {
        routeType: 'openAPI',
        path: filters[0].args.regexp,
        serviceId: routeRecord.routeUri.substring(routeRecord.routeUri.indexOf('://') + 3)
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
