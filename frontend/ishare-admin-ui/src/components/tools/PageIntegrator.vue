<template>
  <a-spin :spinning="spinning" size="large" tip="加载ing...">
    <iframe id="pageIntegrator" width="100%" @load="loading()" :src="url"></iframe>
  </a-spin>
</template>

<script>
export default {
  name: 'PageIntegrator',
  data () {
    return { spinning: true }
  },
  props: {
    componetName: {
      type: String,
      default: () => ''
    }
  },
  methods: {
    loading () {
      this.spinning = false
    }
  },
  mounted () {
    if (this.$store.state.tool.componentUrls[this.componetName]) {
      console.log(this.componetName)
    } else {
      this.$store.dispatch('tool/allUrls')
    }
  },
  computed: {
    url () {
      return this.$store.state.tool.componentUrls[this.componetName]
    }
  }
}
</script>

<style scoped>
  iframe{
    border: 0;
    min-height: 800px;
    background-color: #fff;
    box-shadow: 0 0 1px #e8e8e8;
  }
</style>
