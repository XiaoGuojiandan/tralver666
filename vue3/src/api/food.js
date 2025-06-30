import request from '@/utils/request'

// 获取美食列表
export function getFoodList(params, config = {}) {
  return request.get('/api/food/list', params, {
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
export function getFoodByCategory(categoryId, limit = 6, config = {}) {
  return request.get(`/api/food/category/${categoryId}`, { limit }, {
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

export function getFoodsByCategory(categoryId) {
  return request({
    url: `/api/food/category/${categoryId}`,
    method: 'get'
  })
}

export function createFood(data) {
  return request({
    url: '/api/food/add',
    method: 'post',
    data
  })
}

export function updateFood(id, data) {
  return request({
    url: `/api/food/${id}`,
    method: 'put',
    data
  })
}

export function deleteFood(id) {
  return request({
    url: `/api/food/delete/${id}`,
    method: 'delete'
  })
}

export function getAllFoods() {
  return request({
    url: '/api/food/all',
    method: 'get'
  })
}

export function getHotFoods(limit = 4) {
  return request({
    url: '/api/food/hot',
    method: 'get',
    params: { limit }
  })
}

export function getFoodSuggestions(keyword, limit = 5) {
  return request({
    url: '/api/food/suggestions',
    method: 'get',
    params: { keyword, limit }
  })
} 