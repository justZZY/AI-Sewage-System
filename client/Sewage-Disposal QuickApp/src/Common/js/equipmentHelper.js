function Equipment(uid,boxNo,alias,address,boxnet,connectionState, latitude, longitude){
  this.uid=uid
  this.boxNo=boxNo
  this.alias=alias
  this.address=address
  this.boxnet=boxnet
  this.connectionState=connectionState
  this.latitude = latitude
  this.longitude = longitude
}

function formatList(responseData){
  if(responseData===null || responseData===undefined) return null
  let areaJson = new Object()
  let temp = areaJson
  let reg = /.+?(省|市|自治区|自治州)/g
  for (let i = 0, len=responseData.length; i<len; i++) {
    let boxRegs = responseData[i]['boxRegs']
    for(let j=0; j<boxRegs.length; j++){
      let boxReg = boxRegs[j]
      let item = new Equipment(
        boxReg.box.uid,
        boxReg.box.boxNo,
        boxReg.alias,
        boxReg.box.address,
        boxReg.box.boxnet,
        boxReg.box.connectionState,
        boxReg.box.latitude,
        boxReg.box.longitude
      )
      let areas = item.address.substring(0,item.address.indexOf('市')+1).match(reg)
      if (areas===null) areas = ['其他']
      for(let k=0,kLen=areas.length; k<kLen; k++){
        let area = areas[k]
        if(areaJson[area]!=null){
          areaJson=areaJson[area]
          continue
        }
        if(k===kLen-1){
          areaJson[area] = new Array()
        }else{
          areaJson[area] = new Object()
        }
        areaJson = areaJson[area]
      }
      if(item.connectionState==1){
        areaJson.unshift(item)
      }else{
        areaJson.push(item)
      }
      areaJson = temp
    }
  }
  return toJsonArray(areaJson)
}

function toJsonArray(json){
  let array = new Array()
  for(let key in json){
    let jsonp = new Object()
    jsonp['area'] = key
    let arraychild = new Array()
    let val = json[key]
    for(let k in val){
      let jsonc = new Object()
      jsonc['area'] = k
      jsonc['child'] = val[k]
      arraychild.push(jsonc)
    }
    jsonp['child'] = arraychild
    array.push(jsonp)
  }
  return array

}

export default{
  formatList:formatList
}