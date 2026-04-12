package me.numin.spirits.ability.dark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.command.Commands;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;

import me.numin.spirits.SpiritElement;
import me.numin.spirits.Spirits;
import me.numin.spirits.ability.api.DarkAbility;
import me.numin.spirits.ability.api.SpiritAbility;
import net.md_5.bungee.api.ChatColor;

public class Corruption extends DarkAbility {
	
	private static String path = "Abilities.Spirits.DarkSpirit.Corruption.";
	FileConfiguration config = Spirits.plugin.getConfig();
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<TempBlock> tempBlocks = new ArrayList<TempBlock>();
	
	private long cooldown;
	private double radius;
	private double duration;
	private int effectDuration;
	private int effectPower;
	private long poisonDuration;
	private int attackRadius;
	private int attackYRadius;
	private int spidertotal;
	private double time;
	private int spidernumber = 0;

	static String[] darkSpiritNames = { "Abyssal", "Blighted", "Corrupted", "Dark", "Dread", "Ebon", "Ghastly", "Grim", "Malevolent", "Necrotic", 
	"Shadow", "Sinister", "Spectral", "Vile", "Wicked", "Wrathful", "Vengeful", "Vicious", "Vindictive", "Voracious", "Wretched", "Zealous",
	"Baleful", "Cursed", "Doomed", "Forsaken", "Gloomy", "Haunted", "Infernal", "Macabre", "Morbid", "Ominous", "Pernicious", "Ravenous", 
	"Spiteful", "Tenebrous", "Unholy", "Unhallowed", "Malevolent", "Malignant", "Menacing", "Mournful", "Nocturnal", "Phantom", "Sable", 
	"Somber", "Sorrowful", "Twilight", "Umbra", "Void"
	};

	

	private Location origin;
	public Entity darkSpirit;
	
	Random rand = new Random();
	
	private Material[] plants = new Material[] {
    		Material.SHORT_GRASS, Material.FERN, Material.TALL_GRASS, Material.LARGE_FERN, Material.DANDELION, Material.POPPY, 
    		Material.OXEYE_DAISY, Material.SUNFLOWER, Material.CACTUS, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, 
    		Material.RED_TULIP, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.WHITE_TULIP, Material.LILAC, Material.ROSE_BUSH, Material.PEONY};

	public Corruption(Player player) {
		super(player);
				
		if (bPlayer.isOnCooldown(this)) {
			return;
		}
		cooldown = config.getLong(path + "Cooldown");
		radius = config.getDouble(path + "Radius");
		duration = config.getDouble(path + "Duration");
		effectPower = config.getInt(path + "EffectAmplifier");
		poisonDuration = config.getLong(path + "EffectDuration");
		attackRadius = config.getInt(path + "AttackRadius");
		attackYRadius = config.getInt(path + "AttackYRadius");
		spidertotal = config.getInt(path + "SpiderTotal");
		
		effectDuration = Math.toIntExact((poisonDuration * 1000) / 50);
		
		origin = player.getLocation();
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 0.8F, 0.3F);
		
		if (GeneralMethods.isRegionProtectedFromBuild(player, "Corruption", origin)) {
			return;
		}
	    if (radius <= 0) {
	        radius = 1;  // Default to a positive value if the configured radius is not valid
	    }
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
		return "Corruption";
	}

	@Override
	public boolean isSneakAbility() {
		return false;
	}

	@Override
	public void progress() {
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		
		if (player.isSneaking()) {
			startCorrupting();
			
		} else {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
	}
	
	private void startCorrupting() {
		if (GeneralMethods.isRegionProtectedFromBuild(player, "Corruption", origin)) {
			return;
		}
		
		time += 0.05;
		
		summonDarkSpirits();
		summonDarkSpirits();
		corruptBlocks();
		corruptBlocks();
		corruptBlocks();
		corruptBlocks();
		
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(origin, radius)) {
			if (entity instanceof LivingEntity) {
				if (GeneralMethods.isRegionProtectedFromBuild(this, entity.getLocation()) || ((entity instanceof Player) && Commands.invincible.contains(((Player) entity).getName()))) {
					continue;
				}
				
				if (entity instanceof Player) {
	                Player ePlayer = (Player) entity;
	                BendingPlayer bEntity = BendingPlayer.getBendingPlayer(ePlayer);
	                
	                if (bEntity != null) { //to fix error when, when the, when the, when the no element player gets hit
	                Element darkSpirit = SpiritElement.DARK;
					Element lightSpirit = SpiritElement.LIGHT;
					
					if (bEntity.hasElement(darkSpirit) && entity.getUniqueId() != player.getUniqueId()) {
						((LivingEntity) entity).addPotionEffect(new PotionEffect(
								PotionEffectType.RESISTANCE, effectDuration, effectPower));
						
	                } else if (bEntity.hasElement(lightSpirit) && entity.getUniqueId() != player.getUniqueId()) {
	                	((LivingEntity) entity).addPotionEffect(new PotionEffect(
	                			PotionEffectType.POISON, effectDuration, effectPower));
	                	
	                }
				}
			}
				
				if (entity.getUniqueId() != player.getUniqueId()) {
					((LivingEntity) entity).addPotionEffect(new PotionEffect(
                			PotionEffectType.POISON, effectDuration, effectPower));
				}
			}
		}
		
		if (time >= duration) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}
	
	private void corruptBlocks() {
		Location loc = origin.clone();
		loc.add((rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius),
				(rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius),
				(rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius));
		
		Block block = loc.getBlock().getRelative(BlockFace.UP);

		if (block.getType() != Material.AIR) {
			if (GeneralMethods.isRegionProtectedFromBuild(this, block.getLocation())) {
				return;
			}
			
			ParticleEffect.DRAGON_BREATH.display(origin, 1, radius, 0F, radius, 0, 0.05F);
			
			if (GeneralMethods.isSolid(block)) {
				int randomBlock = rand.nextInt(6);
				if (randomBlock == 0) {
					//block.setType(Material.COARSE_DIRT); Maybe to use later for a configuration option for permanent block changes
					final TempBlock tempBlock = new TempBlock(block, Material.MUDDY_MANGROVE_ROOTS);
					tempBlocks.add(tempBlock);
				} else if (randomBlock == 1) {
					final TempBlock tempBlock = new TempBlock(block, Material.PODZOL);
					tempBlocks.add(tempBlock);
				}
				else if (randomBlock == 2) {
					final TempBlock tempBlock = new TempBlock(block, Material.MUD);
					tempBlocks.add(tempBlock);
				}
				else if (randomBlock > 3) {
					final TempBlock tempBlock = new TempBlock(block, Material.MYCELIUM);
					tempBlocks.add(tempBlock);
				}

				ParticleEffect.SPELL_WITCH.display(block.getLocation().add(0, 1, 0), 3, 0.2F, 0.2F, 0.2F, 0.2F);
				
				
			}
			
			for (Material plants : this.plants) {

				if (block.getType() == plants) {
					ParticleEffect.SPELL_WITCH.display(block.getLocation().add(0, 1, 0), 3, 0.2F, 0.2F, 0.2F, 0.2F);
				int randomBlock = rand.nextInt(6);

				if (randomBlock == 0) {
					//block.setType(Material.COARSE_DIRT);
					final TempBlock tempBlock = new TempBlock(block, Material.PALE_OAK_SAPLING);
					tempBlocks.add(tempBlock);
				} else if (randomBlock == 1) {
					final TempBlock tempBlock = new TempBlock(block, Material.MANGROVE_ROOTS);
					tempBlocks.add(tempBlock);
				}
				else if (randomBlock > 2) {
					final TempBlock tempBlock = new TempBlock(block, Material.DEAD_BUSH);
					tempBlocks.add(tempBlock);
				}

				}
			}
		}
	}
	
	private void summonDarkSpirits() {
		if (Math.random() < 0.1) {
			Location loc = origin.clone();
			loc.add((rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius),
					(rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius),
					(rand.nextBoolean() ? 1 : -1) * rand.nextInt((int) radius));
			
			if (loc.getBlock().getType() == Material.AIR) {
				if (GeneralMethods.isRegionProtectedFromBuild(this, loc)) {
					return;
				}


				
				//this while might break things
				if (spidernumber < spidertotal) {
					darkSpirit = player.getWorld().spawnEntity(loc, EntityType.CAVE_SPIDER);
					player.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.5F);
					spidernumber++;
					//System.out.println("Spider number: " + spidernumber + " out of " + spidertotal);
				}
				
				darkSpirit.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + darkSpiritNames[rand.nextInt(darkSpiritNames.length)]);
				((Mob) darkSpirit).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (int) duration*20, 2, false));
				((Mob) darkSpirit).addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, (int) duration*20, 1, false));
				
				List<Entity> darkspiritTargets11 = player.getNearbyEntities(attackRadius, attackYRadius, attackRadius);
				for (Entity target15 : darkspiritTargets11) {
				    if (target15 instanceof LivingEntity) {
				        LivingEntity livingTarget1 = (LivingEntity) target15;
				        if (!livingTarget1.equals(player) && livingTarget1.getType() != EntityType.CAVE_SPIDER) {
				            ((Mob) darkSpirit).setTarget(livingTarget1);
				            //System.out.println("Target13 acquired: " + livingTarget1);
				        }
				    }
				}
				
				for (Entity e : entities) {
					for (Entity entity : GeneralMethods.getEntitiesAroundPoint(e.getLocation(), 1.5)) {
						if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
							if (GeneralMethods.isRegionProtectedFromBuild(this, entity.getLocation()) || ((entity instanceof Player) && Commands.invincible.contains(((Player) entity).getName()))) {
								continue;
							}
							((LivingEntity) entity).addPotionEffect(new PotionEffect(
		                			PotionEffectType.SLOWNESS, 40, 1));
						}
					}
				}
				
				
				
				ParticleEffect.SPELL_WITCH.display(loc, 10, 0.2F, 0.2F, 0.2F, 0.2F);
				
				entities.add(darkSpirit);
			
			}
		}
	}
	
	@Override
	public void remove() {
		super.remove();

		for (TempBlock temp : tempBlocks) {
			if(TempBlock.isTempBlock(temp.getBlock())) {
				temp.revertBlock();
			}
		}

		for (Entity entity : entities) {
			ParticleEffect.SPELL_WITCH.display(entity.getLocation().add(0, 1, 0), 10, 0.2F, 0.2F, 0.2F, 0.2F);
			ParticleEffect.PORTAL.display(entity.getLocation(), 20, 0.2F, 0.2F, 0.2F);
			player.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5F, 0.5F);
			entity.remove();
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
