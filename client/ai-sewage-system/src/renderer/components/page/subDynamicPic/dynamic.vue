no<template>
  <div>
    <div style="width:100%">
      <div id="myDiagramDiv1" style=" width:730px; height:300px; display: inline-block; vertical-align: top">
      </div>
    </div>
    <div>
      <div id="myDiagramDiv3" style="width: 100px;height: 100px;display: inline-block"></div>
      <div style="width: 630px;height: 100px;display: inline-block">
        <canvas id="canvas1" style="width: 100px;height: 100px"></canvas>
        <canvas id="canvas2" style="width: 100px;height: 100px"></canvas>
        <canvas id="canvas3" style="width: 100px;height: 100px"></canvas>
        <canvas id="canvas4" style="width: 100px;height: 100px"></canvas>
        <canvas id="canvas5" style="width: 100px;height: 100px"></canvas>
        <canvas id="canvas6" style="width: 100px;height: 100px"></canvas>
      </div>
      <div id="myDiagramDiv4" style="width: 100px;height: 100px;display: inline-block"></div>
    </div>
    <div id="myDiagramDiv2" style=" width:730px; height:300px; display: inline-block; vertical-align: top">
    </div>
    <textarea id="mySavedModel1" style="display: none">
      { "class": "GraphLinksModel",
        "nodeDataArray": [
                      {"key":4, "pos":"-600 -230", "color":"blue","icon":"room1","text":"控制间"},
                      {"key":10, "pos":"-350 -230","color":"DodgerBlue","icon":"room2","text":"消毒间"}
                  ],
        "linkDataArray": [
                  {"from":10, "to":10, "fromSpot":"LeftSide", "text":"曝气", "points":[-555,-105,-555,20,-140,20,-140,150]},
                  {"from":10, "to":10, "fromSpot":"LeftSide", "points":[-230,20,130,20,130,150]},
                  {"from":10, "to":10, "fromSpot":"Left", "points":[170,150,170,20,280,20,280,150]},
                  {"from":4, "to":4, "fromSpot":"Left","points":[280,30,280,-120,170,-120,170,30]},
                  {"from":10, "to":10, "fromSpot":"Left","text":"紫外线消毒", "points":[330,150,330,-180,-150,-180],"pipe_color":"blue","color":"blue"}
                  ]}
    </textarea>
    <textarea id="mySavedModel2" style="display: none">
      { "class": "GraphLinksModel",
        "nodeDataArray": [
              {"key":4, "pos":"0 150", "color":"blue","icon":"room1","text":"污泥浓缩池"}
                  ],
        "linkDataArray": [
                  {"from":0, "to":0, "fromSpot":"Left", "points":[600,0,600,100,150,100,150,0],"color":"blue"},
                  {"from":0, "to":0, "fromSpot":"Left", "points":[570,0,570,70,350,70,350,0],"color":"blue"},
                  {"from":0, "to":0, "fromSpot":"Left", "points":[160,100,80,100,80,160],"color":"blue"},
                  {"from":0, "to":0, "fromSpot":"Left", "points":[200,200,300,200],"color":"blue","text":"定期抽取"}
                  ]}
    </textarea>

    <textarea id="mySavedModel3" style="display: none">
      { "class": "GraphLinksModel",
        "linkDataArray": [
                  {"points":[200,70,300,70],"color":"gray","text":"进水"}
                  ]}
    </textarea>
    <textarea id="mySavedModel4" style="display: none">
      { "class": "GraphLinksModel",
        "linkDataArray": [
                  {"points":[200,70,300,70],"color":"green","text":"出水"}
                  ]}
    </textarea>
  </div>
</template>
<script>
  import {go} from '../../../js/go-module'
  function init () {
    let $ = go.GraphObject.make // for conciseness in defining templates
    // Abstract colors
    let Colors = {
      'red': '#be4b15',
      'green': '#52ce60',
      'DodgerBlue': '#1E90FF',
      'blue': '#6ea5f8',
      'lightred': '#fd8852',
      'lightblue': '#afd4fe',
      'lightgreen': '#b9e986',
      'pink': '#faadc1',
      'purple': '#d689ff',
      'orange': '#f08c00'
    }
    let ColorNames = []
    for (let n in Colors) ColorNames.push(n)
    function geoFunc (geoname) {
      let geo = Icons[geoname]
      if (typeof geo === 'string') {
        geo = Icons[geoname] = go.Geometry.parse(geo, true)
      }
      return geo
    }

    // a conversion function for translating general color names to specific colors
    function colorFunc (colorname) {
      var c = Colors[colorname]
      if (c) return c
      return 'gray'
    }

    // Icons derived from SVG paths designed by freepik: http://www.freepik.com/
    let Icons = {}

    Icons.room1 = 'M10,10h100v100h-100zM20,20v80h80v-80z'
    Icons.room2 = 'M10,10h100v100h-100zM20,20v80h80v-80z'
    let IconNames = []
    for (let n in Icons) IconNames.push(n)

    // A data binding conversion function. Given an name, return the Geometry.
    // If there is only a string, replace it with a Geometry object, which can be shared by multiple Shapes.

    let myDiagram1 = $(go.Diagram, 'myDiagramDiv1', // create a Diagram for the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform, // scale to show all of the contents
        'ChangedSelection': onSelectionChanged, // view additional information
        maxSelectionCount: 1, // don't allow users to select more than one thing at a time
        isReadOnly: true
      })
    myDiagram1.nodeTemplate =
      $(go.Node, 'Auto',
        $(go.Panel, 'Horizontal',
          $(go.Shape,
            {
              width: 200,
              height: 120,
              stroke: null,
              strokeWidth: 0,
              fill: 'gray'
            },
            new go.Binding('height', 'icon', function () { return 120 }),
            new go.Binding('fill', 'color', colorFunc),
            new go.Binding('geometry', 'icon', geoFunc),
            new go.Binding('visible', 'visible')
          )
        ),
        new go.Binding('location', 'pos', go.Point.parse).makeTwoWay(go.Point.stringify),
        $(go.TextBlock, {margin: 10},
          new go.Binding('text', 'text'))
      )
    myDiagram1.linkTemplate =
      $(go.Link,
        {
          layerName: 'Background',
          routing: go.Link.Orthogonal,
          corner: 15,
          reshapable: true,
          resegmentable: true,
          fromSpot: go.Spot.RightSide,
          toSpot: go.Spot.LeftSide
        },
        // make sure links come in from the proper direction and go out appropriately
        new go.Binding('fromSpot', 'fromSpot', go.Spot.parse),
        new go.Binding('toSpot', 'toSpot', go.Spot.parse),
        new go.Binding('points').makeTwoWay(),
        // mark each Shape to get the link geometry with isPanelMain: true
        $(go.Shape, { isPanelMain: true, stroke: 'gray', strokeWidth: 10 },
          // get the default stroke color from the fromNode
          new go.Binding('stroke', 'fromNode', function (n) { return go.Brush.lighten((n && Colors[n.data.color]) || 'gray') }).ofObject(),
          // but use the link's data.color if it is set
          new go.Binding('stroke', 'color', colorFunc)),
        $(go.Shape, { isPanelMain: true, stroke: 'white', strokeWidth: 3, name: 'PIPE', strokeDashArray: [20, 40] },
          new go.Binding('stroke', 'pipe_color', colorFunc)
        ),
        $(go.TextBlock, {margin: 50},
          new go.Binding('text', 'text')
        )
      )

    // div2
    let myDiagram2 = $(go.Diagram, 'myDiagramDiv2', // create a Diagram for the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform, // scale to show all of the contents
        'ChangedSelection': onSelectionChanged, // view additional information
        maxSelectionCount: 1, // don't allow users to select more than one thing at a time
        isReadOnly: true,
        initialPosition: new go.Point(0, 0)
      })
    myDiagram2.nodeTemplate =
      $(go.Node, 'Slot',
        $(go.Panel, 'Horizontal',
          $(go.Shape,
            {
              width: 200,
              height: 100,
              stroke: null,
              strokeWidth: 0,
              fill: 'gray'
            },
            new go.Binding('height', 'icon', function () { return 120 }),
            new go.Binding('fill', 'color', colorFunc),
            new go.Binding('geometry', 'icon', geoFunc),
            new go.Binding('visible', 'visible')
          )
        ),
        new go.Binding('location', 'pos', go.Point.parse).makeTwoWay(go.Point.stringify),
        $(go.TextBlock, {margin: 50},
          new go.Binding('text', 'text'))
      )
    myDiagram2.linkTemplate =
      $(go.Link, 'Slot',
        {
          corner: 15
        },
        // make sure links come in from the proper direction and go out appropriately
        new go.Binding('fromSpot', 'fromSpot', go.Spot.parse),
        new go.Binding('toSpot', 'toSpot', go.Spot.parse),
        new go.Binding('points').makeTwoWay(),
        // mark each Shape to get the link geometry with isPanelMain: true
        $(go.Shape, { isPanelMain: true, stroke: 'gray', strokeWidth: 10 },
          // get the default stroke color from the fromNode
          new go.Binding('stroke', 'fromNode', function (n) { return go.Brush.lighten((n && Colors[n.data.color]) || 'gray') }).ofObject(),
          // but use the link's data.color if it is set
          new go.Binding('stroke', 'color', colorFunc)),
        $(go.Shape, { isPanelMain: true, stroke: 'white', strokeWidth: 3, name: 'PIPE', strokeDashArray: [20, 40] },
          new go.Binding('stroke', 'pipe_color', colorFunc)
        ),
        $(go.TextBlock, {margin: 50},
          new go.Binding("text", "text")
        )
      )
    // div3
    let myDiagram3 = $(go.Diagram, 'myDiagramDiv3', // create a Diagram for the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform, // scale to show all of the contents
        'ChangedSelection': onSelectionChanged, // view additional information
        maxSelectionCount: 1, // don't allow users to select more than one thing at a time
        isReadOnly: true,
        initialPosition: new go.Point(0, 0)
      })
    myDiagram3.linkTemplate =
      $(go.Link, 'Slot',
        {
          corner: 15
        },
        // make sure links come in from the proper direction and go out appropriately
        new go.Binding('points').makeTwoWay(),
        // mark each Shape to get the link geometry with isPanelMain: true
        $(go.Shape, { isPanelMain: true, stroke: 'gray', strokeWidth: 10 },
          new go.Binding('stroke', 'color', colorFunc)),
        $(go.Shape, { isPanelMain: true, stroke: 'white', strokeWidth: 3, name: 'PIPE', strokeDashArray: [20, 40] },
          new go.Binding('stroke', 'pipe_color', colorFunc)
        ),
        $(go.TextBlock, {margin: 50},
          new go.Binding("text", "text")
        )
      )

    // div4
    let myDiagram4 = $(go.Diagram, 'myDiagramDiv4', // create a Diagram for the DIV HTML element
      {
        initialAutoScale: go.Diagram.Uniform, // scale to show all of the contents
        'ChangedSelection': onSelectionChanged, // view additional information
        maxSelectionCount: 1, // don't allow users to select more than one thing at a time
        isReadOnly: true,
        initialPosition: new go.Point(0, 0)
      })
    myDiagram4.linkTemplate =
      $(go.Link, 'Slot',
        {
          corner: 15
        },
        // make sure links come in from the proper direction and go out appropriately
        new go.Binding('points').makeTwoWay(),
        // mark each Shape to get the link geometry with isPanelMain: true
        $(go.Shape, { isPanelMain: true, stroke: 'gray', strokeWidth: 10 },
          new go.Binding('stroke', 'color', colorFunc)),
        $(go.Shape, { isPanelMain: true, stroke: 'white', strokeWidth: 3, name: 'PIPE', strokeDashArray: [20, 40] },
          new go.Binding('stroke', 'pipe_color', colorFunc)
        ),
        $(go.TextBlock, {margin: 50},
          new go.Binding("text", "text")
        )
      )

    let SpotNames = ['Top', 'Left', 'Right', 'Bottom', 'TopSide', 'LeftSide', 'RightSide', 'BottomSide']

    myDiagram1.model = go.Model.fromJson(document.getElementById('mySavedModel1').textContent)
    myDiagram2.model = go.Model.fromJson(document.getElementById('mySavedModel2').textContent)
    myDiagram3.model = go.Model.fromJson(document.getElementById('mySavedModel3').textContent)
    myDiagram4.model = go.Model.fromJson(document.getElementById('mySavedModel4').textContent)
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
    loop(myDiagram1) // animate some flow through the pipes
    loop(myDiagram2)
    loop(myDiagram3)
    loop(myDiagram4)
  }
  let opacity = 1
  let down = true
function loop (myDiagram_v) {
    let diagram = myDiagram_v
    setTimeout(function () {
      let oldskips = diagram.skipsUndoManager
      diagram.skipsUndoManager = true
      diagram.links.each(function (link) {
        let shape = link.findObject('PIPE')
        let off = shape.strokeDashOffset - 3
        // animate (move) the stroke dash
        shape.strokeDashOffset = (off <= 0) ? 60 : off
        // animte (strobe) the opacity:
        if (down) opacity = opacity - 0.01
        else opacity = opacity + 0.003
        if (opacity <= 0) { down = !down; opacity = 0 }
        if (opacity > 1) { down = !down; opacity = 1 }
        shape.opacity = opacity
      })
      diagram.skipsUndoManager = oldskips
      loop(myDiagram_v)
    }, 60)
}
  function onSelectionChanged (myDiagram) {
    let node = myDiagram.selection.first()
    if (!(node instanceof go.Node)) return
    let data = node.data
    let image = document.getElementById('Image')
    let title = document.getElementById('Title')
    let description = document.getElementById('Description')
    title.textContent = data.text
    description.textContent = data.description
}
  let wave = {
    speed: 50, // 波浪横向流动速度
    offsetX: 0, // 波浪横向偏移量
    isDrawContainer: false, // 判断是否画了容器
    offsetYRange: 1.1, // 波浪垂直方向最大范围
    offsetY: 0, // 波浪垂直方向位移
    offsetYSpeed: 0.003, // 波浪溢满
    progressNum: 0, // 进度
    text: '调节', // 池子文字
    init: function (id) {
      let canvas = document.getElementById(id)
      let winW = 300

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
      let pointR = this.canvasW / 2
      let lineWidth = 10

      ctx.lineWidth = lineWidth
      ctx.beginPath()
      // ctx.arc(pointR, pointR, circleR, 0, 2 * Math.PI);
      ctx.rect(10, 10, 160, 160)
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
        let y = waveH * Math.sin((startX + x) * waveW + offsetX) + 100

        if (startPos.length === 1) {
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
      ctx.font = "30px '微软雅黑'"
      ctx.textAlign = 'left'
      ctx.fillText(text, 50, 100)
    }
  }
  export default {
    name: 'dynamic',
    mounted () {
      init()
    }
  }
</script>

