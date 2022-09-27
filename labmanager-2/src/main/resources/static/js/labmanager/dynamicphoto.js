/** Add a reference to a remote photo inside the given element.
 * @param $imageElement the HTML element that should contain the image.
 * @param id the identifier of the image on the remote server.
 * @param urlBase the base URL of the remote server which is providing the image. This URL must contains `{0}` that is
 *     replaced by the identifier of the image.
 */
function refreshDynamicPhoto($imageElement, id, urlBase) {
	refreshDynamicImageBase($imageElement, id, urlBase, 'person-photo');
}

/** Add a reference to a remote image inside the given element.
 * @param $imageElement the HTML element that should contain the image.
 * @param id the identifier of the image on the remote server.
 * @param urlBase the base URL of the remote server which is providing the image. This URL must contains `{0}` that is
 *     replaced by the identifier of the image.
 */
function refreshDynamicImage($imageElement, id, urlBase) {
	refreshDynamicImageBase($imageElement, id, urlBase, 'dynamically-loaded-image');
}

/** Add a reference to a remote image inside the given element.
 * @param $imageElement the HTML element that should contain the image.
 * @param id the identifier of the image on the remote server.
 * @param urlBase the base URL of the remote server which is providing the image. This URL must contains `{0}` that is
 *     replaced by the identifier of the image.
 * @param className the name of the class of the div that is displaying the image.
 */
function refreshDynamicImageBase($imageElement, id, urlBase, className) {
	$imageElement.empty();
	var url = urlBase.format('0');
	if (id) {
		url = urlBase.format(id);
	}
	var $img = $("<img></img>").attr('src', url).attr('class', className);
	$imageElement.append($img);
}
