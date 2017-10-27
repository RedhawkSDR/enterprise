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
              <v-flex xs12>
               <v-btn-toggle mandatory v-model="toggle_exclusive">
                 <v-btn>
                   Real Time
                 </v-btn>
                 <v-btn>
                   Raster
                 </v-btn>
                 <v-btn flat>
                   FFT
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

function getTypeArray(type, data){
  if(type=='IDL:BULKIO/dataFloat:1.0'){
    return new Float32Array(data)
  }else if(type=='IDL:BULKIO/dataShort:1.0'){
    return new Int16Array(data)
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
      }else if(this.toggle_excluse==2){
        console.log("Plot FFT")
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
          plot.overlay_pipe({type: 2000, subsize: 409, file_name: "random"});
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
      var plot = this.plot
      var sri = this.sri
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
              xunits: sri.xunits,
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
