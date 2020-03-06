/**
 * @description 存储接口 供外部调用保存到本地
 */
import storage from '@system.storage'

function getStorage (key) {
  return new Promise((resolve, reject) => {
    // 获取storage
    storage.get({
      key,
      success(data) {
        if(data) {
          resolve(JSON.parse(data))
        }
      },
      fail(data, code) {
        console.log(`getStorage fail, code = ${code}`)
        reject({data, code})
      }
    })
  })
}

function setStorage (key, value) {
  // 设置storage
  storage.set({
    key,
    value: JSON.stringify(value),
    fail(data, code) {
      console.log(`setStorage fail, code = ${code}`)
    }
  })
}