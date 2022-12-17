cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "cordova-plugin-device.device",
        "file": "plugins/cordova-plugin-device/www/device.js",
        "pluginId": "cordova-plugin-device",
        "clobbers": [
            "device"
        ]
    },
    {
        "id": "cordova-plugin-iosrtc.Plugin",
        "file": "plugins/cordova-plugin-iosrtc/dist/cordova-plugin-iosrtc.js",
        "pluginId": "cordova-plugin-iosrtc",
        "clobbers": [
            "cordova.plugins.iosrtc"
        ]
    },
    {
		"id": "cordova-plugin-wkwebview-engine.ios-wkwebview-exec",
		"file": "plugins/cordova-plugin-wkwebview-engine/src/www/ios/ios-wkwebview-exec.js",
		"pluginId": "cordova-plugin-wkwebview-engine",
		"clobbers": [
			"cordova.exec"
		]
    },
    {
    	"id": "cordova-plugin-wkwebview-engine.ios-wkwebview",
    	"file": "plugins/cordova-plugin-wkwebview-engine/src/www/ios/ios-wkwebview.js",
    	"pluginId": "cordova-plugin-wkwebview-engine",
    	"clobbers": [
    		"window.WkWebView"
    	]
    },
    {
        "id": "cordova-plugin-android-permissions.Permissions",
        "file": "plugins/cordova-plugin-android-permissions/www/permissions-dummy.js",
        "pluginId": "cordova-plugin-android-permissions",
        "clobbers": [
            "cordova.plugins.permissions"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-device": "1.1.6",
    "cordova-plugin-iosrtc": "4.0.2",
    "cordova-plugin-whitelist": "1.3.2",
    "cordova-plugin-android-permissions": "1.0.0",
    "cordova-plugin-wkwebview-engine": "1.1.4"
};
// BOTTOM OF METADATA
});