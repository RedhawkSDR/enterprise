<template>
  <div class="modal-mask">
          <div class="modal-wrapper">
                  <div class="control-modal-container">
                          <div class="header">
                                  <b>Control Waveform</b>
                          </div>
                          <div class="waveform-info">
                          <span><b>id:</b> {{ identifier }}</span><br>
                          <span><b>name:</b> {{ name }} </span><br>
                          <span><b>started:</b> {{ started }} </span><br>
                          </div>
                          <div class="control-buttons">
                                  <button v-on:click="start" :disabled="started">Start</button>
                                  <button v-on:click="stop" :disabled="!started">Stop</button>
                                  <button v-on:click="release">Release</button>
                                  <button v-on:click="cancel">Cancel</button>
                          </div>
                  </div>
          </div>
  </div>
</template>
