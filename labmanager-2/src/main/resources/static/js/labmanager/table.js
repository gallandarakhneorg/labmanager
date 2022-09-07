/** Initialize a Bootstrap Datatable for an administrator usage.
 * @param config the configuration of the administration table. Keys are:
 *      * `id` the identifier of the button that is used for obtaining the button with jQuery. If this `id` is not
 *        provided, the `selector` or `obj` must be provided.
 *      * `selector` the jQuery selector for obtaining the button. If this `selector` is not
 *        provided, the `id` or `obj` must be provided.
 *      * `obj` the button that is used for obtaining the button with jQuery. If this `obj` is not
 *        provided, the `selector` or `id` must be provided.
 *      * `columns` It may be an array of column definitions, excepting the action column if `actionColumn` is true; or it
 *        may be a number of columns. If a number of columns is provided, the array of columns is automatically built up.
 *      * `order` an array that is specifying the order to apply to the values/columns. Default is `[ [1,'asc'] ]`.
 *      * `actionColumn` a boolean value indicating if the action column is automatically added to the list of columns. Default is true.
 *      * `actionWidth` the exepcted size of the "action column". Default is `150px`.
 * @return the table object.
 */
function initAdministrationTable_base(config) {
	(!('actionWidth' in config)) && (config['actionWidth'] = "150px");
	(!('actionColumn' in config)) && (config['actionColumn'] = true);
	(!('order' in config)) && (config['order'] = [ [1, 'asc'] ]);
	
	if ('columns' in config && config['columns']) {
		if (!(config['columns'] instanceof Array)) {
			var cols = Array.from({length: config['columns']}, () => {});
			config['columns'] = cols;
		}
	} else {
		throw "You must provide the 'columns' configuration entry";
	}
	
	if (config['actionColumn']) {
		config['columns'] = config['columns'].concat([ {
            "orderable": false,
            "width": config['actionWidth'],
        } ]);
	}

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

	var table = $component.DataTable({
	    dom: 'frip',
	    renderer: 'bootstrap',
	    scrollCollapse: false,
	    paging: false,
	    responsive: true,
	    autoWidth: true,
	    order: config['order'],
	    columns: config['columns'],
	});
	return table;
}
