package com.avengers.javadesheros.config;

import com.avengers.javadesheros.model.*;
import com.avengers.javadesheros.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Injecte des données de test Avengers dans la BDD.
 * S'exécute uniquement si la table civil est vide.
 * Ordre 2 → s'exécute après DataInitializer (ordre 1).
 */
@Component
@Order(2)
public class TestDataInitializer implements CommandLineRunner {

    private final CivilRepository civilRepo;
    private final OrganisationRepository organisationRepo;
    private final SuperheroRepository superheroRepo;
    private final SupervillainRepository supervillainRepo;
    private final IncidentRepository incidentRepo;
    private final MissionRepository missionRepo;
    private final CriseRepository criseRepo;
    private final SatisfactionRepository satisfactionRepo;
    private final LitigeRepository litigeRepo;

    public TestDataInitializer(CivilRepository civilRepo,
                               OrganisationRepository organisationRepo,
                               SuperheroRepository superheroRepo,
                               SupervillainRepository supervillainRepo,
                               IncidentRepository incidentRepo,
                               MissionRepository missionRepo,
                               CriseRepository criseRepo,
                               SatisfactionRepository satisfactionRepo,
                               LitigeRepository litigeRepo) {
        this.civilRepo        = civilRepo;
        this.organisationRepo = organisationRepo;
        this.superheroRepo    = superheroRepo;
        this.supervillainRepo = supervillainRepo;
        this.incidentRepo     = incidentRepo;
        this.missionRepo      = missionRepo;
        this.criseRepo        = criseRepo;
        this.satisfactionRepo = satisfactionRepo;
        this.litigeRepo       = litigeRepo;
    }

    @Override
    public void run(String... args) {
        if (civilRepo.count() > 0) {
            System.out.println("ℹ️  Données de test déjà présentes — skip.");
            return;
        }

        System.out.println("🦸 Injection des données de test Avengers...");

        // ─────────────────────────────────────────────
        // 1. CIVILS
        // ─────────────────────────────────────────────
        Civil tony    = civil("Stark",    "Tony",    "M.",  "10880 Malibu Point, Malibu CA",  "tony@starkindustries.com",  "1970-05-29", "+1 310-555-0100", "Américaine");
        Civil steve   = civil("Rogers",   "Steve",   "M.",  "569 Leaman Place, Brooklyn NY",  "steve.rogers@shield.gov",   "1918-07-04", "+1 718-555-0101", "Américaine");
        Civil bruce   = civil("Banner",   "Bruce",   "M.",  "New York, NY",                   "b.banner@science.edu",      "1969-12-18", "+1 212-555-0102", "Américaine");
        Civil natasha = civil("Romanoff", "Natasha", "Mme", "Moscou / New York",              "black.widow@shield.gov",    "1984-11-22", "+7 495-555-0103", "Russe");
        Civil clint   = civil("Barton",   "Clint",   "M.",  "Waverly, Iowa",                  "hawkeye@avengers.com",      "1971-01-07", "+1 515-555-0104", "Américaine");
        Civil scott   = civil("Lang",     "Scott",   "M.",  "San Francisco, CA",              "scott.lang@xcon.com",       "1972-04-08", "+1 415-555-0105", "Américaine");
        Civil wanda   = civil("Maximoff", "Wanda",   "Mme", "Sokovia",                        "wanda@avengers.com",        "1989-02-10", "+421 555-0106",   "Sokovienne");
        Civil peter   = civil("Parker",   "Peter",   "M.",  "20 Ingram Street, Queens NY",    "peter.parker@midtown.edu",  "2001-08-10", "+1 718-555-0107", "Américaine");
        Civil nick    = civil("Fury",     "Nick",    "M.",  "Washington D.C.",                "n.fury@shield.gov",         "1950-09-23", "+1 202-555-0108", "Américaine");
        Civil maria   = civil("Hill",     "Maria",   "Mme", "Washington D.C.",                "m.hill@shield.gov",         "1982-04-04", "+1 202-555-0109", "Américaine");
        Civil thor    = civil("Odinson",  "Thor",    "M.",  "Asgard / New Asgard, Norvège",   "thor@asgard.com",           "0964-11-10", "+47 555-0110",    "Asgardienne");
        Civil vision  = civil("Vision",   "Vision",  "M.",  "Wandavision, NJ",                "vision@avengers.com",       "2015-05-01", null,              "Synthézoïde");
        Civil loki    = civil("Laufeyson","Loki",    "M.",  "Asgard",                         "loki@asgard.com",           "0965-12-17", null,              "Asgardienne");
        Civil pepper  = civil("Potts",    "Pepper",  "Mme", "10880 Malibu Point, Malibu CA",  "pepper@starkindustries.com","1974-05-28", "+1 310-555-0111", "Américaine");

        // ─────────────────────────────────────────────
        // 2. ORGANISATIONS
        // ─────────────────────────────────────────────
        Organisation shield = organisation("S.H.I.E.L.D.",
                "Helicarrier / Washington D.C.",
                nick,
                Set.of(nick, maria, natasha, clint),
                "Organisation mondiale de maintien de la paix");

        Organisation starkIndustries = organisation("Stark Industries",
                "200 Park Avenue, New York NY",
                pepper,
                Set.of(tony, pepper),
                "Entreprise technologique de défense et d'innovation");

        Organisation avengers = organisation("Les Avengers",
                "Avengers Tower, New York NY",
                tony,
                Set.of(tony, steve, bruce, natasha, clint, wanda, peter, thor, vision),
                "Équipe de super-héros de niveau mondial");

        // ─────────────────────────────────────────────
        // 3. SUPER-HÉROS
        // ─────────────────────────────────────────────
        Superhero ironMan  = hero("Iron Man",       "Armure propulsée, IA JARVIS, génie en ingénierie", "Vulnérabilité cardiaque, arrogance",         tony,    4.8);
        Superhero capAm    = hero("Captain America","Super-soldat, bouclier en vibranium, leadership",  "Idéalisme parfois rigide",                   steve,   4.9);
        Superhero hulk     = hero("Hulk",           "Force surhumaine, régénération, résistance extrême","Perte de contrôle sous forme Hulk",          bruce,   4.2);
        Superhero bWidow   = hero("Black Widow",    "Arts martiaux, espionnage, Veuve Noire",           "Pas de super-pouvoirs physiques",             natasha, 4.6);
        Superhero hawkeye  = hero("Hawkeye",        "Tir à l'arc parfait, flèches spéciales, tactique", "Pas de pouvoirs, humain ordinaire",           clint,   4.5);
        Superhero antMan   = hero("Ant-Man",        "Réduction/agrandissement grâce au Pym Particles",  "Perd ses capacités sans la combinaison",      scott,   4.0);
        Superhero scarlett = hero("Scarlet Witch",  "Télékinesie, manipulation de la réalité",          "Émotions instables",                          wanda,   4.7);
        Superhero spidey   = hero("Spider-Man",     "Toile, sens de l'araignée, agilité surhumaine",    "Jeune et inexpérimenté",                      peter,   4.3);

        // ─────────────────────────────────────────────
        // 4. SUPER-VILAINS
        // ─────────────────────────────────────────────
        villain("Thanos",   "Force infinie, Gant de l'Infini",    "Sentiments pour les siens",     null,  95);
        villain("Loki",     "Illusions, magie asgardienne",       "Recherche d'approbation",       loki,  72);
        villain("Ultron",   "IA militarisée, armées de robots",   "Dépend des réseaux informatiques", null, 88);
        villain("Le Vautour","Technologie exo-squelette",         "Motivations financières",       null,  45);

        // ─────────────────────────────────────────────
        // 5. INCIDENTS
        // ─────────────────────────────────────────────
        Incident incAtaque = incident(
                "Attaque alien sur Manhattan",
                "Une flotte d'extraterrestres Chitauri a ouvert un portail au-dessus de Stark Tower.",
                "Manhattan, New York",
                Incident.Statut.transforme_en_mission,
                nick, null, null);

        Incident incRobot = incident(
                "Évasion d'Ultron depuis les serveurs SHIELD",
                "L'IA Ultron a pris le contrôle des systèmes de défense de Stark Industries.",
                "Avengers Tower, New York",
                Incident.Statut.transforme_en_mission,
                null, starkIndustries, ironMan);

        Incident incVol = incident(
                "Tentative de vol du Pym Particles",
                "Individus armés repérés dans les entrepôts Cross Technologies.",
                "San Francisco, CA",
                Incident.Statut.en_attente,
                null, null, antMan);

        // ─────────────────────────────────────────────
        // 6. MISSIONS
        // ─────────────────────────────────────────────
        Mission mBataille = mission(
                "Opération : Bataille de New York",
                Mission.Nature.sauvetage,
                Mission.NiveauGravite.extinction_planete,
                Mission.NiveauUrgence.critique,
                Mission.Statut.terminee,
                LocalDateTime.now().minusMonths(6),
                LocalDateTime.now().minusMonths(6).plusDays(1),
                "New York → Portail Chitauri",
                "Fermer le portail et neutraliser la flotte Chitauri.",
                incAtaque,
                Set.of(ironMan, capAm, hulk, bWidow, hawkeye));

        Mission mUltron = mission(
                "Opération : Age of Ultron",
                Mission.Nature.sauvetage,
                Mission.NiveauGravite.extinction_planete,
                Mission.NiveauUrgence.critique,
                Mission.Statut.terminee,
                LocalDateTime.now().minusMonths(3),
                LocalDateTime.now().minusMonths(3).plusDays(3),
                "Avengers Tower → Sokovia",
                "Détruire Ultron et sauver la population de Sokovia.",
                incRobot,
                Set.of(ironMan, capAm, hulk, bWidow, hawkeye, scarlett));

        Mission mAntMan = mission(
                "Opération : Vol Quantique",
                Mission.Nature.prospection,
                Mission.NiveauGravite.dangereux_infrastructures,
                Mission.NiveauUrgence.eleve,
                Mission.Statut.en_cours,
                LocalDateTime.now().minusDays(2),
                null,
                "San Francisco → Cross Technologies",
                "Infiltrer Cross Technologies et récupérer le Pym Particles avant Darren Cross.",
                incVol,
                Set.of(antMan));

        Mission mSpidey = mission(
                "Opération : Retour au Lycée",
                Mission.Nature.sauvetage,
                Mission.NiveauGravite.dangereux_population,
                Mission.NiveauUrgence.normal,
                Mission.Statut.planifiee,
                LocalDateTime.now().plusDays(5),
                null,
                "Queens, New York",
                "Surveiller le Vautour et protéger les civils.",
                null,
                Set.of(spidey, ironMan));

        // ─────────────────────────────────────────────
        // 7. CRISES
        // ─────────────────────────────────────────────
        crise("Procès contre Iron Man — dommages collatéraux",
                Crise.TypeCrise.proces,
                Crise.NiveauAlerte.eleve,
                Crise.Statut.en_cours,
                mBataille,
                "Le Congrès américain poursuit Tony Stark pour les dégâts causés lors de la Bataille de New York.");

        crise("Démasquage de Spider-Man par la presse",
                Crise.TypeCrise.hero_demasque,
                Crise.NiveauAlerte.critique,
                Crise.Statut.ouverte,
                null,
                "Le Daily Bugle affirme détenir l'identité secrète de Spider-Man.");

        crise("Destruction de Sokovia — couverture médiatique",
                Crise.TypeCrise.dommages_collateraux,
                Crise.NiveauAlerte.modere,
                Crise.Statut.resolue,
                mUltron,
                "Vague de critiques internationale suite à la destruction de Sokovia par Ultron.");

        // ─────────────────────────────────────────────
        // 8. SATISFACTIONS (uniquement missions terminées)
        // ─────────────────────────────────────────────
        satisfaction(mBataille, maria,  4.5, "Excellent travail malgré les destructions importantes.");
        satisfaction(mBataille, pepper, 5.0, "Iron Man a sauvé la planète. Rien à redire.");
        satisfaction(mBataille, loki,   1.0, "Mission personnellement catastrophique pour moi.");
        satisfaction(mUltron,   wanda,  4.0, "Résultat correct mais Sokovia n'aurait pas dû être détruite.");
        satisfaction(mUltron,   maria,  4.8, "Réactivité exemplaire de l'équipe.");

        // ─────────────────────────────────────────────
        // 9. LITIGES
        // ─────────────────────────────────────────────
        litige("Dégâts immobiliers — Bataille de New York",
                Litige.TypeLitige.degats_materiels,
                Litige.Statut.en_instruction,
                mBataille, tony, ironMan,
                "Plusieurs immeubles de Manhattan détruits lors des combats contre les Chitauri.",
                null, null);

        litige("Atteinte physique — Hulk vs Harlem",
                Litige.TypeLitige.atteinte_physique,
                Litige.Statut.resolu,
                null, bruce, hulk,
                "Blessures civiles lors de la transformation de Bruce Banner à Harlem.",
                "Indemnisation accordée aux victimes. Dossier clôturé.",
                LocalDateTime.now().minusMonths(1));

        litige("Propriété intellectuelle — Stark Industries",
                Litige.TypeLitige.propriete,
                Litige.Statut.ouvert,
                null, tony, null,
                "Brevet sur les réacteurs arc revendiqué par un concurrent.",
                null, null);

        System.out.println("✅ Données de test injectées avec succès !");
        System.out.println("   → 14 civils, 3 organisations, 8 héros, 4 vilains");
        System.out.println("   → 3 incidents, 4 missions, 3 crises, 5 satisfactions, 3 litiges");
    }

    // ──────────────────────────────────────────────────────────────
    // Méthodes helpers
    // ──────────────────────────────────────────────────────────────

    private Civil civil(String nom, String prenom, String civilite,
                        String adresse, String email, String naissance,
                        String telephone, String nationalite) {
        Civil c = new Civil();
        c.setNom(nom);
        c.setPrenom(prenom);
        c.setCivilite(civilite);
        c.setAdressePostale(adresse);
        c.setEmail(email);
        c.setDateNaissance(LocalDate.parse(naissance));
        c.setTelephone(telephone);
        c.setNationalite(nationalite);
        return civilRepo.save(c);
    }

    private Organisation organisation(String nom, String siege, Civil dirigeant,
                                      Set<Civil> membres, String commentaire) {
        Organisation o = new Organisation();
        o.setNom(nom);
        o.setAdresseSiege(siege);
        o.setDirigeant(dirigeant);
        o.setMembres(membres);
        o.setCommentaire(commentaire);
        return organisationRepo.save(o);
    }

    private Superhero hero(String nom, String pouvoir, String pointFaible,
                           Civil identite, double score) {
        Superhero h = new Superhero();
        h.setNom(nom);
        h.setPouvoir(pouvoir);
        h.setPointFaible(pointFaible);
        h.setIdentiteSecrete(identite);
        h.setScore(score);
        return superheroRepo.save(h);
    }

    private void villain(String nom, String pouvoir, String pointFaible,
                         Civil identite, int degre) {
        Supervillain v = new Supervillain();
        v.setNom(nom);
        v.setPouvoir(pouvoir);
        v.setPointFaible(pointFaible);
        v.setIdentiteSecrete(identite);
        v.setDegreMalveillance(degre);
        supervillainRepo.save(v);
    }

    private Incident incident(String titre, String description, String lieu,
                              Incident.Statut statut, Civil declarantCivil,
                              Organisation declarantOrg, Superhero declarantHero) {
        Incident i = new Incident();
        i.setTitre(titre);
        i.setDescription(description);
        i.setLieu(lieu);
        i.setStatut(statut);
        i.setDeclarantCivil(declarantCivil);
        i.setDeclarantOrganisation(declarantOrg);
        i.setDeclarantHero(declarantHero);
        return incidentRepo.save(i);
    }

    private Mission mission(String titre, Mission.Nature nature,
                            Mission.NiveauGravite gravite, Mission.NiveauUrgence urgence,
                            Mission.Statut statut,
                            LocalDateTime debut, LocalDateTime fin,
                            String itineraire, String infosComplementaires,
                            Incident incident, Set<Superhero> heroes) {
        Mission m = new Mission();
        m.setTitre(titre);
        m.setNature(nature);
        m.setNiveauGravite(gravite);
        m.setNiveauUrgence(urgence);
        m.setStatut(statut);
        m.setDateDebut(debut);
        m.setDateFin(fin);
        m.setItineraire(itineraire);
        m.setInfosComplementaires(infosComplementaires);
        m.setIncident(incident);
        m.setSuperheros(heroes);
        return missionRepo.save(m);
    }

    private void crise(String titre, Crise.TypeCrise type, Crise.NiveauAlerte alerte,
                       Crise.Statut statut, Mission mission, String description) {
        Crise c = new Crise();
        c.setTitre(titre);
        c.setTypeCrise(type);
        c.setNiveauAlerte(alerte);
        c.setStatut(statut);
        c.setMission(mission);
        c.setDescription(description);
        criseRepo.save(c);
    }

    private void satisfaction(Mission mission, Civil civil, double note, String commentaire) {
        Satisfaction s = new Satisfaction();
        s.setMission(mission);
        s.setCivil(civil);
        s.setNote(note);
        s.setCommentaire(commentaire);
        satisfactionRepo.save(s);
    }

    private void litige(String titre, Litige.TypeLitige type, Litige.Statut statut,
                        Mission mission, Civil plaignant, Superhero superhero,
                        String description, String resolution, LocalDateTime dateCloture) {
        Litige l = new Litige();
        l.setTitre(titre);
        l.setTypeLitige(type);
        l.setStatut(statut);
        l.setMission(mission);
        l.setPlaignant(plaignant);
        l.setSuperhero(superhero);
        l.setDescription(description);
        l.setResolution(resolution);
        l.setDateCloture(dateCloture);
        litigeRepo.save(l);
    }
}
