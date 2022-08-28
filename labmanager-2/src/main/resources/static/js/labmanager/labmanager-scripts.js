// Add the "format()" function to the String type
String.prototype.format = function () {
	// store arguments in an array
	var args = arguments;
	// use replace to iterate over the string
	// select the match and check if the related argument is present
	// if yes, replace the match with the argument
	return this.replace(/{([0-9]+)}/g, function (match, index) {
		// check if the argument is present
		return typeof args[index] == 'undefined' ? match : args[index];
	});
};

// Avoid "console" errors in browsers that lack a console.
(function() {
    var method;
    var noop = function () {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());
