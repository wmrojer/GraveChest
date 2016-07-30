package gravechest.info;

public class ModInfo {

	public static final String MOD_ID = "gravechest";
	public static final String MOD_NAME = "Grave Chest";
	public static final String DEPENDENCIES = "required-after:Forge@[10.13.4.1558,);";
	public static final String VERSION_NUMBER = "@VERSION@";
	
	public static final String GUI_FACTORY_CLASS = "gravechest.ConfigGuiFactory";
	public static final String SERVER_PROXY_CLASS = "gravechest.proxies.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "gravechest.proxies.ClientProxy";
	
	public static final int GRAVE_CHEST_GUI = 1;

}
