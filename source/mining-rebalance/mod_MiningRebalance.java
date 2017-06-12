package net.minecraft.src;

import java.util.List;

public class mod_MiningRebalance extends BaseMod {

	public mod_MiningRebalance() { 
		for (int i = 0; i < 256; ++i) {
			if (Block.blocksList[i] instanceof BlockOre) {
				ItemBlock ib = (ItemBlock) Item.itemsList[i];
				ib.setMaxStackSize(1);
			}
		} 	

		List recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = recipes.size() - 1; i >= 0; --i) {
			IRecipe r = (IRecipe) recipes.get(i);
			if (r.getRecipeOutput().itemID == Block.rail.blockID) {
				recipes.remove(i);
			}
		}

        ModLoader.AddRecipe(new ItemStack(Block.rail, 64), 
						    new Object[] 
							{ "X X", 
							  "X#X", 
							  "X X",
							Character.valueOf('X'), Item.ingotIron, 
							Character.valueOf('#'), Item.stick});

	}

	public String Version() {
		return "0.1";
	}

}
