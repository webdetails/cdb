/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/
/**
 * @namespace
 */

wd.cdb.exporters = wd.cdb.exporters || {};

/**
 * @constructor
 */
wd.cdb.exporters.ExporterEngine = (function() {
  var myself = {};
  var _exporters = {};
  myself.registerExporter = function(exporter) {
    _exporters[exporter.getLabel()] = exporter;
  };
  myself.getExporter = function(type) {
    return _exporters[type];
  };
  myself.listExporters = function() {
    return _exporters;
  };

   myself.exportQuery = function(exporter,query,callback) {
    myself.getExporter(exporter).exportQuery(query,callback);
  }

  return myself; 

})();

