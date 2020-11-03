<template>
  <b-link disabled>
    <b-icon-question-circle :id="infoId" class="h5" animation="cylon-vertical" variant="warning"/>
    <b-popover :target="infoId" triggers="click blur" :placement="infoPlacement" variant="warning"
      custom-class="no-user-select article-7 text-justify">
      <template #title class="no-user-select title-6 font-weight-bold">{{ infoTitle }}</template>
      <slot></slot>
    </b-popover>
  </b-link>
</template>

<script>
export default {
  name: 'Info',
  props: ['id', 'placement', 'title'],
  data() {
    return {
      infoId: this.id,
      infoPlacement: this.placement,
      infoTitle: this.title,
    };
  },
};
</script>
