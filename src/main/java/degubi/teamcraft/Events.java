package degubi.teamcraft;

import degubi.teamcraft.block.*;
import degubi.teamcraft.block.interactive.*;
import degubi.teamcraft.block.vanilla.*;
import degubi.teamcraft.entity.*;
import degubi.teamcraft.gui.*;
import degubi.teamcraft.item.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.network.*;
import net.minecraft.client.renderer.block.statemap.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.player.EntityPlayer.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.play.client.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.*;
import net.minecraftforge.client.event.GuiScreenEvent.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.event.furnace.*;
import net.minecraftforge.event.terraingen.BiomeEvent.*;
import net.minecraftforge.event.world.BlockEvent.*;
import net.minecraftforge.event.world.ExplosionEvent.*;
import net.minecraftforge.fml.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event.*;
import net.minecraftforge.fml.common.gameevent.InputEvent.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.relauncher.*;

public final class Events{
    private int soundTicks;

    @SubscribeEvent
    public void fuelEvent(FurnaceFuelBurnTimeEvent event){
        Item item = event.getItemStack().getItem();

        if(item instanceof ItemDoors && ((ItemDoors)item).getBlock().getDefaultState().getMaterial() == Material.WOOD){
            event.setBurnTime(600);
        }else if(item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof BlockTables){
            event.setBurnTime(800);
        }else if(item == Main.NetherCoal){
            event.setBurnTime(2400);
        }else if(item == Item.getItemFromBlock(Main.NetherCoalBlock)){
            event.setBurnTime(20000);
        }else if(Block.getBlockFromItem(item) instanceof BlockLadder){
            event.setBurnTime(600);
        }
    }

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent event){
        if(event.getModID().equals("tcm")) {
            ConfigManager.sync("tcm", Type.INSTANCE);
        }
    }

    @SubscribeEvent
    public void explosionEvent(Detonate event){
        World world = event.getWorld();

        for(BlockPos pos : event.getAffectedBlocks()){
            Block block = world.getBlockState(pos).getBlock();

            if(block == Blocks.DIAMOND_ORE){
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.DIAMOND)));
            }else if(block == Blocks.COAL_ORE){
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COAL)));
            }
        }
    }

    @SubscribeEvent
    public void playerDeath(PlayerDropsEvent event){
        World world = event.getEntityPlayer().world;

        if(!world.getGameRules().getBoolean("keepInventory")) {
            TileEntityChest chest = new TileEntityChest();
            TileEntityChest chest2 = new TileEntityChest();
            BlockPos pos = event.getEntityPlayer().getPosition();
            BlockPos freePos = pos.west();

            if(world.getBlockState(pos.east()) == Blocks.AIR.getDefaultState()){
                freePos = pos.east();
            }

            int k = 0;
            for(EntityItem lope : event.getDrops()){
                ItemStack items = lope.getItem();

                if(k < 27) {
                    chest.setInventorySlotContents(k, items);
                }else{
                    chest2.setInventorySlotContents(k - 27, items);
                }
                ++k;
            }

            event.getDrops().clear();
            world.setBlockState(pos, Blocks.CHEST.getDefaultState());
            world.setTileEntity(pos, chest);

            if(k > 26) {
                world.setBlockState(freePos, Blocks.CHEST.getDefaultState());
                world.setTileEntity(freePos, chest2);
            }
        }
    }

    @SubscribeEvent
    public void livingUpdate(LivingUpdateEvent event){
        EntityLivingBase entity = event.getEntityLiving();

        if(!entity.world.isRemote && entity instanceof EntityWolf && entity.world.getCurrentMoonPhaseFactor() == 1 && entity.world.getCelestialAngleRadians(1F) > 3.14F && entity.world.getCelestialAngleRadians(1F) < 3.4F){
            EntityWolfMan wolfman = new EntityWolfMan(entity.world);
            wolfman.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0, 0);
            entity.world.spawnEntity(wolfman);
            entity.setDead();
        }
    }

    @SubscribeEvent
    public void playerInteract(RightClickBlock event){
        World world = event.getWorld();

        if(!world.isRemote){
            BlockPos pos = event.getPos();
            IBlockState blockAtPos = world.getBlockState(pos);
            EntityPlayer player = event.getEntityPlayer();

            if(blockAtPos.getBlock() == Blocks.CAULDRON){
                ItemStack stack = player.getHeldItemMainhand();

                if(!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(Blocks.WOOL)){
                    int level = blockAtPos.getValue(BlockCauldron.LEVEL).intValue();

                    if(level > 0 && stack.getMetadata() != 0){
                        event.setCanceled(true);
                        --level;
                        world.setBlockState(pos, blockAtPos.withProperty(BlockCauldron.LEVEL, Integer.valueOf(level)));
                        stack.shrink(1);

                        if (!player.inventory.addItemStackToInventory(new ItemStack(Blocks.WOOL))){
                            world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, new ItemStack(Blocks.WOOL)));
                        }else if (player instanceof EntityPlayerMP){
                            ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                        }
                    }
                }
            }else if(blockAtPos.getBlock() instanceof BlockBed && player.trySleep(pos) == SleepResult.OK){
                event.setCanceled(true);
                BlockBed bed = (BlockBed) blockAtPos.getBlock();
                Vec3d hitVec = event.getHitVec();

                bed.onBlockActivated(world, pos, blockAtPos, player, event.getHand(), event.getFace(), (float) hitVec.x, (float) hitVec.y, (float) hitVec.z);
                world.playSound(null, pos, Main.SLEEPING, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            if(blockAtPos.getBlock() instanceof BlockSign){
                player.openEditSign((TileEntitySign) world.getTileEntity(pos));
            }
        }
    }

    @SubscribeEvent
    public void endermanTeleport(EnderTeleportEvent event){
        EntityLivingBase entity = event.getEntityLiving();

        if(!entity.world.isRemote && entity instanceof EntityEnderman){
            if(entity.world.rand.nextInt(20) == 3){
                EntityEnderSpider spider = new EntityEnderSpider(entity.world);
                spider.setLocationAndAngles(entity.posX, entity.posY + 0.5D, entity.posZ, entity.world.rand.nextFloat() * 360.0F, 0.0F);
                entity.world.spawnEntity(spider);
            }
        }
    }

    @SubscribeEvent
    public void livingDrops(LivingDropsEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if(entity instanceof EntitySheep) {
            if(entity.world.rand.nextInt(3) == 1) {
                event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(Main.RawSheepMeat)));
            }
        }else if(entity instanceof EntityPig) {
            if(entity.world.rand.nextInt(4) == 1) {
                event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(Items.LEATHER)));
            }
        }
    }

    @SubscribeEvent
    public void harvestDrops(HarvestDropsEvent event){
        Block block = event.getState().getBlock();

        if(block == Blocks.BROWN_MUSHROOM || block == Blocks.BROWN_MUSHROOM_BLOCK){
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Main.BrownMushroomItem));
        }else if(block == Blocks.RED_MUSHROOM || block == Blocks.RED_MUSHROOM_BLOCK){
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Main.RedMushroomItem));
        }
    }

    @SubscribeEvent
    public void villageGenerate(GetVillageBlockID event){
        Block original = event.getOriginal().getBlock();

        if(event.getBiome() == Main.iceDesert || event.getBiome() == Biomes.ICE_PLAINS || event.getBiome() == Biomes.ICE_MOUNTAINS || event.getBiome() == Biomes.MUTATED_ICE_FLATS){
            if(original == Blocks.COBBLESTONE){
                event.setReplacement(Blocks.ICE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.PLANKS){
                event.setReplacement(Blocks.PACKED_ICE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.LOG){
                event.setReplacement(Blocks.ICE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.STONE_STAIRS || original == Blocks.OAK_STAIRS){
                event.setReplacement(Block.getBlockFromName("tcm:packedice_stairs").getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.GRAVEL || original == Blocks.DIRT){
                event.setReplacement(Blocks.SNOW.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.TORCH){
                event.setReplacement(Blocks.AIR.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_FENCE){
                event.setReplacement(Block.getBlockFromName("tcm:ice_wall").getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_DOOR){
                event.setReplacement(Main.IceDoor.getDefaultState().withProperty(BlockDoor.FACING, event.getOriginal().getValue(BlockDoor.FACING)).withProperty(BlockDoor.HALF, event.getOriginal().getValue(BlockDoor.HALF)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.LAVA){
                event.setReplacement(Blocks.PACKED_ICE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.GRAVEL){
                event.setReplacement(Blocks.GRASS_PATH.getDefaultState());
                event.setResult(Result.DENY);
            }
        }else if(event.getBiome() == Biomes.ROOFED_FOREST){
            if(original == Blocks.LOG){
                event.setReplacement(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_STAIRS){
                event.setReplacement(Blocks.DARK_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_FENCE){
                event.setReplacement(Blocks.DARK_OAK_FENCE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.COBBLESTONE){
                event.setReplacement(Blocks.STONEBRICK.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.WOODEN_PRESSURE_PLATE){
                event.setReplacement(Block.getBlockFromName("tcm:darkoak_preplate").getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.LADDER){
                event.setReplacement(Block.getBlockFromName("tcm:darkoak_ladder").getDefaultState().withProperty(BlockLadder.FACING, event.getOriginal().getValue(BlockLadder.FACING)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_DOOR){
                event.setReplacement(Blocks.DARK_OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, event.getOriginal().getValue(BlockDoor.FACING)).withProperty(BlockDoor.HALF, event.getOriginal().getValue(BlockDoor.HALF)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.STONE_STAIRS){
                event.setReplacement(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.GRAVEL){
                event.setReplacement(Blocks.GRASS_PATH.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.PLANKS){
                event.setReplacement(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK));
                event.setResult(Result.DENY);
            }else if(original == Blocks.LOG){
                event.setReplacement(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK));
                event.setResult(Result.DENY);
            }
        }else if(event.getBiome() == Biomes.BIRCH_FOREST || event.getBiome() == Biomes.BIRCH_FOREST_HILLS){
            if(original == Blocks.OAK_STAIRS){
                event.setReplacement(Blocks.BIRCH_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_FENCE){
                event.setReplacement(Blocks.BIRCH_FENCE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.COBBLESTONE){
                event.setReplacement(Blocks.BRICK_BLOCK.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.WOODEN_PRESSURE_PLATE){
                event.setReplacement(Block.getBlockFromName("tcm:birch_preplate").getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.LADDER){
                event.setReplacement(Block.getBlockFromName("tcm:birch_ladder").getDefaultState().withProperty(BlockLadder.FACING, event.getOriginal().getValue(BlockLadder.FACING)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_DOOR){
                event.setReplacement(Main.BirchLogDoor.getDefaultState().withProperty(BlockDoor.FACING, event.getOriginal().getValue(BlockDoor.FACING)).withProperty(BlockDoor.HALF, event.getOriginal().getValue(BlockDoor.HALF)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.STONE_STAIRS){
                event.setReplacement(Blocks.BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.GRAVEL){
                event.setReplacement(Blocks.GRASS_PATH.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.PLANKS){
                event.setReplacement(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH));
                event.setResult(Result.DENY);
            }else if(original == Blocks.LOG){
                event.setReplacement(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH));
                event.setResult(Result.DENY);
            }
        }else if(event.getBiome() == Biomes.HELL){
            if(original == Blocks.GRAVEL){
                event.setReplacement(Blocks.OBSIDIAN.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.COBBLESTONE){
                event.setReplacement(Blocks.NETHER_BRICK.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.LOG){
                event.setReplacement(Blocks.NETHERRACK.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.DIRT){
                event.setReplacement(Blocks.SOUL_SAND.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.CARROTS || original == Blocks.POTATOES || original == Blocks.WHEAT){
                event.setReplacement(Blocks.NETHER_WART.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.WATER){
                event.setReplacement(Blocks.LAVA.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_DOOR){
                event.setReplacement(Main.QuartzDoor.getDefaultState().withProperty(BlockDoor.FACING, event.getOriginal().getValue(BlockDoor.FACING)).withProperty(BlockDoor.HALF, event.getOriginal().getValue(BlockDoor.HALF)));
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_FENCE){
                event.setReplacement(Blocks.NETHER_BRICK_FENCE.getDefaultState());
                event.setResult(Result.DENY);
            }else if(original == Blocks.OAK_STAIRS){
                event.setReplacement(Blocks.NETHER_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, event.getOriginal().getValue(BlockStairs.FACING)).withProperty(BlockStairs.SHAPE, event.getOriginal().getValue(BlockStairs.SHAPE)));
                event.setResult(Result.DENY);
            }
        }
    }

    /******************* VANILLA FIXES **********************/
    @SubscribeEvent
    public void fixEndermanTeleport(EnderTeleportEvent event){
        if(event.getEntityLiving().world.isRemote){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void blockLeftClick(LeftClickBlock event){
        if(!event.getWorld().isRemote){
            ItemStack heldItem = event.getEntityPlayer().getHeldItemMainhand();

            if(!heldItem.isEmpty() && heldItem.getItem() == Main.PositionItem){
                if(heldItem.hasTagCompound()){
                    heldItem.getTagCompound().setIntArray("pos1", new int[]{event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()});
                    event.getEntityPlayer().sendMessage(new TextComponentString("Position 1 set! " + event.getPos().getX() + " " + event.getPos().getY() + " " + event.getPos().getZ()));
                }else{
                    heldItem.setTagCompound(new NBTTagCompound());
                    event.getEntityPlayer().sendMessage(new TextComponentString("NBT Tag Created!"));
                    heldItem.getTagCompound().setIntArray("pos1", new int[]{event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()});
                    event.getEntityPlayer().sendMessage(new TextComponentString("Position 1 set! " + event.getPos().getX() + " " + event.getPos().getY() + " " + event.getPos().getZ()));
                }
                event.setCanceled(true);
            }
        }
    }

    /*******************************CLIENT********************************/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiOpen(GuiOpenEvent event){
        if(event.getGui() instanceof GuiMainMenu) {
            event.setGui(new GuiTCMMain());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void modelExceptions(@SuppressWarnings("unused") ModelRegistryEvent event) {
        addExceptionToModelSheet(BlockFenceGate.POWERED, Main.OakLogGate, Main.SpruceLogGate, Main.BirchLogGate, Main.JungleLogGate, Main.AcaciaLogGate, Main.DarkOakLogGate, Main.RetardGate);
        addExceptionToModelSheet(WoolSSign.ROTATION, Main.WoolStandingSign);
        addExceptionToModelSheet(BlockHorizontal.FACING, Main.WoolWallSign, Main.IceSkeletonBlock, Main.IceCreeperBlock, Main.IceGoatBlock, Main.IcePigmanBlock, Main.IcePigSpiderBlock, Main.IceThreeHeadCreeperBlock, Main.IceZombieBlock);
        addExceptionToModelSheet(BlockLantern.ACTIVE, Main.Lantern);
        addExceptionToModelSheet(BlockChair.isOccupied, Main.WoodenChair);
        addExceptionToModelSheet(BlockAppleSapling.STAGE, Main.AppleSapling);
        addExceptionToModelSheet(BlockSlab.HALF, Main.DoubleSlabs1, Main.DoubleSlabs2, Main.DoubleSlabs3);

        IProperty<?>[] leafBaseProp = { BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE };
        ModelLoader.setCustomStateMapper(Main.AppleLeaves, new StateMap.Builder().ignore(leafBaseProp).build());
        ModelLoader.setCustomStateMapper(Main.bananaLeaves, new StateMap.Builder().ignore(leafBaseProp).build());
        ModelLoader.setCustomStateMapper(Main.cocoaLeaves, new StateMap.Builder().ignore(BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE, BlockAppleLeaves.hasApple).build());

        IProperty<?>[] soundBlockProps = { BlockSoundBlocks.redstoneState, BlockSoundBlocks.volume };
        ModelLoader.setCustomStateMapper(Main.SoundBlock1, new StateMap.Builder().ignore(soundBlockProps).build());
        ModelLoader.setCustomStateMapper(Main.SoundBlock2, new StateMap.Builder().ignore(soundBlockProps).build());
        ModelLoader.setCustomStateMapper(Main.SoundBlock3, new StateMap.Builder().ignore(soundBlockProps).build());
        ModelLoader.setCustomStateMapper(Main.SoundBlock4, new StateMap.Builder().ignore(soundBlockProps).build());
        ModelLoader.setCustomStateMapper(Main.SoundBlock5, new StateMap.Builder().ignore(soundBlockProps).build());
        ModelLoader.setCustomStateMapper(Main.SoundBlock6, new StateMap.Builder().ignore(soundBlockProps).build());
    }

    @SideOnly(Side.CLIENT)
    private static void addExceptionToModelSheet(IProperty<?> property, Block... theBlock){
        StateMap stateMap = new StateMap.Builder().ignore(property).build();

        for(Block block : theBlock) {
            ModelLoader.setCustomStateMapper(block, stateMap);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void clientTick(ClientTickEvent event){
        if(event.phase == Phase.START){
            Minecraft mc = Minecraft.getMinecraft();
            WorldClient world = mc.world;

            if(world != null && !mc.isGamePaused() && world.provider.getDimension() == 0 && !mc.player.isCreative()){
                ++soundTicks;

                if(soundTicks > 400){
                    float pitchRand = 0.9F + world.rand.nextFloat() / 2;

                    if(world.isThundering()){
                        mc.getSoundHandler().playSound(new PositionedSoundRecord(Main.THUNDER.getSoundName(), SoundCategory.AMBIENT, 0.4F, pitchRand, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F));
                        soundTicks = 300;
                    }else if(world.getSunBrightness(0.0F) > 0.2F && world.canBlockSeeSky(mc.player.getPosition())){
                        mc.getSoundHandler().playSound(new PositionedSoundRecord(Main.DAY.getSoundName(), SoundCategory.AMBIENT, 1.0F, pitchRand, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F));
                        soundTicks = 0 - world.rand.nextInt(350);
                    }else{
                        mc.getSoundHandler().playSound(new PositionedSoundRecord(Main.NIGHT.getSoundName(), SoundCategory.AMBIENT, 0.4F, pitchRand, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F));
                        soundTicks = 300;
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiInit(InitGuiEvent.Post event){
        GuiScreen gui = event.getGui();

        if(gui instanceof GuiOptions && Minecraft.getMinecraft().world != null){
            int buttonToRemove = Minecraft.getMinecraft().isSingleplayer() ? 3 : 2;
            List<GuiButton> buttonList = event.getButtonList();

            buttonList.remove(buttonToRemove);
            buttonList.add(new GuiButton(3, gui.width / 2 - 155, gui.height / 6 + 42, 150, 20, "Chunk Gui"));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void buttonPressed(ActionPerformedEvent event){
        if(event.getGui() instanceof GuiOptions && event.getButton().id == 3) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiChunkHandler(event.getGui()));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiRenderEvent(Text event){
        Minecraft mc = Minecraft.getMinecraft();

        if(ConfigMenu.enableFPSCounter && !mc.gameSettings.showDebugInfo) {
            event.getLeft().add("FPS: " + Minecraft.getDebugFPS());
        }

        if(ConfigMenu.enablePingCounter && !mc.isSingleplayer()){
            try{
                event.getLeft().add("Ping: " + mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime());
            }catch(NullPointerException exc){}
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void fovUpdateEvent(FOVModifier event){
        if(ConfigMenu.enableZoom && ClientThings.zoomBind.isKeyDown()) {
            event.setFOV(20F);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void keyPressedEvent(@SuppressWarnings("unused") KeyInputEvent event){
        Minecraft mc = Minecraft.getMinecraft();

        if(mc.player.isCreative()) {
            KeyBinding[] binds = ClientThings.binds;

            fireCommandKeybind(binds[0], ConfigMenu.customBind1);
            fireCommandKeybind(binds[1], ConfigMenu.customBind2);
            fireCommandKeybind(binds[2], ConfigMenu.customBind3);
            fireCommandKeybind(binds[3], ConfigMenu.customBind4);
            fireCommandKeybind(binds[4], ConfigMenu.customBind5);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void fireCommandKeybind(KeyBinding bind, String commandStr) {
        if(bind.isKeyDown() && !commandStr.isEmpty()){
            String[] commands = commandStr.split(";");
            NetHandlerPlayClient connection = Minecraft.getMinecraft().getConnection();

            for(String command : commands) {
                connection.sendPacket(new CPacketChatMessage(command));
            }
        }
    }
}