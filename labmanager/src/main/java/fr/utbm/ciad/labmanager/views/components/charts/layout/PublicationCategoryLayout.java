package fr.utbm.ciad.labmanager.views.components.charts.layout;

import fr.utbm.ciad.labmanager.services.publication.PublicationService;
import fr.utbm.ciad.labmanager.views.components.charts.factory.PublicationCategoryChartFactory;
import fr.utbm.ciad.labmanager.views.components.charts.publicationcategory.PublicationCategoryChart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Implementation a publication category chart.
 *
 * @author $Author: sgalland$
 * @author $Author: erenon$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class PublicationCategoryLayout<T extends PublicationCategoryChart> extends AbstractPublicationCategoryLayout {

    /**
     * Constructor.
     *
     * @param publicationService the service for accessing the scientific publications.
     * @param factory            the factory for creating publication category charts.
     */
    public PublicationCategoryLayout(@Autowired PublicationService publicationService, PublicationCategoryChartFactory<T> factory) {
        super(publicationService, factory);
    }

    public PublicationCategoryLayout(@Autowired PublicationService publicationService,
                                     PublicationCategoryChartFactory<T> factory,
                                     Set<String> multiSelectComboBoxItems,
                                     Integer yearRangeStartValue,
                                     Integer yearRangeEndValue,
                                     boolean generateChart) {
        super(publicationService, factory, multiSelectComboBoxItems, yearRangeStartValue, yearRangeEndValue, generateChart);
    }

}
