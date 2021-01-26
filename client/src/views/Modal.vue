<template>
  <b-modal :id="modalId" :title="$store.getters.modalTitle" title-class="no-user-select title-5"
    hide-footer>
    <div class="d-block no-user-select title-4 text-center">
      <h4 v-if="chosenTime.assistant !== undefined">
        <strong>{{ chosenTime.time }}</strong> @{{ chosenTime.assistant.username }}
      </h4>
      <vac v-if="modalId === 'confirm'" speed=10 :end-time="new Date().getTime() + 10000">
        <template v-slot:process="{ timeObj }">
          <h1 class="my-4 countdown font-weight-bolder">{{ `0${timeObj.ceil.s}`.substr(-2) }}</h1>
        </template>
      </vac>
    </div>

    <b-form class="article-6" v-if="modalId === 'alert'" @submit.prevent="$emit('dismiss')">
      <b-button class="mt-3 float-md-right" type="submit" variant="danger">
        Dismiss
      </b-button>
      <slot></slot>
    </b-form>

    <b-form class="article-6" v-if="modalId === 'confirm'"
      @submit.prevent="validated() && $emit('confirm')" @reset.prevent="$emit('cancel')">
      <b-form-input v-model="chosenTime.confirmedBy" :state="validated()" class="form-control"
        type="text" placeholder="Your name...">
      </b-form-input>
      <b-form-invalid-feedback>
        Enter at least 3 letters, but not more than 24
      </b-form-invalid-feedback>
      <b-button class="mt-3 float-md-right" type="submit" variant="danger" :disabled="!validated()">
        Confirm
      </b-button>
      <b-button class="mt-3 mx-3 float-md-right" type="reset" variant="secondary">
        Cancel
      </b-button>
      <slot></slot>
    </b-form>
  </b-modal>
</template>

<script>
export default {
  name: 'Modal',
  props: { id: String },
  data() {
    return {
      modalId: this.id,
    };
  },
  computed: {
    chosenTime: {
      get() { return this.$store.getters.chosenTime; },
    },
  },
  mounted() {
    const self = this;
    self.$root.$on('bv::modal::hide', (ev) => {
      if (['esc', 'backdrop', 'headerclose'].indexOf(ev.trigger) > -1) {
        self.$emit('cancel');
      }
    });
  },
  methods: {
    validated() {
      return (this.chosenTime.confirmedBy || '').match(/^\p{L}{3,24}$/u) !== null;
    },
  },
};
</script>
