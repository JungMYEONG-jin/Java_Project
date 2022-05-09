cordova.define("cordova-plugin-xfingeriface.XFingerInterface", function(require, exports, module) {

function XFingerInterface() {
}

XFingerInterface.prototype.setResult = function(approvalResult, reqKey, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'XFingerInterface', 'setResult', [approvalResult, reqKey]);
};

/*
function routeChangeCallback(reason) {
    cordova.fireDocumentEvent('xfingeriface-changed', reason);
}
*/

var xfingerIface = new XFingerInterface();
module.exports = xfingerIface;

});
