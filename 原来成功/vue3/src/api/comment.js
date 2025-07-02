import request from '@/utils/request'

// 获取评论列表
export function getComments({ targetId, targetType, currentPage = 1, size = 10 }) {
  return request({
    url: '/api/comments',
    method: 'get',
    params: {
      targetId,
      targetType,
      pageNum: currentPage,
      pageSize: size
    }
  })
}

// 添加评论
export function addComment(data) {
  return request({
    url: '/api/comments',
    method: 'post',
    data
  })
}

// 点赞评论
export function likeComment(commentId) {
  return request({
    url: `/api/comments/${commentId}/like`,
    method: 'post'
  })
}

// 回复评论
export function replyComment(commentId, data) {
  return request({
    url: `/api/comments/${commentId}/reply`,
    method: 'post',
    data
  })
} 