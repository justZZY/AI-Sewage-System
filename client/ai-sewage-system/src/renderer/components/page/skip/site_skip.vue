<template>

</template>

<script>
  export default {
    name: 'site_skip',
    created () {
      this.$router.replace({
        path: '@/components/page/site_watch',
        name: 'site_watch'
      })
    }
  }
</script>

<style scoped>

</style>
