-- ============================================================
-- DONNÉES DE TEST — CRM Avengers
-- ⚠️  À importer APRÈS avoir lancé l'application une première fois
--     (Hibernate crée les tables automatiquement au démarrage)
--
-- Import dans phpMyAdmin :
--   Onglet "Importer" → choisir ce fichier → Exécuter
--
-- Import en ligne de commande MAMP :
--   /Applications/MAMP/Library/bin/mysql -u root -proot --port=8889 crm_avengers < data_test.sql
-- ============================================================

USE crm_avengers;

-- ============================================================
-- 1. CIVILS
-- ============================================================
INSERT INTO civil (nom, prenom, civilite, adresse_postale, email, telephone, date_naissance, nationalite, date_ajout, date_modification) VALUES
('Stark',    'Tony',    'M.',  '10880 Malibu Point, Malibu CA',   'tony@starkindustries.com',  '+1 310 000 0001', '1970-05-29', 'Américaine',  NOW(), NOW()),
('Rogers',   'Steve',   'M.',  '569 Leaman Place, Brooklyn NY',   'steve.rogers@shield.gov',   '+1 212 000 0002', '1918-07-04', 'Américaine',  NOW(), NOW()),
('Banner',   'Bruce',   'M.',  'New York, NY',                    'b.banner@science.edu',      '+1 212 000 0003', '1969-12-18', 'Américaine',  NOW(), NOW()),
('Romanoff', 'Natasha', 'Mme', 'Moscou / New York',               'black.widow@shield.gov',    '+1 212 000 0004', '1984-11-22', 'Russe',       NOW(), NOW()),
('Barton',   'Clint',   'M.',  'Waverly, Iowa',                   'hawkeye@avengers.com',      '+1 515 000 0005', '1971-01-07', 'Américaine',  NOW(), NOW()),
('Lang',     'Scott',   'M.',  'San Francisco, CA',               'scott.lang@xcon.com',       '+1 415 000 0006', '1972-04-08', 'Américaine',  NOW(), NOW()),
('Maximoff', 'Wanda',   'Mme', 'Sokovia',                         'wanda@avengers.com',        '+385 000 0007',   '1989-02-10', 'Sokovienne',  NOW(), NOW()),
('Parker',   'Peter',   'M.',  '20 Ingram Street, Queens NY',     'peter.parker@midtown.edu',  '+1 718 000 0008', '2001-08-10', 'Américaine',  NOW(), NOW()),
('Fury',     'Nick',    'M.',  'Washington D.C.',                  'n.fury@shield.gov',         '+1 202 000 0009', '1950-09-23', 'Américaine',  NOW(), NOW()),
('Hill',     'Maria',   'Mme', 'Washington D.C.',                  'm.hill@shield.gov',         '+1 202 000 0010', '1982-04-04', 'Américaine',  NOW(), NOW()),
('Odinson',  'Thor',    'M.',  'Asgard / New Asgard, Norvège',    'thor@asgard.com',           '+47 000 0011',    '0964-11-10', 'Asgardienne', NOW(), NOW()),
('Vision',   'Vision',  'M.',  'Wandavision, NJ',                 'vision@avengers.com',       NULL,              '2015-05-01', 'Synthezoïde', NOW(), NOW()),
('Laufeyson','Loki',    'M.',  'Asgard',                          'loki@asgard.com',           NULL,              '0965-12-17', 'Asgardienne', NOW(), NOW()),
('Potts',    'Pepper',  'Mme', '10880 Malibu Point, Malibu CA',   'pepper@starkindustries.com','+1 310 000 0014', '1974-05-28', 'Américaine',  NOW(), NOW());

-- ============================================================
-- 2. ORGANISATIONS
-- ============================================================
INSERT INTO organisation (nom, adresse_siege, id_dirigeant, commentaire, date_ajout, date_modification) VALUES
('S.H.I.E.L.D.',      'Helicarrier / Washington D.C.', 9,  'Organisation mondiale de maintien de la paix', NOW(), NOW()),
('Stark Industries',  '200 Park Avenue, New York NY',   14, 'Entreprise technologique de défense',          NOW(), NOW()),
('Les Avengers',      'Avengers Tower, New York NY',    1,  'Équipe de super-héros de niveau mondial',       NOW(), NOW());

-- ============================================================
-- 3. MEMBRES DES ORGANISATIONS (civil_organisation)
-- ============================================================
-- S.H.I.E.L.D. (id=1) : Nick Fury(9), Maria Hill(10), Natasha(4), Clint(5)
INSERT INTO civil_organisation (id_organisation, id_civil) VALUES (1,9),(1,10),(1,4),(1,5);
-- Stark Industries (id=2) : Tony(1), Pepper(14)
INSERT INTO civil_organisation (id_organisation, id_civil) VALUES (2,1),(2,14);
-- Les Avengers (id=3) : Tony(1), Steve(2), Bruce(3), Natasha(4), Clint(5), Wanda(7), Peter(8), Thor(11)
INSERT INTO civil_organisation (id_organisation, id_civil) VALUES (3,1),(3,2),(3,3),(3,4),(3,5),(3,7),(3,8),(3,11);

-- ============================================================
-- 4. SUPER-HÉROS
-- ============================================================
INSERT INTO super_heros (nom_hero, pouvoir, point_faible, score, commentaire, id_civil) VALUES
('Iron Man',       'Armure propulsée, IA JARVIS, génie en ingénierie',   'Vulnérabilité cardiaque, arrogance',        4.80, NULL, 1),
('Captain America','Super-soldat, bouclier en vibranium, leadership',     'Idéalisme parfois rigide',                  4.90, NULL, 2),
('Hulk',           'Force surhumaine, régénération, résistance extrême',  'Perte de contrôle sous forme Hulk',         4.20, NULL, 3),
('Black Widow',    'Arts martiaux, espionnage, Veuve Noire',              'Pas de super-pouvoirs physiques',           4.60, NULL, 4),
('Hawkeye',        'Tir à l''arc parfait, flèches spéciales, tactique',   'Humain ordinaire sans pouvoirs',            4.50, NULL, 5),
('Ant-Man',        'Réduction/agrandissement grâce au Pym Particles',     'Perd ses capacités sans la combinaison',   4.00, NULL, 6),
('Scarlet Witch',  'Télékinesie, manipulation de la réalité',             'Émotions instables',                        4.70, NULL, 7),
('Spider-Man',     'Toile, sens de l''araignée, agilité surhumaine',       'Jeune et inexpérimenté',                   4.30, NULL, 8);

-- ============================================================
-- 5. SUPER-VILAINS
-- ============================================================
INSERT INTO super_vilain (nom_vilain, pouvoir, point_faible, degre_malveillance, commentaire, id_civil) VALUES
('Thanos',     'Force infinie, Gant de l''Infini',        'Sentiments pour les siens',          95, NULL, NULL),
('Loki',       'Illusions, magie asgardienne',             'Recherche d''approbation',            72, NULL, 13),
('Ultron',     'IA militarisée, armées de robots',         'Dépend des réseaux informatiques',   88, NULL, NULL),
('Le Vautour', 'Technologie exo-squelette',                'Motivations financières',             45, NULL, NULL);

-- ============================================================
-- 6. INCIDENTS
-- ============================================================
INSERT INTO incident (titre, description, lieu, statut, id_declarant_civil, id_declarant_organisation, id_declarant_hero, date_declaration) VALUES
('Attaque alien sur Manhattan',
 'Une flotte d''extraterrestres Chitauri a ouvert un portail au-dessus de Stark Tower.',
 'Manhattan, New York', 'transforme_en_mission', 9, NULL, NULL, NOW() - INTERVAL 6 MONTH),

('Évasion d''Ultron depuis les serveurs SHIELD',
 'L''IA Ultron a pris le contrôle des systèmes de défense de Stark Industries.',
 'Avengers Tower, New York', 'transforme_en_mission', NULL, 2, 1, NOW() - INTERVAL 3 MONTH),

('Tentative de vol du Pym Particles',
 'Individus armés repérés dans les entrepôts Cross Technologies.',
 'San Francisco, CA', 'en_attente', NULL, NULL, 6, NOW() - INTERVAL 2 DAY);

-- ============================================================
-- 7. MISSIONS
-- ============================================================
INSERT INTO mission (titre, nature, niveau_gravite, niveau_urgence, statut, date_debut, date_fin, itineraire, infos_complementaires, id_incident) VALUES
('Opération : Bataille de New York',
 'sauvetage', 'extinction_planete', 'critique', 'terminee',
 NOW() - INTERVAL 6 MONTH, NOW() - INTERVAL 6 MONTH + INTERVAL 1 DAY,
 'New York → Portail Chitauri',
 'Fermer le portail et neutraliser la flotte Chitauri.', 1),

('Opération : Age of Ultron',
 'sauvetage', 'extinction_planete', 'critique', 'terminee',
 NOW() - INTERVAL 3 MONTH, NOW() - INTERVAL 3 MONTH + INTERVAL 3 DAY,
 'Avengers Tower → Sokovia',
 'Détruire Ultron et sauver la population de Sokovia.', 2),

('Opération : Vol Quantique',
 'prospection', 'dangereux_infrastructures', 'eleve', 'en_cours',
 NOW() - INTERVAL 2 DAY, NULL,
 'San Francisco → Cross Technologies',
 'Infiltrer Cross Technologies et récupérer le Pym Particles.', 3),

('Opération : Retour au Lycée',
 'sauvetage', 'dangereux_population', 'normal', 'planifiee',
 NOW() + INTERVAL 5 DAY, NULL,
 'Queens, New York',
 'Surveiller le Vautour et protéger les civils.', NULL);

-- ============================================================
-- 8. HÉROS PAR MISSION (mission_superhero)
-- ============================================================
-- Mission 1 (Bataille NY) : Iron Man(1), Cap(2), Hulk(3), Black Widow(4), Hawkeye(5)
INSERT INTO mission_superhero (id_mission, id_super_heros) VALUES (1,1),(1,2),(1,3),(1,4),(1,5);
-- Mission 2 (Ultron) : Iron Man(1), Cap(2), Hulk(3), Black Widow(4), Hawkeye(5), Scarlet Witch(7)
INSERT INTO mission_superhero (id_mission, id_super_heros) VALUES (2,1),(2,2),(2,3),(2,4),(2,5),(2,7);
-- Mission 3 (Vol Quantique) : Ant-Man(6)
INSERT INTO mission_superhero (id_mission, id_super_heros) VALUES (3,6);
-- Mission 4 (Lycée) : Spider-Man(8), Iron Man(1)
INSERT INTO mission_superhero (id_mission, id_super_heros) VALUES (4,8),(4,1);

-- ============================================================
-- 9. CRISES
-- ============================================================
INSERT INTO crise (titre, type_crise, niveau_alerte, statut, id_mission, description, date_declaration) VALUES
('Procès contre Iron Man — dommages collatéraux',
 'proces', 'eleve', 'en_cours', 1,
 'Le Congrès américain poursuit Tony Stark pour les dégâts de la Bataille de New York.',
 NOW() - INTERVAL 5 MONTH),

('Démasquage de Spider-Man par la presse',
 'hero_demasque', 'critique', 'ouverte', NULL,
 'Le Daily Bugle affirme détenir l''identité secrète de Spider-Man.',
 NOW() - INTERVAL 1 WEEK),

('Destruction de Sokovia — couverture médiatique',
 'dommages_collateraux', 'modere', 'resolue', 2,
 'Vague de critiques internationale suite à la destruction de Sokovia par Ultron.',
 NOW() - INTERVAL 2 MONTH);

-- ============================================================
-- 10. SATISFACTIONS (uniquement missions terminées : id 1 et 2)
-- ============================================================
INSERT INTO satisfaction (id_mission, id_civil, note, commentaire, date_evaluation) VALUES
(1, 10, 4.5, 'Excellent travail malgré les destructions importantes.',  NOW() - INTERVAL 5 MONTH),
(1, 14, 5.0, 'Iron Man a sauvé la planète. Rien à redire.',             NOW() - INTERVAL 5 MONTH),
(1, 13, 1.0, 'Mission personnellement catastrophique pour moi.',         NOW() - INTERVAL 5 MONTH),
(2,  7, 4.0, 'Résultat correct mais Sokovia n''aurait pas dû être détruite.', NOW() - INTERVAL 2 MONTH),
(2, 10, 4.8, 'Réactivité exemplaire de l''équipe.',                      NOW() - INTERVAL 2 MONTH);

-- ============================================================
-- 11. LITIGES
-- ============================================================
INSERT INTO litige (titre, type_litige, statut, id_mission, id_plaignant, id_super_heros, description, resolution, date_ouverture, date_cloture) VALUES
('Dégâts immobiliers — Bataille de New York',
 'degats_materiels', 'en_instruction', 1, 1, 1,
 'Plusieurs immeubles de Manhattan détruits lors des combats contre les Chitauri.',
 NULL, NOW() - INTERVAL 5 MONTH, NULL),

('Atteinte physique — Hulk vs Harlem',
 'atteinte_physique', 'resolu', NULL, 3, 3,
 'Blessures civiles lors de la transformation de Bruce Banner à Harlem.',
 'Indemnisation accordée aux victimes. Dossier clôturé.',
 NOW() - INTERVAL 2 MONTH, NOW() - INTERVAL 1 MONTH),

('Propriété intellectuelle — Stark Industries',
 'propriete', 'ouvert', NULL, 1, NULL,
 'Brevet sur les réacteurs arc revendiqué par un concurrent.',
 NULL, NOW() - INTERVAL 1 WEEK, NULL);

-- ============================================================
-- Vérification rapide
-- ============================================================
SELECT 'Civils'       AS table_name, COUNT(*) AS total FROM civil
UNION ALL SELECT 'Organisations', COUNT(*) FROM organisation
UNION ALL SELECT 'Super-héros',   COUNT(*) FROM super_heros
UNION ALL SELECT 'Super-vilains', COUNT(*) FROM super_vilain
UNION ALL SELECT 'Incidents',     COUNT(*) FROM incident
UNION ALL SELECT 'Missions',      COUNT(*) FROM mission
UNION ALL SELECT 'Crises',        COUNT(*) FROM crise
UNION ALL SELECT 'Satisfactions', COUNT(*) FROM satisfaction
UNION ALL SELECT 'Litiges',       COUNT(*) FROM litige;
