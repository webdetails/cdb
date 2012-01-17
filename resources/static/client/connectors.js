var wd = wd || {};
var wd.cdb = wd.cbd || {};

wd.cdb.ConnectorEngine = function() {
  var _connectors = {};
  this.registerConnector = function(connector) {
    _connectors[connector.getLabel()] = connector;
  };
  this.getConnector = function(type) {
    return _connectors[type];
  };

  this.newQuery = function(group, name, type, callback) {
    return this.getConnector[type].newQuery(name, group, callback);
  };

  this.readQuery = function(query) {
    this.getConnector(query.type).readQuery(query.group, query.name);
  };

  this.updateQuery = function(query) {
    this.getConnector(query.type).updateQuery(query.group, query.name);
  }
 
  this.deleteQuery = function(query) {
    this.getConnector(query.type).deleteQuery(query.group, query.name);
  }
};

