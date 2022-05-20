<template>
  <div class="info-item">
    <a-row>
      <a-col class="info-label" :span="7" >{{ label }}:</a-col>
      <a-col class="info" :span="17" >{{ info }}<slot></slot></a-col>
    </a-row>
  </div>
</template>

<script>
import RouteView from './RouteView'
export default {
  name: 'InfoItem',
  props: {
    label: {
      type: String,
      default: ''
    },
    info: {
      type: String,
      default: ''
    }
  },
  components: { RouteView }
}
</script>

<style lang="less" scoped>

  .info-item {
    .info-label,.info{
      margin: 5px 0;
      height: 25px;
      line-height: 25px;
    }
    .info-label{
      color: rgba(0, 0, 0, 0.85);
      white-space:pre
    }
    .info{
      color:#777;
    }
  }

</style>
