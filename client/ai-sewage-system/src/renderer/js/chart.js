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

/*
 * @des 需要写入data/links/categories/三组数据
 */
export const relationOption = {
  title: {
    text: '智能水务知识图谱',
    top: 'top',
    left: 'left'
  },
  tooltip: {
    formatter: function (x) {
      return x.data.des
    }
  },
  toolbox: {
    show: true,
    feature: {
      restore: {
        show: true
      },
      saveAsImage: {
        show: true
      }
    }
  },
  animationDuration: 1000,
  animationEasingUpdate: 'quinticInOut',
  series: [{
    type: 'graph',
    layout: 'force',
    force: {
      repulsion: 100,
      edgeLength: 200,
      layoutAnimation: true
    },
    // 添加标记极度影响性能,直接注释掉
    // edgeSymbol: ['none', 'arrow'],
    // edgeLabel: {
    //   normal: {
    //     show: true
    //   }
    // },
    data: [],
    links: [],
    categories: [],
    roam: true,
    focusNodeAdjacency: true,
    label: {
      position: 'right',
      formatter: '{b}'
    },
    itemStyle: {
      normal: {
        borderColor: '#fff',
        borderWidth: 1,
        shadowBlur: 10,
        shadowColor: 'rgba(0, 0, 0, 0.3)'
      }
    },
    lineStyle: {
      color: 'source',
      curveness: 0.3
    },
    emphasis: {
      lineStyle: {
        width: 10
      }
    }
  }]
}
