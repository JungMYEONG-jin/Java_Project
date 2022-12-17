cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-iosrtc/dist/cordova-plugin-iosrtc.js",
        "id": "cordova-plugin-iosrtc.Plugin",
        "pluginId": "cordova-plugin-iosrtc",
        "clobbers": [
            "cordova.plugins.iosrtc"
        ]
    },
    {
        "file": "plugins/cordova-plugin-audioroute/www/audioroute.js",
        "id": "cordova-plugin-audioroute.AudioRoute",
        "pluginId": "cordova-plugin-audioroute",
        "clobbers": [
            "cordova.plugins.audioroute"
        ]
    },
    {
        "file": "plugins/cordova-plugin-xfingeriface/www/xfingeriface.js",
        "id": "cordova-plugin-xfingeriface.XFingerInterface",
        "pluginId": "cordova-plugin-xfingeriface",
        "clobbers": [
            "cordova.plugins.xfingeriface"
        ]
    },
    {
    	"file": "plugins/cordova-plugin-wkwebview-engine/www/ios-wkwebview.js",
        "id": "cordova-plugin-wkwebview-engine.ios-wkwebview",
        "pluginId": "cordova-plugin-wkwebview-engine",
        "clobbers": [
            "window.WkWebView"
        ]
    },
    {
    	"file": "plugins/cordova-plugin-wkwebview-engine/www/ios-wkwebview-exec.js",
        "id": "cordova-plugin-wkwebview-engine.ios-wkwebview-exec",
        "pluginId": "cordova-plugin-wkwebview-engine",
        "clobbers": [
            "cordova.exec"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-iosrtc": "3.0.0",
    "cordova-plugin-whitelist": "1.2.2",
    "cordova-plugin-audioroute": "0.1.2",
    "cordova-plugin-xfingeriface": "0.0.1",
    "cordova-plugin-wkwebview-engine":"1.2.1"
}
// BOTTOM OF METADATA 
});
