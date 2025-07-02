import request from '@/utils/request'

// 获取美食列表（用户端）
export function getFoodList(params, config = {}) {
  return request.get('/api/food/list', params, {
    showDefaultMsg: false,
    ...config
  })
}

// 获取美食列表（管理员端）
export function getAllFoods(params, config = {}) {
  return request.get('/api/food/page', params, {
    showDefaultMsg: false,
    ...config
  })
}

// 获取美食详情
export function getFoodDetail(id, config = {}) {
  return request.get(`/api/food/detail/${id}`, null, {
    showDefaultMsg: false,
    ...config
  })
}

// 获取美食分类列表
export function getCategoryList(config = {}) {
  return request.get('/api/food/categories', null, {
    showDefaultMsg: false,
    ...config
  })
}

// 添加美食评论
export function addComment(data, config = {}) {
  return request.post('/api/food/comment', data, {
    successMsg: '评论成功',
    ...config
  })
}

// 获取美食评论列表
export function getCommentList(foodId, params, config = {}) {
  return request.get(`/api/food/comments/${foodId}`, params, {
    showDefaultMsg: false,
    ...config
  })
}

// 点赞评论
export function likeComment(commentId, userId, config = {}) {
  return request.post(`/api/food/comment/like/${commentId}`, null, {
    params: { userId },
    successMsg: '点赞成功',
    ...config
  })
}

// 获取推荐美食
export function getRecommendedFood(params, config = {}) {
  return request.get('/api/food/recommended', params, {
    showDefaultMsg: false,
    ...config
  })
}

// 根据分类获取美食列表
export function getFoodsByCategory(categoryId, config = {}) {
  return request.get(`/api/food/category/${categoryId}`, null, {
    showDefaultMsg: false,
    ...config
  })
}

export function getFoodById(id) {
  return request({
    url: `/api/food/${id}`,
    method: 'get'
  })
}

// 添加美食
export function createFood(data, config = {}) {
  return request.post('/api/food/add', data, {
    successMsg: '添加成功',
    ...config
  })
}

// 更新美食
export function updateFood(id, data, config = {}) {
  return request.put(`/api/food/${id}`, data, {
    successMsg: '更新成功',
    ...config
  })
}

// 删除美食
export function deleteFood(id, config = {}) {
  return request.delete(`/api/food/delete/${id}`, null, {
    successMsg: '删除成功',
    ...config
  })
}

// 获取热门美食
export function getHotFoods(limit = 4, config = {}) {
  return request.get('/api/food/hot', { limit }, {
    showDefaultMsg: false,
    ...config
  })
}

export function getFoodSuggestions(keyword, limit = 5) {
  return request({
    url: '/api/food/suggestions',
    method: 'get',
    params: { keyword, limit }
  })
} 