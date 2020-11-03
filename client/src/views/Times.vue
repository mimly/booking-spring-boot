<template>
  <div class="container">
    <section class="col-md-12">
      <b-card class="my-2" bg-variant="light">
        <template #header>
          <h4 class="title-1 text-center font-italic">
            All time slots
            <Info id="reservationInstructions" placement="bottomleft" title="Other Instructions">
<p>
  Fetch all the times, e.g. by sending an ordinary HTTP AJAX request, and then keep updating their
  state in real-time. This can be achieved through any of several techniques, e.g.
  <i><a href="https://socket.io/">socket.io library</a></i> based on WebSocket protocol.
  <br><br>
  Make sure that the user won't be allowed to reserve some already reserved time - try it and check
  the web console output afterwards, complete a time booking without reserving the time first, book
  a time that has already been booked by someone else, etc. Remember that all checks need to be
  done, as always, regardless of the protocol being used, on the server side. Do <b>not</b> trust
  the client, check it!
</p>
            </Info>
          </h4>
        </template>

        <b-list-group class="row">
          <b-list-group-item :class="['my-2', {'connecting': !$store.getters.isConnected}]"
            v-show="!$store.getters.isConnected" variant="warning">
            <h5 class="my-2 article-6 text-center">Please wait while connecting...</h5>
          </b-list-group-item>

          <transition-group name="fade" mode="out-in">
            <Time v-show="$store.getters.isConnected" v-for="time of times" :key="time.id"
              :id="time.id" @reserve="reserve(time)">
            </Time>
          </transition-group>
        </b-list-group>

        <Modal id="alert" @dismiss="$bvModal.hide('alert')">
          <Info class="mt-3 mx-3 float-md-right" id="alertInstructions" placement="left"
            title="Other Instructions">
<p>
  Check the web console output now!
  <br><br>
  Do not hesitate while developing this application to send some arbitrary data over WebSocket by
  accesssing the corresponding function in the appropriate Vue component, i.e. <i>view-model</i>,
  instance and check how the server responds. Remember to compile the Vue components in the
  <i>development</i>, and not <i>production</i> mode in order to be able to do that -
  <i>npm run build-dev</i>.
</p>
          </Info>
        </Modal>

        <Modal id="confirm" @confirm="confirm()" @cancel="cancel()">
          <Info class="mt-3 mx-3 float-md-right" id="confirmationInstructions" placement="left"
            title="Other Instructions">
<p>
  Countdown should be implemented both on the client and the server side.
  <br><br>
  The client should control if the application needs to be redirected to the main page, i.e. the
  page showing all the time slots.
  <br><br>
  The server, on the other hand, should decide whether the time slot is allowed to be reserved or
  not.
</p>
          </Info>
        </Modal>
      </b-card>
    </section>
  </div>
</template>

<script>
import Info from './Info.vue';
import Modal from './Modal.vue';
import Time from './Time.vue';

export default {
  name: 'Times',
  components: { Info, Modal, Time },
  computed: {
    socket: {
      get() { return this.$store.getters.socket; },
    },
    times: {
      get() { return this.$store.getters.times; },
      set(value) { this.$store.commit('setTimes', value); },
    },
    chosenTime: {
      get() { return this.$store.getters.chosenTime; },
      set(value) { this.$store.commit('setChosenTime', value); },
    },
  },
  mounted() {
    this.$store.commit('setModal', this.$bvModal);
  },
  methods: {
    reserve(time) {
      this.chosenTime = time;
      this.$store.commit('setLoading', true);
      // to simulate network latency...
      // await (() => new Promise((resolve) => setTimeout(resolve, 2000)))();
      this.socket.send('/app/ws-api/v1/make-reservation', {}, JSON.stringify(time));
    },
    cancel() {
      this.socket.send('/app/ws-api/v1/cancel-reservation', {}, JSON.stringify(this.chosenTime));
    },
    confirm() {
      this.socket.send('/app/ws-api/v1/confirm-reservation', {}, JSON.stringify(this.chosenTime));
    },
  },
};
</script>

<style lang="scss">
@keyframes connecting
{
  0% { opacity: 0.6; }
  30% { opacity: 1.0; }
  100% { opacity: 0.6; }
}
.connecting
{
  animation: connecting 3.6s infinite;
}
</style>
