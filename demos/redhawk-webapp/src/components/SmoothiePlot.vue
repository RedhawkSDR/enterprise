<template>
  <canvas id="mycanvas" width="700" height="100"></canvas>
</template>

<script>
var smoothie;
var sigplotWS = null;
export default{
  name: 'smoothie',
  mounted(){
    smoothie = new SmoothieChart();
    smoothie.streamTo(document.getElementById("mycanvas"));
    console.log('Smoothie')
    console.log(smoothie)
  },
  computed: {
    wsURL(){
      return this.$store.getters.wsURL+'.json'
    }
  },
  watch: {
		wsURL: function(){
			if(this.wsURL!=null){
        if(sigplotWS!=null){
          sigplotWS.disconnect()
        }
				console.log('Do Websocket stuff')
				sigplotWS = new WebSocket(this.wsURL)
				sigplotWS.binaryType = 'arraybuffer'
				sigplotWS.onopen = function(evt) {
					console.log("Connected.");
				};

				sigplotWS.onclose = function() {
					console.log("Shutdown.");
				};

				var self = this
        var messageReceived = 0;
        var ts = new TimeSeries();
        var xdelta;
        smoothie.addTimeSeries(ts)
        sigplotWS.onmessage = function(evt) {
          if(messageReceived!=0){
            for(var i in evt.data){
                ts.append(new Date().getTime(), i)
            }
          }else{
            console.log('SRI '+evt.data)
            var sri = JSON.parse(evt.data);
						xdelta = sri.xdelta

            messageReceived++;
          }
				};

				console.log('Created WS')
				console.log(sigplotWS)
			}else{
				console.log('No url so no websocket')
			}
		}
	}
}
</script>
