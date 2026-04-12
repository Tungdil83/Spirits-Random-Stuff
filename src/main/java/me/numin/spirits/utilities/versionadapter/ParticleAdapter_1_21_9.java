package me.numin.spirits.utilities.versionadapter;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleAdapter_1_21_9 implements ParticleAdapter {

    @Override
    public void displaySpellInstantParticle(Location location, int amount, double offsetX, double offsetY, double offsetZ, double extra) {
        location.getWorld().spawnParticle(Particle.valueOf("INSTANT_EFFECT"), location, amount, offsetX, offsetY, offsetZ, extra, new Particle.Spell(Color.WHITE, 1f));
    }
}

//ParticleAdapter by https://github.com/CozmycDev/