package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class HeroStatusMonitor implements GameObserver {

    private final List<Hero> heroes;

    public HeroStatusMonitor(List<Hero> heroes) {
        this.heroes = heroes;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.HERO_LOW_HP
                || event.getType() == GameEventType.HERO_DIED) {
            printStatus();
        }
    }

    private void printStatus() {
        System.out.println("[HeroStatusMonitor] Current hero status:");
        for (Hero hero : heroes) {
            String state;

            if (!hero.isAlive()) {
                state = "DEAD";
            } else if (hero.getHp() < hero.getMaxHp() * 0.3) {
                state = "LOW HP";
            } else {
                state = "OK";
            }

            System.out.println("- " + hero.getName()
                    + " | HP: " + hero.getHp() + "/" + hero.getMaxHp()
                    + " | State: " + state
                    + " | Strategy: " + hero.getStrategy().getName());
        }
    }
}