import request from '@/utils/request'

export function getSmartRecommendations(query) {
  console.log('Sending smart match request with query:', query)
  return request.get('/api/smart-match/recommendations', {
    query
  }, {
    showDefaultMsg: false, // 禁用默认的错误提示
    timeout: 30000 // 增加超时时间到30秒
  }).catch(error => {
    console.error('Smart match request failed:', error)
    throw error
  })
} 