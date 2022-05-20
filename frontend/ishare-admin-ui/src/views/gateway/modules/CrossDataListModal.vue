<template>
  <a-modal
    title="配置数据"
    style="top: 20px;"
    :width="1100"
    v-model="visible"
    :footer="null"
  >
    <div class="table-operator" tex>
      <a-button v-if="addEnable" type="primary" icon="plus" @click="$refs.saveModal.add(true, queryParam.path)">新增域</a-button>
      <a-dropdown v-if="deleteEnable&&selectedRowKeys.length > 0">
        <a-popconfirm title="确认删除吗？" @confirm="delByIds(selectedRowKeys)">
          <a-button type="danger" icon="delete">删除</a-button>
        </a-popconfirm>
      </a-dropdown>
      <a-span style="padding-left: 20%">当前请求路径为：<span style="color:red;">{{ queryParam.path }}</span></a-span>
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="id"
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
      :showPagination="false"
    >
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnable" @click="handleEdit(record)">编辑</a>
        <a-divider v-if="deleteEnable" type="vertical" />
        <a-popconfirm v-if="deleteEnable" title="确认删除吗？" @confirm="delByIds([record.id])">
          <a>删除</a>
        </a-popconfirm>
      </span>
    </s-table>
    <cross-data-save-modal ref="saveModal" @ok="handleOk" />
  </a-modal>
</template>

<script>
import { STable } from '@/components'
import { getPathCrossList, delCross } from '@/api/gateway/cross'
import CrossDataSaveModal from './CrossDataSaveModal.vue'
import { checkPermission } from '@/utils/permissions'

export default {
  name: 'CrossDataListModal',
  components: {
    STable,
    CrossDataSaveModal
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
      queryParam: { },
      // 表头
      columns: [
        {
          title: '允许的域',
          dataIndex: 'allowOrigin',
          align: 'center'
        },
        {
          title: '描述',
          dataIndex: 'description',
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getPathCrossList(this.queryParam)
      },
      selectedRowKeys: [],
      selectedRows: [],
      addEnable: checkPermission('gateway:cross:add'),
      editEnable: checkPermission('gateway:cross:edit'),
      deleteEnable: checkPermission('gateway:cross:delete')
    }
  },
  created () {
  },
  methods: {
    show (path) {
      this.selectedRowKeys = []
      this.visible = true
      this.queryParam.path = path
      this.$refs.table && this.$refs.table.refresh(true)
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleEdit (record) {
      this.$refs.saveModal.edit(true, record)
    },
    handleOk () {
      this.$refs.table.refresh(true)
    },
    delByIds (ids) {
      delCross({ ids: ids.join(',') }).then(res => {
        if (res.code === '200') {
          this.$message.success(`删除成功`)
          this.handleOk()
        } else {
          this.$message.error(res.message)
        }
        this.selectedRowKeys = []
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
