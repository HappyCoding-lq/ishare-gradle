<template>
  <PageIntegrator componet-name="ZIPKIN"></PageIntegrator>
</template>

<script>
import PageIntegrator from '@/components/tools/PageIntegrator'
export default {
  components: {
    PageIntegrator
  },
  name: 'Zipkin'
}
</script>

<style scoped>
  iframe{
    border: 0;
  }
</style>
