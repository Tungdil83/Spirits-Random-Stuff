package me.numin.spirits.ability.lightspirit.passive;

import me.numin.spirits.utilities.Methods;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.LightAbility;
import me.numin.spirits.ability.api.SpiritAbility;

public class Afterglow extends LightAbility implements PassiveAbility {
	
	private static String path = "Abilities.Spirits.LightSpirit.Passive.Awakening.";
	FileConfiguration config = Spirits.plugin.getConfig();
	
	private long cooldown;
	private double damage;
	private double healAmount;
	private long duration;
	
	public Location location;
	
	private long time;

	public Afterglow(Player player, Location location) {
		super(player);
		
		if (!bPlayer.hasElement(SpiritElement.LIGHT)) {
			return;
		}
		
		time = System.currentTimeMillis();
		cooldown = config.getLong(path + "Cooldown");
		duration = config.getLong(path + "Duration");
		damage = config.getDouble(path + "Damage");
		healAmount = config.getDouble(path + "HealAmount");
		
		this.location = location;
		
		//boolean enabled = config.getBoolean("Abilities.Spirits.LightSpirit.Passive.Afterglow.Enabled");
		
		//if (enabled) {
		//System.out.println("Triggered Afterglow 2");
			start();
		//}
	}

    @Override
    public boolean isEnabled() {
        return Spirits.plugin.getConfig().getBoolean("Abilities.Spirits.LightSpirit.Passive.Afterglow.Enabled");
    }
    
    
	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return "Afterglow";
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {
		if (System.currentTimeMillis() > time + duration) {
			//System.out.println("Triggered AFterglow cd");
			bPlayer.addCooldown(this);
			remove();
			return;
		} else {
			ParticleEffect.ENCHANTMENT_TABLE.display(location, 1, 1F, 1F, 1F, 0F);
			Methods.displaySpellInstantParticle(location, 1, 0, 0, 0, 0F);
			//System.out.println("Triggered Afterglow 2");
			for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 1)) {
				if (entity instanceof LivingEntity) {
					// modified snippet from Numin's Rejuvenate ability
					if (entity instanceof Player && entity.getEntityId() != player.getEntityId()) {
		                Player ePlayer = (Player) entity;
		                BendingPlayer bEntity = BendingPlayer.getBendingPlayer(ePlayer);
		                
		                Element lightSpirit = SpiritElement.LIGHT;
		                Element darkSpirit = SpiritElement.DARK;
		                
		                if (bEntity.hasElement(lightSpirit) && entity.getUniqueId() != player.getUniqueId()) {
	        				
		                	double health = ePlayer.getHealth() + (healAmount / 13);
		        			if (health > ePlayer.getAttribute(Attribute.MAX_HEALTH).getValue()) {
		        	            health = ePlayer.getAttribute(Attribute.MAX_HEALTH).getValue();
		        	        }
		        			ePlayer.setHealth(health);
		        			
	        				ParticleEffect.HEART.display(ePlayer.getLocation().add(0, 2, 0), 1);
	        				bPlayer.addCooldown(this);
	        				remove();
	        				return;
		                }
		                if (bEntity.hasElement(darkSpirit)) {
		                	ParticleEffect.FIREWORKS_SPARK.display(location, 3, 0.2F, 0.2F, 0.2F, 0.8F);
		                    DamageHandler.damageEntity(entity, damage, this);
		                    bPlayer.addCooldown(this);
	        				remove();
	        				return;
		                }
		            } else if (entity instanceof Monster) {
		            	ParticleEffect.FIREWORKS_SPARK.display(location, 3, 0.2F, 0.2F, 0.2F, 0.8F);
		                DamageHandler.damageEntity(entity, damage, this);
		                bPlayer.addCooldown(this);
	    				remove();
	    				return;

		            }
				}
			}
		}
		return;
	}
	
	@Override
	public boolean isInstantiable() {
		return false;
	}

	@Override
	public boolean isProgressable() {
		return true;
	}

	@Override
	public String getAbilityType() {
		return SpiritAbility.PASSIVE;
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
