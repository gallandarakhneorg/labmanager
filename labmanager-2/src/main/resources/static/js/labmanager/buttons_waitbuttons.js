function setButtonWaitState($bt, label) {
	$bt = $bt.prop("disabled", true);
	var $span = $('<span></span>').attr('class', 'spinner-border spinner-border-sm');
	$span = $span.attr('role', 'status');
	$span = $span.attr('aria-hidden', 'true');
	$bt.empty();
	$bt.append($span);
	$bt.append("&nbsp;");
	$bt.append(label);
}

function setButtonStandardState($bt, label) {
	$bt.empty();
	$bt.append(label);
	$bt.prop("disabled", false);
}

function addWaitButtonCallback( $bt, btLabel, bxTitle, url, failureMsg, preStartCallback, startCallback, successCallback) {
	addWaitButtonCallbackWithFailureCallback( $bt, btLabel, bxTitle, url, failureMsg, preStartCallback, startCallback, successCallback, null);
}

function addWaitButtonCallbackWithFailureCallback( $bt, btLabel, bxTitle, url, failureMsg, preStartCallback, startCallback, successCallback, failureCallback) {
	addAjaxButtonCallback( $bt, bxTitle, url, failureMsg,
			preStartCallback,
			(formData, param) => {
				setButtonWaitState($bt, btLabel);
				if (startCallback) {
					startCallback(formData, param);
				}
			},
			() => {
				setButtonStandardState($bt, btLabel);
				if (successCallback) {
					successCallback();
				}
			},
			() => {
				setButtonStandardState($bt, btLabel);
				if (failureCallback) {
					failureCallback();
				}
			} );
}
