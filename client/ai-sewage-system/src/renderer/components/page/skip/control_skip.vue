<template>

</template>

<script>
  export default {
    name: 'control_skip',
    created () {
      this.$router.replace({
        path: '@/components/page/remote_control',
        name: 'remote_control'
      })
    }
  }
</script>

<style scoped>

</style>
