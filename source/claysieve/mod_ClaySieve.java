package net.minecraft.src;

public class mod_ClaySieve extends BaseMod {

    public static final int claySieveID = 204;
    public static BlockClaySieve claySieve;

    public mod_ClaySieve() {
        final int tex[] = { 
                            ModLoader.addOverride("/terrain.png", "/claysieve/sieve_empty.png"),
                            ModLoader.addOverride("/terrain.png", "/claysieve/sieve_filled.png"),
                            ModLoader.addOverride("/terrain.png", "/claysieve/sieve_sifted.png"),
                            ModLoader.addOverride("/terrain.png", "/claysieve/sieve_wet.png"),
                            ModLoader.addOverride("/terrain.png", "/claysieve/sieve_dry.png"),
                           };
        claySieve = new BlockClaySieve(claySieveID, "Clay Sieve", tex);

        ModLoader.AddRecipe(new ItemStack(claySieve),
                            new Object[] {
                            "WSW",
                            "WRW",
                            Character.valueOf('W'), Block.planks,
                            Character.valueOf('R'), Block.stairSingle,
                            Character.valueOf('S'), Item.silk });
    }

    public String Version() {
        return "0.1";
    }
}
