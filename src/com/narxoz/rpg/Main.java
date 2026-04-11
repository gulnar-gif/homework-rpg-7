package com.narxoz.rpg;

import com.narxoz.rpg.boss.DungeonBoss;
import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.engine.DungeonEngine;
import com.narxoz.rpg.engine.EncounterResult;
import com.narxoz.rpg.observer.*;
import com.narxoz.rpg.strategy.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EventPublisher publisher = new EventPublisher();

        List<Hero> heroes = new ArrayList<>();

        Hero hero1 = new Hero("Arlan", 160, 36, 14, new BerserkerStrategy());
        Hero hero2 = new Hero("Selene", 180, 28, 20, new GuardianStrategy());
        Hero hero3 = new Hero("Doran", 170, 32, 16, new BalancedStrategy());

        heroes.add(hero1);
        heroes.add(hero2);
        heroes.add(hero3);

        DungeonBoss boss = new DungeonBoss("The Cursed Lich", 420, 34, 10, publisher);

        BattleLogger logger = new BattleLogger();
        AchievementTracker achievements = new AchievementTracker();
        PartySupport support = new PartySupport(heroes, 20);
        HeroStatusMonitor monitor = new HeroStatusMonitor(heroes);
        LootDropper loot = new LootDropper();

        publisher.registerObserver(logger);
        publisher.registerObserver(achievements);
        publisher.registerObserver(support);
        publisher.registerObserver(monitor);
        publisher.registerObserver(loot);
        publisher.registerObserver(boss);

        DungeonEngine engine = new DungeonEngine(heroes, boss, publisher, 20);

        EncounterResult result = engine.runEncounter();

        System.out.println("\n========== FINAL RESULT ==========");
        System.out.println("Heroes won: " + result.isHeroesWon());
        System.out.println("Rounds played: " + result.getRoundsPlayed());
        System.out.println("Surviving heroes: " + result.getSurvivingHeroes());
    }
}
