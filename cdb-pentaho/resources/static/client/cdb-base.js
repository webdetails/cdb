var cdbFunctions = cdbFunctions || {};

cdbFunctions.loadGroupList = function( myself, callback ) {
    var url = "query",
        params = {
            method: "listGroups"
        }
    $.getJSON( url, params, function(response) {
        var groups = response.object,
            i;

        for (i = 0; i < groups.length;i++) if (groups[i].group) {
            myself.newGroup(groups[i].group, groups[i].name);
        }

        if (callback && typeof callback == 'function') {
            callback(myself);
        }

    });
};

cdbFunctions.loadGroup = function( myself, group, callback ) {
    var url = "query",
        params = {
            method: "loadGroup",
            group: group.getLabel()
        };

    $.getJSON( url, params, function(response){
        if(response) {
            var query, q, queryData;
            myself.addGroup(group);
            for (q in response.object) if (response.object.hasOwnProperty(q)) {
                queryData = response.object[q];
                query = new wd.cdb.Query(queryData.name, queryData.group, queryData.type, queryData.guid, queryData.definition);
                query.setKey(queryData["@rid"]);
                query.setGroupName(group.getDescription());
                group.addQuery(query);
            }
            callback(group);
        }
    });
};

cdbFunctions.saveQuery = function( queries, label ) {
    var q, query,
        url = "connector",
        params = {
            method: "exportCda",
            group: label
        };

    for (q in queries) if (queries.hasOwnProperty(q)) {
      query = queries[q];
      wd.ctools.persistence.saveObject(null,"Query",query);
    }

    $.getJSON( url, params, function(){
        console.log("Saved to CDA")
    });
};

cdbFunctions.getPluginParams = function( solution, path, file, mode ) {
    return $.extend({
        biplugin5: false,
        biplugin: true,
        },
        (mode == "edit" ? {
            solution: solution,
            path: path,
            action: file,
            dimension_prefetch: false,
            mode: "edit"
        } : {} ));
};

cdbFunctions.copyBackend = function(myself, newGuid) {
    var url = webAppPath + "/content/cdb/connector",
        params = {
            method: 'copyQuery',
            id: myself.getKey(),
            newguid: newGuid
        };

      $.getJSON(url, $.param(params),function(response){
        wd.ctools.persistence.saveObject(null, "Query", myself);
      });
};

cdbFunctions.deleteSelf = function(myself) {
    var url = webAppPath + "/content/cdb/connector",
        params = {
            method: 'deleteQuery',
            id: myself.getKey()
        };
    $.getJSON(url, $.param(params),function(response){
      wd.ctools.persistence.deleteObject(myself.getKey(), "Query");
    });
};

cdbFunctions.getSolution = function() {
    return "cdb";
};

cdbFunctions.getSaikuPath = function() {
    return "saiku"
};

cdbFunctions.getPersistenceUrl = function() {
    return webAppPath + "/content/cdb/storage";
};
