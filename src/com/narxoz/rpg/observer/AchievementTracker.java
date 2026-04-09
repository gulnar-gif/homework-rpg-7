package com.narxoz.rpg.observer;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker implements GameObserver {

    private final Set<String> unlocked = new HashSet<>();
    private int totalAttacks = 0;

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.ATTACK_LANDED) {
            totalAttacks++;
            unlock("First Blood", totalAttacks >= 1);
            unlock("Combo Fighter", totalAttacks >= 5);
            unlock("Relentless Striker", totalAttacks >= 10);
        }

        if (event.getType() == GameEventType.HERO_DIED) {
            unlock("A Hero Has Fallen", true);
        }

        if (event.getType() == GameEventType.BOSS_DEFEATED) {
            unlock("Boss Slayer", true);
        }
    }

    private void unlock(String achievementName, boolean condition) {
        if (condition && !unlocked.contains(achievementName)) {
            unlocked.add(achievementName);
            System.out.println("[ACHIEVEMENT UNLOCKED] " + achievementName);
        }
    }
}