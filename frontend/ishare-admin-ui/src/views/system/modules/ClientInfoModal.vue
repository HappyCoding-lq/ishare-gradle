<template>
  <a-modal
    title="操作"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
  >
    <a-form :form="form">
      <a-form-item style="display:none">
        <a-input v-decorator="['id']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="appId">
        <a-input placeholder="appId" v-decorator="['appid', {rules: [{required: true, message: '请输入appId'}]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="appSecret">
        <a-row :gutter="8">
          <a-col :span="20">
            <a-input disabled placeholder="appSecret" v-decorator="['appsecret', {rules: [{required: true, message: '请输入appSecret'}]}]"/>
          </a-col>
          <a-col :span="4">
            <a-button type="primary" @click="getappSecret">重置</a-button>
          </a-col>
        </a-row>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="应用名称">
        <a-input placeholder="应用名称" v-decorator="['appname', {rules: [{required: true, message: '请输入应用名称'}]}]"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveClientInfo, getappSecret } from '@/api/system/clientInfo'
import pick from 'lodash.pick'
export default {
  name: 'ClientInfoModal',
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
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  created () {
  },
  methods: {
    add () {
      this.form.resetFields()
      this.getappSecret()
      this.edit({})
    },
    edit (record) {
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'id', 'appid', 'appsecret', 'appname', 'description', 'createTime', 'updateTime'))
      })
    },
    getappSecret (e) {
      // e.preventDefault()
      getappSecret().then(res => {
        if (res.code === '200') {
          this.form.setFieldsValue({ appsecret: res.data })
          // this.$message.success('重置成功')
        } else {
          this.$message.error(res.message)
        }
      }).catch(() => {
        this.$message.error('系统错误，请稍后再试')
      }).finally(() => {
      })
    },
    handleSubmit (e) {
      this.$confirm({
        title: '提示',
        content: '确定保存吗？',
        onOk: () => {
          e.preventDefault()
          this.form.validateFields((err, values) => {
            if (!err) {
              console.log('Received values of form: ', values)
              this.confirmLoading = true
              saveClientInfo(values).then(res => {
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
