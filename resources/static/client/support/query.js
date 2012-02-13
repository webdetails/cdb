/**
 * @constructor
 */
wd.ctools.cToolsSaikuHandler = (function() {
  var events = {
    save: {}
  };

  this.registerHandler = function(evt, label, handler) {
    if (!events[evt]) {
      throw new Error("No such event");
    }
    events[evt][label] = handler;
  };

  this.triggerEvent = function(evt,data) {
    var handlers;
    try {
      handlers = events[evt];
      for (handler in handlers) if (handlers.hasOwnProperty(handler)) {
        handlers[handler].call(this,data);
      }
    } catch (e) {
    }
  }
}());
