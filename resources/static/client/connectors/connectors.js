/**
 * @namespace
 */

wd.cdb.connectors = wd.cdb.connectors || {};

/**
 * @constructor
 */
wd.cdb.connectors.ConnectorEngine = (function() {
  var myself = {};
  var _connectors = {};
  myself.registerConnector = function(connector) {
    _connectors[connector.getName()] = connector;
  };
  myself.getConnector = function(type) {
    return _connectors[type];
  };
  myself.listConnectors = function() {
    return _connectors
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

