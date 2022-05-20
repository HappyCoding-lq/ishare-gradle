module.exports = {
  'production': {
    proxy: {
      '/api': { // 中台网关
        target: 'https://ishare-gw.e-hqins.com'
      }
    }
  },
  'develop': {
    proxy: {
      '/api': { // 中台网关
        target: 'http://10.9.10.46:9527'
      }
    }
  },
  'dat': {
    proxy: {
      '/api': { // 中台网关
        target: 'http://10.9.10.46:9527'
      }
    }
  },
  'dat4': {
    proxy: {
      '/api': { // 中台网关
        target: 'http://ishare-gateway:9527/'
      }
    }
  },
  'uat': {
    proxy: {
      '/api': { // 中台网关
        target: 'http://172.16.15.103:9527'
      }
    }
  }

}
