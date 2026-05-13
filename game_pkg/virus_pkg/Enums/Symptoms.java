package game_pkg.virus_pkg.Enums;

public enum Symptoms {
    
    // SYMPTOMS (name, dnaCost, infectivity, severity, lethality)
    NAUSEA(2, 0.05, 0.05, 0),
    COUGHING(4, 0.15, 0.05, 0),
    RASH(3, 0.10, 0.05, 0),
    INSOMNIA(2, 0, 0.15, 0),
    CYSTS(2, 0.10, 0.10, 0),
    ANAEMIA(2, 0.05, 0.05, 0),
    VOMITING(3, 0.15, 0.05, 0),
    PNEUMONIA(3, 0.15, 0.10, 0),
    SNEEZING(5, 0.25, 0.05, 0),
    SWEATING(3, 0.10, 0.05, 0),
    PARANOIA(4, 0, 0.20, 0),
    HYPER_SENSITIVITY(2, 0.05, 0.10, 0),
    ABSCESSES(2, 0.20, 0.20, 0),
    HAEMOPHILIA(3, 0.20, 0.15, 0),
    PULMONARY_OEDEMA(7, 0.25, 0.20, 0.10),
    FEVER(9, 0.20, 0.15, 0.15),
    INFLAMMATION(5, 0.10, 0.10, 0.10),
    TUMOURS(11, 0.10, 0, 0.20),
    DIARRHOEA(6, 0.30, 0.20, 0.05),
    PULMONARY_FIBROSIS(6, 0.15, 0.15, 0.10),
    IMMUNE_SUPPRESSION(12, 0.10, 0.30, 0.20),
    SKIN_LESIONS(8, 0.55, 0.20, 0),
    SEIZURES(4, 0.05, 0.35, 0.15),
    PARALYSIS(10, 0.05, 0.25, 0.05),
    SYSTEMIC_INFECTION(17, 0.30, 0.35, 0.30),
    INTERNAL_HAEMORRHAGING(12, 0, 0.45, 0.35),
    DYSENTERY(19, 0.40, 0.75, 0.40),
    INSANITY(18, 0.30, 0.75, 0),
    NECROSIS(27, 0.50, 1.00, 0.65),
    HAEMORRHAGIC_SHOCK(23, 0, 0.75, 0.75),
    COMA(21, 0, 0.75, 0.10),
    TOTAL_ORGAN_FAILURE(28, 0, 1.00, 1.25);

    public final int dnaCost;
    public final double infectivityBonus;
    public final double severityBonus;
    public final double lethalityBonus;

    Symptoms(int dnaCost, double infectivityBonus, double severityBonus, double lethalityBonus){
        this.dnaCost = dnaCost;
        this.infectivityBonus = infectivityBonus;
        this.severityBonus = severityBonus;
        this.lethalityBonus = lethalityBonus;
    }
}
