package com.narxoz.rpg.boss;

import com.narxoz.rpg.observer.EventPublisher;
import com.narxoz.rpg.observer.GameEvent;
import com.narxoz.rpg.observer.GameEventType;
import com.narxoz.rpg.observer.GameObserver;
import com.narxoz.rpg.strategy.BossPhaseOneStrategy;
import com.narxoz.rpg.strategy.BossPhaseThreeStrategy;
import com.narxoz.rpg.strategy.BossPhaseTwoStrategy;
import com.narxoz.rpg.strategy.CombatStrategy;

public class DungeonBoss implements GameObserver {

    private final String name;
    private final int maxHp;
    private int hp;
    private final int attackPower;
    private final int defense;
    private int currentPhase;
    private CombatStrategy strategy;
    private final EventPublisher publisher;

    public DungeonBoss(String name, int maxHp, int attackPower, int defense, EventPublisher publisher) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.publisher = publisher;
        this.currentPhase = 1;
        this.strategy = new BossPhaseOneStrategy();
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public CombatStrategy getStrategy() {
        return strategy;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getEffectiveDamage() {
        return strategy.calculateDamage(attackPower);
    }

    public int getEffectiveDefense() {
        return strategy.calculateDefense(defense);
    }

    public void takeDamage(int amount) {
        int oldHp = hp;
        hp = Math.max(0, hp - amount);

        double oldPercent = (oldHp * 100.0) / maxHp;
        double newPercent = (hp * 100.0) / maxHp;

        if (oldPercent > 60 && newPercent <= 60 && hp > 0) {
            publisher.notifyObservers(
                    new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 2)
            );
        }

        if (oldPercent > 30 && newPercent <= 30 && hp > 0) {
            publisher.notifyObservers(
                    new GameEvent(GameEventType.BOSS_PHASE_CHANGED, name, 3)
            );
        }

        if (hp == 0) {
            publisher.notifyObservers(
                    new GameEvent(GameEventType.BOSS_DEFEATED, name, 0)
            );
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getType() == GameEventType.BOSS_PHASE_CHANGED
                && event.getSourceName().equals(name)) {

            int phase = event.getValue();
            currentPhase = phase;

            if (phase == 2) {
                strategy = new BossPhaseTwoStrategy();
            } else if (phase == 3) {
                strategy = new BossPhaseThreeStrategy();
            } else {
                strategy = new BossPhaseOneStrategy();
            }

            System.out.println("[Boss] " + name + " switched strategy to " + strategy.getName());
        }
    }
}