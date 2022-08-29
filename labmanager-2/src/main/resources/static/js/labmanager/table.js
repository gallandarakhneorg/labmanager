function adminTableDefinitionWithColCount(nbColumns, order = null, addActionColumn = true, actionWidth = '150px') {
	var columns = Array.from({length: nbColumns}, () => {});
	adminTableDefinition(columns, order, addActionColumn, actionWidth);
}

function adminTableDefinition0(selector, columns, order = null, addActionColumn = true, actionWidth = '150px') {
	if (!order) {
		order = [ [1, 'asc'] ];
	}
	if (addActionColumn) {
		columns = columns.concat([ {
            "orderable": false,
            "width": actionWidth
        } ]);
	}
	var table = $(selector).DataTable({
	    dom: 'fri',
	    renderer: 'bootstrap',
	    scrollCollapse: false,
	    responsive: true,
	    autoWidth: true,
	    order: order,
	    columns: columns,
	});
	return table;
}
