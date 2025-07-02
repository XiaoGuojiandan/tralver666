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

export function getAccommodationList(params) {
  return request({
    url: '/accommodation/page',
    method: 'get',
    params: {
      currentPage: params.currentPage,
      size: params.size,
      name: params.name,
      type: params.type,
      priceRange: params.priceRange,
      minStarLevel: params.minRating,
      scenicId: params.scenicId,
      city: params.city,
      province: params.province
    }
  })
} 