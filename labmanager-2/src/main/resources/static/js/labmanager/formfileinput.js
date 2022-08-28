// Send the selected files as a multipart file
GLOBAL_FORM_DATA_INPUT_TRANSFORMERS.push((formData) => {
	$('input.form-control[type="file"]').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			var fieldName = $elt.name;
			var files = $elt.files;
			if (files.length > 0) {
				if (files.length > 1) {
					$.each(files, (index, file) => {
						var fileName = elt.name;
						var fieldName = fileName + '_' + index;
						formData.append(fieldName, file, fileName);
					});
				} else {
					var file = files[0];
					var fileName = file.name;
					formData.append(fieldName, file, fileName);
				}
			    formData.append("upload_file", true);
			}
			$.each($('#fileUploadPdf_removed_' + fieldName), (index, $hiddenElt) => {
				var isManuallyRemoved = $hiddenElt.value
				formData.append('@fileUpload_removed_' + fieldName, isManuallyRemoved === 'true');
			});
		}
	});
});


function initFileUploadSinglePdf(name, initialBaseName, pictureView) {
	var $component = $('#fileUploadPdf_' + name);
	// Initialize file upload library
	var config = {
		sizeUnits: 'MB',
		showPreview: true,
		showRemove: true,
		showUpload: false,
		showUploadStats: false,
		showCancel: false,
		showPause: false,
		showClose: false,
		maxFileCount: 1,
		autoReplace: true,
		initialPreviewShowDelete: false,
		dropZoneEnabled: true,
		browseOnZoneClick: true,
		fileTypeSettings: {
			 pdf: function(vType, vName) {
		        return (typeof vType !== "undefined") ? vType == 'application/pdf' : vName.match(/\.(pdf)$/i);
		    },
		},
		allowedFileTypes: [
			'pdf',
		],
		allowedFileExtensions: [
			'pdf',
		],
	};
	if (pictureView) {
		config['initialPreview'] = [
			"<img src='"+ pictureView + "' class='file-preview-image' title='" + initialBaseName + "'>",
		];
	}
	$component.fileinput(config);
	$component.on('filecleared', (event) => {
		$.each($('#fileUploadPdf_removed_' + name), (index, $elt) => {
			$elt.value = "true";
		});
	});
}

function initFileUploadSingleBibTeX(name, enableRemove) {
	var $component = $('#fileUploadBibTeX_' + name);
	// Initialize file upload library
	var config = {
		sizeUnits: 'MB',
		showPreview: true,
		showRemove: enableRemove,
		showUpload: false,
		showUploadStats: false,
		showCancel: false,
		showPause: false,
		showClose: false,
		maxFileCount: 1,
		autoReplace: true,
		initialPreviewShowDelete: false,
		dropZoneEnabled: true,
		browseOnZoneClick: true,
		fileTypeSettings: {
			 bibtex: function(vType, vName) {
		        return (typeof vType !== "undefined") ? vType == 'text/x-bibtex' : vName.match(/\.(bib|bibtex)$/i);
		    },
		},
		allowedFileTypes: [
			'bibtex',
		],
		allowedFileExtensions: [
			'bib', 'bibtex',
		],
	};
	$component.fileinput(config);
	$component.on('filecleared', (event) => {
		$.each($('#fileUploadBibTeX_removed_' + name), (index, $elt) => {
			$elt.value = "true";
		});
	});
}
