package net.minecraft.src;

import java.util.Random;

public class BlockClaySieve extends Block {

    public static final int tick_rate = 3;

    public static final int drop_min = 3;
    public static final int drop_max = 8;

    public static final int meta_empty  = 0;
    public static final int meta_filled = 1;
    public static final int meta_sifted = 2;
    public static final int meta_wet    = 3;
    public static final int meta_dry    = 4;

    public int textures[];

    public BlockClaySieve(int id, String name, int[] textures) {
        super(id, Material.wood);
        this.textures = textures;

        /* vanilla calls */
        setHardness(2.5F);
        setBlockName(name);
        setTickOnLoad(true);

        /* modloader calls */
        ModLoader.RegisterBlock(this);
        ModLoader.AddName(this, name);
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if (side == 0) {
            return Block.planks.blockIndexInTexture;
        } else if (side == 1) {
            return textures[meta];
        }

        return Block.chest.blockIndexInTexture;
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        final ItemStack item = player.inventory.getCurrentItem();
        final int meta = world.getBlockMetadata(x, y, z);

        if (meta == meta_dry) {
            harvest_sieve(world, x, y, z);
            set_meta(world, x, y, z, meta_empty);
            return true;
        }

        if (item == null) {
            return true;
        }

        switch (meta) {
            case meta_empty:
                if (item.itemID == Block.dirt.blockID) {
                    set_meta(world, x, y, z, meta_filled);
                    item.stackSize--;
                }
                break;

            case meta_filled:
                if (Item.itemsList[item.itemID] instanceof ItemHoe) {
                    set_meta(world, x, y, z, meta_sifted);
                    item.damageItem(1, player);
                }
                break;

            case meta_sifted:
                if (item.itemID == Item.bucketWater.shiftedIndex) {
                    set_meta(world, x, y, z, meta_wet);
                    item.itemID = Item.bucketEmpty.shiftedIndex;
                }
                break;
        }

        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        final int meta = world.getBlockMetadata(x, y, z);
        if (meta != meta_wet) {
            return;
        }

        int mul = dry_rate(world, x, y, z);
        if (random.nextInt(tick_rate) < mul) {
            set_meta(world, x, y, z, meta_dry);
        }
    }

    public void set_meta(World world, int x, int y, int z, int meta) {
            world.setBlockAndMetadataWithNotify(x, y, z, blockID, meta);
    }

    // todo
    public int dry_rate(World world, int x, int y, int z) {
        int rate = world.canBlockSeeTheSky(x, y, z) ? 1 : 0;
        if (block_near(world, x, y, z, Block.fire.blockID)) {
            rate = 2;
        }

        if (block_near(world, x, y, z, Block.lavaMoving.blockID)) {
            rate = 3;
        }
        
        if (block_near(world, x, y, z, Block.lavaStill.blockID)) {
            rate = 3;
        }

        return rate;
    }

    public boolean block_near(World world, int x, int y, int z, int id) {
        for (int i = -1; i < 2; ++i)
            for (int k = -1; k < 1; ++k)
                for (int j = -1; j < 2; ++j)
                    if (world.getBlockId(x + i, y + k, z + j) == id)
                        return true;

        return false;
    }

    public void harvest_sieve(World world, int x, int y, int z) {
        if(world.multiplayerWorld) return;

        final Random rand = world.rand;

        final int amount = drop_min + rand.nextInt(drop_max - drop_min + 1);
        ItemStack drop = new ItemStack(Item.clay, amount);

        float f = 0.7F;
        double d =  (double) (rand.nextFloat() * f + 1.0F - f) * 0.5D + x;
        double d1 = (double) (rand.nextFloat() * f + 1.0F - f) * 0.5D + y + 0.5;
        double d2 = (double) (rand.nextFloat() * f + 1.0F - f) * 0.5D + z;
        EntityItem entityitem = new EntityItem(world, d, d1, d2, drop);
        entityitem.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(entityitem);
    }
}
