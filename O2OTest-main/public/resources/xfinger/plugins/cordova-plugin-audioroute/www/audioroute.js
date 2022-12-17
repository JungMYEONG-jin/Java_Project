cordova.define("cordova-plugin-audioroute.AudioRoute", function(require, exports, module) {

function AudioRoute() {
    cordova.exec(routeChangeCallback, null, 'AudioRoute', 'setRouteChangeCallback', []);
}

AudioRoute.prototype.currentOutputs = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'currentOutputs', []);
};

AudioRoute.prototype.overrideOutput = function(output, successCallback, errorCallback) {
    if (output !== 'default' && output !== 'speaker') {
        throw new Error('output must be one of "default" or "speaker"');
    }
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'overrideOutput', [output]);
};

AudioRoute.prototype.toggleMute = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'toggleMute', []);
};

AudioRoute.prototype.setMute = function(mute, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'AudioRoute', 'setMute', [mute]);
};

function routeChangeCallback(reason) {
    cordova.fireDocumentEvent('audioroute-changed', reason);
}

var audioRoute = new AudioRoute();
module.exports = audioRoute;

});
