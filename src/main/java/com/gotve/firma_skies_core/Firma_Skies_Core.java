package com.gotve.firma_skies_core;

import com.gotve.firma_skies_core.datagen.BlockTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(Firma_Skies_Core.MODID)
public class Firma_Skies_Core {

    public static final String MODID = "firma_skies_core";
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    public Firma_Skies_Core() {

        MinecraftForge.EVENT_BUS.register(this);

        bus.addListener(this::gatherData);
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {

        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),

                new BlockTags(
                        packOutput,
                        lookupProvider,
                        MODID,
                        existingFileHelper
                )
        );
    }
}
