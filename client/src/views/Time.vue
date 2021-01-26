<template>
  <b-list-group-item :class="['my-2', {'loading': $store.getters.isLoading(time), 'reserved':
    time.reserved && !time.confirmedBy, 'confirmed': time.confirmedBy}]" :variant="highlight(time)"
    href="#" @click="!$store.getters.isLoadingAnyTime && $emit('reserve')">
    <div v-show="time.assistant !== undefined" class="title-5 text-center">
      <span class="mx-2 font-weight-bolder">{{ time.time }}</span>
      <span>@{{ time.assistant.username }}</span>
    </div>
    <div v-show="time.confirmedBy !== null" class="article-7 text-right">
      <span class="mx-2 font-weight-light font-italic">booked by</span>
      <span class="font-weight-bolder">{{ time.confirmedBy }}</span>
    </div>
  </b-list-group-item>
</template>

<script>
export default {
  name: 'Time',
  props: { id: Number },
  data() {
    return {
      itemId: this.id,
    };
  },
  computed: {
    time: {
      get() { return this.$store.getters.time(this.itemId); },
    },
  },
  methods: {
    highlight(time) {
      if (time.confirmedBy !== null) return 'danger';
      if (time.reserved === true) return 'warning';
      return 'success';
    },
  },
};
</script>

<style lang="scss">
@keyframes loading
{
  0% { opacity: 0.4; }
  100% { opacity: 0.8; }
}
.loading
{
  animation: loading 1.2s infinite;
}

@keyframes reserved
{
  0% { transform: scaleX(0.996); }
  30% { transform: scaleX(1.010); }
  100% { transform: scaleX(0.996); }
}
.reserved
{
  box-shadow: 0.6rem 0.6rem 0.8rem 0 rgba(0, 0, 0, 0.25),
    -0.4rem -0.4rem 0.6rem 0 rgba(255, 255, 255, 0.3);
  animation: reserved 3.6s infinite;
}

.confirmed
{
  box-shadow: 0.6rem 0.6rem 0.8rem 0 rgba(0, 0, 0, 0.25),
    -0.4rem -0.4rem 0.6rem 0 rgba(255, 255, 255, 0.3);
}
</style>
