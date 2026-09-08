package at.legentpc.skyzen.mixin;

import at.legentpc.skyzen.events.SkyzenEvents;
import at.legentpc.skyzen.events.EntityDisplayNameEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ArmorStandEntityMixin {

    @Inject(method = "shouldShowName()Z", at = @At("RETURN"), cancellable = true)
    private void onShouldShowName(CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof ArmorStand armorStand)) return;
        if (!cir.getReturnValue()) return;

        Component name = armorStand.getDisplayName();
        if (name.getString().isEmpty()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getDisplayName()Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<Component> cir) {
        if (!((Object) this instanceof ArmorStand)) return;
        Component original = cir.getReturnValue();

        EntityDisplayNameEvent event = new EntityDisplayNameEvent(
                (Entity) (Object) this,
                original
        );
        SkyzenEvents.ENTITY_DISPLAY_NAME.invoker().onDisplayName(event);

        cir.setReturnValue(event.getDisplayName());
    }
}
