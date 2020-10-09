<template>

</template>

<script>
  export default {
    name: 'info_skip',
    created () {
      this.$router.replace({
        path: '@/components/page/site_info',
        name: 'site_info'
      })
    }
  }
</script>

<style scoped>

</style>
