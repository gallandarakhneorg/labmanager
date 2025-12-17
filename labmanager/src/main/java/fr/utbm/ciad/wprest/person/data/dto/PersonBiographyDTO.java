package fr.utbm.ciad.wprest.person.data.dto;

/**
 * Data Transfer Object (DTO) representing a person's biography.
 *
 * @param isPrivate        indicates if the biography is private
 * @param biographyContent the content of the biography if the biography is not private
 */
public record PersonBiographyDTO(boolean isPrivate,
                                 String biographyContent) {
}