<template><div>
<!--  <div style="height: 180px;width: 2000px;">-->
  <div style="height: 90px;width: 1000px;">
<!--    <canvas id="room1" style="height: 180px;width: 260px;"></canvas>-->
    <canvas id="room1" style="height: 90px;width: 130px;"></canvas>
    <canvas id="room2" style="height: 90px;width: 130px;"></canvas>
<!--    <canvas id="top6" style="height: 210px;width: 620px;"></canvas>-->
    <canvas id="top6" style="height: 105px;width: 323px;"></canvas>
<!--    <canvas id="top12" style="height: 200px;width: 20px;"></canvas>-->
    <canvas id="top12" style="height: 100px;width: 10px;"></canvas>
  </div>
<!--  <div style="height: 30px;width: 2000px;"-->
  <div style="height: 15px;width: 1000px;">
<!--    <canvas id="top5" style="height: 90px;width: 80px;"></canvas>-->
    <canvas id="top5" style="height: 45px;width: 40px;"></canvas>
<!--    <canvas id="top9" style="height: 100px;width: 180px; margin-left: 840px"></canvas>-->
    <canvas id="top9" style="height: 50px;width: 90px; margin-left: 420px"></canvas>
  </div>
<!--  <div style="margin-left: 100px">-->
  <div style="margin-left: 50px">
<!--    <canvas id="top4" style="height: 350px;width: 350px;"></canvas>-->
    <canvas id="top4" style="height: 175px;width: 175px;"></canvas>
<!--    <canvas id="top1" style="height: 300px;width: 60px;"></canvas>-->
    <canvas id="top1" style="height: 150px;width: 25px;"></canvas>
<!--    <canvas id="top2" style="height: 300px;width: 320px;"></canvas>-->
    <canvas id="top2" style="height: 150px;width: 160px;"></canvas>
<!--    <canvas id="top3" style="height: 300px;width: 50px;"></canvas>-->
    <canvas id="top3" style="height: 150px;width: 25px;"></canvas>
<!--    <canvas id="top8" style="height: 350px;width: 20px;"></canvas>-->
    <canvas id="top8" style="height: 175px;width: 10px;"></canvas>
<!--    <canvas id="top10" style="height: 300px;width: 180px;"></canvas>-->
    <canvas id="top10" style="height: 150px;width: 90px;"></canvas>
<!--    <canvas id="top11" style="height: 350px;width: 20px;"></canvas>-->
    <canvas id="top11" style="height: 175px;width: 10px;"></canvas>
<!--    <canvas id="top13" style="height: 350px;width: 60px;"></canvas>-->
    <canvas id="top13" style="height: 175px;width: 30px;"></canvas>
  </div>
<!--  <div class="box" style="height: 180px;width: 2000px;padding: 0px">-->
  <div class="box" style="height: 90px;width: 1000px;padding: 0px">
<!--    <canvas id="top25" width="100" height="180"></canvas>-->
    <canvas id="top25" width="50" height="90"></canvas>
    <!--调节池-->
<!--    <canvas id="canvas1" width="300" height="400"></canvas>-->
    <canvas id="canvas1"></canvas>
    <!--厌氧池-->
    <canvas id="canvas2" ></canvas>
    <!--缺氧池-->
    <canvas id="canvas3"></canvas>
    <!--氧化池-->
    <canvas id="canvas4"></canvas>
    <!--MBR膜池-->
    <canvas id="canvas5"></canvas>
    <!--消毒池-->
    <canvas id="canvas6"></canvas>

<!--    <canvas id="top26" width="100" height="180"></canvas>-->
    <canvas id="top26" width="50" height="90"></canvas>
  </div>
<!--  <div style="height: 180px;width: 2000px">-->
  <div style="height: 90px;width: 1000px">
<!--    <canvas id="top14" style="height: 200px;width: 20px;margin-left: 90px "></canvas>-->
    <canvas id="top14" style="height: 100px;width: 10px;margin-left: 90px "></canvas>

    <!--    缺氧池到镀膜池-->
<!--    <canvas id="top15" style="height: 200px;width: 20px; margin-left: 350px"></canvas>-->

    <canvas id="top15" style="height: 100px;width: 10px; margin-left: 175px"></canvas>
<!--    <canvas id="top16" style="height: 180px;width: 300px;"></canvas>-->

    <canvas id="top16" style="height: 90px;width: 150px;"></canvas>
<!--    <canvas id="top17" style="height: 200px;width: 20px;"></canvas>-->

    <canvas id="top17" style="height: 100px;width: 10px;"></canvas>
<!--    <canvas id="top21" style="height: 200px;width: 20px;margin-left: 25px"></canvas>-->
    <canvas id="top21" style="height: 100px;width: 10px;margin-left: 25px"></canvas>
  </div>
  <div>
<!--    <canvas id="top22" style="width: 75px;height: 80px;margin-left: 100px"></canvas>-->
    <canvas id="top22" style="width: 35px;height: 40px;margin-left: 50px"></canvas>
<!--    <canvas id="top18" style="height: 115px;width: 20px;"></canvas>-->

    <canvas id="top18" style="height: 50px;width: 10px;"></canvas>
<!--    <canvas id="top19" style="height: 70px;width: 750px"></canvas>-->
    <canvas id="top19" style="height: 35px;width: 380px"></canvas>
<!--    <canvas id="top20" style="height: 115px;width: 20px;"></canvas>-->
    <canvas id="top20" style="height: 50px;width: 10px;"></canvas>
  </div>
<!--  <div style="height: 105px">-->
  <div style="height: 53px">
<!--    <canvas id="top23" style="height: 100px;width: 20px;margin-bottom: 100px;margin-left: 80px"></canvas>-->

    <canvas id="top23" style="height: 50px;width: 10px;margin-bottom:50px;margin-left: 40px"></canvas>
  </div>
<!--  <div style="height: 100px">-->

  <div style="height: 50px">
<!--    <canvas id="room3" style="width: 200px;height: 100px"></canvas>-->
    <canvas id="room3" style="width: 100px;height: 50px"></canvas>
<!--    <canvas id="top24" style="width: 180px;height: 100px"></canvas>-->
    <canvas id="top24" style="width: 90px;height: 50px"></canvas>
  </div>
</div>
</template>
<script>
  let wave = {
    speed: 50.1, // 波浪横向流动速度
    offsetX: 0, // 波浪横向偏移量
    isDrawContainer: false, // 判断是否画了容器
    offsetYRange: 1.1, // 波浪垂直方向最大范围
    offsetY: 0, // 波浪垂直方向位移
    offsetYSpeed: 0.003, // 波浪溢满
    progressNum: 0, // 进度
    text: '调节池', // 池子文字
    init: function (id) {
      let canvas = document.getElementById(id)
      let winW = 150

      this.ctx = canvas.getContext('2d')
      canvas.width = winW * 0.6
      canvas.height = winW * 0.6

      this.canvasW = canvas.width

      this.draw()
    },
    // 所有绘制
    draw: function () {
      let ctx = this.ctx

      ctx.clearRect(0, 0, this.canvasW, this.canvasW)
      this.offsetX += this.speed

      // if (!this.isDrawContainer) {
      this.drawContainer(ctx)
      // }

      //  this.drawWave(ctx, this.offsetX, this.offsetY, 0.04, 6, '#a4def6');
      this.drawWave(ctx, this.offsetX + 2, this.offsetY - 0.02, 0.04, 8, '#79d4f9')
      this.drawFont(ctx, this.text)
      if (this.offsetY < this.offsetYRange) {
        this.offsetY += this.offsetYSpeed

        this.progressNum += 100 / (this.offsetYRange / this.offsetYSpeed)

      // document.querySelector('.proNum').innerHTML = parseInt(this.progressNum) + '%';
      }

      requestAnimationFrame(this.draw.bind(this))
    },

    drawContainer: function (ctx) {
      let lineWidth = 5

      ctx.lineWidth = lineWidth
      ctx.beginPath()
      // ctx.arc(pointR, pointR, circleR, 0, 2 * Math.PI);
      ctx.rect(5, 5, 80, 80)
      ctx.strokeStyle = 'rgba(139,69,19)'
      ctx.stroke()
      ctx.clip()
      this.isDrawContainer = true
    },

    // 画波浪线
    drawWave: function (ctx, offsetX, offsetY, waveW, waveH, color) {
      let canvasW = this.canvasW
      let startX = 0 // 波浪线初始x轴坐标

      ctx.beginPath()

      let startPos = [startX]

      for (let x = startX; x < canvasW; x += 20 / canvasW) {
        // 正弦曲线公式：y=Asin(ωx+φ)+k
        // let y = (1 - offsetY) * canvasW + waveH * Math.sin((startX + x) * waveW + offsetX);
        let y = waveH * Math.sin((startX + x) * waveW + offsetX) + 50

        // eslint-disable-next-line eqeqeq
        if (startPos.length == 1) {
          startPos.push(y)
        }

        ctx.lineTo(x, y)
      // console.log(x,y);
      }
      // 画出完整的波浪形状
      ctx.lineTo(canvasW, canvasW)
      ctx.lineTo(startX, canvasW)
      ctx.lineTo(startPos[0], startPos[1])
      ctx.fillStyle = color
      ctx.fill()
    },
    // 添加文字
    drawFont: function (ctx, text) {
      ctx.fillStyle = 'red'
      ctx.font = "15px '微软雅黑'"
      ctx.textAlign = 'left'

      // ctx.shadowBlur = 10;
      // ctx.shadowOffsetX = 5;
      // ctx.shadowOffsetY = 5;
      // ctx.shadowColor = "black";

      // ctx.strokeText(text, 50, 100);
      ctx.fillText(text, 15, 30)
    }
  }

  function draw () {
    let data1 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 10,
      end_x: 10,
      end_y: 10,
      canvas_w: 50,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 20, // 线长
      interval: 8, // 间隔
      flow_dir: 'down'// 水流方向
    }
    let data2 = {
      time: 200, // 刷新速度
      start_x: 0, // 起点位置
      start_y: 10,
      end_x: 0,
      end_y: 10,
      canvas_w: 320,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 20, // 线长
      interval: 8, // 间隔
      flow_dir: 'right'// 水流方向
    }
    let data3 = {
      time: 200, // 刷新速度
      start_x: 30, // 起点位置
      start_y: 10,
      end_x: 30,
      end_y: 10,
      canvas_w: 50,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 20, // 线长
      interval: 8, // 间隔
      flow_dir: 'down'// 水流方向
    }
    let data4 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 50,
      end_x: 10,
      end_y: 50,
      canvas_w: 320,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 20, // 线长
      interval: 8, // 间隔
      flow_dir: 'right'// 水流方向
    }
    let data5 = {
      time: 200, // 刷新速度
      start_x: 60, // 起点位置
      start_y: 10,
      end_x: 60,
      end_y: 10,
      canvas_w: 80,
      canvas_h: 100,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 20, // 线长
      interval: 8, // 间隔
      flow_dir: 'down'// 水流方向
    }

    let data6 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 320,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 8, // 间隔
      flow_dir: 'right'// 水流方向
    }

    let data9 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 320,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 8, // 坐标变化速度参数
      vy: 6,
      line_w: 10,
      direction: 'h',
      width: 10, // 线长
      interval: 8, // 间隔
      flow_dir: 'right'// 水流方向
    }

    let data10 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 180,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'left'// 水流方向
    }

    let data14 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 200,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'up'// 水流方向
    }

    let data15 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 180,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'up'// 水流方向
    }

    let data16 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 290,
      end_x: 10,
      end_y: 290,
      canvas_w: 180,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'left'// 水流方向
    }

    let data17 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 180,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'down'// 水流方向
    }

    let data18 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 115,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'up'// 水流方向
    }

    let data19 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 250,
      end_x: 50,
      end_y: 250,
      canvas_w: 180,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 3, // 坐标变化速度参数
      vy: 6,
      line_w: 15,
      direction: 'h',
      width: 10,
      interval: 4, // 间隔
      flow_dir: 'left'// 水流方向
    }

    let data20 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 100,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'down'// 水流方向
    }

    let data21 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 80,
      end_x: 10,
      end_y: 80,
      canvas_w: 20,
      canvas_h: 200,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'down'// 水流方向
    }

    let data22 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 250,
      end_x: 10,
      end_y: 250,
      canvas_w: 180,
      canvas_h: 300,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 15,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'left'// 水流方向
    }

    let data23 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 40,
      end_x: 10,
      end_y: 40,
      canvas_w: 20,
      canvas_h: 100,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'v',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'down'// 水流方向
    }

    let data24 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 50,
      end_x: 10,
      end_y: 50,
      canvas_w: 180,
      canvas_h: 100,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'right'// 水流方向
    }

    let data25 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 50,
      end_x: 10,
      end_y: 50,
      canvas_w: 50,
      canvas_h: 90,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'right'// 水流方向
    }

    let data26 = {
      time: 200, // 刷新速度
      start_x: 10, // 起点位置
      start_y: 50,
      end_x: 10,
      end_y: 50,
      canvas_w: 90,
      canvas_h: 90,
      color: '#00BFFF', // 颜色
      vx: 6, // 坐标变化速度参数
      vy: 6,
      line_w: 4,
      direction: 'h',
      width: 10, // 线长
      interval: 4, // 间隔
      flow_dir: 'right'// 水流方向
    }

    // 绘制控制间 消毒间
    drawRect('room1', 20, 10, 250, 120, '控制间')
    drawRect('room2', 20, 10, 275, 120, '消毒间')
    drawRect('room3', 30, 10, 260, 140, '污泥浓缩池')

    // 绘制流线
    // eslint-disable-next-line new-cap
    let res1 = new createLine('top1', data1)
    res1.begin('top1', data1)

    // eslint-disable-next-line new-cap
    let res2 = new createLine('top2', data2)
    res2.begin('top2', data2)

    // eslint-disable-next-line new-cap
    let res3 = new createLine('top3', data3)
    res3.begin('top3', data3)

    // eslint-disable-next-line new-cap
    let res4 = new createLine('top4', data4)
    res4.begin('top4', data4)

    // eslint-disable-next-line new-cap
    let res5 = new createLine('top5', data5)
    res5.begin('top5', data5)

    // var res6 = new createLine("top6", data6);
    // res6.begin("top6", data6);

    // eslint-disable-next-line new-cap
    let res9 = new createLine('top9', data9)
    res9.begin('top9', data9)

    // eslint-disable-next-line new-cap
    let res10 = new createLine('top10', data10)
    res10.begin('top10', data10)

    // var res7 = new createLine("top7", data7);
    // res7.begin("top7", data7);

    // eslint-disable-next-line new-cap
    let res14 = new createLine('top14', data14)
    res14.begin('top14', data14)

    // eslint-disable-next-line new-cap
    let res15 = new createLine('top15', data15)
    res15.begin('top15', data15)

    // eslint-disable-next-line new-cap
    let res16 = new createLine('top16', data16)
    res16.begin('top16', data16)

    // eslint-disable-next-line new-cap
    let res17 = new createLine('top17', data17)
    res17.begin('top17', data17)

    // eslint-disable-next-line new-cap
    let res18 = new createLine('top18', data18)
    res18.begin('top18', data18)

    // eslint-disable-next-line new-cap
    let res19 = new createLine('top19', data19)
    res19.begin('top19', data19)

    // eslint-disable-next-line new-cap
    let res20 = new createLine('top20', data20)
    res20.begin('top20', data20)

    // eslint-disable-next-line new-cap
    let res21 = new createLine('top21', data21)
    res21.begin('top21', data21)

    // eslint-disable-next-line new-cap
    let res22 = new createLine('top22', data22)
    res22.begin('top22', data22)

    // eslint-disable-next-line new-cap
    let res23 = new createLine('top23', data23)
    res23.begin('top23', data23)

    // eslint-disable-next-line new-cap
    let res24 = new createLine('top24', data24)
    res24.begin('top24', data24)

    // eslint-disable-next-line new-cap
    let res25 = new createLine('top25', data25)
    res25.begin('top25', data25)

    // eslint-disable-next-line new-cap
    let res26 = new createLine('top26', data26)
    res26.begin('top26', data26)

    // 绘制直线
    drawline('top8', 200, 0, 200, 300, 60)
    drawline('top11', 200, 0, 200, 300, 60)
    drawline('top6', 0, 50, 300, 50, 4)
    drawline('top6', 300, 50, 300, 160, 4)
    drawline('top13', 110, 0, 110, 300, 24)

    // 填充字体
    drawText('top6', 80, 30, '紫外线消毒')
    drawText('top4', 100, 100, '曝气')
    drawText('top25', 10, 40, '进水管')
    drawText('top26', 20, 40, '外排')
    drawText('top24', 20, 40, '定期抽吸')

    // 调节池
    wave.init('canvas1')
    // 厌氧池
    let wave2 = Object.create(wave)
    wave2.text = '厌氧池'
    wave2.init('canvas2')
    // 缺氧池
    let wave3 = Object.create(wave)
    wave3.text = '缺氧池'
    wave3.init('canvas3')
    // 氧化池
    let wave4 = Object.create(wave)
    wave4.text = '氧化池'
    wave4.init('canvas4')
    // MBR膜池
    let wave5 = Object.create(wave)
    wave5.text = 'MBR膜池'
    wave5.init('canvas5')
    // 消毒池
    let wave6 = Object.create(wave)
    wave6.text = '消毒池'
    wave6.init('canvas6')
  }

  // draw()

function drawRect (element, x, y, width, height, text) {
    let canvas = document.getElementById(element)
    console.log(canvas)
    if (canvas.getContext('2d')) {
      let ctx = canvas.getContext('2d')
      ctx.strokeStyle = 'rgba(139,69,19)'
      ctx.strokeRect(x, y, width, height)
      ctx.fillStyle = 'red'
      ctx.font = "30px '微软雅黑'"
      ctx.textAlign = 'left'
      ctx.fillText(text, 30, 100)
    }
  }

  function createLine (element, config) {
    // eslint-disable-next-line no-new-object
    let flow = new Object()
    let lines = []
    // 方向为水平
    if (config.direction === 'h') {
      let number = config.canvas_w / (config.width + config.width / 2 + config.interval * 2)
      number = Math.ceil(number) + 1
      for (let n = 0; n < number; n++) {
        let start_x = config.canvas_w - n * (config.width + config.width / 2 + config.interval * 2)

        let lines_data = {
          start_x: start_x,
          start_y: config.start_y,
          end_x: start_x - config.width,
          end_y: config.end_y,
          vx: config.vx,
          vy: config.vy
        }
        lines.push(lines_data)
      }
      // eslint-disable-next-line eqeqeq
      if (config.flow_dir == 'left') {
        // 水平逆向翻转数组
        lines.reverse()
      }
    }
    // eslint-disable-next-line eqeqeq
    if (config.direction == 'v') {
      var number = config.canvas_w / (config.width + config.width / 2 + config.interval * 2)
      number = Math.ceil(number) + 1
      for (var n = 0; n < number; n++) {
        var start_y = config.canvas_h - n * (config.width + config.width / 2 + config.interval * 2)
        var lines_data2 = {
          start_x: config.start_x,
          start_y: start_y,
          end_x: config.end_x,
          end_y: start_y - config.width,
          vx: config.vx,
          vy: config.vy
        }
        lines.push(lines_data2)
      }
    }

    flow.begin = function () {
      var canvasObj = document.getElementById(element)
      var canvas = {
        line_w: config.line_w || 3,
        vx: config.vx || 0,
        vy: config.vy || 0,
        color: config.color || 'blue',
        canvas_w: config.canvas_w || 0,
        canvas_h: config.canvas_h || 0,
        start_x: config.start_x || 0,
        start_y: config.start_y || 0,
        end_x: config.end_x || 0,
        end_y: config.end_y || 0,
        direction: config.direction || 'h',
        width: config.width || 20,
        interval: config.interval || 10,
        flow_dir: config.flow_dir || 'left'
      }
      if (canvasObj.getContext('2d')) {
        canvas.ctx = canvasObj.getContext('2d')
        canvasObj.width = canvas.canvas_w
        canvasObj.height = canvas.canvas_h
      } else {
        console.log('当前环境不支持canvas')
        return null
      }
      setInterval(function () {
        drawline(canvas)
        // 水平向右
        // eslint-disable-next-line eqeqeq
        if (canvas.direction == 'h' && canvas.flow_dir == 'right') {
          updateLine(canvas)
        }
        // 水平向左
        // eslint-disable-next-line eqeqeq
        if (canvas.direction == 'h' && canvas.flow_dir == 'left') {
          updateLine2(canvas)
        }
        // 竖直向下
        // eslint-disable-next-line eqeqeq
        if (canvas.direction == 'v' && canvas.flow_dir == 'down') {
          updateline_v(canvas)
        }
        // 竖直向上
        // eslint-disable-next-line eqeqeq
        if (canvas.direction == 'v' && canvas.flow_dir == 'up') {
          updateline_v2(canvas)
        }
        addline(canvas)
      }, config.time)
    }

    function drawline (canvas) {
      let content = canvas.ctx
      content.clearRect(0, 0, canvas.canvas_w, canvas.canvas_h)

      for (let i = 0; i < lines.length; i++) {
        content.beginPath()
        content.moveTo(lines[i].start_x, lines[i].start_y)
        content.lineTo(lines[i].end_x, lines[i].end_y)
        content.lineWidth = canvas.line_w
        content.strokeStyle = canvas.color
        content.stroke()
        content.closePath()
      }
    }

    // 水平流向为右
    function updateLine (canvas) {
      for (let i = 0; i < lines.length; i++) {
        lines[i].start_x = lines[i].start_x + lines[i].vx
        lines[i].end_x = lines[i].end_x + lines[i].vx
      }
      let cnt = []
      for (let j = 0; j < lines.length; j++) {
        if (lines[j].end_x > canvas.canvas_w) {
          cnt.push(j)
        }
      }
      for (let k = 0; k < cnt.length; k++) {
        lines.splice(k, 1)
      }
    }

    // 水平流向为左
    function updateLine2 (canvas) {
      for (let i = 0; i < lines.length; i++) {
        lines[i].start_x = lines[i].start_x - lines[i].vx
        lines[i].end_x = lines[i].end_x - lines[i].vx
      }
      let cnt = []
      for (let j = 0; j < lines.length; j++) {
        if (lines[j].end_x < 0) {
          cnt.push(j)
        }
      }
      for (let k = 0; k < cnt.length; k++) {
        lines.splice(k, 1)
      }
    }

    // 竖直流向
    function updateline_v (canvas) {
      for (let i = 0; i < lines.length; i++) {
        lines[i].start_y = lines[i].start_y + lines[i].vy
        lines[i].end_y = lines[i].end_y + lines[i].vy
      }
      var cnt = []
      for (let j = 0; j < lines.length; j++) {
        if (lines[j].end_y > canvas.canvas_h) {
          cnt.push(j)
        }
      }
      for (let k = 0; k < cnt.length; k++) {
        lines.splice(k, 1)
      }
    }

    // 竖直流向向上
    function updateline_v2 (canvas) {
      for (let i = 0; i < lines.length; i++) {
        lines[i].start_y = lines[i].start_y - lines[i].vy
        lines[i].end_y = lines[i].end_y - lines[i].vy
      }
      let cnt = []
      for (let j = 0; j < lines.length; j++) {
        if (lines[j].end_y < 0) {
          cnt.push(j)
        }
      }
      for (let k = 0; k < cnt.length; k++) {
        lines.splice(k, 1)
      }
    }

    function addline (canvas) {
      let length = lines.length
      // eslint-disable-next-line eqeqeq
      if (canvas.direction == 'h') {
        if (lines[length - 1].start_x < canvas.canvas_w) {
          // 水平流向向右
          // eslint-disable-next-line eqeqeq
          if (canvas.flow_dir == 'right') {
            var start_x2 = lines[length - 1].end_x - canvas.interval * 2.8
            // eslint-disable-next-line brace-style
          }
          // 水平流向向左
          else {
            // eslint-disable-next-line no-redeclare
            var start_x2 = lines[length - 1].end_x + canvas.interval * 8.25
          }
          let end_x2 = start_x2 - canvas.width
          let data = {
            start_x: start_x2,
            start_y: canvas.start_y,
            end_x: end_x2,
            end_y: canvas.end_y,
            vx: canvas.vx,
            vy: canvas.vy
          }
          lines.push(data)
        }
        // eslint-disable-next-line eqeqeq
      } else if (canvas.direction == 'v') {
        // eslint-disable-next-line eqeqeq
        if (canvas.flow_dir == 'up') {
          if (lines[length - 1].end_y < canvas.canvas_h) {
            // if (lines[length - 1].end_y > 0) {
            // let start_y = lines[length - 1].end_y - canvas.interval * 2.8;

            // 竖直流向方向向上
            let start_y = lines[length - 1].end_y + canvas.interval * 8.25
            let end_y = start_y - canvas.width
            let data = {
              start_x: canvas.start_x,
              start_y: start_y,
              end_x: canvas.end_x,
              end_y: end_y,
              vx: canvas.vx,
              vy: canvas.vy
            }
            lines.push(data)
          }
        }
        // eslint-disable-next-line eqeqeq
        if (canvas.flow_dir == 'down') {
          if (lines[length - 1].end_y > 0) {
            let start_y = lines[length - 1].end_y - canvas.interval * 2.8

            // 竖直流向方向向上
            // let start_y = lines[length - 1].end_y + canvas.interval * 8.25;
            let end_y = start_y - canvas.width
            let data = {
              start_x: canvas.start_x,
              start_y: start_y,
              end_x: canvas.end_x,
              end_y: end_y,
              vx: canvas.vx,
              vy: canvas.vy
            }
            lines.push(data)
          }
          // } else {
          //     let data = {
          //         start_x: canvas.start_x,
          //         start_y: canvas.start_y,
          //         end_x: canvas.end_x,
          //         end_y: canvas.end_y,
          //         vx: canvas.vx,
          //         vy: canvas.vy,
          //     }
          //     lines.push(data);
          // }
        }
      }
    }

    return flow
}

  function drawline (element, start_x, start_y, end_x, end_y, lineWidth) {
    let canvas = document.getElementById(element)
    if (canvas.getContext) {
      let ctx = canvas.getContext('2d')
      ctx.strokeStyle = '#79d4f9'
      ctx.lineWidth = lineWidth
      // 开始一个新的绘制路径
      ctx.beginPath()
      // 定义直线的起点坐标
      ctx.moveTo(start_x, start_y)
      // 定义直线的终点坐标
      ctx.lineTo(end_x, end_y)
      // 沿着坐标点顺序的路径绘制直线
      ctx.stroke()
      // 关闭当前的绘制路径
      ctx.closePath()
    }
  }

  function drawText (element, x, y, text) {
    var canvas = document.getElementById(element)
    if (canvas.getContext) {
      var ctx = canvas.getContext('2d')
      ctx.fillStyle = 'red'
      ctx.font = "15px '微软雅黑'"
      ctx.textAlign = 'left'

      // ctx.fillText(text, x, y);
      setInterval(function () {
        ctx.fillText(text, x, y)
      }, 1)
    }
  }
  export default {
    name: 'dynamic',
    mounted () {
      draw()
    }
  }
</script>
