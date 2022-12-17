var app = {
    start: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        app.receivedEvent('deviceready');
        document.addEventListener('audioroute-changed', function(event) {
            cordova.plugins.audioroute.currentOutputs(function(outputs) {
            	if(event.reason != 'override' && cordova.plugins.iosrtc) {
            		if(outputs[0].indexOf('builtin') > -1) {
            			cordova.plugins.iosrtc.selectAudioOutput('speaker');
            		} else {
            			cordova.plugins.iosrtc.selectAudioOutput('earpiece');
            		}
            	}
            }, null);
        });
    },

    receivedEvent: function(id) {
        console.log('Received Event: ' + id);

        $('html, body').css('background-color', 'transparent');
        /*
        $('#video_article').css('background-image', 'none');
        $('#video_remote').css('z-index', -1);
        $('#previewVideoDiv').css('opacity', 0);
        */
        $('#video_local').css('z-index', -1);

        var videos = document.getElementsByTagName('video');
        for(var i in videos) {
            videos[i].playsInline = true;
        }

        cordova.plugins.iosrtc.registerGlobals();
        cordova.plugins.iosrtc.debug.enable('iosrtc*');
        this.loadedComplete = true;
    },

    setVideoSrcObject: function(video, stream) {
        video.srcObject = stream;
    },

    loadedComplete: false
};
app.start();