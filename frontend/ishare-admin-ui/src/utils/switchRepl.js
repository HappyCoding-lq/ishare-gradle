export function switchRepl (repla) {
  switch (repla) {
    case '0':
      repla = '身份证'
      break
    case '1':
      repla = '外国公民护照'
      break
    case '2':
      repla = '军人证（军官证）'
      break
    case '3':
      repla = '驾照'
      break
    case '4':
      repla = '户口本'
      break
    case '5':
      repla = '学生证'
      break
    case '6':
      repla = '工作证'
      break
    case '7':
      repla = '出生证'
      break
    case '8':
      repla = '其它证件'
      break
    case '9':
      repla = '无证件'
      break
    case 'A':
      repla = '士兵证'
      break
    case 'B':
      repla = '港澳居民来往大陆通行证'
      break
    case 'C':
      repla = '临时身份证'
      break
    case 'D':
      repla = '警官证'
      break
    case 'E':
      repla = '台湾居民来往大陆通行证'
      break
    case 'F':
      repla = '中国居民来往港澳台通行证'
      break
    case 'G':
      repla = '港澳居民居住证'
      break
    case 'H':
      repla = '台湾居民居住证'
      break
    case 'I':
      repla = '外国人永久居留身份证'
  }
  return repla
}

export function switchBank (repla) {
  switch (repla) {
    case '0101':
      repla = '中国工商银行'
      break
    case '0102':
      repla = '中国农业银行'
      break
    case '0103':
      repla = '中国银行'
      break
    case '0104':
      repla = '中国建设银行'
      break
    case '0108':
      repla = '交通银行'
      break
    case '0109':
      repla = '中信银行'
      break
    case '0110':
      repla = '中国光大银行'
      break
    case '0111':
      repla = '华夏银行'
      break
    case '0112':
      repla = '中国民生银行'
      break
    case '0113':
      repla = '广东发展银行'
      break
    case '0198':
      repla = '平安银行'
      break
    case '0115':
      repla = '招商银行'
      break
    case '0116':
      repla = '兴业银行'
      break
    case '0117':
      repla = '上海浦东发展银行'
      break
    case '0128':
      repla = '中国邮政储蓄银行'
  }
  return repla
}

const certTypeEnums = {
  '0': '身份证',
  '1': '外国公民护照',
  '2': '军人证（军官证）',
  '3': '驾照',
  '4': '户口本',
  '5': '学生证',
  '6': '工作证',
  '7': '出生证',
  '8': '其它证件',
  '9': '无证件',
  'A': '士兵证',
  'B': '港澳居民来往大陆通行证',
  'C': '临时身份证',
  'D': '警官证',
  'E': '台湾居民来往大陆通行证',
  'F': '中国居民来往港澳台通行证',
  'G': '港澳居民居住证',
  'H': '台湾居民居住证',
  'I': '外国人永久居留身份证'
}
const bankCodeEnums = { '0101': '中国工商银行',
  '0102': '中国农业银行',
  '0103': '中国银行',
  '0104': '中国建设银行',
  '0108': '交通银行',
  '0109': '中信银行',
  '0110': '中国光大银行',
  '0111': '华夏银行',
  '0112': '中国民生银行',
  '0113': '广东发展银行',
  '0198': '平安银行',
  '0115': '招商银行',
  '0116': '兴业银行',
  '0117': '上海浦东发展银行',
  '0128': '中国邮政储蓄银行'
}
export { bankCodeEnums }
export default certTypeEnums
