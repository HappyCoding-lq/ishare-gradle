import { axios } from '@/utils/request'

const api = {
  link: '/tool/link'
}

export function getUrlHashList (parameter) {
  return axios({
    url: api.link + '/all',
    method: 'get',
    params: parameter
  })
}

export function seeLink (parameter) {
  return axios({
    url: api.link + '/seeLink',
    method: 'get',
    params: parameter
  })
}

export function deleteLink (parameter) {
  return axios({
    url: api.link + '/deleteLink',
    method: 'get',
    params: parameter
  })
}

export function addOrUpdate (parameter) {
  return axios({
    url: api.link + '/addOrUpdate',
    method: 'post',
    params: parameter,
    data: parameter
  })
}
