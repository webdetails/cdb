(function() {
SqlConnector = function() {
  var _name = 'SQL',
      _label = 'SQL Query'
      saikuPath = webAppPath + "/content/saiku-ui/index.html?",
      editor = null;
 
  function showEditor($ph,queryObj,callback) {
    var def = queryObj.getDefinition() || {},
        code = "<div><input class='jndi' type='text'><div id='query' style='position:relative;width:100%;height:900px'></div></div>";
    def = typeof def == "string" ? JSON.parse(def) : def;
    $ph.html(code);
    editor = ace.edit($ph.find('#query').get(0));
    $ph.find('.jndi').val(def.jndi);
    setTimeout(function(){
        editor.getSession().setValue(def.query);
        editor.navigateFileStart();
        editor.resize();
      }, 10);
  }

  this.getName = function() {
    return _name;
  }
  this.getLabel = function() {
    return _label;
  }
  this.newQuery = function($ph, query, callback) {
    function cb(jndi, sql){
      query.setDefinition({jndi: jndi, query: sql});
      callback(query);
    };
    showEditor($ph, query, cb);
  };

  this.editQuery = function($ph,query, callback) {
    function cb(jndi, sql){
      query.setDefinition({jndi: jndi, query: sql});
      callback(query);
    };
    showEditor($ph, query, cb);
  };

  this.saveQuery = function($ph, queryObj, callback) {
    queryObj.setDefinition({
      jndi: $ph.find('.jndi').val(),
      query: editor.getSession().getValue()
    });
    callback(queryObj);
  }
}

wd.cdb.connectors.ConnectorEngine.registerConnector(new SqlConnector(),{experimental:true});
}());
