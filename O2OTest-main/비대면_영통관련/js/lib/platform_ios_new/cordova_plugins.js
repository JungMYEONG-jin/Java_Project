cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "cordova-plugin-iosrtc.Plugin",
      "file": "plugins/cordova-plugin-iosrtc/www/cordova-plugin-iosrtc.js",
      "pluginId": "cordova-plugin-iosrtc",
      "clobbers": [
        "cordova.plugins.iosrtc"
      ]
    },
    {
      "id": "cordova-plugin-inappbrowser.inappbrowser",
      "file": "plugins/cordova-plugin-inappbrowser/www/inappbrowser.js",
      "pluginId": "cordova-plugin-inappbrowser",
      "clobbers": [
        "cordova.InAppBrowser.open"
      ]
    }
  ];
  module.exports.metadata = {
    "cordova-plugin-add-swift-support": "2.0.2",
    "cordova-plugin-iosrtc": "6.0.15",
    "cordova-plugin-whitelist": "1.3.4",
    "cordova-plugin-inappbrowser": "4.0.0"
  };
});