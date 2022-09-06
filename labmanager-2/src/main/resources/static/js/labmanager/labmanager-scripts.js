/** Add the "format()" function to the String type.
 */
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

/** Avoid "console" errors in browsers that lack a console.
 */
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

/** Add a click handler for opening an obfuscated email.
 * @param element the jQuery selector for the element to attach to the click event.
 */
function addObfuscatedEmailHandler(element) {
	$(element).css('cursor', 'pointer');
	$(document).on('click', element, (event) => {
		var email = event.target.dataset.obfd + event.target.dataset.obfc + '@' + event.target.dataset.obfb + '.' + event.target.dataset.obfa;
		location.href = 'mailto:' + email;
	});
}

/** Add a click handler for opening an obfuscated phone.
 * @param element the jQuery selector for the element to attach to the click event.
 */
function addObfuscatedPhoneHandler(element) {
	$(element).css('cursor', 'pointer');
	$(document).on('click', element, (event) => {
		var phone = event.target.dataset.obfa + event.target.dataset.obfb + event.target.dataset.obfc + event.target.dataset.obfd;
		location.href = 'tel:' + phone;
	});
}

/** Generate a file with Javascript and force it to be downloaded by the browser.
 * Credits: {@link "https://stackoverflow.com/users/2438165/mat%c4%9bj-pokorn%c3%bd"Matěj Pokorný} and
 * {@link "https://stackoverflow.com/users/476716/orangedog" OrangeDog}.
 * @param filename the filename of the file to be downloaded.
 * @param text the content of the file.
 * @param mimeType the MIME type of the content. Default is `text/plain`.
 */
function download(filename, text, mimeType) {
	(!mimeType) && (mimeType = 'text/plain');
    var pom = document.createElement('a');
    pom.setAttribute('href', 'data:' + mimeType + ';charset=utf-8,' + encodeURIComponent(text));
    pom.setAttribute('download', filename);
    if (document.createEvent) {
        var event = document.createEvent('MouseEvents');
        event.initEvent('click', true, true);
        pom.dispatchEvent(event);
    }
    else {
        pom.click();
    }
}
