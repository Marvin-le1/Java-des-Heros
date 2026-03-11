-- ============================================================
--  CRM Avengers — Script de création MySQL
--  Généré le 2026-03-11
--  Encodage : utf8mb4  |  Moteur : InnoDB
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS crm_avengers;
CREATE DATABASE crm_avengers
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crm_avengers;

-- ============================================================
--  TABLE : civil
-- ============================================================
CREATE TABLE civil (
    id_civil          INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    nom               VARCHAR(100)     NOT NULL,
    prenom            VARCHAR(100)     NOT NULL,
    civilite          ENUM('M.','Mme','Mx')     DEFAULT NULL,
    adresse_postale   TEXT                       DEFAULT NULL,
    email             VARCHAR(255)               DEFAULT NULL,
    telephone         VARCHAR(30)                DEFAULT NULL,
    date_naissance    DATE                       DEFAULT NULL,
    nationalite       VARCHAR(100)               DEFAULT NULL,
    date_deces        DATE                       DEFAULT NULL,
    commentaire       TEXT                       DEFAULT NULL,
    date_ajout        DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
                                              ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_civil)
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : organisation
-- ============================================================
CREATE TABLE organisation (
    id_organisation   INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    nom               VARCHAR(200)     NOT NULL,
    adresse_siege     TEXT                       DEFAULT NULL,
    id_dirigeant      INT UNSIGNED               DEFAULT NULL,   -- FK -> civil (dirigeant)
    commentaire       TEXT                       DEFAULT NULL,
    date_ajout        DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_modification DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
                                              ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_organisation),
    CONSTRAINT fk_org_dirigeant
        FOREIGN KEY (id_dirigeant)
        REFERENCES civil (id_civil)
        ON DELETE SET NULL
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : civil <-> organisation (APPARTENIR  0,N — 0,N)
-- ============================================================
CREATE TABLE civil_organisation (
    id_civil        INT UNSIGNED NOT NULL,
    id_organisation INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_civil, id_organisation),
    CONSTRAINT fk_co_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_co_organisation
        FOREIGN KEY (id_organisation)
        REFERENCES organisation (id_organisation)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : super_heros
-- ============================================================
CREATE TABLE super_heros (
    id_super_heros  INT UNSIGNED     NOT NULL AUTO_INCREMENT,
    nom_hero        VARCHAR(150)     NOT NULL,
    id_civil        INT UNSIGNED     NOT NULL,   -- identité secrète — obligatoire (Accords de Sokovie)
    pouvoir         TEXT                       DEFAULT NULL,
    point_faible    TEXT                       DEFAULT NULL,
    score           DECIMAL(5,2)     NOT NULL DEFAULT 0.00,
    commentaire     TEXT                       DEFAULT NULL,
    PRIMARY KEY (id_super_heros),
    CONSTRAINT uq_sh_civil UNIQUE (id_civil),   -- 1 civil = 1 seul héros
    CONSTRAINT fk_sh_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : super_vilain
-- ============================================================
CREATE TABLE super_vilain (
    id_super_vilain    INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nom_vilain         VARCHAR(150) NOT NULL,
    id_civil           INT UNSIGNED          DEFAULT NULL,   -- identité secrète — optionnelle
    pouvoir            TEXT                  DEFAULT NULL,
    point_faible       TEXT                  DEFAULT NULL,
    degre_malveillance INT UNSIGNED NOT NULL DEFAULT 0,
    commentaire        TEXT                  DEFAULT NULL,
    PRIMARY KEY (id_super_vilain),
    CONSTRAINT uq_sv_civil UNIQUE (id_civil),
    CONSTRAINT fk_sv_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : utilisateur
-- ============================================================
CREATE TABLE utilisateur (
    id_utilisateur  INT UNSIGNED NOT NULL AUTO_INCREMENT,
    login           VARCHAR(100) NOT NULL,
    mot_de_passe    VARCHAR(255) NOT NULL,   -- hash bcrypt / argon2
    email           VARCHAR(255) NOT NULL,
    id_civil        INT UNSIGNED          DEFAULT NULL,   -- civil correspondant (optionnel)
    date_creation   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_utilisateur),
    CONSTRAINT uq_usr_login  UNIQUE (login),
    CONSTRAINT uq_usr_email  UNIQUE (email),
    CONSTRAINT uq_usr_civil  UNIQUE (id_civil),
    CONSTRAINT fk_usr_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : role
-- ============================================================
CREATE TABLE role (
    id_role     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nom_role    VARCHAR(100) NOT NULL,
    module      VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_role)
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : droit
-- ============================================================
CREATE TABLE droit (
    id_droit    INT UNSIGNED NOT NULL AUTO_INCREMENT,
    libelle     VARCHAR(150) NOT NULL,
    PRIMARY KEY (id_droit)
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : utilisateur <-> role (POSSEDER  1,N — 0,N)
-- ============================================================
CREATE TABLE utilisateur_role (
    id_utilisateur  INT UNSIGNED NOT NULL,
    id_role         INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_utilisateur, id_role),
    CONSTRAINT fk_ur_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur (id_utilisateur)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ur_role
        FOREIGN KEY (id_role)
        REFERENCES role (id_role)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : role <-> droit (CONTENIR  1,N — 0,N)
-- ============================================================
CREATE TABLE role_droit (
    id_role     INT UNSIGNED NOT NULL,
    id_droit    INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_role, id_droit),
    CONSTRAINT fk_rd_role
        FOREIGN KEY (id_role)
        REFERENCES role (id_role)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rd_droit
        FOREIGN KEY (id_droit)
        REFERENCES droit (id_droit)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : incident
-- ============================================================
CREATE TABLE incident (
    id_incident               INT UNSIGNED NOT NULL AUTO_INCREMENT,
    titre                     VARCHAR(255) NOT NULL,
    description               TEXT                  DEFAULT NULL,
    lieu                      VARCHAR(255)          DEFAULT NULL,
    statut                    ENUM('en_attente','classe_sans_suite','transforme_en_mission')
                              NOT NULL DEFAULT 'en_attente',
    id_declarant_civil        INT UNSIGNED          DEFAULT NULL,
    id_declarant_organisation INT UNSIGNED          DEFAULT NULL,
    id_declarant_hero         INT UNSIGNED          DEFAULT NULL,
    date_declaration          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    commentaire               TEXT                  DEFAULT NULL,
    PRIMARY KEY (id_incident),
    CONSTRAINT fk_inc_civil
        FOREIGN KEY (id_declarant_civil)
        REFERENCES civil (id_civil)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_inc_organisation
        FOREIGN KEY (id_declarant_organisation)
        REFERENCES organisation (id_organisation)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_inc_hero
        FOREIGN KEY (id_declarant_hero)
        REFERENCES super_heros (id_super_heros)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : mission
-- ============================================================
CREATE TABLE mission (
    id_mission            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    titre                 VARCHAR(255) NOT NULL,
    nature                ENUM('sauvetage','projet','voyage','prospection','autre')
                          NOT NULL DEFAULT 'sauvetage',
    id_incident           INT UNSIGNED          DEFAULT NULL,   -- incident d'origine (0,1)
    date_debut            DATETIME              DEFAULT NULL,
    date_fin              DATETIME              DEFAULT NULL,
    itineraire            TEXT                  DEFAULT NULL,
    niveau_gravite        ENUM('insignifiant','dangereux_population','dangereux_environnement',
                               'dangereux_infrastructures','extinction_planete')
                          NOT NULL DEFAULT 'insignifiant',
    niveau_urgence        ENUM('faible','normal','eleve','critique')
                          NOT NULL DEFAULT 'normal',
    infos_complementaires TEXT                  DEFAULT NULL,
    statut                ENUM('planifiee','en_cours','terminee','annulee')
                          NOT NULL DEFAULT 'planifiee',
    PRIMARY KEY (id_mission),
    CONSTRAINT fk_mis_incident
        FOREIGN KEY (id_incident)
        REFERENCES incident (id_incident)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : mission <-> super_heros (AFFECTER  1,N — 0,N)
-- ============================================================
CREATE TABLE mission_super_heros (
    id_mission      INT UNSIGNED NOT NULL,
    id_super_heros  INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_mission, id_super_heros),
    CONSTRAINT fk_msh_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_msh_super_heros
        FOREIGN KEY (id_super_heros)
        REFERENCES super_heros (id_super_heros)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : mission <-> civil (IMPLIQUER — victimes  0,N — 0,N)
-- ============================================================
CREATE TABLE mission_civil (
    id_mission  INT UNSIGNED NOT NULL,
    id_civil    INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_mission, id_civil),
    CONSTRAINT fk_mc_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mc_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : rapport
-- ============================================================
CREATE TABLE rapport (
    id_rapport              INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_mission              INT UNSIGNED NOT NULL,
    id_interlocuteur_civil  INT UNSIGNED          DEFAULT NULL,
    id_interlocuteur_heros  INT UNSIGNED          DEFAULT NULL,
    detail_intervention     TEXT                  DEFAULT NULL,
    resultat                ENUM('succes','echec_partiel','echec')
                                                  DEFAULT NULL,
    degats                  TEXT                  DEFAULT NULL,
    date_rapport            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    commentaire             TEXT                  DEFAULT NULL,
    PRIMARY KEY (id_rapport),
    CONSTRAINT uq_rap_mission UNIQUE (id_mission),   -- 1 mission -> 1 seul rapport
    CONSTRAINT fk_rap_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_rap_civil
        FOREIGN KEY (id_interlocuteur_civil)
        REFERENCES civil (id_civil)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_rap_heros
        FOREIGN KEY (id_interlocuteur_heros)
        REFERENCES super_heros (id_super_heros)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : rapport <-> super_vilain (IDENTIFIER  0,N — 0,N)
-- ============================================================
CREATE TABLE rapport_super_vilain (
    id_rapport      INT UNSIGNED NOT NULL,
    id_super_vilain INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_rapport, id_super_vilain),
    CONSTRAINT fk_rsv_rapport
        FOREIGN KEY (id_rapport)
        REFERENCES rapport (id_rapport)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rsv_super_vilain
        FOREIGN KEY (id_super_vilain)
        REFERENCES super_vilain (id_super_vilain)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : rapport <-> civil (ENREGISTRER nouveaux civils  0,N — 0,N)
-- ============================================================
CREATE TABLE rapport_civil (
    id_rapport  INT UNSIGNED NOT NULL,
    id_civil    INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_rapport, id_civil),
    CONSTRAINT fk_rci_rapport
        FOREIGN KEY (id_rapport)
        REFERENCES rapport (id_rapport)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rci_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : crise
-- ============================================================
CREATE TABLE crise (
    id_crise          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    titre             VARCHAR(255) NOT NULL,
    type_crise        ENUM('hero_demasque','nouveau_vilain','proces',
                           'dommages_collateraux','autre')
                      NOT NULL DEFAULT 'autre',
    id_mission        INT UNSIGNED          DEFAULT NULL,   -- mission ayant déclenché la crise (0,1)
    description       TEXT                  DEFAULT NULL,
    niveau_alerte     ENUM('faible','modere','eleve','critique')
                      NOT NULL DEFAULT 'modere',
    date_declaration  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut            ENUM('ouverte','en_cours','resolue')
                      NOT NULL DEFAULT 'ouverte',
    PRIMARY KEY (id_crise),
    CONSTRAINT fk_cr_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : rapport <-> crise (RATTACHER  0,N — 0,N)
-- ============================================================
CREATE TABLE rapport_crise (
    id_rapport  INT UNSIGNED NOT NULL,
    id_crise    INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_rapport, id_crise),
    CONSTRAINT fk_rcrp_rapport
        FOREIGN KEY (id_rapport)
        REFERENCES rapport (id_rapport)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rcrp_crise
        FOREIGN KEY (id_crise)
        REFERENCES crise (id_crise)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : crise <-> super_heros (CONCERNER  0,N — 0,N)
-- ============================================================
CREATE TABLE crise_super_heros (
    id_crise        INT UNSIGNED NOT NULL,
    id_super_heros  INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_crise, id_super_heros),
    CONSTRAINT fk_csh_crise
        FOREIGN KEY (id_crise)
        REFERENCES crise (id_crise)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_csh_super_heros
        FOREIGN KEY (id_super_heros)
        REFERENCES super_heros (id_super_heros)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : notification
-- ============================================================
CREATE TABLE notification (
    id_notification INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_crise        INT UNSIGNED NOT NULL,
    id_utilisateur  INT UNSIGNED NOT NULL,
    message         TEXT         NOT NULL,
    date_envoi      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lu              TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (id_notification),
    CONSTRAINT fk_notif_crise
        FOREIGN KEY (id_crise)
        REFERENCES crise (id_crise)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_notif_utilisateur
        FOREIGN KEY (id_utilisateur)
        REFERENCES utilisateur (id_utilisateur)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : litige
-- ============================================================
CREATE TABLE litige (
    id_litige       INT UNSIGNED   NOT NULL AUTO_INCREMENT,
    objet           VARCHAR(255)   NOT NULL,
    type_litige     ENUM('degats_infrastructure','degats_biens','deces_citoyen','autre')
                    NOT NULL DEFAULT 'autre',
    id_crise        INT UNSIGNED   NOT NULL,   -- un litige est toujours issu d'une crise
    id_mission      INT UNSIGNED               DEFAULT NULL,
    cout            DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    commentaire     TEXT                       DEFAULT NULL,
    date_creation   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut          ENUM('ouvert','en_cours','resolu','conteste')
                    NOT NULL DEFAULT 'ouvert',
    PRIMARY KEY (id_litige),
    CONSTRAINT fk_lit_crise
        FOREIGN KEY (id_crise)
        REFERENCES crise (id_crise)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_lit_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  ASSOCIATION : litige <-> civil (CONCERNER  1,N — 0,N)
-- ============================================================
CREATE TABLE litige_civil (
    id_litige   INT UNSIGNED NOT NULL,
    id_civil    INT UNSIGNED NOT NULL,
    PRIMARY KEY (id_litige, id_civil),
    CONSTRAINT fk_lc_litige
        FOREIGN KEY (id_litige)
        REFERENCES litige (id_litige)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_lc_civil
        FOREIGN KEY (id_civil)
        REFERENCES civil (id_civil)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : satisfaction
-- ============================================================
CREATE TABLE satisfaction (
    id_satisfaction INT UNSIGNED NOT NULL AUTO_INCREMENT,
    type_retour     ENUM('courrier','sms','email','presse',
                         'photo_reseaux','enquete_terrain','sondage')
                    NOT NULL,
    id_super_heros  INT UNSIGNED          DEFAULT NULL,
    id_super_vilain INT UNSIGNED          DEFAULT NULL,
    id_mission      INT UNSIGNED          DEFAULT NULL,
    id_incident     INT UNSIGNED          DEFAULT NULL,
    note_globale    DECIMAL(4,2)          DEFAULT NULL,   -- ex: 8.50 / 10
    commentaire     TEXT                  DEFAULT NULL,
    date_retour     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_satisfaction),
    CONSTRAINT fk_sat_super_heros
        FOREIGN KEY (id_super_heros)
        REFERENCES super_heros (id_super_heros)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sat_super_vilain
        FOREIGN KEY (id_super_vilain)
        REFERENCES super_vilain (id_super_vilain)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sat_mission
        FOREIGN KEY (id_mission)
        REFERENCES mission (id_mission)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_sat_incident
        FOREIGN KEY (id_incident)
        REFERENCES incident (id_incident)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
--  TABLE : fichier_joint
--  Un fichier est attaché soit à une satisfaction, soit à un litige.
-- ============================================================
CREATE TABLE fichier_joint (
    id_fichier      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nom_fichier     VARCHAR(255) NOT NULL,
    type_fichier    ENUM('jpeg','png','gif','pdf','xlsx','csv','autre')
                    NOT NULL DEFAULT 'autre',
    chemin          VARCHAR(500) NOT NULL,
    id_satisfaction INT UNSIGNED          DEFAULT NULL,
    id_litige       INT UNSIGNED          DEFAULT NULL,
    date_upload     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_fichier),
    CONSTRAINT fk_fj_satisfaction
        FOREIGN KEY (id_satisfaction)
        REFERENCES satisfaction (id_satisfaction)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_fj_litige
        FOREIGN KEY (id_litige)
        REFERENCES litige (id_litige)
        ON DELETE CASCADE ON UPDATE CASCADE,
    -- Au moins un des deux parents doit être renseigné
    CONSTRAINT chk_fj_parent
        CHECK (id_satisfaction IS NOT NULL OR id_litige IS NOT NULL)
) ENGINE=InnoDB;


-- ============================================================
--  INDEXES SUPPLEMENTAIRES (performances)
-- ============================================================
CREATE INDEX idx_civil_nom           ON civil           (nom, prenom);
CREATE INDEX idx_incident_statut     ON incident        (statut);
CREATE INDEX idx_incident_date       ON incident        (date_declaration);
CREATE INDEX idx_mission_statut      ON mission         (statut);
CREATE INDEX idx_mission_dates       ON mission         (date_debut, date_fin);
CREATE INDEX idx_mission_urgence     ON mission         (niveau_urgence);
CREATE INDEX idx_rapport_date        ON rapport         (date_rapport);
CREATE INDEX idx_crise_statut        ON crise           (statut, niveau_alerte);
CREATE INDEX idx_notif_utilisateur   ON notification    (id_utilisateur, lu);
CREATE INDEX idx_satisfaction_date   ON satisfaction    (date_retour);
CREATE INDEX idx_litige_statut       ON litige          (statut);


-- ============================================================
--  DONNEES DE REFERENCE : droits et roles de base
-- ============================================================

INSERT INTO droit (libelle) VALUES
    ('lecture'),
    ('ecriture'),
    ('modification_propre'),
    ('modification_tout'),
    ('suppression_propre'),
    ('suppression_tout');

INSERT INTO role (nom_role, module) VALUES
    ('Gestionnaire Civils',        'civils'),
    ('Gestionnaire Organisations', 'organisations'),
    ('Gestionnaire Super-Heros',   'super_heros'),
    ('Gestionnaire Super-Vilains', 'super_vilains'),
    ('Gestionnaire Incidents',     'incidents'),
    ('Gestionnaire Missions',      'missions'),
    ('Gestionnaire Rapports',      'rapports'),
    ('Gestionnaire Satisfaction',  'satisfaction'),
    ('Gestionnaire Crises',        'crises'),
    ('Gestionnaire Litiges',       'litiges'),
    ('Maitre Supreme',             'all');

-- Le Maitre Supreme (id_role=11) possède tous les droits
INSERT INTO role_droit (id_role, id_droit)
SELECT 11, id_droit FROM droit;


-- ============================================================
--  VUES UTILES
-- ============================================================

-- Nombre d'incidents déclarés par civil
CREATE OR REPLACE VIEW v_civil_stats AS
SELECT
    c.id_civil,
    c.nom,
    c.prenom,
    COUNT(DISTINCT i.id_incident)  AS nb_incidents_declares,
    COUNT(DISTINCT mc.id_mission)  AS nb_missions_victime
FROM civil c
LEFT JOIN incident   i  ON (i.id_declarant_civil = c.id_civil)
LEFT JOIN mission_civil mc ON (mc.id_civil = c.id_civil)
GROUP BY c.id_civil, c.nom, c.prenom;

-- Score moyen des super-héros (d'après les satisfactions)
CREATE OR REPLACE VIEW v_score_super_heros AS
SELECT
    sh.id_super_heros,
    sh.nom_hero,
    ROUND(AVG(s.note_globale), 2) AS score_moyen,
    COUNT(s.id_satisfaction)      AS nb_evaluations
FROM super_heros sh
LEFT JOIN satisfaction s ON (s.id_super_heros = sh.id_super_heros)
                         AND (s.note_globale IS NOT NULL)
GROUP BY sh.id_super_heros, sh.nom_hero;

-- Missions en cours avec niveau d'urgence critique ou élevé
CREATE OR REPLACE VIEW v_missions_urgentes AS
SELECT
    m.id_mission,
    m.titre,
    m.nature,
    m.niveau_gravite,
    m.niveau_urgence,
    m.date_debut,
    GROUP_CONCAT(sh.nom_hero ORDER BY sh.nom_hero SEPARATOR ', ') AS heros_affectes
FROM mission m
JOIN mission_super_heros msh ON msh.id_mission = m.id_mission
JOIN super_heros sh          ON sh.id_super_heros = msh.id_super_heros
WHERE m.statut = 'en_cours'
  AND m.niveau_urgence IN ('eleve', 'critique')
GROUP BY m.id_mission, m.titre, m.nature, m.niveau_gravite, m.niveau_urgence, m.date_debut;


SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
--  FIN DU SCRIPT
--  Tables créées  : 25 (15 entités + 10 tables d'association)
--  Vues créées    : 3
--  Contraintes FK : 35+
-- ============================================================
