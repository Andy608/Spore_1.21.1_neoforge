package com.Harbinger.Spore.Client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class TintedBufferSource implements MultiBufferSource {
    private final MultiBufferSource original;
    private final float r, g, b, a;

    public TintedBufferSource(MultiBufferSource original, float r, float g, float b, float a) {
        this.original = original;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return new TintedVertexConsumer(original.getBuffer(renderType), r, g, b, a);
    }
    public static class TintedVertexConsumer implements VertexConsumer {
        private final VertexConsumer base;
        private final float r, g, b, a;

        public TintedVertexConsumer(VertexConsumer base, float r, float g, float b, float a) {
            this.base = base;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        private static int clampColor(int v) {
            if (v < 0) return 0;
            return Math.min(v, 255);
        }

        private int applyTintToComponent(int component, float multiplier) {
            int val = (int) (component * multiplier);
            return clampColor(val);
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            base.addVertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer setColor(int red, int green, int blue, int alpha) {
            int nr = applyTintToComponent(red, r);
            int ng = applyTintToComponent(green, g);
            int nb = applyTintToComponent(blue, b);
            int na = applyTintToComponent(alpha, a);
            base.setColor(nr, ng, nb, na);
            return this;
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            base.setUv(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            base.setUv1(u, v);
            return this;
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            base.setUv2(u, v);
            return this;
        }

        @Override
        public VertexConsumer setNormal(float x, float y, float z) {
            base.setNormal(x, y, z);
            return this;
        }
    }
}
