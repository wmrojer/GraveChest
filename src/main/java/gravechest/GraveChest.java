package gravechest;

import gravechest.blocks.BlockGraveChest;
import gravechest.info.ModInfo;
import gravechest.proxies.CommonProxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION_NUMBER, dependencies = ModInfo.DEPENDENCIES)
public class GraveChest {

    @Mod.Instance
    public static GraveChest instance;

	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
    
    public static Block blockGrave;
    public static Block blockGraveChest;
    public static Logger log = LogManager.getLogger(ModInfo.MOD_ID);
    public static boolean useChest = true;
    public static int graveChestRenderID;
    
    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        blockGraveChest = new BlockGraveChest();
        GameRegistry.registerBlock(blockGraveChest, blockGraveChest.getUnlocalizedName());
    }
    
	@EventHandler
	public void init(FMLInitializationEvent event) {
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

    	proxy.registerEvents();
    	proxy.registerTileEntities();
		proxy.registerRenderers();
	}
	
}

