// Send the list of selected item in the multiple selection input box: {name: [value1, value2...]}
GLOBAL_FORM_DATA_INPUT_TRANSFORMERS.push((formData) => {
	$('select.form-contol-multitext').each( (index, $elt) => {
		if ($($elt).is(':visible')) {
			if ($elt.selectedOptions) {
				var selection = [];
				$.each($elt.selectedOptions, (index, elt) => {
					selection.push(elt.value);
				});
				if (selection) {
					var fieldName = $elt.name;
					if (fieldName) {
						formData.append(fieldName, selection);
					}
				}
			}
		}
	});
});

// Prepare a "select2 object with the appropriate configuration for multi-selection and user tags
function select2_prepare_multiple( $selectList, placeHolderText, dataArray ) {
	$selectList.select2( {
		multiple: true,
		placeholder: placeHolderText,
		allowClear: true,
		tokenSeparators: [',', ';'],
		tags: true, // Allow free text
		createTag: function (params) {
			var term = $.trim(params.term);
			if (term === '') {
				return null;
			}
			return {
				id: term,
				text: term,
				newTag: true
			}
		},
		// Data
		data: dataArray
	}).val(null).trigger('change');
}

// Install the event handler for keeping the input order from the user.
// This function supports a single-level of hierarchy in data
function select2_keepInputOrder_dynamic( $selectList ) {
	// Keep the input order of the authors
	// Credits to "kevin-brown" @ http://github.com/select2/select2/issues/3106
	// This code is executed during user interaction only 
	$selectList.on("select2:select", function (evt) {
		var element = evt.params.data.element;
		var $element = $(element);
		$element.detach();
		$(this).append($element);
		$(this).trigger("change");
	});
}

// Initialize the "select2" object by keeping the order in the provided array
// This function supports a single-level of hierarchy in data
function select2_initializeWithOrderKeeping( comboElementSelector, initialListOfEntries) {
	var $selectList = $(comboElementSelector);
	$.each(initialListOfEntries, function (j, obj) {
    	var $child = $(comboElementSelector + " option[value=" + obj + "]");
    	if ($child) {
    		var text = $child.text();
    		$child.remove();
    		$selectList.append(new Option(text , obj, true, false));
    	}
	});
	$selectList.val(initialListOfEntries).trigger("change");
}

// Initialize the combo (select) component to becomes a multiple selection select2 component
function initMultiSelectionComboWithInputOrder(comboElementSelector, placeholder, listOfAllItems, initialSelectedItems) {
	select2_prepare_multiple( $(comboElementSelector), placeholder, listOfAllItems);
 	select2_keepInputOrder_dynamic( $(comboElementSelector) );
 	if (initialSelectedItems) {
    	select2_initializeWithOrderKeeping(comboElementSelector, initialSelectedItems );
 	}
}
