import request from '@/utils/request'

export function getScenicList(params) {
  return request({
    url: '/scenic/page',
    method: 'get',
    params
  })
}

export function getScenicDetail(id) {
  return request({
    url: `/scenic/${id}`,
    method: 'get'
  })
}

export function getCategories() {
  return request({
    url: '/scenic-category/tree',
    method: 'get'
  })
}

export function getRegions() {
  return request({
    url: '/scenic-category/regions',
    method: 'get'
  })
} 