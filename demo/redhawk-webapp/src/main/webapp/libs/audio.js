/*

Queue.js

A function to represent a queue

Created by Stephen Morley - http://code.stephenmorley.org/ - and released under
the terms of the CC0 1.0 Universal legal code:

http://creativecommons.org/publicdomain/zero/1.0/legalcode

*/
function Queue(){

  var queue  = [];
  var offset = 0;

  this.getLength = function(){
    return (queue.length - offset);
  }

  this.isEmpty = function(){
    return (queue.length == 0);
  }

  this.enqueue = function(item){
    queue.push(item);
  }

  this.dequeue = function(){

    if (queue.length == 0) return undefined;

    var item = queue[offset];

    if (++ offset * 2 >= queue.length){
      queue  = queue.slice(offset);
      offset = 0;
    }

    return item;

  }

  this.peek = function(){
    return (queue.length > 0 ? queue[offset] : undefined);
  }

  this.clear = function() {
    while(queue.length > 0) {
      queue.pop();
    }
    offset = 0;
  }

  this.truncate = function(len) {
    while (queue.length > len) {
        this.dequeue()
    }
  }
}

function AudioService() {
    this.QUEUE_MIN = 2;
    this.QUEUE_MAX = 2;

    this.forceStopAudio = true;

    this.audioContext = new AudioContext();
    this.ear = {};
    this.ear.compression = this.audioContext.createDynamicsCompressor();
    this.ear.compression.threshold.value = -50;
    this.ear.compression.knee.value = 40;
    this.ear.compression.ratio.value = 12;
    this.ear.compression.reduction.value = -20;
    this.ear.compression.attack.value = 0;
    this.ear.compression.release.value = 0.25;
    this.ear.compression.connect(this.audioContext.destination);

    this.ear.panner = this.audioContext.createPanner();
    this.ear.panner.panningModel = 'equalpower';
    this.ear.panner.setPosition(0,0,0);
    this.ear.panner.connect(this.ear.compression);

    this.ear.lowpass = this.audioContext.createBiquadFilter();
    this.ear.lowpass.frequency.value = 1000;
    this.ear.lowpass.connect(this.ear.panner);

    this.ear.gainControl = this.audioContext.createGain();
    this.ear.gainControl.gain.value = 1;
    this.ear.gainControl.connect(this.ear.lowpass);

    this.ear.playing = false;
    this.ear.queue = new Queue();
    this.ear.waiting = [];
    this.ear.queueSize = 4;
    this.ear.buffer = undefined;
    this.ear.contextStartTime = 0;
    this.ear.playTime = 0;
    this.ear.sampleRate = 8000;
    this.ear.stopAudio = false;

    return this;
}

AudioService.prototype.updateVolume = function(value) {
    this.ear.gainControl.gain.value = value;
}

AudioService.prototype.setSampleRate = function(newRate) {
    this.ear.sampleRate = Math.round(newRate);
}

AudioService.prototype.stopAudio = function () {
    this.updateVolume(0);
    this.ear.queue.clear();
    this.ear.queue = new Queue();
    this.ear.playing = false;
    this.ear.waiting = [];
    this.ear.queueSize = 4;
    this.ear.contextStartTime = 0;
    this.ear.playTime = 0;

    if (this.ear.source) {
        try {
            this.ear.source.stop(0);
            this.ear.source.buffer = null;
            this.ear.source = null;
        } catch (e) {
            console.error("error! ", e);
        }
    }

    this.forceStopAudio = true;
}

AudioService.prototype.playFromQueue = function() {
    var queue = this.ear.queue;
    if (this.forceStopAudio) {
        return;
    }

    if (!this.ear.playing) {
        this.ear.playing = true;
        this.ear.contextStartTime = this.audioContext.currentTime + 0.1;
    }

    while (queue.getLength() > 0) {
        var buffer = queue.dequeue();

        this.ear.source = this.audioContext.createBufferSource();
        this.ear.source.buffer = buffer;
        this.ear.source.connect(this.ear.gainControl);

        var currStartTime = this.ear.contextStartTime + this.ear.playTime + .25;
        this.ear.source.start(currStartTime);
        this.ear.playTime += this.ear.source.buffer.duration;
    }
}

AudioService.prototype.bufferAudioPacket = function(packet, queue, sampleRate) {
    var buffer = this.audioContext.createBuffer(1, packet.length, sampleRate);
    buffer.getChannelData(0).set(packet);

    queue.enqueue(buffer);
    queue.truncate(this.QUEUE_MAX);
}

AudioService.prototype.allowPlay = function() {
    this.forceStopAudio = false;
    this.ear.gainControl.gain.value = 1;
}

AudioService.prototype.playAudio = function(data) {
    if (!data || data.length === 0) {
        return;
    }

    if (!this.forceStopAudio) {
        this.bufferAudioPacket(data, this.ear.queue, this.ear.sampleRate);

        if (this.ear.queue.getLength() >= this.QUEUE_MIN) {
            this.playFromQueue();
        }
    }
}

AudioService.prototype.reset = function() {
	this.ear.queue.clear();
}