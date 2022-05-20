import { axios } from '@/utils/request'

const api = {
  requestLog: '/gateway/requestLog'
}

export function getRequestLogList (data, parameter) {
  return axios({
    url: api.requestLog + '/list',
    method: 'post',
    data: data,
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getRequestLogDetail (parameter) {
  return axios({
    url: api.requestLog + '/detail',
    method: 'post',
    params: parameter
  })
}

export function delRequestLog (parameter) {
  return axios({
    url: api.requestLog + '/delete',
    method: 'post',
    params: parameter
  })
}

export function cleanRequestLog (parameter) {
  return axios({
    url: api.requestLog + '/clean',
    method: 'post',
    params: parameter
  })
}
