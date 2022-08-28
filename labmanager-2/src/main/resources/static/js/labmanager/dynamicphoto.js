function refreshDynamicPhoto($imageElement, id, urlBase) {
	$imageElement.empty();
	var url = urlBase.format('0');
	if (id) {
		url = urlBase.format(id);
	}
	var $img = $("<img></img>").attr('src', url);
	$imageElement.append($img);
}
