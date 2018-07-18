package cr0s.warpdrive.item;

import cr0s.warpdrive.Commons;
import cr0s.warpdrive.WarpDrive;
import cr0s.warpdrive.api.IParticleContainerItem;
import cr0s.warpdrive.api.Particle;
import cr0s.warpdrive.api.ParticleRegistry;
import cr0s.warpdrive.api.ParticleStack;
import cr0s.warpdrive.data.Vector3;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemElectromagneticCell extends ItemAbstractBase implements IParticleContainerItem {
	
	private static final String AMOUNT_TO_CONSUME_TAG = "amountToConsume";
	
	public ItemElectromagneticCell(final String registryName) {
		super(registryName);
		
		setMaxDamage(0);
		setMaxStackSize(1);
		setUnlocalizedName("warpdrive.atomic.electromagnetic_cell");
		setHasSubtypes(true);
	}
	
	// @TODO MC1.10 rendering
	//	icons[ 0] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-empty");
	//	icons[ 1] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-20");
	//	icons[ 2] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-40");
	//	icons[ 3] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-60");
	//	icons[ 4] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-80");
	//	icons[ 5] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-100");
	//	icons[ 6] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-blue-full");
	//	icons[ 7] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-20");
	//	icons[ 8] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-40");
	//	icons[ 9] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-60");
	//	icons[10] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-80");
	//	icons[11] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-100");
	//	icons[12] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-green-full");
	//	icons[13] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-20");
	//	icons[14] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-40");
	//	icons[15] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-60");
	//	icons[16] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-80");
	//	icons[17] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-100");
	//	icons[18] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-pink-full");
	//	icons[19] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-20");
	//	icons[20] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-40");
	//	icons[21] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-60");
	//	icons[22] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-80");
	//	icons[23] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-100");
	//	icons[24] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-red-full");
	//	icons[25] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-20");
	//	icons[26] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-40");
	//	icons[27] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-60");
	//	icons[28] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-80");
	//	icons[29] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-100");
	//	icons[30] = iconRegister.registerIcon("warpdrive:atomic/electromagnetic_cell-yellow-full");
	
	public static ItemStack getItemStackNoCache(final Particle particle, final int amount) {
		final ItemStack itemStack = new ItemStack(WarpDrive.itemElectromagneticCell, 1, 0);
		ParticleStack particleStack = null;
		if (particle != null && amount != 0) {
			particleStack = new ParticleStack(particle, amount);
			final NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setTag("particle", particleStack.writeToNBT(new NBTTagCompound()));
			itemStack.setTagCompound(tagCompound);
		}
		updateDamageLevel(itemStack, particleStack);
		return itemStack;
	}
	
	@Override
	public void getSubItems(@Nonnull final CreativeTabs creativeTab, @Nonnull final NonNullList<ItemStack> list) {
		if (!isInCreativeTab(creativeTab)) {
			return;
		}
		list.add(getItemStackNoCache(null, 0));
		list.add(getItemStackNoCache(ParticleRegistry.ION, 1000));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, 1000));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, 1000));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, 1000));
		// list.add(getItemStackNoCache(ParticleRegistry.TACHYONS, 1000));
	}
	
	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return true;
	}
	
	@Nonnull
	@Override
	public Item getContainerItem() {
		return Item.getItemFromBlock(Blocks.FIRE);
	}
	
	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull final ItemStack itemStackFilled) {
		final ParticleStack particleStack = getParticleStack(itemStackFilled);
		if (particleStack != null) {
			final int amount = particleStack.getAmount() - getAmountToConsume(itemStackFilled);
			if (amount <= 0) {
				return getItemStackNoCache(null, 0);
			}
			return getItemStackNoCache(particleStack.getParticle(), amount);
		}
		return new ItemStack(Blocks.FIRE);
	}
	
	@Override
	public void setAmountToConsume(final ItemStack itemStack, final int amountToConsume) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		if (particleStack == null || particleStack.getParticle() == null) {
			return;
		}
		final NBTTagCompound tagCompound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		tagCompound.setInteger(AMOUNT_TO_CONSUME_TAG, amountToConsume);
	}
	
	private int getAmountToConsume(final ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			return itemStack.getTagCompound().getInteger(AMOUNT_TO_CONSUME_TAG);
		}
		return 0;
	}
	
	private static int getDamageLevel(final ItemStack itemStack, final ParticleStack particleStack) {
		if (!(itemStack.getItem() instanceof ItemElectromagneticCell)) {
			WarpDrive.logger.error(String.format("Invalid ItemStack passed, expecting ItemElectromagneticCell: %s", itemStack));
			return itemStack.getItemDamage();
		}
		if (particleStack == null || particleStack.getParticle() == null) {
			return 0;
		}
		final ItemElectromagneticCell itemElectromagneticCell = (ItemElectromagneticCell) itemStack.getItem();
		final int type = particleStack.getParticle().getColorIndex() % 5;
		final double ratio = particleStack.getAmount() / (double) itemElectromagneticCell.getCapacity(itemStack);
		final int offset = (ratio < 0.2) ? 0 : (ratio < 0.4) ? 1 : (ratio < 0.6) ? 2 : (ratio < 0.8) ? 3 : (ratio < 1.0) ? 4 : 5;
		return (1 + type * 6 + offset);
	}
	
	private static void updateDamageLevel(final ItemStack itemStack, final ParticleStack particleStack) {
		itemStack.setItemDamage(getDamageLevel(itemStack, particleStack));
	}
	
	@Override
	public ParticleStack getParticleStack(final ItemStack itemStack) {
		if (itemStack.getItem() != this) {
			return null;
		}
		final NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			return null;
		}
		if (!tagCompound.hasKey("particle")) {
			return null;
		}
		return ParticleStack.loadFromNBT(tagCompound.getCompoundTag("particle"));
	}
	
	@Override
	public int getCapacity(final ItemStack container) {
		return 1000;
	}
	
	@Override
	public boolean isEmpty(final ItemStack itemStack) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		return particleStack == null || particleStack.isEmpty();
	}
	
	@Override
	public int fill(final ItemStack itemStack, final ParticleStack resource, final boolean doFill) {
		ParticleStack particleStack = getParticleStack(itemStack);
		if (particleStack == null || particleStack.getParticle() == null) {
			particleStack = new ParticleStack(resource.getParticle(), 0);
		} else if (!particleStack.isParticleEqual(resource) || particleStack.getAmount() >= getCapacity(itemStack)) {
			return 0;
		}
		final int transfer = Math.min(resource.getAmount(), getCapacity(itemStack) - particleStack.getAmount());
		if (doFill) {
			particleStack.fill(transfer);
			
			final NBTTagCompound tagCompound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
			tagCompound.setTag("particle", particleStack.writeToNBT(new NBTTagCompound()));
			if (!itemStack.hasTagCompound()) {
				itemStack.setTagCompound(tagCompound);
			}
			updateDamageLevel(itemStack, particleStack);
		}
		return transfer;
	}
	
	@Override
	public ParticleStack drain(final ItemStack itemStack, final ParticleStack resource, final boolean doDrain) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		if (particleStack == null || particleStack.getParticle() == null) {
			return null;
		}
		if (!particleStack.isParticleEqual(resource) || particleStack.getAmount() <= 0) {
			return null;
		}
		final int transfer = Math.min(resource.getAmount(), particleStack.getAmount());
		if (doDrain) {
			particleStack.fill(-transfer);
			
			final NBTTagCompound tagCompound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
			tagCompound.setTag("particle", particleStack.writeToNBT(new NBTTagCompound()));
			if (!itemStack.hasTagCompound()) {
				itemStack.setTagCompound(tagCompound);
			}
			updateDamageLevel(itemStack, particleStack);
		}
		return resource.copy(transfer);
	}
	
	@Override
	public int getEntityLifespan(final ItemStack itemStack, final World world) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		if ( particleStack == null
		  || particleStack.isEmpty() ) {
			return super.getEntityLifespan(itemStack, world);
		}
		final int lifespan = particleStack.getEntityLifespan();
		if (lifespan < 0) {
			return super.getEntityLifespan(itemStack, world);
		}
		// less content means more stable, so we scale lifespan with emptiness, up to doubling it
		return (2 - particleStack.getAmount() / getCapacity(itemStack)) * lifespan;
	}
	
	@Override
	public void onEntityExpireEvent(final EntityItem entityItem, final ItemStack itemStack) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		if ( particleStack == null
		  || particleStack.isEmpty() ) {
			super.onEntityExpireEvent(entityItem, itemStack);
			return;
		}
		particleStack.onWorldEffect(entityItem.world, new Vector3(entityItem));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull final ItemStack itemStack, @Nullable World world,
	                           @Nonnull final List<String> list, @Nullable final ITooltipFlag advancedItemTooltips) {
		super.addInformation(itemStack, world, list, advancedItemTooltips);
		
		if (!(itemStack.getItem() instanceof  ItemElectromagneticCell)) {
			WarpDrive.logger.error(String.format("Invalid ItemStack passed, expecting ItemElectromagneticCell: %s", itemStack));
			return;
		}
		final ItemElectromagneticCell itemElectromagneticCell = (ItemElectromagneticCell) itemStack.getItem();
		final ParticleStack particleStack = itemElectromagneticCell.getParticleStack(itemStack);
		final String tooltip;
		if (particleStack == null || particleStack.getParticle() == null) {
			tooltip = new TextComponentTranslation("item.warpdrive.atomic.electromagnetic_cell.tooltip.empty").getFormattedText();
			Commons.addTooltip(list, tooltip);
			
		} else {
			final Particle particle = particleStack.getParticle();
			
			tooltip = new TextComponentTranslation("item.warpdrive.atomic.electromagnetic_cell.tooltip.filled",
				particleStack.getAmount(), particle.getLocalizedName()).getFormattedText();
			Commons.addTooltip(list, tooltip);
			
			final String particleTooltip = particle.getLocalizedTooltip();
			if (!particleTooltip.isEmpty()) {
				Commons.addTooltip(list, particleTooltip);
			}
		}
	}
}
