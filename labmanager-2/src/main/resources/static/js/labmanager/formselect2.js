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

function __select2ext_getComponent(config) {
	var $component = null;
	if ('obj' in config && config['obj']) {
		$component = config['obj'];
	} else if ('selector' in config && config['selector']) {
		$component = $(config['selector']);
	} else if ('id' in config && config['id']) {
		$component = $('#' + config['id']);
	} else {
		throw 'You must specify one of the following parameters: obj, selector, id';
	}
	return $component;
}

/** Prepare an combo input to become a multiple entry text input (select2 component).
 * @param config the map of the configuration elements:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `freeText` indicates if the input of free text is enabled. Default is true. 
 *      * `enableRemove` indicates if the entries could be removed from the component. Default is true. 
 *      * `placeholder` the text of the placeholder.
 *      * `separators` the list of separator characters between the entries. Default is `[',',';']`.
 *      * `data` the array of the data that is available in the combo list.
 */
function initSelect2MultipleInput(config) {
	(!('separators' in config)) && (config['separators'] = [',', ';']);
	(!('freeText' in config)) && (config['freeText'] = true);
	(!('enableRemove' in config)) && (config['enableRemove'] = true);

	var s2config = {
		multiple: true,
		placeholder: config['placeholder'],
		allowClear: config['enableRemove'],
		tokenSeparators: config['separators'],
		tags: config['freeText'],
		data: config['data'],
	};
	if (config['freeText']) {
		s2config['createTag'] = (params) => {
			var term = $.trim(params.term);
			if (term === '') {
				return null;
			}
			return {
				id: term,
				text: term,
				newTag: true
			}
		};
	}

	const $component = __select2ext_getComponent(config);
	$component.select2(s2config).val(null).trigger('change');
}

/** Configure a select2 combo imput to keep the input order of the entries and not to sort them automatically as it is configured by default.
 * Credits to "kevin-brown" @ http://github.com/select2/select2/issues/3106
 * This code is executed during user interaction only. 
 * @param config the map of the configuration elements:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 */
function configureSelect2KeepInputOrder(config) {
	const $component = __select2ext_getComponent(config);
	$component.on("select2:select", (evt) => {
		var $element = $(evt.params.data.element);
		var $combo = $(evt.target);
		$element.detach();
		$combo.append($element);
		$combo.trigger("change");
	});
}

/** Configure a select2 combo imput to keep the input order of the entries and not to sort them automatically as it is configured by default.
 * Credits to "kevin-brown" @ http://github.com/select2/select2/issues/3106
 * This code is executed during user interaction only. 
 * @param config the map of the configuration elements:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `selection` the array that represents the initial selection of entries in the combo.
 */
function configureSelect2InitialSelection(config) {
	const $component = __select2ext_getComponent(config);
	$.each(config['selection'], (j, obj) => {
    	var $child = $("option[value=" + obj + "]", $component);
    	if ($child) {
    		var text = $child.text();
    		$child.remove();
    		$component.append(new Option(text , obj, true, false));
    	}
	});
	$component.val(config['selection']).trigger("change");
}

/** Configure a select2 combo imput to accept multiple entries and to keep the order of the imputs.
 * @param config the map of the configuration elements:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `freeText` indicates if the input of free text is enabled. Default is true. 
 *      * `enableRemove` indicates if the entries could be removed from the component. Default is true. 
 *      * `placeholder` the text of the placeholder.
 *      * `separators` the list of separator characters between the entries. Default is `[',',';']`.
 *      * `data` the array of the data that is available in the combo list.
 *      * `selection` the array that represents the initial selection of entries in the combo.
 */
function initMultiSelectionComboWithInputOrder(config) {
	// Save computation time to retrieve the obj for all the internal functions
	const $component = __select2ext_getComponent(config);
	config['obj'] = $component;

	initSelect2MultipleInput(config);
 	configureSelect2KeepInputOrder(config);
 	if ('selection' in config && config['selection']) {
    	configureSelect2InitialSelection(config);
 	}
}
