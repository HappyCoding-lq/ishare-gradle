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
        <a-input v-decorator="['index']"/>
      </a-form-item>
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol">
        <span slot="label">过滤器名字
          <a-tooltip :title="currentFilterObject.objectTips" v-if="currentFilterObject && currentFilterObject.objectTips">
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-select placeholder="请选择过滤器" style="width: 97%" v-decorator="['name', {rules: [{ required: true, message: '请选择过滤器' }]}]" @change="filterTypeChange($event)">
          <a-select-option v-for="(item) in this.filterObjects" :key="item.objectName" :value="item.objectName">{{ item.objectDesc }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-divider v-if="this.argsList.length>0"/>
      <div v-for="(item, index) in this.argsList" :key="item.guid">
        <a-form-item v-if="showTypeOne" :labelCol="labelCol" :wrapperCol="wrapperCol" required>
          <span slot="label">{{ labelName }}
            <a-tooltip :title="item.argTips">
              <a-icon type="question-circle-o" />
            </a-tooltip>
          </span>
          <div>
            <a-input
              v-if="item.argValueControlType === 'input'"
              class="argInput"
              style="width: 97%"
              :placeholder="'请输入' + labelName"
              v-model="item.argValue"
              :required="true"
              @focus.native.capture="inputEvent($event)"/>
            <a-select
              v-if="item.argValueControlType === 'select'"
              class="argInput"
              :placeholder="'请选择' + labelName"
              style="width: 97%"
              v-model="item.argValue"
              allowClear
              @focus.native.capture="inputEvent($event)">
              <a-select-option v-for="(value, indexOfScope) in item.argValueScope" :key="indexOfScope" :value="value">{{ value }}</a-select-option>
            </a-select>
            <a v-if="index>0 && (item.argValueControlType==='input' || item.argValueControlType==='select')" @click="delArg(index)" style="width: 3%"><a-icon type="delete"/></a>
            <div class="ant-form-explain" style="display: none">{{ (item.argValueControlType==='select'? '请选择' : '请输入') + labelName }}</div>
            <a-input
              v-if="item.argValueControlType === 'fixed'"
              style="width: 97%;color: #000"
              v-model="item.argValue"
              disabled/>
          </div>
        </a-form-item>
      </div>
      <div align="center">
        <a-button v-if="addArgAble" type="primary" icon="plus" @click="addArg()">新增</a-button>
      </div>
      <div v-if="showTypeTwo" v-for="(item) in this.argsList" :key="item.guid">
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" :required="item.required">
          <span slot="label">{{ item.argDesc }}
            <a-tooltip :title="item.argTips">
              <a-icon type="question-circle-o" />
            </a-tooltip>
          </span>
          <div>
            <a-input
              v-if="item.argValueControlType === 'input'"
              class="argInput"
              style="width: 97%"
              :placeholder="'请输入' + item.argDesc + (!item.required?'(可为空)':'')"
              v-model="item.argValue"
              :required="item.required"
              @input.native.capture="inputEvent($event)"/>
            <a-select
              v-if="item.argValueControlType === 'select'"
              class="argInput"
              :placeholder="(item.required?'请' : '可') + '选择' + item.argDesc"
              style="width: 97%"
              v-model="item.argValue"
              allowClear
              @focus.native.capture="inputEvent($event)">
              <a-select-option v-for="(value, indexOfScope) in item.argValueScope" :key="indexOfScope" :value="value">{{ value }}</a-select-option>
            </a-select>
            <div class="ant-form-explain" style="display: none">{{ (item.argValueControlType==='select'? ((item.required?'请' : '可') + '选择') : '请输入') + item.argDesc }}</div>
            <a-input
              v-if="item.argValueControlType === 'fixed'"
              style="width: 97%;color: #000"
              v-model="item.argValue"
              disabled/>
          </div>
        </a-form-item>
      </div>
    </a-form>
  </a-modal>
</template>
<script>
import pick from 'lodash.pick'
import { guid } from '@/components/_util/util.js'
import Vue from 'vue'
import { objectList, filterValidCheck } from '@/api/gateway/route'

export default {
  name: 'FilterSaveModal',
  props: {},
  components: {},
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
      filterObjects: null,
      showTypeOne: false,
      showTypeTwo: false,
      addArgAble: false,
      mdl: {},
      argsList: [],
      currentFilterObject: null,
      labelName: '',
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  created () {
  },
  methods: {
    checkObjectRes (data) {
      // 校验获取到的配置数据是否异常
      return true
    },
    init () {
      objectList({ objectType: 'filter' }).then(res => {
        if (res.code === '200' && res.data && res.data.length > 0 && this.checkObjectRes(res.data)) {
          this.filterObjects = res.data
          this.filterObjects.forEach(object => {
            object.objectArgsDefine.forEach(arg => {
              if (arg.argValueControlType === 'select' || arg.argValueControlType === 'fixed') {
                arg.argValueScope = JSON.parse(arg.argValueScope)
              }
            })
          })
        } else {
          this.$message.error('获取配置异常，请联系管理员')
        }
      }).catch(() => {
        this.$notification.error({ message: '系统错误，请联系管理员' })
      })
      if (!this.filterObjects) {
        return false
      }
      this.form.resetFields()
      this.resetParam()
      return true
    },
    resetParam () {
      this.showTypeOne = false
      this.showTypeTwo = false
      this.addArgAble = false
      this.argsList = []
      this.currentFilterObject = {}
      this.labelName = ''
    },
    refreshAddArgAble () {
      const objectArgsDefine = this.currentFilterObject.objectArgsDefine[0]
      if (objectArgsDefine.argValueControlType === 'fixed' || objectArgsDefine.argValueControlType === 'select' && this.argsList.length >= objectArgsDefine.argValueScope.length) {
        this.addArgAble = false
      } else {
        this.addArgAble = true
      }
    },
    addFilter (index) {
      this.editFilter({ newFlag: true, index: index + 1 })
    },
    editFilter (filter) {
      if (!this.init()) {
        return
      }
      this.resetParam()
      this.mdl = Object.assign(filter)
      filter.newFlag = filter.newFlag ? filter.newFlag : false
      if (!filter.newFlag) {
        const filterObject = this.filterObjects.filter(function (item) {
          return item.objectName === filter.name
        })[0]
        this.currentFilterObject = filterObject
        const objectArgsDefine = filterObject.objectArgsDefine
        if (filter.name && filterObject.objectArgType === 'List') {
          this.showTypeOne = true
          this.labelName = objectArgsDefine[0].argDesc
          const scope = {
            argValue: '',
            argTips: objectArgsDefine[0].argTips,
            argValueControlType: objectArgsDefine[0].argValueControlType,
            argValueScope: objectArgsDefine[0].argValueScope,
            guid: guid()
          }
          if (filter.args && objectArgsDefine[0].argValueControlType !== 'fixed') {
            const argsData = JSON.parse(filter.args)
            for (const item in argsData) {
              const copyScope = JSON.parse(JSON.stringify(scope))
              copyScope.argName = item
              copyScope.argValue = argsData[item]
              this.argsList.push(copyScope)
            }
          } else if (objectArgsDefine[0].argValueControlType !== 'fixed') {
            this.argsList.push(scope)
          } else {
            objectArgsDefine[0].argValueScope.forEach(value => {
              const scope = {
                argValue: value,
                argTips: objectArgsDefine[0].argTips,
                required: true,
                argValueControlType: objectArgsDefine[0].argValueControlType,
                argValueScope: objectArgsDefine[0].argValueScope,
                guid: guid()
              }
              this.argsList.push(scope)
            })
          }
          this.refreshAddArgAble()
        }
        if (filter.name && filterObject.objectArgType === 'Map') {
          this.showTypeTwo = true
          if (filter.args) {
            const argsData = JSON.parse(filter.args)
            for (const item in argsData) {
              if (argsData.hasOwnProperty(item)) {
                const argDefine = objectArgsDefine.filter(function (argDefine) { return argDefine.argName === item })[0]
                const scope = {
                  argDesc: argDefine.argDesc,
                  argName: item,
                  argValue: argsData[item],
                  argTips: argDefine.argTips,
                  required: argDefine.required,
                  argValueControlType: argDefine.argValueControlType,
                  argValueScope: argDefine.argValueScope,
                  guid: guid()
                }
                if (argDefine.argValueControlType === 'fixed') {
                  scope.argValue = argDefine.argValueScope[0]
                  scope.required = true
                }
                this.argsList.push(scope)
              }
            }
          } else {
            objectArgsDefine.forEach(argDefine => {
              const scope = {
                argDesc: argDefine.argDesc,
                argName: argDefine.argName,
                argValue: '',
                argTips: argDefine.argTips,
                required: argDefine.required,
                argValueControlType: argDefine.argValueControlType,
                argValueScope: argDefine.argValueScope,
                guid: guid()
              }
              if (argDefine.argValueControlType === 'fixed') {
                scope.argValue = argDefine.argValueScope[0]
                scope.required = true
              }
              this.argsList.push(scope)
            })
          }
        }
        if (filterObject.objectArgType === 'Map') {
          objectArgsDefine.forEach(argDefine => {
            let find = false
            this.argsList.forEach(arg => {
              if (argDefine.argName === arg.argName) {
                find = true
              }
            })
            if (!find) {
              const scope = {
                argDesc: argDefine.argDesc,
                argName: argDefine.argName,
                argTips: argDefine.argTips,
                required: argDefine.required,
                argValueControlType: argDefine.argValueControlType,
                argValueScope: argDefine.argValueScope,
                guid: guid()
              }
              if (argDefine.argValueControlType === 'fixed') {
                scope.argValue = argDefine.argValueScope[0]
                scope.required = true
              }
              this.argsList.push(scope)
            }
          })
        }
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'newFlag', 'index', 'name'))
      })
      this.visible = true
    },
    filterTypeChange (value) {
      this.resetParam()
      const filterObject = this.filterObjects.filter(function (item) {
        return item.objectName === value
      })[0]
      this.currentFilterObject = filterObject
      const objectArgsDefine = filterObject.objectArgsDefine
      if (filterObject.objectArgType === 'List') {
        this.showTypeOne = true
        const scope = {
          argTips: objectArgsDefine[0].argTips,
          argValueControlType: objectArgsDefine[0].argValueControlType,
          argValueScope: objectArgsDefine[0].argValueScope,
          guid: guid()
        }
        if (objectArgsDefine[0].argValueControlType === 'fixed') {
          objectArgsDefine[0].argValueScope.forEach(value => {
            const copyScope = JSON.parse(JSON.stringify(scope))
            copyScope.argValue = value
            copyScope.required = true
            this.argsList.push(copyScope)
          })
        } else {
          this.argsList.push(scope)
        }
        this.labelName = objectArgsDefine[0].argDesc
        this.refreshAddArgAble()
      }
      if (filterObject.objectArgType === 'Map') {
        this.showTypeTwo = true
        objectArgsDefine.forEach(argDefine => {
          const scope = {
            argDesc: argDefine.argDesc,
            argName: argDefine.argName,
            argTips: argDefine.argTips,
            required: argDefine.required,
            argValueControlType: argDefine.argValueControlType,
            argValueScope: argDefine.argValueScope,
            guid: guid()
          }
          if (argDefine.argValueControlType === 'fixed') {
            scope.argValue = argDefine.argValueScope[0]
            scope.required = true
          }
          this.argsList.push(scope)
        })
      }
    },
    addArg () {
      const currentObject = this.currentFilterObject
      const objectArgsDefine = currentObject.objectArgsDefine[0]
      const cope = {
        argTips: objectArgsDefine.argTips,
        argValueControlType: objectArgsDefine.argValueControlType,
        argValueScope: objectArgsDefine.argValueScope,
        guid: guid()
      }
      this.argsList.push(cope)
      this.refreshAddArgAble()
    },
    delArg (index) {
      if (this.argsList.length === 1) {
        const argInputs = Array.from(document.getElementsByClassName('argInput'))
        argInputs.forEach(item => {
          if (!item.value || !item.value.trim()) {
            item.parentNode.classList.add('ant-form-item')
            item.parentNode.classList.add('has-error')
            item.parentNode.lastElementChild.style = null
          }
        })
        return
      }
      this.argsList.splice(index, 1)
      this.refreshAddArgAble()
    },
    inputEvent (event) {
      const target = event.target
      target.parentNode.classList.remove('ant-form-item')
      target.parentNode.classList.remove('has-error')
      target.parentNode.lastElementChild.style = 'display: none;'
    },
    checkInput () {
      let flag = true
      const argInputs = Array.from(document.getElementsByClassName('argInput'))
      if (argInputs.length > 0) {
        const argValueControlType = this.currentFilterObject.objectArgsDefine[0].argValueControlType
        argInputs.forEach(item => {
          if (argValueControlType === 'select' && item.innerText.startsWith('请选择') || argValueControlType === 'input' && item.required && (!item.value || !item.value.trim())) {
            item.parentNode.classList.add('ant-form-item')
            item.parentNode.classList.add('has-error')
            item.parentNode.lastElementChild.style = null
            flag = false
          }
        })
      }
      return flag
    },
    handleSubmit (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err && this.checkInput()) {
          this.confirmLoading = true
          const data = {}
          data.args = {}
          if (this.showTypeOne === true) {
            this.argsList.forEach(item => {
              Vue.set(data.args, '_genkey_' + this.argsList.indexOf(item), item.argValue.trim())
            })
          }
          if (this.showTypeTwo === true) {
            this.argsList.forEach(item => {
              if (item.argValue && item.argValue.trim()) {
                Vue.set(data.args, item.argName, item.argValue.trim())
              }
            })
          }
          data.name = values.name
          filterValidCheck({ filterStr: JSON.stringify(data) }).then(res => {
            if (res.code === '200') {
              data.args = JSON.stringify(data.args)
              if (data.args === '{}') {
                delete data.args
              }
              data.newFlag = values.newFlag
              data.index = values.index
              this.$emit('cancel', data)
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
  }
}
</script>
