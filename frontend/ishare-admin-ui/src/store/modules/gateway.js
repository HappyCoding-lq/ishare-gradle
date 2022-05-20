import { getEnumMap, getServiceIdList, getApiPathList, getPathInfoList, getClientList } from '@/api/gateway/route'

export default {
  state: {
    routeTypeMap: {},
    commonYesAndNoMap: {},
    serviceIdList: [],
    authTypeMap: {},
    apiPathList: [],
    pathInfoList: [],
    clientList: []
  },
  namespaced: true,
  mutations: {
    SET_ROUTE_TYPE_MAP: (state, data) => {
      state.routeTypeMap = data
    },
    SET_COMMON_YES_AND_NO_MAP: (state, data) => {
      state.commonYesAndNoMap = data
    },
    SET_SERVICE_ID_LIST: (state, data) => {
      state.serviceIdList = data
    },
    SET_AUTH_TYPE_LIST: (state, data) => {
      state.authTypeMap = data
    },
    SET_API_PATH_LIST: (state, data) => {
      state.apiPathList = data
    },
    SET_PATH_INFO_LIST: (state, data) => {
      state.pathInfoList = data
    },
    SET_CLIENT_LIST: (state, data) => {
      state.clientList = data
    }
  },
  actions: {
    getRouteTypeMap ({ commit }) {
      return new Promise((resolve, reject) => {
        getEnumMap({ class: 'com.chinapost.life.ishare.gateway.enums.RouteTypeEnum' }).then(response => {
          const result = response
          commit('SET_ROUTE_TYPE_MAP', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getCommonYesAndNoMap ({ commit }) {
      return new Promise((resolve, reject) => {
        getEnumMap({ class: 'com.chinapost.life.ishare.gateway.enums.CommonYesAndNoEnum' }).then(response => {
          const result = response
          commit('SET_COMMON_YES_AND_NO_MAP', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getServiceIdList ({ commit }) {
      return new Promise((resolve, reject) => {
        getServiceIdList().then(response => {
          const result = response
          commit('SET_SERVICE_ID_LIST', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getAuthTypeMap ({ commit }) {
      return new Promise((resolve, reject) => {
        getEnumMap({ class: 'com.chinapost.life.ishare.gateway.enums.AuthTypeEnum' }).then(response => {
          const result = response
          commit('SET_AUTH_TYPE_LIST', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getApiPathList ({ commit }) {
      return new Promise((resolve, reject) => {
        getApiPathList().then(response => {
          const result = response
          commit('SET_API_PATH_LIST', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getPathInfoList ({ commit }) {
      return new Promise((resolve, reject) => {
        getPathInfoList().then(response => {
          const result = response
          commit('SET_PATH_INFO_LIST', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    getClientList ({ commit }) {
      return new Promise((resolve, reject) => {
        getClientList().then(response => {
          const result = response
          commit('SET_CLIENT_LIST', result.data)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
