import { axios } from '@/utils/request'

const api = {
  all: 'system/componets/all',
  selectByNameAndType: 'system/componets/url'
}
export default api

export function all () {
  return axios({
    url: api.all,
    method: 'get'
  })
}

export function selectByNameAndType (name, type) {
  return axios({
    url: api.selectByNameAndType + '/' + name + '/' + type,
    method: 'get'
  })
}
