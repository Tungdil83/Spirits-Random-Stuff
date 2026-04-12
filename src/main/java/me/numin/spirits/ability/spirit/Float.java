package me.numin.spirits.ability.spirit;

import me.numin.spirits.utilities.Methods;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.SpiritAbility;

public class Float extends SpiritAbility {
	
	private static String path = "Abilities.Spirits.Neutral.Float.";
	FileConfiguration config = Spirits.plugin.getConfig();
	
	private long cooldown;
	private int floatPower;
	private long duration;
	
	private int floatDuration;
	private long time;

	public Float(Player player) {
		super(player);
		
		if (bPlayer.isOnCooldown(this)) {
			return;
		}
		
		cooldown = Spirits.plugin.getConfig().getLong(path + "Cooldown");
		duration = Spirits.plugin.getConfig().getInt(path + "FloatDuration");
		floatPower = Spirits.plugin.getConfig().getInt(path + "FloatPower");
		
		floatDuration = Math.toIntExact((duration * 1000) / 50);
		time = System.currentTimeMillis();
		
		start();
	}


	@Override
	public void progress() {
		Element lightSpirit = SpiritElement.LIGHT;
		Element darkSpirit = SpiritElement.DARK;
		
		if (bPlayer.hasElement(darkSpirit)) {
			ParticleEffect.SPELL_WITCH.display(player.getLocation(), 5, 0.2F, 0.2F, 0.2F, 0.2F);
			
		} else if (bPlayer.hasElement(lightSpirit)) {
			Methods.displaySpellInstantParticle(player.getLocation(), 5, 0.2F, 0.2F, 0.2F, 0.2F);
		} else {
			ParticleEffect.CRIT_MAGIC.display(player.getLocation(), 5, 0.2F, 0.2F, 0.2F, 0.2F);
		}
		
		if (System.currentTimeMillis() > time + duration) {
			remove();
			return;
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, floatDuration, floatPower));
		}
		bPlayer.addCooldown(this);
	}
	
	@Override
	public void remove() {
		super.remove();
		if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
			player.removePotionEffect(PotionEffectType.LEVITATION);
		}
	}


    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean(path + ".Enabled", true);
    }
    @Override
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public boolean isIgniteAbility() {
        return false;
    }
    
	@Override
	public String getAbilityType() {
		return UTILITY;
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public String getName() {
		return "Float";
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}
}
