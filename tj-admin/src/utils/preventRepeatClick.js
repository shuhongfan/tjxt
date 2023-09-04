export default {
  install(Vue) {
      Vue.directive('preventReClick', (el, binding) => {
        function preventReClickFun(elValue, bindingValue) {
          if (!elValue.disabled) {
            elValue.disabled = true
            setTimeout(() => {
              elValue.disabled = false
            }, bindingValue.value || 2000)
          }
        }
        el.addEventListener('click', () => preventReClickFun(el, binding))
        binding.dir.unmounted = function() {
          el.removeEventListener('click', () => preventReClickFun(el, binding))
        }
      })
  }
}