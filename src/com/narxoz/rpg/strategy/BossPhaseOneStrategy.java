package com.narxoz.rpg.strategy;

public class BossPhaseOneStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return (int) (basePower * 1.1);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int) (baseDefense * 1.2);
    }

    @Override
    public String getName() {
        return "Phase 1 - Calculated";
    }
}