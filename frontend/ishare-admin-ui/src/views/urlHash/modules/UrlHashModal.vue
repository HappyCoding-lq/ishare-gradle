<template>
  <a-modal
    title="操作"
    :width="800"
    v-model="visible"
    :confirmLoading="confirmLoading"
    centered
    :mask="false"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <a-form :form="form">
      <a-form-item style="display:none">
        <a-input v-decorator="['id']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述信息">
        <a-input placeholder="请输入描述信息" v-decorator="['description', {rules: [{required: true, message: '请输入描述信息'}]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="URL链接">
        <a-input placeholder="请输入URL链接" v-decorator="['lurl', {rules: [{required: true, message: '请输入URL链接'}]}]"/>
      </a-form-item>
      <a-form-item v-if="status === 'edit'" :labelCol="labelCol" :wrapperCol="wrapperCol" label="创建人">
        <a-input placeholder="请输入创建人" disabled v-decorator="['createUser', {rules: [{required: false, message: '请输入创建人'}]}]"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { addOrUpdate } from '@/api/urlhash/urlhash'
import pick from 'lodash.pick'
export default {
  name: 'UrlHashModal',
  props: {
  },
  components: {
  },
  data () {
    return {
      status: 'add',
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
      this.status = 'add'
      this.form.resetFields()
      this.visible = true
    },
    edit (record) {
      this.status = 'edit'
      this.mdl = Object.assign(record)
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'id', 'description', 'lurl', 'surl', 'createUser', 'gmtCreate', 'updateTime'))
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          this.confirmLoading = true
          addOrUpdate(values).then(res => {
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
