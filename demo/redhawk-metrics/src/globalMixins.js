const GlobalMixins = {
  install (Vue) {
    Vue.mixin({
      async mounted () {
        import('jquery').then(async ($) => {
          window.jQuery = window.$ = $
          await import('bootstrap-material-design')
          //TODO: Look deeper into why init is not there likely version difference
          //from template
          //$.material.init()
        }
        )
      }
    })
  }
}

export default GlobalMixins
