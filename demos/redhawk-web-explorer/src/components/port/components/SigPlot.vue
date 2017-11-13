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

/*
* Function to turn typed array to appropriate
* type.
*/
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

/*
* Predefined XUnits
*
const short UNITS_NONE         = 0;  // None / Not Applicable (N/A)
const short UNITS_TIME         = 1;  // Time (sec)
const short UNITS_DELAY        = 2;  // Delay (sec)
const short UNITS_FREQUENCY    = 3;  // Frequency (Hz)
const short UNITS_TIMECODE     = 4;  // Time code format
const short UNITS_DISTANCE     = 5;  // Distance (m)
const short UNITS_VELOCITY     = 6;  // Velocity (m/sec)
const short UNITS_ACCELERATION = 7;  // Acceleration (m/sec^2)
const short UNITS_JERK         = 8;  // Jerk (m/sec^3)
const short UNITS_DOPPLER      = 9;  // Doppler (Hz)
const short UNITS_DOPPLERRATE  = 10; // Doppler rate (Hz/sec)
const short UNITS_ENERGY       = 11; // Energy (J)
const short UNITS_POWER        = 12; // Power (W)
const short UNITS_MASS         = 13; // Mass (g)
*/
function getSigplotMappingToCommonUnitCodes(unit){
  if(unit == 0){
    return 'None'
  }else if(unit == 1){
    return 'Time(sec)'
  }else if(unit == 3){
    return 'Frequency (Hz)'
  }else{
    return unit
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
        this.plotFFTLine()
        //this.plotRT()
      }else if(this.toggle_exclusive==1){
        this.plotFFTRaster()
        //this.plotRaster()
      }

      this.connected = true
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

      this.plot.change_settings({
        cmode : this.cmode.text,
        cmap : this.colormap.value,
        drawmode : this.drawmode.text,
        autol: 5,
        all: true
      });

      this.websocket.onopen = function(evt) {
        console.log("On Open")

        var data_layer = plot.get_layer(0);
        var data_size = null;
        var overlay_for_plot, sri;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
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
            sri = JSON.parse(evt.data)
        }else{
          var arr = new getTypeArray(port.repId, evt.data);

          if(data_size==null){
            data_size = arr.length
            sri.xunits = getSigplotMappingToCommonUnitCodes(sri.xunits)
            sri.yunits = getSigplotMappingToCommonUnitCodes(sri.yunits)

            var pl =  plot.overlay_pipe({
                type: 1000,
                yunits: sri.yunits,
                xunits: sri.xunits,
                subsize : data_size,
                pipesize : 1000000,
                ystart: sri.ystart,
                xstart: sri.xstart,
                xdelta: sri.xdelta
              });
          }

          plot.push(0, arr)
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }
    },
    plotFFTRaster(){
      if(this.websocket!=null){
        this.websocket.close()
      }
      this.websocket = new WebSocket(this.wsURL+'?fft=true')
      this.websocket.binaryType = 'arraybuffer'
      var plot = this.plot
      var port = this.port

      this.plot.change_settings({
        cmode : this.cmode.text,
        cmap : this.colormap.value,
        drawmode : this.drawmode.text,
        autol: 5,
        all: true
      });

      this.websocket.onopen = function(evt) {
        var data_layer = plot.get_layer(0);
        var data_size = null;
        var overlay_for_plot, sri;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
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
            sri = JSON.parse(evt.data)
        }else{
          var arr = new getTypeArray(port.repId, evt.data);

          if(data_size==null){
            console.log(sri)
            data_size = arr.length
            sri.xunits = getSigplotMappingToCommonUnitCodes(sri.xunits)
            sri.yunits = getSigplotMappingToCommonUnitCodes(sri.yunits)
            console.log(sri.xdelta)
            var fs = 1/sri.xdelta
            var myXend = fs/2
            var derived_xdelta = fs/(data_size*2)
            var myXstart = -1*derived_xdelta
            console.log("Derived Xdelta "+derived_xdelta)
            // Derived SRI Based on
            //var derived_xdelta = (sri.xdelta/data_size)/2
            //Derived From PSD https://github.com/RedhawkSDR/psd/blob/master/cpp/psd.cpp
            //var derived_xdelta = 1/(sri.xdelta*data_size)
            // Derived SRI Based on
            //var derived_xdelta = (sri.xdelta/data_size)/2
            //Derived From PSD https://github.com/RedhawkSDR/psd/blob/master/cpp/psd.cpp
            //var derived_xdelta = 1/(sri.xdelta*data_size)
            //var derived_ydelta = sri.xdelts*data_size
            var pl =  plot.overlay_pipe({
                type: 1000,
                xunits: sri.xunits,
                xstart : myXstart,
                xend : myXend,
                xdelta: derived_xdelta,
                subsize : data_size,
                pipesize : 1000000,
              });
          }

          plot.push(0, arr)
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }
    },
    plotRT(){
      var fs = 44100;
      var bufsize = 2048;

      var osc1 = new Oscillator(DSP.SINEWAVE, 440, 1, 2048, fs);
      if(this.websocket!=null){
        this.websocket.close()
      }

      //Need to make sure inside of websocket method I still have access to plot
      var sri = this.sri
      var port = this.port
      var data_size = null
      this.websocket = new WebSocket(this.wsURL)
      this.websocket.binaryType = 'arraybuffer'

      this.plot.change_settings({
        cmode : this.cmode.text,
        autol: 1,
      });

      var plot = this.plot
      var changeup = 0;
      this.websocket.onopen = function(evt){
        /*
        * Adding in onmessage and onclose logic
        */
        var overlay_for_plot;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
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
            sri = JSON.parse(evt.data)
          }else{
            var arr = new getTypeArray(port.repId, evt.data);

            if(data_size==null){
              console.log(sri)
              data_size = arr.length
              sri.xunits = getSigplotMappingToCommonUnitCodes(sri.xunits)
              sri.yunits = getSigplotMappingToCommonUnitCodes(sri.yunits)
              console.log(data_size)

              overlay_for_plot = plot.overlay_array(null, {
                xdelta: sri.xdelta,
                ydelta: sri.ydelta,
                xunits: sri.xunits,
                yunits: sri.yunits,
                size: data_size,
              })
            }

            changeup++
            if(changeup%10==0){
              //TODO: Rotate the array around so it appears to be moving
              plot.reload(overlay_for_plot, arr)
            }else{
              plot.reload(overlay_for_plot, arr)
            }
          }
        }

        this.onclose = function(evt){
          console.log("Close")
        }
      }
    },
    plotFFTLine(){
      if(this.websocket!=null){
        this.websocket.close()
      }

      //Need to make sure inside of websocket method I still have access to plot
      var sri = this.sri
      var port = this.port
      var data_size = null
      this.websocket = new WebSocket(this.wsURL+'?fft=true')
      this.websocket.binaryType = 'arraybuffer'

      this.plot.change_settings({
        cmode : this.cmode.text,
        autol: 1,
      });

      var plot = this.plot
      this.websocket.onopen = function(evt){
        /*
        * Adding in onmessage and onclose logic
        */
        var overlay_for_plot;
        this.onmessage = function(evt){
          if(typeof evt.data == "string"){
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
            sri = JSON.parse(evt.data)
          }else{
            var arr = new getTypeArray(port.repId, evt.data);

            if(data_size==null){
              data_size = arr.length
              sri.xunits = getSigplotMappingToCommonUnitCodes(sri.xunits)
              sri.yunits = getSigplotMappingToCommonUnitCodes(sri.yunits)
              //Derived after convo with Max
              console.log(sri.xdelta)
              var fs = 1/sri.xdelta
              var myXend = fs/2
              var derived_xdelta = fs/(data_size*2)
              var myXstart = -1*derived_xdelta
              console.log("Derived Xdelta "+derived_xdelta)
              // Derived SRI Based on
              //var derived_xdelta = (sri.xdelta/data_size)/2
              //Derived From PSD https://github.com/RedhawkSDR/psd/blob/master/cpp/psd.cpp
              //var derived_xdelta = 1/(sri.xdelta*data_size)

              overlay_for_plot = plot.overlay_array(null, {
                xstart : myXstart,
                xend : myXend,
                xunits: sri.xunits,
                xdelta: derived_xdelta,
                size: data_size,
              })
            }

            plot.reload(overlay_for_plot, arr)
            plot.refresh()
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
