/** Change the visibility of the HTML elements that are associated to "dynamic visibility field".
 * These elements have the class `dynamic-form-group`.
 * @param selectComponentSelector a jQuery selector that targets the "select" HTML component (usually a combo box)
 *     that provides a type that is used for determining the visility of the fields.
 * @param activatedDynamicInputFunction a function that takes the selected type (see `selectComponentSelector`) and replies
 *     the list of the visible fields for that type.
 */
function updateVisibilityForDynamicInputFromSelectOption(selectComponentSelector, activatedDynamicInputFunction) {
	// Hide all the dynamic components
	for (var obj of document.getElementsByClassName("dynamic-form-group")) {
		obj.style.display = 'none';
	}
	// Show the dynamic components that are provided by activatedDynamicInputFunction()
	// depending on the selected option in the select "selectComponentSelector"
    var listElement = $(selectComponentSelector + ' option:selected');
    if (listElement) {
        var selectionValue = listElement.val();
        if (selectionValue) {
        	var currentMapping = activatedDynamicInputFunction(selectionValue);
        	if (currentMapping) {
        		for (var cm of currentMapping) {
        			var obj = document.getElementById(cm);
        			if (obj) {
        	    		obj.style.display = 'block';
        			} else {
        				console.error("Unable to find the element associated to '" + cm + "'. The associated field cannot be shown");
        			}
        		}
        	}
        }
    }
}

/** Initialize the visibility of the HTML elements that are associated to "dynamic visibility field".
 * These elements have the class `dynamic-form-group`.
 * @param selectComponentSelector a jQuery selector that targets the "select" HTML component (usually a combo box)
 *     that provides a type that is used for determining the visility of the fields.
 * @param activatedDynamicInputFunction a function that takes the selected type (see `selectComponentSelector`) and replies
 *     the list of the visible fields for that type.
 */
function initVisibilityUpdatingForDynamicInputsFromSelectOption(selectComponentSelector, activatedDynamicInputFunction) {
	updateVisibilityForDynamicInputFromSelectOption(selectComponentSelector, activatedDynamicInputFunction);
	$(selectComponentSelector).change(() => {
		updateVisibilityForDynamicInputFromSelectOption(selectComponentSelector, activatedDynamicInputFunction);
	});
}
