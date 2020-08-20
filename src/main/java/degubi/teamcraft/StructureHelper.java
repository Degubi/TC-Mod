package degubi.teamcraft;

import com.mojang.realmsclient.gui.*;
import java.io.*;
import java.nio.file.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;

public final class StructureHelper {
    public static final String BLOCKNAME = "blockName";
    public static final String BLOCKCOUNT = "blockCount";
    public static final String BLOCKCOORDS = "blockCoords";
    public static final String BLOCKPROPERTIES = "blockCoords";
    
    public static void setBlocksfromFile(World world, BlockPos startPos, String name, @SuppressWarnings("unused") NBTTagCompound itemTag, EntityPlayer player){
        NBTTagCompound tag = readCompressedTag(name);
        if(tag != null){
            int blockCount = tag.getInteger(BLOCKCOUNT);
            NBTTagCompound backupTag = new NBTTagCompound();
            
            for(int ID = 1; ID <= blockCount; ++ID){
                BlockPos setPos = getPosFromTag(tag, BLOCKCOORDS, ID);
                IBlockState setBlock = getBlockFromID(tag, ID).getDefaultState();
                
                //createBackup(itemTag, setPos, world, backupTag, ID);
                if(hasMetadata(tag, ID)){
                    //setBlockState(world, startPos, setPos, setBlock.getBlock().getDefaultState().withProperty(property, value)getMetaFromTag(tag, ID)));
                }else{
                    world.setBlockState(startPos.add(setPos.getX(), setPos.getY(), setPos.getZ()), setBlock.getBlock().getDefaultState(), 2);
                }
            }
            if(world.isRemote){
                writeCompressedTag(backupTag, name + "_backup");
            }
        }else{
            if(world.isRemote){
                player.sendMessage(new TextComponentString(ChatFormatting.RED + "The file: " + name + " was not found"));
            }
        }
    }
    
    /*******HELPERS*********/
    /*******HELPERS*********/
    /*******HELPERS*********/
    
    /*private static int getMetaFromTag(NBTTagCompound tag, int ID){
        return tag.getIntArray(BLOCKDATA + ID)[4];
    }*/
    public static BlockPos getPosFromTag(NBTTagCompound tag, String name, int ID){
        return new BlockPos(tag.getIntArray(name + ID)[0], tag.getIntArray(name + ID)[1], tag.getIntArray(name + ID)[2]);
    }
    
    private static Block getBlockFromID(NBTTagCompound tag, int ID){
        System.out.println(tag.getString(BLOCKNAME + ID));
        return Block.getBlockFromName(tag.getString(BLOCKNAME + ID));
    }
    
    private static boolean hasMetadata(NBTTagCompound tag, int ID){
        return tag.hasKey(BLOCKPROPERTIES + ID);
    }
    
    public static void clearExportFolder(int deleteMode, EntityPlayer player){
        try{
            Files.walk(Paths.get("./structures/")).filter(Files::isRegularFile).forEach(filePath -> {
                    if(deleteMode == 0){
                        filePath.toFile().delete();
                        player.sendMessage(new TextComponentString(filePath.getFileName().toString() + " was deleted!"));
                    }else if(deleteMode == 1){
                        if(!filePath.getFileName().toString().contains("backup")){
                            filePath.toFile().delete();
                            player.sendMessage(new TextComponentString(filePath.getFileName().toString() + " was deleted!"));
                        }
                    }else{
                        if(filePath.getFileName().toString().contains("backup")){
                            filePath.toFile().delete();
                            player.sendMessage(new TextComponentString(filePath.getFileName().toString() + " was deleted!"));
                        }
                    }
              });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private static NBTTagCompound readCompressedTag(String name){
        return readCompressedTag(name, "./structures");
    }
    
    private static NBTTagCompound readCompressedTag(String name, String path){
        try(final DataInputStream stream = new DataInputStream(new FileInputStream(path + "/" + name + ".deg"))) {
            return CompressedStreamTools.read(stream);
            //return CompressedStreamTools.readCompressed(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void writeCompressedTag(NBTTagCompound setTag, String name){
        /*new Thread(() -> {
            try(final FileOutputStream stream = new FileOutputStream("./structures/" + name + ".deg")){
                CompressedStreamTools.writeCompressed(setTag, stream);
            }catch(IOException e){
                e.printStackTrace();
            }
        },"Structure Exporter").start();
        */
        new Thread(() -> {
            try {
                File file = new File("./structures/" + name + ".deg");
                new File("./structures").mkdir();
                CompressedStreamTools.write(setTag, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"Structure Exporter").start();
    }
    /*private static void createBackup(NBTTagCompound itemTag, BlockPos setPos, World world, NBTTagCompound backupTag, int ID){
    BlockPos backupPos = getPosFromTag(itemTag, "clickPos", 0);
    BlockPos lolPos = backupPos.add(setPos.getX(), setPos.getY(), setPos.getZ());
    IBlockState originalBlock = world.getBlockState(lolPos);
    
    if(originalBlock.getBlock().getMetaFromState(originalBlock) != 0){
        backupTag.setIntArray(StructureHelper.BLOCKDATA + ID, new int[]{lolPos.getX(), lolPos.getY(), lolPos.getZ(), Block.getIdFromBlock(originalBlock.getBlock()), originalBlock.getBlock().getMetaFromState(originalBlock)});
    }else{
        backupTag.setIntArray(StructureHelper.BLOCKDATA + ID, new int[]{lolPos.getX(), lolPos.getY(), lolPos.getZ(), Block.getIdFromBlock(originalBlock.getBlock())});
    }
    backupTag.setInteger(BLOCKCOUNT, ID);
}*/
    /*public static void writeCompressedTag(NBTTagCompound setTag, String fileName, String path){
    new Thread("Structure Exporter"){
        @Override
        public void run() {
            System.out.println(path);
            try(DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(new File(path + "/" + fileName + ".deg")))){
                CompressedStreamTools.writeCompressed(setTag, dataoutputstream);
                dataoutputstream.close();
            }catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }.start();
}*/
}