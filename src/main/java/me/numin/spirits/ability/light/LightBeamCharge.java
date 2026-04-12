package me.numin.spirits.ability.light;

import java.util.Random;

import me.numin.spirits.utilities.Methods;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.LightAbility;
import me.numin.spirits.ability.api.SpiritAbility;

public class LightBeamCharge extends LightAbility {
	
	private static String path = "Abilities.Spirits.LightSpirit.LightBeam.";
	FileConfiguration config = Spirits.plugin.getConfig();
	
	private long chargeTime;
	private long cooldown;
	
	private long time;
	public boolean charged;
	
	Random rand = new Random();

	public LightBeamCharge(Player player) {
		super(player);

		chargeTime = config.getLong(path + "ChargeTime");
		cooldown = config.getLong(path + "Cooldown");
		
		time = System.currentTimeMillis();
		
		start();
	}
    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.LightBeam.Enabled");
    }
	@Override
	public boolean isHiddenAbility() {
		return true;
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
		return "LightBeamCharge";
	}

	@Override
	public boolean isSneakAbility() {
		return true;
	}

	@Override
	public void progress() {
		if (!bPlayer.canBendIgnoreBinds(this)) {
			remove();
			return;
		}
		if (!player.isOnline() || player.isDead()) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		if (player.isSneaking()) {
			if (System.currentTimeMillis() > time + chargeTime) {
				charged = true;
				Methods.displaySpellInstantParticle(player.getLocation(), 5, 0F, 0.2F, 0F, 0.05F);
			} else {
				ParticleEffect.ENCHANTMENT_TABLE.display(player.getLocation().add(0, 1, 0), 5, 0.3F, 0.3F, 0.3F, 0.2F);
			}
		} else {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}

	@Override
	public String getAbilityType() {
		return SpiritAbility.OFFENSE;
	}

	@Override
	public boolean isExplosiveAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIgniteAbility() {
		// TODO Auto-generated method stub
		return false;
	}
}
