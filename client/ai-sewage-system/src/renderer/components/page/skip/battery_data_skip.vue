<template>
</template>

<script>
export default {
  name: 'battery_data_skip',
  created () {
    this.$router.replace({
      path: '@/components/page/site_battery_data',
      name: 'site_battery_data'
    })
  }
}
</script>

<style scoped>

</style>
