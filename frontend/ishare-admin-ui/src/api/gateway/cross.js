import { axios } from '@/utils/request'

const api = {
  Cross: '/gateway/cross'
}

export function getCrossList (data, parameter) {
  return axios({
    url: api.Cross + '/list',
    method: 'post',
    data: data,
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getPathList (parameter) {
  return axios({
    url: api.Cross + '/pathList',
    method: 'post',
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getPathCrossList (parameter) {
  return axios({
    url: api.Cross + '/pathCrossList',
    method: 'post',
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function deletePathCross (parameter) {
  return axios({
    url: api.Cross + '/deletePathCross',
    method: 'post',
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function saveCross (parameter) {
  return axios({
    url: api.Cross + (parameter.newFlag ? '/add' : '/update'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delCross (parameter) {
  return axios({
    url: api.Cross + '/delete',
    method: 'post',
    params: parameter
  })
}

export function refreshCross () {
  return axios({
    url: api.Cross + '/refresh',
    method: 'post'
  })
}
