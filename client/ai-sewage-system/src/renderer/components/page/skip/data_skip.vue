<template>

</template>

<script>
  export default {
    name: 'data_skip',
    created () {
      this.$router.replace({
        path: '@/components/page/site_data',
        name: 'site_data'
      })
    }
  }
</script>

<style scoped>

</style>
