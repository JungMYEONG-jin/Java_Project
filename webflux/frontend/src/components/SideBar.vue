<template>
  <div class="side-bar-wrapper">
    <VueResizable
    class="resizable-side-bar"
    :width="100"
    :min-width="100"
    :max-width="Infinity"
    :active="['r']"
    v-if="isVisibleSideBar">
    <div class="side-bar">

    </div>
    </VueResizable>
    <BButton
      class="side-bar-active-btn"
      size="sm"
      @click="showSideBar">
      {{ isVisibleSideBar ? 'closed' : 'opened'}}
    </BButton>
  </div>
</template>

<script>
import VueResizable from 'vue-resizable';

export default {
  name: "SideBar",
  components: {
    VueResizable
  },
  data(){
    return {
      isVisibleSideBar: true
    }
  },
  methods: {
    showSideBar(){
      this.isVisibleSideBar = !this.isVisibleSideBar;
    }
  }
}
</script>

<style lang="scss" scoped>
.side-bar-wrapper {
  display: flex;

  > .resizable-side-bar {
    > .side-bar {
      background-color: rgba(0, 0, 0, 0.5);
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
    }
  }

  > .side-bar-active-btn{
    flex-shrink: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #000000;
    padding: 0;
    border: none;
    border-radius: unset;
    color: #fff;
    opacity: 0.5;
    width: 40px;
    height: 40px;
  }
}
</style>