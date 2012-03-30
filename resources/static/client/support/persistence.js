wd = wd || {};
wd.ctools = wd.ctools || {};
wd.ctools.persistence = (function(){
  var myself = {},
      persistenceUrl = webAppPath + "/content/cdb/storage";

  myself.query = function(query,callback) {
    var params = {
      method: "query",
      query: query
    };

    $.getJSON(persistenceUrl,params,function(response){
        callback(response);
    });
  };

  myself.saveObject = function(key,type,obj) {
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
      if(response.id)
      obj.setKey(response.id);
    });
  };

  return myself;
}());
