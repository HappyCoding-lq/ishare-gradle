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
        <a-input v-decorator="['newFlag']"/>
      </a-form-item>
      <a-form-item style="display:none">
        <a-input v-decorator="['routeId']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">路由转发目标
          <a-tooltip title="例如：lb://微服务名、http://xxxx">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-select
          v-if="showRouteUriType1"
          showSearch
          allowClear
          style="width: 80%;"
          placeholder="请选择转发到的微服务"
          v-decorator="['routeUriDetail1', {rules: [{ required: true, message: '请选择转发到的微服务' }]}]" >
          <a-select-option v-for="(value, index) in this.serviceIdList" :key="index" :value="value">{{ value }}</a-select-option>
        </a-select>
        <a-input v-if="showRouteUriType2" style="width: 80%;" placeholder="请输入具体地址" v-decorator="['routeUriDetail2', {rules: [{ required: true, message: '请输入具体地址' }]}]"/>
        <a-select style="width: 20%;float:left;padding-top: 4px;" v-decorator="['routeUriPrefix', {initialValue:'lb://'}, {rules: [{ required: true, message: '请选择' }]}]" @change="routeUriTypeChange($event)">
          <a-select-option :value="'lb://'">lb://</a-select-option>
          <a-select-option :value="'http://'">http://</a-select-option>
          <a-select-option :value="'https://'">https://</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">路由信息描述
          <a-tooltip title="例如：XX项目XXXX接口路由匹配">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入路由信息描述" v-decorator="['routeDesc', {rules: [{ required: true, message: '请输入路由信息描述' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">路由类型
          <a-tooltip title="路由类型">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-select ref="test" allowClear placeholder="请选择路由类型" v-decorator="['routeType', {rules: [{ required: true, message: '请选择路由类型' }]}]" @change="apiTransCodeEnableChange($event)">
          <a-select-option v-for="(value, key) in routeTypeMap" :key="key" :value="key">{{ value }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="showApiTransCodeInput">
        <span slot="label">api接口交易编码
          <a-tooltip title="对外提供的api接口交易编码，路由类型选择【开放API】时必录">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input placeholder="请输入api接口交易编码" v-decorator="['apiTransCode', {rules: [{ required: true, message: '请输入api接口交易编码' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" v-if="showApiTransCodeInput">
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
        <span slot="label">路由匹配优先级
          <a-tooltip title="数字越小优先级越高（请求匹配到多个路由时，按优先级最高的处理）">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input-number placeholder="请输入路由匹配优先级(数字越小优先级越高)" v-decorator="['routeOrder', {initialValue:'1',rules: [{ required: true, message: '请输入顺序数字(数字越小优先级越高)' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">需要登录/授权
          <a-tooltip title="匹配到该路由的请求是否需要登录/授权">
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
          <a-tooltip title="匹配到该路由的请求是否需要白名单限制">
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
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">断言JSON字符串
          <a-tooltip title="断言(谓词)是路由转发的判断条件，目前SpringCloud Gateway支持多种方式，常见如：Path、Query、Method、Header等；list json格式，参考标准断言配置，不能为空和[]">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a @click="predicatesModal($refs.predicates.value)"><a-icon type="edit" />编辑</a>
        <a v-if="$refs.predicates && $refs.predicates.value" @click="clearPredicates()" style="margin-left: 50px;"><a-icon type="delete" />清除</a>
        <a v-if="this.mdl.lastPredicates && (!$refs.predicates || !$refs.predicates.value)" @click="recoveryPredicates()" style="margin-left: 50px;"><a-icon type="undo" />还原</a>
        <a-textarea
          ref="predicates"
          rows="9"
          disabled
          clearable
          placeholder="例：
            [
                {
                    &quot;name&quot;: &quot;Path&quot;,
                    &quot;args&quot;: {
                        &quot;pattern&quot;:&quot;/auth/**&quot;
                    }
                }
            ]"
          v-decorator="['predicates', {rules: [{ required: true, message: '请编辑断言，不能为空' }]}]"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">过滤器JSON字符串
          <a-tooltip title="过滤器是路由转发请求时所经过的过滤逻辑，可用于修改请求、响应内容；list json格式，参考标准过滤器配置，可以不配置">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a @click="filtersModal($refs.filters.value)"><a-icon type="edit" />编辑</a>
        <a v-if="$refs.filters && $refs.filters.value" @click="clearFilters()" style="margin-left: 50px;"><a-icon type="delete" />清除</a>
        <a v-if="this.mdl.lastFilters && (!$refs.filters || !$refs.filters.value)" @click="recoveryFilters()" style="margin-left: 50px;"><a-icon type="undo" />还原</a>
        <a-textarea
          ref="filters"
          rows="9"
          disabled
          placeholder="例：
            [
                {
                    &quot;name&quot;: &quot;StripPrefix&quot;,
                    &quot;args&quot;: {
                        &quot;_genkey_0&quot;: &quot;1&quot;
                    }
                }
            ]"
          v-decorator="['filters']"/>
      </a-form-item>
    </a-form>
    <Predicates-modal ref="predicatesModal" @cancel="receivePredicatesCancel" />
    <Filters-modal ref="filtersModal" @cancel="receiveFiltersCancel" />
  </a-modal>
</template>
<script>
import { saveRoute } from '@/api/gateway/route'
import PredicatesModal from './PredicatesModal.vue'
import FiltersModal from './FiltersModal.vue'
import pick from 'lodash.pick'
import { guid } from '@/components/_util/util.js'
import { loadRouter } from '@/utils/routerUtil'

export default {
  name: 'RouteSaveModal',
  props: {
  },
  components: {
    PredicatesModal,
    FiltersModal
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
      showRouteUriType1: false,
      showRouteUriType2: false,
      showApiTransCodeInput: false,
      showIpsInput: false,
      ipTagsList: [],
      ipTagText: '',
      ipTagTextVisible: false,
      addIpButtonEnable: true,
      routeTypeMap: {},
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
    routeUriTypeChange (value) {
      this.showRouteUriType1 = value === 'lb://'
      this.showRouteUriType2 = value !== 'lb://'
    },
    apiTransCodeEnableChange (value) {
      if (value === 'openAPI') {
        this.$confirm({
          title: '提示',
          content: '开放API需要前往API接口管理维护！要去吗？',
          onOk: () => {
            this.form.setFields({ routeType: this.$refs.test.defaultValue })
            const apiManageRoute = loadRouter('gateway/ApiRouteList')
            apiManageRoute.params = {}
            this.$router.push(apiManageRoute)
          },
          onCancel: () => {
            this.form.setFields({ routeType: this.$refs.test.defaultValue })
          }
        })
      }
      // this.showApiTransCodeInput = value === 'openAPI'
      // this.$nextTick(() => {
      // this.form.setFieldsValue(pick(this.mdl, 'apiTransCode'))
      // })
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
    predicatesModal (value) {
      const predicatesStr = {}
      predicatesStr.rows = JSON.parse(value || '[]')
      this.$refs.predicatesModal.show(predicatesStr)
    },
    filtersModal (value) {
      const filtersStr = {}
      filtersStr.rows = JSON.parse(value || '[]')
      this.$refs.filtersModal.show(filtersStr)
    },
    routeAdd (routeTypeMap, serviceIdList, clientList) {
      this.routeEdit({ newFlag: true }, routeTypeMap, serviceIdList, clientList)
    },
    routeEdit (record, routeTypeMap, serviceIdList, clientList) {
      this.ipTagsList = []
      this.clientIdTagList = []
      this.form.resetFields()
      this.mdl = Object.assign(record)
      this.routeTypeMap = routeTypeMap
      this.serviceIdList = serviceIdList
      this.clientList = clientList
      this.usableClientList = clientList
      this.visible = true
      this.showRouteUriType1 = false
      this.showRouteUriType2 = false
      this.showApiTransCodeInput = this.mdl.routeType === 'openAPI'
      this.showIpsInput = this.mdl.trustedIpsEnable === 'YES'
      this.showClientIdChoose = !this.mdl.needCheckAuth || this.mdl.needCheckAuth === 'YES'
      if (record.routeUri) {
        record.routeUriPrefix = record.routeUri.substring(0, record.routeUri.indexOf('://') + 3)
        if (record.routeUriPrefix === 'lb://') {
          this.showRouteUriType1 = true
          record.routeUriDetail1 = record.routeUri.substring(record.routeUri.indexOf('://') + 3)
        } else {
          this.showRouteUriType2 = true
          record.routeUriDetail2 = record.routeUri.substring(record.routeUri.indexOf('://') + 3)
        }
      } else {
        this.showRouteUriType1 = true
      }
      if (!record.apiFactorsVerify) {
        delete this.mdl.apiFactorsVerify
      }
      if (!record.isLogAble) {
        this.mdl.isLogAble = 'NO'
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'newFlag', 'routeId', 'routeUriPrefix', 'routeUriDetail1', 'routeUriDetail2', 'routeUri', 'routeDesc', 'routeType', 'needCheckAuth', 'trustedIpsEnable', 'routeOrder', 'predicates', 'filters', 'isLogAble', 'apiTransCode', 'apiFactorsVerify', 'pageManageOperateAble', 'isDelete', 'createTime', 'updateTime'))
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
    receivePredicatesCancel (predicates) {
      this.form.setFieldsValue({ 'predicates': predicates })
    },
    receiveFiltersCancel (filters) {
      this.form.setFieldsValue({ 'filters': filters })
    },
    clearPredicates () {
      this.mdl.lastPredicates = this.form.getFieldValue('predicates')
      this.form.setFieldsValue({ 'predicates': null })
    },
    recoveryPredicates () {
      this.form.setFieldsValue({ 'predicates': this.mdl.lastPredicates })
    },
    clearFilters () {
      this.mdl.lastFilters = this.form.getFieldValue('filters')
      this.form.setFieldsValue({ 'filters': null })
    },
    recoveryFilters () {
      this.form.setFieldsValue({ 'filters': this.mdl.lastFilters })
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
          return
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
          values.routeUri = values.routeUriPrefix + (values.routeUriDetail1 || values.routeUriDetail2)
          delete values.routeUriPrefix
          delete values.routeUriDetail
          if (values.filters) {
            values.filters = values.filters.trim()
          }
          if (!this.specialCheck(values)) {
            return
          }
          if (values.newFlag) {
            values.isEnable = 'NO'
          } else {
            values.isEnable = this.mdl.isEnable
          }
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
