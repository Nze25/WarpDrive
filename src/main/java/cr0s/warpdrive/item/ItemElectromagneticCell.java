package cr0s.warpdrive.item;

import cr0s.warpdrive.Commons;
import cr0s.warpdrive.WarpDrive;
import cr0s.warpdrive.api.IParticleContainerItem;
import cr0s.warpdrive.api.Particle;
import cr0s.warpdrive.api.ParticleRegistry;
import cr0s.warpdrive.api.ParticleStack;
import cr0s.warpdrive.api.WarpDriveText;
import cr0s.warpdrive.config.WarpDriveConfig;
import cr0s.warpdrive.data.EnumTier;
import cr0s.warpdrive.data.Vector3;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemElectromagneticCell extends ItemAbstractBase implements IParticleContainerItem {
	
	public ItemElectromagneticCell(final String registryName, final EnumTier enumTier) {
		super(registryName, enumTier);
		
		setMaxDamage(0);
		setMaxStackSize(1);
		setTranslationKey("warpdrive.atomic.electromagnetic_cell." + enumTier.getName());
		setHasSubtypes(true);
		
		addPropertyOverride(new ResourceLocation(WarpDrive.MODID, "fill"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(@Nonnull final ItemStack itemStack, @Nullable final World world, @Nullable final EntityLivingBase entity) {
				final ParticleStack particleStack = getParticleStack(itemStack);
				if (particleStack != null) {
					return (float) particleStack.getAmount() / getCapacity(itemStack);
				}
				return 0.0F;
			}
		});
	}
	
	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public ModelResourceLocation getModelResourceLocation(final ItemStack itemStack) {
		String variant = "empty";
		final ParticleStack particleStack = getParticleStack(itemStack);
		if (particleStack != null) {
			variant = particleStack.getTranslationKey().replace("warpdrive.particle.", "");
		}
		ResourceLocation resourceLocation = getRegistryName();
		assert resourceLocation != null;
		resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath() + "-" + variant);
		return new ModelResourceLocation(resourceLocation, "inventory");
	}
	
	@Nonnull
	public static ItemStack getItemStackNoCache(@Nonnull final EnumTier enumTier, @Nullable final Particle particle, final int amount) {
		return WarpDrive.itemElectromagneticCell[enumTier.getIndex()].getItemStackNoCache(particle, amount);
	}
	
	@Nonnull
	public ItemStack getItemStackNoCache(@Nullable final Particle particle, final int amount) {
		final ItemStack itemStack = new ItemStack(WarpDrive.itemElectromagneticCell[enumTier.getIndex()], 1, 0);
		ParticleStack particleStack = null;
		if (particle != null && amount != 0) {
			particleStack = new ParticleStack(particle, amount);
			final NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setTag(IParticleContainerItem.TAG_PARTICLE, particleStack.writeToNBT(new NBTTagCompound()));
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
		final int capacity10PC = WarpDriveConfig.ELECTROMAGNETIC_CELL_CAPACITY_BY_TIER[enumTier.getIndex()] / 10;
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC));
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC * 3));
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC * 5));
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC * 7));
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC * 9));
		list.add(getItemStackNoCache(ParticleRegistry.ION, capacity10PC * 10));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC * 3));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC * 5));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC * 7));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC * 9));
		list.add(getItemStackNoCache(ParticleRegistry.PROTON, capacity10PC * 10));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC * 3));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC * 5));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC * 7));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC * 9));
		list.add(getItemStackNoCache(ParticleRegistry.ANTIMATTER, capacity10PC * 10));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC * 3));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC * 5));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC * 7));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC * 9));
		list.add(getItemStackNoCache(ParticleRegistry.STRANGE_MATTER, capacity10PC * 10));
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
			final int amountToConsume = getAmountToConsume(itemStackFilled);
			if (amountToConsume > 0) {
				final int amountLeft = particleStack.getAmount() - amountToConsume;
				if (amountLeft <= 0) {
					return getItemStackNoCache(null, 0);
				}
				return getItemStackNoCache(particleStack.getParticle(), amountLeft);
			}
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setAmountToConsume(@Nonnull final ItemStack itemStack, final int amountToConsume) {
		final ParticleStack particleStack = getParticleStack(itemStack);
		if (particleStack == null || particleStack.getParticle() == null) {
			return;
		}
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (amountToConsume > 0) {
			if (tagCompound == null) {
				tagCompound = new NBTTagCompound();
			}
			tagCompound.setInteger(IParticleContainerItem.TAG_AMOUNT_TO_CONSUME, amountToConsume);
			tagCompound.setLong(IParticleContainerItem.TAG_TICK_TO_CONSUME, System.currentTimeMillis());
			
		} else if (tagCompound != null) {
			tagCompound.removeTag(IParticleContainerItem.TAG_AMOUNT_TO_CONSUME);
			tagCompound.removeTag(IParticleContainerItem.TAG_TICK_TO_CONSUME);
			if (tagCompound.isEmpty()) {
				itemStack.setTagCompound(null);
			}
		}
	}
	
	private int getAmountToConsume(@Nonnull final ItemStack itemStack) {
		final NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound != null) {
			// when taking a recipe output, the recipe is matched again before checking for recipients, so we can assume it's in the same tick
			final long tickToConsume = tagCompound.getInteger(IParticleContainerItem.TAG_TICK_TO_CONSUME);
			if (System.currentTimeMillis() - tickToConsume < 50L) {
				return tagCompound.getInteger(IParticleContainerItem.TAG_AMOUNT_TO_CONSUME);
			} else {
				tagCompound.removeTag(IParticleContainerItem.TAG_AMOUNT_TO_CONSUME);
				tagCompound.removeTag(IParticleContainerItem.TAG_TICK_TO_CONSUME);
			}
		}
		return 0;
	}
	
	private static int getDamageLevel(@Nonnull final ItemStack itemStack, final ParticleStack particleStack) {
		if (!(itemStack.getItem() instanceof ItemElectromagneticCell)) {
			WarpDrive.logger.error(String.format("Invalid ItemStack passed, expecting ItemElectromagneticCell: %s",
			                                     itemStack));
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
	
	private static void updateDamageLevel(@Nonnull final ItemStack itemStack, final ParticleStack particleStack) {
		itemStack.setItemDamage(getDamageLevel(itemStack, particleStack));
	}
	
	@Override
	public ParticleStack getParticleStack(@Nonnull final ItemStack itemStack) {
		if (itemStack.getItem() != this) {
			return null;
		}
		final NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound == null) {
			return null;
		}
		if (!tagCompound.hasKey(IParticleContainerItem.TAG_PARTICLE)) {
			return null;
		}
		return ParticleStack.loadFromNBT(tagCompound.getCompoundTag(IParticleContainerItem.TAG_PARTICLE));
	}
	
	@Override
	public int getCapacity(final ItemStack container) {
		return WarpDriveConfig.ELECTROMAGNETIC_CELL_CAPACITY_BY_TIER[enumTier.getIndex()];
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
			assert tagCompound != null;
			tagCompound.setTag(IParticleContainerItem.TAG_PARTICLE, particleStack.writeToNBT(new NBTTagCompound()));
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
			assert tagCompound != null;
			tagCompound.setTag(IParticleContainerItem.TAG_PARTICLE, particleStack.writeToNBT(new NBTTagCompound()));
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
	public void addInformation(@Nonnull final ItemStack itemStack, @Nullable final World world,
	                           @Nonnull final List<String> list, @Nullable final ITooltipFlag advancedItemTooltips) {
		super.addInformation(itemStack, world, list, advancedItemTooltips);
		
		if (!(itemStack.getItem() instanceof  ItemElectromagneticCell)) {
			WarpDrive.logger.error(String.format("Invalid ItemStack passed, expecting ItemElectromagneticCell: %s",
			                                     itemStack));
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
			                                       new WarpDriveText(Commons.styleValue, particleStack.getAmount()),
			                                       new WarpDriveText(Commons.styleValue, particle.getLocalizedName()) ).getFormattedText();
			Commons.addTooltip(list, tooltip);
			
			final String particleTooltip = particle.getLocalizedTooltip();
			if (!particleTooltip.isEmpty()) {
				Commons.addTooltip(list, particleTooltip);
			}
		}
	}
}
