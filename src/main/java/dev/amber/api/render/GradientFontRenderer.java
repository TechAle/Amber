package dev.amber.api.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;

public class GradientFontRenderer extends FontRenderer {

    private static final String charmap = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";

    public GradientFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
        this.readFontTexture();
    }

    private void readFontTexture()
    {
        IResource iresource = null;
        BufferedImage bufferedimage;

        try
        {
            iresource = getResource(this.locationFontTexture);
            bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }

        int lvt_3_2_ = bufferedimage.getWidth();
        int lvt_4_1_ = bufferedimage.getHeight();
        int[] lvt_5_1_ = new int[lvt_3_2_ * lvt_4_1_];
        bufferedimage.getRGB(0, 0, lvt_3_2_, lvt_4_1_, lvt_5_1_, 0, lvt_3_2_);
        int lvt_6_1_ = lvt_4_1_ / 16;
        int lvt_7_1_ = lvt_3_2_ / 16;
        boolean lvt_8_1_ = true;
        float lvt_9_1_ = 8.0F / (float)lvt_7_1_;

        for (int lvt_10_1_ = 0; lvt_10_1_ < 256; ++lvt_10_1_)
        {
            int j1 = lvt_10_1_ % 16;
            int k1 = lvt_10_1_ / 16;

            if (lvt_10_1_ == 32)
            {
                this.charWidth[lvt_10_1_] = 4;
            }

            int l1;

            for (l1 = lvt_7_1_ - 1; l1 >= 0; --l1)
            {
                int i2 = j1 * lvt_7_1_ + l1;
                boolean flag1 = true;

                for (int j2 = 0; j2 < lvt_6_1_ && flag1; ++j2)
                {
                    int k2 = (k1 * lvt_7_1_ + j2) * lvt_3_2_;

                    if ((lvt_5_1_[i2 + k2] >> 24 & 255) != 0)
                    {
                        flag1 = false;
                    }
                }

                if (!flag1)
                {
                    break;
                }
            }

            ++l1;
            this.charWidth[lvt_10_1_] = (int)(0.5D + (double)((float)l1 * lvt_9_1_)) + 1;
        }
    }


    public int drawGradientString(String text, float x, float y, int topColor, int bottomColor, boolean dropShadow, boolean horizontal) {
        enableAlpha();
        int i;

        if (dropShadow) {
            i = this.renderGradientString(text, x + 1.0F, y + 1.0F, topColor, bottomColor, true, horizontal);
            i = Math.max(i, this.renderGradientString(text, x, y, topColor, bottomColor, false, horizontal));
        } else {
            i = this.renderGradientString(text, x, y, topColor, bottomColor, false, horizontal);
        }

        return i;
    }

    private int renderGradientString(String text, float x, float y, int startColor, int endColor, boolean dropShadow, boolean horizontal) {
        if (text == null) {
            return 0;
        } else {

            if ((startColor & -67108864) == 0) {
                startColor |= -16777216;
            }

            if ((endColor & -67108864) == 0) {
                endColor |= -16777216;
            }

            if (dropShadow) {
                startColor = (startColor & 16579836) >> 2 | startColor & -16777216;
                endColor = (endColor & 16579836) >> 2 | endColor & -16777216;
            }

            this.posX = x;
            this.posY = y;
            this.renderGradientStringAtPos(text, dropShadow, startColor, endColor, horizontal);
            return (int) this.posX;
        }
    }

    private void renderGradientStringAtPos(String text, boolean shadow, int startColor, int endColor, boolean horizontal) {
        float totalWidth = this.getStringWidth(text);
        float currentCountWidth = 0;

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int j = charmap.indexOf(c0);

            float f1 = j == -1 ? 0.5f : 1f;
            boolean flag = (c0 == 0 || j == -1) && shadow;

            if (flag) {
                this.posX -= f1;
                this.posY -= f1;
            }

            float f;
            if (horizontal) {
                float nextCharWidth = this.getCharWidth(c0);
                float firstMix = currentCountWidth / totalWidth;
                float lastMix = (currentCountWidth + nextCharWidth) / totalWidth;
                int firstColor = colorMix(startColor, endColor, firstMix);
                int lastColor = colorMix(startColor, endColor, lastMix);
                f = this.renderGradientChar(c0, firstColor, lastColor, true);
                currentCountWidth += f;
            } else {
                f = this.renderGradientChar(c0, startColor, endColor, false);
            }

            if (flag) {
                this.posX += f1;
                this.posY += f1;
            }

            doDraw(f);
        }
    }

    private int colorMix(int startColor, int endColor, float mix) {
        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        int mixAlpha = (int) (((1 - mix) * startAlpha + mix * endAlpha) * 255);
        int mixRed = (int) (((1 - mix) * startRed + mix * endRed) * 255);
        int mixGreen = (int) (((1 - mix) * startGreen + mix * endGreen) * 255);
        int mixBlue = (int) (((1 - mix) * startBlue + mix * endBlue) * 255);

        return (mixAlpha << 24) | (mixRed << 16) | (mixGreen << 8) | mixBlue;
    }

    private float renderGradientChar(char ch, int startColor, int endColor, boolean horizontal) {
        if (ch == 160) return 4.0F; // forge: display nbsp as space. MC-2595
        if (ch == ' ') {
            return 4.0F;
        } else {
            int i = charmap.indexOf(ch);
            if (i != -1) {
                return this.renderGradientDefaultChar(i, startColor, endColor, horizontal);
            } else {
                throw new RuntimeException("Unrecognized char: " + ch);
            }
        }
    }

    protected float renderGradientDefaultChar(int ch, int startColor, int endColor, boolean horizontal) {
        float startAlpha = ((startColor >> 24) & 0xFF) / 255f;
        float startRed = ((startColor >> 16) & 0xFF) / 255f;
        float startGreen = ((startColor >> 8) & 0xFF) / 255f;
        float startBlue = (startColor & 0xFF) / 255f;

        float endAlpha = ((endColor >> 24) & 0xFF) / 255f;
        float endRed = ((endColor >> 16) & 0xFF) / 255f;
        float endGreen = ((endColor >> 8) & 0xFF) / 255f;
        float endBlue = (endColor & 0xFF) / 255f;

        float charXPos = ch % 16 * 8f;
        float charYPos = (ch / 16) * 8f;
        bindTexture(this.locationFontTexture);
        int charWidth = this.charWidth[ch];
        float width = (float) charWidth - 0.01F;

        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.glBegin(GL11.GL_QUADS);

        GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        GlStateManager.glTexCoord2f(charXPos / 128.0F, charYPos / 128.0F); // 0 0
        GlStateManager.glVertex3f(this.posX, this.posY, 0.0F);

        if (horizontal) {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        } else {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        }
        GlStateManager.glTexCoord2f(charXPos / 128.0F, (charYPos + 7.99F) / 128.0F); // 0 1
        GlStateManager.glVertex3f(this.posX, this.posY + 7.99F, 0.0F);

        GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        GlStateManager.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, (charYPos + 7.99F) / 128.0F); // 1 1
        GlStateManager.glVertex3f(this.posX + width - 1.0F, this.posY + 7.99F, 0.0F);

        if (horizontal) {
            GlStateManager.color(endRed, endGreen, endBlue, endAlpha);
        } else {
            GlStateManager.color(startRed, startGreen, startBlue, startAlpha);
        }
        GlStateManager.glTexCoord2f((charXPos + width - 1.0F) / 128.0F, charYPos / 128.0F); // 1 0
        GlStateManager.glVertex3f(this.posX + width - 1.0F, this.posY, 0.0F);

        GlStateManager.glEnd();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        return (float) charWidth;
    }
}