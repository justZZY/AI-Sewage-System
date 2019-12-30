
/**
 * toast
 */
function toast (msg, time=1500){
  const prompt = require('@system.prompt')
  prompt.showToast({
    message: msg,
    duration: time
  })
}

/**
 * alert
 */
function alert (msg, title='提示', callback){
  const prompt = require('@system.prompt')
  prompt.showDialog({
    title: title,
    message: msg,
    buttons: [
      {
        text: '确定',
        color: '#33dd44'
      }
    ],
    success: function (data) {
      if(callback==='function')
        callback(data)
    },
    cancel: function (data) {
      console.log('handling cancel')
    },
    fail: function (data, code) {
      console.log('fail')
    }
  })
}

/**
 * 发送GET请求
 */
function get(url, header, callback){
  const fetch = require('@system.fetch')
  if (typeof header === 'function'){
    callback = header
    header = null
  }
  fetch.fetch({
    url: url,
    header: header,
    method: 'GET',
    success: function (response_data) {
      callback(JSON.parse(response_data.data))
    },
    fail: function (response_data, code) {
      alert('服务器维护中，请稍后再试！')
      console.log('errorcode:'+code)
    }
  })
}

/**
 * 发送POST请求
 */
function post(url, data, header, callback){
  const fetch = require('@system.fetch')
  if (typeof data === 'function'){
    callback = data
    data = null
    header = null
  }
  if (typeof header === 'function'){
    callback = header
    header = null
  }
  fetch.fetch({
    url: url,
    data: data,
    header: header,
    method: 'POST',
    success: function (response_data) {
      callback(JSON.parse(response_data.data))
    },
    fail: function (dresponse_dataata, code) {
      alert('服务器维护中，请稍后再试！')
      console.log('errorcode:'+code)
    }
  })
}

function route(uriPath,paramJson=null){
  const router = require('@system.router') 
  router.push({
    uri: uriPath,
    params: paramJson
  })
}

function goback(){
  const router = require('@system.router') 
  router.back()
} 

export default {
  toast,
  alert,
  get,
  post,
  route,
  goback
}
