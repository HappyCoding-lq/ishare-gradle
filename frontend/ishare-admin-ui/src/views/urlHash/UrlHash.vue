<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <!--          <a-col :md="4" :sm="15">-->
          <!--            <a-form-item label="">-->
          <!--              <a-input placeholder="请输入URL KEY" v-model="queryParam.lurl"/>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :md="4" :sm="15">
            <a-form-item label="">
              <a-input placeholder="请输入投放地址" v-model="queryParam.lurl"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="15">
            <a-form-item label="">
              <a-input placeholder="请输入描述信息" v-model="queryParam.description"/>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="15">
            <a-form-item label="">
              <a-input placeholder="请输入创建人信息" v-model="queryParam.createUser"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
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
      :columns="columns"
      :data="loadData"
    >
      <span slot="action" slot-scope="text, record">
        <a v-if="editEnabel" @click="handleEdit(record)">编辑</a>
        <a-divider type="vertical" />
        <a v-if="removeEnable" @click="delByIds([record.id])">删除</a>
      </span>
    </s-table>
    <urlHash-modal ref="modal" @ok="handleOk"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getUrlHashList, deleteLink } from '@/api/urlhash/urlhash'
import UrlHashModal from './modules/UrlHashModal.vue'
import { checkPermission } from '@/utils/permissions'

export default {
  name: 'UrlHash',
  components: {
    STable,
    UrlHashModal
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
        id: 1
      },
      // 表头
      columns: [
        {
          title: '描述',
          dataIndex: 'description',
          width: 200
        },
        {
          title: '投放地址',
          dataIndex: 'surl',
          width: 200
        },
        {
          title: '重定向地址',
          dataIndex: 'lurl',
          width: 500
        },
        {
          title: '创建人',
          dataIndex: 'createUser',
          width: 200
        },
        {
          title: '创建时间',
          dataIndex: 'gmtCreate',
          width: 200
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          width: 200
        },
        {
          title: '操作',
          width: 200,
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getUrlHashList(Object.assign(parameter, this.queryParam)).then(res => {
          return res.data
        })
      },
      selectedRowKeys: [],
      selectedRows: [],
      addEnable: checkPermission('tool:urlHash:add'),
      editEnabel: checkPermission('tool:urlHash:edit'),
      removeEnable: checkPermission('tool:urlHash:remove')
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
      console.log(ids)
      const _that = this
      this.$confirm({
        title: '确定移出该链接吗？',
        content: '',
        centered: 'true',
        onOk () {
          deleteLink({ id: ids.join(',') }).then(res => {
            if (res.code === '200') {
              _that.$message.success('删除成功')
              _that.handleOk()
            } else {
              _that.$message.error(res.message)
            }
            _that.selectedRowKeys = []
          })
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
