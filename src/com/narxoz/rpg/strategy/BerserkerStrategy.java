package com.narxoz.rpg.strategy;

public class BerserkerStrategy implements CombatStrategy {

    @Override
    public int calculateDamage(int basePower) {
        return (int)(basePower * 1.5);
    }

    @Override
    public int calculateDefense(int baseDefense) {
        return (int)(baseDefense * 0.6);
    }

    @Override
    public String getName() {
        return "Berserker";
    }
}