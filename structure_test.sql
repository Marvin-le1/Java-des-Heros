-- ============================================================
-- TESTS DE STRUCTURE — CRM Avengers
-- Vérifie que le schéma de base de données est conforme
-- aux entités JPA du projet (indépendant des données).
--
-- Import dans phpMyAdmin :
--   Onglet "Importer" → choisir ce fichier → Exécuter
--   OU coller directement dans l'onglet "SQL"
--
-- Résultat attendu : toutes les lignes doivent afficher "OK ✓"
-- ============================================================

USE crm_avengers;

-- ============================================================
-- 1. EXISTENCE DES TABLES PRINCIPALES
-- ============================================================
SELECT '========== 1. TABLES PRINCIPALES ==========' AS '';

SELECT
    t.expected_table                                            AS table_attendue,
    CASE
        WHEN it.TABLE_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'civil'       AS expected_table UNION ALL
    SELECT 'role'                          UNION ALL
    SELECT 'utilisateur'                   UNION ALL
    SELECT 'organisation'                  UNION ALL
    SELECT 'super_heros'                   UNION ALL
    SELECT 'super_vilain'                  UNION ALL
    SELECT 'incident'                      UNION ALL
    SELECT 'mission'                       UNION ALL
    SELECT 'crise'                         UNION ALL
    SELECT 'litige'                        UNION ALL
    SELECT 'satisfaction'                  UNION ALL
    SELECT 'rapport'
) t
LEFT JOIN information_schema.TABLES it
    ON  it.TABLE_SCHEMA = DATABASE()
    AND it.TABLE_NAME   = t.expected_table
ORDER BY t.expected_table;

-- ============================================================
-- 2. EXISTENCE DES TABLES DE JOINTURE (ManyToMany)
-- ============================================================
SELECT '========== 2. TABLES DE JOINTURE ==========' AS '';

SELECT
    t.expected_table                                            AS table_jointure_attendue,
    CASE
        WHEN it.TABLE_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'utilisateur_role'   AS expected_table UNION ALL
    SELECT 'civil_organisation'                   UNION ALL
    SELECT 'mission_super_heros'                  UNION ALL
    SELECT 'mission_civil'                        UNION ALL
    SELECT 'crise_super_heros'                    UNION ALL
    SELECT 'rapport_super_vilain'                 UNION ALL
    SELECT 'rapport_civil'                        UNION ALL
    SELECT 'rapport_crise'
) t
LEFT JOIN information_schema.TABLES it
    ON  it.TABLE_SCHEMA = DATABASE()
    AND it.TABLE_NAME   = t.expected_table
ORDER BY t.expected_table;

-- ============================================================
-- 3. COLONNES — TABLE civil
-- ============================================================
SELECT '========== 3. COLONNES : civil ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_civil'          AS expected_col UNION ALL
    SELECT 'nom'                               UNION ALL
    SELECT 'prenom'                            UNION ALL
    SELECT 'civilite'                          UNION ALL
    SELECT 'adresse_postale'                   UNION ALL
    SELECT 'email'                             UNION ALL
    SELECT 'telephone'                         UNION ALL
    SELECT 'date_naissance'                    UNION ALL
    SELECT 'nationalite'                       UNION ALL
    SELECT 'date_deces'                        UNION ALL
    SELECT 'commentaire'                       UNION ALL
    SELECT 'date_ajout'                        UNION ALL
    SELECT 'date_modification'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'civil'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 4. COLONNES — TABLE super_heros
-- ============================================================
SELECT '========== 4. COLONNES : super_heros ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_super_heros'    AS expected_col UNION ALL
    SELECT 'nom_hero'                          UNION ALL
    SELECT 'pouvoir'                           UNION ALL
    SELECT 'point_faible'                      UNION ALL
    SELECT 'score'                             UNION ALL
    SELECT 'commentaire'                       UNION ALL
    SELECT 'id_civil'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'super_heros'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 5. COLONNES — TABLE super_vilain
-- ============================================================
SELECT '========== 5. COLONNES : super_vilain ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_super_vilain'   AS expected_col UNION ALL
    SELECT 'nom_vilain'                        UNION ALL
    SELECT 'pouvoir'                           UNION ALL
    SELECT 'point_faible'                      UNION ALL
    SELECT 'degre_malveillance'                UNION ALL
    SELECT 'commentaire'                       UNION ALL
    SELECT 'id_civil'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'super_vilain'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 6. COLONNES — TABLE organisation
-- ============================================================
SELECT '========== 6. COLONNES : organisation ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_organisation'   AS expected_col UNION ALL
    SELECT 'nom'                               UNION ALL
    SELECT 'adresse_siege'                     UNION ALL
    SELECT 'id_dirigeant'                      UNION ALL
    SELECT 'commentaire'                       UNION ALL
    SELECT 'date_ajout'                        UNION ALL
    SELECT 'date_modification'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'organisation'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 7. COLONNES — TABLE utilisateur
-- ============================================================
SELECT '========== 7. COLONNES : utilisateur ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_utilisateur'    AS expected_col UNION ALL
    SELECT 'login'                             UNION ALL
    SELECT 'mot_de_passe'                      UNION ALL
    SELECT 'email'                             UNION ALL
    SELECT 'date_creation'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'utilisateur'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 8. COLONNES — TABLE role
-- ============================================================
SELECT '========== 8. COLONNES : role ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_role'           AS expected_col UNION ALL
    SELECT 'nom_role'                          UNION ALL
    SELECT 'module'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'role'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 9. COLONNES — TABLE incident
-- ============================================================
SELECT '========== 9. COLONNES : incident ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_incident'              AS expected_col UNION ALL
    SELECT 'titre'                                    UNION ALL
    SELECT 'description'                              UNION ALL
    SELECT 'lieu'                                     UNION ALL
    SELECT 'statut'                                   UNION ALL
    SELECT 'id_declarant_civil'                       UNION ALL
    SELECT 'id_declarant_organisation'                UNION ALL
    SELECT 'id_declarant_hero'                        UNION ALL
    SELECT 'date_declaration'                         UNION ALL
    SELECT 'commentaire'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'incident'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 10. COLONNES — TABLE mission
-- ============================================================
SELECT '========== 10. COLONNES : mission ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_mission'            AS expected_col UNION ALL
    SELECT 'titre'                                 UNION ALL
    SELECT 'nature'                                UNION ALL
    SELECT 'niveau_gravite'                        UNION ALL
    SELECT 'niveau_urgence'                        UNION ALL
    SELECT 'statut'                                UNION ALL
    SELECT 'date_debut'                            UNION ALL
    SELECT 'date_fin'                              UNION ALL
    SELECT 'itineraire'                            UNION ALL
    SELECT 'infos_complementaires'                 UNION ALL
    SELECT 'id_incident'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'mission'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 11. COLONNES — TABLE crise
-- ============================================================
SELECT '========== 11. COLONNES : crise ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_crise'          AS expected_col UNION ALL
    SELECT 'titre'                             UNION ALL
    SELECT 'type_crise'                        UNION ALL
    SELECT 'niveau_alerte'                     UNION ALL
    SELECT 'statut'                            UNION ALL
    SELECT 'description'                       UNION ALL
    SELECT 'date_declaration'                  UNION ALL
    SELECT 'id_mission'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'crise'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 12. COLONNES — TABLE litige
-- ============================================================
SELECT '========== 12. COLONNES : litige ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_litige'         AS expected_col UNION ALL
    SELECT 'titre'                             UNION ALL
    SELECT 'type_litige'                       UNION ALL
    SELECT 'statut'                            UNION ALL
    SELECT 'description'                       UNION ALL
    SELECT 'resolution'                        UNION ALL
    SELECT 'date_ouverture'                    UNION ALL
    SELECT 'date_cloture'                      UNION ALL
    SELECT 'id_mission'                        UNION ALL
    SELECT 'id_plaignant'                      UNION ALL
    SELECT 'id_super_heros'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'litige'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 13. COLONNES — TABLE satisfaction
-- ============================================================
SELECT '========== 13. COLONNES : satisfaction ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_satisfaction'   AS expected_col UNION ALL
    SELECT 'id_mission'                        UNION ALL
    SELECT 'id_civil'                          UNION ALL
    SELECT 'note'                              UNION ALL
    SELECT 'commentaire'                       UNION ALL
    SELECT 'date_evaluation'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'satisfaction'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 14. COLONNES — TABLE rapport
-- ============================================================
SELECT '========== 14. COLONNES : rapport ==========' AS '';

SELECT
    c.expected_col                                              AS colonne_attendue,
    CASE
        WHEN ic.COLUMN_NAME IS NOT NULL THEN 'OK ✓'
        ELSE 'MANQUANT ✗'
    END                                                         AS statut
FROM (
    SELECT 'id_rapport'             AS expected_col UNION ALL
    SELECT 'id_mission'                             UNION ALL
    SELECT 'id_interlocuteur_civil'                 UNION ALL
    SELECT 'id_interlocuteur_heros'                 UNION ALL
    SELECT 'detail_intervention'                    UNION ALL
    SELECT 'resultat'                               UNION ALL
    SELECT 'degats'                                 UNION ALL
    SELECT 'date_rapport'                           UNION ALL
    SELECT 'commentaire'
) c
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = 'rapport'
    AND ic.COLUMN_NAME  = c.expected_col
ORDER BY c.expected_col;

-- ============================================================
-- 15. CONTRAINTES NOT NULL sur les colonnes obligatoires
-- ============================================================
SELECT '========== 15. CONTRAINTES NOT NULL ==========' AS '';

SELECT
    n.table_name                                                AS table_,
    n.expected_col                                              AS colonne_,
    CASE
        WHEN ic.IS_NULLABLE = 'NO' THEN 'NOT NULL OK ✓'
        WHEN ic.IS_NULLABLE = 'YES' THEN 'NULLABLE ✗ (devrait être NOT NULL)'
        ELSE 'COLONNE MANQUANTE ✗'
    END                                                         AS contrainte_not_null
FROM (
    -- civil
    SELECT 'civil'        AS table_name, 'nom'             AS expected_col UNION ALL
    SELECT 'civil',                       'prenom'                          UNION ALL
    -- super_heros
    SELECT 'super_heros',                 'nom_hero'                        UNION ALL
    SELECT 'super_heros',                 'id_civil'                        UNION ALL
    -- super_vilain
    SELECT 'super_vilain',                'nom_vilain'                      UNION ALL
    -- organisation
    SELECT 'organisation',                'nom'                             UNION ALL
    -- utilisateur
    SELECT 'utilisateur',                 'login'                           UNION ALL
    SELECT 'utilisateur',                 'mot_de_passe'                    UNION ALL
    SELECT 'utilisateur',                 'email'                           UNION ALL
    -- role
    SELECT 'role',                        'nom_role'                        UNION ALL
    -- incident
    SELECT 'incident',                    'titre'                           UNION ALL
    SELECT 'incident',                    'statut'                          UNION ALL
    -- mission
    SELECT 'mission',                     'titre'                           UNION ALL
    SELECT 'mission',                     'nature'                          UNION ALL
    SELECT 'mission',                     'niveau_gravite'                  UNION ALL
    SELECT 'mission',                     'niveau_urgence'                  UNION ALL
    SELECT 'mission',                     'statut'                          UNION ALL
    -- crise
    SELECT 'crise',                       'titre'                           UNION ALL
    SELECT 'crise',                       'type_crise'                      UNION ALL
    SELECT 'crise',                       'niveau_alerte'                   UNION ALL
    SELECT 'crise',                       'statut'                          UNION ALL
    -- litige
    SELECT 'litige',                      'titre'                           UNION ALL
    SELECT 'litige',                      'type_litige'                     UNION ALL
    SELECT 'litige',                      'statut'                          UNION ALL
    -- satisfaction
    SELECT 'satisfaction',                'id_mission'                      UNION ALL
    SELECT 'satisfaction',                'note'                            UNION ALL
    -- rapport
    SELECT 'rapport',                     'id_mission'
) n
LEFT JOIN information_schema.COLUMNS ic
    ON  ic.TABLE_SCHEMA = DATABASE()
    AND ic.TABLE_NAME   = n.table_name
    AND ic.COLUMN_NAME  = n.expected_col
ORDER BY n.table_name, n.expected_col;

-- ============================================================
-- 16. CONTRAINTES UNIQUE sur les colonnes concernées
-- ============================================================
SELECT '========== 16. CONTRAINTES UNIQUE ==========' AS '';

SELECT
    u.table_name                                                AS table_,
    u.expected_col                                              AS colonne_,
    CASE
        WHEN uniq.COLUMN_NAME IS NOT NULL THEN 'UNIQUE OK ✓'
        ELSE 'UNIQUE MANQUANT ✗'
    END                                                         AS contrainte_unique
FROM (
    SELECT 'super_heros'  AS table_name, 'nom_hero'    AS expected_col UNION ALL
    SELECT 'super_heros',                'id_civil'                    UNION ALL
    SELECT 'super_vilain',               'nom_vilain'                  UNION ALL
    SELECT 'utilisateur',                'login'                       UNION ALL
    SELECT 'utilisateur',                'email'                       UNION ALL
    SELECT 'role',                       'nom_role'                    UNION ALL
    SELECT 'rapport',                    'id_mission'
) u
LEFT JOIN (
    SELECT kcu.TABLE_NAME, kcu.COLUMN_NAME
    FROM information_schema.KEY_COLUMN_USAGE kcu
    INNER JOIN information_schema.TABLE_CONSTRAINTS tc
        ON  tc.TABLE_SCHEMA    = DATABASE()
        AND tc.TABLE_NAME      = kcu.TABLE_NAME
        AND tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME
        AND tc.CONSTRAINT_TYPE IN ('UNIQUE', 'PRIMARY KEY')
    WHERE kcu.TABLE_SCHEMA = DATABASE()
    GROUP BY kcu.TABLE_NAME, kcu.COLUMN_NAME
) uniq
    ON  uniq.TABLE_NAME  = u.table_name
    AND uniq.COLUMN_NAME = u.expected_col
ORDER BY u.table_name, u.expected_col;

-- ============================================================
-- 17. CLÉS ÉTRANGÈRES (relations principales)
-- ============================================================
SELECT '========== 17. CLÉS ÉTRANGÈRES ==========' AS '';

SELECT
    fk.table_                                                   AS table_source,
    fk.col_                                                     AS colonne_fk,
    fk.ref_table_                                               AS table_cible,
    CASE
        WHEN kcu.COLUMN_NAME IS NOT NULL THEN 'FK OK ✓'
        ELSE 'FK MANQUANTE ✗'
    END                                                         AS statut
FROM (
    -- super_heros → civil
    SELECT 'super_heros'   AS table_,  'id_civil'                   AS col_, 'civil'        AS ref_table_ UNION ALL
    -- super_vilain → civil (optional)
    SELECT 'super_vilain',             'id_civil',                             'civil'                      UNION ALL
    -- organisation → civil (dirigeant)
    SELECT 'organisation',             'id_dirigeant',                         'civil'                      UNION ALL
    -- incident → civil
    SELECT 'incident',                 'id_declarant_civil',                   'civil'                      UNION ALL
    -- incident → organisation
    SELECT 'incident',                 'id_declarant_organisation',            'organisation'               UNION ALL
    -- incident → super_heros
    SELECT 'incident',                 'id_declarant_hero',                    'super_heros'                UNION ALL
    -- mission → incident
    SELECT 'mission',                  'id_incident',                          'incident'                   UNION ALL
    -- crise → mission
    SELECT 'crise',                    'id_mission',                           'mission'                    UNION ALL
    -- litige → mission
    SELECT 'litige',                   'id_mission',                           'mission'                    UNION ALL
    -- litige → civil (plaignant)
    SELECT 'litige',                   'id_plaignant',                         'civil'                      UNION ALL
    -- litige → super_heros
    SELECT 'litige',                   'id_super_heros',                       'super_heros'                UNION ALL
    -- satisfaction → mission
    SELECT 'satisfaction',             'id_mission',                           'mission'                    UNION ALL
    -- satisfaction → civil
    SELECT 'satisfaction',             'id_civil',                             'civil'                      UNION ALL
    -- rapport → mission
    SELECT 'rapport',                  'id_mission',                           'mission'                    UNION ALL
    -- rapport → civil
    SELECT 'rapport',                  'id_interlocuteur_civil',               'civil'                      UNION ALL
    -- rapport → super_heros
    SELECT 'rapport',                  'id_interlocuteur_heros',               'super_heros'
) fk
LEFT JOIN information_schema.KEY_COLUMN_USAGE kcu
    ON  kcu.TABLE_SCHEMA            = DATABASE()
    AND kcu.TABLE_NAME              = fk.table_
    AND kcu.COLUMN_NAME             = fk.col_
    AND kcu.REFERENCED_TABLE_NAME   = fk.ref_table_
ORDER BY fk.table_, fk.col_;

-- ============================================================
-- 18. RÉCAPITULATIF GLOBAL
-- ============================================================
SELECT '========== 18. RÉCAPITULATIF GLOBAL ==========' AS '';

SELECT
    'Tables principales'      AS categorie,
    COUNT(*)                  AS total_attendu,
    SUM(CASE WHEN it.TABLE_NAME IS NOT NULL THEN 1 ELSE 0 END) AS ok,
    SUM(CASE WHEN it.TABLE_NAME IS NULL     THEN 1 ELSE 0 END) AS manquant
FROM (
    SELECT 'civil' AS t UNION ALL SELECT 'role'         UNION ALL
    SELECT 'utilisateur'        UNION ALL SELECT 'organisation' UNION ALL
    SELECT 'super_heros'        UNION ALL SELECT 'super_vilain' UNION ALL
    SELECT 'incident'           UNION ALL SELECT 'mission'      UNION ALL
    SELECT 'crise'              UNION ALL SELECT 'litige'       UNION ALL
    SELECT 'satisfaction'       UNION ALL SELECT 'rapport'
) expected
LEFT JOIN information_schema.TABLES it
    ON it.TABLE_SCHEMA = DATABASE() AND it.TABLE_NAME = expected.t

UNION ALL

SELECT
    'Tables de jointure'      AS categorie,
    COUNT(*)                  AS total_attendu,
    SUM(CASE WHEN it.TABLE_NAME IS NOT NULL THEN 1 ELSE 0 END) AS ok,
    SUM(CASE WHEN it.TABLE_NAME IS NULL     THEN 1 ELSE 0 END) AS manquant
FROM (
    SELECT 'utilisateur_role' AS t  UNION ALL SELECT 'civil_organisation'  UNION ALL
    SELECT 'mission_super_heros'    UNION ALL SELECT 'mission_civil'       UNION ALL
    SELECT 'crise_super_heros'      UNION ALL SELECT 'rapport_super_vilain' UNION ALL
    SELECT 'rapport_civil'          UNION ALL SELECT 'rapport_crise'
) expected
LEFT JOIN information_schema.TABLES it
    ON it.TABLE_SCHEMA = DATABASE() AND it.TABLE_NAME = expected.t;
