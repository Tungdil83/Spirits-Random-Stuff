package me.numin.spirits;

import me.numin.spirits.utilities.versionadapter.ParticleAdapter;
import me.numin.spirits.utilities.versionadapter.ParticleAdapterFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.CollisionInitializer;

import me.numin.spirits.ability.dark.DarkBlast;
import me.numin.spirits.ability.dark.Shackle;
import me.numin.spirits.ability.dark.Strike;
import me.numin.spirits.ability.light.LightBlast;
import me.numin.spirits.ability.light.Shelter;
import me.numin.spirits.config.Config;
import me.numin.spirits.listeners.Abilities;
import me.numin.spirits.listeners.PKEvents;
import me.numin.spirits.listeners.Passives;
import me.numin.spirits.utilities.SpiritPlaceholder;

public final class Spirits extends JavaPlugin {

    public static Spirits plugin;

    private ParticleAdapter particleAdapter;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Init config");
        new Config(this);
        getLogger().info("Init abilities");
        CoreAbility.registerPluginAbilities(plugin, "me.numin.spirits.ability");

        registerListeners();
        registerCollisions();

        ParticleAdapterFactory particleAdapterFactory = new ParticleAdapterFactory();
        particleAdapter = particleAdapterFactory.getAdapter();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SpiritPlaceholder().register();
        }

        plugin.getLogger().info("Successfully enabled Spirits.");
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("Successfully disabled Spirits.");
    }

    public static Spirits getInstance() {
        return plugin;
    }

    //TODO: collision system needs testing
    private void registerCollisions() {
        CollisionInitializer collisionInitializer = ProjectKorra.getCollisionInitializer();
        collisionInitializer.addSmallAbility(CoreAbility.getAbility(DarkBlast.class));
        collisionInitializer.addSmallAbility(CoreAbility.getAbility(Shackle.class));
        collisionInitializer.addSmallAbility(CoreAbility.getAbility(Strike.class));
        collisionInitializer.addSmallAbility(CoreAbility.getAbility(LightBlast.class));
        collisionInitializer.addLargeAbility(CoreAbility.getAbility(Shelter.class));
        collisionInitializer.initializeDefaultCollisions();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Abilities(), this);
        getServer().getPluginManager().registerEvents(new Passives(), this);
        getServer().getPluginManager().registerEvents(new PKEvents(), this);
    }

    public ParticleAdapter getParticleAdapter() {
        return this.particleAdapter;
    }
}
