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
        <a-input v-decorator="['routeId']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">转发到的微服务
          <a-tooltip title="转发到的微服务">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-select
          ref="routeUriDetail"
          showSearch
          allowClear
          placeholder="请选择转发到的微服务"
          v-decorator="['routeUriDetail', {rules: [{ required: true, message: '请选择转发到的微服务' }]}]"
          @change="routeUriDetailChange($event)">
          <a-select-option v-for="(value, index) in this.serviceIdList" :key="index" :value="value">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">微服务接口路径
          <a-tooltip title="微服务接口路径，必须以'/'开头 如 /XX/XX">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input allowClear ref="servicePath" placeholder="请输入微服务接口路径" v-decorator="['servicePath', {rules: [{ required: true, message: '请输入微服务接口路径' }]}]" @blur="inputServicePath($event)" />
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">对外接口路径
          <a-tooltip title="对外接口路径，根据微服务接口路径自动生成">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input disabled placeholder="根据微服务名和微服务接口路径自动生成" v-decorator="['apiPath', {rules: [{ required: true, message: '请输入对外接口路径' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">对外接口交易编码
          <a-tooltip title="对外提供的api接口交易编码，根据微服务名和微服务接口路径自动生成">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input disabled placeholder="根据微服务名和微服务接口路径自动生成" v-decorator="['apiTransCode', {rules: [{ required: true, message: '请输入对外接口交易编码' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">对外接口描述
          <a-tooltip title="例如：XX项目XXXX接口">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入对外接口描述" v-decorator="['routeDesc', {rules: [{ required: true, message: '请输入对外接口描述' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">API接口要素验证
          <a-tooltip title="选择是否进行API接口要素验证，验证transCode、transSource、transNo、transTime">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-decorator="['apiFactorsVerify',{initialValue:'YES'}]">
          <a-radio :value="'YES'">是</a-radio>
          <a-radio :value="'NO'">否</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">需要登录/授权
          <a-tooltip title="匹配到该接口的请求是否需要登录/授权">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-decorator="['needCheckAuth',{initialValue:'YES'}]" @change="needCheckAuthChange($event)">
          <a-radio :value="'YES'">是</a-radio>
          <a-radio :value="'NO'">否</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" required="true" v-if="showClientIdChoose">
        <span slot="label">登录/授权客户端ID
          <a-tooltip title="可以添加多个客户端ID">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-tag v-for="(item, index) in clientIdTagList" :key="item.tagId" closable="true" @close="deleteClientId(index)">
          {{ item.tagText }}
        </a-tag>
        <a-select
          v-if="clientIdTagTextVisible"
          showSearch
          allowClear
          placeholder="请选择客户端"
          v-decorator="['clientIdTagText', {rules: [{ required: true, message: '请选择客户端' }]}]"
          @change="clientIdSubmit($event)"
          @blur="clientIdSubmit($event)"
          style="width: 40%">
          <a-select-option value="*"><span style="color: red">*（全部）</span></a-select-option>
          <a-select-option v-for="(item, index) in this.usableClientList" :key="index" :value="item['appId']">
            <a-tooltip v-if="(item['appId'] + '(' + item['appName'] + ')').length>15" :title="item['appId'] + '(' + item['appName'] + ')'">
              {{ (item['appId'] + '(' + item['appName'] + ')').substring(0, 15) + '...' }}
            </a-tooltip>
            <span v-if="(item['appId'] + '(' + item['appName'] + ')').length<=15">{{ item['appId'] + '(' + item['appName'] + ')' }}</span>
          </a-select-option>
        </a-select>
        <a-button v-if="addClientIdButtonEnable" type="primary" icon="plus" size="small" @click="addClientIdInput()"/>
        <span v-if="this.clientIdTagList.length===0 && !clientIdTagTextVisible" style="color: red;margin-left: 5px;">请添加授权客户端</span>
        <a-button v-if="clearClientIdButtonEnable" style="margin-left: 5px;" type="primary" size="small" @click="clearClientIdInput()">清除</a-button>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">启用IP白名单
          <a-tooltip title="匹配到该接口的请求是否需要白名单限制">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-decorator="['trustedIpsEnable',{initialValue:'NO'}]" @change="ipEnableChange($event)">
          <a-radio :value="'YES'">是</a-radio>
          <a-radio :value="'NO'">否</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="showIpsInput" required="true">
        <span slot="label">白名单IP
          <a-tooltip title="可以添加多个IP，支持Pattern正则表达式，如：133.123.43.21、217.43.3.*">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-tag v-for="(item, index) in ipTagsList" :key="item.tagId" closable="true" @close="deleteIp(index)">
          {{ item.tagText }}
        </a-tag>
        <a-input v-if="ipTagTextVisible" v-model="ipTagText" style="width: 30%" placeholder="请输入合法的IP匹配" @blur="ipSubmit()"/>
        <a-button v-if="addIpButtonEnable" type="primary" icon="plus" size="small" @click="addIpInput()"/>
        <span v-if="this.ipTagsList.length===0 && !ipTagTextVisible" style="color: red;margin-left: 5px;">请添加白名单IP</span>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">记录请求日志
          <a-tooltip title="选择是否记录请求日志">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-decorator="['isLogAble',{initialValue:'NO'}]">
          <a-radio :value="'YES'">是</a-radio>
          <a-radio :value="'NO'">否</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">是否能在页面修改
          <a-tooltip title="选择'是'后续可以在页面做修改等操作；选择'否'之后不能在页面修改">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-decorator="['pageManageOperateAble',{initialValue:'YES'}]">
          <a-radio :value="'YES'">是</a-radio>
          <a-radio :value="'NO'">否</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script>
import { saveRoute, generateApiInfo } from '@/api/gateway/route'
import pick from 'lodash.pick'
import { guid } from '@/components/_util/util.js'

export default {
  name: 'ApiRouteSaveModal',
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
      showIpsInput: false,
      ipTagsList: [],
      ipTagText: '',
      ipTagTextVisible: false,
      addIpButtonEnable: true,
      serviceIdList: [],
      showClientIdChoose: true,
      clientList: [],
      clientIdTagList: [],
      usableClientList: [],
      clientIdTagTextVisible: false,
      addClientIdButtonEnable: true,
      clearClientIdButtonEnable: false,
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  created () {
  },
  methods: {
    routeUriDetailChange (value) {
      if (!value) {
        this.form.setFieldsValue({
          apiPath: '根据微服务名和微服务接口路径自动生成',
          apiTransCode: '根据微服务名和微服务接口路径自动生成'
        })
        return
      }
      if (this.$refs.servicePath && this.$refs.servicePath.value) {
        generateApiInfo({ serviceId: value, servicePath: this.$refs.servicePath.value }).then(res => {
          if (res.code === '200') {
            this.form.setFieldsValue({
              apiPath: res.data.apiPath,
              apiTransCode: res.data.apiTransCode
            })
          } else {
            this.$message.error(res.message)
            this.form.setFieldsValue({
              servicePath: '',
              apiPath: '根据微服务名和微服务接口路径自动生成',
              apiTransCode: '根据微服务名和微服务接口路径自动生成'
            })
          }
        }).catch(() => {
          this.$message.error('系统错误，请稍后再试')
        })
      }
    },
    inputServicePath (event) {
      const target = event.target
      if (target.value) {
        this.$confirm({
          title: '提示',
          content: '微服接口路径确定为：' + target.value + ' ？',
          onOk: () => {
            if (this.$refs.routeUriDetail && this.$refs.routeUriDetail.value) {
              generateApiInfo({ serviceId: this.$refs.routeUriDetail.value, servicePath: target.value }).then(res => {
                if (res.code === '200') {
                  this.form.setFieldsValue({
                    apiPath: res.data.apiPath,
                    apiTransCode: res.data.apiTransCode
                  })
                } else {
                  this.$message.error(res.message)
                  this.form.setFieldsValue({
                    servicePath: '',
                    apiPath: '根据微服务名和微服务接口路径自动生成',
                    apiTransCode: '根据微服务名和微服务接口路径自动生成'
                  })
                }
              }).catch(() => {
                this.$message.error('系统错误，请稍后再试')
              })
            }
          },
          onCancel: () => {
          }
        })
      }
    },
    ipEnableChange (e) {
      this.showIpsInput = e.target.value === 'YES'
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'trustedIps'))
      })
    },
    addIpInput () {
      this.ipTagTextVisible = true
      this.ipTagText = ''
      this.addIpButtonEnable = false
    },
    ipSubmit () {
      const regexp = /^((25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d|\*)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|[1-9]|\*)$/
      if (!this.ipTagText || !regexp.test(this.ipTagText) || this.ipTagText === '*.*.*.*') {
        this.$message.error('IP不合法，请重新添加')
        this.ipTagText = null
      }
      if (this.ipTagText && this.ipTagText.trim()) {
        const existFlag = this.ipTagsList.filter(item => {
          return item.tagText === this.ipTagText.trim()
        }).length > 0
        if (existFlag) {
          this.$message.error('请勿重复添加')
        } else {
          this.ipTagsList.push({ 'tagId': guid(), 'tagText': this.ipTagText.trim() })
        }
      }
      this.ipTagTextVisible = false
      this.addIpButtonEnable = true
    },
    deleteIp (index) {
      this.ipTagsList.splice(index, 1)
    },
    needCheckAuthChange (e) {
      this.showClientIdChoose = e.target.value === 'YES'
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'authClients'))
      })
    },
    addClientIdInput () {
      if (this.usableClientList.length <= 0) {
        this.$message.error('无可添加的客户端')
        return
      }
      this.clientIdTagTextVisible = true
      this.addClientIdButtonEnable = false
      this.clearClientIdButtonEnable = false
    },
    clearClientIdInput () {
      this.clientIdTagList = []
      this.loadUsableClientList()
    },
    clientIdSubmit (value) {
      if (value && value === '*') {
        this.clientIdTagList = []
        this.clientList.forEach(item => {
          this.clientIdTagList.push({ 'tagId': guid(), 'tagText': item.appId })
        })
      } else if (value) {
        this.clientIdTagList.push({ 'tagId': guid(), 'tagText': value })
      }
      this.loadUsableClientList()
    },
    loadUsableClientList () {
      this.clientIdTagTextVisible = false
      this.usableClientList = this.clientList.filter(item => {
        let add = true
        this.clientIdTagList.forEach(tag => {
          if (item.appId === tag.tagText) {
            add = false
          }
        })
        return add
      })
      this.addClientIdButtonEnable = this.usableClientList.length > 0
      this.clearClientIdButtonEnable = this.clientIdTagList.length > 0
    },
    deleteClientId (index) {
      this.clientIdTagList.splice(index, 1)
      this.loadUsableClientList()
    },
    routeAdd (serviceIdList, clientList) {
      this.routeEdit({ newFlag: true }, serviceIdList, clientList)
    },
    routeEdit (record, serviceIdList, clientList) {
      this.ipTagsList = []
      this.clientIdTagList = []
      this.form.resetFields()
      this.mdl = Object.assign(record)
      this.serviceIdList = serviceIdList
      this.clientList = clientList
      this.usableClientList = clientList
      this.visible = true
      this.showIpsInput = this.mdl.trustedIpsEnable && this.mdl.trustedIpsEnable === 'YES'
      this.showClientIdChoose = !this.mdl.needCheckAuth || this.mdl.needCheckAuth === 'YES'
      if (record.routeUri) {
        record.routeUriDetail = record.routeUri.substring(record.routeUri.indexOf('://') + 3)
      }
      if (record.filters) {
        const filters = JSON.parse(record.filters)
        this.mdl.apiPath = filters[0].args.regexp
        this.mdl.servicePath = filters[0].args.replacement
      }
      if (!record.apiFactorsVerify) {
        delete this.mdl.apiFactorsVerify
      }
      if (!record.isLogAble) {
        this.mdl.isLogAble = 'YES'
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'newFlag', 'routeId', 'apiPath', 'servicePath', 'routeUriDetail', 'routeDesc', 'needCheckAuth', 'trustedIpsEnable', 'isLogAble', 'apiTransCode', 'apiFactorsVerify', 'pageManageOperateAble'))
      })
      if (record.trustedIps) {
        record.trustedIps.split(',').forEach(item => {
          this.ipTagsList.push({ 'tagId': guid(), 'tagText': item })
        })
      }
      if (record.authClients) {
        record.authClients.split(',').forEach(item => {
          this.clientIdTagList.push({ 'tagId': guid(), 'tagText': item })
        })
      }
      this.loadUsableClientList()
    },
    specialCheck (values) {
      // 客户端非空校验
      if (values.needCheckAuth === 'YES') {
        if (this.clientIdTagList.length === 0) {
          this.$message.error('授权客户端不能为空，请添加！')
          this.confirmLoading = false
          return false
        }
        values.authClients = ''
        this.clientIdTagList.forEach(item => {
          values.authClients += item.tagText + ','
        })
        values.authClients = values.authClients.substring(0, values.authClients.length - 1)
      }
      // 白名单ip非空校验
      if (values.trustedIpsEnable === 'YES') {
        if (this.ipTagsList.length === 0) {
          this.$message.error('白名单IP不能为空，请添加！')
          this.confirmLoading = false
          return false
        }
        values.trustedIps = ''
        this.ipTagsList.forEach(item => {
          values.trustedIps += item.tagText + ','
        })
        values.trustedIps = values.trustedIps.substring(0, values.trustedIps.length - 1)
      }
      return true
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          this.confirmLoading = true
          values.routeUri = 'lb://' + values.routeUriDetail
          delete values.routeUriDetail
          if (!this.specialCheck(values)) {
            return
          }
          if (values.newFlag) {
            values.isEnable = 'NO'
          } else {
            values.isEnable = this.mdl.isEnable
          }
          values.routeType = 'openAPI'
          values.routeOrder = 1
          values.predicates = '[{"name":"Path","args":{"pattern":"' + values.apiPath + '"}}]'
          values.filters = '[{"name":"RewritePath","args":{"regexp":"' + values.apiPath + '","replacement":"' + values.servicePath + '"}}]'
          saveRoute(values).then(res => {
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
