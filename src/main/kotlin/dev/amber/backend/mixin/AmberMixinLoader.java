package dev.amber.backend.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name("AmberMixinLoader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class AmberMixinLoader implements IFMLLoadingPlugin {

    /* This is NOT using LambdaMod, as importing it causes the issue described here: https://github.com/SpongePowered/Mixin/issues/388 */
    public static final Logger log = LogManager.getLogger("Amber");
    private static boolean isObfuscatedEnvironment = false;

    public AmberMixinLoader() {
        log.info("Amber init mixin...");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.amber.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    // -Dfml.coreMods.load=dev.amber.backend.mixin.AmberMixinLoader
    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}