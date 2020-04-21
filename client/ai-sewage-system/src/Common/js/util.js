const prompt = require('@system.prompt')
const fetch = require('@system.fetch')
const router = require('@system.router') 
const request = require('@system.request')
const media = require('@system.media')

export default {
  /**
   * toast
   */
  toast:function (msg, time=1500){
    // const prompt = require('@system.prompt')
    prompt.showToast({
      message: msg,
      duration: time
    })
  },

  /**
   * alert
   */
  alert :function(msg, title='提示', callback){
    // const prompt = require('@system.prompt')
    if(typeof title === 'function'){
      callback = title
      title = '提示'
    }
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
        if(typeof callback==='function')
          callback(data)
      },
      cancel: function (data) {
        console.log('handling cancel')
      },
      fail: function (data, code) {
        this.hideLoading()
        console.log('fail')
      }.bind(this) 
    })
  },
  showContextMenu: function(list,callback) {
    prompt.showContextMenu({
      itemList: ['item0', 'item1', 'item2', 'item3', 'item4', 'item5'],
      itemColor: '#0faeff',
      success: function (ret) {
        callback(list[ret.index], ret.index)
      },
      cancel: function () {
        // todo
      }
    })
  },

  showLoading :function(msg='loading') {
    // const prompt = require('@system.prompt')
    prompt.showLoading({
      message: msg,
      loadingColor: '#29B6F6'
    })
  },

  hideLoading :function() {
    // const prompt = require('@system.prompt')
    prompt.hideLoading()
  },

  /**
   * 发送GET请求
   */
  get :function(url, header, callback){
    // const fetch = require('@system.fetch')
    this.showLoading('  loading..  ')
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
        this.alert('服务器维护中，请稍后再试！')
        console.log('errorcode:'+code)
      }.bind(this) ,
      complete: function(){this.hideLoading()}.bind(this) 
    })
  },

  /**
   * 发送POST请求
   */
  post:function(url, data, header, callback){
    // const fetch = require('@system.fetch')
    this.showLoading('  loading..  ')
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
        this.alert('服务器维护中，请稍后再试！')
        console.log('errorcode:'+code)
      }.bind(this) ,
      complete: function(){this.hideLoading()}.bind(this)
    })
  },
  upload: function(url,data,header,files,calllback){
    this.showLoading('  文件上传中..  ')
    request.upload({
      url: url,
      files: files,
      data: data,
      header: header,
      success: function(response_data) {
        calllback(JSON.parse(response_data.data))
      },
      fail: function(data, code) {
        console.log(`handling fail, code = ${code}`)
        this.alert('上传失败，请稍后再试！')
      }.bind(this) ,
      complete: function(){this.hideLoading()}.bind(this) 
    })
  },
  route:function(uriPath,paramJson=null){
    // const router = require('@system.router') 
    router.push({
      uri: uriPath,
      params: paramJson
    })
  },
  replace:function(uriPath,paramJson=null){
    // const router = require('@system.router') 
    router.replace({
      uri: uriPath,
      params: paramJson
    })
  },
  goback:function(){
    // const router = require('@system.router') 
    router.back()
  },
  /** 预览图片
   * curimg 当前打开的图片地址
   * imgList 所有图片地址（数组）
   */
  previewImgs: function(curimg, imgList){
    media.previewImage({
      current: curimg,
      uris: imgList,
      success: function() {
        console.log('preview success')
      },
      fail: function(data, code) {
        console.log('preview fail, code = ${code}')
      }
    })
  },


  /**
   * 时间格式化函数, 按照指定格式化字符串格式化传入时间
   * 
   * @param {Date} time 需要格式化的时间
   * @param {String} fmStr 定义时间的格式
   * 		yyyy: 代表四位数年份
   * 		  yy: 代表两位数年份 
   * 		  mm: 代表月份(小于10时补0)
   * 		  dd: 代表日期(小于10时补0)
   * 		  hh: 代表小时(小于10时补0)
   * 		  hh: 代表小时(小于10时补0)
   * 		  MM: 代表分钟(小于10时补0)
   * 		  ss: 代表秒数(小于10时补0)
   * 		 SSS: 代表毫秒数
   * 		   w: 代表周几(数字) 
   * 		   W: 代表周几(中文) 
   * 		  ww: 代表周几(英文) 
   * @returns {String} 返回格式化的时间
   */
  dateFormat: function(time, fmStr) {
    const weekCN = '一二三四五六日'
    const weekEN = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']

    let year = time.getFullYear()
    let month = time.getMonth() + 1
    let day = time.getDate()
    let hours = time.getHours()
    let minutes = time.getMinutes()
    let seconds = time.getSeconds()
    let milliSeconds = time.getMilliseconds()
    let week = time.getDay()

    month = month >= 10 ? month : ('0' + month)
    day = day >= 10 ? day : ('0' + day)
    hours = hours >= 10 ? hours : ('0' + hours)
    minutes = minutes >= 10 ? minutes : ('0' + minutes)
    seconds = seconds >= 10 ? seconds : ('0' + seconds)

    if (fmStr.indexOf('yyyy') !== -1) {
      fmStr = fmStr.replace('yyyy', year)
    } else {
      fmStr = fmStr.replace('yy', (year + '').slice(2))
    }
    fmStr = fmStr.replace('mm', month)
    fmStr = fmStr.replace('dd', day)
    fmStr = fmStr.replace('hh', hours)
    fmStr = fmStr.replace('MM', minutes)
    fmStr = fmStr.replace('ss', seconds)
    fmStr = fmStr.replace('SSS', milliSeconds)
    fmStr = fmStr.replace('W', weekCN[week - 1])
    fmStr = fmStr.replace('ww', weekEN[week - 1])
    fmStr = fmStr.replace('w', week)

    return fmStr
  }
}
