wd = wd || {};
wd.ctools = wd.ctools || {};
wd.ctools.interfaces = wd.ctools.interfaces || {};
wd.ctools.utils = wd.ctools.utils || {};

wd.ctools.interfaces.saiku = (function() {
    var myself = {};
    var _handlers = {};
    myself.registerSaveHandler = function(handler){
      _handlers["save"] = handler;
    };

    myself.getSaveHandler = function() {
      return _handlers["save"];
    }
    return myself;
}());

wd.ctools.utils.getOlapCubes = function() {
  jQuery.getJSON("../../olaputils?operation=GetOlapCubes", function(data){
    wd.ctools.utils.olapCubes = data;
  });
};
