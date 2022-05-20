import validator from 'validator'
const v = validator.default
export const AntdFormValidateMixin = {
  methods: {
    mobileValidator (rule, value, callbackFun) {
      console.log(v.isMobilePhone(value, 'zh-CN'))
      if (!v.isMobilePhone(value) || !/^1[0-9]{10}$/.test(value)) {
        callbackFun('电话号码格式不合法')
      } else {
        callbackFun()
      }
    }
  }
}
