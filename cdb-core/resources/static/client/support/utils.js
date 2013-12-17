window.wd = window.wd || {};
wd.ctools = wd.ctools || {};
wd.ctools.utils = wd.ctools.utils || {};


wd.ctools.utils.createGUID = function() {
  function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
  }
  return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}
