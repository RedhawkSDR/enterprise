/*
* Use this method to update data in the index
*/
export const updateIndex = (state, obj) => {
  //TODO: Should be able to do below
  //console.log(state[key])
  if(obj.key=='available'){
    state.available = obj.value
    console.log("Yay it works")
  }else{
    console.log("Unknown key "+available)
  }
}
