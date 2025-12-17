package fr.utbm.ciad.wprest.data;

/**
 * Describe the name of a person and the link on the website.
 * Having the webpageId is useful to create dynamic link to the public page of a user on the website.
 *
 * @param name      The Name of the person (first name, last name)
 * @param webpageId The webpage id of the person
 */
public record PersonOnWebsite(String name, String webpageId) {
}
