<template>
  <div>
    <el-card shadow="always">
      <div class="clearfix" slot="header">
        <span>频率控制器</span>
      </div>
      <div>
        <div class="slider">
          <span>sfghfsfasdlfkjhsdlfgbzdslkjbva;dsvg</span>
          <el-slider v-model="value" vertical height="200px" show-input :show-input-controls="false"
                     :max="30"  @change="test(value)">
          </el-slider>
        </div>
        <div class="slider">
          <span>sfghadsfasdfadcafg</span>
          <el-slider v-model="value" vertical height="200px" show-input :show-input-controls="false"
                     :max="30" @change="test(value)">
          </el-slider>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'system_manage',
    data () {
      return {
        value: 0
      }
    },
    methods: {
      test (t) {
        console.log(t)
      }
    }
  }
</script>

<style scoped>
  .slider {
    margin-left: 20px;
    float: left;
  }
  .el-slider {
    margin-top: 10px;
    margin-left: 35%;
  }
</style>
