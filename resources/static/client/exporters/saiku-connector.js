(function() {
SaikuConnector = function() {
  var _name = 'Saiku',
      saikuPath = webAppPath + "/saiku-ui/index.html?";
  function openSaiku(placeholder, solution,path,action, callback, mode) {
    var params = $.extend({
        biplugin: true
      },
      (mode == "edit" ? {
        solution: solution,
        path: path,
        action: action,
        dimension_prefetch: false,
        mode: "edit"     
      } :
      mode == "new" ?
        {} :
        {}
      )),
      paramString = $.param(params);

    /* Not entirely sure why, but we need the hash with
     * /query/open/ and a third part of some sort. It
     * works irrespectively of what we put in there, so
     * 'blank' does just fine.
     */
    var $iframe = $("<iframe>", {
          src: saikuPath + paramString + "#query/open/blank",
          style: 'width:100%;height:700px'
        });
    placeholder.empty().append($iframe);
    wd.ctools.interfaces.saiku.registerSaveHandler({
      getSavePath: function() {
        return {filename: action, solution: solution, path: path};
      },
      processSave: function(__,response) {
        var cubeName = response.cube.catalogName,
              cubeList = wd.ctools.utils.olapCubes.catalogs,
              cube;
          for (var i = 0; i < cubeList.length;i++) {
            if(cubeList[i].name == cubeName) {
                cube = cubeList[i];
                break;
            }
          }
          console.log("catalog: " + cube.schema);
          console.log("jndi: " + cube.jndi);
          console.log("query: " + response.mdx);

        callback(cube.schema, cube.name, cube.jndi, response.mdx);
      }
    });
  }

  this.getLabel = function() {
    return _name;
  }
  this.newQuery = function($ph, group, name, callback) {
    function cb(schema, cube, jndi, mdx){
      var query = new wd.cdb.Query(name,"Saiku",{catalog: schema, cube: cube, jndi: jndi, query: mdx});
      query.setGroup(group);
      callback(query);
    };
    openSaiku($ph, "cdb", "saiku" , group + "-" + name + ".saiku", cb, "new");
  };
  this.editQuery = function(placeholder,query, callback) {
    function cb(schema, cube, jndi, mdx){
      query.setDefinition({catalog: schema, cube: cube, jndi: jndi, query: mdx});
      callback(query);
    };
    openSaiku(placeholder, "cdb", "saiku" , query.getGroup() + "-" + query.getLabel() + ".saiku", cb, "edit");    
  };
  this.deleteQuery = function(query) {
  };
  this.viewQuery = function(placeholder,query) {
  };
}

wd.cdb.connectors.ConnectorEngine.registerConnector(new SaikuConnector());
}());
