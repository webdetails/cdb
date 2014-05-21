var cdbFunctions = cdbFunctions || {};

cdbFunctions.loadGroupList = function(callback, myself) {
    $.getJSON('query/listGroups', {}, function(response) {
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
		biplugin5: true,
		biplugin: false,
		},
		(mode == "edit" ? {
			action: '/' + solution + '/' + path + '/' + file,
			dimension_prefetch: false,
			mode: "edit"
		} : {} ));
};

cdbFunctions.getSolution = function() {
    return "public";
};

cdbFunctions.getSaikuPath = function() {
	return "cdb/saiku";
};

cdbFunctions.getPersistenceUrl = function() {
    return webAppPath + "/plugin/cdb/api/storage";
};

cdbFunctions.saveQuery = function(queries, label) {
    var q, query;
    for (q in queries) if (queries.hasOwnProperty(q)) {
      query = queries[q];
      wd.ctools.persistence.saveObject(null,"Query",query);
    }

    $.getJSON("connector/exportCda?group=" + label,function(){ console.log("Saved to CDA") });
};