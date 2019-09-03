import api from '../utils/api'

export function getInfo (token) {
  return api({
    url: 'http://localhost:8081/login/getInfo',
    method: 'get',
    params: {token}
  })
}
