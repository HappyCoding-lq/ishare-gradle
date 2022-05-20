import { axios } from '@/utils/request'

const api = {
  clientInfo: '/system/clientInfo'
}

export function getClientInfoList (parameter) {
  return axios({
    url: api.clientInfo + '/list',
    method: 'get',
    params: parameter
  })
}

export function saveClientInfo (parameter) {
  return axios({
    url: api.clientInfo + (parameter.id !== undefined ? '/update' : '/save'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delClientInfo (parameter) {
  return axios({
    url: api.clientInfo + '/remove',
    method: 'post',
    params: parameter
  })
}

export function getappSecret () {
  return axios({
    url: api.clientInfo + '/secret',
    method: 'get'
  })
}

export const clientInfoExport = api.clientInfo + '/export'
