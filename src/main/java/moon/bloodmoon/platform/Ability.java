package moon.bloodmoon.platform;

public enum Ability {
    BLOOD_DOME,
    BLOOD_BOLOTO,
    BLOOD_CAPTURE,
    GROUP_EXPLOSION,
    BLOOD_PULSE,
    BLOOD_DISARM,
    BLOOD_RITUAL,
    BLOOD_HOLE,
    BLOOD_FIRE,
    BLOOD_INPUT,
    BLOOD_PUSH,
    EXPLODE,
    BLOOD_INFECTION,
    BLOOD_EXPLOSION,
    BLOOD_FOG,
    BLOOD_BEATING_ARROW,
    BLOOD_SUFFOCATE,
    BLOOD_SPAWN,
    BLOOD_FRENZY,
    BLOOD_CURSE;

    private double chance;

    public double getChance() {
        return this.chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
