import { all } from '@/api/tool'

export default {
  state: {
    componentUrls: {}
  },
  namespaced: true,
  mutations: {
    SET_ALL_URL: (state, rows) => {
      const urlMap = {}
      if (rows) {
        for (const row of rows) {
          if (row.componetName && row.url && row.componetName !== '' && row.url !== '') {
            urlMap[ row.componetName ] = row.url
          }
        }
      }
      state.componentUrls = urlMap
    }
  },
  actions: {
    allUrls ({ commit, state }) {
      return new Promise((resolve, reject) => {
        all().then(response => {
          const result = response
          console.log(result)
          commit('SET_ALL_URL', result.data.rows)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
