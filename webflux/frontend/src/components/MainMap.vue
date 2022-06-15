<template>
  <div class="main-map" ref="map">
  </div>
</template>

<script>
import OlLayerTile from 'ol/layer/Tile.js';
import OlView from 'ol/View.js';
import OlMap from 'ol/Map.js';
import OSM from 'ol/source/OSM';
import {fromLonLat, toLonLat} from 'ol/proj.js'
import {defaults} from 'ol/control.js';
import axios from 'axios'
import Geocoder from 'ol-geocoder'


const EPSG_3857 = 'EPSG:3857';
const EPSG_4326 = 'EPSG:4326';

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
      controls: defaults(
          {
            attribution: false,
            zoom: false,
            rotate: false,
          }
      ),
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
    this.olMap.on('click', async (e) => {
      const lonLatArr = toLonLat(e.coordinate)
      const lon = lonLatArr[0]
      const lat = lonLatArr[1]
      const addressInfo = await that.getAddress(lon, lat)
      this.$root.$refs.sideBar.address = addressInfo.data.display_name.split(", ").reverse().join(" ");
      console.log(toLonLat(e.coordinate));
    })

    const geocoder = new Geocoder('nominatim', {
      provider: 'osm',
      lang: 'kr',
      placeholder: '주소검색',
      limit: 5,
      autoComplete: true,
      keepOpen: true
    });
  },
  methods: {

    se

    getAddress (lon, lat) {
      return axios.get('https://nominatim.openstreetmap.org/reverse',
          {
            params: {
              format: 'json',
              lon: lon,
              lat: lat
            }
          })
    }
  }

}
</script>

<style scoped>
.main-map {
  width: 100%;
  height: 100%;
}

</style>