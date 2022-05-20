import { constantRouterMap } from '@/config/router.config'
import { generatorDynamicRouter } from '@/utils/routerUtil'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    // 将后端返回的路由拼接
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    /**
     * 构建获取和构建路由和菜单信息
     * @param commit
     * @param data
     * @returns {Promise<any>}
     * @constructor
     */
    GenerateRoutes ({ commit }, data) {
      // 明天去对应看看后端返回的数据是什么样子，根据样子去创建路由就ok了 （请求路由数据）
      return new Promise(resolve => {
        generatorDynamicRouter(data).then(routers => {
          console.log(routers)
          commit('SET_ROUTERS', routers)
          resolve()
        })
      })
    }
  }
}

export default permission
