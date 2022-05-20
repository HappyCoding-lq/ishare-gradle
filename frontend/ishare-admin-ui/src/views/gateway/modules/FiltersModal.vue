<template>
  <a-modal
    title="过滤器配置"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    :footer="null"
  >
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd(index)">添加</a-button>
      <a-button style="float: right" type="primary" @click="handleSubmit">提交</a-button>
    </div>
    <s-table
      size="default"
      ref="table"
      rowKey="name"
      :columns="columns"
      :data="loadData"
      :showPagination="false"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a @click="delById([record.index])">删除</a>
      </span>
    </s-table>
    <Filter-save-modal @cancel="receiveFilterCancel" ref="filterSaveModal" @ok="handleOk"/>
  </a-modal>
</template>

<script>
import { STable } from '@/components'
import FilterSaveModal from './FilterSaveModal.vue'

export default {
  name: 'FiltersModal',
  components: {
    STable,
    FilterSaveModal
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
          title: '序号',
          dataIndex: 'index'
        },
        {
          title: '过滤器名字',
          dataIndex: 'name'
        },
        {
          title: '过滤器参数',
          dataIndex: 'args'
        },
        {
          title: '操作',
          width: '150px',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return new Promise((resolve) => {
          resolve(this.mdl)
        })
      },
      selectedRowKeys: [],
      selectedRows: [],
      index: 0
    }
  },
  created () {
  },
  methods: {
    show (value) {
      this.visible = true
      this.mdl = value
      this.index = this.mdl.rows.length
      let i = 0
      this.mdl.rows.forEach(column => {
        column.index = ++i
        const argsStr = JSON.stringify(column.args) || '无'
        column.args = argsStr === '{}' ? '无' : argsStr
      })
      this.$refs.table && this.$refs.table.refresh(true)
    },
    handleAdd (index) {
      this.$refs.filterSaveModal.addFilter(index)
    },
    handleEdit (filter) {
      if (filter.args === '无') {
        delete filter.args
      }
      this.$refs.filterSaveModal.editFilter(filter)
    },
    handleOk () {
      this.$refs.table.refresh(true)
    },
    receiveFilterCancel (data) {
      if (data.newFlag && data.newFlag === true) {
        const newRow = {}
        newRow.index = data.index
        newRow.name = data.name
        newRow.args = data.args || '无'
        this.mdl.rows.push(newRow)
        this.index++
      } else {
        this.mdl.rows[data.index - 1] = {}
        this.mdl.rows[data.index - 1].index = data.index
        this.mdl.rows[data.index - 1].name = data.name
        this.mdl.rows[data.index - 1].args = data.args || '无'
      }
      this.$refs.table.refresh(true)
    },
    delById (index) {
      this.mdl.rows.forEach(item => {
        if (item.index >= index) {
          item.index--
        }
      })
      this.mdl.rows.splice(index - 1, 1)
      this.index--
    },
    handleSubmit () {
      this.mdl.rows.forEach(item => {
        if (item.args && item.args !== '无') {
          item.args = JSON.parse(item.args)
        } else {
          delete item.args
        }
        delete item.index
        delete item.newFlag
      })
      if (this.mdl.rows.length === 0) {
        this.$emit('cancel', null)
      } else {
        this.$emit('cancel', JSON.stringify(this.mdl.rows))
      }
      this.visible = false
    }
  }
}
</script>
