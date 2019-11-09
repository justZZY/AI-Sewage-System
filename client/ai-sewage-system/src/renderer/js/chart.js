export const rateOption = {
  title: {
    text: '仪表盘初始化模板',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  series: [
    {
      type: 'gauge',
      detail: {
        formatter: '{value}'
      },
      data: [
        {
          value: 0
        }
      ],
      min: 0,
      max: 40
    }
  ]
}

export const monitorOption = {
  title: {
    text: '折线图初始化模板',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  toolbox: {
    show: true,
    feature: {
      mark: { show: true }
    }
  },
  calculable: true,
  xAxis: [
    {
      type: 'category',
      boundaryGap: false,
      data: [], // 转换为时间戳
      axisLabel: {
        show: false
      }
    }
  ],
  yAxis: [
    {
      type: 'value',
      name: '纵坐标',
      axisLabel: {
        formatter: '{value}'
      }
    }
  ],
  series: [
    {
      type: 'line',
      data: []
    }
  ]
}
