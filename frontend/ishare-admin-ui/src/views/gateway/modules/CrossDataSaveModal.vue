<template>
  <a-modal
    title="新增/修改操作"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
  >
    <a-form :form="form">
      <a-form-item style="display:none">
        <a-input v-decorator="['newFlag']"/>
      </a-form-item>
      <a-form-item style="display:none">
        <a-input v-decorator="['id']"/>
      </a-form-item>
      <a-form-item v-if="!pathFlag" :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">请求路径
          <a-tooltip title="例如：/service/XXXX">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入请求路径" v-decorator="['path', {rules: [{ required: true, message: '请输入请求路径' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">允许的域
          <a-tooltip title="例如：https://XXX.XXX.XXX">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入允许的域" v-decorator="['allowOrigin', {rules: [{ required: true, message: '请输入允许的域' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">配置描述
          <a-tooltip title="例如：XXX请求中台网关">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入配置描述" v-decorator="['description', {rules: [{ required: true, message: '请输入配置描述' }]}]"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveCross } from '@/api/gateway/cross'
import pick from 'lodash.pick'

export default {
  name: 'CrossDataSaveModal',
  props: {
  },
  components: {
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
      confirmLoading: false,
      mdl: {},
      form: this.$form.createForm(this),
      pathFlag: false
    }
  },
  beforeCreate () {
  },
  created () {
  },
  methods: {
    add (pathFlag, path) {
      this.edit(pathFlag, { path: path, newFlag: true })
    },
    edit (pathFlag, record) {
      if (pathFlag) {
        this.pathFlag = true
      } else {
        this.pathFlag = false
      }
      this.form.resetFields()
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'newFlag', 'id', 'path', 'allowOrigin', 'description'))
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          this.$confirm({
            title: '提示',
            content: '确定保存吗？',
            onOk: () => {
              this.confirmLoading = true
              if (this.pathFlag) {
                values.path = this.mdl.path
              }
              saveCross(values).then(res => {
                if (res.code === '200') {
                  this.$message.success('保存成功')
                  this.$emit('ok')
                  this.visible = false
                } else {
                  this.$message.error(res.message)
                }
              }).catch(() => {
                this.$message.error('系统错误，请稍后再试')
              }).finally(() => {
                this.confirmLoading = false
              })
            },
            onCancel () {
            }
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
