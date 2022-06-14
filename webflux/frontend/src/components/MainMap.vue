<template>
  <div class="main-map" ref="map">
  </div>
</template>

<script>
import OlLayerTile from 'ol/layer/Tile.js';
import OlView from 'ol/View.js';
import OlMap from 'ol/Map.js';
import OSM from 'ol/source/OSM';
import {fromLonLat} from 'ol/proj.js'
import {defaults} from 'ol/control.js';


export default {
  name: 'MainMap',
  data() {
    return {
      olMap: undefined,
    }
  },
  mounted() {
    this.olMap = new OlMap({
      target: this.$refs.map,
      controls: defaults({
        attribution: false,
        zoom: false,
        rotate: false,
      }),
      layers: [
        new OlLayerTile({
          source: new OSM()
        })
      ],
      view: new OlView({
        center: fromLonLat([126.5989274, 34.5732516]), // 경기도 성남
        zoom: 10
      })
    })
  }

}
</script>

<style scoped>
.main-map {
  width: 100%;
  height: 100%;
}

</style>