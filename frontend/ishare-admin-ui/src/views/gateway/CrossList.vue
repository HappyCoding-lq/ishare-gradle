<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="9" :sm="15">
            <a-form-item label="请求路径">
              <a-input allowClear placeholder="请输入，支持模糊查询" v-model="queryParam.path"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="15">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="() => queryParam = {}">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button v-if="addEnable" type="primary" icon="plus" @click="handleAdd">新增配置</a-button>
      <a-dropdown v-if="cleanEnable&&selectedRows.length > 0">
        <a-button type="danger" icon="delete" @click="delByPath2(selectedRows)">删除</a-button>
      </a-dropdown>
      <a-button style="float: right;" v-if="refreshEnable" type="primary" icon="reload" @click="refresh()">重载所有配置</a-button>
    </div>
    <s-table
      size="default"
      ref="table"
      :rowSelection="{selectedRows: selectedRows, onChange: onSelectChange}"
      :columns="columns"
      :data="loadData"
      :pagination="{showTotal : total => {return '总共 ' + total + ' 条 '}}"
      :showPagination="false"
    >
      <span slot="action" slot-scope="text, record">
        <a @click="dataListList(record.path)"><a-icon type="bars" />跨域列表</a>
        <a-divider v-if="cleanEnable" type="vertical" @click="handleEdit(record)"/>
        <a v-if="cleanEnable" @click="delByPath(record.path)">清除配置</a>
      </span>
    </s-table>
    <Cross-data-save-modal ref="saveModal" @ok="handleOk"/>
    <Cross-data-list-modal ref="dataListModal"/>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import { getPathList, deletePathCross, refreshCross } from '@/api/gateway/cross'
import CrossDataSaveModal from './modules/CrossDataSaveModal.vue'
import CrossDataListModal from './modules/CrossDataListModal.vue'
import { checkPermission } from '@/utils/permissions'
import TagSelectOption from '../../components/TagSelect/TagSelectOption'

export default {
  name: 'CrossList',
  components: {
    TagSelectOption,
    STable,
    CrossDataSaveModal,
    CrossDataListModal
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
          title: '序号',
          dataIndex: 'index',
          align: 'center',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '请求路径',
          dataIndex: 'path',
          align: 'center'
        },
        {
          title: '操作',
          align: 'center',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        return getPathList(this.queryParam)
      },
      selectedRows: [],
      addEnable: checkPermission('gateway:cross:add'),
      refreshEnable: checkPermission('gateway:cross:refresh'),
      cleanEnable: checkPermission('gateway:cross:clean')
    }
  },
  filters: {
  },
  created () {
  },
  mounted () {
  },
  computed: {
  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    dataListList (path) {
      this.$refs.dataListModal.show(path)
    },
    handleAdd () {
      this.$refs.saveModal.add(false, '')
    },
    handleOk () {
      this.$refs.table.refresh(false)
    },
    delByPath2 (rows) {
      let path = ''
      rows.forEach(item => {
        path += item.path + ','
      })
      path = path.substring(0, path.length - 1)
      this.delByPath(path)
    },
    delByPath (path) {
      this.$confirm({
        title: '提示',
        content: '真的要清除吗？慎重！！！',
        onOk: () => {
          deletePathCross({ path: path }).then(res => {
            if (res.code === '200') {
              this.$message.success('清除成功')
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
        content: '确定重载所有配置信息？慎重！！！',
        onOk: () => {
          refreshCross().then(res => {
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
