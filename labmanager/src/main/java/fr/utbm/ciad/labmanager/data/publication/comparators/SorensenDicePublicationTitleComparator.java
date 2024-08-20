package fr.utbm.ciad.labmanager.data.publication.comparators;

import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Utilities for comparing publication names using the Sorensen Dice algorithm.
 *
 * @author $Author: jferlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
@Primary
public class SorensenDicePublicationTitleComparator extends AbstractPublicationTitleComparator {

    private static final double SIMILARITY_LEVEL = 0.7;

    /**
     * Constructor.
     */
    public SorensenDicePublicationTitleComparator() {
        setSimilarityLevel(SIMILARITY_LEVEL);
    }

    @Override
    protected NormalizedStringSimilarity createStringSimilarityComputer() {
        return new SorensenDice();
    }

}
