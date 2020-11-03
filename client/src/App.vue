<template>
  <div class="no-user-select article-6" id="app">
    <b-navbar toggleable="lg" type="light" variant="light" sticky=true>
      <b-navbar-brand class="title-4">Booking</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav class="title-5">
          <b-nav-item-dropdown>
            <template #button-content>Admin</template>
            <b-dropdown-item href="#" @click="redirect('/admin')">Profile</b-dropdown-item>
            <b-dropdown-item href="#" @click="logout()"
              :disabled="!this.$store.getters.isAuthenticated">
              Sign Out
            </b-dropdown-item>
          </b-nav-item-dropdown>
          <b-nav-item href="#" @click="redirect('/times')">Time Slots</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="title-5 ml-auto">
          <b-nav-item right>
            <Info id="labInstructions" placement="bottomleft" title="Lab Instructions">
<p>
  <b>Your task</b> in this lab assignment is to build <b>a simple lab booking system</b>.
  <br><br>
  The system should consist of (at least) <b>three pages</b>:
  <br><br>
  <ul>
    <li>A booking page including all information about the time slots and their current state.</li>
    <li>A confirmation page including a countdown timer where the students may complete their
    booking.</li>
    <li>An admin page for the assistants only where they can edit (add/remove) their own times.
    </li>
  </ul>
  <br><br>
  ...
  <br><br>
  For more detailed lab instructions, please visit the
  <i><a href="https://kth.instructure.com/courses/21342/assignments/137566">Canvas site</a></i>!
</p>
            </Info>
          </b-nav-item>
          <b-nav-item right>
            <b-avatar button @click="redirect('/admin')" variant="primary"
              :text="this.$store.getters.signedInAvatar">
            </b-avatar>
          </b-nav-item>
        </b-navbar-nav>

      </b-collapse>
    </b-navbar>

    <transition name="fade" mode="out-in">
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
import Info from './views/Info.vue';

export default {
  name: 'App',
  components: { Info },
  created() {
    if (performance.navigation.type === performance.navigation.TYPE_RELOAD) {
      console.info('This page is reloaded');
      this.redirect('/times');
    } else {
      console.info('This page is not reloaded');
    }

    const { commit, dispatch } = this.$store;
    commit('resetAuthMessage');
    dispatch('isAuthenticated');
  },
  computed: {
    authenticated() {
      return this.$store.getters.isAuthenticated;
    },
  },
  watch: {
    authenticated(value) {
      if (value) {
        this.redirect('/admin');
      } else {
        this.redirect('/login');
      }
    },
  },
  methods: {
    redirect(target) {
      this.$router.push(target)
        .catch((e) => console.log(e.message));
    },
    logout() {
      const { commit, dispatch } = this.$store;
      return fetch('/api/secured/v1/logout', {
        method: 'POST',
        headers: {
          'X-XSRF-TOKEN': `${this.$cookies.get('XSRF-TOKEN')}`,
        },
      }).then((res) => res.json())
        .then(({ assistant = null, authenticated = false }) => {
          commit('authenticate', { assistant, authenticated });
          if (!authenticated) {
            commit('setAuthMessage', { message: 'Signed out successfully', variant: 'success' });
          }
          dispatch('reconnect');
        }).catch(console.error);
    },
  },
};
</script>

<style lang="scss">
@keyframes fade-in
{
  0% { opacity: 0.0; }
  30% { opacity: 0.6; }
  100% { opacity: 1.0; }
}
.fade-enter-active
{
  animation: fade-in 0.4s;
}
.fade-leave-active
{
  animation: fade-in 0.4s reverse;
}
</style>
