/**
 * @namespace
 */

wd.cdb.connectors = wd.cdb.connectors || {};

/**
 * @constructor
 */
wd.cdb.connectors.ConnectorEngine = (function() {
  var isExperimental = Dashboards.getQueryParameter("experimental") == "true",
      myself = {},
      _connectors = {},
      _experimental = {};

  myself.registerConnector = function(connector,flags) {
    flags = flags || {};
    if(flags.experimental) {
      _experimental[connector.getName()] = connector;
    } else {
      _connectors[connector.getName()] = connector;
    }
  };

  myself.getConnector = function(type) {
    if(isExperimental) { 
      return _experimental[type] || _connectors[type];
    }else {
      return _connectors[type];
    };
  };

  myself.listConnectors = function() {
    var ret = {};
    if(isExperimental) {
      _.extend(ret,_connectors,_experimental);
    } else {
      _.extend(ret,_connectors);
    }
    return ret
  };

  myself.newQuery = function($ph, type, group, name, callback) {
    return myself.getConnector(type).newQuery($ph, group, name, callback);
  };

  myself.readQuery = function(query) {
    myself.getConnector(query.getType()).readQuery(query);
  };

  myself.editQuery = function($ph,query,callback) {
    myself.getConnector(query.getType()).editQuery($ph,query,callback);
  }

  myself.deleteQuery = function(query) {
    myself.getConnector(query.getType()).deleteQuery(query.getName());
  }

  return myself; 
})();

