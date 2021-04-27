import org.jbibtex.*;

import java.io.StringReader;
import java.util.Map;

public class BibtexParserTest {
    public static void main(String[] args) {
        String bibText = "\n" +
                "\n" +
                "\n" +
                "% VERSION MAINTENUE A JOUR. DERNIERE MAJ  : 02/04/2021\n" +
                "\n" +
                "\n" +
                "\n" +
                "    %Pour les impacts factors pour les journaux\n" +
                "    % http://www.omicsonline.org/open-access-journals-impact-factors.php?gclid=Cj0KEQiA5dK0BRCr49qDzILe74UBEiQA_6gA-kIHVXplE5vZ0ie8rtmK0iEZ13t5fH6CmdzNvll5p-AaAktp8P8HAQ\n" +
                "    \n" +
                "    %impacts des conf\n" +
                "    %http://www.cs.iit.edu/~xli/CS-Conference-Journals-Impact.htm\n" +
                "    \n" +
                " \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %%%   JOURNAUX\n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "\n" +
                "\n" +
                "\n" +
                "    %2021\n" +
                "\n" +
                "@article{bazin_condensed,\n" +
                "\ttitle = {Condensed {Representations} of {Association} {Rules} in n-ary {Relations}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tnumber = {to appear - ongoing revisions},\n" +
                "\tjournal = {\\textbf{IEEE} Transactions on Knowledge and Data Engineering (TKDE) \\textbf{(Impact Factor 5.88- Quartile Q1)}},\n" +
                "\tauthor = {Bazin, Alexandre and Gros, Nicolas and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2021},\n" +
                "}\n" +
                "\n" +
                "    %2019\n" +
                "\n" +
                "\n" +
                "@article{hugol-gential_ontologie_2019,\n" +
                "\ttitle = {Une ontologie de la vigne au verre: la terminologie professionnelle au regard des savoirs mobilisés en viticulture},\n" +
                "\tvolume = {48},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tnumber = {48},\n" +
                "\tjournal = {Recherches en Communication},\n" +
                "\tauthor = {Hugol-Gential, Clémentine and Simon, Marie and Bertaux, Aurélie and Narsis, Ouassila and Belkaroui, Rami and Mouakher, Amira and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "}\n" +
                "    \n" +
                "    %2018\n" +
                " \n" +
                "@article{batrouni_scenario_2018,\n" +
                "\ttitle = {Scenario analysis, from {BigData} to black swan},\n" +
                "\tvolume = {28},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tissn = {1574-0137},\n" +
                "\turl = {http://www.sciencedirect.com/science/article/pii/S157401371730151X},\n" +
                "\tdoi = {https://doi.org/10.1016/j.cosrev.2018.02.001},\n" +
                "\tabstract = {Scenario analysis is a set of methodologies and techniques with the goal of generating strategic insight for decision and policy makers. Our aim for this paper is to overview the scenario analysis field in relation to the relatively new paradigms of BigData. The purpose of such an effort is to clarify where scenario analysis stands today relative to the myriad of data analytics approaches. In an era where the hype about BigData is growing at a breakneck speed, what role scenario analysis can still play? And what kind of synergy it can use to leverage the advances made in other forecasting methods? This paper tries to provide some elements for an answer.},\n" +
                "\tjournal = {Computer Science Review - \\textbf{(Quartile Q1)}},\n" +
                "\tauthor = {Batrouni, Marwan and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2018},\n" +
                "\tnote = {Publisher: \\textbf{Elsevier}},\n" +
                "\tkeywords = {BigData, Black swan, Scenario analysis, Semantic web},\n" +
                "\tpages = {131--139},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "    \n" +
                "    %2016\n" +
                "    @article{peixoto:hal-01356375,\n" +
                "      TITLE = {{Hierarchical Multi-Label Classification Using Web Reasoning for Large Datasets}},\n" +
                "      AUTHOR = {Peixoto, Rafael and Hassan, Thomas and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {https://hal.archives-ouvertes.fr/hal-01356375},\n" +
                "      JOURNAL = {{Open Journal Of Semantic Web}},\n" +
                "      PUBLISHER = {{Research Online Publishing (RonPub)}},\n" +
                "      YEAR = {2016},\n" +
                "      DOI = {10.19210/1006.3.1.1},\n" +
                "      KEYWORDS = {Multi-label classification ;  hierarchical classification ;  Big Data ;  ontology ;  machine learning ;  web reasoning ;  large dataset},\n" +
                "      HAL_ID = {hal-01356375},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    % 2015\n" +
                "    \n" +
                "    @article{chabot:hal-01176091,\n" +
                "      TITLE = {{An Ontology-Based Approach for the Reconstruction and Analysis of Digital Incidents Timelines}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=5&SID=S2rbTXTFkN7h2fBpngU&page=1&doc=1},\n" +
                "      %https://www.researchgate.net/journal/1742-2876_Digital_Investigation},\n" +
                "      JOURNAL = {{Digital Investigation - \\textbf{(Index ISI WoS IF=1.648) (Quartile Q2)}}},\n" +
                "      PUBLISHER = {{Elsevier}},\n" +
                "      SERIES = {Digital Investigation, Special Issue on Big Data and Intelligent Data Analysis},\n" +
                "      PAGES = {18},\n" +
                "      YEAR = {2015},\n" +
                "      KEYWORDS = {Digital Forensics ; Event Reconstruction ; Forensic Ontology ; Knowledge Extraction ; Ontology Population ; Timeline Analysis},\n" +
                "      HAL_ID = {hal-01176091},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2014\n" +
                "    @article{chabot:hal-01025291,\n" +
                "      TITLE = {{A Complete Formalized Knowledge Representation Model for Advanced Digital Forensics Timeline Analysis}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {http://www.journals.elsevier.com/digital-investigation/},\n" +
                "      NOTE = {Fourteenth Annual DFRWS Conference, Denver, USA},\n" +
                "      JOURNAL = {{Digital Investigation - \\textbf{(Index ISI WoS - IF=1.648) (Quartile Q2)}}},\n" +
                "      PUBLISHER = {{Elsevier}},\n" +
                "      VOLUME = {11},\n" +
                "      NUMBER = {2},\n" +
                "      PAGES = {S95-S105},\n" +
                "      YEAR = {2014},\n" +
                "      DOI = {10.1016/j.diin.2014.05.009},\n" +
                "      KEYWORDS = {Digital Forensics ; Timeline Analysis ; Event Reconstruction ; Ontology},\n" +
                "      HAL_ID = {hal-01025291},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    %2012\n" +
                "    @article{martin:hal-00607741,\n" +
                "      TITLE = {{Seasonal Changes of Macroinvertebrate Communities in a Stormwater Wetland Collecting Pesticide Runoff From a Vineyard Catchment (Alsace, France)}},\n" +
                "      AUTHOR = {Martin, Sylvain and Bertaux, Aur{\\'e}lie and Le Ber, Florence and Maillard, Elodie and Imfeld, Gwena{\\\"e}l},\n" +
                "      URL = {https://www.researchgate.net/journal/0090-4341_Archives_of_Environmental_Contamination_and_Toxicology},\n" +
                "      JOURNAL = {{Archives of Environmental Contamination and Toxicology- \\textbf{(Index ISI WoS IF=2,012) (Quartile Q1)}}},\n" +
                "      PUBLISHER = {{\\textbf{Springer Verlag}}},\n" +
                "      VOLUME = {62},\n" +
                "      NUMBER = {1},\n" +
                "      PAGES = {29-41},\n" +
                "      YEAR = {2012},\n" +
                "      DOI = {10.1007/s00244-011-9687-6},\n" +
                "      HAL_ID = {hal-00607741},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    %2010\n" +
                "    @article{bertaux:hal-00531756,\n" +
                "      TITLE = {{Mining Complex Hydrobiological Data with Galois Lattices}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Le Ber, Florence and Braud, Agn{\\`e}s and Tr{\\'e}moli{\\`e}res, Mich{\\`e}le},\n" +
                "      URL = {},\n" +
                "      JOURNAL = {{International Journal of Computing \\& Information Sciences}},\n" +
                "      VOLUME = {7},\n" +
                "      NUMBER = {2},\n" +
                "      PAGES = {63--77},\n" +
                "      YEAR = {2010},\n" +
                "      KEYWORDS = {Galois Lattices ; Formal Concept Analysis ; Multi-Valued Data ; Conceptual Scales ; Multiple Correspondence Analysis},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-00531756/file/bertaux-ijcis.pdf},\n" +
                "      HAL_ID = {hal-00531756},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %%%   CONFERENCES\n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    \n" +
                "    %2020\n" +
                "    \n" +
                "@inproceedings{brabant_preventing_2020,\n" +
                "\ttitle = {Preventing {Overlaps} in {Agglomerative} {Hierarchical} {Conceptual} {Clustering}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {International {Conference} on {Conceptual} {Structures}},\n" +
                "\tpublisher = {\\textbf{Springer}, Cham, \\textbf{Rank A}},\n" +
                "\tauthor = {Brabant, Quentin and Mouakher, Amira and Bertaux, Aurélie},\n" +
                "\tyear = {2020},\n" +
                "\tpages = {74--89},\n" +
                "}\n" +
                "\n" +
                "    \n" +
                "    %2019\n" +
                "\n" +
                "@inproceedings{belkaroui_winecloud_2019,\n" +
                "\ttitle = {{WINECLOUD}: {Une} ontologie d'événements pour la modélisation sémantique des données de capteurs hétérogènes},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {Revue des {Nouvelles} {Technologies} de l'{Information}. {EGC} 2019, vol. {RNTI}-{E}-35},\n" +
                "\tauthor = {Belkaroui, Rami and Mouakher, Amira and Bertaux, Aurélie and Labbani, Ouassila and Hugol-Gential, Clémentine and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "\tpages = {379--380},\n" +
                "}\n" +
                "\n" +
                "@inproceedings{bazin_representation_2019,\n" +
                "\ttitle = {Représentation condensée de règles d’association multidimensionnelles},\n" +
                "\tvolume = {79},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {Extraction et {Gestion} des {Connaissances}: {Actes} de la conférence {EGC}},\n" +
                "\tauthor = {Bazin, Alexandre and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "}\n" +
                "\n" +
                "@inproceedings{hugol-gential_ontologie_2019-1,\n" +
                "\ttitle = {Une ontologie de la culture de la vigne : des savoirs académiques aux savoirs d'expérience},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {Recherches en communication},\n" +
                "\tauthor = {Hugol-Gential, Clémentine and Simon, Marie and Bertaux, Aurélie and Narsis, Ouassila and Belkaroui, Rami and Mouakher, Amira and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "\tpages = {111--129},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "@inproceedings{hugol-gential_science_2019,\n" +
                "\ttitle = {De la science à l’expérience, la pluralité des savoirs en viticulture: le cas des maladies},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {Univigne},\n" +
                "\tauthor = {Hugol-Gential, Clémentine and Simon, Marie and Bertaux, Aurélie and Labbani, Ouassila and Belkaroui, Rami and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "}\n" +
                "\n" +
                "@inproceedings{hugol-gential_tracabilite_2019,\n" +
                "\ttitle = {Traçabilité et analyse {BigData} sur la chaîne de valeur vitivinicole. {De} la science à l'expérience, la pluralité des savoirs en viticulture : le cas des maladies},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {Univigne},\n" +
                "\tauthor = {Hugol-Gential, Clémentine and Simon, Marie and Bertaux, Aurélie and Narsis, Ouassila and Belkaroui, Rami and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "@inproceedings{mouakher_ontology_2019,\n" +
                "\ttitle = {Ontology for {Smart} {Viticulture}: {Integrating} {Inference} {Rules} {Based} on {Sensor} {Data}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tbooktitle = {International {Conference} on {Service}-{Oriented} {Computing}},\n" +
                "\tpublisher = {\\textbf{Springer}, Cham, \\textbf{Rank A}},\n" +
                "\tauthor = {Mouakher, Amira and Bertaux, Aurélie and Labbani, Ouassila and Hugol-Gential, Clémentine and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "\tpages = {168--177},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "@inproceedings{mouakher_ontology-based_2019,\n" +
                "\ttitle = {An {Ontology}-{Based} {Monitoring} {System} in {Vineyards} of the {Burgundy} {Region}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\turl = {https://sci-hub.se/10.1109/wetice.2019.00070},\n" +
                "\tbooktitle = {28th {\\textbf{IEEE}} {International} {Conference} on {Enabling} {Technologies}: {Infrastructure} for {Collaborative} {Enterprises} ({WETICE}-2019)},\n" +
                "\tpublisher = {IEEE , \\textbf{Rank B}},\n" +
                "\tauthor = {Mouakher, Amira and Belkaroui, Rami and Bertaux, Aurélie and Labbani, Ouassila and Hugol-Gential, Clémentine and Nicolle, Christophe},\n" +
                "\tyear = {2019},\n" +
                "\tpages = {307--312},\n" +
                "}\n" +
                "\n" +
                "   \n" +
                "    \n" +
                "    \n" +
                "    %2018\n" +
                " \n" +
                " @inproceedings{belkaroui_towards_2018,\n" +
                "\taddress = {New York, NY, USA},\n" +
                "\tseries = {{IOT} '18},\n" +
                "\ttitle = {Towards {Events} {Ontology} {Based} on {Data} {Sensors} {Network} for {Viticulture} {Domain}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tisbn = {978-1-4503-6564-2},\n" +
                "\turl = {http://doi.acm.org/10.1145/3277593.3277619},\n" +
                "\tdoi = {10.1145/3277593.3277619},\n" +
                "\tbooktitle = {Proceedings of the 8th {International} {Conference} on the {Internet} of {Things}},\n" +
                "\tpublisher = {\\textbf{ACM}},\n" +
                "\tauthor = {Belkaroui, Rami and Bertaux, Aurélie and Labbani, Ouassila and Hugol-Gential, Clémentine and Nicolle, Christophe},\n" +
                "\tyear = {2018},\n" +
                "\tnote = {event-place: Santa Barbara, California},\n" +
                "\tkeywords = {big data, event ontology, IoT, ontologies, semantic sensor data, smart viticulture},\n" +
                "\tpages = {1--7},\n" +
                "}\n" +
                "    \n" +
                "\n" +
                "@inproceedings{batrouni_intelligent_2018,\n" +
                "\ttitle = {Intelligent {Cloud} {Storage} {Management} for {Layered} {Tiers}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\turl = {https://doi.org/10.1007/978-3-030-00560-3_5},\n" +
                "\tdoi = {10.1007/978-3-030-00560-3_5},\n" +
                "\tbooktitle = {International {Conference} on {Cooperative} {Design}, {Visualization} and {Engineering}},\n" +
                "\tpublisher = {\\textbf{Springer}, Cham},\n" +
                "\tauthor = {Batrouni, Marwan and Finch, Steven and Wilson, Scott and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2018},\n" +
                "\tpages = {33--43},\n" +
                "}\n" +
                "\n" +
                "    \n" +
                "   \n" +
                "@inproceedings{batrouni_analyse_2018,\n" +
                "\ttitle = {Analyse {Ontologique} de scénario dans un contexte {Big} {Data}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\turl = {http://editions-rnti.fr/?inprocid=1002414},\n" +
                "\tbooktitle = {Extraction et {Gestion} des {Connaissances}, {EGC} 2018, {Paris}, {France}, {January} 23-26, 2018},\n" +
                "\tauthor = {Batrouni, Marwan and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2018},\n" +
                "\tpages = {387--388},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "@inproceedings{bazin_k-partite_2018,\n" +
                "\taddress = {Olomouc, Czech Republic},\n" +
                "\ttitle = {k-{Partite} {Graphs} as {Contexts}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\turl = {https://hal.archives-ouvertes.fr/hal-01964756},\n" +
                "\tbooktitle = {The 14th {International} {Conference} on {Concept} {Lattices} and {Their} {Applications} ({CLA2018}, \\textbf{Rank B})},\n" +
                "\tauthor = {Bazin, Alexandre and Bertaux, Aurélie},\n" +
                "\tyear = {2018},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "@inproceedings{batrouni_analyse_2018,\n" +
                "\ttitle = {Analyse {Ontologique} de scénario dans un contexte {Big} {Data}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\turl = {http://editions-rnti.fr/?inprocid=1002414},\n" +
                "\tbooktitle = {Extraction et {Gestion} des {Connaissances}, {EGC} 2018, {Paris}, {France}, {January} 23-26, 2018},\n" +
                "\tauthor = {Batrouni, Marwan and Bertaux, Aurélie and Nicolle, Christophe},\n" +
                "\tyear = {2018},\n" +
                "\tpages = {387--388},\n" +
                "}\n" +
                " \n" +
                "    \n" +
                "  \n" +
                "    %2017\n" +
                "\n" +
                "@inproceedings{hassan_ontology-based_2017,\n" +
                "\taddress = {New York, NY, USA},\n" +
                "\tseries = {{SBD} '17},\n" +
                "\ttitle = {Ontology-based {Approach} for {Unsupervised} and {Adaptive} {Focused} {Crawling}},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tisbn = {978-1-4503-4987-1},\n" +
                "\turl = {http://doi.acm.org/10.1145/3066911.3066912},\n" +
                "\tdoi = {10.1145/3066911.3066912},\n" +
                "\tbooktitle = {Proceedings of {The} {International} {Workshop} on {Semantic} {Big} {Data}},\n" +
                "\tpublisher = {\\textbf{ACM}},\n" +
                "\tauthor = {Hassan, Thomas and Cruz, Christophe and Bertaux, Aurélie},\n" +
                "\tyear = {2017},\n" +
                "\tnote = {event-place: Chicago, Illinois},\n" +
                "\tkeywords = {ontology, adaptive systems, classification, cross-referencing, focused crawler, reasonning},\n" +
                "\tpages = {1--6},\n" +
                "}\n" +
                "\n" +
                "\n" +
                "@inproceedings{hassan_predictive_2017,\n" +
                "\ttitle = {Predictive and evolutive cross-referencing for web textual sources},\n" +
                "\tcopyright = {All rights reserved},\n" +
                "\tdoi = {10.1109/SAI.2017.8252230},\n" +
                "\tbooktitle = {2017 {Computing} {Conference}},\n" +
                "\tpublisher = {\\textbf{IEEE}},\n" +
                "\tauthor = {Hassan, Thomas and Cruz, Christophe and Bertaux, Aurélie},\n" +
                "\tyear = {2017},\n" +
                "\tkeywords = {Big Data, Ontology, focused crawler, Adaptive, Classification, competitive intelligence, Computational modeling, Crawlers, cross-reference textual items, Cross-Referencing, Data mining, evolutive cross-referencing, expert intervention, Focused Crawler, information extraction, information retrieval, Knowledge based systems, Ontologies, pattern classification, predictive cross-referencing, Reasonning, search engines, semantic Web, semantic Web technologies, semantic-based classifier, Semantics, Taxonomy, text analysis, Web scale, Web textual sources},\n" +
                "\tpages = {1114--1122},\n" +
                "}\n" +
                "    \n" +
                "    %2016\n" +
                "    @inproceedings{Peixoto:2016:UCP:2928294.2928301,\n" +
                "     author = {Peixoto, Rafael and Hassan, Thomas and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "     title = {An Unsupervised Classification Process for Large Datasets Using Web Reasoning},\n" +
                "     booktitle = {Proceedings of the International Workshop on Semantic Big Data},\n" +
                "     series = {SBD '16},\n" +
                "     year = {2016},\n" +
                "     isbn = {978-1-4503-4299-5},\n" +
                "     location = {San Francisco, California},\n" +
                "     pages = {9:1--9:6},\n" +
                "     articleno = {9},\n" +
                "     numpages = {6},\n" +
                "     url = {http://doi.acm.org/10.1145/2928294.2928301},\n" +
                "     doi = {10.1145/2928294.2928301},\n" +
                "     acmid = {2928301},\n" +
                "     publisher = {ACM},\n" +
                "     address = {New York, NY, USA},\n" +
                "     keywords = {big-data, classification, machine learning, ontology},\n" +
                "    } \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    %2015\n" +
                "    @inproceedings{peixoto:hal-01356367,\n" +
                "      TITLE = {{Semantic HMC: A Predictive Model Using Multi-label Classification for Big Data}},\n" +
                "      AUTHOR = {Peixoto, Rafael and Hassan, Thomas and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {https://hal.archives-ouvertes.fr/hal-01356367},\n" +
                "      BOOKTITLE = {{Trustcom/BigDataSE/ISPA, 2015 IEEE}},\n" +
                "      ADDRESS = {Helsinki, France},\n" +
                "      YEAR = {2015},\n" +
                "      DOI = {10.1109/Trustcom.2015.578},\n" +
                "      KEYWORDS = {Big data ;  Semantics ;  Ontologies ;  Data mining},\n" +
                "      HAL_ID = {hal-01356367},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{chabot:hal-01099652,\n" +
                "      TITLE = {{De la sc{\\`e}ne de crime aux connaissances : repr{\\'e}sentation d'{\\'e}v{\\`e}nements et peuplement d'ontologie appliqu{\\'e}s au domaine de la criminalistique informatique}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Extraction et Gestion des Connaissances 2015}},\n" +
                "      ADDRESS = {Luxembourg, Luxembourg},\n" +
                "      EDITOR = {Editions RNTI},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      YEAR = {2015},\n" +
                "      KEYWORDS = {Criminalistique informatique ; Reconstruction de sc{\\'e}narios ; Repr{\\'e}sentation d'incidents ; Peuplement d'ontologie},\n" +
                "      HAL_ID = {hal-01099652},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{raad:hal-01199440,\n" +
                "      TITLE = {{A Survey on how to Cross-Reference Web Information Sources}},\n" +
                "      AUTHOR = {Raad, Joe and Bertaux, Aur{\\'e}lie and Cruz, Christophe},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Science and Information Conference}},\n" +
                "      ADDRESS = {Londres, United Kingdom},\n" +
                "      EDITOR = {\\textbf{IEEE}},\n" +
                "      PAGES = {609 - 618},\n" +
                "      YEAR = {2015},\n" +
                "      DOI = {10.1109/SAI.2015.7237206},\n" +
                "      HAL_ID = {hal-01199440},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{thomas:hal-01199512,\n" +
                "      TITLE = {{Analyse S{\\'e}mantique du Big Data par Classification Hi{\\'e}rarchique Multi-Label}},\n" +
                "      AUTHOR = {Thomas, Hassan and Peixoto, Rafael and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{extraction et Gestion des Connaissances}},\n" +
                "      ADDRESS = {Luxembourg, Luxembourg},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      YEAR = {2015},\n" +
                "      HAL_ID = {hal-01199512},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{chabot:hal-01199518,\n" +
                "      TITLE = {{Repr{\\'e}sentation s{\\'e}mantiquement riche d'{\\'e}v{\\`e}nements pour le domaine de la criminalistique informatique}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Extraction et Gestion des Connaissances}},\n" +
                "      ADDRESS = {Luxembourg, Luxembourg},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      YEAR = {2015},\n" +
                "      HAL_ID = {hal-01199518},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{peixoto:hal-01198942,\n" +
                "      TITLE = {{Semantic HMC for Business Intelligence using Cross-Referencing}},\n" +
                "      AUTHOR = {Peixoto, Rafael and Thomas, Hassan and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=5&SID=S2rbTXTFkN7h2fBpngU&excludeEventConfig=ExcludeIfFromFullRecPage&page=1&doc=3},\n" +
                "      %http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=8&SID=P1IhTi7rQL2fRvFrK9x&page=1&doc=3},\n" +
                "      BOOKTITLE = {{14th International Conference on Informatics in Economy - \\textbf{(Index ISI WoS)}}},\n" +
                "      ADDRESS = {Bucharest, Romania},\n" +
                "      YEAR = {2015},\n" +
                "      HAL_ID = {hal-01198942},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{benezeth:hal-01177779,\n" +
                "      TITLE = {{Bag-of-word based brand recognition using Markov Clustering Algorithm for codebook generation}},\n" +
                "      AUTHOR = {Benezeth, Yannick and Bertaux, Aur{\\'e}lie and Manceau, Aldric},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{\\textbf{IEEE} International Conference on Image Processing (ICIP)}},\n" +
                "      ADDRESS = {Qu{\\'e}bec, France},\n" +
                "      YEAR = {2015},\n" +
                "      PDF = {https://hal-univ-bourgogne.archives-ouvertes.fr/hal-01177779/file/ICIP_BENEZETH_2015.pdf},\n" +
                "      HAL_ID = {hal-01177779},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{Werner2015,\n" +
                "    author=\"Werner, David\n" +
                "    and Hassan, Thomas\n" +
                "    and Bertaux, Aurelie\n" +
                "    and Cruz, Christophe\n" +
                "    and Silva, Nuno\",\n" +
                "    editor=\"Arai, Kohei\n" +
                "    and Kapoor, Supriya\n" +
                "    and Bhatia, Rahul\",\n" +
                "    title=\"Semantic-Based Recommender System with Human Feeling Relevance Measure\",\n" +
                "    bookTitle=\"Intelligent Systems in Science and Information 2014: Extended and Selected Results from the Science and Information Conference 2014\",\n" +
                "    year=\"2015\",\n" +
                "    publisher=\"Springer International Publishing\",\n" +
                "    address=\"Cham\",\n" +
                "    pages=\"177--191\",\n" +
                "    isbn=\"978-3-319-14654-6\",\n" +
                "    doi=\"10.1007/978-3-319-14654-6_11\",\n" +
                "    url=\"http://dx.doi.org/10.1007/978-3-319-14654-6_11\"\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2014\n" +
                "    \n" +
                "    @inproceedings{chabot:hal-01017212,\n" +
                "      TITLE = {{Automatic Timeline Construction and Analysis For Computer Forensics Purposes}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{\\textbf{IEEE} Joint Intelligence \\& Security Informatics Conference 2014 (IEEE JISIC2014) - \\textbf{(Index ISI WoS)}}},\n" +
                "      ADDRESS = {La Haye, Netherlands},\n" +
                "      PAGES = {4},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {Digital Forensics ; Timeline Analysis ; Event Reconstruction ; Ontology},\n" +
                "      HAL_ID = {hal-01017212},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{werner:hal-01086195,\n" +
                "      TITLE = {{Evaluation de la pertinence dans un syst{\\`e}me de recommandation s{\\'e}mantique de nouvelles {\\'e}conomiques}},\n" +
                "      AUTHOR = {Werner, David and Cruz, Christophe and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{EGC - Fouille de donn{\\'e}es complexes}},\n" +
                "      ADDRESS = {Rennes, France},\n" +
                "      SERIES = {EGC - Fouille de donn{\\'e}es complexes},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {syst{\\`e}me de recommandation ; ontologie},\n" +
                "      HAL_ID = {hal-01086195},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{david:hal-01086188,\n" +
                "      TITLE = {{Using DL-Reasoner for Hierarchical Multilabel Classification applied to Economical e-News}},\n" +
                "      AUTHOR = {David, Werner and Thomas, Hassan and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=5&SID=S2rbTXTFkN7h2fBpngU&excludeEventConfig=ExcludeIfFromFullRecPage&page=1&doc=5},\n" +
                "      BOOKTITLE = {{SAI, Science and Information Conference, 2014 - \\textbf{(Index ISI WoS)}}},\n" +
                "      ADDRESS = {london, United Kingdom},\n" +
                "      PUBLISHER = {{\\textbf{IEEE}}},\n" +
                "      SERIES = {Science and Information Conference (SAI), 2014},\n" +
                "      PAGES = {313 - 320},\n" +
                "      YEAR = {2014},\n" +
                "      DOI = {10.1109/SAI.2014.6918205},\n" +
                "      KEYWORDS = {machine learning ; e-news ; ontology ; recommender system},\n" +
                "      HAL_ID = {hal-01086188},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{thomas:hal-01086191,\n" +
                "      TITLE = {{profile refinement in ontology-based recommander systems for economical e-news}},\n" +
                "      AUTHOR = {Thomas, Hassan and Werner, David and Bertaux, Aur{\\'e}lie and Cruz, Christophe},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{IE, The 14th International Conference on Informatics in Economy - \\textbf{(Index ISI WoS)}}},\n" +
                "      ADDRESS = {Bucharrest, Romania},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {Semantic web. ; e-news ; profile refinement ; recommender system ; ontology},\n" +
                "      HAL_ID = {hal-01086191},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{chabot:hal-00941150,\n" +
                "      TITLE = {{Reconstruction et analyse s{\\'e}mantique de chronologies cybercriminelles}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Kechadi, Tahar and Nicolle, Christophe},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Extraction et Gestion des Connaissances 2014}},\n" +
                "      ADDRESS = {Rennes, France},\n" +
                "      EDITOR = {Editions RNTI},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      VOLUME = {RNTI-E-26},\n" +
                "      PAGES = {521-524},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {Ontologie ; Extraction de connaissances ; Criminalistique informatique ; Reconstruction de sc{\\'e}narios},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-00941150/file/sadfc.pdf},\n" +
                "      HAL_ID = {hal-00941150},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{chabot:hal-01199449,\n" +
                "      TITLE = {{A Complete Formalized Knowledge Representation Model for Advanced Digital Forensics Timeline Analysis}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Nicolle, Christophe and Kechadi, Tahar},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=5&SID=S2rbTXTFkN7h2fBpngU&excludeEventConfig=ExcludeIfFromFullRecPage&page=1&doc=4},\n" +
                "      BOOKTITLE = {{Fourteenth Annual DFRWS Conference - \\textbf{(Index ISI WoS)(Quartile Q2)}}},\n" +
                "      ADDRESS = {Denver, United States},\n" +
                "      PUBLISHER = {{Elsevier}},\n" +
                "      SERIES = {Digital Investigation},\n" +
                "      VOLUME = {11},\n" +
                "      NUMBER = {2},\n" +
                "      PAGES = {S95--S105},\n" +
                "      YEAR = {2014},\n" +
                "      DOI = {10.1016/j.diin.2014.05.009},\n" +
                "      KEYWORDS = {Digital forensics ; Timeline analysis ; Event reconstruction ; Knowledge management ; Ontology},\n" +
                "      HAL_ID = {hal-01199449},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{werner:hal-01199458,\n" +
                "      TITLE = {{An Ontology-Based Recommender System using Hierarchical Multiclassification for Economical e-News}},\n" +
                "      AUTHOR = {Werner, David and Silva, Nuno and Cruz, Christophe and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Science and Information Conference}},\n" +
                "      ADDRESS = {Londres, United Kingdom},\n" +
                "      PUBLISHER = {\\textbf{IEEE/Springer}},\n" +
                "      YEAR = {2014},\n" +
                "      HAL_ID = {hal-01199458},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2013\n" +
                "    \n" +
                "    @inproceedings{tehrani:hal-00881195,\n" +
                "      TITLE = {{Towards a Framework for Semantic Exploration of Frequent Patterns}},\n" +
                "      AUTHOR = {Tehrani, Behrooz Omidvar and Amer-Yahia, Sihem and Termier, Alexandre and Bertaux, Aur{\\'e}lie and Gaussier, {\\'E}ric and Rousset, Marie-Christine},\n" +
                "      URL = {},\n" +
                "      NOTE = {http://ceur-ws.org/Vol-1075/ - ISSN: 1613-0073},\n" +
                "      BOOKTITLE = {{IMMoA 2013 - International Workshop on Information Management in Mobile Application (in conjunction with VLDB 2013)}},\n" +
                "      ADDRESS = {Riva del Garda, Trento, Italy},\n" +
                "      EDITOR = {Thierry Delot and Sandra Geisler and Sergio Ilarri and Christoph Quix},\n" +
                "      PUBLISHER = {{CEUR-WS}},\n" +
                "      VOLUME = {1075},\n" +
                "      PAGES = {7-14},\n" +
                "      YEAR = {2013},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-00881195/file/Towards_a_Framework_for_Semantic_Exploration_of_Frequent_Patterns.pdf},\n" +
                "      HAL_ID = {hal-00881195},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    %2012\n" +
                "    @inproceedings{lopezcueva:hal-00750941,\n" +
                "      TITLE = {{Debugging Multimedia Application Traces through Periodic Pattern Mining}},\n" +
                "      AUTHOR = {L{\\'o}pez-Cueva, Patricia and Bertaux, Aur{\\'e}lie and Termier, Alexandre and M{\\'e}haut, Jean-Fran{\\c c}ois and Santana, Miguel},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=8&SID=P1IhTi7rQL2fRvFrK9x&page=1&doc=5},\n" +
                "      NOTE = {Session 1A: Testing and Characterization of Embedded Software},\n" +
                "      BOOKTITLE = {{EMSOFT 2012, part of ESWEEK - Embedded Systems Week - \\textbf{Index ISI WOS}}},\n" +
                "      ADDRESS = {Tampere, Finland},\n" +
                "      PUBLISHER = {{\\textbf{ACM}}},\n" +
                "      PAGES = {13-22},\n" +
                "      YEAR = {2012},\n" +
                "      DOI = {10.1145/2380356.2380366},\n" +
                "      HAL_ID = {hal-00750941},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{lopezcueva:hal-00741206,\n" +
                "      TITLE = {{Periodic Pattern Mining of Embedded Multimedia Application Traces}},\n" +
                "      AUTHOR = {L{\\'o}pez-Cueva, Patricia and Bertaux, Aur{\\'e}lie and Termier, Alexandre and M{\\'e}haut, Jean-Fran{\\c c}ois and Santana, Miguel},\n" +
                "      URL = {http://apps.webofknowledge.com/full_record.do?product=UA&search_mode=GeneralSearch&qid=5&SID=S2rbTXTFkN7h2fBpngU&excludeEventConfig=ExcludeIfFromFullRecPage&page=1&doc=6},\n" +
                "      BOOKTITLE = {{EMC 2012 - 7th International Conference on Embedded and Multimedia Computing- \\textbf{(Index ISI WoS)}}},\n" +
                "      ADDRESS = {Gwangju, South Korea},\n" +
                "      EDITOR = {James J. Park, Young-Sik Jeong, Sang Oh Park, Hsing-Chung Chen},\n" +
                "      PUBLISHER = {{\\textbf{Springer}}},\n" +
                "      SERIES = {Lecture Notes in Electrical Engineering (LNEE) / Engineering},\n" +
                "      VOLUME = {181},\n" +
                "      PAGES = {29-37},\n" +
                "      YEAR = {2012},\n" +
                "      DOI = {10.1007/978-94-007-5076-0\\_4},\n" +
                "      HAL_ID = {hal-00741206},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    %2009\n" +
                "    @inproceedings{bertaux:hal-00350679,\n" +
                "      TITLE = {{Correspondances de Galois pour la manipulation de contextes flous multi-valu{\\'e}s}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Le Ber, Florence and Braud, Agn{\\`e}s},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Extraction et Gestion de Connaissances}},\n" +
                "      ADDRESS = {Strasbourg, France},\n" +
                "      PUBLISHER = {{C{\\'e}padu{\\`e}s}},\n" +
                "      SERIES = {RNTI},\n" +
                "      PAGES = {193-198},\n" +
                "      YEAR = {2009},\n" +
                "      HAL_ID = {hal-00350679},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{bertaux:hal-00369290,\n" +
                "      TITLE = {{Identifying ecological traits: a concrete FCA-based approach}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Le Ber, Florence and Braud, Agn{\\`e}s and Tr{\\'e}moli{\\`e}res, Mich{\\`e}le},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{7th International Conference on Formal Concept Analysis - ICFCA 2009}},\n" +
                "      ADDRESS = {Darmstadt, Germany},\n" +
                "      EDITOR = {S. Ferr{\\'e}, S. Rudolph},\n" +
                "      PUBLISHER = {{\\textbf{Springer}}},\n" +
                "      SERIES = {LNAI},\n" +
                "      VOLUME = {5548},\n" +
                "      PAGES = {224-236},\n" +
                "      YEAR = {2009},\n" +
                "      HAL_ID = {hal-00369290},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{bertaux:hal-00418920,\n" +
                "      TITLE = {{Combiner treillis de Galois et analyse factorielle multiple pour l'analyse de traits biologiques}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Le Ber, Florence and Li, Pulu and Tr{\\'e}moli{\\`e}res, Mich{\\`e}le},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{XVI{\\`e}me Recontres de la Soci{\\'e}t{\\'e} Francophone de Classification - SFC 2009}},\n" +
                "      ADDRESS = {Grenoble, France},\n" +
                "      PAGES = {117-120},\n" +
                "      YEAR = {2009},\n" +
                "      HAL_ID = {hal-00418920},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    %2007\n" +
                "    @inproceedings{deruyver:hal-00881199,\n" +
                "      TITLE = {{Graph Consistency Checking for Automatic Image Segmentation Driven by Knowledge}},\n" +
                "      AUTHOR = {Deruyver, Aline and Hod{\\'e}, Yann and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{International Conference on Human Machine Interaction}},\n" +
                "      ADDRESS = {Timimoun, Algeria},\n" +
                "      PAGES = {246-251},\n" +
                "      YEAR = {2007},\n" +
                "      HAL_ID = {hal-00881199},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @inproceedings{bertaux:hal-00177936,\n" +
                "      TITLE = {{Mining Complex Hydrobiological Data with Galois Lattices}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Braud, Agn{\\`e}s and Le Ber, Florence},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{International Workshop on Advances in Conceptual Knowledge Engineering (ACKE'07)}},\n" +
                "      ADDRESS = {Regensburg, Germany},\n" +
                "      EDITOR = {A.M.Tjoa and R.R. Wagner},\n" +
                "      PAGES = {519--523},\n" +
                "      YEAR = {2007},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-00177936/file/bertaux_acke07.pdf},\n" +
                "      HAL_ID = {hal-00177936},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2006\n" +
                "    @inproceedings{korczak:hal-01199468,\n" +
                "      TITLE = {{Extension de l’algorithme CURE aux fouilles de donn{\\'e}es volumineuses}},\n" +
                "      AUTHOR = {Korczak, J. and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{extraction et Gestion des Connaissances}},\n" +
                "      ADDRESS = {Villeneuve d'Ascq, France},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      VOLUME = {RNTI-E-6},\n" +
                "      PAGES = {547-548},\n" +
                "      YEAR = {2006},\n" +
                "      HAL_ID = {hal-01199468},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @inproceedings{korczak:hal-01199553,\n" +
                "      TITLE = {{Fouille d'images IRMf : algorithme CURE}},\n" +
                "      AUTHOR = {Korczak, Jerzy and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Extraction et Gestion des Connaissances}},\n" +
                "      ADDRESS = {lille, France},\n" +
                "      SERIES = {Revue des Nouvelles Technologies de l'Information},\n" +
                "      PAGES = {107-117},\n" +
                "      YEAR = {2006},\n" +
                "      HAL_ID = {hal-01199553},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2005\n" +
                "    @inproceedings{korczak:hal-00881183,\n" +
                "      TITLE = {{Fouille d'images IRMf: algorithme CURE}},\n" +
                "      AUTHOR = {Korczak, Jerzy and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Conf{\\'e}rence Extraction et Gestion des connaissances (EGC)}},\n" +
                "      ADDRESS = {Paris, France},\n" +
                "      PAGES = {107-117},\n" +
                "      YEAR = {2005},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-00881183/file/Fouille_da_images_IRMf_algorithme_CURE.pdf},\n" +
                "      HAL_ID = {hal-00881183},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %%%   WORKSHOP - POSTER\n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %2017\n" +
                "    @inproceedings{Hassan:2017:OAU:3066911.3066912,\n" +
                "     author = {Hassan, Thomas and Cruz, Christophe and Bertaux, Aur{\\'e}lie},\n" +
                "     title = {Ontology-based Approach for Unsupervised and Adaptive Focused Crawling},\n" +
                "     booktitle = {Proceedings of The International Workshop on Semantic Big Data},\n" +
                "     series = {SBD '17},\n" +
                "     year = {2017},\n" +
                "     isbn = {978-1-4503-4987-1},\n" +
                "     location = {Chicago, Illinois},\n" +
                "     pages = {2:1--2:6},\n" +
                "     articleno = {2},\n" +
                "     numpages = {6},\n" +
                "     url = {http://doi.acm.org/10.1145/3066911.3066912},\n" +
                "     doi = {10.1145/3066911.3066912},\n" +
                "     acmid = {3066912},\n" +
                "     publisher = {ACM},\n" +
                "     address = {New York, NY, USA},\n" +
                "     keywords = {adaptive systems, classification, cross-referencing, focused crawler, ontology, reasonning},\n" +
                "    } \n" +
                "    \n" +
                "    %2014\n" +
                "    @misc{hassan:hal-01089741,\n" +
                "      TITLE = {{Semantic HMC for Big Data Analysis}},\n" +
                "      AUTHOR = {Hassan, Thomas and Peixoto, Rafael and Cruz, Christophe and Bertaux, Aurlie and Silva, Nuno},\n" +
                "      URL = {},\n" +
                "      NOTE = {Poster},\n" +
                "      HOWPUBLISHED = {{2014 \\textbf{IEEE} International Conference on Big Data}},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {Index Terms-classification ; multi-classify ; Big-Data ; ontology ; semantic technologies ; machine learning},\n" +
                "      PDF = {https://hal.archives-ouvertes.fr/hal-01089741/file/poster-SHMC.pdf},\n" +
                "      HAL_ID = {hal-01089741},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    @misc{werner:hal-01086193,\n" +
                "      TITLE = {{Evaluation de la pertinence dans un syst{\\`e}me de recommandation s{\\'e}mantique de nouvelles {\\'e}conomiques}},\n" +
                "      AUTHOR = {Werner, David and Cruz, Christophe and Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      NOTE = {Poster},\n" +
                "      HOWPUBLISHED = {{Extraction et Gestion des Connaissances}},\n" +
                "      VOLUME = {RNTI-E-26},\n" +
                "      PAGES = {549 - 552},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {syst{\\`e}me de recommandation ; ontologie ; pertinence},\n" +
                "      HAL_ID = {hal-01086193},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %2007\n" +
                "    @misc{bertaux:hal-01199540,\n" +
                "      TITLE = {{Mining Complex Hydrobiological Data with Galois Lattices}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie and Braud, Agn{\\`e}s and Le Ber, Florence},\n" +
                "      URL = {},\n" +
                "      NOTE = {International Workshop on Advances in Conceptual Knowledge Engineering},\n" +
                "      YEAR = {2007},\n" +
                "      HAL_ID = {hal-01199540},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %%%   LIVRES\n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    \n" +
                "    \n" +
                "    %2014\n" +
                "    @incollection{chabot:hal-00955527,\n" +
                "      TITLE = {{Event Reconstruction: A State of the Art}},\n" +
                "      AUTHOR = {Chabot, Yoan and Bertaux, Aur{\\'e}lie and Kechadi, Tahar and Nicolle, Christophe},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Handbook of Research on Digital Crime, Cyberspace Security, and Information Assurance}},\n" +
                "      PUBLISHER = {{IGI Global}},\n" +
                "      PAGES = {15},\n" +
                "      YEAR = {2014},\n" +
                "      KEYWORDS = {Digital Forensic ; Event Reconstruction},\n" +
                "      HAL_ID = {hal-00955527},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    @incollection{werner:hal-01199485,\n" +
                "      TITLE = {{An Ontology-Based Recommender System using Hierarchical Multiclassification for Economical e-News}},\n" +
                "      AUTHOR = {Werner, David and Cruz, Christophe and Bertaux, Aur{\\'e}lie and Silva, Nuno},\n" +
                "      URL = {},\n" +
                "      BOOKTITLE = {{Studies in Computational Intelligence}},\n" +
                "      PUBLISHER = {{\\textbf{Springer} book series}},\n" +
                "      YEAR = {2014},\n" +
                "      HAL_ID = {hal-01199485},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    %%%   THESES\n" +
                "    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n" +
                "    @phdthesis{bertaux:tel-00545647,\n" +
                "      TITLE = {{Galois Lattices for fuzzy many-valued contexts. Application to life traits study in hydrobiology.}},\n" +
                "      AUTHOR = {Bertaux, Aur{\\'e}lie},\n" +
                "      URL = {},\n" +
                "      SCHOOL = {{Universit{\\'e} de Strasbourg}},\n" +
                "      YEAR = {2010},\n" +
                "      KEYWORDS = {Hydrobiology ; biological indices ; ecological features ; Galois lattice ; Formal Concept Analysis ; Multiple Factor Analysis ; algorithm. ; Hydrologie ; Indices biologiques ; Traits {\\'e}cologiques ; Treillis de Galois ; Analyse Formelle de Concepts ; Analyse Factorielle Multiple ; algorithme.},\n" +
                "      TYPE = {Theses},\n" +
                "      PDF = {https://tel.archives-ouvertes.fr/tel-00545647/file/these_aurelie_bertaux.pdf},\n" +
                "      HAL_ID = {tel-00545647},\n" +
                "      HAL_VERSION = {v1},\n" +
                "    }\n" +
                "\n";

        String s = fixEncoding(bibText);
        System.out.println(s);
//
//        try {
//            BibTeXParser bibtexParser = new BibTeXParser();
//            BibTeXDatabase database = bibtexParser.parse(new StringReader(bibText));
//            Map<Key, BibTeXEntry> entries = database.getEntries();
//            for(BibTeXEntry entry : entries.values()) {
//                Value title = entry.getField(new Key("title"));
//                Value abstr = entry.getField(new Key("abstract"));
//                Value keywords = entry.getField(new Key("keywords"));
//                Value year = entry.getField(new Key("year"));
//                Value month = entry.getField(new Key("month"));
//
//
//                System.out.println("Key = " + entry.getKey());
//                if(title != null) System.out.println("\tTitle = " + title.toUserString());
//                if(abstr != null) System.out.println("\tAbstract = " + abstr.toUserString());
//                if(keywords != null) System.out.println("\tKeywords = " + keywords.toUserString());
//                if(year != null) System.out.println("\tYear = " + year.toUserString());
//                if(month != null) System.out.println("\tMonth = " + month.toUserString());
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

    public static String fixEncoding(String bibText) {
        //Manually deal with as much characters as possible yourself
        //Characters not dealt with manually just gets deleted
        //Depending on how the export is handled, we dont have to deal with any of this but I cant control how other DB export their stuff
        bibText = bibText.replaceAll("(\\{\\\\'E\\})", "É");
        bibText = bibText.replaceAll("(\\{\\\\'e\\})", "é");
        bibText = bibText.replaceAll("(\\{\\\\`E\\})", "È");
        bibText = bibText.replaceAll("(\\{\\\\`e\\})", "è");
        bibText = bibText.replaceAll("(\\{\\\\\\^E\\})", "Ê");
        bibText = bibText.replaceAll("(\\{\\\\\\^e\\})", "ê");
        bibText = bibText.replaceAll("(\\{\\\\\"E\\})", "Ë");
        bibText = bibText.replaceAll("(\\{\\\\\"e\\})", "ë");
        bibText = bibText.replaceAll("(\\{\\\\`A\\})", "À");
        bibText = bibText.replaceAll("(\\{\\\\`a\\})", "à");
        bibText = bibText.replaceAll("(\\{\\\\\\^A\\})", "Â");
        bibText = bibText.replaceAll("(\\{\\\\\\^a\\})", "â");
        bibText = bibText.replaceAll("(\\{\\\\\"A\\})", "Ä");
        bibText = bibText.replaceAll("(\\{\\\\\"a\\})", "ä");
        bibText = bibText.replaceAll("(\\{\\\\\\^\\{\\\\I\\}\\})", "Î");
        bibText = bibText.replaceAll("(\\{\\\\\\^\\{\\\\i\\}\\})", "î");
        bibText = bibText.replaceAll("(\\{\\\\\"\\{\\\\I\\}\\})", "Ï");
        bibText = bibText.replaceAll("(\\{\\\\\"\\{\\\\i\\}\\})", "ï");
        bibText = bibText.replaceAll("(\\{\\\\\\^O\\})", "Ô");
        bibText = bibText.replaceAll("(\\{\\\\\\^o\\})", "o");
        bibText = bibText.replaceAll("(\\{\\\\~O\\})", "Õ");
        bibText = bibText.replaceAll("(\\{\\\\~o\\})", "õ");
        bibText = bibText.replaceAll("(\\{\\\\\\^U\\})", "Û");
        bibText = bibText.replaceAll("(\\{\\\\\\^u\\})", "û");
        bibText = bibText.replaceAll("(\\{\\\\\"U\\})", "Ü");
        bibText = bibText.replaceAll("(\\{\\\\\"u\\})", "ü");
        bibText = bibText.replaceAll("(\\{\\\\c\\{C\\}\\})", "Ç");
        bibText = bibText.replaceAll("(\\{\\\\c\\{c\\}\\})", "ç");
        bibText = bibText.replaceAll("(\\{\\\\&\\})", "&");

        // TMT 25/11/20 : deal with {\string#####} chars
        bibText = bibText.replaceAll("\\{\\\\string(.)\\}", "$1");

        //Kind of a radical solution but we can just remove all characters we dont care about
        //We keep normal characters : a-z, A-Z, 0-9
        //Characters we need to read the bibText : @, ",", {, } =
        //Some accents we know how to manage : é, É, è, È... (see above)
        //And characters java & the DB can handle : _, -, ., /, \, ;, :, ', ", ’, ^, !, ?, &, (, ), [, ], whitespaces, line jumps
        //System.out.println(bibText);

        String charsToKeep = "([^"
                + "a-zA-Z0-9"
                + "@,\\{\\}="
                + "ÉéÈèÊêËëÀàÂâÄäÎîÏïÔôÕõÛûÜüÇç&"
                + "\\(\\)\\[\\]’:!?_\\-./\\\\;'\"\\^\\s+"
                + "])";

        bibText = bibText.replaceAll(charsToKeep, "?");
        //System.out.println(bibText);
        return bibText;
    }
}
