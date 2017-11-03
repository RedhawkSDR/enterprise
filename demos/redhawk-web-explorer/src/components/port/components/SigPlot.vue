<template>
  <v-container grid-list-md text-xs-center>
    <v-layout row wrap>
      <v-toolbar card color="red" prominent>
        <v-toolbar-title class="body-2 black--text">{{ portName }} Plot</v-toolbar-title>
        <v-spacer/>
        <v-btn
        @click="showPlotMenu()"
        icon>
          <v-icon>menu</v-icon>
        </v-btn>
      </v-toolbar>
      <v-divider></v-divider>
      <v-flex>
        <v-card>
          <v-card-media>
            <v-flex xs10 id="plot">

            </v-flex>
          </v-card-media>
          <v-card-actions>
            <v-layout row wrap>
              <!--
              <v-flex xs6 offset-xs3>
                  <v-select
                    v-bind:items="plots"
                    v-model="e1"
                    label="Plot"
                    dark
                    item-value="text"
                    ></v-select>
              </v-flex>
              -->
              <v-flex xs12 class="py-2">
               <v-btn-toggle mandatory v-model="toggle_exclusive">
                 <v-btn>
                   Line
                 </v-btn>
                 <v-btn>
                   Raster
                 </v-btn>
               </v-btn-toggle>
             </v-flex>
             <v-flex xs12 class="py-2">
               <v-btn
                 :disabled="connected"
                 @click="plotData()" color="green">
                 Connect
               </v-btn>
               <v-btn
                 :disabled="!connected"
                 @click="stopPlot()" color="red">
                 Disconnect
               </v-btn>
             </v-flex>
            </v-layout>
          </v-card-actions>
        </v-card>
      </v-flex>
      <v-flex xs3 v-if="showMenu">
        <v-card>
          <v-card-text>
            <v-select
              v-bind:items="cmodes"
              v-model="cmode"
              label="Plot Mode"
              dark
              item-value="text"
              @input="updateCmode(cmode)"
            ></v-select>
            <v-select
              v-bind:items="drawmodes"
              v-model="drawmode"
              label="Raster Drawmode"
              dark
              item-value="text"
              @input="updateDrawMode(drawmode)"
            ></v-select>
            <v-select
              v-bind:items="colormaps"
              v-model="colormap"
              label="Raster Colormap"
              dark
              @input="updateColorMap(colormap)"
            ></v-select>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
let sigplot = require("sigplot");
//import { FFT } from './dsp.js'

function getTypeArray(type, data){
  if(type=='IDL:BULKIO/dataFloat:1.0'){
    return new Float32Array(data)
  }else if(type=='IDL:BULKIO/dataShort:1.0'){
    return new Int16Array(data)
  }else if(type=='IDL:BULKIO/dataOctet:1.0'){
    return new Uint8Array(data)
  }else if(type=='IDL:BULKIO/dataDouble:1.0'){
    return new Float64Array(data)
  }else if(type=='IDL:BULKIO/dataUshort:1.0'){
    return new Uint16Array(data);
  }else if(type=='IDL:BULKIO/dataLong:1.0'){
    return new Int32Array(data);
  }else if(type=='IDL:BULKIO/dataULong:1.0'){
    return new UInt32Array(data);
  }else{
    return null;
  }
}

export default {
  name: 'sigplot',
  props: ['meta', 'port'],
  data(){
    return {
      plot: null,
      websocket : null,
      showMenu : true,
      sri : {},
      toggle_exclusive: 0,
      connected: false,
      plots : [
        {text : 'Time'},
      ],
      cmode: { text: 'real' },
      cmodes: [
        {
          text: 'magnitude'
        },
        {
          text: 'phase'
        },
        {
          text: 'real'
        },
        {
          text: 'imaginary'
        },
        {
          text: 'real/imag'
        },
        {
          text: '10*log10'
        },
        {
          text: '20*log10'
        }
      ],
      drawmode: { text: 'scrolling'},
      drawmodes: [
        {
          text: 'scrolling'
        },
        {
          text: 'falling'
        },
        {
          text: 'rising'
        }
      ],
      colormap: { text: 'ramp', value : 1 },
      colormaps: [
        {
          text: 'greyscale',
          value: 0
        },
        {
          text: 'ramp',
          value: 1
        },
        {
          text: 'spectrum',
          value: 3
        },
        {
          text: 'wheel',
          value: 2
        },
        {
          text: 'spectrum',
          value: 3
        },
        {
          text: 'sunset',
          value: 4
        }
      ]
    }
  },
  mounted(){
    this.plot = new sigplot.Plot(document.getElementById('plot'), this.options)
  },
  destroyed(){
    if(this.websocket!=null){
      this.websocket.close()
      this.websocket = null
    }
  },
  methods: {
    showPlotMenu(){
      console.log("Made it "+this.showMenu)
      this.showMenu = !this.showMenu
    },
    updateCmode(value){
      this.plot.change_settings({
        cmode : value
      })
    },
    updateColorMap(value){
      this.plot.change_settings({
        cmap : value
      })
    },
    updateDrawMode(value){
      this.plot.change_settings({
        drawmode : value
      })
    },
    plotData(){
      this.plot.deoverlay();
      if(this.toggle_exclusive==0){
        this.plotRT()
      }else if(this.toggle_exclusive==1){
        this.plotRaster()
      }else if(this.toggle_exclusive==2){
        this.plotFFT()
      }
      this.connected = true
    },
    plotFFT(){
      //console.log("Made it to plotFFT")
      var osc1 = new Oscillator(DSP.SINEWAVE, 440, 1, 1024, 44100);
      var hann1 = new WindowFunction(DSP.HANN)
      var fft = new FFT(1024, 44100);
      //osc1.generate();
      //hann1.process(osc1.signal)
      //fft.forward(osc1.signal);
      console.log(fft)
      var spectrum1 = fft.spectrum;
      var plot = this.plot
      var port = this.port

      this.websocket = new WebSocket(this.wsURL)
      this.websocket.binaryType = 'arraybuffer'

      this.websocket.onopen = function(evt){
        console.log("What do we have here ")
        console.log(evt)

        var data_layer = plot.get_layer(0);
        plot.change_settings({
          cmode : 3,
          autol: 5,
          all: true
        });

        /*
        * Adding in onmessage and onclose logic
        */
        var overlay_for_plot;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
            console.log("SRI "+evt.data)
            var sri = JSON.parse(evt.data)
            /*
            * {
              "endOfStream":false, "streamId":"sineStream",
              "hversion":1, "xstart":0.0,
              "xdelta":1.0E-4, "xunits":1,
              "subsize":0,"ystart":0.0,
              "ydelta":0.0,"yunits":0,
              "mode":0,"blocking":true,
              "keywords":{},"tcmode":1,
              "tcstatus":1,"tfsec":0.1698589999999999,
              "toff":0.0,"twsec":1.50887594E9
              }
            */
            overlay_for_plot = plot.overlay_array(null, {
              xdelta: sri.xdelta,
              xunits: sri.xunits,
              yunits: sri.yunits,
              subsize: sri.subsize
            })
          }else{
            var arr = new getTypeArray(port.repId, evt.data);
            fft.forward(arr);
            var spectrum1 = fft.spectrum;
            plot.reload(0, spectrum1)
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }

    },
    stopPlot(){
      if(this.websocket!=null)
        this.websocket.close()

      this.connected = false
    },
    plotRaster(){
      if(this.websocket!=null){
        this.websocket.close()
      }
      this.websocket = new WebSocket(this.wsURL)
      this.websocket.binaryType = 'arraybuffer'
      var plot = this.plot
      var port = this.port

      this.websocket.onopen = function(evt) {
        console.log("On Open")

        var data_layer = plot.get_layer(0);
        plot.change_settings({
          cmode : 3,
          autol: 5,
          all: true
        });

        var overlay_for_plot, sri;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
            console.log("SRI "+evt.data)
            sri = JSON.parse(evt.data)
            /*
            * {
              "endOfStream":false, "streamId":"sineStream",
              "hversion":1, "xstart":0.0,
              "xdelta":1.0E-4, "xunits":1,
              "subsize":0,"ystart":0.0,
              "ydelta":0.0,"yunits":0,
              "mode":0,"blocking":true,
              "keywords":{},"tcmode":1,
              "tcstatus":1,"tfsec":0.1698589999999999,
              "toff":0.0,"twsec":1.50887594E9
              }
            */
            var signalSubsize = 0
            if(sri.subsize==0){
              signalSubsize = 2048
            }else{
              signalSubsize = sri.subsize
            }
          plot.overlay_pipe({
              type: 2000,
              xdelta: sri.xdelta,
              xunits: 'Frequency',
              yunits: sri.xunits,
              subsize: 1000,
              pipesize : 1000000
            });
        }else{
          var arr = new getTypeArray(port.repId, evt.data);
          plot.push(0, arr)
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }
    },
    plotRT(){
      if(this.websocket!=null){
        this.websocket.close()
      }

      //Need to make sure inside of websocket method I still have access to plot
      var sri = this.sri
      var port = this.port
      this.websocket = new WebSocket(this.wsURL)
      this.websocket.binaryType = 'arraybuffer'

      this.plot.change_settings({
        cmode : this.cmode.text,
        autol: 5,
      });

      var plot = this.plot
      this.websocket.onopen = function(evt){
        console.log("What do we have here ")
        console.log(evt)

        var data_layer = plot.get_layer(0);

        /*
        * Adding in onmessage and onclose logic
        */
        var overlay_for_plot;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
            console.log("SRI "+evt.data)
            sri = JSON.parse(evt.data)
            /*
            * {
              "endOfStream":false, "streamId":"sineStream",
              "hversion":1, "xstart":0.0,
              "xdelta":1.0E-4, "xunits":1,
              "subsize":0,"ystart":0.0,
              "ydelta":0.0,"yunits":0,
              "mode":0,"blocking":true,
              "keywords":{},"tcmode":1,
              "tcstatus":1,"tfsec":0.1698589999999999,
              "toff":0.0,"twsec":1.50887594E9
              }
            */
            overlay_for_plot = plot.overlay_array(null, {
              xdelta: sri.xdelta,
              ydelta: sri.ydelta,
              xunits: 3,
              yunits: sri.yunits,
              subsize: sri.subsize
            })
          }else{
            var arr = new getTypeArray(port.repId, evt.data);
            plot.reload(overlay_for_plot, arr)
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }
    }
  },
  computed:{
    portName(){
      return this.$route.params.portName
    },
    wsURL(){
      return this.$store.getters.portWSURL
    }
  }
}
</script>

<style>
#plot {
  height: 400px;
}
</style>
