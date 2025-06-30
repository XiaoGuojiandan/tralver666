import request from '@/utils/request'

export function getSmartRecommendations(query) {
  console.log('Sending smart match request with query:', query)
  const params = new URLSearchParams()
  params.append('query', query)
  
  return request.get('/api/smart-match/recommendations?' + params.toString(), {}, {
    showDefaultMsg: false, // 禁用默认的错误提示
    timeout: 30000 // 增加超时时间到30秒
  }).catch(error => {
    console.error('Smart match request failed:', error)
    throw error
  })
} 