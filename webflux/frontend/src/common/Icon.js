import Vue from 'vue';

import {library} from "@fortawesome/fontawesome-svg-core";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

// 원하는 아이콘 불러오기
import {
    faAngleLeft,
    faAngleRight,
    faLocationDot,
} from "@fortawesome/free-solid-svg-icons";

// 불러온 아이콘 lib 담기
library.add(faAngleLeft);
library.add(faAngleRight);
library.add(faLocationDot);

// vue template에서 icon 사용 가능하게 등록
Vue.component("FontAwesomeIcon", FontAwesomeIcon);