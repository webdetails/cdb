/**
 * @namespace global WebDetails namespace
 */

var wd = wd || {};

/**
 * @namespace Community Data Browser namespace
 */

wd.cdb = wd.cbd || {};

wd.cdb.groupIndex = 0;
wd.cdb.queryIndex = 0;


wd.cdb.cloneGroupNameInput = function(){
	var groupName = 'group'+wd.cdb.groupIndex;
	var objectPlaceHolderMap = {
		'dummyGroupName': (groupName+'Name') 
	};
	
	var paramMap = {
		'groupNameParam' : (groupName+'NameParam')
	};
	
	Dashboards.setParameter(groupName+'NameParam','Untitled Group'+wd.cdb.groupIndex);
	
	var clone = render_groupNameInput.clone(paramMap,{},objectPlaceHolderMap);
	clone.htmlObject = groupName+'Name';
	clone.name = 'render_'+groupName+'NameInput';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneSaveGroupButton = function(){
	var groupName = 'group'+wd.cdb.groupIndex;
	var objectPlaceHolderMap = {
		'dummyGroupSave': (groupName+'Save') 
	};
	
	var clone = render_saveGroupButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = groupName+'Save';
	clone.name = 'render_'+groupName+'Save';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneRemoveGroupButton = function(){
	var groupName = 'group'+wd.cdb.groupIndex;
	var objectPlaceHolderMap = {
		'dummyGroupRemove': (groupName+'Remove') 
	};
	
	var clone = render_removeGroupButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = groupName+'Remove';
	clone.name = 'render_'+groupName+'Remove';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneAddQueryButton = function(){
	var groupName = 'group'+wd.cdb.groupIndex;
	var objectPlaceHolderMap = {
		'dummyGroupAddQuery': (groupName+'AddQuery') 
	};
	
	var clone = render_newQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = groupName+'AddQuery';
	clone.name = 'render_'+groupName+'AddQuery';
	clone.expression = function(){ 
		wd.cdb.addQuery(groupName+'Queries');
	};
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.clonePasteQueryButton = function(){
	var groupName = 'group'+wd.cdb.groupIndex;
	var objectPlaceHolderMap = {
		'dummyGroupPasteQuery': (groupName+'PasteQuery') 
	};
	
	var clone = render_pasteQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = groupName+'PasteQuery';
	clone.name = 'render_'+groupName+'PasteQuery';
	clone.expression = function(){
		wd.cdb.pasteQuery(groupName+'Queries');
	};
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.createGroup = function(){
	var objectPlaceholder = $("#"+placeHolderName);
	
	var dummyGroup = $("#dummyGroup");
	
	var group = dummyGroup.clone();
	var groupName = 'group'+(++wd.cdb.groupIndex)
	group.attr('id',groupName);
	
	//header
	group.find('#dummyGroupName').attr('id',groupName+'Name');
	group.find('#dummyGroupSave').attr('id',groupName+'Save');
	group.find('#dummyGroupRemove').attr('id',groupName+'Remove');
	
	group.find('#dummyGroupQueries').attr('id',groupName+'Queries');
	
	group.find('#dummyGroupAddQuery').attr('id',groupName+'AddQuery');
	group.find('#dummyGroupPasteQuery').attr('id',groupName+'PasteQuery'); 
	
	objectPlaceholder.append(group);
	
	Dashboards.update(wd.cdb.cloneGroupNameInput());
	Dashboards.update(wd.cdb.cloneSaveGroupButton());	
	Dashboards.update(wd.cdb.cloneRemoveGroupButton());
	Dashboards.update(wd.cdb.cloneAddQueryButton());
	Dashboards.update(wd.cdb.clonePasteQueryButton());
	
	$('#'+groupName+'PasteQuery button').attr('disabled','disabled');
	
	group.animate({
	    height: 'toggle'
	  }, 250, function() {
	    // Animation complete.
	  });
	
};

wd.cdb.saveGroup = function(){
	console.log("Save Group");
};

wd.cdb.removeGroup = function(){
	console.log("Remove Group");
};	


wd.cdb.cloneQueryNameInput = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);
	
	var objectPlaceHolderMap = {
		'dummyQueryName': (queryName+'Name') 
	};
	
	var paramMap = {
		'queryNameParam' : (queryName+'NameParam')
	};
	
	Dashboards.setParameter(queryName+'NameParam','Insert Query Name...');
	
	var clone = render_queryNameInput.clone(paramMap,{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'Name';
	clone.name = 'render_'+queryName+'NameInput';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneQueryTypeSelector = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);
	
	var objectPlaceHolderMap = {
		'dummyQueryType': (queryName+'Type') 
	};
	
	var paramMap = {
		'queryTypeParam' : (queryName+'TypeParam')
	};
	
	Dashboards.setParameter(queryName+'TypeParam','Saiku');
	
	var clone = render_queryTypeSelector.clone(paramMap,{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'Type';
	clone.name = 'render_'+queryName+'TypeSelector';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};


wd.cdb.cloneCopyButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyCopy': (queryName+'CopyButton') 
	};
	
	var clone = render_copyQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'CopyButton';
	clone.name = 'render_'+queryName+'CopyButton';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneExportButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyExport': (queryName+'ExportButton') 
	};
	
	var clone = render_exportQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'ExportButton';
	clone.name = 'render_'+queryName+'ExportButton';
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneOkButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyOkButton': (queryName+'OkButton') 
	};
	
	var clone = render_okQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'OkButton';
	clone.name = 'render_'+queryName+'OkButton';
	
	clone.expression = function(){
		$('#editionEnvironment').animate({
		    height: '0px'
		  }, 500, function() {
		    $('#editionEnvironment').hide();
		  }
		);
		
		$("#"+queryName+'OkButton').hide();
		$("#"+queryName+'CancelButton').hide();
		$("#"+queryName+"CopyButton").css('margin','4px 1px 4px 8px');
		$("#"+queryName+"ExportButton").css('margin','4px 4px 4px 1px');

		//save changes
		
		$("#"+queryName+'ActiveTypeButton button').removeAttr('disabled').width('487px');
		wd.cdb.setQueryState(queryName,'closed');
	}
	
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};

wd.cdb.cloneCancelButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyCancelButton': (queryName+'CancelButton') 
	};
	
	var clone = render_cancelQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'CancelButton';
	clone.name = 'render_'+queryName+'CancelButton';
	
	clone.expression = function(){
		if(wd.cdb.getQueryState(queryName) == 'edition no-edited'){
			wd.cdb.setQueryState(queryName,'new');
			$("#"+queryName+'OkButton').hide();
			$("#"+queryName+'CancelButton').hide();
			$("#"+queryName+'CopyButton').hide();
			$("#"+queryName+'ExportButton').hide();
			$("#"+queryName+'ProceedButton').show();
			$("#"+queryName+'Type').show();
			$("#"+queryName+'ActiveTypeButton').hide();
			 
			
		} else if(wd.cdb.getQueryState(queryName) == 'edition edited'){
			wd.cdb.setQueryState(queryName,'closed');
			//cancel changes
			$("#"+queryName+'OkButton').hide();
			$("#"+queryName+'CancelButton').hide();
			$("#"+queryName+'CopyButton').show();
			$("#"+queryName+'ExportButton').show();
			$("#"+queryName+'ProceedButton').hide();
			$("#"+queryName+'Type').hide();
			$("#"+queryName+'ActiveTypeButton').show();
			
			$("#"+queryName+"CopyButton").css('margin','4px 1px 4px 8px');
			$("#"+queryName+"ExportButton").css('margin','4px 4px 4px 1px');
			
			//save changes
					
			$("#"+queryName+'ActiveTypeButton button').removeAttr('disabled').width('487px');
		}
		
		$('#editionEnvironment').animate({
		    height: '0px'
		  }, 500, function() {
		    $('#editionEnvironment').hide();
		  }
		);
	
		
	}
	
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	
	
	return clone;
};


wd.cdb.cloneProceedButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyProceedButton': (queryName+'ProceedButton') 
	};
	
	var clone = render_proceedQueryButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'ProceedButton';
	clone.name = 'render_'+queryName+'ProceedButton';
	
	clone.expression = function(){
		if(wd.cdb.getQueryState(queryName) == 'new'){
			$('#editionEnvironment').css('top',$('#'+queryName).position().top+$('#'+queryName).height()+6+'px');
			$('#editionEnvironment').show().animate({
			    height: '300px'
			  }, 500, function() {
			  	wd.cdb.setQueryState(queryName,'edition no-edited');
			  	$("#"+queryName+'OkButton').show();
			  	$("#"+queryName+'CancelButton').show();
			  	$("#"+queryName+'CopyButton').show();
			  	$("#"+queryName+'ExportButton').show();
			  	$("#"+queryName+'ProceedButton').hide();
			  	$("#"+queryName+'Type').hide();
			  	$("#"+queryName+'ActiveTypeButton').show();
			  	
			  	$("#"+queryName+'ActiveTypeButton button').text(wd.cdb.getSelectedQueryType(queryName)).attr('disabled','disabled');
			});
		}
	}
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};


wd.cdb.cloneActiveTypeButton = function(){
	var queryName = 'query'+wd.cdb.groupIndex+''+(wd.cdb.queryIndex);

	var objectPlaceHolderMap = {
		'dummyQueryActiveTypeButton': (queryName+'ActiveTypeButton') 
	};
	
	var clone = render_activeTypeButton.clone({},{},objectPlaceHolderMap);
	clone.htmlObject = queryName+'ActiveTypeButton';
	clone.name = 'render_'+queryName+'ActiveTypeButton';
	
	clone.expression = function(){
		if(wd.cdb.getQueryState(queryName) == 'closed'){
			$('#editionEnvironment').css('top',$('#'+queryName).position().top+$('#'+queryName).height()+6+'px');
			$('#editionEnvironment').show().animate({
			    height: '300px'
			  }, 500, function() {
			  	wd.cdb.setQueryState(queryName,'edition edited');
			  	$("#"+queryName+'OkButton').show();
			  	$("#"+queryName+'CancelButton').show();
			  	$("#"+queryName+'CopyButton').show();
			  	$("#"+queryName+'ExportButton').show();
			  	$("#"+queryName+'ProceedButton').hide();
			  	$("#"+queryName+'Type').hide();
			  	$("#"+queryName+'ActiveTypeButton').show();
			  	
			  	$("#"+queryName+"CopyButton").css('margin','4px 1px 4px 8px');
			  	$("#"+queryName+"ExportButton").css('margin','4px 8px 4px 1px');
			  	
			  	$("#"+queryName+'ActiveTypeButton button').attr('disabled','disabled').css('width','295px');
			});
		}
	}
	
	
	Dashboards.addComponents([clone]);
	window[clone.name] = clone;
	
	return clone;
};



wd.cdb.addQuery = function(holderName){
	var holder = $("#"+holderName);
	
	if(holder.children().length == 0){
		holder.height('37px');
	} else {
		holder.height(parseInt(holder.height()+37)+'px');	
	}
	
	var dummyQuery = $("#dummyQuery");
	var query = dummyQuery.clone();
	query.css('maxWidth','950px');
	var queryName = 'query'+wd.cdb.groupIndex+''+(++wd.cdb.queryIndex);
	query.attr('id',queryName);
	
	query.find("#dummyQueryName").attr('id',queryName+'Name').css('margin','4px 8px 4px 4px');
	query.find("#dummyQueryType").attr('id',queryName+'Type').css('margin','4px 4px 4px 8px');
	query.find("#dummyCopyButton").attr('id',queryName+'CopyButton').css('margin','4px 1px 4px 8px').hide();
	query.find("#dummyExportButton").attr('id',queryName+'ExportButton').css('margin','4px 8px 4px 1px').hide();
	query.find("#dummyProceedButton").attr('id',queryName+'ProceedButton').css('margin','4px 4px 4px 295px');
	query.find("#dummyOkButton").attr('id',queryName+'OkButton').css('margin','4px 4px 4px 1px').hide();
	query.find("#dummyCancelButton").attr('id',queryName+'CancelButton').css('margin','4px 1px 4px 4px').hide();
	query.find("#dummyQueryActiveTypeButton").attr('id',queryName+'ActiveTypeButton').css('margin','4px 8px').hide();

	
	
	holder.append(query);
	
	wd.cdb.setQueryState(queryName,'new');
	
	Dashboards.update(wd.cdb.cloneQueryNameInput());
	Dashboards.update(wd.cdb.cloneQueryTypeSelector());
	Dashboards.update(wd.cdb.cloneCopyButton());
	Dashboards.update(wd.cdb.cloneExportButton());	
	Dashboards.update(wd.cdb.cloneProceedButton());
	Dashboards.update(wd.cdb.cloneOkButton());
	Dashboards.update(wd.cdb.cloneCancelButton());
	Dashboards.update(wd.cdb.cloneActiveTypeButton());
	
	$("#"+queryName+"ActiveTypeButton button").css('width','295px');
	
	query.animate({
	    width: 'toggle'
	  }, 500, function() {
	    // Animation complete.
	  });
	  
	
	console.log("Add Query");
};

wd.cdb.pasteQuery = function(queryHolder){
	console.log("Paste Query");
};


wd.cdb.getQueryState = function(queryName){
	return $("#"+queryName).attr('state');
}

wd.cdb.setQueryState = function(queryName, state){
	$("#"+queryName).attr('state',state);
}

wd.cdb.getSelectedQueryType = function(queryName){
	var name = "";
	var component = Dashboards.getComponentByName('render_'+queryName+'TypeSelector');
	
	return Dashboards.getParameterValue(component.parameter);
}