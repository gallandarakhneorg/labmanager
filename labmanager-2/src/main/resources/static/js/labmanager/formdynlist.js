// Send the selected items as an array of keys
var GLOBAL_DYNAMIC_DATA_LIST_DATA = {};

// Send the selected items as an array of keys
GLOBAL_FORM_DATA_INPUT_TRANSFORMERS.push((formData) => {
	$.each(GLOBAL_DYNAMIC_DATA_LIST_DATA, (fieldName, componentData) => {
		if (componentData && componentData['converter']) {
			componentData['converter'](formData, fieldName, componentData['data']);
		}
	});
});

/** Initialize the input component for enabling a dynamic data list.
 * @param config the map of the configuration elements:
 *      * `name` the name of the field to upload. The component id is build from the componentIdPrefix and the given name.
 *      * `componentIdPrefix` the prefix to use for building identifiers from names (default is `dynamicDataList_`).
 *      * `columnCount` the number of columns that is used for displaying data (default is 1).
 *      * `data` the array of data that is inside the dynamic list at init time. The structure of each element depends on
 *        the data to be inserted.
 *      * `rowBuilder` the function that add the columns for a data. Parameters of this function are: the data, the column element, the index. 
 *      * `dataCreator` the function that create a data. Parameter is a function that is invoked if data is created. The function parameter
 *        is the created data.
 *      * `deleteTitle` the title of the deletion dialog box.
 *      * `deleteMessage` the message for querying for deletion. 
 *      * `deleteLabel` the label for the deletion button.
 *      * `dataName` the function that replies the name of the data that is passed as argument.
 *      * `dataConverter` a function that add data to the form data. It takes three arguments: the form data, the field name, the data.
 */
function initDynamicDataList(config) {
	(!('componentIdPrefix' in config)) && (config['componentIdPrefix'] = "dynamicDataList_");
	(!('columnCount' in config)) && (config['columnCount'] = 1);
	(!('dataName' in config)) && (config['dataName'] = (dt) => {
		return dt;
	});
	(!('dataConverter' in config)) && (config['dataConverter'] = (formData, fieldName, data) => {
		formData.append(fieldName, data);
	});

	var componentId = config['componentIdPrefix'] + config['name'];
	
	// Add the empty columns on the last line (those with add button) to avoid invalid HTML rendering
	var $addButtonCell = $("#" + componentId + "_addButton").parent();
	for (let i = 0; i < config['columnCount']; i++) {
		var $emptyCell = $('<td></td>');
		$addButtonCell.after($emptyCell);
	}

	// Add the initial data
	GLOBAL_DYNAMIC_DATA_LIST_DATA[config['name']] = {
		converter: config['dataConverter'],
		data: [],
	};
	if ('data' in config && config['data']) {
		$.each(config['data'], (index, data) => {
			dynamicDataList_addRow(config, componentId, data);
		});
	}

	// Add event handler on the 'add' button
	$(document).on('click', "#" + componentId + "_new", (event) => {
		dynamicDataList_addData(config, componentId);
	});
}

function dynamicDataList_addData(config, componentId) {
	if ('dataCreator' in config && config['dataCreator']) {
		var callback = (data) => {
			if (data) {
				dynamicDataList_addRow(config, componentId, data);
			}
		};
		config['dataCreator'](callback);
	}
}

function dynamicDataList_addRow(config, componentId, data) {
	if (!(config['name'] in GLOBAL_DYNAMIC_DATA_LIST_DATA)) {
		GLOBAL_DYNAMIC_DATA_LIST_DATA[config['name']] = {
			converter: config['dataConverter'],
			data: [],
		};
	}
	var dataIndex = GLOBAL_DYNAMIC_DATA_LIST_DATA[config['name']]['data'].length;
	
	var $row = $('<tr></tr>').attr('id', componentId + '_data_' + dataIndex);

	var $deleteButton = $("<span/>").addClass('fa-solid').addClass('fa-trash').attr('id', componentId + '_delete_' + dataIndex);
	$deleteButton = $deleteButton.attr('title', config['deleteLabel']);
	$deleteButton = $deleteButton.attr('style', 'font-size:14pt;cursor: pointer;');
	var $col0 = $("<td/>");
	$col0.append($deleteButton);

	$row.append($col0);

	for (let i = 0; i < config['columnCount']; i++) {
		var $dataCell = $('<td class="dataCell"></td>');
		if ('rowBuilder' in config && config['rowBuilder']) {
			config['rowBuilder'](data, $dataCell, i);
		}
		$row.append($dataCell);
	}
	var $anchor = $("#" + componentId + "_new");
	$anchor.before($row);

	GLOBAL_DYNAMIC_DATA_LIST_DATA[config['name']]['data'].push(data);

	var dataName = config['dataName'](data);

	attachDeletionHandlerNoAjax({
		selector: '#' + componentId + '_delete_' + dataIndex,
		name: dataName,
		questionTitle: config['deleteTitle'],
		questionText: config['deleteMessage'].format(dataName),
		onDeletion: () => {
			var $row = $("table[id='" + componentId + "_table'] tr[id='" + componentId + "_data_" + dataIndex + "']");
			$row.remove();
			GLOBAL_DYNAMIC_DATA_LIST_DATA[config['name']]['data'].splice(dataIndex, 1);
		},
	});	
}
