import request from '@/utils/request'

// 获取门票列表
export function getTicketList(params, config = {}) {
  return request.get('/api/ticket/page', params, {
    showDefaultMsg: false,
    ...config
  })
}

// 获取门票详情
export function getTicketDetail(id, config = {}) {
  return request.get(`/api/ticket/${id}`, null, {
    showDefaultMsg: false,
    ...config
  })
}

// 添加门票
export function createTicket(data, config = {}) {
  return request.post('/api/ticket', data, {
    successMsg: '添加成功',
    ...config
  })
}

// 更新门票
export function updateTicket(id, data, config = {}) {
  return request.put(`/api/ticket/${id}`, data, {
    successMsg: '更新成功',
    ...config
  })
}

// 删除门票
export function deleteTicket(id, config = {}) {
  return request.delete(`/api/ticket/${id}`, null, {
    successMsg: '删除成功',
    ...config
  })
}

// 获取景点的门票列表
export function getTicketsByScenicId(scenicId, config = {}) {
  return request.get(`/api/ticket/scenic/${scenicId}`, null, {
    showDefaultMsg: false,
    ...config
  })
} 