package me.numin.spirits.ability.spirit;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.attribute.Attribute;

import me.numin.spirits.ability.api.SpiritAbility;

public class PaintChangeColor extends SpiritAbility {

    //TODO: Update sounds.


	@Attribute(Attribute.COOLDOWN)
	private long cooldown;
	@Attribute(Attribute.DURATION)
	private long duration;
	private long time;
	Random rand = new Random();
	private Color customDustColor;
	static Material clickedMaterial;
	private static Map<String, Integer> COLORS = new HashMap<>();
	
	
	
    public PaintChangeColor(Player player) {
        super(player);
        if (COLORS.isEmpty()) defineColors();
        setFields();
        time = System.currentTimeMillis();
        //start();
        bPlayer.addCooldown(this);
        this.start();
    }
	public static Material handleClickedMaterial(Material clickedMaterial) {
		System.out.println("Clicked Material in Class B: " + clickedMaterial);
		return clickedMaterial;
	}
    private void setFields() {
       // this.cooldown = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Paint.Cooldown");
      //  this.duration = Spirits.plugin.getConfig().getLong("Abilities.Spirits.Neutral.Paint.Duration");
        
    }
    

	public static void defineColors() {
		COLORS.put("amethyst_block", 0x8561bf);						COLORS.put("ancient_debris", 0x5f3f37);
		COLORS.put("andesite", 0x888888);							COLORS.put("anvil", 0x444444);
		COLORS.put("basalt", 0x49484d);								COLORS.put("bedrock", 0x555555);
		COLORS.put("blackstone", 0x2a2328);							COLORS.put("black_concrete", 0x080a0f);
		COLORS.put("black_concrete_powder", 0x191a1f);				COLORS.put("black_glazed_terracotta", 0x431e20);
		COLORS.put("black_terracotta", 0x251610);					COLORS.put("blue_concrete", 0x2c2e8f);
		COLORS.put("blue_concrete_powder", 0x4649a6);				COLORS.put("blue_glazed_terracotta", 0x2f408b);
		COLORS.put("blue_terracotta", 0x4a3b5b);					COLORS.put("bricks", 0x966153);
		COLORS.put("brown_concrete", 0x603b1f);						COLORS.put("brown_concrete_powder", 0x7d5435);
		COLORS.put("brown_glazed_terracotta", 0x776a55);			COLORS.put("brown_terracotta", 0x4d3323);
		COLORS.put("calcite", 0xdfe0dc);							COLORS.put("cauldron", 0x4a494a);
		COLORS.put("chain", 0x0b0c10);								COLORS.put("chiseled_deepslate", 0x363636);
		COLORS.put("chiseled_nether_bricks", 0x2f171c);				COLORS.put("chiseled_polished_blackstone", 0x353038);
		COLORS.put("chiseled_quartz_block", 0xe7e2da);				COLORS.put("chiseled_red_sandstone", 0xb7601b);
		COLORS.put("chiseled_sandstone", 0xd8ca9b);					COLORS.put("chiseled_stone_bricks", 0x777677);
		COLORS.put("clay", 0xa0a6b3);								COLORS.put("coal_block", 0x100f0f);
		COLORS.put("coal_ore", 0x696969);							COLORS.put("coarse_dirt", 0x77553b);
		COLORS.put("cobbled_deepslate", 0x4d4d50);					COLORS.put("cobblestone", 0x7f7f7f);
		COLORS.put("copper_block", 0xc06b4f);						COLORS.put("copper_ore", 0x7c7d78);
		COLORS.put("cracked_deepslate_bricks", 0x404041);			COLORS.put("cracked_deepslate_tiles", 0x343434);
		COLORS.put("cracked_nether_bricks", 0x281417);				COLORS.put("cracked_polished_blackstone_bricks", 0x2c252b);
		COLORS.put("cracked_stone_bricks", 0x767576);				COLORS.put("crimson_nylium", 0x6b1a1a);
		COLORS.put("crying_obsidian", 0x200a3c);					COLORS.put("cut_copper", 0xbf6a50);
		COLORS.put("cut_red_sandstone", 0xbd651f);					COLORS.put("cut_sandstone", 0xd9ce9f);
		COLORS.put("cyan_concrete", 0x157788);						COLORS.put("cyan_concrete_powder", 0x24939d);
		COLORS.put("cyan_glazed_terracotta", 0x34767d);				COLORS.put("cyan_terracotta", 0x565b5b);
		COLORS.put("dark_prismarine", 0x335b4b);					COLORS.put("deepslate", 0x505052);
		COLORS.put("deepslate_bricks", 0x464647);					COLORS.put("deepslate_coal_ore", 0x4a4a4c);
		COLORS.put("deepslate_copper_ore", 0x5c5d59);				COLORS.put("deepslate_diamond_ore", 0x536a6a);
		COLORS.put("deepslate_emerald_ore", 0x4e6857);				COLORS.put("deepslate_gold_ore", 0x73664e);
		COLORS.put("deepslate_iron_ore", 0x6a635e);					COLORS.put("deepslate_lapis_ore", 0x4f5a73);
		COLORS.put("deepslate_redstone_ore", 0x68494a);				COLORS.put("deepslate_tiles", 0x363637);
		COLORS.put("diamond_block", 0x62ede4);						COLORS.put("diamond_ore", 0x798d8c);
		COLORS.put("diorite", 0xbcbcbc);							COLORS.put("dirt", 0x866043);
		COLORS.put("dirt_path", 0x805e3e);							COLORS.put("dragon_egg", 0x0c090f);
		COLORS.put("dripstone_block", 0x866b5c);					COLORS.put("emerald_block", 0x2acb57);
		COLORS.put("emerald_ore", 0x6c8873);						COLORS.put("end_stone", 0xdbde9e);
		COLORS.put("end_stone_bricks", 0xdae0a2);					COLORS.put("exposed_copper", 0xa17d67);
		COLORS.put("exposed_cut_copper", 0x9a7965);					COLORS.put("farmland", 0x8f6646);
		COLORS.put("gilded_blackstone", 0x372a26);					COLORS.put("glowstone", 0xab8354);
		COLORS.put("gold_block", 0xf6d03d);							COLORS.put("gold_ore", 0x91856a);
		COLORS.put("granite", 0x956755);							COLORS.put("grass_block", 0x738A4E);
		COLORS.put("gravel", 0x837f7e);								COLORS.put("gray_concrete", 0x36393d);
		COLORS.put("gray_concrete_powder", 0x4c5154);				COLORS.put("gray_glazed_terracotta", 0x535a5d);
		COLORS.put("gray_terracotta", 0x392a23);					COLORS.put("green_concrete", 0x495b24);
		COLORS.put("green_concrete_powder", 0x61772c);				COLORS.put("green_glazed_terracotta", 0x758e43);
		COLORS.put("green_terracotta", 0x4c532a);					COLORS.put("hopper", 0x424144);
		COLORS.put("iron_bars", 0x3e3f3d);							COLORS.put("iron_block", 0xdcdcdc);
		COLORS.put("iron_ore", 0x88817a);							COLORS.put("lapis_block", 0x1e438c);
		COLORS.put("lapis_ore", 0x6b758d);							COLORS.put("lava", 0xcf5b13);
		COLORS.put("lightning_rod", 0x1e110c);						COLORS.put("light_blue_concrete", 0x2389c6);
		COLORS.put("light_blue_concrete_powder", 0x4ab4d5);			COLORS.put("light_blue_glazed_terracotta", 0x5ea4d0);
		COLORS.put("light_blue_terracotta", 0x716c89);				COLORS.put("light_gray_concrete", 0x7d7d73);
		COLORS.put("light_gray_concrete_powder", 0x9a9a94);			COLORS.put("light_gray_glazed_terracotta", 0x90a6a7);
		COLORS.put("light_gray_terracotta", 0x876a61);				COLORS.put("lime_concrete", 0x5ea818);
		COLORS.put("lime_concrete_powder", 0x7dbd29);				COLORS.put("lime_glazed_terracotta", 0xa2c537);
		COLORS.put("lime_terracotta", 0x677534);					COLORS.put("magenta_concrete", 0xa9309f);
		COLORS.put("magenta_concrete_powder", 0xc053b8);			COLORS.put("magenta_glazed_terracotta", 0xd064bf);
		COLORS.put("magenta_terracotta", 0x95586c);					COLORS.put("magma", 0x8e3f1f);
		COLORS.put("mossy_cobblestone", 0x6e765e);					COLORS.put("mossy_stone_bricks", 0x737969);
		COLORS.put("moss_block", 0x596d2d);							COLORS.put("mud", 0x3c393c);
		COLORS.put("muddy_mangrove_roots", 0x443a30);				COLORS.put("mud_bricks", 0x89674f);
		COLORS.put("mushroom_stem", 0xcbc4b9);						COLORS.put("mycelium", 0x6f6265);
		COLORS.put("netherite_block", 0x423d3f);					COLORS.put("netherrack", 0x612626);
		COLORS.put("nether_bricks", 0x2c151a);						COLORS.put("nether_gold_ore", 0x73362a);
		COLORS.put("nether_quartz_ore", 0x75413e);					COLORS.put("nether_wart_block", 0x720202);
		COLORS.put("obsidian", 0x0f0a18);							COLORS.put("orange_concrete", 0xe06100);
		COLORS.put("orange_concrete_powder", 0xe3831f);				COLORS.put("orange_glazed_terracotta", 0x9a935b);
		COLORS.put("orange_terracotta", 0xa15325);					COLORS.put("oxidized_copper", 0x52a284);
		COLORS.put("oxidized_cut_copper", 0x4f997e);				COLORS.put("packed_mud", 0x8e6a4f);
		COLORS.put("pink_concrete", 0xd5658e);						COLORS.put("pink_concrete_powder", 0xe499b5);
		COLORS.put("pink_glazed_terracotta", 0xeb9ab5);				COLORS.put("pink_terracotta", 0xa14e4e);
		COLORS.put("podzol", 0x7a5739);								COLORS.put("pointed_dripstone", 0x745c50);
		COLORS.put("polished_andesite", 0x848685);					COLORS.put("polished_basalt", 0x58585b);
		COLORS.put("polished_blackstone", 0x353038);				COLORS.put("polished_blackstone_bricks", 0x302a31);
		COLORS.put("polished_deepslate", 0x484849);					COLORS.put("polished_diorite", 0xc0c1c2);
		COLORS.put("polished_granite", 0x9a6a59);					COLORS.put("prismarine", 0x639c97);
		COLORS.put("prismarine_bricks", 0x63ab9e);					COLORS.put("purple_concrete", 0x641f9c);
		COLORS.put("purple_concrete_powder", 0x8337b1);				COLORS.put("purple_glazed_terracotta", 0x6d3098);
		COLORS.put("purple_terracotta", 0x764656);					COLORS.put("purpur_block", 0xa97da9);
		COLORS.put("purpur_pillar", 0xab81ab);						COLORS.put("quartz_block", 0xebe5de);
		COLORS.put("quartz_bricks", 0xeae5dd);						COLORS.put("quartz_pillar", 0xebe6e0);
		COLORS.put("raw_copper_block", 0x9a694f);					COLORS.put("raw_gold_block", 0xdda92e);
		COLORS.put("raw_iron_block", 0xa6876b);						COLORS.put("redstone_block", 0xaf1805);
		COLORS.put("redstone_ore", 0x8c6d6d);						COLORS.put("red_concrete", 0x8e2020);
		COLORS.put("red_concrete_powder", 0xa83632);				COLORS.put("red_glazed_terracotta", 0xb53b35);
		COLORS.put("red_mushroom_block", 0xc82e2d);					COLORS.put("red_nether_bricks", 0x450709);
		COLORS.put("red_sand", 0xbe6621);							COLORS.put("red_sandstone", 0xba631d);
		COLORS.put("red_terracotta", 0x8f3d2e);						COLORS.put("reinforced_deepslate", 0x666d64);
		COLORS.put("rooted_dirt", 0x90674c);						COLORS.put("sand", 0xdbcfa3);
		COLORS.put("sandstone", 0xd8cb9b);							COLORS.put("sculk", 0x0c1d24);
		COLORS.put("sculk_catalyst", 0x0f1f26);						COLORS.put("sculk_sensor", 0x074654);
		COLORS.put("sculk_shrieker", 0x3f534e);						COLORS.put("sculk_vein", 0x052128);
		COLORS.put("sea_lantern", 0xacc7be);						COLORS.put("shroomlight", 0xf09246);
		COLORS.put("smooth_basalt", 0x48484e);						COLORS.put("smooth_stone", 0x9e9e9e);
		COLORS.put("soul_sand", 0x513e32);							COLORS.put("soul_soil", 0x4b392e);
		COLORS.put("stone", 0x7d7d7d);								COLORS.put("stone_bricks", 0x7a797a);
		COLORS.put("terracotta", 0x985e43);							COLORS.put("tuff", 0x6c6d66);
		COLORS.put("warped_nylium", 0x2b7265);						COLORS.put("warped_wart_block", 0x167779);
		COLORS.put("weathered_copper", 0x6c996e);					COLORS.put("weathered_cut_copper", 0x6d916b);
		COLORS.put("white_concrete", 0xcfd5d6);						COLORS.put("white_concrete_powder", 0xe1e3e3);
		COLORS.put("white_glazed_terracotta", 0xbcd4ca);			COLORS.put("white_terracotta", 0xd1b2a1);
		COLORS.put("yellow_concrete", 0xf0af15);					COLORS.put("yellow_concrete_powder", 0xe8c736);
		COLORS.put("yellow_glazed_terracotta", 0xeac058);			COLORS.put("yellow_terracotta", 0xba8523);
	}

	/**
	 * Gets the color of the provided material
	 * @param clickedMaterial The material
	 * @return The color in RGB/int
	 */
	public static int getColor(Material clickedMaterial) {
	 /*   if (material == null) {
	    	System.out.println("Material name is Null ");
	        return 0x7d7d7d; // Default to stone color if material is null
	    }*/
		if (clickedMaterial != null) {
		    String mat = clickedMaterial.name().toLowerCase();
		    System.out.println("Material name: " + mat);
			
			if (mat.startsWith("waxed_")) { //For copper
				mat = mat.substring(6);
			} else if (mat.startsWith("smooth_") && !mat.equals("smooth_stone") && !mat.equals("smooth_basalt")) { //For smoothed block variants
				mat = mat.substring(7);
			}
			if (mat.endsWith("_slab") || mat.endsWith("_wall")) {
				mat = mat.substring(0, mat.length() - 5);
			} else if (mat.endsWith("_stairs")) {
				mat = mat.substring(0, mat.length() - 7);
			} else if (mat.endsWith("_trap_door")) {
				mat = mat.substring(0, mat.length() - 10) + "_block";
			} else if (mat.endsWith("_door")) {
				mat = mat.substring(0, mat.length() - 5) + "_block";
			} else if (mat.equals("chipped_anvil") || mat.equals("damaged_anvil")) {
				mat = "anvil";
			} else if (mat.equals("grass_path")) {
				mat = "dirt_path";
			}

			if (COLORS.containsKey(mat)) {
				return COLORS.get(mat);
			}
		} else {
			return 0x7d7d7d; //Stone
		}
		return 0x7d7d7d;
	}

    @Override
    public void progress() {
    	
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		
		if (!player.isOnline() || player.isDead()) {
			remove();
			return;
		}

		changeColor(clickedMaterial);
		remove();
    }
     
    
    
    
   public static void changeColor(Material clickedMaterial2) {
			 

	   		PaintChangeColor.handleClickedMaterial(clickedMaterial);
	   		int color2 = PaintChangeColor.getColor(clickedMaterial);
	   		System.out.println("Color2 of clicked material: " + color2);
            int color = PaintChangeColor.getColor(clickedMaterial);
            System.out.println("Color1: " + color);
			//customDustColor = (Color.fromRGB(getColor(this.clickedMaterial.getColor)));

    }

    
    
    
    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public String getName() {
        return "Paint";
    }

    @Override
    public String getAbilityType() {
        return UTILITY;
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
    public boolean isSneakAbility() {
        return true;
    }


}