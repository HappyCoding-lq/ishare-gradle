<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="5" :sm="15">
            <a-form-item label="appId">
              <a-input placeholder="请输入appId" v-model="queryParam.appid"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="appSecret">
              <a-input placeholder="请输入appSecret" v-model="queryParam.appsecret"/>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="15">
            <a-form-item label="应用名称">
              <a-input placeholder="请输入应用名称" v-model="queryParam.appname"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => queryParam = {}">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button v-if="addEnable" type="primary" icon="plus" @click="$refs.modal.add()">新建</a-button>
      <a-dropdown v-if="removeEnable&&selectedRowKeys.length > 0">
        <a-button type="danger" icon="delete" @click="delByIds(selectedRowKeys)">删除</a-button>
      </a-dropdown>
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="id"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
      :pagination="{showTotal : total => {return '总共 ' + total + ' 条 '}}"
    >
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnabel" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a v-if="removeEnable" @click="delByIds([record.id])">删除</a>
        <a-divider type="vertical"/>
        <a @click="handleLogQuery(record)">请求日志</a>
      </span>
    </s-table>
    <clientInfo-modal ref="modal" @ok="handleOk"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getClientInfoList, delClientInfo } from '@/api/system/clientInfo'
import ClientInfoModal from './modules/ClientInfoModal.vue'
import { checkPermission } from '@/utils/permissions'
import { loadRouter } from '@/utils/routerUtil'

export default {
  name: 'TableList',
  components: {
    STable,
    ClientInfoModal
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
      queryParam: {},
      // 表头
      columns: [
        {
          title: 'appId',
          dataIndex: 'appid'
        },
        {
          title: 'appSecret',
          dataIndex: 'appsecret'
        },
        {
          title: '应用名称',
          dataIndex: 'appname'
        },
        {
          title: '应用简述',
          dataIndex: 'description'
        },
        {
          title: '操作',
          width: '200px',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getClientInfoList(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      },
      selectedRowKeys: [],
      selectedRows: [],
      addEnable: checkPermission('system:clientInfo:add'),
      editEnabel: checkPermission('system:clientInfo:edit'),
      removeEnable: checkPermission('system:clientInfo:remove')
    }
  },
  filters: {
  },
  created () {
  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    handleLogQuery (record) {
      this.$router.push({ name: loadRouter('gateway/RequestLogList').name, params: { appId: record.appid } })
    },
    handleAdd () {
      this.$refs.modal.add()
    },
    handleEdit (record) {
      this.$refs.modal.edit(record)
    },
    handleOk () {
      this.$refs.table.refresh(true)
      console.log('handleSaveOk')
    },
    delByIds (ids) {
      this.$confirm({
        title: '提示',
        content: '真的要删除吗？慎重！！！',
        onOk: () => {
          delClientInfo({ ids: ids.join(',') }).then(res => {
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
