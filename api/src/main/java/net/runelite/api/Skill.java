package net.runelite.api;

import com.meteor.api.R;

public enum Skill {
    ATTACK(0, R.drawable.attack),
    DEFENCE(1, R.drawable.defence),
    STRENGTH(2, R.drawable.strength),
    HITPOINTS(3, R.drawable.hitpoints),
    RANGED(4, R.drawable.ranged),
    PRAYER(5, R.drawable.prayer),
    MAGIC(6, R.drawable.magic),
    COOKING(7, R.drawable.cooking),
    WOODCUTTING(8, R.drawable.woodcutting),

    FISHING(10, R.drawable.fishing),
    FIREMAKING(11, R.drawable.firemaking),

    SMITHING(13, R.drawable.smithing),
    MINING(14, R.drawable.mining),


    THIEVING(17, R.drawable.thieving),
    AGILITY(999, R.drawable.agility);


    public int id;
    public int iconResource;
    public String name;

    Skill(int id, int iconResource) {
        this.id = id;
        this.iconResource = iconResource;
        this.name = name().toLowerCase();
    }

    public static Skill from(int id) {
        for (Skill skill : values()) {
            if (skill.id == id) {
                return skill;
            }
        }
        throw new IllegalArgumentException("Invalid skill id: " + id);
    }

    public int smallIconResource() {
        return iconResource;
    }
}
