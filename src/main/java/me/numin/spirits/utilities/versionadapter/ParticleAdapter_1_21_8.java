package me.numin.spirits.utilities.versionadapter;

import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleAdapter_1_21_8 implements ParticleAdapter {

    @Override
    public void displaySpellInstantParticle(Location location, int amount, double offsetX, double offsetY, double offsetZ, double extra) {
        location.getWorld().spawnParticle(Particle.valueOf("SPELL_INSTANT"), location, amount, offsetX, offsetY, offsetZ, extra);
    }
}

//ParticleAdapter by https://github.com/CozmycDev/