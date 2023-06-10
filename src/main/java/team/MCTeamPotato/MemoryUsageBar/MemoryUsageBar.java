package team.MCTeamPotato.MemoryUsageBar;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryUsageBar implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("memoryusagebar");

	@Override
	public void onInitialize() {
		LOGGER.info("MemoryUsageBar Mod Load!");
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				MemoryBarUsage(matrices);
			}
		});
	}

	private int getHealthBarColor(long usedMemory, long maxMemory) {
		float percentage = (float) usedMemory / (float) maxMemory;
		if (percentage < 0.2) {
			return 0xFF00FF00; // 绿色 0xFF00FF00
		} else if (percentage < 0.5) {
			return 0xFFFF8000; // 橙色 0xFFFF8000
		} else {
			return 0xFFFF0000; // 红色 0xFFFF0000
		}
	}

	public void MemoryBarUsage(MatrixStack matrices) {


		Runtime runtime = Runtime.getRuntime();
		long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		int memoryPercent = (int) (usedMemory * 100 / (double) runtime.maxMemory());



		//内存使用量-变量
		int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
		int barWidth = 90;
		int barHeight = 5;
		int barX = 10;
		int barY = screenHeight - 10;
		//DrawableHelper.fill(matrices, barX - 1, barY - 1, barX + barWidth + 1, barY + barHeight + 1, 0x80000000);
		//DrawableHelper.fill(matrices, barX, barY, barX + barWidth, barY + barHeight, 0x80C0C0C0);
		DrawableHelper.fill(matrices, barX + 1, barY - 1, barX + 100, barY + barHeight + 1, 0x80000000);
		//计算并绘制内存值条和百分比
		int MemoryBarWidth = MathHelper.ceil((float) usedMemory / (float) runtime.maxMemory() * (barWidth - 2));
		int MemoryBarColor = getHealthBarColor((int) usedMemory, memoryPercent);
		String memoryUsagePercentage = String.format("%.0f%%", (double)usedMemory / (double)runtime.maxMemory() * 100.0);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int percentageX = barX + MemoryBarWidth - textRenderer.getWidth(memoryUsagePercentage) / 2;
		int percentageY = barY - textRenderer.fontHeight - 1;
		//背景
		DrawableHelper.fill(matrices, barX - 1, barY - 1, barX + MemoryBarWidth + 1, barY + barHeight + 1, 0x80000000);
		DrawableHelper.fill(matrices, barX, barY, barX + MemoryBarWidth, barY + barHeight, MemoryBarColor);
        //内存率条
		DrawableHelper.fill(matrices, barX + 1, barY + 1, barX + MemoryBarWidth + 1, barY + barHeight - 1, MemoryBarColor);

		textRenderer.drawWithShadow(matrices, memoryUsagePercentage, percentageX, percentageY, 0xFFFFFFFF);


	}
}
