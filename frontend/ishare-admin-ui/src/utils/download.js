import { pureAxios, axios } from '@/utils/request'

const downloadUrl = '/system/common/download'

export const mimeMap = {
  xlsx: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  zip: 'application/zip',
  pdf: 'application/pdf'
}

export function exportExcel (url, params) {
  axios({
    url: url,
    method: 'post',
    params: params
  }).then(res => {
    if (res.code === '200') {
      downloadXlsx(res.message)
    }
  })
}

/**
 * 下载.xlsx文件
 * @param {String} filename 文件名
 */
export function downloadXlsx (filename) {
  pureAxios({
    url: downloadUrl,
    method: 'get',
    params: { fileName: filename, delete: true },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.xlsx)
  })
}
/**
 * 代码生成并下载为zip
 * @param {String} url 链接
 * @param {String} tables 表名
 */
export function genCodeZip (url, tables) {
  pureAxios({
    url: url,
    method: 'get',
    params: { tables: tables },
    responseType: 'blob'
  }).then(res => {
    resolveBlob(res, mimeMap.zip)
  })
}

/**
 * 解析blob响应内容并下载
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 */
export function resolveBlob (res, mimeType) {
  const aLink = document.createElement('a')
  var blob = new Blob([res.data], { type: mimeType })
  // //从response的headers中获取filename, 后端response.setHeader("Content-disposition", "attachment; filename=xxxx.docx") 设置的文件名;
  var patt = new RegExp('filename=([^;]+\\.[^\\.;]+);*')
  var contentDisposition = decodeURI(res.headers['content-disposition'])
  var result = patt.exec(contentDisposition)
  var fileName = result[1]
  aLink.setAttribute('download', fileName)
  aLink.href = URL.createObjectURL(blob)
  document.body.appendChild(aLink)
  aLink.click()
  document.body.removeChild(aLink)
}

/**
 * 解析blob响应内容并下载excel
 * @param {*} res blob响应内容
 * @param {String} mimeType MIME类型
 * @param {String} fileName MIME类型
 */
export function downloadFilea (result, mimeType, fileName) {
  console.log(result)
  const blob = new Blob([result], { type: mimeType })
  const a = document.createElement('a')
  a.setAttribute('download', decodeURIComponent(fileName)) // 设置下载文件名称
  const url = window.URL.createObjectURL(blob)
  // 生成文件路径
  a.href = url
  a.click()
  window.URL.revokeObjectURL(url)
  /* document.body.appendChild(a)
  document.body.removeChild(a) */
  /*
   */
}
