package com.gotve.firma_skies_core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(Firma_Skies_Core.MODID)
public class Firma_Skies_Core {

    public static final String MODID = "firma_skies_core";
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    public Firma_Skies_Core() {

        MinecraftForge.EVENT_BUS.register(this);
    }
}
