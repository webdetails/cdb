(function() {
SaikuConnector = function() {
  var _name = 'Saiku',
      _label = 'MDX Query via Saiku',
      _solution = cdbFunctions.getSolution(),
      _path = cdbFunctions.getSaikuPath(),
      saikuPath = webAppPath + "/content/saiku-ui/index.html?";
  function openSaiku(placeholder, solution, path, action, callback, mode) {
    var params = cdbFunctions.getPluginParams(solution, path, action, mode),
        paramString = $.param(params);

    /* the hash parameter starts a Backbone Router that opens the given query
     */
    var $iframe = $("<iframe>", {
          src: saikuPath + paramString + ( mode == "edit" ? "#query/open/" + params.action : ""),
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

  this.getName = function() {
    return _name;
  }
  this.getLabel = function() {
    return _label;
  }
  this.newQuery = function($ph, query, callback) {
    function cb(schema, cube, jndi, mdx){
      query.setDefinition({catalog: schema, cube: cube, jndi: jndi, query: mdx});
      callback(query);
    };
    openSaiku($ph, _solution, _path , query.getGroup() + "-" + query.getLabel() + ".saiku", cb, "new");
  };
  this.editQuery = function(placeholder,query, callback) {
    function cb(schema, cube, jndi, mdx){
      query.setDefinition({catalog: schema, cube: cube, jndi: jndi, query: mdx});
      callback(query);
    };
    openSaiku(placeholder, _solution, _path , query.getGUID() + ".saiku", cb, "edit");    
  };

  this.saveQuery = function(placeholder, queryObj, callback) {
    var filename = queryObj.getGUID() + '.saiku',
        solution = _solution,
        path = _path,
        overwrite = true,
        wnd = placeholder.find("iframe").get(0).contentWindow,
        query = wnd.Saiku.tabs._tabs[0].content.query,
        parser = new DOMParser(),
        doc;
    query.action.get("/xml", {
      success: function(model, response) {
        console.log('success');
        if (window.DOMParser) {
          parser=new DOMParser();
          doc = parser.parseFromString(response.xml,"text/xml");
        } else {
          doc=new ActiveXObject("Microsoft.XMLDOM");
          doc.async=false;
          doc.loadXML(text); 
        }
        queryObj.setDefinition({
          catalog: Dashboards.schemas[$('Query', doc).attr('catalog')].schema,
          cube: $('Query', doc).attr('cube'),
          jndi: Dashboards.schemas[$('Query', doc).attr('catalog')].jndi,
          query: $('MDX',doc).text()
        });
        var filePath = _solution +'/' + _path + '/' + filename;       
        (new wnd.SavedQuery({
          content: response.xml,
          file: filePath
        })).save({ success: function() {
          wnd.puc.refresh_repo();
          callback(queryObj);
        }});
      }
    });
  }
}
wd.cdb.connectors.ConnectorEngine.registerConnector(new SaikuConnector());
}());
