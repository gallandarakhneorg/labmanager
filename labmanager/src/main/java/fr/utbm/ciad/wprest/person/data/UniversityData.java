package fr.utbm.ciad.wprest.person.data;

/**
 * Describe basic information about a university
 *
 * @param name     the name of the university
 * @param country  the country of the university
 * @param inFrance whether the university is in France or not
 */
public record UniversityData(String name,
                             String country,
                             boolean inFrance) {
}
