/**
 * @namespace global WebDetails namespace
 */

var wd = wd || {};

/**
 * @namespace Community Data Browser namespace
 */

wd.cdb = wd.cbd || {};

wd.cdb.groupIndex = 1;
wd.cdb.queryIndex = 0;

wd.cdb.queryClipboard = undefined;

Dashboards.storage.groupsQueries = {}; 


wd.cdb.cloneGroupNameInput = function(name, description){
  var groupName = name;
  var objectPlaceHolderMap = {
    'dummyGroupName': (groupName+'Name') 
  };
  
  var paramMap = {
    'groupNameParam' : (groupName+'NameParam')
  };
  

  Dashboards.setParameter(groupName+'NameParam',  description); 
  
  var clone = render_groupNameInput.clone(paramMap,{},objectPlaceHolderMap);
  clone.htmlObject = groupName+'Name';
  clone.name = 'render_'+groupName+'NameInput';
  clone.postChange = function() {
    var affectedGroup = wd.cdb.QueryManager.getGroup(groupName);
    affectedGroup.setDescription(Dashboards.getParameterValue(this.parameter));
    affectedGroup.save();
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

/*
wd.cdb.cloneSaveGroupButton = function(groupName){
  var objectPlaceHolderMap = {
    'dummyGroupSave': (groupName+'Save') 
  };
  
  var clone = render_saveGroupButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = groupName+'Save';
        clone.expression = function() {
          wd.cdb.saveGroup(groupName);
        };
  clone.name = 'render_'+groupName+'Save';
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};
*/

wd.cdb.cloneRemoveGroupButton = function(groupName){
  var objectPlaceHolderMap = {
    'dummyGroupRemove': (groupName+'Remove') 
  };
  
  var clone = render_removeGroupButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = groupName+'Remove';
  clone.name = 'render_'+groupName+'Remove';

  Dashboards.addComponents([clone]);
  window[clone.name] = clone;

  var originalExpr = clone.expression;

  clone.expression = function() {
    var queriesOriginal = wd.cdb.QueryManager.getGroup(groupName).listQueries();
    var queries = [];
    for (q in queriesOriginal) 
      if (queriesOriginal.hasOwnProperty(q)) {
        query = queriesOriginal[q];
	    queries.push([query.getGUID(), query.getLabel()]);
      }
    render_queriesToRemove.valuesArray = queries;
    render_confirmSelectionButton.groupName = groupName;
    Dashboards.update(render_queriesToRemove);
    Dashboards.update(render_removeGroupOrQueries);
    originalExpr.apply(this,arguments);
  };

  render_confirmSelectionButton.groupName = groupName;
  return clone;
};

wd.cdb.cloneAddQueryButton = function(groupName){
  var objectPlaceHolderMap = {
    'dummyGroupAddQuery': (groupName+'AddQuery') 
  };
  
  var clone = render_newQueryButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = groupName+'AddQuery';
  clone.name = 'render_' + groupName + 'AddQuery';
  clone.expression = function(){ 
    wd.cdb.addQuery(groupName);
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

wd.cdb.clonePasteQueryButton = function(groupName){
  var objectPlaceHolderMap = {
    'dummyGroupPasteQuery': (groupName+'PasteQuery') 
  };
  
  var clone = render_pasteQueryButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = groupName+'PasteQuery';
  clone.name = 'render_'+groupName+'PasteQuery';
  clone.expression = function(){
    wd.cdb.pasteQuery(groupName);
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

wd.cdb.createGroup = function(){
  var groupName = 'group'+(wd.cdb.groupIndex);
  while (wd.cdb.QueryManager.getGroup(groupName)) {
  	groupName = 'group'+(++wd.cdb.groupIndex);  
  }
  
  var group = wd.cdb.QueryManager.newGroup(groupName, 'Untitled Group');
  wd.cdb.showGroup(group);
  
};


wd.cdb.saveGroup = function(groupName){
  wd.cdb.QueryManager.getGroup(groupName).save();
};


wd.cdb.showGroup = function(newGroup) {
  var objectPlaceholder = $("#"+placeHolderName),
      dummyGroup = $("#dummyGroup"),
      groupName = newGroup.getLabel(),
      groupDescription = newGroup.getDescription(),
      group = dummyGroup.clone();
  
	
  group.attr('id',groupName);
  
  //header
  group.find('#dummyGroupName').attr('id',groupName+'Name').click(function(){return false;});
  group.find('#dummyGroupSave').attr('id',groupName+'Save');
  group.find('#dummyGroupRemove').attr('id',groupName+'Remove');
  
  group.find('#dummyGroupQueries').attr('id',groupName+'Queries');
  
  group.find('#dummyGroupAddQuery').attr('id',groupName+'AddQuery');
  group.find('#dummyGroupPasteQuery').attr('id',groupName+'PasteQuery'); 
  
  objectPlaceholder.append(group);
  
  wd.cdb.createGroupElement(groupName,groupDescription);
  
  Dashboards.update(wd.cdb.cloneGroupNameInput(groupName, groupDescription));
//  Dashboards.update(wd.cdb.cloneSaveGroupButton(groupName));  
  Dashboards.update(wd.cdb.cloneRemoveGroupButton(groupName));
  Dashboards.update(wd.cdb.cloneAddQueryButton(groupName));
  Dashboards.update(wd.cdb.clonePasteQueryButton(groupName));
  
  if(wd.cdb.queryClipboard == undefined)
  $('#'+groupName+'PasteQuery button').hide();
  
  group.animate({
      height: 'toggle'
    }, 250, function() {
      // Animation complete.
    });
  var queries = newGroup.listQueries();
  for(var q in queries) if (queries.hasOwnProperty(q)){
    wd.cdb.addQuery(groupName,queries[q]);
  }

  group.find(".WDdataCellHeader").click(function(){
    $(this).toggleClass('collapsed');
    group.find(".groupContent").animate({
      height: 'toggle'
    }, 250, function() {
      // Animation complete.
    });
  });
  wd.cdb.groupIndex++;
  
};

wd.cdb.cloneQueryNameInput = function(groupName, queryObj){
  var queryGuid = queryObj.getGUID(),
      guidParam = 'param' + queryGuid.replace(/-/g, '_'),
      objectPlaceHolderMap = {
    'dummyQueryName': (queryGuid+'Name') 
  };
  
  var paramMap = {
    'queryNameParam' : (guidParam +'NameParam')
  };
  
  Dashboards.setParameter(guidParam + 'NameParam',queryObj.getLabel());
  
  var clone = render_queryNameInput.clone(paramMap,{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid + 'Name';
  clone.name = 'render_'+guidParam+'NameInput';
  clone.postChange = function(value){
    queryObj.setLabel(value, function() {
      wd.cdb.QueryManager.getGroup(groupName).save();
    });
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

wd.cdb.cloneQueryTypeSelector = function(groupName, queryObj){
    var queryGuid = queryObj.getGUID(),
        guidParam = 'param' + queryGuid.replace(/-/g, '_'),
        objectPlaceHolderMap = {
    'dummyQueryType': (queryGuid+'Type') 
  };
  
  var paramMap = {
    'queryTypeParam' : (guidParam+'TypeParam')
  };
  
  Dashboards.setParameter(guidParam+'TypeParam',queryObj.getType());
  
  var clone = render_queryTypeSelector.clone(paramMap,{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid+'Type';
  clone.name = 'render_'+guidParam+'TypeSelector';
  clone.postChange = function(value){
                queryObj.setType(value);
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};


wd.cdb.cloneCopyButton = function(groupName, queryObj){
  var queryGuid = queryObj.getGUID(),
      guidParam = queryGuid.replace(/-/g, '_'),
      objectPlaceHolderMap = {
        'dummyCopy': (queryGuid+'CopyButton') 
      };
  
  var clone = render_copyQueryButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid+'CopyButton';
  clone.name = 'render_'+guidParam+'CopyButton';
  clone.expression = function(){
    var storage = Dashboards.storage.groupsQueries;
    wd.cdb.queryClipboard = queryObj;
    $.each($.find('.pasteQueryButton button'),
      function(){
        $(this).show();
      });

    $.each($.find('.queryCopyButton button'),function(){$(this).css('background-color','transparent');});
    $(this).css('background-color','#CAD3D6');
  };
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

wd.cdb.cloneExportButton = function(groupName, queryObj){
  var queryGuid = queryObj.getGUID(),
      guidParam = queryGuid.replace(/-/g, '_'),
      objectPlaceHolderMap = {
        'dummyExport': (queryGuid+'ExportButton') 
      };
  
  var clone = render_exportQueryButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid+'ExportButton';
  clone.name = 'render_'+guidParam+'ExportButton';
        clone.queryObj = queryObj;
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};


wd.cdb.cloneProceedButton = function(groupName, queryObj,isNew){
  var queryGuid = queryObj.getGUID(),
      guidParam = queryGuid.replace(/-/g, '_'),
      objectPlaceHolderMap = {
        'dummyProceedButton': (queryGuid+'ProceedButton') 
      };
  
  var clone = render_proceedQueryButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid+'ProceedButton';
  clone.name = 'render_'+guidParam+'ProceedButton';


  clone.expression = function(){
    wd.cdb.showQueryEditor(groupName,queryObj,function(){
      wd.cdb.setEditMode(queryObj);
    },isNew);
  } 
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};


wd.cdb.cloneActiveTypeButton = function(groupName, queryObj){
  var queryGuid = queryObj.getGUID(),
      guidParam = queryGuid.replace(/-/g, '_'),
      objectPlaceHolderMap = {
        'dummyQueryActiveTypeButton': (queryGuid+'ActiveTypeButton') 
      };
  
  var clone = render_activeTypeButton.clone({},{},objectPlaceHolderMap);
  clone.htmlObject = queryGuid+'ActiveTypeButton';
  clone.name = 'render_'+guidParam+'ActiveTypeButton';
  
  clone.expression = function(){
          wd.cdb.showQueryEditor(groupName,queryObj,function(){wd.cdb.setEditMode(queryObj)});
  }
  
  
  Dashboards.addComponents([clone]);
  window[clone.name] = clone;
  
  return clone;
};

wd.cdb.setClosedMode = function(queryObj) {
  var queryGuid = queryObj.getGUID();
  wd.cdb.setQueryState(queryObj,'closed');
  $("#"+queryGuid+'OkButton').hide();
  $("#"+queryGuid+'CancelButton').hide();
  $("#"+queryGuid+'CopyButton').show();
  $("#"+queryGuid+'ExportButton').show();
  $("#"+queryGuid+'ProceedButton').hide();
  $("#"+queryGuid+'Type').hide();
  $("#"+queryGuid+'ActiveTypeButton').show();
  
  $("#"+queryGuid+"CopyButton").css('margin','4px 1px 4px 8px');
  $("#"+queryGuid+"ExportButton").css('margin','4px 4px 4px 1px');
  
  $("#"+queryGuid+'ActiveTypeButton button').removeAttr('disabled').css('width','487px');
};

wd.cdb.setNewMode = function (queryObj) {
  var queryGuid = queryObj.getGUID();
  wd.cdb.setQueryState(queryObj,'closed');
  $("#"+queryGuid+'OkButton').hide();
  $("#"+queryGuid+'CancelButton').hide();
  $("#"+queryGuid+'CopyButton').hide();
  $("#"+queryGuid+'ExportButton').hide();
  $("#"+queryGuid+'ProceedButton').show();
  $("#"+queryGuid+'Type').show();
  $("#"+queryGuid+'ActiveTypeButton').hide();
};

wd.cdb.setEditMode = function(queryObj) {
  var queryGuid = queryObj.getGUID();
  wd.cdb.setQueryState(queryObj,'edition edited');
  $("#"+queryGuid+'OkButton').show();
  $("#"+queryGuid+'CancelButton').show().css('margin-left','200px');
  $("#"+queryGuid+'CopyButton').hide();
  $("#"+queryGuid+'ExportButton').hide();
  $("#"+queryGuid+'ProceedButton').hide();
  $("#"+queryGuid+'Type').hide();
  $("#"+queryGuid+'ActiveTypeButton').show();
  
  $("#"+queryGuid+"CopyButton").css('margin','4px 1px 4px 8px');
  $("#"+queryGuid+"ExportButton").css('margin','4px 8px 4px 1px');
  
  $("#"+queryGuid+'ActiveTypeButton button').attr('disabled','disabled').css('width','295px');
  
};

wd.cdb.showQueryEditor = function(groupName,queryObj,callback,isNew) {
  var queryGuid = queryObj.getGUID();
  var queryState = wd.cdb.getQueryState(queryGuid).replace(/ /g, '');

  /*
   * First thing we need to do is find the currently active editor,
   * and trigger a cancel so the query being edited has its row cleaned
   * up nicely.
   */
  if( queryState === 'closed' || queryState === 'new'){
     var env = $('#editionEnvironment'),
        popup = $('#editionPopup');
    popup.stop();
    popup.find(".title").text(queryObj.getLabel());
    var connector = wd.cdb.connectors.ConnectorEngine.getConnector(queryObj.getType());
    if (isNew) {
      connector.newQuery(env,queryObj,function(){
        console.log('done!');
      });
    } else {
      connector.editQuery(env,queryObj,function(){
        console.log('done!');
      });
    }
    popup.show().animate({height: '100%'}, 500, callback);
    $("#body").hide();
  }

  render_cancelQueryButton.expression = function(){
    if(!queryObj.getDefinition()) {
      wd.cdb.QueryManager.getGroup(groupName).deleteQueryByObj(queryObj);
      $("#" + queryGuid).remove();
    } else if(wd.cdb.getQueryState(queryGuid) == 'edition no-edited'){
      wd.cdb.setQueryState(queryObj,'new');
      wd.cdb.setNewMode(queryObj);
      
    } else if(wd.cdb.getQueryState(queryGuid) == 'edition edited'){
      wd.cdb.setQueryState(queryObj,'closed');
      wd.cdb.setClosedMode(queryObj);
      //cancel changes
          
      $("#"+queryGuid+'ActiveTypeButton button').removeAttr('disabled').width('487px');
    }
    
    $("#body").show();
    $('#editionPopup').animate({
        height: '0px'
      }, 500, function() {
        $('#editionPopup').hide();
      }
    );
  };

  render_okQueryButton.expression = function(){
    var connector = wd.cdb.connectors.ConnectorEngine.getConnector(queryObj.getType()),
        $ph =  $('#editionPopup');
    connector.saveQuery($('#editionEnvironment'),queryObj,function(){
      $("#body").show();
      $ph.animate({ height: '0px'}, 500, function() {
        $ph.hide();
      });
      wd.cdb.saveGroup(groupName);
    });
    wd.cdb.setQueryState(queryObj,'closed');
      wd.cdb.setClosedMode(queryObj);
  };

};

wd.cdb.addQuery = function(groupName,queryObj){
  var isNew = false;
  if (!queryObj) {
    isNew = true;
    queryObj = new wd.cdb.Query("Untitled Query", groupName);
    queryObj.setGroupName(wd.cdb.QueryManager.getGroup(groupName).getDescription());
  }
  var holder = $("#"+groupName+'Queries');
  
  if(holder.children().length == 0){
    holder.height('36px');
  } else {
    holder.height(parseInt(holder.height()+36)+'px'); 
  }
  
  var dummyQuery = $("#dummyQuery");
  var query = dummyQuery.clone();
  query.css('maxWidth','950px');
  var queryGuid = queryObj.getGUID();
  query.attr('id',queryGuid);
  
  query.find("#dummyQueryName").attr('id',queryGuid+'Name').css('margin','4px 8px 4px 4px');
  query.find("#dummyQueryType").attr('id',queryGuid+'Type').css('margin','4px 4px 4px 8px');
  query.find("#dummyCopyButton").attr('id',queryGuid+'CopyButton').css('margin','4px 1px 4px 8px').hide();
  query.find("#dummyExportButton").attr('id',queryGuid+'ExportButton').css('margin','4px 8px 4px 1px').hide();
  query.find("#dummyProceedButton").attr('id',queryGuid+'ProceedButton').css('margin','4px 4px 4px 295px');
  query.find("#dummyOkButton").attr('id',queryGuid+'OkButton').css('margin','4px 4px 4px 1px').hide();
  query.find("#dummyCancelButton").attr('id',queryGuid+'CancelButton').css('margin','4px 1px 4px 4px').hide();
  query.find("#dummyQueryActiveTypeButton").attr('id',queryGuid+'ActiveTypeButton').css('margin','4px 8px').hide();

  wd.cdb.createQueryElement(groupName,queryGuid);
  
  holder.append(query);
  

        Dashboards.update(wd.cdb.cloneQueryNameInput(groupName, queryObj));
  Dashboards.update(wd.cdb.cloneQueryTypeSelector(groupName, queryObj));

  Dashboards.update(wd.cdb.cloneActiveTypeButton(groupName, queryObj));
  Dashboards.update(wd.cdb.cloneCopyButton(groupName, queryObj));
  Dashboards.update(wd.cdb.cloneExportButton(groupName, queryObj)); 
  Dashboards.update(wd.cdb.cloneProceedButton(groupName, queryObj,isNew));
  
  $("#"+queryGuid+"ActiveTypeButton button").css('width','295px');

  if(isNew) {
    wd.cdb.setNewMode(queryObj);
  } else {
    wd.cdb.setClosedMode(queryObj);
  }
  query.animate({
      width: 'toggle'
    }, 500, function() {
      // Animation complete.
    });
};

wd.cdb.pasteQuery = function(groupName){
	var group = wd.cdb.QueryManager.getGroup(groupName);
  	wd.cdb.addQuery(groupName,wd.cdb.queryClipboard.duplicate(groupName, group.getDescription()));
  	group.save();
};


wd.cdb.getQueryState = function(queryGuid){
  return $("#"+queryGuid).attr('state');
};

wd.cdb.setQueryState = function(queryObj, state){
  var queryGuid = queryObj.getGUID();
  $("#"+queryGuid).attr('state',state);
};

wd.cdb.getSelectedQueryType = function(queryName){
  var component = Dashboards.getComponentByName('render_'+queryName+'TypeSelector');
  
  return Dashboards.getParameterValue(component.parameter);
};

wd.cdb.getIndexFromGroupName = function(groupName){
  return parseInt(groupName.substr(5));
};

wd.cdb.createGroupElement = function(groupName, name){
/*  var storage = Dashboards.storage.groupsQueries;
  storage[groupName] = {
    'name': name,
    'queries': {}
  }; 
  
  Dashboards.setParameter('Dashboards.storage.groupsQueries',storage);
  Dashboards.saveStorage();
  */
};

wd.cdb.createQueryElement = function(groupName, queryName){
/*  var storage = Dashboards.storage.groupsQueries;
  storage[groupName].queries[queryName] = {
    'name': 'none',
    'type': 'none',
    'body': 'none'
  };
  
  Dashboards.setParameter('Dashboards.storage.groupsQueries',storage);
  Dashboards.saveStorage();
  */
};


wd.cdb.clickBox = function(event){
  event.stopPropagation();
};  

wd.cdb.clickDocRemoveEnv = function(){
    var $removeEnv = $("#removeEnvironment"); 
  $removeEnv.animate({
      height: 'toggle'
  }, 400, function(){
      $(document).unbind("click",wd.cdb.clickDocRemoveEnv);
      $removeEnv.unbind("click", wd.cdb.clickBox);
  });
};

wd.cdb.clickDocOpenEnv = function(){
    var $openEnv = $("#openEnvironment"); 
  $openEnv.animate({
      height: 'toggle'
  }, 400, function(){
      $(document).unbind("click",wd.cdb.clickDocOpenEnv);
      $openEnv.unbind("click", wd.cdb.clickBox);
  });
};

wd.cdb.clickDocExportEnv = function(){
  var $exportEnv = $("#exportEnvironment"); 
  $exportEnv.animate({
      height: 'toggle'
  }, 400, function(){
      $(document).unbind("click",wd.cdb.clickDocExportEnv);
      $exportEnv.unbind("click", wd.cdb.clickBox);
  });
};

wd.cdb.openGroups = function(list){
  for(var i = 0; i < list.length; i++){
    wd.cdb.QueryManager.loadGroup(list[i],function(group){
      wd.cdb.showGroup(group);
    });
  }
};

wd.cdb.removeQueries = function(groupName, list){
  var group = wd.cdb.QueryManager.getGroup(groupName);
  for(var i = 0; i < list.length; i++){
    var queryName = list[i];
    Dashboards.log("Removing query "+ queryName + " from group " + groupName);
    var queryGuid = group.getQuery(queryName).getGUID();
    group.deleteQuery(queryName);
    $("#" + groupName).find("#" + queryGuid).remove();
  }
};

wd.cdb.removeGroup = function(name){
  Dashboards.log("Removing Group " + name);
  wd.cdb.QueryManager.deleteGroup(name);
  $("#" + name).remove();
};


wd.cdb.queryElement = {};
