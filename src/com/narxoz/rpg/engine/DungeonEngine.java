package com.narxoz.rpg.engine;

import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.strategy.GuardianStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DungeonEngine {

    private final List<Hero> heroes;
    private final DungeonBoss boss;
    private final EventPublisher publisher;
    private final int maxRounds;
    private final Set<String> lowHpTriggered = new HashSet<>();

    public DungeonEngine(List<Hero> heroes, DungeonBoss boss, EventPublisher publisher, int maxRounds) {
        this.heroes = heroes;
        this.boss = boss;
        this.publisher = publisher;
        this.maxRounds = maxRounds;
    }

    public EncounterResult runEncounter() {
        int round = 1;
        boolean switched = false;

        while (round <= maxRounds && boss.isAlive() && hasLivingHeroes()) {
            System.out.println("\n========== ROUND " + round + " ==========");

            if (round == 3 && !switched) {
                for (Hero hero : heroes) {
                    if (hero.isAlive()) {
                        hero.setStrategy(new GuardianStrategy());
                        System.out.println("[Engine] " + hero.getName()
                                + " switched strategy to " + hero.getStrategy().getName());
                        switched = true;
                        break;
                    }
                }
            }

            for (Hero hero : heroes) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }

                int heroDamage = hero.getStrategy().calculateDamage(hero.getAttackPower());
                int bossDefense = boss.getEffectiveDefense();
                int finalDamage = Math.max(0, heroDamage - bossDefense);

                boss.takeDamage(finalDamage);

                System.out.println(hero.getName()
                        + " attacks " + boss.getName()
                        + " using " + hero.getStrategy().getName()
                        + " and deals " + finalDamage
                        + " damage. Boss HP: " + boss.getHp() + "/" + boss.getMaxHp());

                publisher.notifyObservers(
                        new GameEvent(GameEventType.ATTACK_LANDED, hero.getName(), finalDamage)
                );
            }

            if (!boss.isAlive()) {
                break;
            }

            for (Hero hero : heroes) {
                if (!hero.isAlive()) {
                    continue;
                }

                int bossDamage = boss.getEffectiveDamage();
                int heroDefense = hero.getStrategy().calculateDefense(hero.getDefense());
                int finalDamage = Math.max(0, bossDamage - heroDefense);

                hero.takeDamage(finalDamage);

                System.out.println(boss.getName()
                        + " attacks " + hero.getName()
                        + " using " + boss.getStrategy().getName()
                        + " and deals " + finalDamage
                        + " damage. Hero HP: " + hero.getHp() + "/" + hero.getMaxHp());

                publisher.notifyObservers(
                        new GameEvent(GameEventType.ATTACK_LANDED, boss.getName(), finalDamage)
                );

                checkHeroEvents(hero);
            }

            round++;
        }

        boolean heroesWon = !boss.isAlive();
        int roundsPlayed = round - 1;
        int survivingHeroes = countLivingHeroes();

        return new EncounterResult(heroesWon, roundsPlayed, survivingHeroes);
    }

    private void checkHeroEvents(Hero hero) {
        if (!hero.isAlive()) {
            publisher.notifyObservers(
                    new GameEvent(GameEventType.HERO_DIED, hero.getName(), 0)
            );
            lowHpTriggered.remove(hero.getName());
            return;
        }

        boolean isLowHp = hero.getHp() < hero.getMaxHp() * 0.3;

        if (isLowHp && !lowHpTriggered.contains(hero.getName())) {
            publisher.notifyObservers(
                    new GameEvent(GameEventType.HERO_LOW_HP, hero.getName(), hero.getHp())
            );
            lowHpTriggered.add(hero.getName());
        }

        if (!isLowHp) {
            lowHpTriggered.remove(hero.getName());
        }
    }

    private boolean hasLivingHeroes() {
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private int countLivingHeroes() {
        int count = 0;
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                count++;
            }
        }
        return count;
    }
}