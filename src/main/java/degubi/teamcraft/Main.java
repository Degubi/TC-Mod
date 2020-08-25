package degubi.teamcraft;

import static degubi.teamcraft.Registry.*;

import degubi.teamcraft.biome.*;
import degubi.teamcraft.block.*;
import degubi.teamcraft.block.interactive.*;
import degubi.teamcraft.block.tile.*;
import degubi.teamcraft.block.vanilla.*;
import degubi.teamcraft.block.variant.*;
import degubi.teamcraft.command.*;
import degubi.teamcraft.entity.*;
import degubi.teamcraft.gui.*;
import degubi.teamcraft.item.*;
import degubi.teamcraft.network.*;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.BlockPlanks.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.Item.*;
import net.minecraft.item.ItemArmor.*;
import net.minecraft.potion.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.BiomeManager.*;
import net.minecraftforge.common.brewing.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.common.registry.*;

@Mod(modid = "tcm", name = "TeamCraft")
public final class Main{
    public static final SimpleNetworkWrapper redstoneTimerChannel = createNewServerChannel("RedstoneTimerChannel", HandlerRedstoneTimer.class, MessageCoordInt.class);
    public static final SimpleNetworkWrapper nbtChannel = createNewServerChannel("NBTChannel", HandlerNBTMessage.class, MessageString.class);

    private static final ArmorMaterial lasulitArmor = EnumHelper.addArmorMaterial("lasulit", "tcm:lasulit", 10, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F).setRepairItem(new ItemStack(Items.DYE, 1, 4));
    private static final ArmorMaterial emeraldArmor = EnumHelper.addArmorMaterial("emerald", "tcm:emerald", 21, new int[]{2, 5, 6, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F).setRepairItem(new ItemStack(Items.EMERALD));
    private static final ToolMaterial emeraldTool = EnumHelper.addToolMaterial("emeraldTool", 2, 600, 7.0F, 2.0F, 10).setRepairItem(new ItemStack(Items.EMERALD));
    private static final ToolMaterial lasulittool = EnumHelper.addToolMaterial("lasulitTool", 1, 250, 5.0F, 1.0F, 10).setRepairItem(new ItemStack(Items.DYE, 1, 4));
    private static final ToolMaterial multitool = EnumHelper.addToolMaterial("multitool", 3, 2250, 9.0F, 3.0F, 15).setRepairItem(new ItemStack(Items.DIAMOND));

    public static final CreaTab tabBlocks = new CreaTab("tcmTabBlocks", Blocks.BEDROCK);
    public static final CreaTab tabDecorations = new CreaTab("tcmTabDecorations", Blocks.BOOKSHELF);
    public static final CreaTab tabRedstone = new CreaTab("tcmTabRedstone", Blocks.REDSTONE_BLOCK);
    public static final CreaTab tabFood = new CreaTab("tcmTabFood", Items.BEEF);
    public static final CreaTab ToolsWeapons = new CreaTab("tcmTabToolsWeapons", Items.ARMOR_STAND);
    public static final CreaTab tabMaterials = new CreaTab("tcmTabMaterials", Items.BOWL);
    public static final CreaTab tabAdmin = new CreaTab("tcmTabAdmin", Blocks.BEDROCK);

    private static final SoundEvent SOUNDBLOCK_1 = registerSound("tcm:soundblock.sound1");
    private static final SoundEvent SOUNDBLOCK_2 = registerSound("tcm:soundblock.sound2");
    private static final SoundEvent SOUNDBLOCK_3 = registerSound("tcm:soundblock.sound3");
    private static final SoundEvent SOUNDBLOCK_4 = registerSound("tcm:soundblock.sound4");
    public static final SoundEvent SOUNDBLOCK_5 = registerSound("tcm:soundblock.sound5");
    private static final SoundEvent SOUNDBLOCK_6 = registerSound("tcm:soundblock.sound6");
    public static final SoundEvent ENTITY_GOAT_AMBIENT = registerSound("tcm:mob.goat.say");
    public static final SoundEvent ENTITY_GOAT_HURT = registerSound("tcm:mob.goat.hit");
    public static final SoundEvent ENTITY_GOAT_DEATH = registerSound("tcm:mob.goat.death");
    public static final SoundEvent MEATBLOCK_EAT = registerSound("tcm:meatblock.eat");
    public static final SoundEvent DAY = registerSound("tcm:game.day");
    public static final SoundEvent NIGHT = registerSound("tcm:game.night");
    public static final SoundEvent THUNDER = registerSound("tcm:game.thunder");
    public static final SoundEvent SLEEPING = registerSound("tcm:player.sleep");
    public static final SoundType deepSlime = new SoundType(1.0F, 0.75F, SoundEvents.BLOCK_SLIME_BREAK, SoundEvents.BLOCK_SLIME_STEP, SoundEvents.BLOCK_SLIME_PLACE, SoundEvents.BLOCK_SLIME_HIT, SoundEvents.BLOCK_SLIME_FALL);

    public static Block SingleSlabs3, DoubleSlabs2, DoubleSlabs3, SingleSlabs1, DoubleSlabs1, SingleSlabs2, DarkOakTrapDoor, SpruceTrapDoor, BirchTrapDoor, JungleTrapDoor, AcaciaTrapDoor;

    public static final Block AppleLeaves = registerBlock(new BlockAppleLeaves(), "appleleaves");
    public static final Block AppleSapling = registerBlock(new BlockAppleSapling(), "applesapling");
    public static final Block bananaSapling = registerBlock(new BlockAppleSapling(), "bananasapling");
    public static final Block bananaLeaves = registerBlock(new BlockAppleLeaves(), "bananaleaves");
    public static final Block cocoaLeaves = registerBlock(new BlockAppleLeaves(), "cocoaleaves");

    public static final Block QuickSand = registerBlock(new BlockQuickSand(), "quicksand");
    public static final Block WoolStandingSign = registerBlockWItem(new ItemWoolSign(), "woolstandingsign");
    public static final Block WoolWallSign = registerBlockNoItem(new WoolWSign(), "woolwallsign");
    public static final Block Cactus = registerBlock(new BlockRotatingBlocks().setHardness(0.9F).setCreativeTab(tabDecorations), "ice_cactus");
    public static final Block SpiderEgg = registerBlock(new BlockSpiderEgg(), "spideregg");
    public static final Block Lantern = registerBlock(new BlockLantern(), "lantern");
    public static final Block WoodenChair = registerBlock(new BlockChair(), "chair");
    public static final Block IcIcle = registerBlock(new BlockIcicle(), "icicle");

    public static final Block SoundBlock1 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_1), "soundblock1");
    public static final Block SoundBlock2 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_2), "soundblock2");
    public static final Block SoundBlock3 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_3), "soundblock3");
    public static final Block SoundBlock4 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_4), "soundblock4");
    public static final Block SoundBlock5 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_5), "soundblock5");
    public static final Block SoundBlock6 = registerBlock(new BlockSoundBlocks(SOUNDBLOCK_6), "soundblock6");

    public static final Block GlassDoor = registerDoor(new BlockDoors(Blocks.GLASS), "glass_door");
    public static final Block GoldDoor = registerDoor(new BlockDoors(Blocks.GOLD_BLOCK), "gold_door");
    public static final Block DiamondDoor = registerDoor(new BlockDoors(Blocks.DIAMOND_BLOCK), "diamond_door");
    public static final Block CobbleDoor = registerDoor(new BlockDoors(Blocks.COBBLESTONE), "cobble_door");
    public static final Block ObsidianDoor = registerDoor(new BlockDoors(Blocks.OBSIDIAN), "obsi_door");
    public static final Block QuartzDoor = registerDoor(new BlockDoors(Blocks.QUARTZ_BLOCK), "quartz_door");
    public static final Block SandstoneDoor = registerDoor(new BlockDoors(Blocks.SANDSTONE), "sandstone_door");
    public static final Block RetardDoor = registerDoor(new BlockDoors(Blocks.PLANKS), "retard_door");
    public static final Block EmeraldDoor = registerDoor(new BlockDoors(Blocks.EMERALD_BLOCK), "emerald_door");
    public static final Block OakLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "oaklog_door");
    public static final Block SpruceLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "sprucelog_door");
    public static final Block BirchLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "birchlog_door");
    public static final Block JungleLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "junglelog_door");
    public static final Block AcaciaLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "acacialog_door");
    public static final Block DarkOakLogDoor = registerDoor(new BlockDoors(Blocks.LOG), "darkoaklog_door");
    public static final Block IceDoor = registerDoor(new BlockDoors(Blocks.ICE), "ice_door");
    public static final Block PackedIceDoor = registerDoor(new BlockDoors(Blocks.PACKED_ICE), "packedice_door");

    public static final Block NetherCoalOre = registerBlock(new BlockOres(Blocks.COAL_ORE), "nethercoal_ore");
    public static final Block NetherIronOre = registerBlock(new BlockOres(Blocks.IRON_ORE), "netheriron_ore");
    public static final Block NetherGoldOre = registerBlock(new BlockOres(Blocks.GOLD_ORE), "nethergold_ore");
    public static final Block NetherLapisOre = registerBlock(new BlockOres(Blocks.LAPIS_ORE), "netherlapis_ore");
    public static final Block NetherEmeraldOre = registerBlock(new BlockOres(Blocks.EMERALD_ORE), "netheremerald_ore");
    public static final Block NetherDiamondOre = registerBlock(new BlockOres(Blocks.DIAMOND_ORE), "netherdiamond_ore");

    public static final Block Basalt = registerBlock(new BlockBase(Blocks.OBSIDIAN, tabBlocks), "basalt");
    public static final Block SmoothBasalt = registerBlock(new BlockBase(Blocks.OBSIDIAN, tabBlocks), "smoothbasalt");
    public static final Block NetherCoalBlock = registerBlock(new BlockBase(Blocks.COAL_BLOCK, tabBlocks), "nethercoalblock");
    public static final Block MarbleStone = registerBlock(new BlockOres(Blocks.STONE), "marblestone");
    public static final Block MarbleBrick = registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "marblebrick");
    public static final Block MarbleCobble = registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "marblecobble");
    public static final Block GravelStone = registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "gravelstone");

    public static final Block OakLeafBush = registerBlock(new BlockWalls(Blocks.LEAVES), "oak_bush");
    public static final Block SpruceLeafBush = registerBlock(new BlockWalls(Blocks.LEAVES), "spruce_bush");
    public static final Block BirchLeafBush = registerBlock(new BlockWalls(Blocks.LEAVES), "birch_bush");
    public static final Block JungleLeafBush = registerBlock(new BlockWalls(Blocks.LEAVES), "jungle_bush");

    public static final Block OakLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor()).setCreativeTab(tabDecorations), "oaklog_fence");
    public static final Block SpruceLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.getMapColor()).setCreativeTab(tabDecorations), "sprucelog_fence");
    public static final Block BirchLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.getMapColor()).setCreativeTab(tabDecorations), "birchlog_fence");
    public static final Block JungleLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.getMapColor()).setCreativeTab(tabDecorations), "junglelog_fence");
    public static final Block AcaciaLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.getMapColor()).setCreativeTab(tabDecorations), "acacialog_fence");
    public static final Block DarkOakLogFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor()).setCreativeTab(tabDecorations), "darkoaklog_fence");
    public static final Block RetardFence = registerBlock(new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor()).setCreativeTab(tabDecorations), "retard_fence");
    public static final Block OakLogGate = registerBlock(new BlockFenceGate(EnumType.OAK).setHardness(2.0F).setCreativeTab(tabRedstone), "oaklog_gate");
    public static final Block SpruceLogGate = registerBlock(new BlockFenceGate(EnumType.SPRUCE).setHardness(2.0F).setCreativeTab(tabRedstone), "sprucelog_gate");
    public static final Block BirchLogGate = registerBlock(new BlockFenceGate(EnumType.BIRCH).setHardness(2.0F).setCreativeTab(tabRedstone), "birchlog_gate");
    public static final Block JungleLogGate = registerBlock(new BlockFenceGate(EnumType.JUNGLE).setHardness(2.0F).setCreativeTab(tabRedstone), "junglelog_gate");
    public static final Block AcaciaLogGate = registerBlock(new BlockFenceGate(EnumType.ACACIA).setHardness(2.0F).setCreativeTab(tabRedstone), "acacialog_gate");
    public static final Block DarkOakLogGate = registerBlock(new BlockFenceGate(EnumType.DARK_OAK).setHardness(2.0F).setCreativeTab(tabRedstone), "darkoaklog_gate");
    public static final Block RetardGate = registerBlock(new BlockFenceGate(EnumType.OAK).setHardness(2.0F).setCreativeTab(tabRedstone), "retard_gate");

    public static final Block IceCreeperBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icecreeper_block");
    public static final Block IcePigmanBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icepigman_block");
    public static final Block IcePigSpiderBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icepigspider_block");
    public static final Block IceSkeletonBlock = registerBlockNoItem(new BlockMobIceBlocks(), "iceskeleton_block");
    public static final Block IceZombieBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icezombie_block");
    public static final Block IceThreeHeadCreeperBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icethreeheadcreeper_block");
    public static final Block IceGoatBlock = registerBlockNoItem(new BlockMobIceBlocks(), "icegoat_block");
    public static final Block FakeBlock = registerBlockNoItem(new BlockFakeBlock(), "fakeblock");

    public static final Block HalfGlowstone = registerBlock(new BlockHalfBlocks(Blocks.GLOWSTONE).setLightLevel(1.0F), "halfglowstone");
    public static final Block HalfCobble = registerBlock(new BlockHalfBlocks(Blocks.COBBLESTONE), "halfcobble");
    public static final Block HalfBrick = registerBlock(new BlockHalfBlocks(Blocks.BRICK_BLOCK), "halfbrick");
    public static final Block HalfStoneBrick = registerBlock(new BlockHalfBlocks(Blocks.STONEBRICK), "halfstonebrick");
    public static final Block HalfOakPlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfoakplanks");
    public static final Block HalfSprucePlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfspruceplanks");
    public static final Block HalfBirchPlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfbirchplanks");
    public static final Block HalfJunglePlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfjungleplanks");
    public static final Block HalfAcaciaPlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfacaciaplanks");
    public static final Block HalfDarkOakPlanksBlock = registerBlock(new BlockHalfBlocks(Blocks.PLANKS), "halfdarkoakplanks");
    public static final Block HalfOakLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfoaklog");
    public static final Block HalfSpruceLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfsprucelog");
    public static final Block HalfBirchLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfbirchlog");
    public static final Block HalfJungleLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfjunglelog");
    public static final Block HalfAcaciaLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfacacialog");
    public static final Block HalfDarkOakLog = registerBlock(new BlockHalfBlocks(Blocks.LOG), "halfdarkoaklog");
    public static final Block HalfBlackGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfblackglass");
    public static final Block HalfWhiteGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfwhiteglass");
    public static final Block HalfBlueGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfblueglass");
    public static final Block HalfBrownGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfbrownglass");
    public static final Block HalfCyanGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfcyanglass");
    public static final Block HalfGrayGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfgrayglass");
    public static final Block HalfGreenGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfgreenglass");
    public static final Block HalfLightBlueGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halflightblueglass");
    public static final Block HalfLimeGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halflimeglass");
    public static final Block HalfMagentaGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfmagentaglass");
    public static final Block HalfOrangeGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halforangeglass");
    public static final Block HalfPinkGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfpinkglass");
    public static final Block HalfPurpleGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfpurpleglass");
    public static final Block HalfRedGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfredglass");
    public static final Block HalfSilverGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfnorrisglass");
    public static final Block HalfYellowGlass = registerBlock(new BlockHalfBlocks(Blocks.STAINED_GLASS), "halfyellowglass");
    public static final Block HalfGlass = registerBlock(new BlockHalfBlocks(Blocks.GLASS), "halfglass");
    public static final Block HalfAndesite = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfandesite");
    public static final Block HalfDiorite = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfdiorite");
    public static final Block HalfGranite = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfgranite");
    public static final Block HalfQuartz = registerBlock(new BlockHalfBlocks(Blocks.QUARTZ_BLOCK), "halfquartz");
    public static final Block HalfQuartzPillar = registerBlock(new BlockHalfBlocks(Blocks.QUARTZ_BLOCK), "halfquartzpillar");
    public static final Block HalfChiseledQuartz = registerBlock(new BlockHalfBlocks(Blocks.QUARTZ_BLOCK), "halfchiseledquartz");
    public static final Block HalfIce = registerBlock(new BlockHalfBlocks(Blocks.ICE), "halfice");
    public static final Block HalfPackedIce = registerBlock(new BlockHalfBlocks(Blocks.PACKED_ICE), "halfpackedice");
    public static final Block HalfSandstone = registerBlock(new BlockHalfBlocks(Blocks.SANDSTONE), "halfsandstone");
    public static final Block HalfSmoothSandstone = registerBlock(new BlockHalfBlocks(Blocks.SANDSTONE), "halfsmoothsandstone");
    public static final Block HalfCleanQuartz = registerBlock(new BlockHalfBlocks(Blocks.QUARTZ_BLOCK), "halfcleanquartz");
    public static final Block HalfSand = registerBlock(new BlockHalfBlocks(Blocks.SAND), "halfsand");
    public static final Block HalfStone = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfstone");
    public static final Block HalfMarbleCobble = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfmarblecobble");
    public static final Block HalfGraniteBrick = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfgranitebrick");
    public static final Block HalfAndesiteBrick = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfandesitebrick");
    public static final Block HalfBasaltBrick = registerBlock(new BlockHalfBlocks(Blocks.STONE), "halfbasaltbrick");

    public static final Item PositionItem = registerItem(new ItemPosItem(), "positem");
    public static final Item GenerationItem = registerItem(new ItemGenItem(), "genitem");
    public static final Item MultiTool = registerItem(new ItemMultiTool(multitool), "multitool");
    public static final Item LapisHelmet = registerItem(new ItemArmor(lasulitArmor, 4, EntityEquipmentSlot.HEAD).setCreativeTab(ToolsWeapons), "lasulit_helmet");
    public static final Item LapisChestPlate = registerItem(new ItemArmor(lasulitArmor, 4, EntityEquipmentSlot.CHEST).setCreativeTab(ToolsWeapons), "lasulit_chestplate");
    public static final Item LapisLeggings = registerItem(new ItemArmor(lasulitArmor, 4, EntityEquipmentSlot.LEGS).setCreativeTab(ToolsWeapons), "lasulit_leggings");
    public static final Item LapisBoots = registerItem(new ItemArmor(lasulitArmor, 4, EntityEquipmentSlot.FEET).setCreativeTab(ToolsWeapons), "lasulit_boots");
    public static final Item EmeraldHelmet = registerItem(new ItemArmor(emeraldArmor, 4, EntityEquipmentSlot.HEAD).setCreativeTab(ToolsWeapons), "emerald_helmet");
    public static final Item EmeraldChestPlate = registerItem(new ItemArmor(emeraldArmor, 4, EntityEquipmentSlot.CHEST).setCreativeTab(ToolsWeapons), "emerald_chestplate");
    public static final Item EmeraldLeggings = registerItem(new ItemArmor(emeraldArmor, 4, EntityEquipmentSlot.LEGS).setCreativeTab(ToolsWeapons), "emerald_leggings");
    public static final Item EmeraldBoots = registerItem(new ItemArmor(emeraldArmor, 4, EntityEquipmentSlot.FEET).setCreativeTab(ToolsWeapons), "emerald_boots");
    public static final Item tcmMultiTool = registerItem(new ItemAdminTool(), "admintool");
    public static final Item NetherCoal = registerItem(new Item().setCreativeTab(tabMaterials), "nethercoal");
    public static final Item CureItem = registerItem(new ItemCure(), "cureitem");
    public static final Item RawGoatMeat = registerItem(new ItemFood(3, true).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 15, 0), 0.5F).setCreativeTab(tabFood), "rawgoat_meat");
    public static final Item CookedGoatMeat = registerItem(new ItemFood(8, true).setCreativeTab(tabFood), "cookedgoat_meat");
    public static final Item RawSheepMeat = registerItem(new ItemFood(2, true).setFull3D().setCreativeTab(tabFood), "rawsheep_meat");
    public static final Item CookedSheepMeat = registerItem(new ItemFood(7, true).setFull3D().setCreativeTab(tabFood), "cookedsheep_meat");
    public static final Item Banana = registerItem(new ItemFood(5, false).setCreativeTab(tabFood), "banana");
    public static final Item EmeraldSword = registerItem(new ItemSword(emeraldTool).setCreativeTab(ToolsWeapons), "emerald_sword");
    public static final Item LasulitSword = registerItem(new ItemSword(lasulittool).setCreativeTab(ToolsWeapons), "lasulit_sword");
    public static final Item BrownMushroomItem = registerItem(new ItemMushrooms(5), "brown_mushroom");
    public static final Item RedMushroomItem = registerItem(new ItemMushrooms(4).setAlwaysEdible().setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20, 2), 3.0F), "red_mushroom");

    public static final Biome snowHills = registerBiome(new BiomeHighBlocked("SnowyHills", Blocks.SNOW, 0.5F, 2.0F), "SnowyHills", 4, BiomeType.COOL);
    public static final Biome iceHills = registerBiome(new BiomeHighBlocked("IceHills", Blocks.PACKED_ICE, 0.1F, 0.7F), "IceHills", 4, BiomeType.COOL);
    public static final Biome darkForest = registerBiome(new BiomeDarkForest(), "darkforest", 3, BiomeType.WARM);
    public static final Biome extremestHill = registerBiome(new BiomeBase("TheExtremestHills", 0.5F), "TheExtremestHills", 3, BiomeType.COOL);
    public static final Biome extremerHills = registerBiome(new BiomeBase("ExtremerHills", 6.0F), "ExtremerHills", 3, BiomeType.COOL);
    public static final Biome iceDesert = registerBiome(new BiomeIceDesert(), "IceDesert", 5, BiomeType.COOL);
    public static final Biome forbiddenForest = registerBiome(new BiomeForbiddenForest(), "ForbiddenForest", 3, BiomeType.WARM);
    public static final Biome flatNothing = registerBiome(new BiomeFlatNothing(), "FlatNothing", 5, BiomeType.DESERT);
    public static final Biome tundra = registerBiome(new BiomeTundra(), "Tundra", 5, BiomeType.COOL);
    public static final Biome nsForest = registerBiome(new BiomeNiceShotForest(), "nsforest", 3, BiomeType.WARM);
    public static final Biome iceberg = registerBiome(new BiomeIceberg(), "iceberg", 4, BiomeType.COOL);

    public Main() {
        registerItem(new ItemKnife(lasulittool), "knife");
        registerItem(new Item().setCreativeTab(tabMaterials).setFull3D(), "stonerod");
        registerBlock(new Block(Material.ROCK).setCreativeTab(tabDecorations).setLightLevel(1.0f).setTickRandomly(true), "stonelantern");
        registerBlock(new BlockBase(Blocks.OBSIDIAN, tabBlocks), "hardenedstonebrick");

        registerBlock(new BlockRedstoneTimer(), "redstonetimer");
        registerBlock(new BlockRotatingBlocks(), "quartz_pillar");
        registerBlock(new BlockMeatBlock(), "meatblock");
        registerBlock(new BlockStonePath(), "stonepath");
        registerBlock(new BlockConveyor(), "conveyor");
        registerBlockWItem(new ItemRope(), "rope");
        registerBlock(new BlockHalfBlockMaker(), "halfblockmaker");
        registerBlock(new BlockRedslimeBlock(), "redslimeblock");
        registerBlock(new BlockRotatingBlocks().setHardness(2.0f), "stone_pillar");

        Constructor<BlockLever> leverConstructor = getConstructor(BlockLever.class);
        registerBlock("stonebrick_lever", tabRedstone, Blocks.LEVER, leverConstructor);
        registerBlock("quartz_lever", tabRedstone, Blocks.LEVER, leverConstructor);

        registerBlock(new BlockInvBedrock(0), "invbedrock");
        registerBlock(new BlockInvBedrock(1), "fix_block");
        registerBlock(new BlockInvBedrock(2), "air_block");

        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "spruce_preplate");
        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "birch_preplate");
        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "jungle_preplate");
        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "acacia_preplate");
        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "darkoak_preplate");
        registerBlock(new BlockPressurePlates(Blocks.PLANKS), "retard_preplate");
        registerBlock(new BlockPressurePlates(Blocks.GLASS), "glass_preplate");
        registerBlock(new BlockPressurePlates(Blocks.COBBLESTONE), "cobble_preplate");
        registerBlock(new BlockPressurePlates(Blocks.OBSIDIAN), "obsidian_preplate");
        registerBlock(new BlockPressurePlates(Blocks.BRICK_BLOCK), "brick_preplate");
        registerBlock(new BlockPressurePlates(Blocks.STONEBRICK), "stonebrick_preplate");
        registerBlock(new BlockPressurePlates(Blocks.STONE), "andesite_preplate");
        registerBlock(new BlockPressurePlates(Blocks.STONE), "granite_preplate");
        registerBlock(new BlockPressurePlates(Blocks.STONE), "diorite_preplate");
        registerBlock(new BlockPressurePlates(Blocks.QUARTZ_BLOCK), "quartz_preplate");

        registerBlock(new BlockBase(Blocks.QUARTZ_BLOCK, tabBlocks), "cleanquartz");
        registerBlock(new BlockBase(Blocks.QUARTZ_BLOCK, tabBlocks), "quartzbrick1");
        registerBlock(new BlockBase(Blocks.QUARTZ_BLOCK, tabBlocks), "quartzbrick2");
        registerBlock(new BlockBase(Blocks.QUARTZ_BLOCK, tabBlocks), "carvedquartz");
        registerBlock(new BlockBase(Blocks.QUARTZ_BLOCK, tabBlocks), "crackedquartz");

        registerBlock(new BlockTables(), "oakplanks_crafttable");
        registerBlock(new BlockTables(), "spruceplanks_crafttable");
        registerBlock(new BlockTables(), "birchplanks_crafttable");
        registerBlock(new BlockTables(), "jungleplanks_crafttable");
        registerBlock(new BlockTables(), "acaciaplanks_crafttable");
        registerBlock(new BlockTables(), "darkoakplanks_crafttable");
        registerBlock(new BlockTables(), "retard_crafttable");
        registerBlock(new BlockTables(), "oaklog_crafttable");
        registerBlock(new BlockTables(), "sprucelog_crafttable");
        registerBlock(new BlockTables(), "birchlog_crafttable");
        registerBlock(new BlockTables(), "junglelog_crafttable");
        registerBlock(new BlockTables(), "acacialog_crafttable");
        registerBlock(new BlockTables(), "darkoaklog_crafttable");

        registerBlock(new BlockPillars(), "oakplanks_pillar");
        registerBlock(new BlockPillars(), "spruceplanks_pillar");
        registerBlock(new BlockPillars(), "birchplanks_pillar");
        registerBlock(new BlockPillars(), "jungleplanks_pillar");
        registerBlock(new BlockPillars(), "acaciaplanks_pillar");
        registerBlock(new BlockPillars(), "darkoakplanks_pillar");
        registerBlock(new BlockPillars(), "retard_pillar");
        registerBlock(new BlockPillars(), "oaklog_pillar");
        registerBlock(new BlockPillars(), "sprucelog_pillar");
        registerBlock(new BlockPillars(), "birchlog_pillar");
        registerBlock(new BlockPillars(), "junglelog_pillar");
        registerBlock(new BlockPillars(), "acacialog_pillar");
        registerBlock(new BlockPillars(), "darkoaklog_pillar");

        registerBlock(new BlockBase(Blocks.OBSIDIAN, tabAdmin), "illustone1");
        registerBlock(new BlockBase(Blocks.OBSIDIAN, tabAdmin), "illustone2");

        registerBlock(new BlockBase(Blocks.OBSIDIAN, tabBlocks), "basaltbrick");
        registerBlock(new BlockBase(Blocks.SANDSTONE, tabBlocks), "cleansandstone");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "cleanstone");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "chiseledstone");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "granitestonebrick");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "andesitestonebrick");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "carvedgranitebrick");
        registerBlock(new BlockBase(Blocks.STONE, tabBlocks), "carvedandesitebrick");

        registerBlock(new BlockStairBlocks(Blocks.LOG), "oaklog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LOG), "sprucelog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LOG), "birchlog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LOG), "junglelog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LOG), "acacialog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LOG), "darkoaklog_stairs");
        registerBlock(new BlockStairBlocks(Blocks.PLANKS), "retard_stairs");
        registerBlock(new BlockStairBlocks(Blocks.GLASS), "glass_stairs");
        registerBlock(new BlockStairBlocks(Blocks.GLOWSTONE), "glowstone_stairs").setLightLevel(1.0F);
        registerBlock(new BlockStairBlocks(Blocks.EMERALD_BLOCK), "emerald_stairs");
        registerBlock(new BlockStairBlocks(Blocks.COBBLESTONE), "marblecobble_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONEBRICK), "marblebrick_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "marblestone_stairs");
        registerBlock(new BlockStairBlocks(Blocks.ICE), "ice_stairs");
        registerBlock(new BlockStairBlocks(Blocks.SNOW), "snow_stairs");
        registerBlock(new BlockStairBlocks(Blocks.PACKED_ICE), "packedice_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "andesite_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "diorite_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "granite_stairs");
        registerBlock(new BlockStairBlocks(Blocks.DIAMOND_BLOCK), "diamond_stairs");
        registerBlock(new BlockStairBlocks(Blocks.IRON_BLOCK), "iron_stairs");
        registerBlock(new BlockStairBlocks(Blocks.GOLD_BLOCK), "gold_stairs");
        registerBlock(new BlockStairBlocks(Blocks.LAPIS_BLOCK), "lasulit_stairs");
        registerBlock(new BlockStairBlocks(Blocks.PRISMARINE), "prismarine_stairs");
        registerBlock(new BlockStairBlocks(Blocks.PRISMARINE), "prismarinebrick_stairs");
        registerBlock(new BlockStairBlocks(Blocks.PRISMARINE), "darkprismarine_stairs");
        registerBlock(new BlockStairBlocks(Blocks.OBSIDIAN), "hardenedbrick_stairs");
        registerBlock(new BlockStairBlocks(Blocks.QUARTZ_BLOCK), "cleanquartz_stairs");
        registerBlock(new BlockStairBlocks(Blocks.QUARTZ_BLOCK), "quartzpillar_stairs");
        registerBlock(new BlockStairBlocks(Blocks.QUARTZ_BLOCK), "chiseledquartz_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "basalt_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "granitestonebrick_stairs");
        registerBlock(new BlockStairBlocks(Blocks.STONE), "andesitestonebrick_stairs");

        registerBlock(new BlockWalls(Blocks.STONEBRICK), "stonebrick_wall");
        registerBlock(new BlockWalls(Blocks.SANDSTONE), "sandstone_wall");
        registerBlock(new BlockWalls(Blocks.STONEBRICK), "mossybrick_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "stone_wall");
        registerBlock(new BlockWalls(Blocks.OBSIDIAN), "obsidian_wall");
        registerBlock(new BlockWalls(Blocks.DIRT), "dirt_wall");
        registerBlock(new BlockWalls(Blocks.QUARTZ_BLOCK), "quartz_wall");
        registerBlock(new BlockWalls(Blocks.QUARTZ_BLOCK), "quartzpillar_wall");
        registerBlock(new BlockWalls(Blocks.BRICK_BLOCK), "brick_wall");
        registerBlock(new BlockWalls(Blocks.ICE), "ice_wall");
        registerBlock(new BlockWalls(Blocks.SNOW), "snow_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "andesite_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "diorite_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "granite_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "basaltbricks_wall");
        registerBlock(new BlockWalls(Blocks.OBSIDIAN), "hardenedstonebrick_wall");
        registerBlock(new BlockWalls(Blocks.PACKED_ICE), "packedice_wall");
        registerBlock(new BlockWalls(Blocks.GLOWSTONE), "glowstone_wall").setLightLevel(1.0F);
        registerBlock(new BlockWalls(Blocks.NETHER_BRICK), "netherbrick_wall");
        registerBlock(new BlockWalls(Blocks.PRISMARINE), "prismarine_wall");
        registerBlock(new BlockWalls(Blocks.PRISMARINE), "prismarinebrick_wall");
        registerBlock(new BlockWalls(Blocks.PRISMARINE), "darkprismarine_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "cleanstone_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "chiseledstone_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "andesitestonebrick_wall");
        registerBlock(new BlockWalls(Blocks.STONE), "granitestonebrick_wall");

        registerBlock(new BlockButtons(Blocks.PLANKS), "spruce_button");
        registerBlock(new BlockButtons(Blocks.PLANKS), "birch_button");
        registerBlock(new BlockButtons(Blocks.PLANKS), "jungle_button");
        registerBlock(new BlockButtons(Blocks.PLANKS), "acacia_button");
        registerBlock(new BlockButtons(Blocks.PLANKS), "darkoak_button");
        registerBlock(new BlockButtons(Blocks.GLOWSTONE), "glowstone_button").setLightLevel(0.7F);
        registerBlock(new BlockButtons(Blocks.BRICK_BLOCK), "brick_button");
        registerBlock(new BlockButtons(Blocks.STONEBRICK), "stonebrick_button");
        registerBlock(new BlockButtons(Blocks.REDSTONE_BLOCK), "redstone_button");
        registerBlock(new BlockButtons(Blocks.OBSIDIAN), "obsidian_button");
        registerBlock(new BlockButtons(Blocks.ICE), "ice_button");
        registerBlock(new BlockButtons(Blocks.PACKED_ICE), "packedice_button");

        registerItem(new ItemHoe(emeraldTool).setCreativeTab(ToolsWeapons), "emerald_hoe");
        registerItem(new ItemHoe(lasulittool).setCreativeTab(ToolsWeapons), "lasulit_hoe");
        registerItem(new ItemSpade(emeraldTool).setCreativeTab(ToolsWeapons), "emerald_shovel");
        registerItem(new ItemSpade(lasulittool).setCreativeTab(ToolsWeapons), "lasulit_shovel");

        registerVanillaSlab(SingleSlabs1 = new Slabs1(false), DoubleSlabs1 = new Slabs1(true), 1);
        registerVanillaSlab(SingleSlabs2 = new Slabs2(false), DoubleSlabs2 = new Slabs2(true), 2);
        registerVanillaSlab(SingleSlabs3 = new Slabs3(false), DoubleSlabs3 = new Slabs3(true), 3);

        Constructor<BlockLadder> ladderConstructor = getConstructor(BlockLadder.class);
        registerBlock("spruce_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);
        registerBlock("birch_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);
        registerBlock("jungle_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);
        registerBlock("acacia_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);
        registerBlock("darkoak_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);
        registerBlock("stone_ladder", tabDecorations, Blocks.LADDER, ladderConstructor);

        Constructor<ItemAxe> axeConstructor = getConstructor(ItemAxe.class, ToolMaterial.class, float.class, float.class);
        registerItem("emerald_axe", ToolsWeapons, axeConstructor, emeraldTool, Float.valueOf(6.0F), Float.valueOf(-3.0F));
        registerItem("lasulit_axe", ToolsWeapons, axeConstructor, lasulittool, Float.valueOf(4.0F), Float.valueOf(-3.0F));

        Constructor<ItemPickaxe> pickaxeConstructor = getConstructor(ItemPickaxe.class, ToolMaterial.class);
        registerItem("emerald_pickaxe", ToolsWeapons, pickaxeConstructor, emeraldTool);
        registerItem("lasulit_pickaxe", ToolsWeapons, pickaxeConstructor, lasulittool);
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event) {
        if(event.getSide().isClient()){
            ClientThings.registerClientThings();

            new Thread(() -> {
                System.out.println("Started loading gui resources!");
                GuiTCMMain.loadGuiResources();
                System.out.println("Gui resources loaded!");
            }, "DataLoader Thread").start();
        }

        Constructor<BlockTrapDoor> trapdoorConstructor = getConstructor(BlockTrapDoor.class, Material.class);
        SpruceTrapDoor = registerBlock("spruce_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);
        BirchTrapDoor = registerBlock("birch_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);
        JungleTrapDoor = registerBlock("jungle_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);
        AcaciaTrapDoor = registerBlock("acacia_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);
        DarkOakTrapDoor = registerBlock("darkoak_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);
        registerBlock("retard_trapdoor", tabRedstone, Blocks.PLANKS, trapdoorConstructor, Material.WOOD);

        registerTileEntities(TileEntityWoolSign.class, TileEntityIceBlock.class, TileEntityTimer.class);
        registerEntityWithSpawn(EntityPigSpider.class, 0, 0x00eaff, EnumCreatureType.MONSTER, 50, 1, 2, forbiddenForest, Biomes.PLAINS, Biomes.EXTREME_HILLS, Biomes.FOREST, Biomes.ROOFED_FOREST, Biomes.COLD_TAIGA, Biomes.DESERT, Biomes.JUNGLE, Biomes.SWAMPLAND, Biomes.SAVANNA, Biomes.HELL, Biomes.MESA);
        registerEntityWithSpawn(EntityNetherCreeper.class, 0x004d16, 0xaa2400, EnumCreatureType.MONSTER, 30, 2, 3, Biomes.HELL);
        registerEntityWithSpawn(EntityThreeHeadCreeper.class, 0x0cff00, 0x068500, EnumCreatureType.MONSTER, 100, 2, 3, darkForest, forbiddenForest, Biomes.MUTATED_REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA_HILLS, Biomes.EXTREME_HILLS_WITH_TREES, Biomes.ROOFED_FOREST, Biomes.SWAMPLAND);
        registerEntityWithSpawn(EntityCowBrine.class, 0x421f00, 0, EnumCreatureType.MONSTER, 100, 1, 2, Biomes.SKY);
        registerEntityWithSpawn(EntityGoat.class, 0x252423, 0xab9c93, EnumCreatureType.CREATURE, 40, 2, 3, extremerHills, extremestHill, snowHills, Biomes.BIRCH_FOREST, Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_WITH_TREES, Biomes.FOREST);
        registerEntityWithSpawn(EntitySkeletonSpider.class, 0, 0x555555, EnumCreatureType.MONSTER, 0, 0, 0, Biomes.HELL);
        registerEntity(EntityChair.class, 1, false);
        registerEntity(EntityCureItem.class, 7, true);
        registerEntityWithSpawn(EntityPigman.class, 0xda6969, 0x743b3b, EnumCreatureType.MONSTER, 50, 1, 2, Biomes.PLAINS, Biomes.BEACH, Biomes.SWAMPLAND, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.MESA, Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES, Biomes.ROOFED_FOREST, forbiddenForest);
        registerEntityWithSpawn(EntityEnderSpider.class, 0x1f1c1c, 0x6d6d6d, EnumCreatureType.MONSTER, 0, 0, 0, Biomes.PLAINS);
        registerEntityWithSpawn(EntityClimberZombie.class, 0x008789, 0x6a2500, EnumCreatureType.MONSTER, 25, 1, 1, Biomes.FOREST, Biomes.DESERT, Biomes.EXTREME_HILLS, Biomes.JUNGLE);
        registerEntityWithSpawn(EntityWolfMan.class, 0, 0, EnumCreatureType.MONSTER, 0, 0, 0, Biomes.BEACH);
        addMobSpawn(EntitySnowman.class, EnumCreatureType.CREATURE, 50, 1, 3, iceDesert, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.TAIGA_HILLS, snowHills, iceHills);
        removeMobSpawn(EntityEnderman.class, Biomes.SKY);
        addMobSpawn(EntityEnderman.class, EnumCreatureType.MONSTER, 200, 1, 2, Biomes.SKY);
        addMobSpawn(EntityCaveSpider.class, EnumCreatureType.MONSTER, 50, 2, 4, forbiddenForest, darkForest);
        addMobSpawn(EntitySlime.class, EnumCreatureType.MONSTER, 30, 1, 3, darkForest);
        addMobSpawn(EntityWitch.class, EnumCreatureType.MONSTER, 25, 1, 2, darkForest);

        ArrayList<Biome> biomes = new ArrayList<>(MapGenVillage.VILLAGE_SPAWN_BIOMES);
        biomes.add(nsForest);
        biomes.add(iceDesert);
        biomes.add(Biomes.FOREST);
        biomes.add(Biomes.FOREST_HILLS);
        biomes.add(Biomes.ICE_PLAINS);
        biomes.add(Biomes.ROOFED_FOREST);
        biomes.add(Biomes.SWAMPLAND);
        biomes.add(Biomes.BIRCH_FOREST);
        biomes.add(Biomes.HELL);
        MapGenVillage.VILLAGE_SPAWN_BIOMES = biomes;

        Events events = new Events();
        MinecraftForge.EVENT_BUS.register(events);
        MinecraftForge.TERRAIN_GEN_BUS.register(events);
        NetworkRegistry.INSTANCE.registerGuiHandler("tcm", new TCMGuiHandler());
        GameRegistry.registerWorldGenerator(new WorldGens(), 0);
    }

    @EventHandler
    public void load(@SuppressWarnings("unused") FMLInitializationEvent event){
        setStepSound(SoundType.WOOD, OakLogFence, SpruceLogFence, BirchLogFence, JungleLogFence, AcaciaLogFence, DarkOakLogFence,
                RetardFence, OakLogGate, SpruceLogGate, BirchLogGate, JungleLogGate, AcaciaLogGate, DarkOakLogGate, RetardGate);

        GameRegistry.addSmelting(RawGoatMeat, new ItemStack(CookedGoatMeat), 0.35F);
        GameRegistry.addSmelting(RawSheepMeat, new ItemStack(CookedSheepMeat), 0.35F);
        GameRegistry.addSmelting(MarbleCobble, new ItemStack(MarbleStone), 0.35F);
        GameRegistry.addSmelting(NetherIronOre, new ItemStack(Items.IRON_INGOT), 0.6F);
        GameRegistry.addSmelting(NetherGoldOre, new ItemStack(Items.GOLD_INGOT), 0.9F);
        GameRegistry.addSmelting(Basalt, new ItemStack(SmoothBasalt), 0.35F);
        GameRegistry.addSmelting(Cactus, new ItemStack(Items.DYE, 1, 2), 0.3F);
        BrewingRecipeRegistry.addRecipe(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(Items.MILK_BUCKET), new ItemStack(CureItem));

        BiomeDictionary.addTypes(snowHills, BiomeDictionary.Type.COLD, BiomeDictionary.Type.MOUNTAIN);
        BiomeDictionary.addTypes(iceHills, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.COLD);
        BiomeDictionary.addTypes(darkForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
        BiomeDictionary.addTypes(extremestHill, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY);
        BiomeDictionary.addTypes(extremerHills, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY);
        BiomeDictionary.addTypes(iceDesert, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.PLAINS);
        BiomeDictionary.addTypes(forbiddenForest, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.DENSE);
        BiomeDictionary.addTypes(flatNothing, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.DRY);
        BiomeDictionary.addTypes(tundra, BiomeDictionary.Type.COLD, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(nsForest, BiomeDictionary.Type.FOREST);
        BiomeDictionary.addTypes(iceberg, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.SNOWY);

        setStepSound(SoundType.CLOTH, Blocks.WEB);
        Blocks.LADDER.setUnlocalizedName("oak_ladder");
        Blocks.WOODEN_BUTTON.setUnlocalizedName("oak_button");
        Blocks.TRAPDOOR.setUnlocalizedName("oak_trapdoor");
        Blocks.WOODEN_PRESSURE_PLATE.setUnlocalizedName("oak_preplate");
        Blocks.RED_MUSHROOM.setCreativeTab(null);
        Blocks.BROWN_MUSHROOM.setCreativeTab(null);
        Blocks.GRASS_PATH.setCreativeTab(CreativeTabs.DECORATIONS);
        Items.EGG.setMaxStackSize(64);
        Items.SNOWBALL.setMaxStackSize(64);
        Items.MINECART.setMaxStackSize(16);
        Items.IRON_PICKAXE.setHarvestLevel("pickaxe", 1);
    }

    @EventHandler
    public void server(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandExport());
        event.registerServerCommand(new CommandUndo());
        event.registerServerCommand(new CommandClear());
        event.registerServerCommand(new CommandHalfSphere());
        event.registerServerCommand(new CommandExpand());
    }



    private static void addMobSpawn(Class<? extends EntityLiving> entityClass, EnumCreatureType type, int spawnChance, int minSpawn, int maxSpawn, Biome... biome){
        EntityRegistry.addSpawn(entityClass, spawnChance, minSpawn, maxSpawn, type, biome);
    }

    private static void removeMobSpawn(Class<? extends EntityLiving> entityClass, Biome... biome){
        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.CREATURE, biome);
        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.AMBIENT, biome);
        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.MONSTER, biome);
    }

    @SafeVarargs
    private static void registerTileEntities(Class<? extends TileEntity>... tileEntityClass){
        for(Class<? extends TileEntity> tile : tileEntityClass) {
            GameRegistry.registerTileEntity(tile, new ResourceLocation("tcm:" + tile.getSimpleName()));
        }
    }

    @SafeVarargs
    private static<T> Constructor<T> getConstructor(Class<T> theClass, Class<?>... types) {
        try{
            Constructor<T> tempConstructor = theClass.getDeclaredConstructor(types);
            tempConstructor.setAccessible(true);
            return tempConstructor;
        }catch(Throwable e){
            e.printStackTrace();
            return null;
        }
    }
}