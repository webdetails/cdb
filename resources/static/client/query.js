/**
 * @constructor
 */

wd.cdb.TypeAlreadySetException = function(){};
wd.cdb.Query = function (label,type,definition) {
  "use strict";
  var _label = label,
      _group,
      _query = definition,
      _type = type,
      _id,
      _guid = wd.ctools.utils.createGUID();

  function updateBackend() {
    /* We only want to notify the backend of changes
     * to queries it already knows about, and the id
     * is the only way of knowing that's the case.
     */
    if (_id) {}
  }

  this.getDefinition = function() {
    return _query;
  };

  this.setDefinition = function(query) {
    _query = query;
  };

  this.getLabel = function() {
    return _label;
  };
  this.setLabel = function(label){
    _label = label;
    updateBackend();
  };
  this.getType = function() {
    return _type;
  };
  this.setType = function(type){
      if(_type) throw new wd.cdb.TypeAlreadySetException; 
    _type = type;
  };
  this.toJSON = function() {
    return JSON.stringify({name: _label, group: _group, type: _type, definition: _query});
  };

  this.getGroup = function() {
    return _group;
  };
  this.setGroup = function(group) {
    _group = group;
    wd.cdb.QueryManager.newGroup(group);
    wd.cdb.QueryManager.getGroup(group).addQuery(this);
    updateBackend();
  };

  this.getGUID = function () {
    return _guid
  };

  this.getKey = function() {
    return _id;
  };
  this.setKey = function(key) {
    _id = key;
  };

  this.duplicate = function() {
    var that = new wd.cdb.Query(this.getLabel() + "Copy",this.getType(),this.getDefinition());
    that.setGroup(this.getGroup());
    copyBackend(that.getLabel(),that.getGroup());
    return that;
  };

  this.deleteSelf = function() {
    var params = {
      method: 'deleteQuery',
      group: this.getGroup(),
      name: this.getLabel()
    };
    var myself = this;
    $.getJSON(webAppPath + "/content/cdb/connector", $.param(params),function(response){
      wd.ctools.persistence.deleteObject(myself.getKey(), "Query");
    });
  }
}

/**
 * @constructor
 */
wd.cdb.QueryGroup = function(label) {
  var _label = label,
      _queries = {};

  this.getLabel = function() {
      return _label;
  };
  
  this.setLabel = function(label){
    _label = label;
  };

  this.addQuery = function(dataAccess) {
      _queries[dataAccess.getLabel()] = dataAccess;
  };

  this.getQuery = function(label) {
      return _queries[label];
  }

  this.listQueries = function() {
      return _queries;
  };

  this.toJSON = function() {
      return JSON.stringify({label: _label, queries: _queries.map(function(e){return e.getLabel();})});
  };

  this.save = function() {
  var q, query;
  for (q in _queries) if (_queries.hasOwnProperty(q)) {
    query = _queries[q];
    wd.ctools.persistence.saveObject(null,"Query",query);
  }

  $.getJSON("connector?method=exportCda&group=" + _label,function(){console.log("Saved to CDA")});
  };

  this.deleteQuery = function(queryName) {
    var query = this.getQuery(queryName);

    delete _queries[queryName];
    query.deleteSelf();
  }
}

wd.cdb.QueryManager = (function() {
    var myself = {};
    var _groups = {};

    myself.newGroup = function(label) {
      if (!myself.getGroup(label)) {
        myself.addGroup(new wd.cdb.QueryGroup(label));
      }
      return myself.getGroup(label);
    };

    myself.getGroup = function(label) {
      return _groups[label];
    };

    myself.addGroup = function(group) {
        _groups[group.getLabel()] = group;
    };

    myself.loadGroup = function(label,callback) {
      wd.ctools.persistence.query("select * from Query where group = \"" + label +"\"",function(results){
        if(results) {
          var group = new wd.cdb.QueryGroup(label), query, q, queryData;
          myself.addGroup(group);
          for (q in results.object) if (results.object.hasOwnProperty(q)) {
            queryData = results.object[q];
            query = new wd.cdb.Query(queryData.name, queryData.type, queryData.definition);
            query.setKey(queryData["@rid"]);
            query.setGroup(queryData.group);
            group.addQuery(query);
          }
          callback(group);
        }
      });
    };

    myself.deleteGroup = function(label) {
    };
    
    myself.listGroups = function(){
      return _groups;
    };
    
    myself.loadGroupList = function() {
      $.getJSON('query?method=listGroups',{},function(response){
        var groups = response.object,
            i;
        for (i = 0; i < groups.length;i++) {
          myself.newGroup(groups[i].name);
        }
      });
    };
    
    return myself;
}());
