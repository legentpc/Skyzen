package at.legentpc.skyzen.mixin;

import at.legentpc.skyzen.events.EntityDisplayNameEvent;
import at.legentpc.skyzen.events.SkyzenEvents;
import at.legentpc.skyzen.features.misc.GiftCleanDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ArmorStandEntityMixin {

    @Inject(method = "getDisplayName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<Component> cir) {
        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof ArmorStand)) return;
        if (!GiftCleanDisplay.isEnabled()) return;

        EntityDisplayNameEvent event = new EntityDisplayNameEvent(
                entity,
                cir.getReturnValue()
        );
        SkyzenEvents.ENTITY_DISPLAY_NAME.invoker().onDisplayName(event);

        cir.setReturnValue(event.getDisplayName());
    }
}
