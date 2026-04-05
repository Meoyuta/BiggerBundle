package mys.biggerbundle.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record BagIdComponent(UUID id) {
    public static final com.mojang.serialization.Codec<BagIdComponent> CODEC =
            UUIDUtil.STRING_CODEC.xmap(BagIdComponent::new, BagIdComponent::id);

    public static final StreamCodec<ByteBuf, BagIdComponent> STREAM_CODEC =
            UUIDUtil.STREAM_CODEC.map(BagIdComponent::new, BagIdComponent::id);
}