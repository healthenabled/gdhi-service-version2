package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_22__Update_i18n_Health_Indicator_Data_fr   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "definition='Existe-t-il une loi pour protéger la vie privée des personnes, régissant la propriété, " +
                "l''accès et le partage des données numériques sur la santé qui sont identifiables individuellement ?' " +
                "WHERE indicator_id=6 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation initiale des professionnels de la santé et dans la formation professionnelle connexe (avant le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des docteurs/médecins en formation médicale ?' " +
                "WHERE indicator_id=20 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation initiale des professionnels de la santé et dans la formation professionnelle connexe (avant le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des infirmières en formation initiale ?  ' " +
                "WHERE indicator_id=21 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation initiale des professionnels de la santé et dans la formation professionnelle connexe (avant le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des professionnels de la santé et des professionnels de soutien à la santé en formation pour les agents de santé communautaires ? ' " +
                "WHERE indicator_id=22 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation en cours d''emploi des professionnels de la santé et des professionnels connexes (après le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des professionnels de la santé et des personnels de soutien en santé en général ? [Définie comme étant les travailleurs de la santé communautaire, les infirmières, les médecins, les professionnels paramédicaux, les gestionnaires/administrateurs de la santé et les technologues.]' " +
                "WHERE indicator_id=10 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation en cours d''emploi des professionnels de la santé et des professionnels connexes (après le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des docteurs/médecins en milieu de travail ?' " +
                "WHERE indicator_id=23 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation en cours d''emploi des professionnels de la santé et des professionnels connexes (après le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des infirmières en milieu de travail ?' " +
                "WHERE indicator_id=24 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Intégration de la santé numérique dans la formation en cours d''emploi des professionnels de la santé et des professionnels connexes (après le déploiement)', " +
                "definition='Plus précisément, la santé numérique fait-elle partie du programme d''études des travailleurs de la santé communautaire sur le marché du travail ?' " +
                "WHERE indicator_id=25 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Formation du personnel de santé numérique''', " +
                "definition='En général, la formation en santé numérique / informatique de la santé / systèmes d''information sur la santé / systèmes d''information sur la santé / programmes de diplôme en informatique biomédicale (dans des établissements publics ou privés) forme-t-elle des travailleurs en santé numérique ?' " +
                "WHERE indicator_id=11 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Formation du personnel de santé numérique''', " +
                "definition='Plus précisément, la formation en informatique sanitaire et/ou biomédicale (dans des établissements publics ou privés) produit-elle des informaticiens ou des spécialistes des systèmes d''information sanitaire formés ?' " +
                "WHERE indicator_id=26 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Maturité des carrières professionnelles en santé numérique dans le secteur public', " +
                "definition='Existe-t-il des titres professionnels et des cheminements de carrière dans le secteur public en santé numérique ?' " +
                "WHERE indicator_id=12 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Cadre national d''architecture de santé numérique et/ou échange d''information sur la santé', " +
                "definition='Existe-t-il un cadre national d''architecture de santé numérique (cybersanté) et/ou d''échange d''information sur la santé ?' " +
                "WHERE indicator_id=13 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Normes d''information sur la santé', " +
                "definition='Existe-t-il des normes relatives à l''échange, à la transmission, à la messagerie, à la sécurité, à la confidentialité et au matériel d''échange de données et d''information sur la santé numérique ?' " +
                "WHERE indicator_id=14 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Disponibilité du réseau', " +
                "definition='Extraire le score de l''indice de préparation du réseau WEF' " +
                "WHERE indicator_id=15 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Planification et soutien de l''entretien continu de l''infrastructure pour la santé numérique''', " +
                "definition='Existe-t-il un plan articulé pour soutenir la fourniture et l''entretien de l''infrastructure de santé numérique (y compris l''équipement - ordinateurs, tablettes, téléphones, fournitures, logiciels, appareils, etc.' " +
                "WHERE indicator_id=16 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Systèmes de santé numériques à l''échelle nationale''', " +
                "definition='Les priorités du secteur public en matière de santé sont appuyées par des systèmes de santé numériques à l''échelle nationale. (Utilisez une feuille de travail distincte pour énumérer les domaines prioritaires spécifiés du pays, indiquer si des systèmes numériques sont en place et si ces systèmes sont à l''échelle nationale.) [ex. Le pays X choisit 4 domaines prioritaires, utilise des systèmes numériques pour traiter 2 des 4, dont 1 seulement à l''échelle nationale, reçoit un score de 25%].' " +
                "WHERE indicator_id=17 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité numérique des fournisseurs de services, des administrateurs et des installations pour la santé numérique, y compris les données de localisation pour la cartographie SIG ', " +
                "definition='Les registres du système de santé: des prestataires, des administrateurs et des établissements publics (et privés s''il y a lieu) identifiables de façon unique sont-ils disponibles, accessibles et à jour ? Les données sont-elles géolocalisées pour permettre la cartographie SIG ?' " +
                "WHERE indicator_id=18 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité des individus pour la santé numérique', " +
                "definition='Existe-t-il des registres sécurisés ou un fichier maître des patients contenant les noms de personnes identifiables de façon unique, accessibles et à jour pour utilisation à des fins liées à la santé ?' " +
                "WHERE indicator_id=19 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité des individus pour la santé numérique', " +
                "definition='Plus précisément, existe-t-il un fichier index de référence sécurisé des patients contenant les noms de personnes identifiables de façon unique, accessible et à jour, qui peut être utilisé à des fins médicales ?' " +
                "WHERE indicator_id=27 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité des individus pour la santé numérique', " +
                "definition='Plus précisément, existe-t-il un registre des naissances sûr, accessible et à jour, contenant les noms de personnes identifiables de façon unique, qui peut être utilisé à des fins médicales ?' " +
                "WHERE indicator_id=28 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité des individus pour la santé numérique', " +
                "definition='Plus précisément, existe-t-il un registre sûr des décès de personnes identifiables de façon unique, accessible et à jour, qui peut être utilisé à des fins médicales ?' " +
                "WHERE indicator_id=29 AND language_id='fr'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestion de l''identité des individus pour la santé numérique', " +
                "definition='Plus précisément, existe-t-il un registre d''immunisation sûr, accessible et à jour, où l''on peut inscrire des personnes identifiables de façon unique, et qui peut être utilisé à des fins de promotion de la santé ?' " +
                "WHERE indicator_id=30 AND language_id='fr'");
    }
}
