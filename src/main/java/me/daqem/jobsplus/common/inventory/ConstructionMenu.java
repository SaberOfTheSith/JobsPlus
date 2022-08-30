package me.daqem.jobsplus.common.inventory;

import me.daqem.jobsplus.SideProxy;
import me.daqem.jobsplus.common.crafting.ConstructionRecipe;
import me.daqem.jobsplus.handlers.HotbarMessageHandler;
import me.daqem.jobsplus.init.ModBlocks;
import me.daqem.jobsplus.init.ModMenuTypes;
import me.daqem.jobsplus.init.ModRecipes;
import me.daqem.jobsplus.utils.ChatColor;
import me.daqem.jobsplus.utils.JobGetters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ConstructionMenu extends RecipeBookMenu<CraftingContainer> {

    private final CraftingContainer constructingSlots = new CraftingContainer(this, 5, 5);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private CompoundTag enabledJobsTag;
    private Player player;

    //Doesn't do Server side.
    public ConstructionMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
        this.enabledJobsTag = buf.readNbt();
        this.player = inventory.player;
        if (enabledJobsTag == null || enabledJobsTag.isEmpty())
            HotbarMessageHandler.sendHotBarMessageClient(ChatColor.red() + "Press '" + SideProxy.Client.OPEN_GUI_KEYBIND.getKey().getDisplayName().getString().toUpperCase() + "' first, and start a job.");
    }

    //Does Server and Client side.
    public ConstructionMenu(int containerId, Inventory inventory, final ContainerLevelAccess access) {
        super(ModMenuTypes.CONSTRUCTION.get(), containerId);
        this.access = access;
        this.player = inventory.player;
        this.addSlot(new ResultSlot(inventory.player, this.constructingSlots, this.resultSlots, 0, -40, 130));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 170));
        }

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                this.addSlot(new Slot(constructingSlots, j + i * 5, 44 + j * 18, 5 + i * 18));
            }
        }
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftingContainer, ResultContainer resultContainer) {
        if (!level.isClientSide && level.getServer() != null) {
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ConstructionRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(ModRecipes.CONSTRUCTION_TYPE.get(), craftingContainer, level);
            if (optional.isPresent()) {
                ConstructionRecipe constructionRecipe = optional.get();
                if (resultContainer.setRecipeUsed(level, serverplayer, constructionRecipe)) {
                    if (JobGetters.getJobLevel(player, constructionRecipe.getJob()) >= constructionRecipe.getRequiredLevel()) {
                        itemstack = constructionRecipe.assemble(craftingContainer);
                    }
                }
            }

            resultContainer.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.access.execute((p_39386_, p_39387_) -> {
            slotChangedCraftingGrid(this, p_39386_, this.player, this.constructingSlots, this.resultSlots);
        });
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemStack = itemStack1.copy();
            if (i <= 35) {
                if (!this.moveItemStackTo(itemStack1, 36, 61, false)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack1, itemStack);
            } else if (i <= 60) {
                if (!this.moveItemStackTo(itemStack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack1.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
            if (i == 0) {
                player.drop(itemStack1, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.CONSTRUCTION_TABLE.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((p_39575_, p_39576_) -> this.clearContainer(player, this.constructingSlots));
    }

    public CompoundTag getDataTag() {
        return enabledJobsTag;
    }

    public CraftingContainer getConstructingSlots() {
        return constructingSlots;
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents stackedContents) {
        this.constructingSlots.fillStackedContents(stackedContents);
    }

    @Override
    public void clearCraftingContent() {
        this.constructingSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(@NotNull Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.constructingSlots, this.player.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.constructingSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.constructingSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 26;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int i) {
        return i != this.getResultSlotIndex();
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    public Player getPlayer() {
        return player;
    }
}
