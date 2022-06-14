<template>
  <div id="app">
    <MainMap/>
    <SideBar class="side-bar"></SideBar>
  </div>
</template>

<script>
// import HelloWorld from './components/HelloWorld.vue'
import MainMap from "@/components/MainMap";
import SideBar from "@/components/SideBar";

export default {
  name: 'App',
  components: {
    MainMap,
    SideBar
  }
}
</script>

<style lang="scss">
/*#app {*/
/*  font-family: Avenir, Helvetica, Arial, sans-serif;*/
/*  -webkit-font-smoothing: antialiased;*/
/*  -moz-osx-font-smoothing: grayscale;*/
/*  text-align: center;*/
/*  color: #2c3e50;*/
/*  margin-top: 60px;*/
/*}*/
#app{
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;

  > .side-bar{
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
  }
}
</style>
