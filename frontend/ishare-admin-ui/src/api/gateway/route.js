import { axios } from '@/utils/request'

const api = {
  route: '/gateway/route'
}

export function getRouteList (data, parameter) {
  return axios({
    url: api.route + '/list',
    method: 'post',
    data: data,
    params: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function enableRoute (parameter) {
  return axios({
    url: api.route + '/enableAndReload',
    method: 'post',
    params: parameter
  })
}

export function saveRoute (parameter) {
  return axios({
    url: api.route + (parameter.newFlag ? '/addAndReload' : '/updateAndReload'),
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function delRoute (parameter) {
  return axios({
    url: api.route + '/deleteAndReload',
    method: 'post',
    params: parameter
  })
}

export function refreshRoute () {
  return axios({
    url: api.route + '/refresh',
    method: 'get'
  })
}

export function predicateValidCheck (parameter) {
  return axios({
    url: api.route + '/predicateValidCheck',
    method: 'post',
    params: parameter
  })
}

export function filterValidCheck (parameter) {
  return axios({
    url: api.route + '/filterValidCheck',
    method: 'post',
    params: parameter
  })
}

export function objectList (parameter) {
  return axios({
    url: api.route + '/objectList',
    method: 'post',
    params: parameter
  })
}

export function getEnumMap (parameter) {
  return axios({
    url: '/gateway/common/enum/list',
    method: 'get',
    params: parameter
  })
}

export function getServiceIdList () {
  return axios({
    url: api.route + '/serviceIdList',
    method: 'get'
  })
}

export function getApiPathList (parameter) {
  return axios({
    url: api.route + '/apiPathList',
    method: 'get',
    params: parameter
  })
}

export function getPathInfoList (parameter) {
  return axios({
    url: api.route + '/pathInfoList',
    method: 'get',
    params: parameter
  })
}

export function getClientList (parameter) {
  return axios({
    url: api.route + '/clientList',
    method: 'get',
    params: parameter
  })
}

export function generateApiInfo (data) {
  return axios({
    url: api.route + '/generateApiInfo',
    method: 'post',
    data: data
  })
}
