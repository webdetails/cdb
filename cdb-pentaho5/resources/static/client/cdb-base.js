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

cdbFunctions.getPluginParam = function() {
	return { biplugin5: true };
};
