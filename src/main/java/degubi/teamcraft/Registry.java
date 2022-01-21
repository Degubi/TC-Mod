package degubi.teamcraft;

import degubi.teamcraft.item.*;
import java.lang.reflect.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.launchwrapper.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.BiomeManager.*;
import net.minecraftforge.event.RegistryEvent.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.registries.*;

@EventBusSubscriber(modid = "tcm")
public final class Registry {
    private static int entityID = 0, biomeCounter = 0, networkID = 0, slabCounter = 0, soundCounter = 0, blockCounter = 0, itemCounter = 0, doorCounter = 0;
    static Block[] blockArray = new Block[286];
    static Item[] itemArray = new Item[302];
    static SoundEvent[] soundArray = new SoundEvent[14];
    static Biome[] biomeArray = new Biome[11];
    static EntityEntry[] entityArray = new EntityEntry[12];
    static Block[] doorArray = new Block[17];
    static Item[] slabArray = new Item[3];
    private static final Method setStepSound = Registry.getMethod(Block.class, "func_149672_a", "setSoundType", SoundType.class);

    private Registry(){}

    public static<T extends Block> Block registerBlock(String name , CreativeTabs tab, Block modelBlock, Constructor<T> constructor, Object... pars){
        try{
            Block block = constructor.newInstance(pars).setCreativeTab(tab).setHardness(modelBlock.getDefaultState().getBlockHardness(null, null));
            setStepSound(modelBlock.getSoundType(null, null, null, null), block);
            registerBlock(block, name);
            return block;
        }catch(Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static<T extends Item> void registerItem(String name, CreativeTabs tab, Constructor<T> constructor, Object... pars){
        try{
            registerItem(constructor.newInstance(pars).setCreativeTab(tab), name);
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    public static Block registerBlock(Block block, String name){
        return registerBlockWItem(new ItemBlock(block), name);
    }

    public static void registerVanillaSlab(Block singleSlab, Block doubleSlab, int counter){
        blockArray[blockCounter++] = singleSlab.setRegistryName("single_slabs" + counter);
        blockArray[blockCounter++] = doubleSlab.setRegistryName("double_slabs" + counter);
        slabArray[slabCounter++] = new ItemSlab(singleSlab, (BlockSlab) singleSlab, (BlockSlab) doubleSlab).setRegistryName(singleSlab.getRegistryName());
    }

    public static Block registerBlockNoItem(Block block, String name){
        return blockArray[blockCounter++] = block.setRegistryName(name).setUnlocalizedName(name);
    }

    public static Block registerDoor(Block door, String name){
        return doorArray[doorCounter++] = registerBlockWItem(new ItemDoors(door), name);
    }

    public static Block registerBlockWItem(ItemBlock itemBlock, String name){
        registerBlockNoItem(itemBlock.getBlock(), name);
        itemArray[itemCounter++] = itemBlock.setRegistryName(name);
        return itemBlock.getBlock();
    }

    public static Item registerItem(Item item, String name){
        return itemArray[itemCounter++] = item.setRegistryName(name).setUnlocalizedName(name);
    }

    public static SoundEvent registerSound(String soundName) {
        return soundArray[soundCounter++] = new SoundEvent(new ResourceLocation(soundName)).setRegistryName(soundName);
    }

    public static void setStepSound(SoundType sound, Block... instance){
        try{
            for(Block blocks : instance) {
                setStepSound.invoke(blocks, sound);
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    public static Biome registerBiome(Biome biome, String name, int weight, BiomeType type){
        biomeArray[biomeCounter++] = biome.setRegistryName(name);
        BiomeManager.addBiome(type, new BiomeEntry(biome, weight));
        BiomeProvider.allowedBiomes.add(biome);
        return biome;
    }

    public static void registerEntity(Class<? extends Entity> entityclass, int updateFrequency, boolean sendVelUpdates) {
        entityArray[entityID] = EntityEntryBuilder.create()
                                                  .entity(entityclass)
                                                  .id(entityclass.getSimpleName(), entityID++)
                                                  .name("tcm." + entityclass.getSimpleName().substring(6))
                                                  .tracker(64, updateFrequency, sendVelUpdates)
                                                  .build();
    }

    public static void registerEntityWithSpawn(Class<? extends Entity> entityclass, int backColor, int frontColor, EnumCreatureType type, int spawnChance, int minSpawnWeight, int maxSpawnWeight, Biome... biomes) {
        entityArray[entityID] = EntityEntryBuilder.create()
                                                  .entity(entityclass)
                                                  .id(entityclass.getSimpleName(), entityID++)
                                                  .name("tcm." + entityclass.getSimpleName().substring(6))
                                                  .tracker(64, 3, true)
                                                  .egg(backColor, frontColor)
                                                  .spawn(type, spawnChance, minSpawnWeight, maxSpawnWeight, biomes)
                                                  .build();
    }

    @SuppressWarnings("unchecked")
    public static SimpleNetworkWrapper createNewServerChannel(String channelName, Class handlerClass, Class<? extends IMessage> messageClass){
        SimpleNetworkWrapper channelObject = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        channelObject.registerMessage(handlerClass, messageClass, networkID++, Side.SERVER);
        return channelObject;
    }

    @SubscribeEvent
    public static void registerEntities(Register<EntityEntry> event){
        event.getRegistry().registerAll(entityArray);
    }

    @SubscribeEvent
    public static void registerBlocks(Register<Block> event){
        event.getRegistry().registerAll(blockArray);
    }

    @SubscribeEvent
    public static void registerItems(Register<Item> event){
        event.getRegistry().registerAll(itemArray);
        event.getRegistry().registerAll(slabArray);
    }

    @SubscribeEvent
    public static void registerSounds(Register<SoundEvent> event) {
        event.getRegistry().registerAll(soundArray);
    }

    @SubscribeEvent
    public static void registerBiomes(final Register<Biome> event) {
        event.getRegistry().registerAll(biomeArray);
    }

    @SubscribeEvent
    public static void registerRecipes(Register<IRecipe> event){
        IForgeRegistryModifiable<IRecipe> modRegistry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        modRegistry.remove(new ResourceLocation("minecraft:wooden_pressure_plate"));
        modRegistry.remove(new ResourceLocation("minecraft:wooden_button"));
        modRegistry.remove(new ResourceLocation("minecraft:ladder"));
        modRegistry.remove(new ResourceLocation("minecraft:trapdoor"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(@SuppressWarnings("unused") ModelRegistryEvent event){
        StateMap ignorePoweredDoorMap = new StateMap.Builder().ignore(BlockDoor.POWERED).build();

        for(Block block : doorArray) {
            ModelLoader.setCustomStateMapper(block, ignorePoweredDoorMap);
        }

        for(Item items : itemArray) {
            ModelLoader.setCustomModelResourceLocation(items, 0, new ModelResourceLocation(items.getRegistryName(), "inventory"));
        }

        for(Item item : slabArray) {
            for(int asd = 0; asd < 8; ++ asd) {
                ModelLoader.setCustomModelResourceLocation(item, asd, new ModelResourceLocation(item.getRegistryName(), "inventory_" + asd));
            }
        }
    }

    private static final int BIRCHPLANKS = 0xd7cc8e;
    private static final int SPRUCEPLANKS = 7426864;
    private static final int JUNGLEPLANKS = 9590071;
    private static final int ACACIAPLANKS = 14642228;
    private static final int DARKOAKPLANKS = 0x3e2912;
    private static final int SPRUCELEAF = 6396257;
    private static final int BIRCHLEAF = 8431445;
    private static final int OTHERLEAF = 4764952;
    private static final int GOLD = 0xe9da37;
    private static final int STONE = 0x979797;
    private static final int QUARTZ = 0xede9e3;
    private static final int EMERALD = 0x59e781;
    private static final int ICE = 0x8ab3fa;
    private static final int PACKEDICE = 0xb5cfff;
    private static final int OAKLOG = 0x81663e;
    private static final int SPRUCELOG = 0x362413;
    private static final int BIRCHLOG = 0xa9a5a0;
    private static final int JUNGLELOG = 0x443e15;
    private static final int ACACIALOG = 0x605a51;
    private static final int DARKOAKLOG = 0x1b150c;
    private static final int LASULIT = 0x2338b1;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerColorItems(ColorHandlerEvent.Item event) {
        event.getItemColors().registerItemColorHandler((stack, index) -> LASULIT, Main.LapisBoots, Main.LapisChestPlate, Main.LapisHelmet, Main.LapisLeggings);
        event.getItemColors().registerItemColorHandler((stack, index) -> EMERALD, Main.EmeraldBoots, Main.EmeraldChestPlate, Main.EmeraldHelmet, Main.EmeraldLeggings);
        event.getItemColors().registerItemColorHandler((stack, index) -> JUNGLEPLANKS, Items.JUNGLE_DOOR);
        event.getItemColors().registerItemColorHandler((stack, index) -> ACACIAPLANKS, Items.ACACIA_DOOR);
        event.getItemColors().registerItemColorHandler((stack, index) -> GOLD, Main.GoldDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> STONE, Main.CobbleDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> QUARTZ, Main.QuartzDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> EMERALD, Main.EmeraldDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> OAKLOG, Main.OakLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> SPRUCELOG, Main.SpruceLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> BIRCHLOG, Main.BirchLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> JUNGLELOG, Main.JungleLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> ACACIALOG, Main.AcaciaLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> DARKOAKLOG, Main.DarkOakLogDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> ICE, Main.IceDoor);
        event.getItemColors().registerItemColorHandler((stack, index) -> PACKEDICE, Main.PackedIceDoor);

        registerBlockColor(Main.BirchTrapDoor, BIRCHPLANKS, event);
        registerBlockColor(Main.JungleTrapDoor, JUNGLEPLANKS, event);
        registerBlockColor(Main.SpruceTrapDoor, SPRUCEPLANKS, event);
        registerBlockColor(Main.DarkOakTrapDoor, DARKOAKPLANKS, event);
        registerBlockColor(Main.AcaciaTrapDoor, ACACIAPLANKS, event);
        registerBlockColor(Main.BirchLeafBush, BIRCHLEAF, event);
        registerBlockColor(Main.SpruceLeafBush, SPRUCELEAF, event);

        Block[] blocks = { Main.AppleLeaves, Main.OakLeafBush, Main.JungleLeafBush };
        event.getBlockColors().registerBlockColorHandler((state, world, pos, index1) -> world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic(), blocks);
        event.getItemColors().registerItemColorHandler((stack1, index2) -> OTHERLEAF, blocks);
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockColor(Block block, int colorCode, ColorHandlerEvent.Item event) {
        event.getBlockColors().registerBlockColorHandler((stack, tintIndex, pos, pass) -> colorCode, block);
        event.getItemColors().registerItemColorHandler((stack, tintIndex) -> colorCode, block);
    }

    private static Method getMethod(Class<?> getClass, String realName, String eclipseName, Class<?>... parameters) {
        try{
            Method method = getClass.getDeclaredMethod(((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")).booleanValue() ? eclipseName : realName, parameters);
            method.setAccessible(true);
            return method;
        }catch(Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}