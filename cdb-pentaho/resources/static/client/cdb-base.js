var cdbFunctions = cdbFunctions || {};

cdbFunctions.loadGroupList = function(callback, myself) {
    $.getJSON('query?method=listGroups', {}, function(response) {
        var groups = response.result,
            i;
        for (i = 0; i < groups.length;i++) {
            if (groups[i].name)
                myself.newGroup(groups[i].name, groups[i].groupName);
        }

        if (callback && typeof callback == 'function') {
            callback(myself);
        }
    });
};

cdbFunctions.getPluginParams = function(solution, path, file, mode) {
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

cdbFunctions.getSolution = function() {
    return "cdb";
};

cdbFunctions.getSaikuPath = function() {
    return "saiku"
};

cdbFunctions.getPersistenceUrl = function() {
    return webAppPath + "/content/cdb/storage";
};

cdbFunctions.saveQuery = function(queries, label) {
    var q, query;
    for (q in queries) if (queries.hasOwnProperty(q)) {
      query = queries[q];
      wd.ctools.persistence.saveObject(null,"Query",query);
    }

    $.getJSON("connector?method=exportCda&group=" + label,function(){console.log("Saved to CDA")});
};