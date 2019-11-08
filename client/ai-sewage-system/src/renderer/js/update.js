import { autoUpdater } from 'electron-updater'
import { ipcMain } from 'electron'
/**
 * -1 检查更新失败 0 正在检查更新 1 检测到新版本，准备下载 2 未检测到新版本 3 下载中 4 下载暂停 5 下载暂停恢复 6 下载完成 7 下载失败 8 取消下载
 * */
class Update {
  mainWindow
  constructor (mainWindow) {
    this.mainWindow = mainWindow
    autoUpdater.setFeedURL('http://114.55.146.36:8083/update/') // 更新地址与package.json中的build.publish.url相对应
  }
  runUpdate () {
    /**
     * 根据自身需求选择下方方法
     */
    this.error()
    this.start()
    this.allow()
    this.unallowed()
    this.listen()
    this.download()
  }
  Message (type, data) {
    this.mainWindow.webContents.send('message', type, data)
  }
  error () { // 当更新发生错误的时候触发。
    autoUpdater.on('error', (err) => {
      this.Message(-1, err)
      console.log(err)
    })
  }
  start () { // 当开始检查更新的时候触发
    autoUpdater.on('checking-for-update', (event, arg) => {
      this.Message(0)
    })
  }
  allow () { // 发现可更新数据时
    autoUpdater.on('update-available', (event, arg) => {
      this.Message(1)
    })
  }
  unallowed () { // 没有可更新数据时
    autoUpdater.on('update-not-available', (event, arg) => {
      this.Message(2)
    })
  }
  listen () { // 下载监听
    autoUpdater.on('download-progress', () => {
      this.Message('下载进行中')
    })
  }
  download () { // 下载完成
    autoUpdater.on('update-downloaded', () => {
      this.Message(6)
      setTimeout(m => {
        autoUpdater.quitAndInstall()
      }, 1000)
    })
  }
  load () { // 触发器
    autoUpdater.checkForUpdates()
  }
}
export default Update
