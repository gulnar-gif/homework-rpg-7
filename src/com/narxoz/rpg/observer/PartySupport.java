package com.narxoz.rpg.observer;

import com.narxoz.rpg.combatant.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PartySupport implements GameObserver {

    private final List<Hero> heroes;
    private final int healAmount;
    private final Random random = new Random();

    public PartySupport(List<Hero> heroes, int healAmount) {
        this.heroes = heroes;
        this.healAmount = healAmount;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() != GameEventType.HERO_LOW_HP) {
            return;
        }

        List<Hero> livingHeroes = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }

        if (livingHeroes.isEmpty()) {
            return;
        }

        Hero target = livingHeroes.get(random.nextInt(livingHeroes.size()));
        target.heal(healAmount);

        System.out.println("[PartySupport] " + target.getName()
                + " was healed for " + healAmount
                + " HP. Current HP: " + target.getHp() + "/" + target.getMaxHp());
    }
}