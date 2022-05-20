import { axios } from '@/utils/request'

const api = {
  user: '/user',
  /* role: '/mock/role', */
  /*  service: '/services', */
  permission: '/permission',
  permissionNoPager: '/permission/no-pager'
  // orgTree: '/org/tree'
}

export default api

export function getUserList (parameter) {
  return axios({
    url: api.user,
    method: 'get',
    params: parameter
  })
}

export function getPermissions (parameter) {
  return axios({
    url: api.permissionNoPager,
    method: 'get',
    params: parameter
  })
}

// id == 0 add     post
// id != 0 update  put
export function saveService (parameter) {
  return axios({
    url: api.service,
    method: parameter.id === 0 ? 'post' : 'put',
    data: parameter
  })
}
