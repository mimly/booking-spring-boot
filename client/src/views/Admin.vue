<template>
  <div class="container">
    <section class="col-md-12">
      <b-card class="my-2" bg-variant="light">
        <template #header>
          <h4 class="title-1 text-center font-italic">
            Signed in <em>as</em> {{ $store.getters.signedIn }}
            <Info id="adminInstructions" placement="right" title="Other Instructions">
<p>
  Two aspects are important here, <i>communication</i> and <i>security</i>.
  <br><br>
  <i>Communication</i> works well if the time/times that has/have been added/removed immediately
   appears/disappear in/from all other connected clients. Feel free to use
  <i><a href="https://socket.io/">socket.io library</a></i> based on WebSocket or any other
  technique/protocol of Your choice.
  <br><br>
  Open at least two different clients (web browsers with 1+ tabs each) or just one single client in
  two different modes, ordinary and incognito. Then add/remove some times, open/close some
  clients/tabs and make sure that everything works properly.
  <br><br>
  <i>Security</i> is a bit more complicated topic.
  <br><br>
  Both forms validate the input data. The first one makes it impossible to send some non-valid,
  <i>suspicious</i> data by using the web interface. The second one makes it <b>not</b>. For
  example, it's allowed for the <i>Lorem</i> user to try to remove those times that belong to
  <i>Ipsum</i> and vice versa. Such an attempt from the very beginning is doomed to fail due to the
  server side checks. However, the client gives such an opportunity.
  <br><br>
  Is it enough to state without further consideration that the application is secured properly? The
  first form, the second one, both, any? The answer is <b>NO</b>.
</p>
            </Info>
          </h4>
        </template>

        <div class="row">
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
          <div class="col-xs-8 col-sm-6 col-md-4">
            <b-form @submit.prevent="validateAddition() && addTime()">
              <b-form-timepicker class="my-2" v-model="timeToAdd" :state="validateAddition()"
                locale="sv" menu-class="article-6" now-button reset-button no-close-button>
              </b-form-timepicker>
              <b-form-invalid-feedback :state="!validateAddition()">
                <span v-if="!validateTimeFormat()">No time selected</span>
                <span v-if="!validateTimeAvailability()">Time already exists</span>
              </b-form-invalid-feedback>
              <b-alert class="my-2 text-center" v-if="additionError" variant="danger" fade show>
                {{ additionError }}
              </b-alert>
              <b-button class="my-2 font-bold" type="submit" :disabled="!validateAddition()" block
                variant="success">
                ADD
              </b-button>
            </b-form>
            <Info id="additionInstructions" placement="left" title="Other Instructions">
<p>
  This part of the client is secured, i.e. it's impossible to send non-valid data by using
  <i>only</i> the web interface.
  <br><br>
  Open <i>Network</i> tab among <i>Web Developer</i> options and add some time. Then, right-click on
  the HTTP AJAX request being sent and copy it as fetch. Switch tab to <i>Web Console</i> and
  paste it. Try to change the payload to some non-valid, <i>suspicious</i> data before pressing the
  <i>Enter</i> key. How has the server responded? Has the server crashed? Has "the time" been added?
  <br><br>
  <b>Securing client is <i>recommended</i>, but still <i>optional</i>. Securing server is absolutely
  <i>mandatory</i>.</b>
</p>
            </Info>
          </div>
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
        </div>

        <div class="row">
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
          <div class="col-xs-8 col-sm-6 col-md-4">
            <b-form @submit.prevent="validateRemoval() && removeTimes()">
              <b-form-checkbox-group class="my-2" :state="validateRemoval()">
                <b-form-checkbox class="mt-2" v-for="time in times" v-model="timesToRemove"
                  :key="time.id" :value="time.id">

                  <b-card title-tag="h5" :title="`Time: ${time.time}`"
                    sub-title-tag="h6" :sub-title="`@${time.assistant.username}`"
                    body-class="pt-2 pb-1" footer-class="py-1" footer-border-variant="danger">
                    <template #footer v-if="time.confirmedBy !== null">
                      <div class="text-right">
                        <span class="mx-2 font-weight-light font-italic">booked by</span>
                        <span class="font-weight-bolder">{{ time.confirmedBy }}</span>
                      </div>
                    </template>
                  </b-card>

                </b-form-checkbox>
                <b-form-invalid-feedback :state="validateRemoval()">
                  Please select at least one time
                </b-form-invalid-feedback>
              </b-form-checkbox-group>
              <b-alert class="my-2 text-center" v-if="removalError" variant="danger" fade show>
                {{ removalError }}
              </b-alert>
              <b-button class="my-2 font-bold" type="submit" :disabled="!validateRemoval()" block
                variant="danger">
                REMOVE
              </b-button>
            </b-form>
            <Info id="removalInstructions" placement="left" title="Other Instructions">
<p>
  This part of the client is <i>mostly</i> secured, i.e. it's possible to send some non-valid (in
  that particular case, <i>unauthorized</i>) data by using <i>only</i> the web interface as
  discussed above.
  <br><br>
  Once again... Open <i>Web Console</i>, copy the respective HTTP AJAX request as fetch and play
  with it before sending. Feel free to change not only the payload, but also the headers and check
  how the server will respond.
  <br><br>
  <b>Securing client is <i>recommended</i>, but still <i>optional</i>. Securing server is absolutely
  <i>mandatory</i>.</b>
</p>
            </Info>
          </div>
          <div class="col-xs-2 col-sm-3 col-md-4"></div>
        </div>
      </b-card>
    </section>
  </div>
</template>

<script>
import Info from './Info.vue';

export default {
  name: 'Admin',
  components: { Info },
  data: () => ({
    indeterminateRemoveForm: true,
    timeToAdd: null,
    timesToRemove: [],
    additionError: null,
    removalError: null,
  }),
  computed: {
    times: {
      get() { return this.$store.getters.times; },
      set(value) { this.$store.commit('setTimes', value); },
    },
  },
  watch: {
    timesToRemove(newValue, oldValue) {
      if (newValue === null) {
        this.indeterminateRemoveForm = true;
        this.timesToRemove = [];
      } else if (oldValue !== null) {
        this.indeterminateRemoveForm = false;
      }
    },
  },
  methods: {
    validateTimeFormat() {
      return this.timeToAdd === null
        ? this.timeToAdd
        : this.timeToAdd.match(/^([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/) !== null;
    },
    validateTimeAvailability() {
      const { times, signedIn } = this.$store.getters;
      return this.timeToAdd === null
        ? this.timeToAdd
        : !times
          .filter((time) => time.assistant.username === signedIn)
          .map((time) => time.time)
          .includes(this.timeToAdd.substring(0, 5));
    },
    validateAddition() {
      return this.timeToAdd === null
        ? this.timeToAdd
        : this.validateTimeFormat() && this.validateTimeAvailability();
    },
    validateRemoval() {
      return this.indeterminateRemoveForm
        ? null
        : this.timesToRemove.length !== 0;
    },
    addTime() {
      const { commit, dispatch } = this.$store;
      const request = fetch('/api/secured/v1/times', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': `${this.$cookies.get('XSRF-TOKEN')}`,
        },
        body: JSON.stringify({
          time: this.timeToAdd.substring(0, 5),
        }),
      });
      this.timeToAdd = null;
      return Promise.resolve(request)
        .then(async (res) => {
          try {
            this.additionError = null;
            const { message } = await res.json();
            if (message) {
              this.additionError = message;
            }

            if (res.status === 401 || res.status === 403) {
              commit('setAuthMessage', { message, variant: 'danger' });
              dispatch('isAuthenticated');
            }
          } catch (e) {
            console.error(e);
          }
        }).catch(console.error);
    },
    removeTimes() {
      const { commit, dispatch } = this.$store;
      const requests = this.timesToRemove.map((id) => fetch(`/api/secured/v1/times/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': `${this.$cookies.get('XSRF-TOKEN')}`,
        },
      }));
      this.timesToRemove = null;
      return Promise.all(requests)
        .then((responses) => {
          this.removalError = null;
          responses.forEach(async (res) => {
            try {
              const { message } = await res.json();
              if (message) {
                this.removalError = message;
              }

              if (res.status === 401 || res.status === 403) {
                commit('setAuthMessage', { message, variant: 'danger' });
                dispatch('isAuthenticated');
              }
            } catch (e) {
              console.error(e);
            }
          });
        }).catch(console.error);
    },
  },
};
</script>

<style lang="scss">
form .custom-control,form .custom-control .custom-control-label
{
  width: 100%;
}
</style>
