package me.numin.spirits.ability.light;

import me.numin.spirits.utilities.Methods;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.LightAbility;
import me.numin.spirits.ability.api.SpiritAbility;

public class Wish extends LightAbility {
	
	private static String path = "Abilities.Spirits.LightSpirit.Wish.";
	FileConfiguration config = Spirits.plugin.getConfig();
	
	private long cooldown;
	private long chargeTime;
	private long waitDuration;

	
	private boolean charged;
	private boolean wished;
	private long time;
	private double tickTime;
	private double absorptionHealthRatio;
	private int healthBoostDuration;
	private int healthBoostPower;
	private int regenerationDuration;
	private int regenerationPower;
	private long wishDuration;
	
	public Wish(Player player) {
		super(player);

		if (bPlayer.isOnCooldown(this)) {
			return;
		}
		
		cooldown = config.getLong(path + "Cooldown");
		chargeTime = config.getLong(path + "ChargeTime");
		waitDuration = config.getLong(path + "WaitDuration");
		absorptionHealthRatio = config.getDouble(path + "AbsorptionHealthRatio");
		healthBoostDuration = config.getInt(path + "HealthBoostDuration");
		healthBoostPower = config.getInt(path + "HealthBoostPower");
		regenerationDuration = config.getInt(path + "RegenerationDuration");
		regenerationPower = config.getInt(path + "RegenerationPower");
		wishDuration = config.getLong(path + "WishDuration");
		
		time = System.currentTimeMillis();
		
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 0.5F, 1F);
		
		start();
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
		return "Wish";
	}

	@Override
	public boolean isSneakAbility() {
		return true;
	}

	@Override
	public void progress() {
		if (player.isSneaking()) {
			if (System.currentTimeMillis() > time + chargeTime) {
				if (!wished) {
					charged = true;
					Methods.displaySpellInstantParticle(player.getLocation(), 5, 0.2F, 0.2F, 0.2F, 0.1F);
				}
			} else {
				ParticleEffect.ENCHANTMENT_TABLE.display(player.getLocation().add(0, 1, 0), 5, 0.5F, 0.5F, 0.5F, 0.2F);
			}
		} else {
			if (charged) {
				bPlayer.addCooldown(this);
				wish();
			} else {
				remove();
				return;
			}
		}
	}
	
	private void wish() {
		wished = true;
		tickTime += 0.05;
		long time = (long) (tickTime * 1000);
		if (time >= waitDuration) {

			
			
			for (int i = 0; i < 10 ; i++) {
				ParticleEffect.END_ROD.display(player.getLocation().add(0, 1, 0), 5, 0.3F, 0.3F, 0.3F, 0.2F);
				ParticleEffect.ENCHANTMENT_TABLE.display(player.getLocation().add(0, 1, 0), 5, 0.3F, 0.3F, 0.3F, 0.2F);
			}
			
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1.2F);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1F, 1F);
			
			player.sendMessage(SpiritElement.LIGHT.getColor() + "Your wish came true!");
			// if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()/3 - player.getHealth()/1.2 >= 0) {}
			if (Math.round(player.getAttribute(Attribute.MAX_HEALTH).getValue() * absorptionHealthRatio - player.getHealth()) > 0) {

			    
			    double absorptionAmount = player.getAttribute(Attribute.MAX_HEALTH).getValue() * absorptionHealthRatio - player.getHealth();
			    int roundedAbsorptionAmount = (int) Math.round(absorptionAmount);
			    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 9999, 3, true, false));
			    player.setAbsorptionAmount(roundedAbsorptionAmount);
			    System.out.println("New absorption amount set to: " + player.getAbsorptionAmount());
			}
		    
		        	
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, healthBoostDuration, healthBoostPower));
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenerationDuration, regenerationPower));
	       
	        new BukkitRunnable() {
	        	@Override
	        	public void run() {
	        		player.setAbsorptionAmount(0);
	        	}
	        }.runTaskLater(ProjectKorra.plugin, wishDuration);
	        
			remove();
			return;
		} else if (tickTime <= 0.05) {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.3F, 1F);
			
			for (int i = 0; i < 8; i++) {
				player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.FIREWORK_ROCKET);
			}
			
			player.sendMessage(SpiritElement.LIGHT.getColor() + "You made a wish!");
		}
	}
    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean(path + ".Enabled", true);
    }

	@Override
	public String getAbilityType() {
		return SpiritAbility.UTILITY;
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
