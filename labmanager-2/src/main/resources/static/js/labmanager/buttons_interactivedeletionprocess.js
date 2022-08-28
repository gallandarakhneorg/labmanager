function addDeletionButtonCallback( objId, bxTitle, bxMessage, deletionUrl, successMsg, failureMsg, elementName, successCallback ) {
	addDeletionButtonCallback0(objId, bxTitle, bxMessage, deletionUrl, successMsg, failureMsg, () => { return elementName; }, successCallback);
}

function addDeletionButtonCallback1( objId, bxTitle, bxMessage, deletionUrl, successMsg, failureMsg, elementNameCallback, successCallback, confirmButtonText, deletedTitle, errorTitle ) {
	var bt = $('#_btDeleteButton_' + objId);
	addDeletionButtonCallback2( bt, bxTitle, bxMessage, deletionUrl, successMsg, failureMsg, elementNameCallback, successCallback, confirmButtonText, deletedTitle, errorTitle );
}

function addDeletionButtonCallback2( bt, bxTitle, bxMessage, deletionUrl, successMsg, failureMsg, elementNameCallback, successCallback, confirmButtonText, deletedTitle, errorTitle ) {
    bt.on('click', (e) => {
    	Swal.fire( {
   		  title: bxTitle,
   		  text: bxMessage,
   		  icon: 'question',
   		  showCancelButton: true,
   		  confirmButtonColor: '#d33',
   		  cancelButtonColor: '#99cc00',
   		  confirmButtonText: confirmButtonText
   		} ).then(result => {
			if (result.isConfirmed) {
				fetch(deletionUrl, {
		    	    method:'DELETE'
		    	} ).then(response => {
		    		if (response.ok) {
						Swal.fire(
						  deletedTitle,
						  successMsg,
						  'success'
						).then(result => {
		    				if (result.isConfirmed) {
    							successCallback();
		    				}
						});
		    		} else {
		    			var boxmsg = '' + response.status;
		    			if (response.statusText) {
		    				boxmsg = boxmsg + ' - ' + response.statusText;
		    			}
						Swal.fire(
						  errorTitle,
						  failureMsg.format(elementNameCallback(), boxmsg),
						  'error'
						);
		    		}
		    	} ).catch(err => {
					Swal.fire(
					  errorTitle,
					  failureMsg.format(elementNameCallback(), err),
					  'error'
					);
		    	} );
			}
   		} );
    } );
}
