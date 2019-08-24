package cr0s.warpdrive.block.energy;

import cr0s.warpdrive.Commons;
import cr0s.warpdrive.block.TileEntityAbstractEnergy;
import cr0s.warpdrive.config.WarpDriveConfig;
import cr0s.warpdrive.data.EnumComponentType;
import cr0s.warpdrive.data.EnumDisabledInputOutput;
import cr0s.warpdrive.data.EnumTier;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityCapacitor extends TileEntityAbstractEnergy {
	
	// global properties
	private static final String TAG_MODE_SIDE = "modeSide";
	
	private static final EnumDisabledInputOutput[] MODE_DEFAULT_SIDES = {
			EnumDisabledInputOutput.INPUT,
			EnumDisabledInputOutput.INPUT,
			EnumDisabledInputOutput.OUTPUT,
			EnumDisabledInputOutput.OUTPUT,
			EnumDisabledInputOutput.OUTPUT,
			EnumDisabledInputOutput.OUTPUT };
	
	// persistent properties
	private EnumDisabledInputOutput[] modeSide = MODE_DEFAULT_SIDES.clone();
	
	public TileEntityCapacitor() {
		super();
		
		peripheralName = "warpdriveCapacitor";
		doRequireUpgradeToInterface();
		
		setUpgradeMaxCount(EnumComponentType.SUPERCONDUCTOR, WarpDriveConfig.CAPACITOR_EFFICIENCY_PER_UPGRADE.length - 1);
	}
	
	@Override
	protected void onConstructed() {
		super.onConstructed();
		
		energy_setParameters(WarpDriveConfig.CAPACITOR_MAX_ENERGY_STORED_BY_TIER[enumTier.getIndex()],
		                     WarpDriveConfig.CAPACITOR_FLUX_RATE_INPUT_BY_TIER[enumTier.getIndex()],
		                     WarpDriveConfig.CAPACITOR_FLUX_RATE_OUTPUT_BY_TIER[enumTier.getIndex()],
		                     WarpDriveConfig.CAPACITOR_IC2_SINK_TIER_NAME_BY_TIER[enumTier.getIndex()], 2,
		                     WarpDriveConfig.CAPACITOR_IC2_SOURCE_TIER_NAME_BY_TIER[enumTier.getIndex()], 2);
	}
	
	private double getEfficiency() {
		final int upgradeCount = getValidUpgradeCount(EnumComponentType.SUPERCONDUCTOR);
		return WarpDriveConfig.CAPACITOR_EFFICIENCY_PER_UPGRADE[upgradeCount];
	}
	
	@Override
	public long energy_getEnergyStored() {
		if (enumTier == EnumTier.CREATIVE) {
			return WarpDriveConfig.CAPACITOR_MAX_ENERGY_STORED_BY_TIER[0] / 2L;
		} else {
			return super.energy_getEnergyStored();
		}
	}
	
	@Override
	public int energy_getPotentialOutput() {
		return (int) Math.round(Math.min(energy_getEnergyStored() * getEfficiency(), WarpDriveConfig.CAPACITOR_FLUX_RATE_OUTPUT_BY_TIER[enumTier.getIndex()]));
	}
	
	@Override
	public boolean energy_consume(final long amount_internal, final boolean simulate) {
		if (enumTier == EnumTier.CREATIVE) {
			return true;
		}
		final long amountWithLoss = Math.round(amount_internal / getEfficiency());
		if (energy_getEnergyStored() >= amountWithLoss) {
			if (!simulate) {
				super.energy_consume(amountWithLoss);
			}
			return true;
		}
		return false;
	}
	@Override
	public void energy_consume(final long amount_internal) {
		if (enumTier == EnumTier.CREATIVE) {
			return;
		}
		final long amountWithLoss = Math.round(amount_internal > 0 ? amount_internal / getEfficiency() : amount_internal * getEfficiency());
		super.energy_consume(amountWithLoss);
	}
	
	@Override
	public boolean energy_canInput(final EnumFacing from) {
		if (from != null) {
			return modeSide[from.ordinal()] == EnumDisabledInputOutput.INPUT;
		} else {
			for (final EnumFacing enumFacing : EnumFacing.VALUES) {
				if (modeSide[enumFacing.ordinal()] == EnumDisabledInputOutput.INPUT) {
					return true;
				}
			}
			return false;
		}
	}
	
	@Override
	public boolean energy_canOutput(final EnumFacing to) {
		if (to != null) {
			return modeSide[to.ordinal()] == EnumDisabledInputOutput.OUTPUT;
		} else {
			for (final EnumFacing enumFacing : EnumFacing.VALUES) {
				if (modeSide[enumFacing.ordinal()] == EnumDisabledInputOutput.OUTPUT) {
					return true;
				}
			}
			return false;
		}
	}
	
	EnumDisabledInputOutput getMode(final EnumFacing facing) {
		return modeSide[facing.ordinal()];
	}
	
	void setMode(final EnumFacing facing, final EnumDisabledInputOutput enumDisabledInputOutput) {
		modeSide[facing.ordinal()] = enumDisabledInputOutput;
		markDirty();
		energy_refreshConnections(facing);
	}
	
	// Forge overrides
	@Nonnull
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeToNBT(tagCompound);
		
		final byte[] bytes = new byte[EnumFacing.values().length];
		for (final EnumFacing enumFacing : EnumFacing.values()) {
			bytes[enumFacing.ordinal()] = (byte) modeSide[enumFacing.ordinal()].getIndex();
		}
		tagCompound.setByteArray(TAG_MODE_SIDE, bytes);
		return tagCompound;
	}
	
	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		final byte[] bytes = tagCompound.getByteArray(TAG_MODE_SIDE);
		if (bytes.length != 6) {
			modeSide = MODE_DEFAULT_SIDES.clone();
		} else {
			for (final EnumFacing enumFacing : EnumFacing.values()) {
				modeSide[enumFacing.ordinal()] = EnumDisabledInputOutput.get(bytes[enumFacing.ordinal()]);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeItemDropNBT(NBTTagCompound tagCompound) {
		tagCompound = super.writeItemDropNBT(tagCompound);
		return tagCompound;
	}

	@Nonnull
	@Override
	public NBTTagCompound getUpdateTag() {
		final NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return tagCompound;
	}
	
	@Override
	public void onDataPacket(final NetworkManager networkManager, final SPacketUpdateTileEntity packet) {
		final NBTTagCompound tagCompound = packet.getNbtCompound();
		readFromNBT(tagCompound);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}
	
	@Override
	public String toString() {
		if (enumTier == null) {
			return String.format("%s %s",
			                     getClass().getSimpleName(),
			                     Commons.format(world, pos));
		} else {
			return String.format("%s %s %8d",
			                     getClass().getSimpleName(),
			                     Commons.format(world, pos),
			                     energy_getEnergyStored());
		}
	}
}