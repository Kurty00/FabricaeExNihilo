package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record StrainerConfig(int minWaitTime, int maxWaitTime) implements MutableStrainerConfig.Source {
    public static final StrainerConfig DEFAULT = new StrainerConfig(1200, 6000);
    public static final Codec<StrainerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "minWaitTime", StrainerConfig.DEFAULT.minWaitTime).forGetter(StrainerConfig::minWaitTime),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "maxWaitTime", StrainerConfig.DEFAULT.maxWaitTime).forGetter(StrainerConfig::maxWaitTime)
    ).apply(instance, StrainerConfig::new));
}
