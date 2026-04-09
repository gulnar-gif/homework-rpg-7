package com.narxoz.rpg.observer;

import java.util.Random;

public class LootDropper implements GameObserver {

    private final Random random = new Random();

    private final String[] phaseLoot = {
            "Ancient Coin",
            "Shadow Gem",
            "Cursed Fang",
            "Bone Charm"
    };

    private final String[] finalLoot = {
            "Boss Crown",
            "Legendary Sword",
            "Dragonheart Armor",
            "Orb of Ruin"
    };

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED) {
            String loot = phaseLoot[random.nextInt(phaseLoot.length)];
            System.out.println("[LootDropper] Phase loot dropped: " + loot);
        }

        if (event.getType() == GameEventType.BOSS_DEFEATED) {
            String loot = finalLoot[random.nextInt(finalLoot.length)];
            System.out.println("[LootDropper] Final boss loot dropped: " + loot);
        }
    }
}