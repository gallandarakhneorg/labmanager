function addAjaxButtonCallback0( $bt, bxTitle, url, failureMsg, errorTitle, preStartCallback, startCallback, successCallback, failureCallback ) {
    $bt.on('click', (e) => {
		var internalFct = (param) => {
			var formData = new FormData();
			startCallback(formData, param);
			$.ajax({
				url: url,
				timeout: 100000,
				type: 'post',
				data: formData,
				//dataType: 'json',
				contentType: false,
				processData: false,
				cache: false,
				async:true,
				success: (jsonData, textStatus, obj) => {
					if (successCallback) {
						successCallback();
					}					
				},
				error: (obj, textStatus, errorThrown) => {
					var errCode = obj.status;
					var errMsg = '';
					if (obj.responseJSON) {
						errMsg = obj.responseJSON.message ? obj.responseJSON.message : obj.responseJSON.error;
					} else {
						errMsg = obj.statusText;
					}
					var errMessage = failureMsg.format(errCode, errMsg);	
					Swal.fire(
							  errorTitle,
							  errMessage,
							  'error'
							).then(result => {
						if (failureCallback) {
							failureCallback();
						}
					});
				},
			});
		};
		if (preStartCallback) {
			preStartCallback(internalFct);
		} else {
			internalFct();
		}
    } );
}
