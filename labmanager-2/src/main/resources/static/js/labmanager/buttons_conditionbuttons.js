function addConditionButtonCallback0( $bt, bxTitle, bxMessage, btContinue, url, failureMsg, errorTitle, startCallback, successCallback, failureCallback) {
	addAjaxButtonCallback0( $bt, bxTitle, url, failureMsg, errorTitle,
		(continuationFunction) => {
			Swal.fire( {
	   		  title: bxTitle,
	   		  text: bxMessage,
	   		  icon: 'question',
	   		  showCancelButton: true,
	   		  confirmButtonColor: '#d33',
	   		  cancelButtonColor: '#99cc00',
	   		  confirmButtonText: btContinue
	   		} ).then(result => {
	    		if (result.isConfirmed) {
					continuationFunction(result.value);
				}
			});
		},
		startCallback, successCallback, failureCallback);
}

function addConditionButtonCallback1( $bt, bxTitle, bxHtml, preConfirmConfig, btContinue, url, failureMsg, errorTitle, startCallback, successCallback, failureCallback) {
	addAjaxButtonCallback0( $bt, bxTitle, url, failureMsg, errorTitle,
		(continuationFunction) => {
			Swal.fire( {
	   		  title: bxTitle,
	   		  html: bxHtml,
	   		  icon: 'question',
	   		  preConfirm: () => {
				if (preConfirmConfig) {
					return preConfirmConfig();
				} else {
					return true;
				}
			  },
	   		  showCancelButton: true,
	   		  confirmButtonColor: '#d33',
	   		  cancelButtonColor: '#99cc00',
	   		  confirmButtonText: btContinue
	   		} ).then(result => {
	    		if (result.isConfirmed && continuationFunction) {
					var dialogResult = result.value;
					continuationFunction(dialogResult);
				}
			});
		},
		startCallback, successCallback, failureCallback);
}
