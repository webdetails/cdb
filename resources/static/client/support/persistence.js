wd = wd || {};
wd.ctools = wd.ctools || {};
wd.ctools.persistence = (function(){
  var myself = {},
      persistenceUrl = webAppPath + "/content/cdb/storage";

  myself.query = function(query,callback) {
    throw "UnsupportedOperation";
    var params = {
      method: "query",
      query: query
    };

    $.getJSON(persistenceUrl,params,function(response){
        callback(response);
    });
  };

  myself.saveObject = function(key,type,obj, callback) {
    var params = {
      method: "store",
      "class": type,
      data: obj.toJSON(),
    };

    if (key) {
      params.rid = key;
    } else if(obj.getKey && (key = obj.getKey())) {
      params.rid = key;
    }

    $.getJSON(persistenceUrl,params,function(response){
      if(response.id) obj.setKey(response.id);
      if(callback && typeof callback == "function") callback(obj);
      
    });
  };

  myself.deleteObject = function(key, type) {
    var params = {
      method: "delete",
      "class": type,
      rid: key
    };

    $.getJSON(persistenceUrl,params,function(response){
      if(response.id)
      obj.setKey(response.id);
    });

  }
  return myself;
}());
